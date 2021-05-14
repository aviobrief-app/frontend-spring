package com.petkov.spr_final_1.web;


import com.petkov.spr_final_1.model.rest.ATAChapterEditErrorsViewModel;
import com.petkov.spr_final_1.model.binding.document.ATAChapterAddBindingModel;
import com.petkov.spr_final_1.model.service.document.ATAChapterServiceModel;
import com.petkov.spr_final_1.model.view.ATAChapterViewModel;
import com.petkov.spr_final_1.service.ATAChapterService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/ata-chapters/api")
@RestController
public class ATAChapterRestController {

    private final ATAChapterService chapterService;
    private final ModelMapper modelMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(ATAChapterRestController.class);


    public ATAChapterRestController(ATAChapterService chapterService, ModelMapper modelMapper) {
        this.chapterService = chapterService;
        this.modelMapper = modelMapper;
    }

    //todo - add Aspect and/or EventListener to log at different place
    @GetMapping(value = "", produces = "application/json")
    public DeferredResult<ResponseEntity<List<ATAChapterViewModel>>> getAllChaptersSortedByATA() {

        LOGGER.info("Received async-deferred request at </ata-chapters>");

        DeferredResult<ResponseEntity<List<ATAChapterViewModel>>> deferredResult = new DeferredResult<>();


        chapterService
                .findAllATAChaptersSortedByAtaDescAsync()
                .thenApply(ataChapterViewModels ->
                        deferredResult.setResult(ResponseEntity
                                .ok()
                                .body(ataChapterViewModels))
                )
                .exceptionally(ex ->
                        //todo - getAllChaptersSortedByATA handle ex with Aspect
                        deferredResult.setResult(ResponseEntity
                                .notFound()
                                .build())
                );

        return deferredResult;
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<? extends ATAChapterViewModel> editById(@PathVariable Long id,
                                                        @Valid @RequestBody ATAChapterAddBindingModel ataChapterAddBindingModel,
                                                        BindingResult bindingResult){



        if(bindingResult.hasErrors()){

            ATAChapterEditErrorsViewModel ataChapterEditErrors =
                    modelMapper.map(ataChapterAddBindingModel, ATAChapterEditErrorsViewModel.class);

            bindingResult.getAllErrors()
                    .forEach(objectError ->
                            ataChapterEditErrors.getBindingErrors().add(objectError.getDefaultMessage()));

            return ResponseEntity
                    .unprocessableEntity()
                    .body(ataChapterEditErrors);

        } else {

            ATAChapterServiceModel ataChapterServiceModel =
                    chapterService.findChapterById(id);

            ataChapterServiceModel.setName(ataChapterAddBindingModel.getName());

            return ResponseEntity
                    .ok()
                    .body(modelMapper
                            .map(chapterService.addChapterToDB(ataChapterServiceModel), ATAChapterViewModel.class));
        }

    }
}
