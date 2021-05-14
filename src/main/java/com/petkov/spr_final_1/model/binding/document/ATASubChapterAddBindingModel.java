package com.petkov.spr_final_1.model.binding.document;

import javax.validation.constraints.*;

public class ATASubChapterAddBindingModel {

    private Integer ataSubCode;
    private String subchapterName;
    private String ataChapterRefInput;

    public ATASubChapterAddBindingModel() {
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
    public String getSubchapterName() {
        return subchapterName;
    }

    public void setSubchapterName(String subchapterName) {
        this.subchapterName = subchapterName;
    }

    @NotBlank(message = "ATA Chapter required.")
    public String getAtaChapterRefInput() {
        return ataChapterRefInput;
    }

    public void setAtaChapterRefInput(String ataChapterRefInput) {
        this.ataChapterRefInput = ataChapterRefInput;
    }
}
