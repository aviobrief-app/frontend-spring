package com.petkov.spr_final_1.service;

import com.petkov.spr_final_1.model.service.document.ATASubChapterServiceModel;
import com.petkov.spr_final_1.model.view.ATASubChapterViewModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ATASubChapterService {

    void seedATASubchapterToDb(ATASubChapterServiceModel subChapterServiceModel);

    boolean subChapterCodeExists(int ataSubCode, int chapterRef);

    void initSeedSubchaptersFromJson();

    ATASubChapterServiceModel findByChapterAndSubchapterAta(int ataChapterCode, int ataSubchapterCode);

    List<ATASubChapterViewModel> getAllSortedByATADesc();

    CompletableFuture<List<ATASubChapterViewModel>> getAllSortedByATAAsync();

}
