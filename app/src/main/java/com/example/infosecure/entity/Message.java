package com.example.infosecure.entity;

import java.util.ArrayList;

public class Message{
    private String date;
    private String content;
    private String phone;
    private String name;

    public Message(String name,String phone,String date,String content){
        this.date=date;
        this.content=content;
        this.name=name;
        this.phone=phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
