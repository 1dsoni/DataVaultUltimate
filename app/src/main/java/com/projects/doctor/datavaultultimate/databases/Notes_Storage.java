package com.projects.doctor.datavaultultimate.databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by doctor on 03-07-2017.
 *
 * this is the helper class for manageing instant notes database
 */

public class Notes_Storage {

    static String TAG = "notes";

    public static final String CREATE_TABLE = "CREATE TABLE `notestable` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`name`\tTEXT,\n" +
            "\t`date`\tTEXT,\n" +
            "\t`time`\tTEXT,\n" +
            "\t`image`\tBLOB,\n" +
            "\t`color`\tTEXT\n" +
            ");";
    public static final String TABLE_NAME = "notestable";
    public static final String ID_COl = "id";
    public static final String NOTE_COL = "name";
    public static final String DATE_COL = "date";
    public static final String TIME_COL = "time";
    public static final String COLOR_COL = "color";
    public static final String IMAGE_COL = "image";

    public static long insertIntoDb(SQLiteDatabase sqLiteDatabase, ContentValues contentValues){
        return sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
    }

    public static Cursor select(SQLiteDatabase sqLiteDatabase,String whereClause){
        return sqLiteDatabase.query(TABLE_NAME,null,whereClause,null,null,null,null);
    }

    public static long update(SQLiteDatabase sqLiteDatabase, ContentValues contentValues, String whereClause,String[] whereArgs){
        return sqLiteDatabase.update(TABLE_NAME,contentValues,whereClause,whereArgs);
    }

    public static long delete(SQLiteDatabase sqLiteDatabase, String whereClause){
        return sqLiteDatabase.delete(TABLE_NAME,whereClause,null);
    }
}
