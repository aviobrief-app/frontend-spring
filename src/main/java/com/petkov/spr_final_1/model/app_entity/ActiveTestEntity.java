package com.petkov.spr_final_1.model.app_entity;

import com.petkov.spr_final_1.model.view.ActiveQuestionViewModel;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "active_test_backup")
@Access(AccessType.PROPERTY)
public class ActiveTestEntity extends BaseEntity {


    private String name;
    private LocalDate dueDate;
    private List<ActiveQuestionViewModel> questionEntities;
    private List<SubmittedQuestionEntity> submittedQuestionEntities;
    private UserEntity userEntity;

    public ActiveTestEntity() {
    }


    @Column(name = "name", unique = true, nullable = false)
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


    @Transient
    public List<ActiveQuestionViewModel> getQuestionEntities() {
        return questionEntities;
    }

    public void setQuestionEntities(List<ActiveQuestionViewModel> questionEntities) {
        this.questionEntities = questionEntities;
    }

    @Transient
    public List<SubmittedQuestionEntity> getSubmittedQuestionEntities() {
        return submittedQuestionEntities;
    }

    public void setSubmittedQuestionEntities(List<SubmittedQuestionEntity> submittedQuestionEntities) {
        this.submittedQuestionEntities = submittedQuestionEntities;
    }

    @OneToOne
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
