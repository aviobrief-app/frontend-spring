package com.petkov.spr_final_1.model.app_entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Access(AccessType.PROPERTY)
public class UserEntity extends BaseEntity {


    private String username;
    private String fullName;
    private String password;
    private String email;
    private List<TestEntity> testEntities;
    private List<UserRoleEntity> roles = new ArrayList<>();
    private List<CompletedTestEntity> completedTestEntities;

    public UserEntity() {
    }

    @Column(name = "username", unique = true, nullable = false)
    public String getUsername() {
        return username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    @Column(name = "full_name", unique = false, nullable = false)
    public String getFullName() {
        return fullName;
    }

    public UserEntity setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    @Column(name = "password", unique = false, nullable = false)
    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    @Column(name = "email", unique = true, nullable = true)//todo - email must not be empty, set like this for dev process
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OneToMany
    public List<TestEntity> getTestEntities() {
        return testEntities;
    }

    public void setTestEntities(List<TestEntity> testEntities) {
        this.testEntities = testEntities;
    }


    @ManyToMany(fetch = FetchType.EAGER)
    public List<UserRoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRoleEntity> roles) {
        this.roles = roles;
    }

    @OneToMany(mappedBy = "userEntity", targetEntity = CompletedTestEntity.class)
    public List<CompletedTestEntity> getCompletedTestEntities() {
        return completedTestEntities;
    }

    public void setCompletedTestEntities(List<CompletedTestEntity> completedTestEntities) {
        this.completedTestEntities = completedTestEntities;
    }
}
