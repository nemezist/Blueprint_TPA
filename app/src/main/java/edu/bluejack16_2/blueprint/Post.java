package edu.bluejack16_2.blueprint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

/**
 * Created by BAGAS on 21-Jun-17.
 */

public class Post {

    String postId, userId, postContent, username;
    int postType;
    long postTime;

    public static final int POST_TEXT = 0;
    public static final int POST_IMAGE = 1;
    public static final int POST_MUSIC = 2;
    public static final int POST_GAME = 3;
    public static final int POST_MOVIE = 4;
    public static final int POST_LOCATION = 5;

    public Post(){

    }

    public Post(String postId, String userId, String postContent, int postType, long postTime){
        this.postId = postId;
        this.userId = userId;
        this.postContent = postContent;
        this.postType = postType;
        this.postTime = postTime;
    }

    public Post(String postId, String userId, String username, String postContent, int postType, long postTime){
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.postContent = postContent;
        this.postType = postType;
        this.postTime = postTime;
    }

    public Post(String postId, String userId, String postContent, int postType){
        this.postId = postId;
        this.userId = userId;
        this.postContent = postContent;
        this.postType = postType;
        this.postTime = new Date().getTime();
    }

    public Post(String userId, String postContent, int postType){
        this.userId = userId;
        this.postContent = postContent;
        this.postType = postType;
        this.postTime = new Date().getTime();
    }

    public Post(Post newPost, String key){
        this.postId = key;
        this.userId = newPost.getUserId();
        this.postContent = newPost.getPostContent();
        this.postTime = newPost.getPostTime();
        this.postType = newPost.getPostType();
    }

    public static void addPost(Post newPost, final Context ctx){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        final ProgressDialog mProgressDialog = new ProgressDialog(ctx);
        mProgressDialog.setMessage("Publishing Post");
        mProgressDialog.setIndeterminate(true);

        mProgressDialog.show();

        databaseReference.child("posts").push().setValue(newPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mProgressDialog.dismiss();
                Toast.makeText(ctx, "Success publish post!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ctx,MainActivity.class);
                ctx.startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressDialog.dismiss();
                Toast.makeText(ctx, "Failed to publish post!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ctx,MainActivity.class);
                ctx.startActivity(intent);
            }
        });

        return;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
