package com.petkov.spr_final_1.model.binding.document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class DocumentAddBindingModel {

    private String name;

    public DocumentAddBindingModel() {
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
