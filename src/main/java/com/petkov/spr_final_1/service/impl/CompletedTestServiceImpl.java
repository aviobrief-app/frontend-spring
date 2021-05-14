package com.petkov.spr_final_1.service.impl;

import com.petkov.spr_final_1.model.binding.test.SubmitTestBindingModel;
import com.petkov.spr_final_1.model.app_entity.UserEntity;
import com.petkov.spr_final_1.model.app_entity.ActiveTestEntity;
import com.petkov.spr_final_1.model.app_entity.CompletedTestEntity;
import com.petkov.spr_final_1.model.app_entity.SubmittedQuestionEntity;
import com.petkov.spr_final_1.model.service.test.CompletedTestServiceModel;
import com.petkov.spr_final_1.repository.CompletedTestRepository;
import com.petkov.spr_final_1.service.CompletedTestService;
import com.petkov.spr_final_1.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CompletedTestServiceImpl implements CompletedTestService {

    private final CompletedTestRepository completedTestRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public CompletedTestServiceImpl(CompletedTestRepository completedTestRepository, ModelMapper modelMapper, UserService userService) {
        this.completedTestRepository = completedTestRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }


    @Override
    public CompletedTestServiceModel scoreAndArchiveTest(ActiveTestEntity activeTestEntity, SubmitTestBindingModel submitTestBindingModel) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        CompletedTestEntity completedTestEntity = new CompletedTestEntity();

        completedTestEntity.setName(activeTestEntity.getName());
        completedTestEntity.setParentTestId(activeTestEntity.getId());
        completedTestEntity.setTimeCompleted(LocalDateTime.now());
        completedTestEntity.setDueDate(activeTestEntity.getDueDate());
        completedTestEntity
                .setUserEntity(modelMapper
                        .map(userService.findByUsername(username), UserEntity.class));

        Map<String, String> answersMatrix = getAnswerMatrix(submitTestBindingModel);

        List<SubmittedQuestionEntity> submittedQuestionEntities =
                activeTestEntity
                        .getSubmittedQuestionEntities()
                        .stream()
                        .peek(submittedQuestionEntity -> {

                            submittedQuestionEntity.setId(null);

                            submittedQuestionEntity.setCompletedTestEntity(completedTestEntity);

                            String givenAnswer = answersMatrix.get(submittedQuestionEntity.getQuestion());
                            submittedQuestionEntity.setSubmittedAnswer(givenAnswer);

                            String correctAnswer = submittedQuestionEntity.getCorrectAnswer();
                            submittedQuestionEntity.setAnsweredCorrectly(givenAnswer.equals(correctAnswer));


                        }).collect(Collectors.toList());


        completedTestEntity.setQuestionEntities(submittedQuestionEntities);

        return modelMapper
                .map(completedTestRepository.saveAndFlush(completedTestEntity),
                        CompletedTestServiceModel.class);

    }

    private Map<String, String> getAnswerMatrix(SubmitTestBindingModel submitTestBindingModel) {
        Map<String, String> answersMatrix = new HashMap<>();

        submitTestBindingModel.getAskedQuestionMatrix()
                .forEach((index, askedQuestion) -> {

                    String givenAnswer =
                            submitTestBindingModel.getGivenAnswerMatrix().get(index);

                    answersMatrix.put(askedQuestion, givenAnswer);
                });

        return answersMatrix;
    }



}
