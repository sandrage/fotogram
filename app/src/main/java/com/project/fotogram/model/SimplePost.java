package com.project.fotogram.model;

public class SimplePost {
    protected Integer id;
    protected String msg, img, timestamp;

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

    @Override
    public String toString() {
        return "SimplePost{" +
                "msg='" + msg + '\'' +
                ", img='" + img + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

}
