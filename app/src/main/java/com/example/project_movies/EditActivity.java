package com.example.project_movies;

import static com.example.project_movies.MovieDataContract.MovieData.COLUMN_NAME_BOOKMARKED;
import static com.example.project_movies.MovieDataContract.MovieData.COLUMN_NAME_DESCRIPTION;
import static com.example.project_movies.MovieDataContract.MovieData.COLUMN_NAME_RELEASE_DATE;
import static com.example.project_movies.MovieDataContract.MovieData.COLUMN_NAME_TITLE;
import static com.example.project_movies.MovieDataContract.MovieData.CONTENT_URI;
import static com.example.project_movies.MovieDataContract.MovieData.TABLE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {
    EditText title_input, description_input, image_input;
    TextView releaseDate_input, bookmark_input;
    FloatingActionButton fab_add_img, fab_delete_img;
    ImageView IVPreviewImage, bookmark_true, bookmark_false, menu, open_settings;
    int selected_picture = 200, bookmark;
    boolean confirm_delete, confirm_edit;
    Button edit_button, pickDateBtn, delete_button;
    String id, title, description, releaseDate;
    DrawerLayout drawerLayout;
    LinearLayout home, bookmarked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Objects.requireNonNull(getSupportActionBar()).hide();

        title_input = findViewById(R.id.title_input_edit);
        description_input = findViewById(R.id.description_input_edit);
        //image_input = findViewById(R.id.image_input_edit);
        releaseDate_input = findViewById(R.id.tv_selectedDate_edit);
        bookmark_input = findViewById(R.id.bookmark_input);

        delete_button = findViewById(R.id.delete_button);
        edit_button = findViewById(R.id.edit_button);

        bookmark_false = findViewById(R.id.iv_bookmark_false);
        bookmark_true = findViewById(R.id.iv_bookmark_true);

        getAndSetIntentData();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        confirm_edit = sp.getBoolean("confirm_edit_key_setting", false);
        confirm_delete = sp.getBoolean("confirm_delete_key_setting", false);

        bookmark_true.setOnClickListener(v -> {
            Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME_BOOKMARKED, 0);
            getContentResolver().update(Uri.parse(CONTENT_URI + "/" + id), contentValues, null, null);
            /*MovieDataDbHelper movieDataDbHelper = new MovieDataDbHelper(EditActivity.this);
            movieDataDbHelper.bookmark(id, 0);*/
            bookmark_false.setVisibility(View.VISIBLE);
            bookmark_true.setVisibility(View.INVISIBLE);
            Toast.makeText(EditActivity.this, "Successfully Un-bookmarked", Toast.LENGTH_SHORT).show();
        });

        bookmark_false.setOnClickListener(v -> {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME_BOOKMARKED, 1);
            getContentResolver().update(Uri.parse(CONTENT_URI + "/" + id), contentValues, null, null);

            /*MovieDataDbHelper movieDataDbHelper = new MovieDataDbHelper(EditActivity.this);
            movieDataDbHelper.bookmark(id, 1);*/
            bookmark_false.setVisibility(View.INVISIBLE);
            bookmark_true.setVisibility(View.VISIBLE);
            Toast.makeText(EditActivity.this, "Successfully Bookmarked", Toast.LENGTH_SHORT).show();
        });

        edit_button.setOnClickListener(v -> {
            if(confirm_edit){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("Save Changes?");
                builder.setMessage("Are u sure u want to save changes made?");
                builder.setPositiveButton("Confirm",
                        (dialog, which) -> {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(COLUMN_NAME_TITLE, title_input.getText().toString().trim());
                            contentValues.put(COLUMN_NAME_DESCRIPTION, description_input.getText().toString().trim());
                            contentValues.put(COLUMN_NAME_RELEASE_DATE, releaseDate_input.getText().toString().trim());
                            getContentResolver().update(Uri.parse(CONTENT_URI + "/" + id), contentValues, null, null);

                            /*MovieDataDbHelper movieDataDbHelper = new MovieDataDbHelper(EditActivity.this);
                            title = title_input.getText().toString().trim();
                            description = description_input.getText().toString().trim();
                            releaseDate = releaseDate_input.getText().toString().trim();
                            movieDataDbHelper.editData(id, title, description, releaseDate);*/
                            Intent intent = new Intent(EditActivity.this, MainActivity.class);
                            startActivity(intent);
                        });
                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else{
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_NAME_TITLE, title_input.getText().toString().trim());
                contentValues.put(COLUMN_NAME_DESCRIPTION, description_input.getText().toString().trim());
                contentValues.put(COLUMN_NAME_RELEASE_DATE, releaseDate_input.getText().toString().trim());
                getContentResolver().update(Uri.parse(CONTENT_URI + "/" + id), contentValues, null, null);
                /*MovieDataDbHelper movieDataDbHelper = new MovieDataDbHelper(EditActivity.this);
                title = title_input.getText().toString().trim();
                description = description_input.getText().toString().trim();
                releaseDate = releaseDate_input.getText().toString().trim();
                movieDataDbHelper.editData(id, title, description, releaseDate);*/
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }


        });

        delete_button.setOnClickListener(v -> {
            if(confirm_delete){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("Delete?");
                builder.setMessage("Are u sure u want to delete movie: " + id + "?");
                builder.setPositiveButton("Confirm",
                        (dialog, which) -> {
                            try{
                                getContentResolver().delete(Uri.parse(CONTENT_URI + "/" + id), null, null);
                            }
                            catch (Exception e){
                                Toast.makeText(EditActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                            }

                            Intent intent = new Intent(EditActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(EditActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();


                            /*MovieDataDbHelper movieDataDbHelper = new MovieDataDbHelper(EditActivity.this);
                            movieDataDbHelper.deleteMovie(id);
                            Intent intent = new Intent(EditActivity.this, MainActivity.class);
                            startActivity(intent);*/
                        });
                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                try{
                    getContentResolver().delete(Uri.parse(CONTENT_URI + "/" + id), null, null);
                }
                catch (Exception e){
                    Toast.makeText(EditActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                }
                /*MovieDataDbHelper movieDataDbHelper = new MovieDataDbHelper(EditActivity.this);
                movieDataDbHelper.deleteMovie(id);*/
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        IVPreviewImage = findViewById(R.id.IV_PreviewImage);
        fab_add_img = findViewById(R.id.fab_add_img);
        fab_delete_img = findViewById(R.id.fab_delete_img);
        fab_add_img.setOnClickListener(v -> imageChooser());

        fab_delete_img.setOnClickListener(v -> {
            IVPreviewImage.setImageURI(null);
            fab_add_img.setVisibility(View.VISIBLE);
            fab_delete_img.setVisibility(View.INVISIBLE);
            Toast.makeText(EditActivity.this, "Has Image", Toast.LENGTH_SHORT).show();
        });


        pickDateBtn = findViewById(R.id.btn_datePicker);
        pickDateBtn.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        releaseDate_input.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
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
            Intent intent = new Intent(EditActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        menu.setOnClickListener(v -> openDrawer(drawerLayout));

        home.setOnClickListener(v -> redirectActivity(EditActivity.this, MainActivity.class));

        bookmarked.setOnClickListener(v -> redirectActivity(EditActivity.this, BookmarkedActivity.class));
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
    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title")
                && getIntent().hasExtra("description")
                && getIntent().hasExtra("releaseDate")){
            // Getting data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            description = getIntent().getStringExtra("description");
            releaseDate = getIntent().getStringExtra("releaseDate");
            bookmark = Integer.parseInt(getIntent().getStringExtra("bookmarked"));

            if(bookmark == 0){
                bookmark_false.setVisibility(View.VISIBLE);
                bookmark_true.setVisibility(View.INVISIBLE);
            }else if(bookmark == 1){
                bookmark_false.setVisibility(View.INVISIBLE);
                bookmark_true.setVisibility(View.VISIBLE);
            }

            title_input.setText(title);
            description_input.setText(description);
            releaseDate_input.setText(releaseDate);



        }else{
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
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
