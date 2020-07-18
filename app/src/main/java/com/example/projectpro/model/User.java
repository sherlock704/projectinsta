package com.example.projectpro.model;



public class User {
    public String id,username,bio,imageUrl;

    public User() {
    }

    public User(String id, String username, String bio, String imageUrl) {
        this.id = id;
        this.username = username;
        this.bio = bio;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
