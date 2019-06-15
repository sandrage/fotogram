package com.project.fotogram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.project.fotogram.R;
import com.project.fotogram.model.SessionInfo;
import com.project.fotogram.sync.PostsListRetrievalAsyncTask;

public class ShowcaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcase);
        Log.d("fotogramLogs", "bacheca!");
        ImageButton createPostImage = (ImageButton) findViewById(R.id.action_createPost);
        ImageButton ownProfile = (ImageButton) findViewById(R.id.action_ownProfile);
        ImageButton searchFriend = (ImageButton) findViewById(R.id.action_searchFriend);
        ImageButton dashboard = (ImageButton) findViewById(R.id.action_showcase);

        createPostImage.setOnClickListener(getMenuOnClickListener());
        ownProfile.setOnClickListener(getMenuOnClickListener());
        searchFriend.setOnClickListener(getMenuOnClickListener());
        dashboard.setOnClickListener(getMenuOnClickListener());

        dashboard.setPressed(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWall();
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
                    case R.id.action_showcase:
                        Intent dashb = new Intent(ShowcaseActivity.this, ShowcaseActivity.class);
                        startActivity(dashb);
                        break;
                    /*case R.id.action_goBack:
                        finish();
                        break;*/
                }
            }
        };
    }

    private void updateWall() {
        new PostsListRetrievalAsyncTask(ShowcaseActivity.this).execute();
    }
}
