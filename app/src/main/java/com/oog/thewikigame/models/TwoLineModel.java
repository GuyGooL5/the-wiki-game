package com.oog.thewikigame.models;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.databinding.BaseObservable;

public class TwoLineModel extends BaseObservable {
    private String primaryText;
    private String secondaryText;

    public TwoLineModel(String primaryTitle, String secondaryTitle) {
        setPrimaryText(primaryTitle);
        setSecondaryText( secondaryTitle);
    }

    public TwoLineModel(Context context, @StringRes int primaryTitleRes, @StringRes int secondaryTitleRes) {
        this( context.getString(primaryTitleRes),context.getString(secondaryTitleRes));
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }


    public String getPrimaryText() {
        return primaryText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }


}
