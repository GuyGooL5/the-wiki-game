package com.oog.thewikigame.handlers;

import android.annotation.SuppressLint;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.StringRes;

import com.google.android.material.snackbar.Snackbar;
import com.oog.thewikigame.R;
import com.oog.thewikigame.utilities.LogTag;
import com.oog.thewikigame.utilities.Logger;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;



abstract public class WebViewHandler{

    //The wikipedia base url
    private static final String WIKI_BASE_URL = "m.wikipedia.org";
    //The endpoint for the articles
    private static final String WIKI_BASE_ARTICLE_ENDPOINT = "wiki";

    abstract public void onNextArticle(String article);

    //An enum for all playable wikipedia languages.
    enum Language {
        ENGLISH,
        HEBREW,
        FRENCH,
        ARABIC,
        SPANISH
    }

    //This map will reference each Language entry to it's corresponding string name
    private static final Map<Language, String> WIKI_LANGUAGE_MAP = new HashMap<Language, String>() {{
        put(Language.ENGLISH, "en");
        put(Language.HEBREW, "he");
        put(Language.FRENCH, "fr");
        put(Language.ARABIC, "ar");
        put(Language.SPANISH, "es");
    }};

    //The WebView this class handles
    private final WebView webView;
    //The language of the wikipedia pages
    private Language activeLanguage;
    //The current loaded url.
    private String currentUrl = null;

    /**
     * This Class will hold a WebView that is manipulated by it's methods to abstract the use of WebView methods.
     * It will also handle all the web interactions including navigation, page handling and alerting errors.
     *
     * @param webView - The view to hold and manipulate
     * @param lang - The language of the wikipedia game.
     */
    @SuppressLint("SetJavaScriptEnabled")
    public WebViewHandler(@NotNull WebView webView, Language lang) {
        this.activeLanguage = lang;
        this.webView = webView;

        WebViewClient mainClient = new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Logger.log(LogTag.WEB_VIEW_HANDLER, "Finished loading the page", url);
                injectSimplifyContent();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String validURLPrefix = constructURL(null, activeLanguage);
                String newURL = request.getUrl().toString();

                if (!newURL.startsWith(validURLPrefix)) {
                    view.loadUrl(currentUrl);
                    Logger.log(LogTag.WEB_VIEW_HANDLER,"URL:",newURL,"doesn't match validURLPrefix:",validURLPrefix);
                    alert(R.string.alert_invalid_link);
                    return false;
                }
                if(!currentUrl.equals(newURL)) {
                    currentUrl = newURL;
                    onNextArticle(newURL);
                }
                return false;
            }
        };

        webView.setWebViewClient(mainClient);
        webView.getSettings().setJavaScriptEnabled(true);

    }

    /**
     * The default constructor for English language
     * @param webView - The WebView to manipuate
     */
    public WebViewHandler(WebView webView) {
        this(webView, Language.ENGLISH);
    }

    /**
     * Loads a wikipedia article to the WebView.
     * @param article - The article endpoint to load
     * @param lang - The language of the article
     */
    public void loadArticle(String article, Language lang) {
        String url = constructURL(article, lang);
        currentUrl = url;
        webView.loadUrl(url);
    }

    /**
     * Loads a wikipedia article to the WebView with a default language of English.
     * @param article - The article endpoint to load
     */
    public void loadArticle(String article) {
        loadArticle(article, Language.ENGLISH);
    }

    /**
     * This method will construct a wikipedia friendly url with the given params
     * @param article - the article endpoint
     * @param lang - the language of the page
     * @return the formatted url of the article.
     */
    public static @NotNull String constructURL(String article, Language lang) {
        if (article == null) article = "";
        return String.format("https://%s.%s/%s/%s",
                WIKI_LANGUAGE_MAP.get(lang),
                WIKI_BASE_URL,
                WIKI_BASE_ARTICLE_ENDPOINT,
                article);
    }

    /**
     * This method will construct a wikipedia friendly url with the given article, default language is English.
     * @param article - the article endpoint
     * @return the formatted url of the article.
     */
    public static String constructURL(String article) {
        return constructURL(article, Language.ENGLISH);
    }

    /*

     PRIVATE METHODS

     */

    /**
     * This will inject a javascript snippet to the page that strips it down to the article only without the content around.
     */
    private void injectSimplifyContent() {
        String js = "javascript:" +
                "var v = document.querySelector(\"#mw-mf-viewport\");" +
                "const a = ()=>v.parentNode.replaceChild(document.querySelector(\"main#content\"),v);" +
                "a();";
        webView.loadUrl(js);
    }


    /**
     * This method will pop a Snackbar with a given message and a dismiss button.
     * @param resID - The message (resource ID) to view.
     */
    private void alert(@StringRes int resID) {
        Snackbar snackbar;
        snackbar = Snackbar.make(webView, resID, Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.button_ok,(v) -> snackbar.dismiss()).show();
    }
}
