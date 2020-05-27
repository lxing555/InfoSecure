package com.example.infosecure.entity;

import java.util.ArrayList;

public class Message{
    private String date;
    private String content;
    private String phone;
    private String name;
    private String mesId;
    private int type;    //1为接收，2为发送

    public Message(String name,String phone,String date,String content,String id,int type){
        this.date=date;
        this.content=content;
        this.name=name;
        this.phone=phone;
        this.mesId=id;
        this.type=type;
    }
    public Message(String name,String phone,String date,String content){
        this.date=date;
        this.content=content;
        this.name=name;
        this.phone=phone;
    }
    public Message(String phone,String date,String content,int type){
        this.date=date;
        this.content=content;
        this.type=type;
        this.phone=phone;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public String getMesId() {
        return mesId;
    }

    public void setMesId(String mesId) {
        this.mesId = mesId;
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
