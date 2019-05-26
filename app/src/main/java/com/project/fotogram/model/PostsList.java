package com.project.fotogram.model;

import java.util.List;

public class PostsList {
    private List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "PostsList{" +
                "posts=" + posts +
                '}';
    }
}
