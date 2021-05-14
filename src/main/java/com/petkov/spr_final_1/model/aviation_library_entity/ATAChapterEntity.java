package com.petkov.spr_final_1.model.aviation_library_entity;


import javax.persistence.*;

@Entity
@DiscriminatorValue("ata_chapter")
@Access(AccessType.PROPERTY)
public class ATAChapterEntity extends BasicReference {

    private Integer ataCode;

    public ATAChapterEntity() {
    }

    @Column(name = "ata_code", unique = true)
    public Integer getAtaCode() {
        return ataCode;
    }

    public void setAtaCode(Integer ataCode) {
        this.ataCode = ataCode;
    }

}
