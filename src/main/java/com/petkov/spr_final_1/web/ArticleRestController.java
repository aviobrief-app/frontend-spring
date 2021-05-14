package com.petkov.spr_final_1.web;

import com.petkov.spr_final_1.model.view.ArticleViewModel;
import com.petkov.spr_final_1.service.ArticleService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@RestController
@RequestMapping("articles/api")
public class ArticleRestController {

    private final ArticleService articleService;
    private final ModelMapper modelMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(ATAChapterRestController.class);

    public ArticleRestController(ArticleService articleService, ModelMapper modelMapper) {
        this.articleService = articleService;
        this.modelMapper = modelMapper;
    }


    @GetMapping(value = "", produces = "application/json")
    //todo - change ArticleViewModel to ArticleThumbnailViewModel
    //todo - add Aspect and/or EventListener to log at different place

    public DeferredResult<ResponseEntity<List<ArticleViewModel>>> getAllDocumentsSortedByName() {

        LOGGER.info("Received async-deferred request at </articles/api>");

        DeferredResult<ResponseEntity<List<ArticleViewModel>>> deferredResult = new DeferredResult<>();

        articleService
                .findAllSortedByNameDescAsync()
                .thenApply(articleViewModels ->
                        deferredResult.setResult(ResponseEntity
                                .ok()
                                .body(articleViewModels))
                )
                .exceptionally(ex ->
                        //todo -  handle ex with Aspect
                        deferredResult.setResult(ResponseEntity
                                .notFound()
                                .build())
                );

        return deferredResult;
    }

}
