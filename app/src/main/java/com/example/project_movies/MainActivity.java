package com.example.project_movies;

import static com.example.project_movies.MovieDataContract.MovieData.CONTENT_URI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    LinearLayout home, bookmarked;
    ImageView menu, open_settings;
    boolean nightMode;
    FloatingActionButton fab_add_movie;
    RecyclerView recyclerView;
    MovieDataDbHelper movieDataDbHelper;
    ArrayList<String> movie_id, movie_title, movie_description, movie_image, movie_releaseDate, movie_bookmarked;

    AdapterClass adapterClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_movies);

        Objects.requireNonNull(getSupportActionBar()).hide();

        fab_add_movie = findViewById(R.id.fab_add_movie);

        fab_add_movie.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, AddMovieActivity.class);
            startActivity(intent);
        });

        movieDataDbHelper = new MovieDataDbHelper(MainActivity.this);
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

        adapterClass = new AdapterClass(MainActivity.this, movie_id, movie_title, movie_description, movie_image, movie_releaseDate, movie_bookmarked);
        recyclerView.setAdapter(adapterClass);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, col));

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        open_settings = findViewById(R.id.open_settings);
        home = findViewById(R.id.home);
        bookmarked = findViewById(R.id.bookmarked);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        nightMode = sp.getBoolean("theme_key_setting", false);
        if(nightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        open_settings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        menu.setOnClickListener(v -> openDrawer(drawerLayout));

        home.setOnClickListener(v -> recreate());

        bookmarked.setOnClickListener(v -> redirectActivity(MainActivity.this, BookmarkedActivity.class));

    }

    void storeData(){
        String[] projection = {"_ID", "title", "description", "picture", "releaseDate", "bookmarked"};
        Cursor cursor = getContentResolver().query(CONTENT_URI, projection, null, null,null);
        //Cursor cursor = movieDataDbHelper.readAllData();
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

        /**if(cursor.getCount() == 0){
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
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