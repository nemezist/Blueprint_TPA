package edu.bluejack16_2.blueprint;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewFollowingActivity extends AppCompatActivity {

    Context context;
    String userId;
    ListView listView;
    ExploreListViewAdapter exploreListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_following);
        userId = getIntent().getStringExtra("userId");
        listView = (ListView) findViewById(R.id.followingUserLv);
        context = this;

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userReference = firebaseDatabase.getReference("users/" + userId + "/follow");
        Query userQuery = userReference;
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                exploreListViewAdapter = new ExploreListViewAdapter(context);
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String followUserId = ds.getKey();

                    DatabaseReference listUserReference = firebaseDatabase.getReference("users/" + followUserId);
                    Query listUserQuery = listUserReference;
                    listUserQuery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String userId = dataSnapshot.child("id").getValue().toString();
                            String email = dataSnapshot.child("email").getValue().toString();
                            String username = dataSnapshot.child("username").getValue().toString();
                            String photoUrl = dataSnapshot.child("photoUrl").getValue().toString();

                            UserExploreModel currUser = new UserExploreModel(userId, email, username, photoUrl);
                            exploreListViewAdapter.addItem(currUser);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                listView.setAdapter(exploreListViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
