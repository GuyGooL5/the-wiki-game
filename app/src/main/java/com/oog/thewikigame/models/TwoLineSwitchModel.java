package com.oog.thewikigame.models;

import android.content.Context;

import androidx.annotation.StringRes;

abstract public class TwoLineSwitchModel extends TwoLineModel {
    private boolean checked;
    public TwoLineSwitchModel(String primaryTitle, String secondaryTitle) {
        super(primaryTitle, secondaryTitle);
    }
    public TwoLineSwitchModel(Context context, @StringRes int primaryTitleRes, @StringRes int secondaryTitleRes ){
        super(context, primaryTitleRes,secondaryTitleRes);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        onToggle(this.checked);
    }

    abstract public void onToggle(boolean checked);

}
