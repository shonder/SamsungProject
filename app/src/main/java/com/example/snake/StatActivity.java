package com.example.snake;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.snake.db.MyDbManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class StatActivity extends Activity {

    MyDbManager myDbManager;
    DatabaseReference databaseReference;
    String GROUP = "Stat";
    Stat stat;
    TextView maxScore;
    TextView maxSession;
    TextView gameSession;
    int ms = 0;
    long mss = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        myDbManager = new MyDbManager(this);
        maxScore = findViewById(R.id.tv_maxScore);
        maxSession = findViewById(R.id.tv_maxSession);
        gameSession = findViewById(R.id.tv_gameSession);
    }

    public void onClickBack(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(SnakeActivity.orientation);

        //
        myDbManager.openDb();

        maxScore.append(myDbManager.getMaxScore().toString());
        maxSession.append(myDbManager.getMaxSession().toString());


        String[] res = myDbManager.getAllFromDb();
        for (int i = 0; i < res.length; i++) {
            gameSession.append(res[i] + '\n');
        }



        databaseReference = FirebaseDatabase.getInstance().getReference(GROUP);
        databaseReference.orderByChild("score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    stat = ds.getValue(Stat.class);
                    gameSession.append("Date: " + stat.getDate() + "; " + "TimeInGame: " + stat.getTimeSession() + "; " + "Score: " + stat.getScore() + '\n');
                    if (stat.getScore() > ms) {
                        ms = stat.getScore();
                        maxScore.setText("Max Score: " + ms);
                    };
                    if (stat.getTimeSession() > mss) {
                        mss = stat.getTimeSession();
                        maxSession.setText("Max Time Session: " + mss);
                    };
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Восстановление ориентации экрана по умолчанию
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //
        myDbManager.closeDb();
    }
}