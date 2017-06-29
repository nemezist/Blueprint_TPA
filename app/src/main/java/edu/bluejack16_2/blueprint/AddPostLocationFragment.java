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

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostLocationFragment extends Fragment implements DataResponse {

    double longitude = -1;
    double latitude = -1;
    Location location;
    ProgressDialog progressDialog;
    LocationManager lm;

    DataResponse dataResponse = this;
    PostLocationListViewAdapter locationAdapter;

    ListView listView;

    public AddPostLocationFragment() {
        // Required empty public constructor
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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationAdapter = new PostLocationListViewAdapter(getContext());
                RequetsData rd = new RequetsData();
                rd.resp = dataResponse;
                rd.execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&key=AIzaSyBFC-JlSetRsng87yd4HrWfl9jo_1tlmw8");
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



