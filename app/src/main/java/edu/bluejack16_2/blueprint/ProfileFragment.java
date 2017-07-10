package edu.bluejack16_2.blueprint;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {

    ImageView profilePicIv;
    TextView profileUsernameTv, profileEmailTv;
    Button profileEditBtn, followingBtn, followerBtn;
    ListView profilePostLv;
    PostListViewAdapter postListViewAdapter;
    Context context;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        //Toast.makeText(getContext(), "Masuk Profile", Toast.LENGTH_SHORT).show();
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        profilePicIv = (ImageView) v.findViewById(R.id.profileFragPictureIv);
        profileUsernameTv = (TextView) v.findViewById(R.id.profileFragUsernameTv);
        profileEmailTv = (TextView) v.findViewById(R.id.profileFragEmailTv);
        profileEditBtn = (Button) v.findViewById(R.id.profileFragEditBtn);
        followingBtn = (Button) v.findViewById(R.id.profileFollowingBtn);
        followerBtn = (Button) v.findViewById(R.id.profileFollowerBtn);
        profilePostLv = (ListView) v.findViewById(R.id.profilePostLv);
        context = getContext();

        final FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userReference = firebaseDatabase.getReference("users/" + currUser.getUid());
        Query userQuery = userReference;

        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue().toString();
                final String photoUrl = dataSnapshot.child("photoUrl").getValue().toString();
                final String username = dataSnapshot.child("username").getValue().toString();

                profileUsernameTv.setText(username);
                profileEmailTv.setText(email);
                Glide.with(context).load(photoUrl).into(profilePicIv);

                DatabaseReference postReference = firebaseDatabase.getReference("posts");
                Query postQuery = postReference;
                postQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        postListViewAdapter = new PostListViewAdapter(getContext());
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            String userId = ds.child("userId").getValue().toString();

                            if(userId.equals(currUser.getUid())){
                                String postId = ds.getKey();
                                String postContent = ds.child("postContent").getValue().toString();
                                int postType = Integer.parseInt(ds.child("postType").getValue().toString());
                                Long postTime = Long.parseLong(ds.child("postTime").getValue().toString());

                                postListViewAdapter.addItem(postId, postContent, postType, postTime, userId, username, photoUrl);
                            }
                        }

                        profilePostLv.setAdapter(postListViewAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        profileEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EditProfileActivity.class);
                startActivity(i);
            }
        });

        followingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ViewFollowingActivity.class);
                i.putExtra("userId", currUser.getUid());
                startActivity(i);
            }
        });

        followerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }

}
