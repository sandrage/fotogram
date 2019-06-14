package com.project.fotogram.sync;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
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

public class ProfilePostsAsyncTask extends AsyncTask<String, Void, UserData> {
    private FragmentActivity activity;
    private UserData userData;
    private Bitmap image;

    public ProfilePostsAsyncTask(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    protected UserData doInBackground(String... usernames) {
        Log.d("fotogramLogs", "cerco profile info");
        RequestWithParams profileRequest = new RequestWithParams(Request.Method.POST, Constants.BASEURL + "profile", mess -> {
            Log.d("fotogramLogs", "messaggio: " + mess);
            Gson gson = new Gson();
            UserData user = gson.fromJson(mess, UserData.class);
            userData = user;
            Synchronizer.getInstance().release();
        }, error -> UtilityMethods.manageCommunicationError(this.activity, error));
        profileRequest.addParam("session_id", SessionInfo.getInstance().getSessionId(this.activity));
        profileRequest.addParam("username", usernames[0]);
        VolleySingleton.getInstance(this.activity).addToRequestQueue(profileRequest);
        Synchronizer.getInstance().acquire();
        Log.d("fotogramLogs", "riprendo!");
        if (userData.getImg() != null && !userData.getImg().equalsIgnoreCase("")) {
            byte[] decodedPostImageString = Base64.decode(userData.getImg(), Base64.DEFAULT);
            this.image = BitmapFactory.decodeByteArray(decodedPostImageString, 0, decodedPostImageString.length);
        }
        return userData;
    }

    @Override
    protected void onPostExecute(UserData toBeDisplayed) {
        ListView listView = (ListView) this.activity.findViewById(R.id.profilePostsList);
        TextView profileUsernameView = (TextView) this.activity.findViewById(R.id.profileUsername);
        ImageView profileImgView = (ImageView) this.activity.findViewById(R.id.profileImage);

        profileUsernameView.setText(userData.getUsername());
        profileImgView.setImageBitmap(this.image);
        //display them through the adapter
        UserDataAdapter userDataAdapter = new UserDataAdapter(this.activity, R.layout.profile_list_element, userData.getPosts());
        listView.setAdapter(userDataAdapter);
    }
}
