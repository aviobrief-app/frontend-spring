package com.petkov.spr_final_1.model.aviation_library_entity;

import javax.persistence.*;

@Entity
@DiscriminatorValue("document")
@Access(AccessType.PROPERTY)
public class DocumentEntity extends BasicReference {

    public DocumentEntity() {
    }

}
