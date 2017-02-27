package com.example.coderqiang.followme.Model;

/**
 * Created by CoderQiang on 2017/2/27.
 */

public class FMUser {
    int id;
    String userName;
    String nickName;
    String signature;
    String address;
    String phone;
    String email;
    long birthDay;
    int sex;
    int concern;
    int travle;
    int follower;

    public FMUser(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(long birthDay) {
        this.birthDay = birthDay;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getConcern() {
        return concern;
    }

    public void setConcern(int concern) {
        this.concern = concern;
    }

    public int getTravle() {
        return travle;
    }

    public void setTravle(int travle) {
        this.travle = travle;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }
}
