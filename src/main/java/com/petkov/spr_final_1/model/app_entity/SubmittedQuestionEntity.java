package com.petkov.spr_final_1.model.app_entity;

import javax.persistence.*;

@Entity
@Table(name = "submitted_questions")
@Access(AccessType.PROPERTY)

public class SubmittedQuestionEntity extends BaseEntity {

    private String name;
    private String question;
    private String fullReferencePath;
    private String correctAnswer;
    private String altAnswer1;
    private String altAnswer2;
    private String altAnswer3;
    private String altAnswer4;

    private String submittedAnswer;
    private boolean answeredCorrectly;
    private CompletedTestEntity completedTestEntity;

    public SubmittedQuestionEntity() {
    }

    @Column(name = "name", unique = false, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "question", unique = false, nullable = false, columnDefinition = "TEXT")
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Column(name = "full_reference_path", unique = false, nullable = true)
    public String getFullReferencePath() {
        return fullReferencePath;
    }

    public void setFullReferencePath(String fullReferencePath) {
        this.fullReferencePath = fullReferencePath;
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

    @Column(name = "submitted_answer", unique = false, nullable = false)
    public String getSubmittedAnswer() {
        return submittedAnswer;
    }

    public void setSubmittedAnswer(String submittedAnswer) {
        this.submittedAnswer = submittedAnswer;
    }

    @Column(name = "is_correct", unique = false, nullable = false)
    public boolean isAnsweredCorrectly() {
        return answeredCorrectly;
    }

    public void setAnsweredCorrectly(boolean answeredCorrectly) {
        this.answeredCorrectly = answeredCorrectly;
    }


    @ManyToOne
    @JoinColumn(name = "test", referencedColumnName = "id")
    public CompletedTestEntity getCompletedTestEntity() {
        return completedTestEntity;
    }

    public void setCompletedTestEntity(CompletedTestEntity completedTestEntity) {
        this.completedTestEntity = completedTestEntity;
    }
}
