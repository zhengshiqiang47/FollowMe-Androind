package com.example.coderqiang.followme.Model;

/**
 * Created by CoderQiang on 2017/2/27.
 */

public class FMUser {

    /**
     * id : 8
     * userName : zhengshiqiang
     * password : zsqqq1996424
     * nickName : CoderQiang
     * signature : 很懒,什么都没写
     * birthDay : 0
     * sex : 0
     * concern : 0
     * travle : 0
     * follower : 1
     */

    private int id;
    private String userName;
    private String password;
    private String nickName;
    private String signature;
    String phone;
    String email;
    String city;
    private long birthDay;
    private int sex;
    private int concern;
    private int travle;
    private int follower;
    private int dynamicIndex=1;
    private int distance;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getDynamicIndex() {
        return dynamicIndex;
    }

    public void setDynamicIndex(int dynamicIndex) {
        this.dynamicIndex = dynamicIndex;
    }

    public void addDynamicIndex(){
        this.dynamicIndex++;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(long birthDay) {
        this.birthDay = birthDay;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
