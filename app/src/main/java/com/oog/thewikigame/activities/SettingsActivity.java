package com.oog.thewikigame.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.oog.thewikigame.R;
import com.oog.thewikigame.models.IconButtonModel;
import com.oog.thewikigame.models.TwoLineIconButtonModel;
import com.oog.thewikigame.utilities.LogTag;
import com.oog.thewikigame.utilities.Logger;
import com.oog.thewikigame.models.TwoLineSwitchModel;
import com.oog.thewikigame.databinding.ActivitySettingsBinding;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    String selectedLanguage = "English";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);



        /*
            This will build a dialog that prompts a language selection.
            It will be dispatched by the click on the language icon.

         */

        //TODO: extract this into a language helper class

        MaterialAlertDialogBuilder selectLanguageDialogBuilder = new MaterialAlertDialogBuilder(this);
        //TODO: Change checked item to the current language.

        final Locale heIL = new Locale.Builder().setLanguage("he").build();
        final Locale enUS = new Locale.Builder().setLanguage("en").build();
        int checkedItem = getResources().getConfiguration().locale.equals(enUS) ? 0 : 1;
        selectLanguageDialogBuilder.setSingleChoiceItems(R.array.languages, checkedItem, (dialogInterface, index) -> {
            //TODO: add language change logic.
            if (checkedItem == index) {
                dialogInterface.cancel();
                return;
            }
            selectedLanguage = getResources().getStringArray(R.array.languages)[index];
            Configuration configuration = getResources().getConfiguration();

            configuration.setLocale(index == 0 ? enUS : heIL);

            getSharedPreferences("SETTINGS", MODE_PRIVATE).edit().putString("lang", index == 0 ? "en" : "he").apply();
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
            recreate();

            dialogInterface.cancel();
        });


        TwoLineSwitchModel soundsModel = new TwoLineSwitchModel(this,
                R.string.settings_text_sounds,
                R.string.settings_text_sounds_description) {
            @Override
            public void onToggle(boolean checked) {
                Logger.log(LogTag.SETTINGS_ACTIVITY, "Toggled sounds", checked ? "on" : "off");
            }
        };
        TwoLineSwitchModel darkThemeModel = new TwoLineSwitchModel(this,
                R.string.settings_text_dark_theme,
                R.string.settings_text_dark_theme_description) {
            @Override
            public void onToggle(boolean checked) {
                Logger.log(LogTag.SETTINGS_ACTIVITY, "Toggled dark theme", checked ? "on" : "off");
            }
        };


        TwoLineIconButtonModel languageModel = new TwoLineIconButtonModel(this,
                R.string.settings_text_language,
                R.string.settings_text_language_description,
                new IconButtonModel(this, R.drawable.ic_baseline_language_24,
                        v -> selectLanguageDialogBuilder.show()));
        ;

        binding.settingsAppbarToolbarId.setNavigationOnClickListener(view -> finish());

        binding.setSounds(soundsModel);
        binding.setDarkTheme(darkThemeModel);
        binding.setLanguage(languageModel);


    /*
      Reset all data alert builder
      This will build an alert dialog that will make sure the user want s to delete all data.
     */
        MaterialAlertDialogBuilder resetAllDataDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Danger);
        resetAllDataDialogBuilder.setTitle(R.string.settings_dialog_reset_title);
        resetAllDataDialogBuilder.setMessage(R.string.settings_dialog_reset_description);
        resetAllDataDialogBuilder.setPositiveButton(R.string.text_no, ((dialogInterface, which) -> dialogInterface.cancel()));
        resetAllDataDialogBuilder.setNegativeButton(R.string.text_yes, ((dialogInterface, i) ->

        {
            //TODO: Add reset all data logic.
            Logger.log(LogTag.SETTINGS_ACTIVITY, "Clicked on Reset all data on dialog.");
        }));


        binding.settingsResetItemId.setOnClickListener(v -> resetAllDataDialogBuilder.show());

    }


}