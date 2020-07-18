package com.example.projectpro.model;

public class Post {
    String postid;
    String postimage;
    String publisher;
    String description;

    public Post(String postid, String postimage, String publisher, String description) {
        this.postid = postid;
        this.postimage = postimage;
        this.publisher = publisher;
        this.description = description;
    }

    public Post() {
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
