package com.projects.doctor.datavaultultimate.databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by doctor on 06-07-2017.
 *
 * manages all tables of the subjects
 */

public class Subject_Notes_DatabaseTable {

    private static final String CREATE = "\"CREATE TABLE `\"+sub_name+\"` (\\n\" +\n" +
            "                 \"\\t`_id`\\tINTEGER PRIMARY KEY AUTOINCREMENT,\\n\" +\n" +
            "                 \"\\t`name`\\tTEXT,\\n\" +\n" +
            "                 \"\\t`date`\\tTEXT,\\n\" +\n" +
            "                 \"\\t`time`\\tTEXT,\\n\" +\n" +
            "                 \"\\t`image`\\tTEXT,\\n\" +\n" +
            "                 \"\\t`sub_name`\\tTEXT,\\n\" +\n" +
            "                 \"\\t`color`\\tTEXT,\\n\" +\n" +
            "                 \"\\t`password`\\tTEXT\\n\" +\n" +
            "                 \");\";\n";


    public static final String ID_COL="_id";
    public static final String NAME_COL="name";
    public static final String DATE_COL="date";
    public static final String TIME_COL="time";
    public static final String IMAGE_COL="image";
    public static final String COLOR_COL="color";
    public static final String SUB_NAME="sub_name";
    public static final String PASSWORD_COL="password";

    public static long insert(SQLiteDatabase sqLiteDatabase, String table_name, ContentValues cv){
        return sqLiteDatabase.insert(table_name,null,cv);
    }

    public static long update(SQLiteDatabase sqLiteDatabase, String table_name, ContentValues contentValues, String whereClause,String[] whereArgs){
        return sqLiteDatabase.update(table_name,contentValues,whereClause,whereArgs);
    }

    public static long delete(SQLiteDatabase sqLiteDatabase, String table_name, String whereClause, String[] whereArgs){
        return sqLiteDatabase.delete(table_name,whereClause,whereArgs);
    }

    public static Cursor select(Boolean distinct,String table_name, SQLiteDatabase sqLiteDatabase, String[] columns, String where, String[] whereArgs,String orderBy ){
        return sqLiteDatabase.query(distinct,table_name,columns,where,whereArgs,null,null,orderBy,null);
    }
    
}
