package com.petkov.spr_final_1.service.impl;

import com.petkov.spr_final_1.model.app_entity.QuestionEntity;
import com.petkov.spr_final_1.model.aviation_library_entity.*;
import com.petkov.spr_final_1.model.service.test.QuestionServiceModel;
import com.petkov.spr_final_1.model.view.QuestionViewModel;
import com.petkov.spr_final_1.repository.QuestionRepository;
import com.petkov.spr_final_1.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final ModelMapper modelMapper;
    private final ArticleService articleService;
    private final DocumentService documentService;
    private final ATASubChapterService ataSubChapterService;
    private final ATAChapterService ataChapterService;
    private final DocumentSubChapterService documentSubChapterService;

    public QuestionServiceImpl(QuestionRepository questionRepository, ModelMapper modelMapper, ArticleService articleService, DocumentService documentService, ATASubChapterService ataSubChapterService, ATAChapterService ataChapterService, DocumentSubChapterService documentSubChapterService) {
        this.questionRepository = questionRepository;
        this.modelMapper = modelMapper;
        this.articleService = articleService;
        this.documentService = documentService;
        this.ataSubChapterService = ataSubChapterService;
        this.ataChapterService = ataChapterService;
        this.documentSubChapterService = documentSubChapterService;
    }

    @Override
    public void saveQuestion(QuestionEntity questionEntity) {
        this.questionRepository.save(questionEntity);
    }

    @Override
    public boolean questionExistsByName(String name) {
        return this.questionRepository.findByName(name).isPresent();
    }

    @Override
    @Transactional
    public void seedQuestionToDb(QuestionServiceModel questionServiceModel) {

        QuestionEntity questionEntity =
                modelMapper.map(questionServiceModel, QuestionEntity.class);

        setDocument(questionServiceModel, questionEntity);

        setDocumentSubchapterIfNotNull(questionServiceModel, questionEntity);

        setAtaChapter(questionServiceModel, questionEntity);

        setAtaSubChapterIfNotNull(questionServiceModel, questionEntity);

        setArticleIfNotNull(questionServiceModel, questionEntity);

        questionRepository.saveAndFlush(questionEntity);
    }

    private void setArticleIfNotNull(QuestionServiceModel questionServiceModel, QuestionEntity questionEntity) {
        if (!questionServiceModel.getArticle().isBlank()) {

            try {
                ArticleEntity articleEntity = modelMapper
                        .map(articleService.findArticleByTitle(questionServiceModel.getArticle()), ArticleEntity.class);

                questionEntity.setArticle(articleEntity);

            } catch (IllegalArgumentException exception) {
                //todo - advice somewhere for the exception
            }
        }
    }

    private void setAtaSubChapterIfNotNull(QuestionServiceModel questionServiceModel, QuestionEntity questionEntity) {

        if (!questionServiceModel.getAtaSubChapter().isBlank()) {

            int ataChapterCode = Integer.parseInt(questionServiceModel.getChapter());
            int ataSubChapterCode = Integer.parseInt(questionServiceModel.getAtaSubChapter());

            try {
                ATASubChapterEntity ataSubChapterEntity = modelMapper
                        .map(ataSubChapterService
                                .findByChapterAndSubchapterAta(ataChapterCode, ataSubChapterCode), ATASubChapterEntity.class);


            } catch (IllegalArgumentException ignored) {
                //todo advice somewhere for what happened
            }


        }
    }

    private void setAtaChapter(QuestionServiceModel questionServiceModel, QuestionEntity questionEntity) {

        ATAChapterEntity ataChapterEntity =
                modelMapper.map(ataChapterService
                                .findChapterByAtaCode(Integer.parseInt(questionServiceModel.getChapter())),
                        ATAChapterEntity.class);


    }

    private void setDocument(QuestionServiceModel questionServiceModel, QuestionEntity questionEntity) {


        DocumentEntity documentEntity =
                modelMapper.map(documentService
                        .findDocumentByName(questionServiceModel.getDocument()), DocumentEntity.class);

    }

    private void setDocumentSubchapterIfNotNull(QuestionServiceModel questionServiceModel, QuestionEntity questionEntity) {

        if (!questionServiceModel.getDocumentSubchapter().isBlank()) {

            String documentName = questionServiceModel.getDocument();
            String documentSubchapterName = questionServiceModel.getDocumentSubchapter();

            try {
                DocumentSubchapterEntity documentSubchapterEntity =
                        modelMapper.map(documentSubChapterService.
                                        findByDocumentAndDocumentSubchapter(documentName, documentSubchapterName),
                                DocumentSubchapterEntity.class);


            } catch (IllegalArgumentException exception) {
                //todo - advice somewhere for the exception
            }

        }
    }

    @Override
    @Async
    public CompletableFuture<List<QuestionViewModel>> getAllQuestionsSortedByATA() {

        return CompletableFuture
                .supplyAsync(() -> questionRepository.findAll()
                        .stream()
                        .map(questionEntity -> modelMapper.map(questionEntity, QuestionViewModel.class))
                        .collect(Collectors.toList()))
                .orTimeout(30, TimeUnit.SECONDS);
    }

    @Override
    public QuestionServiceModel findById(Long id) {

        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        QuestionEntity questionEntity = questionRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found ind DB."));

        QuestionServiceModel questionServiceModel =
                modelMapper.map(questionEntity, QuestionServiceModel.class);

        try {
        } catch (NullPointerException ignored) {
        }

        try {

        } catch (NullPointerException ignored) {
        }

        return questionServiceModel;
    }


}
