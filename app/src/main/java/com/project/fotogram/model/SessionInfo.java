package com.project.fotogram.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.project.fotogram.communication.RequestWithParams;
import com.project.fotogram.communication.VolleySingleton;
import com.project.fotogram.utility.Constants;
import com.project.fotogram.utility.UtilityMethods;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SessionInfo {
    private static SessionInfo instance;
    private HashMap<String, String> profilePhotos;
    private static ExecutorService executorService;

    public synchronized static SessionInfo getInstance() {
        if (instance == null) {
            instance = new SessionInfo();
        }
        return instance;
    }

    private SessionInfo() {
    }

    public synchronized ExecutorService getExecutorService() {
        if (this.executorService == null) {
            this.executorService = Executors.newSingleThreadExecutor();
        }
        return this.executorService;
    }

    public synchronized void setSessionId(Activity activity, String sessionid) {
        SharedPreferences settings = activity.getSharedPreferences(Constants.APPLICATION_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("session_id", sessionid);
        editor.commit();
    }

    public synchronized void resetSessionId(Activity activity) {
        SharedPreferences settings = activity.getSharedPreferences(Constants.APPLICATION_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("session_id");
        editor.commit();
    }

    public String getSessionId(Activity activity) {
        SharedPreferences settings = activity.getSharedPreferences(Constants.APPLICATION_ID, Context.MODE_PRIVATE);
        return settings.getString("session_id", null);
    }

    public String getCurrentUsername(Activity activity) {
        SharedPreferences settings = activity.getSharedPreferences(Constants.APPLICATION_ID, Context.MODE_PRIVATE);
        return settings.getString("username", null);
    }

    public void resetCurrentUsername(Activity activity) {
        SharedPreferences settings = activity.getSharedPreferences(Constants.APPLICATION_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("username");
        editor.commit();
    }

    public synchronized void updateCurrentUsername(Activity activity, String username) {
        SharedPreferences settings = activity.getSharedPreferences(Constants.APPLICATION_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", username);
        editor.commit();
    }

    public synchronized void updateProfilePhotos(FragmentActivity activity) {
        Log.d("fotogramLogs", "aggiorno le foto profilo! Thread: " + Thread.currentThread().getId());
        this.profilePhotos = new HashMap<>();
        RequestWithParams request = new RequestWithParams(Request.Method.POST, Constants.BASEURL + "followed", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("fotogramLogs", "new profile photos " + response);
                Gson gson = new Gson();
                FollowedFriends friends = gson.fromJson(response, FollowedFriends.class);
                if (friends != null) {
                    friends.getFollowed().stream().forEach(friend -> profilePhotos.put(friend.getName(), friend.getPicture()));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UtilityMethods.manageCommunicationError(activity, error);
            }
        });
        request.addParam("session_id", SessionInfo.getInstance().getSessionId(activity));
        VolleySingleton.getInstance(activity.getApplicationContext()).addToRequestQueue(request);
    }

    public HashMap<String, String> getProfilePhotos() {
        return profilePhotos;
    }


}
