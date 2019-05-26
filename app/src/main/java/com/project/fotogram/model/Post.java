package com.project.fotogram.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Post extends SimplePost{
    private String user;


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Post(String user, String postMessage, String msg, String postImage) {
        this.user = user;
        this.msg = postMessage;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Post{" +
                "user='" + user + '\'' +
                ", msg='" + msg + '\'' +
                ", img='" + img + '\'' +
                ", timestamp=" + timestamp +
                ", id=" + id +
                '}';
    }
}
