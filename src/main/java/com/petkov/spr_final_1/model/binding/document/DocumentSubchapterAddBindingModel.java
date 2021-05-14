package com.petkov.spr_final_1.model.binding.document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class DocumentSubchapterAddBindingModel {

    private String name;
    private String document;

    public DocumentSubchapterAddBindingModel() {
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
