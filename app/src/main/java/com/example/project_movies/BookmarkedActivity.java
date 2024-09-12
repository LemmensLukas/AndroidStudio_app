package com.example.project_movies;

import static com.example.project_movies.MovieDataContract.MovieData.COLUMN_NAME_BOOKMARKED;
import static com.example.project_movies.MovieDataContract.MovieData.CONTENT_URI;
import static com.example.project_movies.MovieDataContract.MovieData.TABLE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class BookmarkedActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    LinearLayout home, settings, bookmarked;
    ImageView menu, open_settings;
    RecyclerView recyclerView;
    MovieDataDbHelper movieDataDbHelper;
    ArrayList<String> movie_id, movie_title, movie_description, movie_image, movie_releaseDate, movie_bookmarked;
    AdapterClass adapterClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarked);
        recyclerView = findViewById(R.id.rv_bookmarked);
        Objects.requireNonNull(getSupportActionBar()).hide();

        movieDataDbHelper = new MovieDataDbHelper(BookmarkedActivity.this);
        movie_id = new ArrayList<>();
        movie_title = new ArrayList<>();
        movie_description = new ArrayList<>();
        movie_image = new ArrayList<>();
        movie_releaseDate = new ArrayList<>();
        movie_bookmarked = new ArrayList<>();

        int orientation = getResources().getConfiguration().orientation;
        int col;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            col = 1;
        } else {
            col = 2;
        }

        storeData();

        adapterClass = new AdapterClass(BookmarkedActivity.this, movie_id, movie_title, movie_description,
                movie_image, movie_releaseDate, movie_bookmarked);
        recyclerView.setAdapter(adapterClass);
        recyclerView.setLayoutManager(new GridLayoutManager(BookmarkedActivity.this, col));

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        open_settings = findViewById(R.id.open_settings);
        home = findViewById(R.id.home);
        bookmarked = findViewById(R.id.bookmarked);

        open_settings.setOnClickListener(v -> {
            Intent intent = new Intent(BookmarkedActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        menu.setOnClickListener(v -> openDrawer(drawerLayout));

        home.setOnClickListener(v -> redirectActivity(BookmarkedActivity.this, MainActivity.class));

        bookmarked.setOnClickListener(v -> recreate());
    }

    void storeData(){
        String[] projection = {"_ID", "title", "description", "picture", "releaseDate", "bookmarked"};
        String selection = COLUMN_NAME_BOOKMARKED + "= ?";
        String[] selectionArgs = {"1"};
        Cursor cursor = getContentResolver().query(CONTENT_URI, projection, selection, selectionArgs,null);
        if (cursor == null){
            //do something here
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }else if(cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                movie_id.add(cursor.getString(0));
                movie_title.add(cursor.getString(1));
                movie_description.add(cursor.getString(2));
                movie_image.add(cursor.getString(3));
                movie_releaseDate.add(cursor.getString(4));
                movie_bookmarked.add(cursor.getString(5));
                cursor.moveToNext();
            }
        }
        /**Cursor cursor = movieDataDbHelper.readBookmarkedData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Bookmarks", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursor.moveToNext()){
                movie_id.add(cursor.getString(0));
                movie_title.add(cursor.getString(2));
                movie_description.add(cursor.getString(3));
                movie_image.add(cursor.getString(4));
                movie_releaseDate.add(cursor.getString(5));
                movie_bookmarked.add(cursor.getString(6));
            }
        }**/
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }
}