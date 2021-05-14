package com.petkov.spr_final_1.model.aviation_library_entity;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("FCOM_article")
@Access(AccessType.PROPERTY)
public class ArticleEntity extends BasicArticle {

    private String fullReferencePath;
    private String documentRevision;
    private LocalDate documentDate;

    public ArticleEntity() {
    }


    @Column(name = "full_reference_path", unique = false, nullable = true)
    public String getFullReferencePath() {
        return fullReferencePath;
    }

    public void setFullReferencePath(String fullReferencePath) {
        this.fullReferencePath = fullReferencePath;
    }

    @Column(name = "document_revision", unique = false, nullable = true)
    public String getDocumentRevision() {
        return documentRevision;
    }

    public void setDocumentRevision(String documentRevision) {
        this.documentRevision = documentRevision;
    }

    @Column(name = "document_date", unique = false, nullable = true)
    public LocalDate getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(LocalDate documentDate) {
        this.documentDate = documentDate;
    }

}
