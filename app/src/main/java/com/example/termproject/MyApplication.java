package com.example.termproject;

import android.app.Application;

import java.util.ArrayList;

public class MyApplication extends Application {
    private ArrayList<String> phone;
    public ArrayList<String> getPhone(){
        return phone;
    }
    public void setPhone(ArrayList<String> phone){
        this.phone = phone;
    }
}