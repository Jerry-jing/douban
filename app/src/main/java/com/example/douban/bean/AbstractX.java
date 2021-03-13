package com.example.douban.bean;

import com.google.gson.annotations.SerializedName;

public class AbstractX {


    /*"作者: 幾米",
    "出版社: 辽宁教育出版社",
    "出版年: 2002-2"*/


    @SerializedName("作者")
    public String author;

    @SerializedName("出版社")
    public String press;

    @SerializedName("出版年")
    public String publicationYear;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }
}
