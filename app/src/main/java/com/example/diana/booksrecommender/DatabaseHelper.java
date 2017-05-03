package com.example.diana.booksrecommender;

/**
 * Created by Diana on 5/2/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Books.db";
    public static final String USER_TABLE_NAME = "USER";
    public static final String BOOK_TABLE_NAME = "BOOK";
    public static final String FAVORITES_TABLE_NAME = "FAVORITES";

    public static final String ID = "ID";
    public static final String EMAIL = "EMAIL";

    public static final String BOOK_ID_API = "BOOK_ID_API";
    public static final String TITLE = "TITLE";
    public static final String AUTHOR = "AUTHOR";
    public static final String CATEGORY = "CATEGORY";
    public static final String IMG_URL = "IMG_URL";

    public static final String BOOK_ID_FK = "BOOK_ID";
    public static final String USER_ID_FK = "USER_ID";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USER_TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + EMAIL + " TEXT )" );
        db.execSQL("CREATE TABLE " + BOOK_TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + BOOK_ID_API + " TEXT," +
                TITLE + " TEXT," + AUTHOR + " TEXT," + CATEGORY+ " TEXT,"+ IMG_URL + " TEXT )");
        db.execSQL("CREATE TABLE " + FAVORITES_TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + BOOK_ID_FK + " INTEGER," +
                USER_ID_FK + " INTEGER," + " FOREIGN KEY( " + BOOK_ID_FK + ") REFERENCES " + BOOK_TABLE_NAME+"(" + ID + " ) ON DELETE CASCADE," +
                " FOREIGN KEY( " + USER_ID_FK + ") REFERENCES " + USER_TABLE_NAME+"(" + ID + " ) ON DELETE CASCADE)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + FAVORITES_TABLE_NAME);
        onCreate(db);
    }

    //returns the users id
    public int insertUser(String email){
        int id = userExists(email);
        if (id != -1)
            return id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EMAIL,email);

        long result = db.insert(USER_TABLE_NAME,null,contentValues);
        if (result == -1)
            return -1;
        return (int)result;
    }

    public List<Book> getBooks(int userId)
    {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        int id = -1;
        ArrayList<Book> result = null;
        String checkQuery = "SELECT " + BOOK_ID_API + ", " + TITLE + ", " + AUTHOR + ", " + CATEGORY +", " + IMG_URL + " FROM "
                + FAVORITES_TABLE_NAME + " fav JOIN "+ BOOK_TABLE_NAME + " books ON " + " fav." + BOOK_ID_FK + " = books."+ID+
               " WHERE fav." + USER_ID_FK + "= "+userId;
        try{
            cursor= db.rawQuery(checkQuery,null);
            Book aux;
            result = new ArrayList<Book>();
               while (cursor.moveToNext())
            {
                String[] categories = new String[1];
                categories[0] = cursor.getString(3);
                aux = new Book(cursor.getString(1),cursor.getString(2),cursor.getString(4),3.2,100,null,categories,null,cursor.getString(0));
                result.add(aux);
            }

            cursor.close();
            db.close();
        }catch (Exception e)
        {
            String ex = e.getMessage();
        }
        if(result.size() == 0)
            return null;
        return result;
    }

    public int userExists(String email){
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        int id = -1;
        String checkQuery = "SELECT " + ID + " FROM " + USER_TABLE_NAME + " WHERE " + EMAIL + "= '"+email + "'";
        try{
            cursor= db.rawQuery(checkQuery,null);

            if (cursor.getCount() == 1 && cursor.moveToFirst())
                id = cursor.getInt(0);

            cursor.close();
            db.close();
        }catch (Exception e)
        {
            String ex = e.getMessage();
        }
        return id;
    }

    public int insertBook(String bookID,String title,String author,String category,String img_url)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues;
        contentValues = new ContentValues();
        contentValues.put(BOOK_ID_API,bookID);
        contentValues.put(TITLE,title);
        contentValues.put(AUTHOR,author);
        contentValues.put(CATEGORY,category);
        contentValues.put(IMG_URL,img_url);
        long result = db.insert(BOOK_TABLE_NAME,null,contentValues);
        if (result == -1)
            return -1;
        return (int)result;
    }

    public boolean insertFavorite(int userId,int bookIdFk){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues;
        contentValues = new ContentValues();
        contentValues.put(BOOK_ID_FK,bookIdFk);
        contentValues.put(USER_ID_FK,userId);
        long result = db.insert(FAVORITES_TABLE_NAME,null,contentValues);
        if (result == -1)
            return false;
        return true;
    }

    public int bookExists(String bookId){
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String checkQuery = "SELECT " + ID + " FROM " + BOOK_TABLE_NAME + " WHERE " + BOOK_ID_API + "='"+bookId+"'";
        cursor= db.rawQuery(checkQuery,null);
        int id;
        if (cursor.getCount() == 1 && cursor.moveToFirst())
            id = cursor.getInt(0);
        else
            id = -1;
        cursor.close();
        db.close();
        return id;
    }


    public boolean favoriteExists(int userId, int bookId)
    {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String checkQuery = "SELECT " + ID + " FROM " + FAVORITES_TABLE_NAME + " WHERE " + BOOK_ID_FK + "="+bookId + " and " + USER_ID_FK + " = " + userId;
        cursor= db.rawQuery(checkQuery,null);

        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public int deleteFavorite(int bookId,int userId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int res =  db.delete(FAVORITES_TABLE_NAME, BOOK_ID_FK + " = " + bookId +" and "+ USER_ID_FK + " = " + userId,null);
        db.close();
        return  res;
    }
}