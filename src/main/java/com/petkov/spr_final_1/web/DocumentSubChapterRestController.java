package com.petkov.spr_final_1.web;


import com.petkov.spr_final_1.model.view.DocumentSubchapterViewModel;
import com.petkov.spr_final_1.model.view.DocumentViewModel;
import com.petkov.spr_final_1.service.DocumentSubChapterService;
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
@RequestMapping("/document-subchapters/api")
public class DocumentSubChapterRestController {

    private final DocumentSubChapterService documentSubChapterService;
    private final ModelMapper modelMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(ATAChapterRestController.class);

    public DocumentSubChapterRestController(DocumentSubChapterService documentSubChapterService, ModelMapper modelMapper) {
        this.documentSubChapterService = documentSubChapterService;
        this.modelMapper = modelMapper;
    }


    //todo - add Aspect and/or EventListener to log at different place
    @GetMapping(value = "", produces = "application/json")
    public DeferredResult<ResponseEntity<List<DocumentSubchapterViewModel>>> getAllDocumentSubchaptersSortedByATA() {

        LOGGER.info("Received async-deferred request at </document-subchapters/api>");

        DeferredResult<ResponseEntity<List<DocumentSubchapterViewModel>>> deferredResult = new DeferredResult<>();

        documentSubChapterService
                .getAllSortedByNameDescAsync()
                .thenApply(documentSubchapterViewModels ->
                        deferredResult.setResult(ResponseEntity
                                .ok()
                                .body(documentSubchapterViewModels))
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
