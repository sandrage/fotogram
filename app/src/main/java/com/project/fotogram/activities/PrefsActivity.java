package com.project.fotogram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.project.fotogram.R;
import com.project.fotogram.model.SessionInfo;

public class PrefsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.activity_prefs);
        Button logoutButt = (Button) findViewById(R.id.action_logout);
        logoutButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionInfo.getInstance().resetSessionId(PrefsActivity.this);
                SessionInfo.getInstance().resetCurrentUsername(PrefsActivity.this);
                Intent createMainMenuIntent = new Intent(PrefsActivity.this, MainActivity.class);
                startActivity(createMainMenuIntent);
            }
        });
    }
}
