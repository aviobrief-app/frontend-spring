package com.petkov.spr_final_1.init;

import com.petkov.spr_final_1.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBInit implements CommandLineRunner {

    private final UserService userService;
    private final DocumentService documentService;
    private final DocumentSubChapterService documentSubChapterService;
    private final ATAChapterService chapterService;
    private final ATASubChapterService subchapterService;
    private final TestService testService;

    public DBInit(UserService userService,
                  DocumentService documentService, DocumentSubChapterService documentSubChapterService, ATAChapterService chapterService,
                  ATASubChapterService subchapterService,
                  TestService testService) {
        this.userService = userService;
        this.documentService = documentService;
        this.documentSubChapterService = documentSubChapterService;
        this.chapterService = chapterService;
        this.subchapterService = subchapterService;
        this.testService = testService;
    }


    @Override
    public void run(String... args) throws Exception {

        userService.seedUsers();
        documentService.initSeedDocumentsFromJson();
        documentSubChapterService.initSeedDocumentSubchaptersFromJson();
        chapterService.initSeedChaptersFromJson();
        subchapterService.initSeedSubchaptersFromJson();


    }

}
