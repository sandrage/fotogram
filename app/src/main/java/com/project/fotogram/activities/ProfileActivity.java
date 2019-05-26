package com.project.fotogram.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.project.fotogram.R;
import com.project.fotogram.adapters.UserDataAdapter;
import com.project.fotogram.communication.RequestWithParams;
import com.project.fotogram.communication.VolleySingleton;
import com.project.fotogram.model.SessionInfo;
import com.project.fotogram.model.UserData;
import com.project.fotogram.utility.Constants;
import com.project.fotogram.utility.UtilityMethods;

public class ProfileActivity extends AppCompatActivity {
    ListView listView;
    TextView profileUsernameView;
    ImageView profileImgView;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.username = getIntent().getStringExtra("username");
        listView = (ListView) findViewById(R.id.profilePostsList);
        profileUsernameView = (TextView) findViewById(R.id.profileUsername);
        profileImgView = (ImageView) findViewById(R.id.profileImage);
        SessionInfo sessionInfo = SessionInfo.getInstance();
        String currentUsername = sessionInfo.getCurrentUsername(this);
        if (!this.username.equals(currentUsername) && !sessionInfo.getFollowedFriends().containsByName(username)) {
            ImageButton followFriend = (ImageButton) findViewById(R.id.follow_user);
            followFriend.setVisibility(View.VISIBLE);
        } else if (!this.username.equals(currentUsername)) {
            ImageButton unfollowFriend = (ImageButton) findViewById(R.id.unfollow_user);
            unfollowFriend.setVisibility(View.VISIBLE);
        } else if (this.username.equals(currentUsername)) {
            ImageButton updateMyPicture = (ImageButton) findViewById(R.id.update_picture);
            updateMyPicture.setVisibility(View.VISIBLE);
        }
        RequestWithParams profileRequest = new RequestWithParams(Request.Method.POST, Constants.BASEURL + "profile", mess -> {
            Log.d("fotogramLogs", "messaggio: " + mess);
            Gson gson = new Gson();
            UserData user = gson.fromJson(mess, UserData.class);
            profileUsernameView.setText(user.getUsername());
            if (user.getImg() != null) {
                byte[] decodedPostImageString = Base64.decode(user.getImg(), Base64.DEFAULT);
                Bitmap decodedImageByte = BitmapFactory.decodeByteArray(decodedPostImageString, 0, decodedPostImageString.length);
                profileImgView.setImageBitmap(decodedImageByte);
            }
            //display them through the adapter
            UserDataAdapter userDataAdapter = new UserDataAdapter(ProfileActivity.this, R.layout.posts_list_element, user.getUsername(), user.getImg(), user.getPosts());
            listView.setAdapter(userDataAdapter);
        }, error -> UtilityMethods.manageCommunicationError(error));
        profileRequest.addParam("session_id", SessionInfo.getInstance().getSessionId(ProfileActivity.this));
        profileRequest.addParam("username", username);
        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(profileRequest);
    }

    public void followTheFriend(View v) {
        Log.d("fotogramLog", "follow the friend");
        RequestWithParams followRequest = new RequestWithParams(Request.Method.POST, Constants.BASEURL + "follow", mess -> {
            SessionInfo.getInstance().updateFollowedFriends(ProfileActivity.this);
        }, error -> UtilityMethods.manageCommunicationError(error));
        followRequest.addParam("session_id", SessionInfo.getInstance().getSessionId(ProfileActivity.this));
        followRequest.addParam("username", this.username);
        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(followRequest);
    }

    public void unfollowTheFriend(View v) {
        Log.d("fotogramLog", "unfollow the friend");
        RequestWithParams unfollowRequest = new RequestWithParams(Request.Method.POST, Constants.BASEURL + "unfollow", mess -> {
            SessionInfo.getInstance().updateFollowedFriends(ProfileActivity.this);
        }, error -> UtilityMethods.manageCommunicationError(error));
        unfollowRequest.addParam("session_id", SessionInfo.getInstance().getSessionId(ProfileActivity.this));
        unfollowRequest.addParam("username", this.username);
        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(unfollowRequest);
    }

    public void updateMyPhoto(View v) {
        Log.d("fotogramLog", "update photo");
        Intent updatePhotoIntent = new Intent(ProfileActivity.this, ProfilePhotoUpdateActivity.class);
        startActivity(updatePhotoIntent);
    }

}
