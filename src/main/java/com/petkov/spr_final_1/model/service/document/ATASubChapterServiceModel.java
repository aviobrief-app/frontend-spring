package com.petkov.spr_final_1.model.service.document;

import com.google.gson.annotations.Expose;
import com.petkov.spr_final_1.model.service.BaseServiceModel;

import javax.validation.constraints.*;

public class ATASubChapterServiceModel extends BaseServiceModel {

    @Expose
    private Integer ataSubCode;
    @Expose
    private String name;
    @Expose
    private String ataChapterRefInput;

    public ATASubChapterServiceModel() {
    }

    @NotNull(message = "ATA code required.")
    @Min(value = 0, message = "ATA chapter minimum is 00.")
    @Max(value = 100,  message = "ATA chapter maximum is 100.")
    public Integer getAtaSubCode() {
        return ataSubCode;
    }

    public void setAtaSubCode(Integer ataSubCode) {
        this.ataSubCode = ataSubCode;
    }

    @Size(min = 3, message = "SubChapter name min 3 characters.")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank(message = "ATA Chapter required.")
    public String getAtaChapterRefInput() {
        return ataChapterRefInput;
    }

    public void setAtaChapterRefInput(String ataChapterRefInput) {
        this.ataChapterRefInput = ataChapterRefInput;
    }
}
