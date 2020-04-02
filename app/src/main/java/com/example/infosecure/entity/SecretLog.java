package com.example.infosecure.entity;

import java.util.ArrayList;

public class SecretLog {
    private String title;
    private String content;
    private String logId;
    private String time;
    public ArrayList<String>images_base64=new ArrayList<String>();
    public ArrayList<String>images_encrypt=new ArrayList<String>();
    public SecretLog(String title,String content,String time,String logId){
        this.title=title;
        this.content=content;
        this.time=time;
        this.logId=logId;
    }
    public SecretLog(){}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

}
