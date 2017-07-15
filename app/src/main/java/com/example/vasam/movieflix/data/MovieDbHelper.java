package com.example.vasam.movieflix.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.vasam.movieflix.data.MovieContract.MovieEntry;

/**
 * Created by vasam on 7/12/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "entertainment.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + "("
                + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieEntry.MOVIE_ID + " INTEGER,"
                + MovieEntry.MOVIE_TITLE + " TEXT NOT NULL, "
                + MovieEntry.MOVIE_IMAGE_PATH + " TEXT NOT NULL,"
                + MovieEntry.MOVIE_DATE + " TEXT NOT NULL,"
                + MovieEntry.MOVIE_RATING + " INTEGER NOT NULL,"
                + MovieEntry.MOVIE_DURATION + " INTEGER NOT NULL,"
                + MovieEntry.MOVIE_OVERVIEW + " TEXT NOT NULL,"
                + MovieEntry.MOVIE_TRAILER_KEY + " TEXT NOT NULL,"
                + MovieEntry.MOVIE_REVIEWS + " TEXT NOT NULL,"
                + " UNIQUE (" + MovieEntry.MOVIE_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
