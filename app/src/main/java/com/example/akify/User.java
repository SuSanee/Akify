package com.example.akify;

public class User {

    private String name;
    private String id;
    private String email;
    private String pswd;

    User(String name, String id, String email, String pswd){
        this.name = name;
        this.id = id;
        this.email = email;
        this.pswd = pswd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }
}
