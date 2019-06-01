package com.project.fotogram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.project.fotogram.R;
import com.project.fotogram.adapters.PostAdapter;
import com.project.fotogram.communication.RequestWithParams;
import com.project.fotogram.communication.VolleySingleton;
import com.project.fotogram.model.PostsList;
import com.project.fotogram.model.SearchedFriends;
import com.project.fotogram.model.SessionInfo;
import com.project.fotogram.utility.Constants;
import com.project.fotogram.utility.UtilityMethods;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ShowcaseActivity extends AppCompatActivity {
    private PostsList postsList;
    private ListView postsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcase);
        Log.d("fotogramLogs", "bacheca!");
        ImageButton createPostImage = (ImageButton) findViewById(R.id.action_createPost);
        ImageButton ownProfile = (ImageButton) findViewById(R.id.action_ownProfile);
        ImageButton searchFriend = (ImageButton) findViewById(R.id.action_searchFriend);
        postsListView = (ListView) findViewById(R.id.postsList);

        //Request all the wall posts
        updateWall(true);

        createPostImage.setOnClickListener(getMenuOnClickListener());
        ownProfile.setOnClickListener(getMenuOnClickListener());
        searchFriend.setOnClickListener(getMenuOnClickListener());
    }

    public View.OnClickListener getMenuOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.action_createPost:
                        Intent createPostIntent = new Intent(ShowcaseActivity.this, PostCreationActivity.class);
                        startActivity(createPostIntent);
                        break;
                    case R.id.action_ownProfile:
                        Intent ownProfile = new Intent(ShowcaseActivity.this, ProfileActivity.class);
                        ownProfile.putExtra("username", SessionInfo.getInstance().getCurrentUsername(ShowcaseActivity.this));
                        startActivity(ownProfile);
                        break;
                    case R.id.action_searchFriend:
                        Intent search = new Intent(ShowcaseActivity.this, SearchActivity.class);
                        startActivity(search);
                        break;
                }
            }
        };
    }

    private void updateWall(boolean withProfilePhotos) {
        RequestWithParams wallRequest = new RequestWithParams(Request.Method.POST, Constants.BASEURL + "wall", new Response.Listener<String>() {
            @Override
            public void onResponse(String mess) {
                Log.d("fotogramLogs", "messaggio: " + mess);
                Gson gson = new Gson();
                PostsList posts = gson.fromJson(mess, PostsList.class);
                postsList = posts;
                //display them through the adapter
                PostAdapter postAdapter = new PostAdapter(ShowcaseActivity.this, R.layout.posts_list_element, postsList.getPosts());
                postsListView.setAdapter(postAdapter);
                //Updating the profile photos associated to the users creators of the posts returned
                //Actually, I could ask for all the photos of my followed friends but here I only get 10 posts, so it would be an unecessary effort
                if (withProfilePhotos) {
                    Set<String> userNames = posts.getPosts() != null ? posts.getPosts().stream().map(post -> post.getUser()).collect(Collectors.toSet()) : new HashSet<>();
                    Log.d("fotogramLogs", "users per cui chiamo la profile: " + userNames);
                    userNames.stream().distinct().forEach(user -> {
                        RequestWithParams profilePhotoReq = new RequestWithParams(Request.Method.POST, Constants.BASEURL + "users", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String mess) {
                                //TODO SINCRONIZZARE e lockare prima di postadapter nell'attesa che tutti questi thread finiscano!
                                SearchedFriends profileInfo = gson.fromJson(mess, SearchedFriends.class);
                                SessionInfo.getInstance().addProfilePhotos(profileInfo.getUsers().get(0));
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                UtilityMethods.manageCommunicationError(error);
                            }
                        });
                        profilePhotoReq.addParam("session_id", SessionInfo.getInstance().getSessionId(ShowcaseActivity.this));
                        profilePhotoReq.addParam("usernamestart", user);
                        profilePhotoReq.addParam("limit", "1");
                        VolleySingleton.getInstance(ShowcaseActivity.this).addToRequestQueue(profilePhotoReq);
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UtilityMethods.manageCommunicationError(error);
            }
        });
        wallRequest.addParam("session_id", SessionInfo.getInstance().getSessionId(ShowcaseActivity.this));
        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(wallRequest);
    }

    public void viewUserProfile(View v) {
        View usernameTextView = findViewById(v.getId());
        Log.d("fotogramLogs", "view: " + usernameTextView + ", " + usernameTextView.getTag());
        String username = (String) usernameTextView.getTag();
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        profileIntent.putExtra("username", username);
        startActivity(profileIntent);
    }


}
