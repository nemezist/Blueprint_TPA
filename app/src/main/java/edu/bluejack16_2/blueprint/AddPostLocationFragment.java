package edu.bluejack16_2.blueprint;


import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostLocationFragment extends Fragment implements DataResponse {


    Location location;
    ProgressDialog progressDialog;
    LocationManager lm;

    DataResponse dataResponse = this;
    PostLocationListViewAdapter locationAdapter;

    ListView listView;

    public AddPostLocationFragment() {
        // Required empty public constructor
    }

    double longitude = -9999;
    double latitude = -9999;
    boolean isGPSEnabled = false, isNetworkEnabled = false,canGetLocation = false;


    LocationManager locationManager;

    public Location getLocation(){

        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(getContext(), "Can't Get Location! \n Please turn on GPS or Network!", Toast.LENGTH_SHORT).show();
                return null;
            } else {
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 100, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
                    if(locationManager != null){
                        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                    else{
                        Toast.makeText(getContext(), "location manager null", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                } else if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
                    if(locationManager != null){
                        return  locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                    else{
                        Toast.makeText(getContext(), "location manager null", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                }
            }
        }catch (SecurityException se){
            Toast.makeText(getContext(), "Security Exception! \n\n" + se.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
        return null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_post_location, container, false);

        lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        final DataResponse dr = this;

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting Your Location Data");
        progressDialog.setIndeterminate(true);

        listView = (ListView) v.findViewById(R.id.listViewLocation);
        Button btn = (Button) v.findViewById(R.id.buttonSearchLocation);

        // TODO get current location coordinates programmatically.

        final Location currentLocation = getLocation();

        if(currentLocation != null){
            Toast.makeText(getContext(), "Lat : " + currentLocation.getLatitude() + " Lon : " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationAdapter = new PostLocationListViewAdapter(getContext());
                RequetsData rd = new RequetsData();
                rd.resp = dataResponse;
                if(currentLocation != null) rd.execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+currentLocation.getLatitude()+","+currentLocation.getLongitude()+"&radius=500&key=AIzaSyBFC-JlSetRsng87yd4HrWfl9jo_1tlmw8");
            }
        });

        return v;

    }

    @Override
    public void processFinish(JSONObject obj) {
        progressDialog.dismiss();


        try {

            if(obj.getString("status") == "OK" || obj.getString("status").equals("OK")) {
                JSONArray arr = obj.getJSONArray("results");
                // Toast.makeText(getContext(), arr.length() + "", Toast.LENGTH_SHORT).show();

                StringBuilder build = new StringBuilder();

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject temp = arr.getJSONObject(i);
                    JSONArray arrTypes = temp.getJSONArray("types");

                    if(arrTypes.length() > 0){
                        locationAdapter.addItem(temp.getString("place_id"),temp.getString("name"),arrTypes.getString(0).replace('_',' ').toUpperCase(Locale.ENGLISH));
                    }
                    else{
                        locationAdapter.addItem(temp.getString("place_id"),temp.getString("name")," - ");
                    }
                }

                listView.setAdapter(locationAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Post post = new Post(FirebaseAuth.getInstance().getCurrentUser().getUid(),((edu.bluejack16_2.blueprint.Location) locationAdapter.getItem(position)).getPlacesId(),Post.POST_LOCATION);
                        Post.addPost(post,getContext());
                    }
                });
            }

            else{
                Toast.makeText(getContext(), "Get Location Error! : " + obj.getString("status"), Toast.LENGTH_SHORT).show();
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



