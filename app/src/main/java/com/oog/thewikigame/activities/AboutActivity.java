package com.oog.thewikigame.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.oog.thewikigame.R;
import com.oog.thewikigame.databinding.ActivityAboutBinding;
import com.oog.thewikigame.models.AuthorModel;
import com.oog.thewikigame.utilities.LogTag;
import com.oog.thewikigame.utilities.Logger;
import com.oog.thewikigame.views.PrettyCard;

public class AboutActivity extends AppCompatActivity {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_about);


        binding.aboutToolbarId.setNavigationOnClickListener(v -> finish());

        AuthorModel authorModel1 =new AuthorModel("Omer Gez", "omergez72@gmail.com", "https://www.linkedin.com/in/omer-gez-004b931b0/");
        AuthorModel authorModel2 = new AuthorModel("Guy Tsitsiashvili", "guygool4@gmail.com", "https://www.linkedin.com/in/guygool5/");
        AuthorModel authorModel3 = new AuthorModel("Omer Cohen", "omerc1997@gmail.com", "https://www.linkedin.com/in/omer-cohen-400583201/");


        binding.aboutAuthor1Id.authorExpandableCardId.setOnClickListener(v -> {
            authorModel1.toggle();
            ((PrettyCard) v).setShapeBackgroundColor(authorModel1.isVisible()?R.color.lightBlue_700:R.color.transparent);
        });
        binding.aboutAuthor2Id.authorExpandableCardId.setOnClickListener(v -> {
            authorModel2.toggle();
            ((PrettyCard) v).setShapeBackgroundColor(authorModel2.isVisible()?R.color.lightBlue_700:R.color.transparent);
        });
        binding.aboutAuthor3Id.authorExpandableCardId.setOnClickListener(v -> {
            authorModel3.toggle();
            ((PrettyCard) v).setShapeBackgroundColor(authorModel3.isVisible()?R.color.lightBlue_700:R.color.transparent);
        });

        binding.setAuthor1Model(authorModel1);
        binding.setAuthor2Model(authorModel2);
        binding.setAuthor3Model(authorModel3);
    }
}