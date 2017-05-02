package com.example.diana.booksrecommender;

import java.io.Serializable;

/**
 * Created by Diana on 4/30/2017.
 */

public class Book implements Serializable{

    private String title;
    private String authors;
    private String imgUrl;
    private Double averageRating;
    private int pageCount;
    private String description;
    private String[]  categories;
    private String selfLink;
    private static final long serialVersionUID = 1L;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Book(String title, String authors, String imgUrl, Double averageRating, int pageCount, String description, String[] categories, String selfLink, String id) {
        this.title = title;
        this.authors = authors;
        this.imgUrl = imgUrl;
        this.averageRating = averageRating;
        this.pageCount = pageCount;
        this.description = description;
        if (categories == null)
        {
            this.categories = new String[1];
            this.categories[0] = "NO CATEGORY";
        }else
            this.categories = categories;

        this.selfLink = selfLink;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
