package com.googleinterns.smb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.PropertyName;


public class User {

    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("user_address")
    private String userAddress;
    @JsonProperty("user_contactNo")
    private String userContactNo;


    public User() {

    }

    public User(String userId) {
        this.userId = userId;
    }

    public User(String userId, String userAddress, String userContactNo) {
        this.userId = userId;
        this.userAddress=userAddress;
        this.userContactNo=userContactNo;
    }

    @PropertyName("user_id")
    public String getUserId() {
        return userId;
    }

    @PropertyName("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @PropertyName("user_address")
    public String getUserAddress() {
        return userAddress;
    }

    @PropertyName("user_address")
    public void setUserAddress(String userAddress) {
        this.userAddress=userAddress;
    }

    @PropertyName("user_contactNo")
    public String getUserContactNo() {
        return userContactNo;
    }

    @PropertyName("user_contactNo")
    public void setUserContactNo(String contactNo) {
        this.userContactNo=contactNo;
    }

}