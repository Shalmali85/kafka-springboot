package com.kindred.kafka.jsonEntity;

public class MobileClickStreamEntity {

    private String userId;
    private String userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public MobileClickStreamEntity(String userId,String userName){
        this.userId=userId;
        this.userName=userName;

    }
public MobileClickStreamEntity(){}
    @Override
    public String toString() {
        return "MobileClickStreamEntity{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
