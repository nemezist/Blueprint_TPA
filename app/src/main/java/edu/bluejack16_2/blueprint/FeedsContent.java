package edu.bluejack16_2.blueprint;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by BAGAS on 11-Jul-17.
 */

public class FeedsContent {

    private static ConcurrentHashMap<String,String> gameMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String,String> movieMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String,String> placesMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String,String> musicMap = new ConcurrentHashMap<>();



    public static String getGameById(Context ctx, String gameId){

        String ret = " ";
        if(gameMap.get(gameId) == null){
            String url = "https://www.giantbomb.com/api/game/"+gameId+"/?api_key=66b227b052a6060c25085ca5dc9b9d53de6895db&format=json";
            Toast.makeText(ctx, "", Toast.LENGTH_SHORT).show();
            JSONObject obj = null;
            try {
                obj = new RequetsData().execute(url).get().getJSONObject("results");
                ret = obj.getString("name");
                gameMap.put(gameId,ret);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else{
            ret = gameMap.get(gameId);
        }


        return ret;

    }

    //public static String getMusicById(){}

    public static String getMovieById(Context ctx, String movieId){

        String ret = "";
        if(movieMap.get(movieId) == null) {
            String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=8db47054a6c0db44fa27694f3b7ebf08";
            JSONObject obj = null;
            try {
                obj = new RequetsData().execute(url).get();
                ret = obj.getString("original_title");
                movieMap.put(movieId,ret);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else{
            ret = movieMap.get(movieId);
        }

        return ret;
    }

    public static String getPlacesById(Context ctx, String placesId){
        String ret = " ";


        if(placesMap.get(placesId) == null) {
            String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placesId + "&key=AIzaSyBFC-JlSetRsng87yd4HrWfl9jo_1tlmw8";
            JSONObject obj = null;
            try {
                obj = new RequetsData().execute(url).get().getJSONObject("result");
                ret = obj.getString("name");
                placesMap.put(placesId,ret);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else{
            ret = placesMap.get(placesId);
        }
        return ret;
    }

    public static String getMusicById(Context ctx, String musicId){
        String ret = "";

        if(musicMap.get(musicId) == null) {
            String url = "https://api.spotify.com/v1/tracks/"+musicId;
            JSONObject obj = null;
            try {
                obj = new RequetsData().execute(url,KeyManager.getInstance().getKey()).get() ;
                ret = obj.getString("name") + " by " + obj.getJSONArray("artists").getJSONObject(0).getString("name");;
                musicMap.put(musicId,ret);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else{
            ret = musicMap.get(musicId);
        }

        return ret;
    }
}
