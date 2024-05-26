package com.example.snake.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDbManager {
    private Context context;
    private MyDbHelper myDbHelper;
    private SQLiteDatabase db;

    public MyDbManager(Context context) {
        this.context = context;
        myDbHelper = new MyDbHelper(context);
    }

    public void openDb() {
        db = myDbHelper.getWritableDatabase();
    }

    public void insertToDb(String date, Long timeSession, Integer score) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.DATE, date);
        cv.put(MyConstants.TIME_SESSION, timeSession);
        cv.put(MyConstants.SCORE, score);

        db.insert(MyConstants.TABLE_NAME, null, cv);
    }

    public Integer getMaxScore() {
        Cursor cursor = db.query(MyConstants.TABLE_NAME, new String[] {"MAX("+ MyConstants.SCORE +")"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int maxScore = cursor.getInt(0);
            return maxScore;
        }
        cursor.close();
        return 0;
    }

    public Integer getMaxSession() {
        Cursor cursor = db.rawQuery("SELECT MAX(timeSession) FROM stat_table", null);
        if (cursor.moveToFirst()) {
            int maxSession = cursor.getInt(0);
            return maxSession;
        }
        cursor.close();
        return 0;
    }


    public String[] getAllFromDb() {
        String[] res;
        Cursor cursor = db.query(MyConstants.TABLE_NAME, null, null, null, null, null, MyConstants.SCORE + " DESC");
        if( cursor.getCount() < 5) res = new String[cursor.getCount()];
        else res = new String[5];

        int size = res.length;
        int pos = 0;
        while (cursor.moveToNext()) {
            if(pos < size) {
                String row = "Date: " + cursor.getString(1) + "; " + "TimeInGame: " + cursor.getString(2) + "; " + "Score: " + cursor.getString(3);
                res[pos] = row;
                pos++;
            } else break;
        }
        cursor.close();

        return res;
    }

    public void deleteData() {
        db.execSQL("delete from "+ MyConstants.TABLE_NAME);
    }

    public void closeDb() {
        myDbHelper.close();
    }
}
