package edu.bluejack16_2.blueprint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by BAGAS on 29-Jun-17.
 */

public class PostLocationListViewAdapter extends BaseAdapter {

    ArrayList<Location> locations;
    Context context;

    public PostLocationListViewAdapter() {
        this.locations = new ArrayList<>();
        this.context = null;
    }

    public PostLocationListViewAdapter(Context context) {
        this.locations = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Object getItem(int position) {
        return locations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.location_row, parent, false);
        }

        TextView tvLocationName = (TextView) convertView.findViewById(R.id.tvPlacesName);
        TextView tvLocationType = (TextView) convertView.findViewById(R.id.tvPlacesType);

        tvLocationName.setText(this.locations.get(position).getPlacesName());
        tvLocationType.setText(this.locations.get(position).getPlacesType());

        return convertView;
    }

    public void addItem(String placesId, String placesName, String placesType){
        locations.add(new Location(placesId,placesName,placesType));
    }

}
