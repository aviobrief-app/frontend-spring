package com.petkov.spr_final_1.service;

import com.petkov.spr_final_1.model.service.document.ArticleServiceModel;
import com.petkov.spr_final_1.model.view.ArticleViewModel;
import com.petkov.spr_final_1.model.view.DocumentSubchapterViewModel;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ArticleService {

    boolean articleExistsByTitle(String title);

    void seedArticleToDb(ArticleServiceModel articleServiceModel) throws IOException;

    ArticleServiceModel findArticleByTitle(String title) throws IllegalArgumentException;

    List<ArticleViewModel> findAllSortedByNameDesc();

    CompletableFuture<List<ArticleViewModel>> findAllSortedByNameDescAsync();
}
