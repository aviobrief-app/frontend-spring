package com.petkov.spr_final_1.web;

import com.petkov.spr_final_1.model.view.QuestionViewModel;
import com.petkov.spr_final_1.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@RequestMapping("questions/api")
@RestController
public class QuestionsRestController {

    private final QuestionService questionService;
    private final Logger LOGGER = LoggerFactory.getLogger(ATAChapterRestController.class);

    public QuestionsRestController(QuestionService questionService) {
        this.questionService = questionService;
    }

    //todo - add Aspect and/or EventListener to log at different place
    //todo - change QuestionViewModel to QuestionThumbnailViewModel

    @GetMapping(value = "", produces = "application/json")
    public DeferredResult<ResponseEntity<List<QuestionViewModel>>> getAllQuestionsSortedByATA() {

        LOGGER.info("Received async-deferred request at <questions/api>");

        DeferredResult<ResponseEntity<List<QuestionViewModel>>> deferredResult = new DeferredResult<>();

        questionService
                .getAllQuestionsSortedByATA()
                .thenApply(questionViewModels ->
                        deferredResult.setResult(ResponseEntity
                                .ok()
                                .body(questionViewModels))
                )
                .exceptionally(ex ->
                        //todo - getAllChaptersSortedByATA handle ex with Aspect
                        deferredResult.setResult(ResponseEntity
                                .notFound()
                                .build())
                );

        return deferredResult;
    }


}
