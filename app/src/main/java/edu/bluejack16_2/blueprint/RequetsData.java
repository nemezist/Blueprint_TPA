package edu.bluejack16_2.blueprint;


import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class RequetsData extends AsyncTask<String, String, JSONObject> {

    public DataResponse resp = null;

    @Override
    protected void onPreExecute() {
        resp.processRunning();
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        JSONObject ret = null;
        HttpURLConnection connect = null;
        StringBuilder builder = new StringBuilder();

        try {
            connect = (HttpURLConnection) new URL(params[0]).openConnection();
            connect.setRequestMethod("GET");

            String res = "";

            if(params.length == 2){
                connect.setRequestProperty("Authorization","Bearer " + params[1]);
            }

            BufferedReader buff = new BufferedReader(new InputStreamReader(connect.getInputStream()));

            while((res = buff.readLine()) != null){
                builder.append(res);
            }

            buff.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connect.disconnect();
        }

        try {
            ret = new JSONObject(builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    @Override
    protected void onPostExecute(JSONObject jsonArray) {
        resp.processFinish(jsonArray);
    }

}
