package com.petkov.spr_final_1.model.binding.document;

import javax.validation.constraints.*;

public class ATAChapterAddBindingModel {

    private Integer ataCode;
    private String name;

    public ATAChapterAddBindingModel() {
    }

    @NotNull(message = "ATA code cannot be empty.")
    @Min(value = 0, message = "ATA code minimum 00.")
    @Max(value = 100, message = "ATA code maximum 100.")
    public Integer getAtaCode() {
        return ataCode;
    }

    public void setAtaCode(Integer ataCode) {
        this.ataCode = ataCode;
    }

    @NotBlank(message = "Chapter name cannot be empty.")
    @Size(min = 3, message = "Chapter name length must be at least 3 characters.")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
