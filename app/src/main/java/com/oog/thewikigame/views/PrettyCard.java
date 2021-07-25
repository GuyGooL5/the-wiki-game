package com.oog.thewikigame.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.card.MaterialCardView;
import com.oog.thewikigame.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PrettyCard extends MaterialCardView {
    public PrettyCard(@NonNull @NotNull Context context) {
        super(context);
        super.setBackground(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.pretty_card, null)));
    }

    public PrettyCard(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        super.setBackground(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.pretty_card, null)));

    }

    public PrettyCard(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setBackground(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.pretty_card, null)));
    }

    public void setShapeBackgroundColor(@ColorRes int id) {
        @SuppressLint("ResourceType") int color = getResources().getColor(id);
        GradientDrawable shapeDrawable = (GradientDrawable) getBackground();
        shapeDrawable.setColor(color);
        setBackground(shapeDrawable);

    }
}
