package com.example.newspaper.Activity.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.newspaper.Activity.Model.ItemRelated;

import java.util.ArrayList;
import java.util.List;

public class SQLClickHistory extends SQLiteOpenHelper {
    private static final String TAG = "SQLClickHistory";
    static final String DB_NAME_TABLE = "ClickHistory";
    static final String DB_NAME= "ClickHistory.db";
    static final int VERSION= 2;

    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    Cursor cursor;

    public SQLClickHistory(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String ClickHistory = "CREATE TABLE ClickHistory ( "+
                "title TEXT," +
                "link TEXT )";

        sqLiteDatabase.execSQL(ClickHistory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i!= i1){
            sqLiteDatabase.execSQL("drop table if exists "+ DB_NAME_TABLE);
            onCreate(sqLiteDatabase);
        }
    }

    public void insertItem(String title, String link){
        sqLiteDatabase = getWritableDatabase();
        contentValues= new ContentValues();
        contentValues.put("title", title);
        contentValues.put("link", link);

        sqLiteDatabase.insert(DB_NAME_TABLE, null, contentValues);
        closeDB();
    }

    public List<ItemRelated> getAllItem(){
        List<ItemRelated> itemRelatedList= new ArrayList<>();
        ItemRelated itemRelated;

        sqLiteDatabase= getReadableDatabase();
        cursor = sqLiteDatabase.query(false, DB_NAME_TABLE, null, null,
                null, null, null, null, null);
        while (cursor.moveToNext()){
            String title= cursor.getString(cursor.getColumnIndex("title"));
            String link= cursor.getString(cursor.getColumnIndex("link"));
            itemRelated= new ItemRelated(title, link);
            itemRelatedList.add(itemRelated);
        }
        closeDB();
        return itemRelatedList;

    }

    public int deleteItemClick(String title){
        sqLiteDatabase= getWritableDatabase();
        return sqLiteDatabase.delete(DB_NAME_TABLE, "title=?", new String[]{title});
    }
    public boolean deleteAll(){
        int result;
        sqLiteDatabase = getWritableDatabase();
        result=sqLiteDatabase.delete(DB_NAME_TABLE, null, null);
        closeDB();
        if (result==1)
            return true;
        else return false;
    }
    private void closeDB() {
        if (sqLiteDatabase != null) sqLiteDatabase.close();
        if (contentValues != null) contentValues.clear();
        if (cursor != null) cursor.close();
    }
}
