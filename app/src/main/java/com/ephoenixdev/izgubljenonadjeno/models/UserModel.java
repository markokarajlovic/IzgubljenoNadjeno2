package com.ephoenixdev.izgubljenonadjeno.models;

public class UserModel {

    private String userId;
    private String image;

    public UserModel() {
    }

    public UserModel(String userId, String image) {
        this.userId = userId;
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
