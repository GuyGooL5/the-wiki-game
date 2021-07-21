package com.oog.thewikigame.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.oog.thewikigame.R;
import com.oog.thewikigame.databinding.ActivityGameBinding;
import com.oog.thewikigame.handlers.Game;
import com.oog.thewikigame.handlers.Page;
import com.oog.thewikigame.handlers.RescueType;
import com.oog.thewikigame.utilities.LogTag;
import com.oog.thewikigame.utilities.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

//TODO: Implement Exit.

public class GameActivity extends AppCompatActivity {

    public static final String INTENT_GAME_CONFIG = "CONFIG";
    ActivityGameBinding binding;
    Game game;
    public Game.GameConfig gameConfig;
    Handler timeHandler;
    String findText = "";

    BadgeDrawable goBackBadge;
    BadgeDrawable showLinksOnlyBadge;
    BadgeDrawable findInTextBadge;

    boolean isRunning = false;


    @SuppressLint("UnsafeExperimentalUsageError") //BadgeDrawable is experimental
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_game);

        gameConfig = (Game.GameConfig) getIntent().getExtras().get(INTENT_GAME_CONFIG);


        //TODO: Handle this better
        if (gameConfig == null) {
            finish();
            return;
        }

        //Initializers.
        timeHandler = new Handler();

        goBackBadge = BadgeDrawable.create(this);
        showLinksOnlyBadge = BadgeDrawable.create(this);
        findInTextBadge = BadgeDrawable.create(this);
        game = new Game(binding.gameWebView, "Son_Goku", "Monkey_King", gameConfig) {
            @Override
            protected void onGameFinished(boolean success, List<Page.PageSummary> pageSummaryList) {
                for (Page.PageSummary pageSummary : pageSummaryList)
                    Logger.log(LogTag.HANDLERS, pageSummary);
            }

            @Override
            protected void onRescueUseSuccess(RescueType rescueType, int amountLeft) {
                BadgeDrawable selectedBadge;
                switch (rescueType) {
                    case SHOW_LINKS_ONLY:
                        selectedBadge = showLinksOnlyBadge;
                        break;
                    case FIND_IN_TEXT:
                        selectedBadge = findInTextBadge;
                        break;
                    case GO_BACK:
                        selectedBadge = goBackBadge;
                        break;
                    default:
                        return;
                }
                if (amountLeft == Game.UNLIMITED) selectedBadge.setVisible(false);
                else selectedBadge.setNumber(amountLeft);
            }

            @Override
            protected void onRescueUseFailed(RescueType rescueType, CharSequence reason) {
                Snackbar failedRescueSnackbar = Snackbar.make(binding.getRoot(), reason, Snackbar.LENGTH_SHORT);
                failedRescueSnackbar.setAction(R.string.button_ok, v -> failedRescueSnackbar.dismiss()).show();
            }
        };

        if (game.getGameStats().getNumOfGoBack() == Game.UNLIMITED)
            goBackBadge.setVisible(false);
        else goBackBadge.setNumber(game.getGameStats().getNumOfGoBack());

        if (game.getGameStats().getNumOfShowLinksOnly() == Game.UNLIMITED)
            showLinksOnlyBadge.setVisible(false);
        else showLinksOnlyBadge.setNumber(game.getGameStats().getNumOfShowLinksOnly());

        if (game.getGameStats().getNumOfFindInText() == Game.UNLIMITED)
            findInTextBadge.setVisible(false);
        else findInTextBadge.setNumber(game.getGameStats().getNumOfFindInText());

        binding.gameToolbarId.getMenu().findItem(R.id.game_menu_go_back_id).setOnMenuItemClickListener(item -> {
            game.useGoBack();
            return false;
        });
        binding.gameToolbarId.getMenu().findItem(R.id.game_menu_show_links_only_id).setOnMenuItemClickListener(item -> {
            game.toggleShowLinksOnly();
            return false;
        });
        binding.gameToolbarId.getMenu().findItem(R.id.game_menu_find_in_text_id).setOnMenuItemClickListener(item -> {
            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
            View dialogView = LayoutInflater.from(dialogBuilder.getContext()).inflate(R.layout.dialog_view_find_in_text, null);
            EditText textField = dialogView.findViewById(R.id.dialog_find_in_text_input_id);
            textField.setText(findText);
            dialogBuilder.setTitle(R.string.game_find_in_text);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setNeutralButton(R.string.button_cancel, null);
            dialogBuilder.setPositiveButton(R.string.game_button_find, (dialogInterface, v) ->
                    game.useFindInText(findText = textField.getText().toString()));
            dialogBuilder.show();
            return false;
        });


        BadgeUtils.attachBadgeDrawable(goBackBadge, binding.gameToolbarId, R.id.game_menu_go_back_id);
        BadgeUtils.attachBadgeDrawable(showLinksOnlyBadge, binding.gameToolbarId, R.id.game_menu_show_links_only_id);
        BadgeUtils.attachBadgeDrawable(findInTextBadge, binding.gameToolbarId, R.id.game_menu_find_in_text_id);

        loopTimer();


    }


    /**
     * This method will update the title every 200ms and
     * call game failed on time end.
     */
    private void loopTimer() {
        timeHandler.postDelayed(() -> {
            long limit;
            if (gameConfig.getTimeLimit() != Game.UNLIMITED) {
                limit = gameConfig.getTimeLimit() - game.getTimeElapsedMillis();
                if (limit <= 0) game.failed();
                else loopTimer();
            } else {
                limit = game.getTimeElapsedMillis();
                loopTimer();
            }
            binding.gameToolbarId.setTitle(timeToString(limit) + " " + jumpsToString());
        }, 200);
    }

    private String jumpsToString() {
        if (gameConfig.getNumOfJumps() == Game.UNLIMITED)
            return game.getJump() + "/∞";
        else return game.getJump() + "/" + gameConfig.getNumOfJumps();
    }

    private String timeToString(long milliTime) {
        long hours = TimeUnit.MILLISECONDS.toHours(milliTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliTime) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliTime) % 60;
        String hoursString = hours > 0 ? (hours < 10 ? "0" + hours : "" + hours) + ":" : "";
        String minutesString = (minutes < 10 ? "0" + minutes : "" + minutes) + ":";
        String secondsString = seconds < 10 ? "0" + seconds : "" + seconds;
        return hoursString + minutesString + secondsString;
    }


}