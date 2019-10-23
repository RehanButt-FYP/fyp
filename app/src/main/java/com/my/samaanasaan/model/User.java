package com.my.samaanasaan.model;

public class User {


    String Name ;
    String userPhone;
    String userCnic;

    public void setName(String name) {
        Name = name;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserCnic(String userCnic) {
        this.userCnic = userCnic;
    }

    public String getName() {
        return Name;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserCnic() {
        return userCnic;
    }

    public User(String name, String userPhone, String userCnic) {
        Name = name;
        this.userPhone = userPhone;
        this.userCnic = userCnic;
    }

    public User() {
    }
}
