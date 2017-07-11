package edu.bluejack16_2.blueprint;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by BAGAS on 11-Jul-17.
 */

public class MyMedia {
    private static final MyMedia ourInstance = new MyMedia();

    MediaPlayer player;

    public static MyMedia getInstance() {
        return ourInstance;
    }

    private MyMedia() {
    }

    void playFromUrl(String URL){
        if (player != null){
            player.release();
        }

        player = new MediaPlayer();
        try {
            player.setDataSource(URL);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
