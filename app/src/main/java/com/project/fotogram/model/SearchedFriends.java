package com.project.fotogram.model;

import java.util.List;

public class SearchedFriends {

    private List<Friend> users;

    public List<Friend> getUsers() {
        return users;
    }

    public void setUsers(List<Friend> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "SearchedFriends{" +
                "users=" + users +
                '}';
    }
}
