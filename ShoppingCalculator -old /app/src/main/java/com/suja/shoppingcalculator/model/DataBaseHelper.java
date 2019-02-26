package com.suja.shoppingcalculator.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.suja.shoppingcalculator.model.ShoppingItemContract;

/**
 * Created by Suja Manu on 11/26/2018.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "NewMovie.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold waitlist data
        final String SQL_CREATE_ITEM_TABLE = "CREATE TABLE " + ShoppingItemContract.ShoppingItemEntry.TABLE_NAME + " (" +
                ShoppingItemContract.ShoppingItemEntry.COLUMN_ITEM + " TEXT PRIMARY KEY ," +
                ShoppingItemContract.ShoppingItemEntry.COLUMN_PRICE + " TEXT  NULL, " +
                ShoppingItemContract.ShoppingItemEntry.COLUMN_QUANTITY + " TEXT NULL, " +
                ShoppingItemContract.ShoppingItemEntry.COLUMN_DATE + " TEXT NULL" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ShoppingItemContract.ShoppingItemEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
