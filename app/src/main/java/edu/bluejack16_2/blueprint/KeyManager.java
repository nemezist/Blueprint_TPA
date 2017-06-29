package edu.bluejack16_2.blueprint;

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

    public String getKey() {

        if (key == null || new Date().getTime() - lastGenerated <= 60) {
            try {
                key = new SpotifyKey().execute("http://himmatbinus.or.id/API").get();
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
