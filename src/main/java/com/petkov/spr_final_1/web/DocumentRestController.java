package com.petkov.spr_final_1.web;


import com.petkov.spr_final_1.model.binding.document.DocumentAddBindingModel;
import com.petkov.spr_final_1.model.service.document.DocumentServiceModel;
import com.petkov.spr_final_1.model.view.DocumentViewModel;
import com.petkov.spr_final_1.service.DocumentService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/documents/api")
@RestController
public class DocumentRestController {

    private final DocumentService documentService;
    private final ModelMapper modelMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(ATAChapterRestController.class);

    public DocumentRestController(DocumentService documentService, ModelMapper modelMapper) {
        this.documentService = documentService;
        this.modelMapper = modelMapper;
    }

    //todo - add Aspect and/or EventListener to log at different place
    @GetMapping(value = "", produces = "application/json")
    public DeferredResult<ResponseEntity<List<DocumentViewModel>>> getAllDocumentsSortedByName() {

        LOGGER.info("Received async-deferred request at </documents/api>");

        DeferredResult<ResponseEntity<List<DocumentViewModel>>> deferredResult = new DeferredResult<>();

        documentService
                .findAllDocumentsSortedByNameDescAsync()
                .thenApply(documentViewModels ->
                        deferredResult.setResult(ResponseEntity
                                .ok()
                                .body(documentViewModels))
                )
                .exceptionally(ex ->
                        //todo -  handle ex with Aspect
                        deferredResult.setResult(ResponseEntity
                                .notFound()
                                .build())
                );

        return deferredResult;
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<DocumentViewModel> editById(@PathVariable Long id,
                                                      @Valid @RequestBody DocumentAddBindingModel documentAddBindingModel) {

        DocumentServiceModel documentServiceModel = documentService.findDocumentById(id);

        return ResponseEntity
                .ok()
                .body(modelMapper
                        .map(documentService.renameDocument(documentServiceModel, documentAddBindingModel.getName()), DocumentViewModel.class));
    }


}
