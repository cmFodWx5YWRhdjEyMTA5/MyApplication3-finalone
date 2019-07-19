package com.example.myapplication.helpClasses;

public class myInquiry {





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
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String createdTime;
    private String updatedTime;

    public myInquiry() {
    }

    public myInquiry(String id, String disasterType, String priorityType, String location, String desc, String createdDate, String createdUser, String updatedDate, String updateUser, int activeStatus, String status, String token, String aNew, String logtitude, String latitude, String additionalFeedback, String img1, String img2, String img3, String img4, String createdTime, String updatedTime) {
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
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.img4 = img4;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
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

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
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
}
