package com.petkov.spr_final_1.model.app_entity;

import com.petkov.spr_final_1.model.enumeration.TestStatusEnum;
import com.petkov.spr_final_1.model.enumeration.TestTagEnum;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tests")
@Access(AccessType.PROPERTY)
public class TestEntity extends BaseEntity {

    private String name;
    private LocalDate dateCreated;
    private LocalDate dueDate;
    private UserEntity createdBy;
    private TestStatusEnum testStatusEnum;
    private List<TestTagEnum> testTagEnums;
    private List<QuestionEntity> questionEntities;

    public TestEntity() {
    }

    public TestEntity(String name) {
        this.name = name;
    }

    @Column(name = "name", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "date_created", unique = false, nullable = false)
    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Column(name = "due_date", unique = false, nullable = false)
    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @ManyToOne
    public UserEntity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserEntity createdBy) {
        this.createdBy = createdBy;
    }

    @Enumerated(EnumType.STRING)
    public TestStatusEnum getTestStatus() {
        return testStatusEnum;
    }

    public void setTestStatus(TestStatusEnum testStatusEnum) {
        this.testStatusEnum = testStatusEnum;
    }

    @ElementCollection(targetClass = TestTagEnum.class)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    public List<TestTagEnum> getTestTagEnums() {
        return testTagEnums;
    }

    public void setTestTagEnums(List<TestTagEnum> testTagEnums) {
        this.testTagEnums = testTagEnums;
    }

    @ManyToMany // todo - refactor this (cascade = CascadeType.ALL) -\
                     // todo it has no place here when using frontend to add tests
    @JoinTable(
            name = "tests_questions",
            joinColumns = @JoinColumn(name = "question_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "test_id", referencedColumnName = "id")
    )
    public List<QuestionEntity> getQuestions() {
        return questionEntities;
    }

    public void setQuestions(List<QuestionEntity> questionEntities) {
        this.questionEntities = questionEntities;
    }
}
