package edu.bluejack16_2.blueprint;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailPostActivity extends AppCompatActivity {

    String postId, userId;
    Post currPost;
    User currUser;
    TextView usernameTv, postContentTv, postTimeTv;
    Button likeBtn, dislikeBtn, addCommentButton;
    ImageView profilePicIv, postContentIv;
    FirebaseUser currLoginUser;
    int likeStatus;
    ListView commentLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        currLoginUser = FirebaseAuth.getInstance().getCurrentUser();
        usernameTv = (TextView) findViewById(R.id.detailPostUsernameTv);
        postContentTv = (TextView) findViewById(R.id.detailPostContentTv);
        postTimeTv = (TextView) findViewById(R.id.detailPostTimeTv);
        likeBtn = (Button) findViewById(R.id.detailPostLikeBtn);
        dislikeBtn = (Button) findViewById(R.id.detailPostDislikeBtn);
        addCommentButton = (Button) findViewById(R.id.btAddComment);
        profilePicIv = (ImageView) findViewById(R.id.detailPostprofileIv);
        postContentIv = (ImageView) findViewById(R.id.detailPostContentImageIv);
        commentLv = (ListView) findViewById(R.id.lvCommnetList);

        postId = getIntent().getStringExtra("postId");

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),AddCommentActivity.class);
                in.putExtra("postId",postId);
                startActivity(in);
            }
        });

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference postReference = firebaseDatabase.getReference("posts/" + postId);
        Query postQuery = postReference;
        postQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userId = dataSnapshot.child("userId").getValue().toString();

                String postContent = dataSnapshot.child("postContent").getValue().toString();

                final long postTime = Long.parseLong(dataSnapshot.child("postTime").getValue().toString());
                int postType = Integer.parseInt(dataSnapshot.child("postType").getValue().toString());
                currPost = new Post(postId, userId, postContent, postType, postTime);

                if(currPost.getPostType() == Post.POST_GAME){
                    String postSet = FeedsContent.getGameById(getApplicationContext(),postContent);
                    currPost.setPostContent(postSet);
                }

                if(currPost.getPostType() == Post.POST_MUSIC){
                    String postSet = FeedsContent.getMusicById(getApplicationContext(),postContent);
                    currPost.setPostContent(postSet);
                }

                if(currPost.getPostType() == Post.POST_LOCATION){
                    String postSet = FeedsContent.getPlacesById(getApplicationContext(),postContent);
                    currPost.setPostContent(postSet);
                }

                if(currPost.getPostType() == Post.POST_MOVIE){
                    String postSet = FeedsContent.getMovieById(getApplicationContext(),postContent);
                    currPost.setPostContent(postSet);
                }

                DatabaseReference userReference = firebaseDatabase.getReference("users/" + userId);
                Query postQuery = userReference;
                postQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String email = dataSnapshot.child("email").getValue().toString();
                        String username = dataSnapshot.child("username").getValue().toString();
                        String photoUrl = dataSnapshot.child("photoUrl").getValue().toString();
                        currUser = new User(userId, email, username, photoUrl);

                        if(currPost.getPostType() == Post.POST_IMAGE){
                            postContentIv.setVisibility(View.VISIBLE);
                            postContentTv.setVisibility(View.GONE);
                            Glide.with(getApplicationContext()).load(currPost.getPostContent()).into(postContentIv);
                        }
                        else{
                            postContentIv.setVisibility(View.GONE);
                            postContentTv.setVisibility(View.VISIBLE);
                            postContentTv.setText(currPost.getPostContent());
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("dd, MMM yyyy");
                        usernameTv.setText(currUser.getUsername());
                        postTimeTv.setText(sdf.format(new Date(currPost.getPostTime())));

                        Glide.with(getApplicationContext()).load(currUser.getPhotoUrl()).into(profilePicIv);
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

        postReference = firebaseDatabase.getReference("posts/" + postId + "/like/" + currLoginUser.getUid());
        postQuery = postReference;
        postQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                likeStatus = 0;
                if(dataSnapshot.getValue() != null){
                    likeStatus = Integer.parseInt(dataSnapshot.getValue().toString());
                }
                if(likeStatus == 1)
                    likeBtn.setBackgroundResource(R.color.white);
                if(likeStatus == -1)
                    dislikeBtn.setBackgroundResource(R.color.white);

                likeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        Log.v("postLike", "" + likeStatus);
                        if(likeStatus == 1){
                            databaseReference.child("posts/" + currPost.getPostId() + "/like/" + currLoginUser.getUid()).setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    likeStatus = 0;
                                    likeBtn.setBackgroundResource(R.color.colorPrimary);
                                }
                            });
                        }
                        else if(likeStatus == 0 || likeStatus == -1){
                            databaseReference.child("posts/" + currPost.getPostId() + "/like/" + currLoginUser.getUid()).setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    likeStatus = 1;
                                    likeBtn.setBackgroundResource(R.color.white);
                                    dislikeBtn.setBackgroundResource(R.color.colorPrimary);
                                }
                            });
                        }
                    }
                });

                dislikeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        Log.v("postLike", "" + likeStatus);
                        if(likeStatus == -1){
                            databaseReference.child("posts/" + currPost.getPostId() + "/like/" + currLoginUser.getUid()).setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    likeStatus = 0;
                                    dislikeBtn.setBackgroundResource(R.color.colorPrimary);
                                }
                            });
                        }
                        else if(likeStatus == 0 || likeStatus == 1){
                            databaseReference.child("posts/" + currPost.getPostId() + "/like/" + currLoginUser.getUid()).setValue(-1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    likeStatus = -1;
                                    dislikeBtn.setBackgroundResource(R.color.white);
                                    likeBtn.setBackgroundResource(R.color.colorPrimary);
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        postReference = firebaseDatabase.getReference("posts/" + postId + "/comments/");
        postQuery = postReference;
        postQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PostCommentListViewAdapter adap = new PostCommentListViewAdapter(getApplicationContext());
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //Toast.makeText(DetailPostActivity.this, ds.child("userId").getValue().toString() + " : " +ds.child("commentContent").getValue().toString(), Toast.LENGTH_SHORT).show();
                    adap.addItem(ds.child("userId").getValue().toString(),ds.child("commentContent").getValue().toString());
                }
                //Toast.makeText(DetailPostActivity.this, adap.getCount() + "", Toast.LENGTH_SHORT).show();

                commentLv.setAdapter(adap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
