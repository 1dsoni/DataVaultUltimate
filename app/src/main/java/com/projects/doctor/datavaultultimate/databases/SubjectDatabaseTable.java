package com.projects.doctor.datavaultultimate.databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by doctor on 06-07-2017.
 */

public class SubjectDatabaseTable {
    public static final String table_name="subject_table_name";

    public static final String CREATE_TABLE="CREATE TABLE `"+"subject_table_name"+"` (\n" +
            "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`name`\tTEXT,\n" +
            "\t`date`\tTEXT,\n" +
            "\t`time`\tTEXT,\n" +
            "\t`image`\tBLOB,\n" +
            "\t`color`\tTEXT,\n" +
            "\t`password`\tTEXT\n" +
            ");";

    public static final String ID_COL="_id";
    public static final String NAME_COL="name";
    public static final String DATE_COL="date";
    public static final String TIME_COL="time";
    public static final String IMAGE_COL="image";
    public static final String COLOR_COL="color";
    public static final String PASSWORD_COL="password";

    public static long insert(SQLiteDatabase sqLiteDatabase, ContentValues contentValues){
        return sqLiteDatabase.insert(table_name,null,contentValues);
    }

    public static long update(SQLiteDatabase sqLiteDatabase, ContentValues contentValues, String whereClause,String[] whereArgs){
        return sqLiteDatabase.update(table_name,contentValues,whereClause,whereArgs);
    }

    public static long delete(SQLiteDatabase sqLiteDatabase, String whereClause, String[] whereArgs){
        return sqLiteDatabase.delete(table_name,whereClause,whereArgs);
    }

    public static Cursor select(Boolean distinct,SQLiteDatabase sqLiteDatabase, String[] columns, String where, String[] whereArgs,String orderBy ){
        Log.d("12345",table_name);

        try {
            if (orderBy.contains("NULL")){
                orderBy=null;
            }
        }catch (Exception e){

        }

        return sqLiteDatabase.query(distinct,table_name,columns,where,whereArgs,null,null,orderBy,null);
    }
}
