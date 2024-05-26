package com.example.snake.db;

public class MyConstants {
    public static final String TABLE_NAME = "stat_table";
    public static final String _ID = "_id";
    public static final String DATE = "date";
    public static final String TIME_SESSION = "timeSession";
    public static final String SCORE = "score";
    public static final String DB_NAME = "my_db.db";
    public static final int DB_VERSION = 1;
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY, " +
                    DATE + " TEXT, " +
                    TIME_SESSION + " INTEGER, " +
                    SCORE + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
