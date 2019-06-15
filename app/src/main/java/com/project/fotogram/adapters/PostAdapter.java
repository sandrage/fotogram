package com.project.fotogram.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.fotogram.R;
import com.project.fotogram.model.Post;
import com.project.fotogram.model.SessionInfo;
import com.project.fotogram.utility.UtilityMethods;

import java.util.HashMap;
import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {
    private int postsLayout;
    private List<Post> posts;
    private Context context;

    public PostAdapter(@NonNull Context context, int resource, @NonNull List<Post> objects) {
        super(context, resource, objects);
        this.postsLayout = resource;
        this.posts = objects;
        this.context = context;
    }

    public PostAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public int getCount() {
        return posts != null ? posts.size() : 0;
    }

    @Override
    public Post getItem(int position) {
        return posts != null ? posts.get(position) : null;
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
        ImageView creatorProfileImageView = (ImageView) postsTemplate.findViewById(R.id.userProfileImage);
        ImageView postImageView = (ImageView) postsTemplate.findViewById(R.id.postImage);
        try {

            if (post != null) {

                String parsedString = UtilityMethods.formatDate(post.getTimestamp());

                TextView usernameView = (TextView) postsTemplate.findViewById(R.id.creatorUsername);
                TextView postCommentView = (TextView) postsTemplate.findViewById(R.id.postComment);
                TextView creationDateView = (TextView) postsTemplate.findViewById(R.id.showcase_creationdatevalue);

                HashMap<String, String> profilePhotos = SessionInfo.getInstance().getProfilePhotos();

                usernameView.setText(post.getUser());
                postCommentView.setText(post.getMsg());
                creationDateView.setText(parsedString);

                if (post.getImg() != null && !post.getImg().trim().equalsIgnoreCase("")) {
                    byte[] decodedPostImageString = Base64.decode(post.getImg(), Base64.DEFAULT);
                    Bitmap decodedImageByte = BitmapFactory.decodeByteArray(decodedPostImageString, 0, decodedPostImageString.length);
                    postImageView.setImageBitmap(decodedImageByte);
                } else {
                    postImageView.setImageBitmap(null);
                }
                String profilePhotoString = profilePhotos.get(post.getUser());
                Log.d("fotogramLogs", "profile photo: " + profilePhotoString + ", per l'utente: " + post.getUser());
                if (profilePhotos != null && profilePhotos.containsKey(post.getUser()) && profilePhotoString != null) {
                    byte[] decodedProfileImage = Base64.decode(profilePhotoString, Base64.DEFAULT);
                    Bitmap profilePhoto = BitmapFactory.decodeByteArray(decodedProfileImage, 0, decodedProfileImage.length);
                    creatorProfileImageView.setImageBitmap(UtilityMethods.getRoundedBitmapToDisplay(profilePhoto, profilePhoto.getWidth(), profilePhoto.getHeight()));
                } else {
                    creatorProfileImageView.setImageBitmap(null);
                    creatorProfileImageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_round_account_circle_24px, null));
                }
            } else {
                creatorProfileImageView.setImageBitmap(null);
                creatorProfileImageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ic_round_account_circle_24px, null));
            }
        } catch (Exception e) {
            Log.e("fotogramLogs", "Unexpected exception: ", e);
        }

        return postsTemplate;
    }
}
