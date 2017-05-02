package com.example.diana.booksrecommender;

import java.io.Serializable;

/**
 * Created by Diana on 4/30/2017.
 */

public class Book implements Serializable{

    private String title;
    private String authors;
    private String imgUrl;
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



    public Book(String title, String authors,String imgUrl,String id) {
        this.title = title;
        this.authors = authors;
        this.imgUrl = imgUrl;
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
