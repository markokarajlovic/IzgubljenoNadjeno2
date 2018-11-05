package com.ephoenixdev.izgubljenonadjeno.models;

public class LostModel {

    private String lostId;
    private String place;
    private String phone;
    private String description;
    private String image;
    private String date;

    public LostModel(){

    }

    public LostModel(String lostId, String place, String phone, String description, String image, String date) {
        this.lostId = lostId;
        this.place = place;
        this.phone = phone;
        this.description = description;
        this.image = image;
        this.date = date;
    }

    public String getLostId() {
        return lostId;
    }

    public void setLostId(String lostId) {
        this.lostId = lostId;
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
}
