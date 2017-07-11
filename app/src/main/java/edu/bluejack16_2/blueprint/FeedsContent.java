package edu.bluejack16_2.blueprint;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by BAGAS on 11-Jul-17.
 */

public class FeedsContent {

    public static String getGameById(Context ctx, String gameId){
        String url = "https://www.giantbomb.com/api/game/"+gameId+"/?api_key=66b227b052a6060c25085ca5dc9b9d53de6895db&format=json";
        Toast.makeText(ctx, "", Toast.LENGTH_SHORT).show();
        JSONObject obj = null;
        String ret = " ";
        try {
            obj = new RequetsData().execute(url).get().getJSONObject("results");
            ret = obj.getString("name");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;

    }

    //public static String getMusicById(){}

    public static String getMovieById(Context ctx, String movieId){
        String url = "https://api.themoviedb.org/3/movie/"+movieId+"?api_key=8db47054a6c0db44fa27694f3b7ebf08";
        JSONObject obj = null;
        String ret = " ";
        try {
            obj = new RequetsData().execute(url).get();
            ret = obj.getString("original_title");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static String getPlacesById(Context ctx, String placesId){
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+placesId+"&key=AIzaSyBFC-JlSetRsng87yd4HrWfl9jo_1tlmw8";
        JSONObject obj = null;
        String ret = " ";

        try {
            obj = new RequetsData().execute(url).get().getJSONObject("result");
            ret = obj.getString("name");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
