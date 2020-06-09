package com.googleinterns.smb;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("user_id")
    private String userId;

    public String getUserId() {
        return this.userId;
    }
}