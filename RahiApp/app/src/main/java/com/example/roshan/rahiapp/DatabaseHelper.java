package com.example.roshan.rahiapp;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class DatabaseHelper extends SQLiteAssetHelper {
    public static  String DB_NAME = "trainsDB.sqlite3";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME,null,2);
    }

}
