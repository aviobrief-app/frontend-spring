package com.petkov.spr_final_1.model.app_entity;

import com.petkov.spr_final_1.model.enumeration.UserRole;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Access(AccessType.PROPERTY)
public class UserRoleEntity extends BaseEntity {

    public UserRoleEntity() {
    }

    public UserRoleEntity(UserRole role) {
        this.role = role;
    }

    private UserRole role;

    @Enumerated(EnumType.STRING)
    public UserRole getRole() {
        return role;
    }

    public UserRoleEntity setRole(UserRole role) {
        this.role = role;
        return this;
    }
}
