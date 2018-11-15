package com.ephoenixdev.izgubljenonadjeno.models;

public class FoundModel {

    private String foundId;
    private String userId;
    private String title;
    private String place;
    private String phone;
    private String description;
    private String image;
    private String date;

    public FoundModel(){

    }

    public FoundModel(String foundId, String userId, String title, String place, String phone, String description, String image, String date) {
        this.foundId = foundId;
        this.userId = userId;
        this.title = title;
        this.place = place;
        this.phone = phone;
        this.description = description;
        this.image = image;
        this.date = date;
    }

    public String getFoundId() {
        return foundId;
    }

    public void setFoundId(String foundId) {
        this.foundId = foundId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
