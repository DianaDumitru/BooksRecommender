package com.example.diana.booksrecommender;

/**
 * Created by Diana on 4/30/2017.
 */

public class Book {

    private String title;
    private String authors;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private String imgUrl;

    public Book(String title, String authors,String imgUrl) {
        this.title = title;
        this.authors = authors;
        this.imgUrl = imgUrl;
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
}
