package edu.bluejack16_2.blueprint;


import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostLocationFragment extends Fragment implements DataResponse {

    double longitude = -1;
    double latitude = -1;
    Location loc;
    ProgressDialog progressDialog;

    public AddPostLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_post_location, container, false);

        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        try {
            loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitude = loc.getLatitude();
            longitude = loc.getLongitude();
        } catch (SecurityException e){
            Toast.makeText(getContext(), "Security Error!", Toast.LENGTH_SHORT).show();
            return v;
        } catch (Exception err){
            Toast.makeText(getContext(), "Get Location Error!", Toast.LENGTH_SHORT).show();
            return v;
        }

        String urlString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius=500&key=AIzaSyBpTx9zuY66PRVHyrrgZnHThaz83_rsec8";

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting Your Location Data");
        progressDialog.setIndeterminate(true);

        RequetsData req = new RequetsData();
        req.resp = this;
        req.execute(urlString);
        return v;

    }

    @Override
    public void processFinish(JSONObject obj) {
        progressDialog.dismiss();

        try {
            JSONArray arr = obj.getJSONArray("results");
            Toast.makeText(getContext(), arr.length() + "", Toast.LENGTH_SHORT).show();

            StringBuilder build = new StringBuilder();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject temp = arr.getJSONObject(i);
                String str = temp.getString("id") + " : " + temp.getString("name") + "\n";
                build.append(str);
            }

        }catch (Exception e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void processRunning() {
        progressDialog.show();
    }
}
