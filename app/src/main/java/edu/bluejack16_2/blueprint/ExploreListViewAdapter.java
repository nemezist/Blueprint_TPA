package edu.bluejack16_2.blueprint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by BAGAS on 05-Jul-17.
 */

public class ExploreListViewAdapter extends BaseAdapter {

    ArrayList<UserExploreModel> userList;
    Context context;

    public ExploreListViewAdapter(Context context) {
        this.userList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.user_list_row,parent,false);
        }

        TextView tvUsernameExplore = (TextView) convertView.findViewById(R.id.tvUsernameExplore);
        TextView tvEmailExplore = (TextView) convertView.findViewById(R.id.tvEmailExplore);

        tvUsernameExplore.setText(userList.get(position).userDisplayName);
        tvEmailExplore.setText(userList.get(position).userEmail);

        return  convertView;
    }

    public void addItem(UserExploreModel userExploreModel){
        userList.add(userExploreModel);
    }
}
