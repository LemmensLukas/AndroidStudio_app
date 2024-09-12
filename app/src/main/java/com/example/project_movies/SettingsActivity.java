package com.example.project_movies;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals("theme_key_setting")){
            boolean pref = sharedPreferences.getBoolean("theme_key_setting", false);
            if(pref){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor = sharedPreferences.edit();
                editor.putBoolean("theme_key_setting", true);
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor = sharedPreferences.edit();
                editor.putBoolean("theme_key_setting", false);
            }
            editor.apply();
        }

        if(s.equals("confirm_edit_key_setting")){
            boolean pref = sharedPreferences.getBoolean("confirm_edit_key_setting", false);
            editor = sharedPreferences.edit();
            if(pref){
                editor.putBoolean("confirm_edit_key_setting", true);
            }
            else {
                editor.putBoolean("confirm_edit_key_setting", false);
            }
            editor.apply();
        }

        if(s.equals("confirm_delete_key_setting")){
            boolean pref = sharedPreferences.getBoolean("confirm_delete_key_setting", false);
            editor = sharedPreferences.edit();
            if(pref){
                editor.putBoolean("confirm_delete_key_setting", true);
            }
            else {
                editor.putBoolean("confirm_delete_key_setting", false);
            }
            editor.apply();
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.root_preferences);
        }
    }
}