package com.oog.thewikigame.Handlers;

import android.webkit.WebView;

import java.util.Stack;

abstract public class Game {

    /*  This stack will hold all the visited pages.
        We decided to use a stack because it really fits into our design of popping the last visited page if the player chooses so.
     */
    private final Stack<Page> pageStack=new Stack<>();
    private final WebViewHandler webViewHandler ;
    private int jumps = 0;
    private final long startTime = System.nanoTime();
    private final GamePreferences gamePreferences;

    public class GamePreferences {
        public int n_Jumps;
        public int n_Returns;
        public int n_FindInText;
        public int n_ShowLinksOnly;

        public GamePreferences(int n_Jumps, int n_Returns, int n_FindInText, int n_ShowLinksOnly) {
            this.n_Jumps = n_Jumps;
            this.n_Returns = n_Returns;
            this.n_FindInText = n_FindInText;
            this.n_ShowLinksOnly = n_ShowLinksOnly;
        }

        public boolean useRescue(RescueType help) {
            switch (help) {
                case SHOW_LINKS_ONLY:
                    if (n_ShowLinksOnly == 0) return false;
                    if (gamePreferences.n_ShowLinksOnly != -1) n_ShowLinksOnly--;
                    break;
                case FIND_IN_TEXT:
                    if (n_FindInText == 0) return false;
                    if (n_FindInText != -1) n_FindInText--;
                    break;
                case RETURN_BACK:
                    if (n_Returns == 0) return false;
                    if (n_Returns != -1) n_Returns--;
                    break;
            }
            return true;
        }
    }

    public Game(WebView webView, GamePreferences gamePreferences){
        webViewHandler = new WebViewHandler(webView) {
            @Override
            public void onNextArticle(String article) {
                pageStack.peek().exit();
                jumps++;
                addNextPage(article,jumps);
            }
        };
        this.gamePreferences = gamePreferences;
    }

    private void addNextPage(String article,int nextJump){
        Page nextPage = new Page(article,nextJump,System.nanoTime()-startTime) {

            @Override
            void onEnteredPage(String article,boolean usedFindInText, boolean usedShowLinksOnly) {
                if(usedFindInText) onUnlock(RescueType.FIND_IN_TEXT);
                if(usedShowLinksOnly) onUnlock(RescueType.SHOW_LINKS_ONLY);
                webViewHandler.loadArticle(article);
            }

            @Override
            void onExitedPage() {
                onExitPage();
            }

            @Override
            void onUnlockedHelp(RescueType help) {
                onUnlock(help);
            }
        };
        pageStack.push(nextPage);
    }

    /**
     * This needs to be implemented by the game activity to handle exiting a page.
     * This will be used to lock all helps on the game activity.
     */
    abstract void onExitPage();

    /**
     * This needs to be implemented to unlock the ui elements and logic that related to the specified help
     * @param help - The rescue that has been unlocked.
     */
    abstract void onUnlock(RescueType help);



    //TODO: Change the reason to resource string id.
    /**
     * This will be an error handler that is called when an unlock failed
     * @param help - The rescue that has failed unlocking.
     * @param reason - The reason why it happened.
     */
    abstract void onUnlockFailed(RescueType help, String reason);

    /**
     * This method will be called when trying to unlock a certain help.
     * It will deploy a chain of events that consequentially tell the game activity how to act.
     * @param help - The rescue that we try to unlock.
     */
    void unlockHelp(RescueType help){
        if(!gamePreferences.useRescue(help)) {
            onUnlockFailed(help,"Used Maximum Unlocks");
            return;
        }
        if (help == RescueType.RETURN_BACK) {
            if (jumps == 0) {
                onUnlockFailed(RescueType.RETURN_BACK, "Reached first article, nowhere to go back to");
                return;
            }
            jumps--;
            pageStack.pop();
            pageStack.peek().enter();
        }
        else pageStack.peek().unlockHelp(help);
    }

}
