package com.project.fotogram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.project.fotogram.R;
import com.project.fotogram.adapters.SearchListAdapter;
import com.project.fotogram.communication.RequestWithParams;
import com.project.fotogram.communication.VolleySingleton;
import com.project.fotogram.model.Friend;
import com.project.fotogram.model.SearchedFriends;
import com.project.fotogram.model.SessionInfo;
import com.project.fotogram.utility.Constants;
import com.project.fotogram.utility.UtilityMethods;

public class SearchActivity extends AppCompatActivity {
    private final int TRIGGER_AUTO_COMPLETE = 100;
    private final long AUTO_COMPLETE_DELAY = 300;
    AutoCompleteTextView autoCompleteSearch;
    SearchListAdapter searchAdapter;
    Handler handler;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_search);
        Log.d("fotogramLogs", "entro quiii!!!");
        ImageButton createPostImage = (ImageButton) findViewById(R.id.action_createPost);
        ImageButton ownProfile = (ImageButton) findViewById(R.id.action_ownProfile);
        ImageButton searchFriend = (ImageButton) findViewById(R.id.action_searchFriend);
        ImageButton goBack = (ImageButton) findViewById(R.id.action_goBack);
        createPostImage.setOnClickListener(getMenuOnClickListener());
        ownProfile.setOnClickListener(getMenuOnClickListener());
        searchFriend.setOnClickListener(getMenuOnClickListener());
        goBack.setOnClickListener(getMenuOnClickListener());

        //TextView selectedText = (TextView) findViewById(R.id.partialresult_username);
        AutoCompleteTextView autoCompleteSearch = (AutoCompleteTextView) findViewById(R.id.action_searchUser);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.d("fotogramLogs", "handler!!");
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteSearch.getText())) {
                        getSomeFriendsInfo(autoCompleteSearch.getText().toString());
                    }
                }
                return false;
            }
        });
        searchAdapter = new SearchListAdapter(SearchActivity.this, R.layout.search_list_element);
        autoCompleteSearch.setThreshold(1);
        autoCompleteSearch.setAdapter(searchAdapter);
        autoCompleteSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("fotogramLogs", "searched user clicked!");
                Friend searched = (Friend) parent.getAdapter().getItem(position);
                Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                intent.putExtra("username", searched.getName());
                startActivity(intent);

            }
        });

        autoCompleteSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public View.OnClickListener getMenuOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.action_createPost:
                        Intent createPostIntent = new Intent(SearchActivity.this, PostCreationActivity.class);
                        startActivity(createPostIntent);
                        break;
                    case R.id.action_ownProfile:
                        Intent ownProfile = new Intent(SearchActivity.this, ProfileActivity.class);
                        ownProfile.putExtra("username", SessionInfo.getInstance().getCurrentUsername(SearchActivity.this));
                        startActivity(ownProfile);
                        break;
                    case R.id.action_searchFriend:
                        Intent search = new Intent(SearchActivity.this, SearchActivity.class);
                        startActivity(search);
                        break;
                    case R.id.action_goBack:
                        finish();
                        break;
                }
            }
        };
    }

    public void getSomeFriendsInfo(String startsWith) {
        RequestWithParams friendsList = new RequestWithParams(Request.Method.POST, Constants.BASEURL + "users", success -> {
            Log.d("fotogramLogs", "ho ricevuto gli amici: " + success);
            Gson gson = new Gson();
            SearchedFriends friends = gson.fromJson(success, SearchedFriends.class);
            searchAdapter.setData(friends.getUsers());
            searchAdapter.notifyDataSetChanged();
            Log.d("fotogramLogs", "ho chiamato!!");
        }, error -> UtilityMethods.manageCommunicationError(this, error));
        friendsList.addParam("session_id", SessionInfo.getInstance().getSessionId(this));
        friendsList.addParam("usernamestart", startsWith);
        friendsList.addParam("limit", "15");
        VolleySingleton.getInstance(this).addToRequestQueue(friendsList);
    }
}
