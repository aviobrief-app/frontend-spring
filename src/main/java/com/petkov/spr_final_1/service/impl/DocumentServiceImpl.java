package com.petkov.spr_final_1.service.impl;

import com.google.gson.Gson;
import com.petkov.spr_final_1.model.aviation_library_entity.DocumentEntity;
import com.petkov.spr_final_1.model.service.document.DocumentServiceModel;
import com.petkov.spr_final_1.model.view.DocumentViewModel;
import com.petkov.spr_final_1.repository.DocumentRepository;
import com.petkov.spr_final_1.service.DocumentService;
import com.petkov.spr_final_1.service.DocumentSubChapterService;
import com.petkov.spr_final_1.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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

import static com.petkov.spr_final_1.constants.GlobalMessages.*;

@Service
public class DocumentServiceImpl implements DocumentService {

    private static final String DEFAULT_DOCUMENT_SUBCHAPTER_NAME = "other";

    private final ModelMapper modelMapper;
    private final DocumentRepository documentRepository;
    private final Resource documentInitFile;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final DocumentSubChapterService documentSubChapterService;

    //todo - service - extract constants


    public DocumentServiceImpl(ModelMapper modelMapper,
                               DocumentRepository documentRepository,
                               @Value(DOCUMENTS_INIT_FILE_PATH) Resource documentInitFile,
                               Gson gson,
                               ValidationUtil validationUtil, DocumentSubChapterService documentSubChapterService) {
        this.modelMapper = modelMapper;
        this.documentRepository = documentRepository;
        this.documentInitFile = documentInitFile;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.documentSubChapterService = documentSubChapterService;
    }

    @Override
    public void initSeedDocumentsFromJson() {
        if (documentRepository.count() == 0) {
            try {
                DocumentServiceModel[] documentServiceModels =
                        gson.fromJson(Files.readString(Path.of(documentInitFile.getURI())),
                                DocumentServiceModel[].class);

                Arrays.stream(documentServiceModels)
                        .forEach(this::seedDocumentsIfValidOrPrintError);

                // TODO: 4/11/2021 -  initSeedDocumentsFromJson - add successful seed message

            } catch (IOException e) {
                throw new IllegalStateException(DOCUMENTS_INIT_IO_ERROR_MESSAGE);
            }
        }
    }

    private void seedDocumentsIfValidOrPrintError(DocumentServiceModel documentServiceModel) {

        if (this.validationUtil.isValid(documentServiceModel)) {

            seedDocumentToDb(documentServiceModel);

        } else {
            //todo seedChaptersIfValidOrPrintError - Log errors io printing
            System.out.println(String.format("Document init seed errors from file 'init/documents-init.json':%n "));

            validationUtil.getViolations(documentServiceModel)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            System.out.printf("For document '%s'%n", documentServiceModel.getName());
        }
    }

    @Override
    public boolean documentExists(String documentName) {
        return documentRepository
                .findByName(documentName.toLowerCase().trim())
                .isPresent();
    }

    @Override
    @Transactional // todo - > is Transactional needed at seedDocumentToDb?
    public DocumentServiceModel seedDocumentToDb(DocumentServiceModel documentServiceModel) {

        DocumentEntity documentEntity = this.modelMapper.map(documentServiceModel, DocumentEntity.class);
        documentEntity.setName(documentEntity.getName().toLowerCase().trim());

        return modelMapper.map(documentRepository
                .saveAndFlush(documentEntity), DocumentServiceModel.class);

    }

    @Override
    public DocumentServiceModel findDocumentByName(String documentName) {

        DocumentEntity documentEntity = documentRepository
                .findByName(documentName)
                .orElseThrow(() -> new IllegalArgumentException(DOCUMENT_NOT_FOUND_MESSAGE));

        return modelMapper.map(documentEntity, DocumentServiceModel.class);
    }

    @Override
    public List<DocumentViewModel> findAllDocumentsSortedByNameDesc() {

        return documentRepository
                .findAll(Sort.by(Sort.Direction.DESC, "name"))
                .stream()
                .map(documentEntity -> modelMapper.map(documentEntity, DocumentViewModel.class))
                .collect(Collectors.toList());

    }

    @Override
    public DocumentServiceModel findDocumentById(Long id) {
        DocumentEntity documentEntity = documentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException(DOCUMENT_NOT_FOUND_MESSAGE));

        return modelMapper.map(documentEntity, DocumentServiceModel.class);
    }

    @Override
    @Async
    public CompletableFuture<List<DocumentViewModel>> findAllDocumentsSortedByNameDescAsync() {
        return CompletableFuture
                .supplyAsync(this::findAllDocumentsSortedByNameDesc)
                .orTimeout(30, TimeUnit.SECONDS);
    }

    @Override
    public DocumentServiceModel renameDocument(DocumentServiceModel documentServiceModel, String newName) {
        DocumentEntity documentEntity = documentRepository
                .findById(documentServiceModel.getId())
                .orElseThrow(() -> new IllegalArgumentException(DOCUMENT_NOT_FOUND_MESSAGE));

        documentEntity.setName(newName.toLowerCase().trim());

        documentRepository.saveAndFlush(documentEntity);

        return modelMapper.map(documentEntity, DocumentServiceModel.class);
    }

}
