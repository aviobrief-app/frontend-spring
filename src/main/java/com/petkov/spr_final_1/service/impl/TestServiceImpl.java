package com.petkov.spr_final_1.service.impl;

import com.petkov.spr_final_1.model.app_entity.UserEntity;
import com.petkov.spr_final_1.model.enumeration.TestStatusEnum;
import com.petkov.spr_final_1.model.app_entity.ActiveTestEntity;
import com.petkov.spr_final_1.model.app_entity.QuestionEntity;
import com.petkov.spr_final_1.model.app_entity.SubmittedQuestionEntity;
import com.petkov.spr_final_1.model.app_entity.TestEntity;
import com.petkov.spr_final_1.model.service.test.TestServiceModel;
import com.petkov.spr_final_1.model.view.ActiveQuestionViewModel;
import com.petkov.spr_final_1.model.view.ArticleViewModel;
import com.petkov.spr_final_1.model.view.TestThumbnailViewModel;
import com.petkov.spr_final_1.repository.TestRepository;
import com.petkov.spr_final_1.service.ArticleService;
import com.petkov.spr_final_1.service.QuestionService;
import com.petkov.spr_final_1.service.TestService;
import com.petkov.spr_final_1.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final ModelMapper modelMapper;
    private final ArticleService articleService;
    private final QuestionService questionService;
    private final UserService userService;

    public TestServiceImpl(TestRepository testRepository,
                           ModelMapper modelMapper,
                           ArticleService articleService,
                           QuestionService questionService, UserService userService) {
        this.testRepository = testRepository;
        this.modelMapper = modelMapper;
        this.articleService = articleService;
        this.questionService = questionService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public ActiveTestEntity buildActiveTest(Long id) {

        TestEntity testEntity =
                testRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(String.format("Test with id '%s' not found", id)));

        ActiveTestEntity activeTestEntity
                = modelMapper.map(testEntity, ActiveTestEntity.class);


        setActiveQuestionViewModels(testEntity, activeTestEntity);

        setSubmittedQuestionServiceModels(testEntity, activeTestEntity);

        return activeTestEntity;
    }

    private void setSubmittedQuestionServiceModels(TestEntity testEntity, ActiveTestEntity activeTestEntity) {
        List<SubmittedQuestionEntity> submittedQuestionEntities =
                testEntity.getQuestions()
                        .stream()
                        .map(questionEntity -> {

                            SubmittedQuestionEntity submittedQuestionEntity =
                                    modelMapper.map(questionEntity, SubmittedQuestionEntity.class);

                            return submittedQuestionEntity;

                        }).collect(Collectors.toList());

        activeTestEntity.setSubmittedQuestionEntities(submittedQuestionEntities);
    }

    private void setActiveQuestionViewModels(TestEntity testEntity, ActiveTestEntity activeTestEntity) {
        List<ActiveQuestionViewModel> activeQuestionViewModels =
                testEntity.getQuestions()
                        .stream()
                        .map(questionEntity -> {

                            ActiveQuestionViewModel activeQuestionViewModel =
                                    modelMapper.map(questionEntity, ActiveQuestionViewModel.class);

                            //set article to question if article exists
                            setArticleToQuestionIfNotNull(questionEntity, activeQuestionViewModel);

                            //set answers
                            //TODO - randomise setting answers,
                            //                 so they appear different on page every time
                            setAnswersToQuestionAsList(questionEntity, activeQuestionViewModel);

                            return activeQuestionViewModel;
                        })
                        .collect(Collectors.toList());

        activeTestEntity.setQuestionEntities(activeQuestionViewModels);
    }

    private void setAnswersToQuestionAsList(QuestionEntity questionEntity, ActiveQuestionViewModel activeQuestionViewModel) {
        List<String> answers = List.of(
                questionEntity.getCorrectAnswer(),
                questionEntity.getAltAnswer1(),
                questionEntity.getAltAnswer2(),
                questionEntity.getAltAnswer3(),
                questionEntity.getAltAnswer4()
        );

        activeQuestionViewModel.setAnswers(answers);
    }

    private void setArticleToQuestionIfNotNull(QuestionEntity questionEntity, ActiveQuestionViewModel activeQuestionViewModel) {
        try {
            ArticleViewModel articleViewModel =
                    modelMapper.map(questionEntity.getArticle(), ArticleViewModel.class);

            activeQuestionViewModel.setArticleViewModel(articleViewModel);
        } catch (IllegalArgumentException exception) {
            //todo handle exception elsewhere and advise in Log?
        }
    }

    @Override
    @Transactional
    public List<TestThumbnailViewModel> getAllUpcomingTestsView() {

        return testRepository.findAll()
                .stream()
                .map(testEntity -> {
                    TestThumbnailViewModel testThumbnailViewModel =
                            modelMapper.map(testEntity, TestThumbnailViewModel.class);

                    testThumbnailViewModel.setNumberOfQuestions(testEntity.getQuestions().size());


                    return testThumbnailViewModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TestServiceModel seedTestToDb(TestServiceModel testServiceModel) {

        TestEntity testEntity =
                modelMapper.map(testServiceModel, TestEntity.class);

        //user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        testEntity.setCreatedBy(modelMapper
                .map(userService.findByUsername(username), UserEntity.class));

        //questions
        List<QuestionEntity> questionEntities =
                testServiceModel.getQuestionIds()
                        .stream()
                        .map(questionService::findById)
                        .map(serviceModel -> modelMapper.map(serviceModel, QuestionEntity.class))
                        .collect(Collectors.toList());

        testEntity.setQuestions(questionEntities);

        //status
        testEntity.setTestStatus(TestStatusEnum.ACTIVE);

        //date Created
        testEntity.setDateCreated(LocalDate.now());

        //todo addTest() debugPoint
        System.out.println();
        TestEntity seedResult = testRepository.saveAndFlush(testEntity);

        return modelMapper.map(seedResult, TestServiceModel.class);
    }
}
