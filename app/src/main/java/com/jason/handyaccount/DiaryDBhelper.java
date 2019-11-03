package com.jason.handyaccount;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DiaryDBhelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "maydiary.db";
    public static final String TABLE_NAME = "tb_diaryone";

    public DiaryDBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DiaryDBhelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }
    @Override

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists " + TABLE_NAME + " (Id integer primary key autoincrement, Story text,  Writing_Date text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
