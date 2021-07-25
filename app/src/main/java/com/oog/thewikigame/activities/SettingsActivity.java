package com.oog.thewikigame.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.oog.thewikigame.R;
import com.oog.thewikigame.databinding.ActivitySettingsBinding;
import com.oog.thewikigame.handlers.LocaleHandler;
import com.oog.thewikigame.handlers.ThemeHandler;
import com.oog.thewikigame.models.IconButtonModel;
import com.oog.thewikigame.models.TwoLineIconButtonModel;
import com.oog.thewikigame.models.TwoLineSwitchModel;
import com.oog.thewikigame.utilities.ActivityResultUtil;
import com.oog.thewikigame.utilities.LogTag;
import com.oog.thewikigame.utilities.Logger;
import com.oog.thewikigame.wrappers.SharedPreferencesWrapper;


public class SettingsActivity extends AppCompatActivity {


    private static final int THEME_INDEX_DEFAULT = 0;
    private static final int THEME_INDEX_DARK = 1;
    private static final int THEME_INDEX_LIGHT = 2;
    LocaleHandler.LocaleCode initialLocaleCode;
    ActivitySettingsBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeHandler.updateSystemToSavedTheme(this);

        super.onCreate(savedInstanceState);


        initialLocaleCode = (LocaleHandler.LocaleCode) getIntent().getSerializableExtra("LocaleCode");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        binding.settingsAppbarToolbarId.setNavigationOnClickListener(view -> finish());

        initSoundsSwitch();
        initLanguageSelect();
        initThemeSelect();


    /*
      Reset all data alert builder
      This will build an alert dialog that will make sure the user wants to delete all data.
     */
        MaterialAlertDialogBuilder resetAllDataDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Danger);
        resetAllDataDialogBuilder.setTitle(R.string.settings_dialog_reset_title);
        resetAllDataDialogBuilder.setMessage(R.string.settings_dialog_reset_description);
        resetAllDataDialogBuilder.setPositiveButton(R.string.text_no, ((dialogInterface, which) -> dialogInterface.cancel()));
        resetAllDataDialogBuilder.setNegativeButton(R.string.text_yes, ((dialogInterface, i) ->

        {
            SharedPreferencesWrapper.make(this, SharedPreferencesWrapper.Preference.SETTINGS).clearAllPreferences();
            SharedPreferencesWrapper.make(this, SharedPreferencesWrapper.Preference.GAME).clearAllPreferences();
            SharedPreferencesWrapper.make(this, SharedPreferencesWrapper.Preference.USER).clearAllPreferences();
            startActivity(new Intent(this, MainActivity.class));
            Runtime.getRuntime().exit(0);
        }));


        binding.settingsResetItemId.setOnClickListener(v -> resetAllDataDialogBuilder.show());

    }

    private void initSoundsSwitch() {
        SharedPreferencesWrapper settingsSharedPreferences = SharedPreferencesWrapper
                .make(this, SharedPreferencesWrapper.Preference.SETTINGS);
        TwoLineSwitchModel soundsModel = new TwoLineSwitchModel(this,
                R.string.settings_text_sounds,
                R.string.settings_text_sounds_description) {{
            this.setOnToggleListener(checked -> {
                //TODO: Add Sound toggle Logic.
                settingsSharedPreferences.putString(
                        "MUSIC", checked ? "true" : "false");
            });
        }};
        soundsModel.setChecked(settingsSharedPreferences.getString("MUSIC","true").equals("true"));
        binding.setSounds(soundsModel);
    }

    private void initLanguageSelect() {
        //This will be initialized to show the language select dialog and update the app accordingly.
        final LocaleHandler localeHandler = new LocaleHandler(this);
        final int checkedItem = (localeHandler.getCurrentLocale() == LocaleHandler.LocaleCode.EN_US) ? 0 : 1;

        MaterialAlertDialogBuilder selectLanguageDialogBuilder = new MaterialAlertDialogBuilder(this);
        selectLanguageDialogBuilder.setSingleChoiceItems(R.array.languages, checkedItem, (dialogInterface, index) -> {
            if (checkedItem == index) {
                dialogInterface.cancel();
                return;
            }
            localeHandler.updateSystemLocale(index == 0 ? LocaleHandler.LocaleCode.EN_US : LocaleHandler.LocaleCode.HE_IL);
            recreate();
        });

        TwoLineIconButtonModel languageModel = new TwoLineIconButtonModel(this,
                R.string.settings_text_language,
                R.string.settings_text_language_description,
                new IconButtonModel(this,
                        R.drawable.ic_baseline_language_24,
                        v -> selectLanguageDialogBuilder.show()));


        binding.setLanguage(languageModel);

    }

    private int getThemeIndex(ThemeHandler.Theme theme) {
        switch (theme) {
            case LIGHT:
                return THEME_INDEX_LIGHT;
            case DARK:
                return THEME_INDEX_DARK;
            case DEFAULT:
            default:
                return THEME_INDEX_DEFAULT;
        }
    }

    private void initThemeSelect() {

        final ThemeHandler themeHandler = new ThemeHandler(this);

        MaterialAlertDialogBuilder selectThemeDialogBuilder = new MaterialAlertDialogBuilder(this);
        selectThemeDialogBuilder.setTitle(R.string.settings_text_theme);

        TwoLineIconButtonModel themeModel = new TwoLineIconButtonModel(this,
                R.string.settings_text_theme,
                R.string.settings_text_theme_description,
                new IconButtonModel(this,
                        R.drawable.ic_baseline_color_lens_24, v -> {

                    int checkedItem = getThemeIndex(themeHandler.getSavedTheme());
                    selectThemeDialogBuilder.setSingleChoiceItems(R.array.themes, checkedItem, ((dialog, which) -> {
                        if (checkedItem != which)
                            switch (which) {
                                case THEME_INDEX_DARK:
                                    themeHandler.updateSystemTheme(ThemeHandler.Theme.DARK);
                                    break;
                                case THEME_INDEX_LIGHT:
                                    themeHandler.updateSystemTheme(ThemeHandler.Theme.LIGHT);
                                    break;
                                case THEME_INDEX_DEFAULT:
                                default:
                                    themeHandler.updateSystemTheme(ThemeHandler.Theme.DEFAULT);
                                    break;
                            }
                        dialog.cancel();
                    }));

                    selectThemeDialogBuilder.show();

                }
                ));

        binding.setTheme(themeModel);

    }


    @Override
    public void finish() {
        if (initialLocaleCode != LocaleHandler.getSavedLocale(this))
            setResult(ActivityResultUtil.RESULT_UPDATE);
        super.finish();
    }

}