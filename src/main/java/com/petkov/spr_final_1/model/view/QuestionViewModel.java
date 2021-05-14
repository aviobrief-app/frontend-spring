package com.petkov.spr_final_1.model.view;

public class QuestionViewModel {

    private String id;
    private String question;
    private String fullReferencePath;
    private String correctAnswer;

    private ArticleViewModel article;

    private ATAChapterViewModel chapter;
    private ATASubChapterViewModel ataSubChapter;

    private DocumentViewModel document;
    private DocumentSubchapterViewModel documentSubchapter;

    public QuestionViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getFullReferencePath() {
        return fullReferencePath;
    }

    public void setFullReferencePath(String fullReferencePath) {
        this.fullReferencePath = fullReferencePath;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public ArticleViewModel getArticle() {
        return article;
    }

    public void setArticle(ArticleViewModel article) {
        this.article = article;
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

    public DocumentViewModel getDocument() {
        return document;
    }

    public void setDocument(DocumentViewModel document) {
        this.document = document;
    }

    public DocumentSubchapterViewModel getDocumentSubchapter() {
        return documentSubchapter;
    }

    public void setDocumentSubchapter(DocumentSubchapterViewModel documentSubchapter) {
        this.documentSubchapter = documentSubchapter;
    }
}
