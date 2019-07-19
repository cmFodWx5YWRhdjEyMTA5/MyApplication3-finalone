package com.example.myapplication.helpClasses;

public class userInquiry {

    private String id;
private String disasterType;
private String priorityType;
private String location;
private String Desc;
private String createdDate;
private String createdUser;
private String updatedDate;
private String updateUser;
private int activeStatus;
private String status;
private String token;
private String New;
private String logtitude;
private String latitude;
private String additionalFeedback;
private String createdTime;
private String updatedTime;
private int web_status;


public userInquiry(){ }

    public userInquiry(String id, String disasterType, String priorityType, String location, String desc, String createdDate, String createdUser, String updatedDate, String updateUser, int activeStatus, String status, String token, String aNew, String logtitude, String latitude, String additionalFeedback, String createdTime, String updatedTime, int web_status) {
        this.id = id;
        this.disasterType = disasterType;
        this.priorityType = priorityType;
        this.location = location;
        Desc = desc;
        this.createdDate = createdDate;
        this.createdUser = createdUser;
        this.updatedDate = updatedDate;
        this.updateUser = updateUser;
        this.activeStatus = activeStatus;
        this.status = status;
        this.token = token;
        New = aNew;
        this.logtitude = logtitude;
        this.latitude = latitude;
        this.additionalFeedback = additionalFeedback;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.web_status = web_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(String disasterType) {
        this.disasterType = disasterType;
    }

    public String getPriorityType() {
        return priorityType;
    }

    public void setPriorityType(String priorityType) {
        this.priorityType = priorityType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public int getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(int activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNew() {
        return New;
    }

    public void setNew(String aNew) {
        New = aNew;
    }

    public String getLogtitude() {
        return logtitude;
    }

    public void setLogtitude(String logtitude) {
        this.logtitude = logtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAdditionalFeedback() {
        return additionalFeedback;
    }

    public void setAdditionalFeedback(String additionalFeedback) {
        this.additionalFeedback = additionalFeedback;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public int getWeb_status() {
        return web_status;
    }

    public void setWeb_status(int web_status) {
        this.web_status = web_status;
    }
}
