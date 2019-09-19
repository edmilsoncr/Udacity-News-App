package com.edmilson.newsapp;

import android.graphics.Bitmap;

public class News {

    private String mTitle;
    private String mSection;
    private String mAuthor;
    private String mPublicationDate;
    private Bitmap mBitmap;
    private String mNewsUrl;

    /**
     * @param title           is the news title
     * @param section         is the News section
     * @param author          is the name of the news writer
     * @param publicationDate is the publication date
     * @param bitmap          is the news image resource ID
     * @param newsUrl         is the news URL
     */
    public News(String title, String section, String author, String publicationDate, Bitmap bitmap, String newsUrl) {
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mPublicationDate = publicationDate;
        mBitmap = bitmap;
        mNewsUrl = newsUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public String getNewsUrl() {
        return mNewsUrl;
    }

    public boolean hasAuthorName() {
        return !mAuthor.equals(null);
    }

    public boolean hasPublishedDate() {
        return !mPublicationDate.equals(null);
    }

    @Override
    public String toString() {
        return "News{" +
                "Title='" + mTitle + '\'' +
                ", Section='" + mSection + '\'' +
                ", Author='" + mAuthor + '\'' +
                ", PublicationDate='" + mPublicationDate + '\'' +
                ", NewsUrl='" + mNewsUrl + '\'' +
                '}';
    }
}
