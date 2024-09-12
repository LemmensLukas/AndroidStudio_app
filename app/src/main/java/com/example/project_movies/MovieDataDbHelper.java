package com.example.project_movies;

import static com.example.project_movies.MovieDataContract.MovieData.COLUMN_NAME_BOOKMARKED;
import static com.example.project_movies.MovieDataContract.MovieData.COLUMN_NAME_DESCRIPTION;
import static com.example.project_movies.MovieDataContract.MovieData.COLUMN_NAME_PICTURE;
import static com.example.project_movies.MovieDataContract.MovieData.COLUMN_NAME_RELEASE_DATE;
import static com.example.project_movies.MovieDataContract.MovieData.COLUMN_NAME_TITLE;
import static com.example.project_movies.MovieDataContract.MovieData.TABLE_NAME;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MovieDataDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    public static final int DATABASE_VERSION = 1;
    Context context;

    //private ContentResolver mContentResolver;

    public static final String SQL_CREATE_TABLES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    MovieDataContract.MovieData._ID + " INTEGER PRIMARY KEY, " +
                    TABLE_NAME + " TEXT, " +
                    COLUMN_NAME_TITLE + " TEXT, " +
                    COLUMN_NAME_DESCRIPTION + " TEXT, " +
                    COLUMN_NAME_PICTURE + " TEXT, " +
                    COLUMN_NAME_RELEASE_DATE + " DATE, " +
                    COLUMN_NAME_BOOKMARKED + " INT)";

    public static final String SQL_DROP_TABLES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public MovieDataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DROP_TABLES);
        onCreate(sqLiteDatabase);
    }



    void addMovie(String title, String description, String date, int bookmarked){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME_TITLE, title);
        contentValues.put(COLUMN_NAME_DESCRIPTION, description);
        //contentValues.put(COLUMN_NAME_PICTURE, image);
        contentValues.put(COLUMN_NAME_RELEASE_DATE, date);
        contentValues.put(COLUMN_NAME_BOOKMARKED, bookmarked);

        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully added", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if(sqLiteDatabase != null){
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor readBookmarkedData(){
        String query = "SELECT * FROM " + TABLE_NAME  + " WHERE " + COLUMN_NAME_BOOKMARKED + "='1'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if(sqLiteDatabase != null){
            cursor = sqLiteDatabase.rawQuery(query, null);
        }

        return cursor;
    }

    void editData(String row_id, String title, String description, String releaseDate){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_TITLE, title);
        contentValues.put(COLUMN_NAME_DESCRIPTION, description);
        contentValues.put(COLUMN_NAME_RELEASE_DATE, releaseDate);

        long result = sqLiteDatabase.update(TABLE_NAME, contentValues, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Successfully edited", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteMovie(String row_id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.delete(TABLE_NAME, "_id=?", new String[]{row_id});

        if(result == -1){
            Toast.makeText(context, "Failed deleting", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
        }
    }

    void bookmark(String row_id, int bookmarked){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_BOOKMARKED, bookmarked);

        long result = sqLiteDatabase.update(TABLE_NAME, contentValues, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
