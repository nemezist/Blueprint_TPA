package edu.bluejack16_2.blueprint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

/**
 * Created by Philip on 6/23/2017.
 */

public class PostListViewAdapter extends BaseAdapter {

    ArrayList<Post> postList;
    Context context;

    public PostListViewAdapter(Context context){
        this.context = context;
        postList = new ArrayList<Post>();
    }

    public void addItem(String postId, String postContent, int postType, long postTime, String userId){
        Post currPost = new Post(postId, userId, postContent, postType, postTime);
        postList.add(currPost);
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int position) {
        return postList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.post_row, parent, false);
        }

        TextView usernameTv = (TextView) convertView.findViewById(R.id.postUsernameTv);
        TextView contentTv = (TextView) convertView.findViewById(R.id.postContentTv);
        TextView timeTv = (TextView) convertView.findViewById(R.id.postTimeTv);

        SimpleDateFormat sdf = new SimpleDateFormat("dd, mm yyyy");

        usernameTv.setText(postList.get(position).getUserId());
        contentTv.setText(postList.get(position).getPostContent());
        timeTv.setText(sdf.format(new Date(postList.get(position).getPostTime())));

        return convertView;
    }
}
