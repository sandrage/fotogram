package com.project.fotogram.model;

import java.util.List;

public class FollowedFriends {
    private List<Friend> followed;

    public List<Friend> getFollowed() {
        return followed;
    }

    public void setFollowed(List<Friend> followed) {
        this.followed = followed;
    }

    @Override
    public String toString() {
        return "FollowedFriends{" +
                "followed=" + followed +
                '}';
    }

    public boolean containsByName(String name){
        if(followed==null){
            return false;
        }
        return followed.stream().anyMatch(friend -> friend.getName().equals(name));
    }
}
