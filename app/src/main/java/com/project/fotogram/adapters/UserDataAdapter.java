package com.project.fotogram.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.fotogram.R;
import com.project.fotogram.model.Post;
import com.project.fotogram.model.SimplePost;
import com.project.fotogram.model.UserData;

import java.util.List;

public class UserDataAdapter extends ArrayAdapter<SimplePost> {

    private int postsLayout;
    private String username, userImg;

    public UserDataAdapter(@NonNull Context context, int resource, @NonNull String username, @NonNull String userImg, @NonNull List<SimplePost> objects) {
        super(context, resource, objects);
        this.username = username;
        this.userImg=userImg;
        this.postsLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View postsTemplate = convertView;
        if(postsTemplate==null){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            postsTemplate = layoutInflater.inflate(this.postsLayout, null);
        }
        SimplePost post = getItem(position);
        if(post!=null){
            TextView usernameView = (TextView)postsTemplate.findViewById(R.id.creatorUsername);
            TextView postCommentView = (TextView)postsTemplate.findViewById(R.id.postComment);
            ImageView postImageView = (ImageView)postsTemplate.findViewById(R.id.postImage);
            ImageView creatorProfileImageView = (ImageView)postsTemplate.findViewById(R.id.userProfileImage);

            usernameView.setText(username);
            usernameView.setTag(username);
            postCommentView.setText(post.getMsg());
            postCommentView.setTag(username);

            byte[] decodedPostImageString = Base64.decode(post.getImg(), Base64.DEFAULT);
            Bitmap decodedImageByte = BitmapFactory.decodeByteArray(decodedPostImageString, 0, decodedPostImageString.length);
            postImageView.setImageBitmap(decodedImageByte);
            postImageView.setTag(username);
            if(userImg!=null) {
                byte[] decodedProfileImageString = Base64.decode(this.userImg, Base64.DEFAULT);
                Bitmap decodedProfileByte = BitmapFactory.decodeByteArray(decodedProfileImageString, 0, decodedProfileImageString.length);
                creatorProfileImageView.setImageBitmap(decodedProfileByte);
            }
        }
        return postsTemplate;
    }
}
