package com.petkov.spr_final_1.model.aviation_library_entity;

import javax.persistence.*;

@Entity
@DiscriminatorValue("ata_subchapter")
@Access(AccessType.PROPERTY)
public class ATASubChapterEntity extends BasicReference {

    private Integer ataSubCode;
    private ATAChapterEntity ataChapter;

    public ATASubChapterEntity() {
    }

    @Column(name = "ata_subcode")
    public Integer getAtaSubCode() {
        return ataSubCode;
    }

    public void setAtaSubCode(Integer ataSubCode) {
        this.ataSubCode = ataSubCode;
    }


    @ManyToOne
    public ATAChapterEntity getAtaChapter() {
        return ataChapter;
    }

    public void setAtaChapter(ATAChapterEntity ataChapterRef) {
        this.ataChapter = ataChapterRef;
    }
}

