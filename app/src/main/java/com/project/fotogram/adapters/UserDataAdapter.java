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
import com.project.fotogram.model.SimplePost;

import java.util.List;

public class UserDataAdapter extends ArrayAdapter<SimplePost> {

    private int postsLayout;

    public UserDataAdapter(@NonNull Context context, int resource, @NonNull List<SimplePost> objects) {
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
        SimplePost post = getItem(position);
        if (post != null) {
            TextView postCommentView = (TextView) postsTemplate.findViewById(R.id.profile_postComment);
            ImageView postImageView = (ImageView) postsTemplate.findViewById(R.id.profile_postImage);

            postCommentView.setText(post.getMsg());

            byte[] decodedPostImageString = Base64.decode(post.getImg(), Base64.DEFAULT);
            Bitmap decodedImageByte = BitmapFactory.decodeByteArray(decodedPostImageString, 0, decodedPostImageString.length);
            postImageView.setImageBitmap(decodedImageByte);
        }
        return postsTemplate;
    }
}
