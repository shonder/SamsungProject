package com.example.snake;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.snake.db.MyDbManager;

public class SnakeActivity extends Activity {
    Button startBtn;
    Button statBtn;
    Button settBtn;
    public static int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(orientation);
        startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(view -> {
            Intent i = new Intent(SnakeActivity.this, DiffActivity.class);
            SnakeActivity.this.startActivity(i);
        });

        statBtn = findViewById(R.id.stat_btn);
        statBtn.setOnClickListener(view -> {
            Intent i = new Intent(SnakeActivity.this, StatActivity.class);
            SnakeActivity.this.startActivity(i);
        });

        settBtn = findViewById(R.id.sett_btn);
        settBtn.setOnClickListener(view -> {
            Intent i = new Intent(SnakeActivity.this, SettingsActivity.class);
            SnakeActivity.this.startActivity(i);
        });

        //
        MyDbManager myDbManager = new MyDbManager(this);
        myDbManager.openDb();
        myDbManager.deleteData();
        myDbManager.closeDb();
         //

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Установка ориентации экрана на вертикальную
        setRequestedOrientation(orientation);
    }

}