package com.petkov.spr_final_1.model.aviation_library_entity;

import javax.persistence.*;

@Entity
@DiscriminatorValue("document_subchapter")
@Access(AccessType.PROPERTY)
public class DocumentSubchapterEntity extends BasicReference {

    private DocumentEntity document;

    public DocumentSubchapterEntity() {
    }

    @ManyToOne
    public DocumentEntity getDocument() {
        return document;
    }

    public void setDocument(DocumentEntity document) {
        this.document = document;
    }

}
