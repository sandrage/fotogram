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
import com.project.fotogram.model.SessionInfo;

import java.util.HashMap;
import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {
    private int postsLayout;

    public PostAdapter(@NonNull Context context, int resource, @NonNull List<Post> objects) {
        super(context, resource, objects);
        this.postsLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View postsTemplate = convertView;
        if (postsTemplate == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            postsTemplate = layoutInflater.inflate(this.postsLayout, null);
        }
        Post post = getItem(position);
        HashMap<String, String> profilePhotos = SessionInfo.getInstance().getProfilePhotos();
        if (post != null) {
            TextView usernameView = (TextView) postsTemplate.findViewById(R.id.creatorUsername);
            TextView postCommentView = (TextView) postsTemplate.findViewById(R.id.postComment);
            ImageView postImageView = (ImageView) postsTemplate.findViewById(R.id.postImage);
            ImageView creatorProfileImageView = (ImageView) postsTemplate.findViewById(R.id.userProfileImage);

            usernameView.setText(post.getUser());
            usernameView.setTag(post.getUser());
            postCommentView.setText(post.getMsg());
            postCommentView.setTag(post.getUser());

            byte[] decodedPostImageString = Base64.decode(post.getImg(), Base64.DEFAULT);
            Bitmap decodedImageByte = BitmapFactory.decodeByteArray(decodedPostImageString, 0, decodedPostImageString.length);
            postImageView.setImageBitmap(decodedImageByte);
            postImageView.setTag(post.getUser());

            if (profilePhotos != null && profilePhotos.containsKey(post.getUser())) {
                byte[] decodedProfileImage = Base64.decode(profilePhotos.get(post.getUser()), Base64.DEFAULT);
                Bitmap decodedProfileImageByte = BitmapFactory.decodeByteArray(decodedProfileImage, 0, decodedProfileImage.length);
                creatorProfileImageView.setImageBitmap(decodedProfileImageByte);
                creatorProfileImageView.setTag(post.getUser());
            } else {
                //TODO dovrei mettergli la X magari
            }
        }
        return postsTemplate;
    }
}
