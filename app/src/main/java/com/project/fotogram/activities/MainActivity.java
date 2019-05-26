package com.project.fotogram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.project.fotogram.R;
import com.project.fotogram.communication.RequestWithParams;
import com.project.fotogram.communication.VolleySingleton;
import com.project.fotogram.model.SessionInfo;
import com.project.fotogram.utility.Constants;
import com.project.fotogram.utility.UtilityMethods;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SessionInfo.getInstance().getSessionId(MainActivity.this) == null) {
            setContentView(R.layout.activity_main);

            TextView link = (TextView) findViewById(R.id.register);
            Spanned text = Html.fromHtml("Don't you have an account yet? Register <b> <a href='https://ewserver.di.unimi.it/mobicomp/fotogram/userreg.html'>here</></b>", Html.FROM_HTML_MODE_LEGACY);
            link.setMovementMethod(LinkMovementMethod.getInstance());
            link.setText(text);

            Button button = (Button) findViewById(R.id.login);
            button.setOnClickListener(this);
        } else {
            Log.d("fotogramLogs", "Ho trovato il session_id configurato!");
            SessionInfo.getInstance().updateFollowedFriends(MainActivity.this);
            Intent showCaseIntent = new Intent(MainActivity.this, ShowcaseActivity.class);
            startActivity(showCaseIntent);
        }

    }

    @Override
    public void onClick(View v) {
        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);
        final String currentUsername = username.getText() != null ? username.getText().toString() : "";
        RequestWithParams loginRequest = new RequestWithParams(Request.Method.POST, Constants.BASEURL + "login", responseToken -> {
            Log.d("fotogramLogs", "token session: " + responseToken);
            SessionInfo.getInstance().setSessionId(MainActivity.this, responseToken);
            SessionInfo.getInstance().updateCurrentUsername(MainActivity.this, currentUsername);
            SessionInfo.getInstance().updateFollowedFriends(MainActivity.this);
            Intent showCaseIntent = new Intent(MainActivity.this, ShowcaseActivity.class);
            startActivity(showCaseIntent);

        }, error -> {
            UtilityMethods.manageCommunicationError(error);
            //TODO qua dovrei riportarlo alla login mostrandogli un messaggio di errore in rosso
        });

        loginRequest.addParam("username", currentUsername);
        loginRequest.addParam("password", password.getText() != null ? password.getText().toString() : "");
        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(loginRequest);
    }
}
