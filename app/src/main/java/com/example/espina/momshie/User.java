package com.example.espina.momshie;



/**
 * Created by ESPINA on 10/1/2017.
 */

public class User  {
    String userKey;
    String userName;
    String userPhone;
    String userAge;
    String userBirth;
    String userUserName;
    String userID;

    public User() {
    }


    public User(String userID, String userKey, String userName, String userPhone, String userAge, String userBirth, String userUserName) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.userAge = userAge;
        this.userBirth = userBirth;
        this.userUserName = userUserName;
        this.userKey = userKey;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserKey() {
        return userKey;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserAge() {
        return userAge;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public String getUserUserName() {
        return userUserName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }

    public void setUserUserName(String userUserName) {
        this.userUserName = userUserName;
    }
}

