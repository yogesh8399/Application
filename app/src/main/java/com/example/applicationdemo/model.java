package com.example.applicationdemo;

public class model {
    int age;
    String country,fname,gender,lname,url;

    public model(int age, String country, String fname, String gender, String lname, String url) {
        this.age = age;
        this.country = country;
        this.fname = fname;
        this.gender = gender;
        this.lname = lname;
        this.url = url;
    }

    public model() {
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
