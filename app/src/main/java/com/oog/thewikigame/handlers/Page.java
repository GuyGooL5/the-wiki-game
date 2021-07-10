package com.oog.thewikigame.handlers;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;


abstract public class Page {
    private final String article;
    private final int jumpNumber;

    private final long enterTime;
    long startTime, endTime;
    long elapsedTime =0;
    private boolean usedShowLinksOnly = false;
    private boolean usedFindInText = false;

    /*
    Public methods
     */

    /**
     * @param article - The article of the page.
     * @param jumpNumber - The jump number in the game.
     * @param enterTime - The time when the page was first entered.
     */
    public Page(@NotNull String article, int jumpNumber,long enterTime) {
        this.article = article;
        this.jumpNumber = jumpNumber;
        this.enterTime = enterTime;
        enter();
    }

    /**
     * This method allows unlock the page's specified rescue type .
     * It will be unlocked for the rest of this class' lifetime.
     * @param rescueType - The type of rescue used in the page
     */
    void unlockHelp(RescueType rescueType) {
        switch (rescueType) {
            case SHOW_LINKS_ONLY:
                if (!usedShowLinksOnly) {
                    usedShowLinksOnly = true;
                }
                break;
            case FIND_IN_TEXT:
                if (!usedFindInText) {
                    usedFindInText = true;
                }
                break;
            case RETURN_BACK:
                break;
            }
        onUnlockedHelp(rescueType);
    }

    /**
     * This needs to be called every time the page is entered.
     */
    void enter(){
        startTime = System.nanoTime();
        onEnteredPage(article, usedFindInText, usedShowLinksOnly);
    }

    /**
     * This needs to be called every time the page is exited (or switched)
     */
    void exit() {
        endTime = System.nanoTime();
        elapsedTime+=endTime-startTime;
        onExitedPage();
    }


    /**
     * This method should be overriden to notify when the page was entered.
     */
    abstract void onEnteredPage(String article, boolean usedFindInText, boolean usedShowLinksOnly);

    /**
     * This method should be overriden to notify when the page was exited.
     */
    abstract void onExitedPage();

    /**
     * This method should be overriden to listen to help uses and notify to the calling method.
     * @param help - The help that was used
     */
    abstract void onUnlockedHelp(RescueType help);

    /**
     * This method will serialize to a JSON the current state of this class.
     * @return A JSON object that holds the data of this class
     * @throws JSONException - The default JSONException
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("article", article);
        json.put("jumpNumber", jumpNumber);
        //TODO:test this' validity
        json.put("enterTime", enterTime);
        json.put("elapsedTime",elapsedTime);
        json.put("usedShowLinksOnly", usedShowLinksOnly);
        json.put("usedFindInText", usedFindInText);
        return json;
    }

}
