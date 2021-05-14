package com.petkov.spr_final_1.model.aviation_library_entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "articles")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="article_type", discriminatorType = DiscriminatorType.STRING)
@Access(AccessType.PROPERTY)
public abstract class BasicArticle extends BaseEntity {

    private String title;
    private String articleText;
    private String imageUrl;
    private List<BasicReference> references;

    public BasicArticle() {
    }

    @Column(name = "title", unique = true, nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "article_text", unique = false, nullable = false, columnDefinition = "TEXT")
    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    @Column(name = "image_url", unique = false, nullable = true)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @OneToMany
    @JoinColumn(name = "article_id")
    public List<BasicReference> getReferences() {
        return references;
    }

    public void setReferences(List<BasicReference> references) {
        this.references = references;
    }

}
