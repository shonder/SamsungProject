package com.example.snake;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.Button;

import com.example.snake.db.MyDbManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DiffActivity extends Activity {

    SnakeEngine snakeEngine;

    Button easyBtn;
    Button mediumBtn;
    Button highBtn;
    MyDbManager myDbManager;
    DatabaseReference databaseReference;
    String GROUP = "Stat";
    String dateBegin;
    Long timeBegin;
    Long timeEnd;
    Long timeSession;
    Integer score;
    public Stat stat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        myDbManager = new MyDbManager(this);

        Display display = getWindowManager().getDefaultDisplay();


        Point size = new Point();
        display.getSize(size);
        snakeEngine = new SnakeEngine(this, size);

        setContentView(R.layout.activity_diff);
        easyBtn = findViewById(R.id.easy_btn);
        easyBtn.setOnClickListener(view -> {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            dateBegin = dateFormat.format(date);
            timeBegin = System.currentTimeMillis() / 1000;

            SnakeEngine.MILLIS_PER_SECOND = 1700;
            setContentView(snakeEngine);
        });

        mediumBtn = findViewById(R.id.medium_btn);
        mediumBtn.setOnClickListener(view -> {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            dateBegin = dateFormat.format(date);
            timeBegin = System.currentTimeMillis() / 1000;

            SnakeEngine.MILLIS_PER_SECOND = 1000;
            setContentView(snakeEngine);
        });


        highBtn = findViewById(R.id.high_btn);
        highBtn.setOnClickListener(view -> {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            dateBegin = dateFormat.format(date);
            timeBegin = System.currentTimeMillis() / 1000;

            SnakeEngine.MILLIS_PER_SECOND = 550;
            setContentView(snakeEngine);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(SnakeActivity.orientation);
        if (snakeEngine != null) {
            snakeEngine.resume();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        if (snakeEngine != null) {
            snakeEngine.pause();
        }

    }



    @Override
    protected void onDestroy() {
        if (timeBegin != null) {

            //
            myDbManager = new MyDbManager(this);
            myDbManager.openDb();
            //
            timeEnd = System.currentTimeMillis() / 1000;
            timeSession = timeEnd - timeBegin;
            score = snakeEngine.maxScore;
            //
            myDbManager.insertToDb(dateBegin, timeSession, score);
            myDbManager.closeDb();
            //

            databaseReference = FirebaseDatabase.getInstance().getReference(GROUP);
            stat = new Stat(dateBegin, timeSession, score);
            databaseReference.push().setValue(stat);


        }

        super.onDestroy();
    }

}