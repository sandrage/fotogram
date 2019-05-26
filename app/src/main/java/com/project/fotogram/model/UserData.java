package com.project.fotogram.model;

import java.util.List;

public class UserData {
    private String username, img;
    private List<SimplePost> posts;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<SimplePost> getPosts() {
        return posts;
    }

    public void setPosts(List<SimplePost> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "username='" + username + '\'' +
                ", img='" + img + '\'' +
                ", posts=" + posts +
                '}';
    }


}
