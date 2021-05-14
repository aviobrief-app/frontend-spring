package com.petkov.spr_final_1.model.app_entity;

import com.petkov.spr_final_1.model.aviation_library_entity.BasicArticle;
import com.petkov.spr_final_1.model.aviation_library_entity.BasicReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "questions")
@Access(AccessType.PROPERTY)
public class QuestionEntity extends BaseEntity {

    private String name;
    private String question;
    private String correctAnswer;
    private String altAnswer1;
    private String altAnswer2;
    private String altAnswer3;
    private String altAnswer4;

    private String fullReferencePath;
    private BasicArticle article;
    private List<BasicReference> references;

    public QuestionEntity() {
    }

    @Column(name = "name", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "question", unique = true, nullable = false, columnDefinition = "TEXT")
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    @Column(name = "correct_answer", unique = false, nullable = false)
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Column(name = "alt_answer_1", unique = false, nullable = false)
    public String getAltAnswer1() {
        return altAnswer1;
    }

    public void setAltAnswer1(String altAnswer1) {
        this.altAnswer1 = altAnswer1;
    }

    @Column(name = "alt_answer_2", unique = false, nullable = false)
    public String getAltAnswer2() {
        return altAnswer2;
    }

    public void setAltAnswer2(String altAnswer2) {
        this.altAnswer2 = altAnswer2;
    }

    @Column(name = "alt_answer_3", unique = false, nullable = true)
    public String getAltAnswer3() {
        return altAnswer3;
    }

    public void setAltAnswer3(String altAnswer3) {
        this.altAnswer3 = altAnswer3;
    }

    @Column(name = "alt_answer_4", unique = false, nullable = true)
    public String getAltAnswer4() {
        return altAnswer4;
    }

    public void setAltAnswer4(String altAnswer4) {
        this.altAnswer4 = altAnswer4;
    }


    @Column(name = "full_reference_path", unique = false, nullable = true)
    public String getFullReferencePath() {
        return fullReferencePath;
    }

    public void setFullReferencePath(String fullReferencePath) {
        this.fullReferencePath = fullReferencePath;
    }

    @ManyToOne(cascade = CascadeType.ALL)// - todo - refactor this (cascade = CascadeType.ALL). Needed only for init DB setup
    public BasicArticle getArticle() {
        return article;
    }

    public void setArticle(BasicArticle article) {
        this.article = article;
    }


    @OneToMany
    @JoinColumn(name = "question_id")
    public List<BasicReference> getReferences() {
        return references;
    }

    public void setReferences(List<BasicReference> references) {
        this.references = references;
    }
}
