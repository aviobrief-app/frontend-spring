package com.petkov.spr_final_1.model.service;

public abstract class BaseServiceModel {

    private Long id;

    public BaseServiceModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
