package com.oog.thewikigame.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.oog.thewikigame.R;
import com.oog.thewikigame.databinding.ActivityMainBinding;
import com.oog.thewikigame.utilities.LogTag;
import com.oog.thewikigame.utilities.Logger;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: Clear this clutter and encapsulate ton of shit...
        Logger.log(LogTag.MAIN_ACTIVITY, "Created MainActivity");

        String localeString = getSharedPreferences("SETTINGS",MODE_PRIVATE).getString("lang","en");
        locale = new Locale.Builder().setLanguage(localeString).build();
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        binding.mainButtonPlayId.setOnClickListener(v -> startActivityIntent(GameConfigActivity.class));
        binding.mainButtonRecordsId.setOnClickListener(v -> startActivityIntent(GameRecordsActivity.class));
        binding.mainButtonSettingsId.setOnClickListener(v -> startActivityIntent(SettingsActivity.class));
        binding.mainButtonAboutId.setOnClickListener(v -> startActivityIntent(AboutActivity.class));

    }

    private void startActivityIntent(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }


    //TODO: Change to startActivityForResult and listen to changes only from settings.
    @Override
    protected void onResume() {
        super.onResume();
        String localeString= getSharedPreferences("SETTINGS",MODE_PRIVATE).getString("lang","en");
        Locale testNewLocale = new Locale.Builder().setLanguage(localeString).build();
        if(!locale.equals(testNewLocale)) recreate();
    }
}