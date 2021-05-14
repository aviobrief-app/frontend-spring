package com.petkov.spr_final_1.web;


import com.petkov.spr_final_1.model.view.ATASubChapterViewModel;
import com.petkov.spr_final_1.model.view.DocumentSubchapterViewModel;
import com.petkov.spr_final_1.service.ATASubChapterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@RestController
@RequestMapping("/ata-subchapters/api")
public class ATASubChapterRestController {

    public ATASubChapterRestController(ATASubChapterService ataSubChapterService) {
        this.ataSubChapterService = ataSubChapterService;
    }

    private final ATASubChapterService ataSubChapterService;
    private final Logger LOGGER = LoggerFactory.getLogger(ATAChapterRestController.class);


    //todo - add Aspect and/or EventListener to log at different place
    @GetMapping(value = "", produces = "application/json")
    public DeferredResult<ResponseEntity<List<ATASubChapterViewModel>>> getAllATASubchaptersSortedByATA() {

        LOGGER.info("Received async-deferred request at </ata-subchapters/api");

        DeferredResult<ResponseEntity<List<ATASubChapterViewModel>>> deferredResult = new DeferredResult<>();

        ataSubChapterService
                .getAllSortedByATAAsync()
                .thenApply(ataSubChapterViewModels ->
                        deferredResult.setResult(ResponseEntity
                                .ok()
                                .body(ataSubChapterViewModels))
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
