package com.petkov.spr_final_1.model.service.test;

import com.petkov.spr_final_1.model.app_entity.UserEntity;
import com.petkov.spr_final_1.model.app_entity.SubmittedQuestionEntity;
import com.petkov.spr_final_1.model.service.BaseServiceModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CompletedTestServiceModel extends BaseServiceModel {

    private String parentTestId;
    private String name;
    private LocalDate dueDate;
    private LocalDateTime timeCompleted;
    private List<SubmittedQuestionEntity> questionEntities;
    private UserEntity userEntity;

    public CompletedTestServiceModel() {
    }

     // todo - CompletedTestServiceModel add restrictions?
    public String getParentTestId() {
        return parentTestId;
    }

    public void setParentTestId(String parentTestId) {
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

    public List<SubmittedQuestionEntity> getQuestionEntities() {
        return questionEntities;
    }

    public void setQuestionEntities(List<SubmittedQuestionEntity> questionEntities) {
        this.questionEntities = questionEntities;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
