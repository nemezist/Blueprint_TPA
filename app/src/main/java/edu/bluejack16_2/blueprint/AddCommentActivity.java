package edu.bluejack16_2.blueprint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCommentActivity extends AppCompatActivity {

    Button btnPost;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        btnPost = (Button) findViewById(R.id.addCommentButton);
        et = (EditText) findViewById(R.id.etAddComment);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentString = et.getText().toString();
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/posts/"+getIntent().getStringExtra("postId")+"/comments");
                String key = dbRef.push().getKey();
                PostComments comments = new PostComments(FirebaseAuth.getInstance().getCurrentUser().getUid(),commentString);
                dbRef.child(key).setValue(comments).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent in = new Intent(getApplicationContext(),DetailPostActivity.class);
                        in.putExtra("postId",getIntent().getStringExtra("postId"));
                        startActivity(in);
                    }
                });
            }
        });
    }

}
