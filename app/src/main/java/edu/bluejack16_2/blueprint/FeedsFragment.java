package edu.bluejack16_2.blueprint;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedsFragment extends Fragment {

    private final String TAG_SHOW = "ShowPost";
    ListView listView;
    PostListViewAdapter postListViewAdapter;

    public FeedsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_feeds, container, false);

        Button postBtn = (Button) v.findViewById(R.id.buttonPost);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AddPostActivity.class);
                startActivity(i);
            }
        });

        listView = (ListView) v.findViewById(R.id.postListView);

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("posts");
        Query postQuery = databaseReference;
        postQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postListViewAdapter = new PostListViewAdapter(getContext());

                for(DataSnapshot ds: dataSnapshot.getChildren()) {
//                    Post currPost = new Post();
//                    currPost.setPostId(ds.getKey());
//                    currPost.setPostType(Integer.parseInt(ds.child("postType").getValue().toString()));
//                    currPost.setPostContent(ds.child("postContent").getValue().toString());
//                    currPost.setPostTime(Long.parseLong(ds.child("postTime").getValue().toString()));
//                    currPost.setUserId(ds.child("userId").getValue().toString());

                    String postId = ds.getKey();
                    String postContent = ds.child("postContent").getValue().toString();
                    String userId = ds.child("userId").getValue().toString();
                    int postType = Integer.parseInt(ds.child("postType").getValue().toString());
                    long postTime = Long.parseLong(ds.child("postTime").getValue().toString());

//                    Log.v(TAG_SHOW, currPost.getPostId());
//                    Log.v(TAG_SHOW, "" + currPost.getPostType());
//                    Log.v(TAG_SHOW, currPost.getPostContent());
//                    Log.v(TAG_SHOW, "" + currPost.getPostTime());
//                    Log.v(TAG_SHOW, currPost.getUserId());

                    postListViewAdapter.addItem(postId, postContent, postType, postTime, userId);
                }

                listView.setAdapter(postListViewAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getContext(), ((Post)postListViewAdapter.getItem(position)).getPostContent(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG_SHOW, "Read failed: " + databaseError.getCode());
            }
        });

        return v;
    }

}
