package com.petkov.spr_final_1.model.service.test;

import com.petkov.spr_final_1.model.service.BaseServiceModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QuestionServiceModel extends BaseServiceModel {

    private String name;
    private String question;
    private String fullReferencePath;

    private String correctAnswer;
    private String altAnswer1;
    private String altAnswer2;
    private String altAnswer3;
    private String altAnswer4;

    private String article;

    private String chapter;
    private String ataSubChapter;

    private String document;
    private String documentSubchapter;

    public QuestionServiceModel() {
    }


    @NotNull(message = "Question title is required.")
    @Size(min = 3, message = "Question title should be min 3 characters.")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "Question text is required.")
    @Size(min = 50, message = "Question text should be min 50 characters.")
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

    @NotBlank(message = "Correct Answer is required.")
    @Size(min = 1, message = "Correct Answer should be min 1 character.")
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @NotBlank(message = "Alternate answer 1 is required.")
    @Size(min = 1, message = "Alternate answer 1 should be min 1 character.")
    public String getAltAnswer1() {
        return altAnswer1;
    }

    public void setAltAnswer1(String altAnswer1) {
        this.altAnswer1 = altAnswer1;
    }

    @NotBlank(message = "Alternate answer 2 is required.")
    @Size(min = 1, message = "Alternate answer 2 should be min 1 character.")
    public String getAltAnswer2() {
        return altAnswer2;
    }

    public void setAltAnswer2(String altAnswer2) {
        this.altAnswer2 = altAnswer2;
    }

    public String getAltAnswer3() {
        return altAnswer3;
    }

    public void setAltAnswer3(String altAnswer3) {
        this.altAnswer3 = altAnswer3;
    }

    public String getAltAnswer4() {
        return altAnswer4;
    }

    public void setAltAnswer4(String altAnswer4) {
        this.altAnswer4 = altAnswer4;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    @NotBlank(message = "ATA Chapter is required.")
    @Size(min = 3, message = "ATA Chapter be min 3 characters.")
    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getAtaSubChapter() {
        return ataSubChapter;
    }

    public void setAtaSubChapter(String ataSubChapter) {
        this.ataSubChapter = ataSubChapter;
    }

    @NotBlank(message = "Document is required.")
    @Size(min = 3, message = "Document name should be min 3 characters.")
    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    @NotBlank(message = "Document Subchapter is required.")
    @Size(min = 3, message = "Document Subchapter name should be min 3 characters.")
    public String getDocumentSubchapter() {
        return documentSubchapter;
    }

    public void setDocumentSubchapter(String documentSubchapter) {
        this.documentSubchapter = documentSubchapter;
    }
}
