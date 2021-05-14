package com.petkov.spr_final_1.model.app_entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "completed_tests")
@Access(AccessType.PROPERTY)
public class CompletedTestEntity extends BaseEntity {

    private Long parentTestId;
    private String name;
    private LocalDate dueDate;
    private LocalDateTime timeCompleted;
    private List<SubmittedQuestionEntity> questionEntities;
    private UserEntity userEntity;

    public CompletedTestEntity() {
    }

    //todo - add columns at CompletedTestEntity
    public Long getParentTestId() {
        return parentTestId;
    }

    public void setParentTestId(Long parentTestId) {
        this.parentTestId = parentTestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getTimeCompleted() {
        return timeCompleted;
    }

    public void setTimeCompleted(LocalDateTime timeCompleted) {
        this.timeCompleted = timeCompleted;
    }

    @OneToMany(
            mappedBy = "completedTestEntity",
            targetEntity = SubmittedQuestionEntity.class,
            cascade = CascadeType.ALL)
    public List<SubmittedQuestionEntity> getQuestionEntities() {
        return questionEntities;
    }

    public void setQuestionEntities(List<SubmittedQuestionEntity> questionEntities) {
        this.questionEntities = questionEntities;
    }

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "id")
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}