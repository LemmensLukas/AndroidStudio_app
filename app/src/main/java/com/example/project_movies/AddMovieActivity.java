package com.example.project_movies;

import static com.example.project_movies.MovieDataContract.AUTHORITY;
import static com.example.project_movies.MovieDataContract.BASE_CONTENT_URI;
import static com.example.project_movies.MovieDataContract.MovieData.COLUMN_NAME_BOOKMARKED;
import static com.example.project_movies.MovieDataContract.MovieData.COLUMN_NAME_DESCRIPTION;
import static com.example.project_movies.MovieDataContract.MovieData.COLUMN_NAME_RELEASE_DATE;
import static com.example.project_movies.MovieDataContract.MovieData.COLUMN_NAME_TITLE;
import static com.example.project_movies.MovieDataContract.MovieData.CONTENT_URI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Objects;

public class AddMovieActivity extends AppCompatActivity {

    Button pickDateBtn, add_movie;
    TextView selectedDateTV;
    EditText title_input, description_input, image_input;
    ImageView IVPreviewImage;
    int selected_picture = 200;

    Context mContext;

    FloatingActionButton fab_add_img;
    FloatingActionButton fab_delete_img;

    DrawerLayout drawerLayout;
    LinearLayout home, bookmarked;
    ImageView menu, open_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        Objects.requireNonNull(getSupportActionBar()).hide();
        title_input = findViewById(R.id.add_movie_title);
        description_input = findViewById(R.id.add_movie_description);
        //image_input = findViewById(R.id.);
        add_movie = findViewById(R.id.add_movie);
        add_movie.setOnClickListener(v -> {
            try{
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_NAME_TITLE, title_input.getText().toString().trim());
                contentValues.put(COLUMN_NAME_DESCRIPTION, description_input.getText().toString().trim());
                contentValues.put(COLUMN_NAME_RELEASE_DATE, selectedDateTV.getText().toString().trim());
                contentValues.put(COLUMN_NAME_BOOKMARKED, 0);
                getContentResolver().insert(CONTENT_URI, contentValues);
                Intent intent = new Intent(AddMovieActivity.this, MainActivity.class);
                startActivity(intent);
            }
            catch (Exception e){
                Toast.makeText(AddMovieActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }


            /*MovieDataDbHelper movieDataDbHelper = new MovieDataDbHelper(AddMovieActivity.this);
            movieDataDbHelper.addMovie(title_input.getText().toString().trim(),
                    description_input.getText().toString().trim(),
                    selectedDateTV.getText().toString().trim(), 0);
            Intent intent = new Intent(AddMovieActivity.this, MainActivity.class);
            startActivity(intent);*/
        });

        IVPreviewImage = findViewById(R.id.IV_PreviewImage);
        fab_add_img = findViewById(R.id.fab_add_img);
        fab_delete_img = findViewById(R.id.fab_delete_img);

        fab_add_img.setOnClickListener(v -> imageChooser());

        fab_delete_img.setOnClickListener(v -> {
            IVPreviewImage.setImageURI(null);
            fab_add_img.setVisibility(View.VISIBLE);
            fab_delete_img.setVisibility(View.INVISIBLE);
            Toast.makeText(AddMovieActivity.this, "Has Image", Toast.LENGTH_SHORT).show();
        });

        pickDateBtn = findViewById(R.id.btn_datePicker);
        selectedDateTV = findViewById(R.id.tv_selectedDate);

        pickDateBtn.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(

                    AddMovieActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        selectedDateTV.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        open_settings = findViewById(R.id.open_settings);
        home = findViewById(R.id.home);
        bookmarked = findViewById(R.id.bookmarked);

        open_settings.setOnClickListener(v -> {
            Intent intent = new Intent(AddMovieActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        menu.setOnClickListener(v -> openDrawer(drawerLayout));

        home.setOnClickListener(v -> redirectActivity(AddMovieActivity.this, MainActivity.class));


        bookmarked.setOnClickListener(v -> redirectActivity(AddMovieActivity.this, BookmarkedActivity.class));
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    void imageChooser() {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), selected_picture);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == selected_picture) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    fab_add_img.setVisibility(View.INVISIBLE);
                    fab_delete_img.setVisibility(View.VISIBLE);

                    // update the preview image in the layout
                    IVPreviewImage.setImageURI(selectedImageUri);
                    Log.d("message", selectedImageUri.toString());
                }
            }
        }

    }
}