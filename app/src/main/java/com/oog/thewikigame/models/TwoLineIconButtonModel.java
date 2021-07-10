package com.oog.thewikigame.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;

import com.oog.thewikigame.BR;

public class TwoLineIconButtonModel extends TwoLineModel {


    private IconButtonModel iconButtonModel;


    public TwoLineIconButtonModel(String primaryTitle, String secondaryTitle, IconButtonModel iconButtonModel) {
        super(primaryTitle, secondaryTitle);
        initIconButtonModel(iconButtonModel);
    }

    public TwoLineIconButtonModel(Context context, @StringRes int primaryTitleRes, @StringRes int secondaryTitleRes, IconButtonModel iconButtonModel) {
        super(context, primaryTitleRes, secondaryTitleRes);
        initIconButtonModel(iconButtonModel);
    }


    @Bindable
    public IconButtonModel getIconButtonModel() {
        return iconButtonModel;
    }

    public void setIconButtonModel(IconButtonModel iconButtonModel) {
        this.iconButtonModel = iconButtonModel;
        notifyPropertyChanged(BR.iconButtonModel);
    }


    private void initIconButtonModel(IconButtonModel iconButtonModel) {
        setIconButtonModel(iconButtonModel);
        iconButtonModel.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                notifyPropertyChanged(BR.iconButtonModel);
            }
        });
    }

}
