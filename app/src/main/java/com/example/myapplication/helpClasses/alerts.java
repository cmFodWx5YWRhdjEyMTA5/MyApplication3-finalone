package com.example.myapplication.helpClasses;

public class alerts {

    private int activeStatus;
    private String context;
    private String createdDate;
    private String createdTime;
    private String sender_uid;
    private String title;
    private String UID;
    private String token;
    private String profile_image;

    public alerts() {
    }


    public alerts(int activeStatus, String context, String createdDate, String createdTime, String sender_uid, String title, String UID, String token, String profile_image) {
        this.activeStatus = activeStatus;
        this.context = context;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
        this.sender_uid = sender_uid;
        this.title = title;
        this.UID = UID;
        this.token = token;
        this.profile_image = profile_image;
    }


    public int getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(int activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
