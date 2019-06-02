package com.project.fotogram.sync;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.project.fotogram.R;
import com.project.fotogram.activities.ProfileActivity;
import com.project.fotogram.adapters.PostAdapter;
import com.project.fotogram.communication.RequestWithParams;
import com.project.fotogram.communication.VolleySingleton;
import com.project.fotogram.model.Post;
import com.project.fotogram.model.PostsList;
import com.project.fotogram.model.SessionInfo;
import com.project.fotogram.utility.Constants;
import com.project.fotogram.utility.UtilityMethods;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PostsListRetrievalAsyncTask extends AsyncTask<Void, Void, List<Post>> {
    private FragmentActivity fragmentActivity;
    private List<Post> postsToBeReturned;

    public PostsListRetrievalAsyncTask(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    protected List<Post> doInBackground(Void... voids) {
        RequestWithParams wallRequest = new RequestWithParams(Request.Method.POST, Constants.BASEURL + "wall", new Response.Listener<String>() {
            @Override
            public void onResponse(String mess) {
                Gson gson = new Gson();
                PostsList posts = gson.fromJson(mess, PostsList.class);
                postsToBeReturned = posts.getPosts();
                Set<String> userNames = posts.getPosts() != null ? posts.getPosts().stream().map(post -> post.getUser()).collect(Collectors.toSet()) : new HashSet<>();
                Log.d("fotogramLogs", "users per cui chiamo la profile: " + userNames + ", " + Thread.currentThread().getId());
                VolleyReqSynchronizer.getInstance().setSyncNumber(userNames.size());
                new ProfilePhotoListRetrievalAsyncTask(fragmentActivity, userNames).start();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UtilityMethods.manageCommunicationError(fragmentActivity, error);
            }
        });
        wallRequest.addParam("session_id", SessionInfo.getInstance().getSessionId(fragmentActivity));
        Log.d("fotogramLogs", "mando la chiamata per posts: " + Thread.currentThread().getId());
        VolleySingleton.getInstance(this.fragmentActivity).addToRequestQueue(wallRequest);
        VolleyReqSynchronizer.getInstance().acquire();
        return postsToBeReturned;
    }

    protected void onPostExecute(List<Post> posts) {
        ListView postsListView = (ListView) this.fragmentActivity.findViewById(R.id.postsList);
        Log.d("fotogramLogs", "popolo l'adapter: " + Thread.currentThread().getId());
        PostAdapter postAdapter = new PostAdapter(this.fragmentActivity, R.layout.posts_list_element, posts);
        postsListView.setAdapter(postAdapter);
        postsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = (Post) posts.get(position);
                Intent profileIntent = new Intent(fragmentActivity, ProfileActivity.class);
                profileIntent.putExtra("username", post.getUser());
                fragmentActivity.startActivity(profileIntent);
            }
        });
    }
}
