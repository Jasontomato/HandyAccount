package com.jason.handyaccount;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DiaryManager {
    private DiaryDBhelper dbHelper;
    private  String TBNAME;

    public DiaryManager(Context context) {
        dbHelper = new DiaryDBhelper(context);
        TBNAME = dbHelper.TABLE_NAME;
    }

    //添加一行数据
    public void add(DiaryItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Story",item.getCurStory());
        values.put("Writing_Date",item.getWriting_date());
    //    values.put("Location",item.getLocation());
        db.insert(TBNAME,null,values);
        db.close();

    }

    //查询整个表的所有数据
    public List<DiaryItem> listall(){
        List<DiaryItem> diarylist = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME,null,null,null,null,null,null);
        if (cursor != null){
            diarylist = new ArrayList<DiaryItem>();
            while (cursor.moveToNext()){
                DiaryItem item = new DiaryItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                item.setCurStory(cursor.getString(cursor.getColumnIndex("Story")));
                item.setWriting_date(cursor.getString(cursor.getColumnIndex("Writing_Date")));
         //       item.setLocation(cursor.getString(cursor.getColumnIndex("Location")));
                diarylist.add(item);
            }
            cursor.close();
        }
        db.close();
        return diarylist;
    }


    public void delete(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME,"Id=?",new String[]{String.valueOf(id)});
        db.close();
    }
    /*public void delete(String diary){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME,"Story=?",new String[]{String.valueOf(diary)});
        db.close();
    }*/

    public void update(DiaryItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Story",item.getCurStory());
        values.put("Writing_Date",item.getWriting_date());
      //  values.put("Location",item.getLocation());
        db.update(TBNAME,values,"Id=?",new String[]{String.valueOf(item.getId())});
        db.close();
    }

    public DiaryItem findById(int id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME,null,"Id=?",new String[]{String.valueOf(id)},null,null,null);
        DiaryItem diaryItem = null;
        if(cursor != null && cursor.moveToFirst()){
            diaryItem = new DiaryItem();
            diaryItem.setId(cursor.getInt(cursor.getColumnIndex("Id")));
            diaryItem.setCurStory(cursor.getString(cursor.getColumnIndex("Story")));
            diaryItem.setWriting_date(cursor.getString(cursor.getColumnIndex("Writing_Date")));
            //diaryItem.setLocation(cursor.getString(cursor.getColumnIndex("Location")));
            cursor.close();
        }
        db.close();
        return diaryItem;
    }

}
