package com.suja.mypopularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Suja Manu on 3/8/2018.
 */

public class MovieDBHelper extends SQLiteOpenHelper {


    // The database name
    private static final String DATABASE_NAME = "NewMovie.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold waitlist data
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT PRIMARY KEY ," +
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_VOTE + " TEXT NULL," +
                MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE_PATH + " TEXT NULL," +
                MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NULL ," +
                MovieContract.MovieEntry.COLUMN_MOVIE_FAVURITE + " TEXT NULL" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
