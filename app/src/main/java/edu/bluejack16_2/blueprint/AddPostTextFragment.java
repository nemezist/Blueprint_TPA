package edu.bluejack16_2.blueprint;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostTextFragment extends Fragment {

    private Button addPostBtn;
    private EditText addPostEt;
    private DatabaseReference databaseReference;

    public AddPostTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_post_text, container, false);
        addPostEt = (EditText) v.findViewById(R.id.addPostTextEt);

        addPostBtn = (Button) v.findViewById(R.id.addPostTextBtn);
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference();

                String postText = addPostEt.getText().toString();
                Post newPost = new Post(FirebaseAuth.getInstance().getCurrentUser().getUid(), postText, Post.POST_TEXT);
                Post.addPost(newPost,getContext());

            }
        });

        return v;
    }

}
