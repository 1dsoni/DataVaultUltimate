package com.projects.doctor.datavaultultimate.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by doctor on 03-07-2017.
 */

public class Mydb extends SQLiteOpenHelper{
    private static String DATABASE_NAME = "note_ind_dx";
    public Mydb(Context context) {
        super(context, "note_ind_dx", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Notes_Storage.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
