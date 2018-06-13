package com.gxg.entities;

import org.json.JSONObject;

/**
 * Created by 郭欣光 on 2018/5/29.
 */


public class User {

    private String userId;
    private String phoneNumber;
    private String password;
    private String name;
    private String gender;
    private String birth;
    private String headPortrait;

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("phone_number", phoneNumber);
        jsonObject.accumulate("password", password);
        jsonObject.accumulate("name", name);
        jsonObject.accumulate("gender", gender);
        jsonObject.accumulate("birth", birth);
        jsonObject.accumulate("head_portrait", headPortrait);
        jsonObject.accumulate("user_id", userId);
        return jsonObject;
    }
}
