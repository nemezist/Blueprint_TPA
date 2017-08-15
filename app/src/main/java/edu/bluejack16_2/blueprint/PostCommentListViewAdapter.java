package edu.bluejack16_2.blueprint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by BAGAS on 16-Jul-17.
 */

public class PostCommentListViewAdapter extends BaseAdapter {

    ArrayList<PostCommentModels> commentses;

    Context context;

    public PostCommentListViewAdapter(Context ctx) {
        this.commentses = new ArrayList<>();
        context = ctx;
    }

    @Override
    public int getCount() {
        return commentses.size();
    }

    @Override
    public Object getItem(int position) {
        return commentses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.comment_row, parent, false);
        }

        TextView commentUsername = (TextView) convertView.findViewById(R.id.tvCommentUsername);
        TextView commentContent = (TextView) convertView.findViewById(R.id.tvCommentContent);

        commentUsername.setText(commentses.get(position).getUserName());
        commentContent.setText(commentses.get(position).getPostContent());

        return  convertView;
    }

    public void addItem(String userId, final String postContent){

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/users/"+userId);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PostCommentModels model = new PostCommentModels();
                model.setPhotoUrl(dataSnapshot.child("photoUrl").getValue().toString());
                model.setPostContent(postContent);
                model.setUserName(dataSnapshot.child("username").getValue().toString());
                commentses.add(model);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private class PostCommentModels{
        String userName,photoUrl, postContent;

        public PostCommentModels() {
        }


        public String getPostContent() {
            return postContent;
        }

        public void setPostContent(String postContent) {
            this.postContent = postContent;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }
    }

}
