package edu.bluejack16_2.blueprint;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by BAGAS on 21-Jun-17.
 */

public class SpotifyKey extends AsyncTask<String, Integer, String> {
    private Exception exception;

    private Context context;
    private ProgressDialog prog;

    public SpotifyKey() {
        context = null;
    }

    public SpotifyKey(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        if(context != null){
            prog = new ProgressDialog(context);
            prog.setMessage("Getting Music Data");
            prog.setIndeterminate(true);
            prog.show();
        }
    }

    @Override
    protected String doInBackground(String... params) {
//        try {
//
//            URL url = new URL("https://accounts.spotify.com/api/token");
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("POST");
//            urlConnection.setRequestProperty("Authorization","Basic " + Base64.decode("308fcfefc1584319a428eecc03a31103:1778702977204410809e76d55dd948b9",Base64.DEFAULT));
//
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            URL url = new URL("http://himmatbinus.or.id/API");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close();
                String ret = stringBuilder.toString();
                return  ret.substring(new String("<!DOCTYPE html> <html>\n <head>\n" +
                        "\t<title>Asd</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "\t").length()).trim().substring(0,86);
            }
//
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }


    }

    @Override
    protected void onPostExecute(String s) {
        if (prog != null) {
            prog.hide();
        }
    }

}
