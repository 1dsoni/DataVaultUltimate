package com.projects.doctor.datavaultultimate.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by doctor on 06-07-2017.
 *
 * used for the Database of everything except Independent notes
 */

public class SubjectDatabase extends SQLiteOpenHelper{

    private static final String subject_info_db="subject_info_db";

    public SubjectDatabase(Context context) {

        super(context, subject_info_db, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(SubjectDatabaseTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SubjectDatabaseTable.CREATE_TABLE);
    }

}
