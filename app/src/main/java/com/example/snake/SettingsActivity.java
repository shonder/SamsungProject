package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.example.snake.db.MyDbManager;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends Activity {
    ToggleButton orientTb;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setRequestedOrientation(SnakeActivity.orientation);

        orientTb = findViewById(R.id.orient_tb);

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view -> {
            finish();
        });

    }

    public void onClickOrientation(View view) {
        if (orientTb.isChecked()) {
            SnakeActivity.orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            setRequestedOrientation(SnakeActivity.orientation);
        } else {
            SnakeActivity.orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            setRequestedOrientation(SnakeActivity.orientation);
        }
    }

}