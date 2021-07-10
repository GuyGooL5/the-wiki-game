package com.oog.thewikigame.handlers;

import org.junit.Assert;
import org.junit.Test;

public class WebViewHandlerTest {

    @Test
    public void constructURLTest(){
        //TODO: document and give titles to each array.

        Assert.assertEquals("https://en.m.wikipedia.org/wiki/Goku",
                WebViewHandler.constructURL("Goku"));

        Assert.assertEquals("https://en.m.wikipedia.org/wiki/Goku",
                WebViewHandler.constructURL("Goku", WebViewHandler.Language.ENGLISH));

        Assert.assertEquals("https://fr.m.wikipedia.org/wiki/Goku",
                WebViewHandler.constructURL("Goku", WebViewHandler.Language.FRENCH));

        Assert.assertEquals("https://en.m.wikipedia.org/wiki/",
                WebViewHandler.constructURL(null, WebViewHandler.Language.ENGLISH));

        Assert.assertEquals("https://en.m.wikipedia.org/wiki/%s",
                WebViewHandler.constructURL("%s", WebViewHandler.Language.ENGLISH));

    }
}