package com.example.project_movies;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;
import java.sql.Date;

public class MovieDataContract {

    public static final String AUTHORITY = "com.example.project_movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY );
    public static final String MOVIE_PATH = "moviedata";

    private MovieDataContract(){ }

    public static final class MovieData implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(MOVIE_PATH).build();
        public static final String TABLE_NAME = "moviedata";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PICTURE = "picture";
        public static final String COLUMN_NAME_RELEASE_DATE = "releasedate";
        public static String COLUMN_NAME_BOOKMARKED = "bookmarked";
    }
}
