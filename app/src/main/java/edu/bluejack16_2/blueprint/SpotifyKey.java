package edu.bluejack16_2.blueprint;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by BAGAS on 21-Jun-17.
 */

public class SpotifyKey extends AsyncTask<String, Integer, String> {

    public SpotifyKey() {

    }


    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

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
    }

}
