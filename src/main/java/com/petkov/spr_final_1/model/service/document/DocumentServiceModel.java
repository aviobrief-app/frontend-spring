package com.petkov.spr_final_1.model.service.document;

import com.google.gson.annotations.Expose;
import com.petkov.spr_final_1.model.service.BaseServiceModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class DocumentServiceModel extends BaseServiceModel {

    @Expose
    private String name;

    public DocumentServiceModel(){
    }

    @NotBlank(message = "Document name cannot be empty. ")
    @Size(min = 3, message = "Document name must be at least 3 characters. ")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
