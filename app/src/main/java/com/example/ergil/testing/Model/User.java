package com.example.ergil.testing.Model;

public class User {
    private String name;
    private String lastname;
    private String email;
    private String created_at;
    private String updated_at = "";
    private String token;
    private int logged;

    public User() {

    }

    public User(String name, String lastname, String email, String created_at, String updated_at, String token, int logged) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.token = token;
        this.logged = logged;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getLoggedInfo() {
        return logged;
    }

    public void setLogged (boolean isLogged) {
        this.logged=logged;
    }
}


