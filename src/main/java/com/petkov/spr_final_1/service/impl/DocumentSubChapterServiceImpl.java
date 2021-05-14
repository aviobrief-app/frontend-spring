package com.petkov.spr_final_1.service.impl;

import com.google.gson.Gson;
import com.petkov.spr_final_1.model.aviation_library_entity.DocumentEntity;
import com.petkov.spr_final_1.model.aviation_library_entity.DocumentSubchapterEntity;
import com.petkov.spr_final_1.model.service.document.DocumentSubchapterServiceModel;
import com.petkov.spr_final_1.model.view.DocumentSubchapterViewModel;
import com.petkov.spr_final_1.repository.DocumentSubchapterRepository;
import com.petkov.spr_final_1.service.DocumentService;
import com.petkov.spr_final_1.service.DocumentSubChapterService;
import com.petkov.spr_final_1.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DocumentSubChapterServiceImpl implements DocumentSubChapterService {

    private final DocumentSubchapterRepository documentSubchapterRepository;
    private final DocumentService documentService;
    private final ModelMapper modelMapper;
    private final Resource documentInitFile;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    public DocumentSubChapterServiceImpl(DocumentSubchapterRepository documentSubchapterRepository,
                                         @Lazy DocumentService documentService,
                                         ModelMapper modelMapper,
                                         @Value("classpath:init/document-subchapters-init.json") Resource documentInitFile,
                                         Gson gson,
                                         ValidationUtil validationUtil) {
        this.documentSubchapterRepository = documentSubchapterRepository;
        this.documentService = documentService;
        this.modelMapper = modelMapper;
        this.documentInitFile = documentInitFile;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    @Transactional
    public void initSeedDocumentSubchaptersFromJson() {

        if (documentSubchapterRepository.count() == 0) {

            try {
                DocumentSubchapterServiceModel[] documentSubchapterServiceModels =
                        gson.fromJson(Files.readString(Path.of(documentInitFile.getURI())),
                                DocumentSubchapterServiceModel[].class);

                Arrays
                        .stream(documentSubchapterServiceModels)
                        .forEach(this::seedIfValidOrPrintError);

                // TODO: 4/11/2021 -  initSeedDocumentsFromJson - add successfull seed message

            } catch (IOException e) {
                throw new IllegalStateException("IO error from file 'init/document-subchapters-init.json'!");
            }
        }
    }

    private void seedIfValidOrPrintError(DocumentSubchapterServiceModel documentSubchapterServiceModel) {

        if (this.validationUtil.isValid(documentSubchapterServiceModel)) {

            DocumentEntity documentEntity =
                    modelMapper.map(documentService
                            .findDocumentByName(documentSubchapterServiceModel.getDocument()), DocumentEntity.class);

            DocumentSubchapterEntity documentSubchapterEntity =
                    modelMapper.map(documentSubchapterServiceModel, DocumentSubchapterEntity.class);


            boolean subChapterExists =
                    subChapterExistsInDocument(documentEntity.getName(),
                            documentSubchapterServiceModel.getName());

            if (!subChapterExists) {
                documentSubchapterEntity.setDocument(documentEntity);
                documentSubchapterRepository.saveAndFlush(documentSubchapterEntity);
            }


        } else {
            //todo seedChaptersIfValidOrPrintError - Log errors io printing
            System.out.println(String.format("Document init seed errors from file 'init/document-subchapters-init.json':%n "));

            validationUtil.getViolations(documentSubchapterServiceModel)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            System.out.printf("For document '%s - %s'%n",
                    documentSubchapterServiceModel.getDocument(),
                    documentSubchapterServiceModel.getName());
        }
    }


    @Override
    public boolean subChapterExistsInDocument(String documentName, String docSubchapterName) {

        DocumentEntity documentEntity =
                modelMapper.map(documentService.findDocumentByName(documentName), DocumentEntity.class);

        DocumentSubchapterEntity documentSubchapterEntity =
                documentSubchapterRepository
                        .findByDocumentAndName(documentEntity, docSubchapterName)
                        .orElse(null);

        return documentSubchapterEntity != null;
    }

    @Override
    @Transactional
    public void seedDocumentSubchapterToDb(DocumentSubchapterServiceModel serviceModel) {

        DocumentSubchapterEntity documentSubchapterEntity =
                modelMapper.map(serviceModel, DocumentSubchapterEntity.class);

        DocumentEntity documentEntity
                = modelMapper.map(documentService.findDocumentByName(serviceModel.getDocument()), DocumentEntity.class);

        documentSubchapterEntity.setDocument(documentEntity);

        documentSubchapterRepository.saveAndFlush(documentSubchapterEntity);

    }

    @Override
    public List<DocumentSubchapterViewModel> getAllSortedByNameDesc() {
        return documentSubchapterRepository
                .findAll(Sort.by(Sort.Direction.DESC, "name"))
                .stream()
                .map(documentSubchapterEntity -> {
                    DocumentSubchapterViewModel documentSubchapterViewModel =
                            modelMapper.map(documentSubchapterEntity, DocumentSubchapterViewModel.class);

                    documentSubchapterViewModel.setDocumentRef(documentSubchapterEntity.getDocument().getName());

                    return documentSubchapterViewModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Async
    public CompletableFuture<List<DocumentSubchapterViewModel>> getAllSortedByNameDescAsync() {
        return CompletableFuture
                .supplyAsync(this::getAllSortedByNameDesc)
                .orTimeout(30, TimeUnit.SECONDS);

    }

    @Override
    public DocumentSubchapterServiceModel findByDocumentAndDocumentSubchapter(String documentName, String subchapterName) {

        DocumentEntity documentEntity =
                modelMapper.map(documentService.findDocumentByName(documentName), DocumentEntity.class);

        DocumentSubchapterEntity documentSubchapterEntity = documentSubchapterRepository
                .findByDocumentAndName(documentEntity, subchapterName)
                .orElseThrow(() -> new IllegalArgumentException("Document Subchapter not found in DB."));

        return modelMapper.map(documentSubchapterEntity, DocumentSubchapterServiceModel.class);
    }
}
