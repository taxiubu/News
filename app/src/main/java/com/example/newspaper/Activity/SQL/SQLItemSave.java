package com.example.newspaper.Activity.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.newspaper.Activity.Model.ItemSave;
import java.util.ArrayList;
import java.util.List;

public class SQLItemSave extends SQLiteOpenHelper {
    private static final String TAG = "SQLItemSave";
    static final String DB_NAME_TABLE = "SQLItemSave";
    static final String DB_NAME= "SQLItemSave.db";
    static final int VERSION= 3;

    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    Cursor cursor;

    public SQLItemSave(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQLItemSave = "CREATE TABLE SQLItemSave ( "+
                "title TEXT," +
                "document TEXT )";

        sqLiteDatabase.execSQL(SQLItemSave);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i!= i1){
            sqLiteDatabase.execSQL("drop table if exists "+ DB_NAME_TABLE);
            onCreate(sqLiteDatabase);
        }
    }
    public void insertItemSave(String title, String document){
        sqLiteDatabase = getWritableDatabase();
        contentValues= new ContentValues();
        contentValues.put("title", title);
        contentValues.put("document", document);

        sqLiteDatabase.insert(DB_NAME_TABLE, null, contentValues);
        closeDB();
    }

    public List<ItemSave> getAllItemSave(){
        List<ItemSave> itemSaves= new ArrayList<>();
        ItemSave itemSave;

        sqLiteDatabase= getReadableDatabase();
        cursor = sqLiteDatabase.query(false, DB_NAME_TABLE, null, null,
                null, null, null, null, null);
        while (cursor.moveToNext()){
            String title= cursor.getString(cursor.getColumnIndex("title"));
            String document= cursor.getString(cursor.getColumnIndex("document"));
            itemSave= new ItemSave(title, document);
            itemSaves.add(itemSave);
        }
        closeDB();
        return itemSaves;

    }

    public int deleteItemSave(String title){
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