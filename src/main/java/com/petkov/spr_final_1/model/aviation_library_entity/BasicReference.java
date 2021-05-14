package com.petkov.spr_final_1.model.aviation_library_entity;


import javax.persistence.*;

@Entity
@Table(name = "library_references")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="reference_type", discriminatorType = DiscriminatorType.STRING)
@Access(AccessType.PROPERTY)
public abstract class BasicReference extends BaseEntity {

    private String name;

    public BasicReference() {
    }

    @Column(name = "name", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
