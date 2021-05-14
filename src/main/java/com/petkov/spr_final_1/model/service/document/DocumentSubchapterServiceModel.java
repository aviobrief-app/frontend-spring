package com.petkov.spr_final_1.model.service.document;

import com.google.gson.annotations.Expose;
import com.petkov.spr_final_1.model.service.BaseServiceModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class DocumentSubchapterServiceModel extends BaseServiceModel {

    @Expose
    private String name;
    @Expose
    private String document;

    public DocumentSubchapterServiceModel() {
    }

    @NotBlank(message = "Cannot be empty. ")
    @Size(min = 3, message = "Subchapter name must be at least 3 characters. ")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank(message = "Document required.")
    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
