package com.gxg.entities;

import org.json.JSONObject;

/**
 * Created by 郭欣光 on 2018/6/1.
 */

public class AdminUser {
    private String id;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("id", id);
        jsonObject.accumulate("password", password);
        return jsonObject;
    }
}
