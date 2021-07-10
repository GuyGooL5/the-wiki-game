package com.oog.thewikigame.utilities;

import android.util.Log;


public class Logger {

    public static void log(LogTag tag, String ...messages){
        StringBuilder sb=new StringBuilder();
        for(String i: messages){
            sb.append(i).append(" ");
        }
        Log.d(tag.toString(), sb.toString());
    }

}
