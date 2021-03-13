package com.example.douban.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Book {

    /**
     * id : 1021847
     * title : 我的心中每天开出一朵花
     * img : https://img9.doubanio.com/view/subject/l/public/s1150266.jpg
     * score : 8.5
     * people : (23042人评价)
     * abstract : ["作者: 幾米","出版社: 辽宁教育出版社","出版年: 2002-2"]
     * time : 2011年3月22日
     * order_num : 0
     */

    private String id;
    private String title;
    private String img;
    private String score;
    private String people;
    private String time;
    private int order_num;
    @SerializedName("abstract")
    public AbstractX abstractX;

    public Book(String id, String title, String img, String score, String people, String time, int order_num, AbstractX abstractX) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.score = score;
        this.people = people;
        this.time = time;
        this.order_num = order_num;
        this.abstractX = abstractX;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public AbstractX getAbstractX() {
        return abstractX;
    }

    public void setAbstractX(AbstractX abstractX) {
        this.abstractX = abstractX;
    }
}
