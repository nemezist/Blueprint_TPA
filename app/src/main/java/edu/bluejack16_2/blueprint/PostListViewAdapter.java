package edu.bluejack16_2.blueprint;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

/**
 * Created by Philip on 6/23/2017.
 */

public class PostListViewAdapter extends BaseAdapter {

    ArrayList<Post> postList;
    ArrayList<String> postedProfilePicUrl;
    Context context;
    FirebaseUser currUser;
    static int status;

    public PostListViewAdapter(Context context){
        this.context = context;
        postList = new ArrayList<Post>();
        postedProfilePicUrl = new ArrayList<String>();
    }

    //userId dimasukin sama username ya
    public void addItem(String postId, String postContent, int postType, long postTime, String userId, String username, String profileUrl){
        Post currPost = new Post(postId, userId, username, postContent, postType, postTime);
        postList.add(currPost);
        postedProfilePicUrl.add(profileUrl);
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
        final Post currPost = postList.get(position);
        final String currProfPic = postedProfilePicUrl.get(position);

        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.post_row, parent, false);
        }

        TextView usernameTv = (TextView) convertView.findViewById(R.id.postUsernameTv);
        TextView contentTv = (TextView) convertView.findViewById(R.id.postContentTv);
        TextView timeTv = (TextView) convertView.findViewById(R.id.postTimeTv);

        ImageView profileIv = (ImageView) convertView.findViewById(R.id.profileIv);
        ImageView contentIv = (ImageView) convertView.findViewById(R.id.postContentImageIv);

        currUser = FirebaseAuth.getInstance().getCurrentUser();

        Glide.with(context).load(currProfPic).into(profileIv);

        SimpleDateFormat sdf = new SimpleDateFormat("dd, MMM yyyy");

        usernameTv.setText(currPost.getUsername());
        timeTv.setText(sdf.format(new Date(currPost.getPostTime())));
        if(currPost.getPostType() == Post.POST_TEXT){
            contentIv.setVisibility(View.GONE);
            contentTv.setText(currPost.getPostContent());
        }
        else if(currPost.getPostType() == Post.POST_IMAGE){
            contentTv.setVisibility(View.GONE);
            //contentTv.setText("");
            contentIv.setVisibility(View.VISIBLE);
            Glide.with(context).load(currPost.getPostContent()).into(contentIv);
        }
        else if(currPost.getPostType() == Post.POST_GAME){
            contentIv.setVisibility(View.GONE);
            contentTv.setVisibility(View.VISIBLE);
            contentTv.setText("Now playing " + FeedsContent.getGameById(convertView.getContext(),currPost.getPostContent()));
        }
        else if(currPost.getPostType() == Post.POST_MOVIE){
            contentIv.setVisibility(View.GONE);
            contentTv.setVisibility(View.VISIBLE);
            contentTv.setText("Now watching " + FeedsContent.getMovieById(convertView.getContext(),currPost.getPostContent()));
        }

        else if(currPost.getPostType() == Post.POST_MUSIC){
            contentIv.setVisibility(View.GONE);
            contentTv.setVisibility(View.VISIBLE);
            contentTv.setText("Now listening " + FeedsContent.getMusicById(convertView.getContext(),currPost.getPostContent()));
        }
        else if(currPost.getPostType() == Post.POST_LOCATION){
            contentIv.setVisibility(View.GONE);
            contentTv.setVisibility(View.VISIBLE);
            contentTv.setText("Now at " +  FeedsContent.getPlacesById(convertView.getContext(),currPost.getPostContent()));
        }

        return convertView;
    }
}
