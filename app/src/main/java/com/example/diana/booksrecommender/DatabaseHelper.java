package com.example.diana.booksrecommender;

/**
 * Created by Diana on 5/2/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Books.db";
    public static final String TABLE_NAME = "FAVORITE_BOOK";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "EMAIL";//users email
    public static final String COL_3 = "BOOK_ID";//id provided by google
    public static final String COL_4 = "TITLE";
    public static final String COL_5 = "AUTHOR";
    public static final String COL_6 = "CATEGORY";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " TEXT," + COL_3 + " TEXT," + COL_4 + " TEXT,"+ COL_5 + " TEXT," + COL_6 + " TEXT)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate(db);
    }

    public boolean insertData(String email,String bookID,String title,String author,String category)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,email);
        contentValues.put(COL_3,bookID);
        contentValues.put(COL_4,title);
        contentValues.put(COL_5,author);
        contentValues.put(COL_6,category);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean bookExists(String email, String bookId)
    {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String checkQuery = "SELECT " + COL_1 + " FROM " + TABLE_NAME + " WHERE " + COL_2 + "= '"+email + "' and " + COL_3 + " = '" + bookId + "'";
        cursor= db.rawQuery(checkQuery,null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public Cursor getAllData(String email)
    {
        Cursor res = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_2 + "='" + email+ "'", null);

        }catch (Exception e)
        {
            String s = e.getMessage();
        }
        return res;
    }


    public int deleteFavorite(String bookId,String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int res =  db.delete(TABLE_NAME, COL_3 + " = '" + bookId +"' and "+ COL_2 + " = '" + email + "'",null);
        db.close();
        return  res;
    }
}