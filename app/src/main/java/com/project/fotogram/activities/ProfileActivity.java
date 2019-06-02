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
import com.project.fotogram.dialogs.MyDialog;
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
        ImageButton createPostImage = (ImageButton) findViewById(R.id.action_createPost);
        ImageButton ownProfile = (ImageButton) findViewById(R.id.action_ownProfile);
        ImageButton searchFriend = (ImageButton) findViewById(R.id.action_searchFriend);
        ImageButton goBack = (ImageButton) findViewById(R.id.action_goBack);
        createPostImage.setOnClickListener(getMenuOnClickListener());
        ownProfile.setOnClickListener(getMenuOnClickListener());
        searchFriend.setOnClickListener(getMenuOnClickListener());
        goBack.setOnClickListener(getMenuOnClickListener());

        this.username = getIntent().getStringExtra("username");
        listView = (ListView) findViewById(R.id.profilePostsList);
        profileUsernameView = (TextView) findViewById(R.id.profileUsername);
        profileImgView = (ImageView) findViewById(R.id.profileImage);
        SessionInfo sessionInfo = SessionInfo.getInstance();
        String currentUsername = sessionInfo.getCurrentUsername(this);
        if (this.username.equals(currentUsername)) {
            ImageButton prefs = (ImageButton) findViewById(R.id.action_preferences);
            prefs.setVisibility(View.VISIBLE);
            prefs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent prefsIntent = new Intent(ProfileActivity.this, PrefsActivity.class);
                    startActivity(prefsIntent);
                }
            });
            profileImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("fotogramLog", "update photo");
                    Intent updatePhotoIntent = new Intent(ProfileActivity.this, ProfilePhotoUpdateActivity.class);
                    startActivity(updatePhotoIntent);
                }

            });
        }

        retrieveProfileInfo();

    }

    public View.OnClickListener getMenuOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.action_createPost:
                        Intent createPostIntent = new Intent(ProfileActivity.this, PostCreationActivity.class);
                        startActivity(createPostIntent);
                        break;
                    case R.id.action_ownProfile:
                        Intent ownProfile = new Intent(ProfileActivity.this, ProfileActivity.class);
                        ownProfile.putExtra("username", SessionInfo.getInstance().getCurrentUsername(ProfileActivity.this));
                        startActivity(ownProfile);
                        break;
                    case R.id.action_searchFriend:
                        Intent search = new Intent(ProfileActivity.this, SearchActivity.class);
                        startActivity(search);
                        break;
                    case R.id.action_goBack:
                        finish();
                        break;
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        //todo MA DOVREI DAVVERO FARLO ALLA RESUME? LO FA DUE VOLTE SE NO // o farlo solo alla create
        retrieveProfileInfo();
    }

    private void retrieveProfileInfo() {
        RequestWithParams profileRequest = new RequestWithParams(Request.Method.POST, Constants.BASEURL + "profile", mess -> {
            Log.d("fotogramLogs", "messaggio: " + mess);
            Gson gson = new Gson();
            UserData user = gson.fromJson(mess, UserData.class);
            profileUsernameView.setText(user.getUsername());
            if (user.getImg() != null) {
                byte[] decodedPostImageString = Base64.decode(user.getImg(), Base64.DEFAULT);
                Bitmap decodedImageByte = BitmapFactory.decodeByteArray(decodedPostImageString, 0, decodedPostImageString.length);
                profileImgView.setImageBitmap(decodedImageByte);
                profileImgView.setBackgroundResource(R.drawable.rounded_button);
            }
            //display them through the adapter
            UserDataAdapter userDataAdapter = new UserDataAdapter(ProfileActivity.this, R.layout.profile_list_element, user.getPosts());
            listView.setAdapter(userDataAdapter);
        }, error -> UtilityMethods.manageCommunicationError(this, error));
        profileRequest.addParam("session_id", SessionInfo.getInstance().getSessionId(ProfileActivity.this));
        profileRequest.addParam("username", username);
        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(profileRequest);
    }

    public void followTheFriend(View v) {
        Log.d("fotogramLog", "follow the friend");
        RequestWithParams followRequest = new RequestWithParams(Request.Method.POST, Constants.BASEURL + "follow", mess -> {
            SessionInfo.getInstance().updateFollowedFriends(ProfileActivity.this);
            MyDialog alert = new MyDialog();
            alert.setMsg("You are now following this user!");
            alert.show(getSupportFragmentManager(), "MyDialog");
        }, error -> {
            UtilityMethods.manageCommunicationError(this, error);
        });
        followRequest.addParam("session_id", SessionInfo.getInstance().getSessionId(ProfileActivity.this));
        followRequest.addParam("username", this.username);
        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(followRequest);
    }

    public void unfollowTheFriend(View v) {
        Log.d("fotogramLog", "unfollow the friend");
        RequestWithParams unfollowRequest = new RequestWithParams(Request.Method.POST, Constants.BASEURL + "unfollow", mess -> {
            SessionInfo.getInstance().updateFollowedFriends(ProfileActivity.this);
            MyDialog alert = new MyDialog();
            alert.setMsg("You are not following this user anymore!");
            alert.show(getSupportFragmentManager(), "MyDialog");
        }, error -> {
            Log.d("fotogramLogs", "error: " + new String(error.networkResponse.data));
            UtilityMethods.manageCommunicationError(this, error);
        });
        unfollowRequest.addParam("session_id", SessionInfo.getInstance().getSessionId(ProfileActivity.this));
        unfollowRequest.addParam("username", this.username);
        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(unfollowRequest);
    }

}
