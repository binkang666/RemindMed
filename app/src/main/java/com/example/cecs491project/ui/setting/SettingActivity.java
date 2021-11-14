package com.example.cecs491project.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cecs491project.R;

//TODO: Theme Setting
//TODO: Alarm Music Setting
//TODO: Vibration Setting
//TODO: Notification Setting
//TODO: Upgrade to registered account

//OPTIONAL:
//DISABLE ACCOUNT
//ABOUT USER PAGE
//COPYRIGHT PAGE
public class SettingActivity extends AppCompatActivity {

    Button link;
    Button myProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        link = findViewById(R.id.link_account);
        myProfile = findViewById(R.id.profile);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(SettingActivity.this, LinkAccountActivity.class));
                finish();
            }
        });
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(SettingActivity.this, MyProfileActivity.class));
                finish();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");

    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();

        return true;
    }


}