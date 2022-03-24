package com.aoservice.entities;

public class Article {

    private String id;
    private String author;
    private String url;
    private String cv;

    public Article() {
    }
    public Article(String id, String author, String url, String cv) {
        this.id = id;
        this.author = author;
        this.url = url;
        this.cv = cv;
    }
    public Article(String author, String url) {
        this.author = author;
        this.url = url;
    }



    public Article(String id, String author, String url) {
        this.id = id;
        this.author = author;
        this.url = url;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return ("[" + this.author + " " + this.url + "]");
    }

}
