package edu.bluejack16_2.blueprint;

import android.content.Context;

import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by BAGAS on 23-Jun-17.
 */

public class KeyManager {
    private static KeyManager ourInstance;

    private static String key;
    private static long lastGenerated;

    public static KeyManager getInstance() {

        if(ourInstance == null){
            ourInstance = new KeyManager();
        }

        return ourInstance;
    }

    public long getLastGenerated(){
        return new Date().getTime() - lastGenerated;
    }


    public String getKey() {

        if (key == null || new Date().getTime() - lastGenerated >= 350*60*1000) {
            try {
                key = new SpotifyKey().execute().get();
                lastGenerated = new Date().getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return key;
    }


    private KeyManager() {
        key = null;
        lastGenerated = -1;
    }
}
