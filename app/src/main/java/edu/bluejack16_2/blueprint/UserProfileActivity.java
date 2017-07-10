package edu.bluejack16_2.blueprint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class UserProfileActivity extends AppCompatActivity {

    TextView usernameTv, emailTv;
    ImageView profilePicIv;
    Button followBtn;
    ListView postLv;
    String currUserId;
    PostListViewAdapter postListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        currUserId = getIntent().getStringExtra("userId");

        usernameTv = (TextView) findViewById(R.id.userProfileUsernameTv);
        emailTv = (TextView) findViewById(R.id.userProfileEmailTv);
        profilePicIv = (ImageView) findViewById(R.id.userProfilePictureIv);
        followBtn = (Button) findViewById(R.id.userProfileEditBtn);
        postLv = (ListView) findViewById(R.id.userProfilePostLv);

        final FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currUser.getUid().equals(currUserId))
            followBtn.setVisibility(View.GONE);
        else{
            final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference userReference = firebaseDatabase.getReference("users/" + currUser.getUid() + "/follow");
            Query userQuery = userReference;
            userQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        Log.v("FollowKey", ds.getKey());
                        if(currUserId.equals(ds.getKey()))
                            followBtn.setText("Unfollow User");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            followBtn.setVisibility(View.VISIBLE);
            followBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser currLoginUser = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    if(followBtn.getText().equals("Follow User")){
                        databaseReference.child("users/" + currLoginUser.getUid() + "/follow/" + currUserId).setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                followBtn.setText("Unfollow User");
                                //Toast.makeText(UserProfileActivity.this, "Success...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        databaseReference.child("users/" + currLoginUser.getUid() + "/follow/" + currUserId).removeValue();
                        followBtn.setText("Follow User");
                    }

                    //Toast.makeText(UserProfileActivity.this, "Success...", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //Log.v("UserId", userId);
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userReference = firebaseDatabase.getReference("users/" + currUserId);
        Query userQuery = userReference;

        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue().toString();
                final String photoUrl = dataSnapshot.child("photoUrl").getValue().toString();
                final String username = dataSnapshot.child("username").getValue().toString();

                usernameTv.setText(username);
                emailTv.setText(email);
                Glide.with(getApplicationContext()).load(photoUrl).into(profilePicIv);

                DatabaseReference postReference = firebaseDatabase.getReference("posts");
                Query postQuery = postReference;
                postQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        postListViewAdapter = new PostListViewAdapter(getApplicationContext());
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            String userId = ds.child("userId").getValue().toString();

                            if(userId.equals(currUserId)){
                                String postId = ds.getKey();
                                String postContent = ds.child("postContent").getValue().toString();
                                int postType = Integer.parseInt(ds.child("postType").getValue().toString());
                                Long postTime = Long.parseLong(ds.child("postTime").getValue().toString());

                                postListViewAdapter.addItem(postId, postContent, postType, postTime, userId, username, photoUrl);
                            }
                        }

                        postLv.setAdapter(postListViewAdapter);
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
    }
}
