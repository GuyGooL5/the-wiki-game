package com.oog.thewikigame.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oog.thewikigame.R;
import com.oog.thewikigame.databinding.ActivityGameConfigBinding;
import com.oog.thewikigame.models.IconButtonModel;
import com.oog.thewikigame.models.TwoLineSwitchModel;
import com.oog.thewikigame.utilities.LogTag;
import com.oog.thewikigame.utilities.Logger;

import java.util.concurrent.CompletableFuture;

public class GameConfigActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActivityGameConfigBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_game_config);

        binding.setStartArticleString("Son Goku");
        binding.setStartArticleIconButtonModel(new IconButtonModel(this, R.drawable.ic_baseline_refresh_24, v -> {

            binding.setStartArticleString(this.getString(R.string.text_loading));
            //TODO: Add web view integration.
            //TODO: delete this simulation.
            CompletableFuture.runAsync(()->{
                binding.setStartArticleString(this.getString(R.string.text_loading));
                try {
                    Thread.sleep(3000);
                    binding.setStartArticleString("Makita");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }));


        binding.setTimeLimitTwoLineSwitchModel(new TwoLineSwitchModel("Time Limit","This will set a time limit to finish the game.") {
            @Override
            public void onToggle(boolean checked) {
                //TODO: Add time limit functionality.
            }
        });

    }
}