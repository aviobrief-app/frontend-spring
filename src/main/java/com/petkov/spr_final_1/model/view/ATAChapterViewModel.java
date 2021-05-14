package com.petkov.spr_final_1.model.view;

public class ATAChapterViewModel {

    private String id;
    private Integer ataCode;
    private String name;

    public ATAChapterViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAtaCode() {
        return ataCode;
    }

    public void setAtaCode(Integer ataCode) {
        this.ataCode = ataCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
