package com.example.project_movies;

import static android.provider.BaseColumns._ID;
import static com.example.project_movies.MovieDataContract.AUTHORITY;
import static com.example.project_movies.MovieDataContract.MOVIE_PATH;
import static com.example.project_movies.MovieDataContract.MovieData.COLUMN_NAME_BOOKMARKED;
import static com.example.project_movies.MovieDataContract.MovieData.TABLE_NAME;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;

public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDataDbHelper movieDataDbHelper;
    private static final int ALL_MOVIES = 100;
    private static final int MOVIE_ID = 101;

    @Override
    public boolean onCreate(){
        Context context = getContext();
        movieDataDbHelper = new MovieDataDbHelper(context);
        return true;
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, MOVIE_PATH, ALL_MOVIES);
        uriMatcher.addURI(AUTHORITY, MOVIE_PATH + "/#", MOVIE_ID);
        return uriMatcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = movieDataDbHelper.getWritableDatabase();
        Cursor movieData;
        int match =sUriMatcher.match(uri);

        switch (match){
            case ALL_MOVIES:
            case MOVIE_ID:
                movieData = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        return movieData;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = movieDataDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        long id;
        Uri returnUri;
        switch (match){
            case ALL_MOVIES:
                id = db.insert(TABLE_NAME, null, values);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(MovieDataContract.MovieData.CONTENT_URI, id);
                }
                else{
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = movieDataDbHelper.getWritableDatabase();
        int deleted = 0;
        int match = sUriMatcher.match(uri);
        switch(match){
            case ALL_MOVIES:
                Log.e("Wrong path","wrong path");
                break;
            case MOVIE_ID:
                String[] id = new String[]{String.valueOf(ContentUris.parseId(uri))};
                Log.e("id in provider", Arrays.toString(id));
                deleted = db.delete(MovieDataContract.MovieData.TABLE_NAME, _ID + " = ?", id);
                //deleted = db.delete(MovieDataContract.MovieData.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = movieDataDbHelper.getWritableDatabase();
        int rows = 0;
        int match = sUriMatcher.match(uri);
        switch (match){
            case ALL_MOVIES:
                break;
            case MOVIE_ID:
                String[] id = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rows = db.update(TABLE_NAME, values, _ID + " = ?", id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        if(rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }
}
