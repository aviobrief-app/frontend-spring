package com.petkov.spr_final_1.model.view;

public class ATASubChapterViewModel {

    private String id;
    private Integer ataSubCode;
    private String subchapterName;
    private Integer ataChapter;

    public ATASubChapterViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAtaSubCode() {
        return ataSubCode;
    }

    public void setAtaSubCode(Integer ataSubCode) {
        this.ataSubCode = ataSubCode;
    }

    public String getSubchapterName() {
        return subchapterName;
    }

    public void setSubchapterName(String subchapterName) {
        this.subchapterName = subchapterName;
    }

    public Integer getAtaChapter() {
        return ataChapter;
    }

    public void setAtaChapter(Integer ataChapter) {
        this.ataChapter = ataChapter;
    }
}
