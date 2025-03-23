package com.example.bt3_loginregister.model;

public class Otp {
    private String email;
    private String otp;

    public Otp(String email) {
        this.email = email;
    }

    public Otp(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}