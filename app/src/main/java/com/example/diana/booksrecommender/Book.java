package com.example.diana.booksrecommender;

import java.io.Serializable;

/**
 * Created by Diana on 4/30/2017.
 */

public class Book implements Serializable{

    private String mTitle;
    private String mAuthor;
    private String mImgUrl;
    private Double mAverageRating;

    private int mPageCount;
    private String mDescription;
    private String[] mCategories;
    private String mSelfLink;
    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getImgUrl() {
        return mImgUrl;
    }

    public Book(String title, String author, String imgUrl, Double averageRating, int pageCount, String description, String[] categories, String selfLink, String id) {
        this.mTitle = title;
        this.mAuthor = author;
        this.mImgUrl = imgUrl;
        this.mAverageRating = (averageRating==0)?3.2:averageRating;

        this.mPageCount = pageCount;
        this.mDescription = description;
        if (categories == null)
        {
            this.mCategories = new String[1];
            this.mCategories[0] = "NO CATEGORY";
        }else
            this.mCategories = categories;

        this.mSelfLink = selfLink;
        this.id = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public Double getAverageRating() {
        return mAverageRating;
    }


    public int getPageCount() {
        return mPageCount;
    }


    public String getDescription() {
        return mDescription;
    }


    public String[] getCategories() {
        return mCategories;
    }


    public String getSelfLink() {
        return mSelfLink;
    }


    @Override
    public String toString() {
        return "Book{" +
                "mTitle='" + mTitle + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mImgUrl='" + mImgUrl + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
