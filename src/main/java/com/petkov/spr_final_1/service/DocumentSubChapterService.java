package com.petkov.spr_final_1.service;

import com.petkov.spr_final_1.model.service.document.DocumentSubchapterServiceModel;
import com.petkov.spr_final_1.model.view.DocumentSubchapterViewModel;
import com.petkov.spr_final_1.model.view.DocumentViewModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DocumentSubChapterService {

    void initSeedDocumentSubchaptersFromJson();

    boolean subChapterExistsInDocument(String documentRef, String docSubchapterName);

    void seedDocumentSubchapterToDb(DocumentSubchapterServiceModel documentSubchapterServiceModel);

    List<DocumentSubchapterViewModel> getAllSortedByNameDesc();

    CompletableFuture<List<DocumentSubchapterViewModel>> getAllSortedByNameDescAsync();

    DocumentSubchapterServiceModel findByDocumentAndDocumentSubchapter(String documentName, String subchapterName);

}
