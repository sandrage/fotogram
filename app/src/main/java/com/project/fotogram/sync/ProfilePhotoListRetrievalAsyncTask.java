package com.project.fotogram.sync;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.project.fotogram.communication.RequestWithParams;
import com.project.fotogram.communication.VolleySingleton;
import com.project.fotogram.model.SearchedFriends;
import com.project.fotogram.model.SessionInfo;
import com.project.fotogram.utility.Constants;
import com.project.fotogram.utility.UtilityMethods;

import java.util.Set;

public class ProfilePhotoListRetrievalAsyncTask extends Thread {
    private FragmentActivity activity;
    private Set<String> users;

    public ProfilePhotoListRetrievalAsyncTask(FragmentActivity activity, Set<String> users) {
        this.activity = activity;
        this.users = users;
    }

    public void run() {
        Gson gson = new Gson();
        Log.d("fotogramLogs", "inizio qui!");
        for (String user : users) {
            RequestWithParams profilePhotoReq = new RequestWithParams(Request.Method.POST, Constants.BASEURL + "users", new Response.Listener<String>() {
                @Override
                public void onResponse(String mess) {
                    Log.d("fotogramLogs", "onResponse requet users! " + Thread.currentThread().getId());
                    SearchedFriends profileInfo = gson.fromJson(mess, SearchedFriends.class);
                    SessionInfo.getInstance().addProfilePhotos(profileInfo.getUsers().get(0));
                    VolleyReqSynchronizer.getInstance().release();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    UtilityMethods.manageCommunicationError(activity, error);
                    VolleyReqSynchronizer.getInstance().release();
                }
            });
            profilePhotoReq.addParam("session_id", SessionInfo.getInstance().getSessionId(activity));
            profilePhotoReq.addParam("usernamestart", user);
            profilePhotoReq.addParam("limit", "1");
            Log.d("fotogramLogs", "retrieveRequests profile photo: " + Thread.currentThread().getId());
            VolleySingleton.getInstance(activity).addToRequestQueue(profilePhotoReq);
        }
    }
}
