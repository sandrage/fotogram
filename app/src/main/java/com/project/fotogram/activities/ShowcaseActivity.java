package com.project.fotogram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
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
import com.project.fotogram.model.SessionInfo;
import com.project.fotogram.utility.Constants;
import com.project.fotogram.utility.UtilityMethods;

public class ShowcaseActivity extends AppCompatActivity {
    private PostsList postsList;
    private ListView postsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcase);
        Log.d("fotogramLogs", "bacheca!");
        BottomNavigationItemView createPostImage = (BottomNavigationItemView) findViewById(R.id.action_createPost);
        BottomNavigationItemView ownProfile = (BottomNavigationItemView) findViewById(R.id.action_ownProfile);
        BottomNavigationItemView searchFriend = (BottomNavigationItemView) findViewById(R.id.action_searchFriend);
        ImageButton prefs = (ImageButton) findViewById(R.id.action_preferences);
        postsListView = (ListView) findViewById(R.id.postsList);

        //Request all the wall posts
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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UtilityMethods.manageCommunicationError(error);
            }
        });
        wallRequest.addParam("session_id", SessionInfo.getInstance().getSessionId(ShowcaseActivity.this));
        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(wallRequest);

        createPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createPostIntent = new Intent(ShowcaseActivity.this, PostCreationActivity.class);
                startActivity(createPostIntent);
            }
        });

        prefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prefsIntent = new Intent(ShowcaseActivity.this, PrefsActivity.class);
                startActivity(prefsIntent);
            }
        });

        ownProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ownProfile = new Intent(ShowcaseActivity.this, ProfileActivity.class);
                ownProfile.putExtra("username", SessionInfo.getInstance().getCurrentUsername(ShowcaseActivity.this));
                startActivity(ownProfile);
            }
        });

        searchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(ShowcaseActivity.this, SearchActivity.class);
                startActivity(search);
            }
        });
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
