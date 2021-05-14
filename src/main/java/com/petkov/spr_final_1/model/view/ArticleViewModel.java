package com.petkov.spr_final_1.model.view;

import java.time.LocalDate;

public class ArticleViewModel {

    private String id;
    private String title;
    private String articleText;
    private String imageUrl;
    private String fullReferencePath;

    private ATAChapterViewModel chapter;
    private ATASubChapterViewModel ataSubChapter;

    private String documentSubchapterRef;
    private DocumentViewModel document;
    private String documentRevision;
    private LocalDate documentDate;

    public ArticleViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullReferencePath() {
        return fullReferencePath;
    }

    public void setFullReferencePath(String fullReferencePath) {
        this.fullReferencePath = fullReferencePath;
    }

    public ATAChapterViewModel getChapter() {
        return chapter;
    }

    public void setChapter(ATAChapterViewModel chapter) {
        this.chapter = chapter;
    }

    public ATASubChapterViewModel getAtaSubChapter() {
        return ataSubChapter;
    }

    public void setAtaSubChapter(ATASubChapterViewModel ataSubChapter) {
        this.ataSubChapter = ataSubChapter;
    }

    public String getDocumentSubchapterRef() {
        return documentSubchapterRef;
    }

    public void setDocumentSubchapterRef(String documentSubchapterRef) {
        this.documentSubchapterRef = documentSubchapterRef;
    }

    public DocumentViewModel getDocument() {
        return document;
    }

    public void setDocument(DocumentViewModel document) {
        this.document = document;
    }

    public String getDocumentRevision() {
        return documentRevision;
    }

    public void setDocumentRevision(String documentRevision) {
        this.documentRevision = documentRevision;
    }

    public LocalDate getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(LocalDate documentDate) {
        this.documentDate = documentDate;
    }
}
