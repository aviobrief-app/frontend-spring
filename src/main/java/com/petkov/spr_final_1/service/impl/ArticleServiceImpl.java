package com.petkov.spr_final_1.service.impl;

import com.petkov.spr_final_1.model.aviation_library_entity.ArticleEntity;
import com.petkov.spr_final_1.model.aviation_library_entity.ATAChapterEntity;
import com.petkov.spr_final_1.model.aviation_library_entity.ATASubChapterEntity;
import com.petkov.spr_final_1.model.service.document.ArticleServiceModel;
import com.petkov.spr_final_1.model.view.ArticleViewModel;
import com.petkov.spr_final_1.repository.ArticleRepository;
import com.petkov.spr_final_1.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    private final ATAChapterService chapterService;
    private final ATASubChapterService subChapterService;
    private final DocumentService documentService;

    public ArticleServiceImpl(ArticleRepository articleRepository,
                              ModelMapper modelMapper,
                              CloudinaryService cloudinaryService,
                              ATAChapterService chapterService,
                              ATASubChapterService subChapterService,
                              DocumentService documentService) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.chapterService = chapterService;
        this.subChapterService = subChapterService;
        this.documentService = documentService;
    }

    @Override
    public boolean articleExistsByTitle(String title) {
        return this.articleRepository.findByTitle(title).isPresent();
    }

    @Override
    public void seedArticleToDb(ArticleServiceModel articleServiceModel) throws IOException {

        ArticleEntity articleEntity = modelMapper.map(articleServiceModel, ArticleEntity.class);

        if (!articleServiceModel.getImage().isEmpty()) {
            MultipartFile img = articleServiceModel.getImage();
            String imageUrl = cloudinaryService.uploadImage(img);
            articleEntity.setImageUrl(imageUrl);
        }

        if (!articleServiceModel.getAtaSubChapterRef().isEmpty() && !articleServiceModel.getChapterRef().isEmpty()) {

            //todo - put back in controller
            int chapterRef = Integer.parseInt(articleServiceModel.getChapterRef().split(" ")[0]);

            // todo seedArticleToDb - add try catch
            ATAChapterEntity chapterEntity =
                    modelMapper.map(chapterService.findChapterByAtaCode(chapterRef), ATAChapterEntity.class);

            //todo - put back in controller
            int subChapterRef = Integer.parseInt(articleServiceModel.getAtaSubChapterRef().split(" ")[0]);
            ATASubChapterEntity subChapterEntity =
                    modelMapper.map(subChapterService
                            .findByChapterAndSubchapterAta(chapterRef, subChapterRef), ATASubChapterEntity.class);


        } else if (!articleServiceModel.getChapterRef().isEmpty()) {
            //todo - put back in controller
            int chapterRef = Integer.parseInt(articleServiceModel.getChapterRef().split(" ")[0]);
            ATAChapterEntity chapterEntity =
                    modelMapper.map(chapterService.findChapterByAtaCode(chapterRef), ATAChapterEntity.class);


        }

        //todo - refactor articleEntity.setDocument(documentEntity) to
        // articleEntity.setDocumentSubchapter(documentSubchapterEntity)
//        if (!articleServiceModel.getDocumentRef().isEmpty()) {
//            String documentRef = articleServiceModel.getDocumentRef();
//            DocumentEntity documentEntity =
//                    modelMapper.map(documentService.findDocumentByName(documentRef), DocumentEntity.class);
//
//            articleEntity.setDocument(documentEntity);
//        }


        //todo seedArticleToDb debug point
        System.out.println();

        articleRepository.saveAndFlush(articleEntity);
    }

    @Override
    public ArticleServiceModel findArticleByTitle(String title) throws IllegalArgumentException {

        ArticleEntity articleEntity = articleRepository
                .findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Article not found in DB"));

        return modelMapper.map(articleEntity, ArticleServiceModel.class);
    }

    @Override
    public List<ArticleViewModel> findAllSortedByNameDesc() {

        //todo - add sorting to List<ArticleViewModel> findAllSortedByNameDesc() {
        return articleRepository
                .findAll()
                .stream()
                .map(articleEntity -> modelMapper.map(articleEntity, ArticleViewModel.class))
//                .sorted(Comparator.comparing(
//                        (ArticleViewModel articleViewModel) -> articleViewModel.getChapter().getAtaCode()))
                .collect(Collectors.toList());
    }

    @Override
    @Async
    public CompletableFuture<List<ArticleViewModel>> findAllSortedByNameDescAsync() {
        return CompletableFuture
                .supplyAsync(this::findAllSortedByNameDesc)
                .orTimeout(30, TimeUnit.SECONDS);
    }
}
