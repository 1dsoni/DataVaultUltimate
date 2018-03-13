package com.projects.doctor.datavaultultimate.databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by doctor on 07-07-2017.
 *
 * manages the notes of independent note
 *
 */

public class Sub_item_Notes_Storage {

    public static String table_name="sub_name";

    public static String CREATE_QUERY = "CREATE TABLE `` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`note`\tTEXT,\n" +
            "\t`date`\tTEXT,\n" +
            "\t`time`\tTEXT,\n" +
            "\t`image`\tTEXT\n" +
            ");";

    public static final String ID_COl = "id";
    public static final String NOTE_COL = "note";
    public static final String DATE_COL = "date";
    public static final String TIME_COL = "time";
    public static final String IMAGE_COL = "image";

    public static long insert(SQLiteDatabase sqLiteDatabase, ContentValues contentValues){
        return sqLiteDatabase.insert(table_name,null,contentValues);
    }

    public static long update(SQLiteDatabase sqLiteDatabase, ContentValues contentValues, String whereClause,String[] whereArgs){
        return sqLiteDatabase.update(table_name,contentValues,whereClause,whereArgs);
    }

    public static long delete(SQLiteDatabase sqLiteDatabase, String whereClause, String[] whereArgs){
        return sqLiteDatabase.delete(table_name,whereClause,whereArgs);
    }

    public static Cursor select(Boolean distinct, SQLiteDatabase sqLiteDatabase, String[] columns, String where, String[] whereArgs, String orderBy ){
        return sqLiteDatabase.query(distinct,table_name,columns,where,whereArgs,null,null,orderBy,null);
    }

}
