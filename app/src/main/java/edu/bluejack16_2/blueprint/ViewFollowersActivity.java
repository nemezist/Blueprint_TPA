package edu.bluejack16_2.blueprint;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewFollowersActivity extends AppCompatActivity {

    Context context;
    String userId;
    ListView listView;
    ExploreListViewAdapter exploreListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_followers);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        listView = (ListView) findViewById(R.id.lvViewFollowers);
        context = this;
        exploreListViewAdapter = new ExploreListViewAdapter(context);

        Query ref = FirebaseDatabase.getInstance().getReference("/users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot){
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Query query2 = FirebaseDatabase.getInstance().getReference("/users/"+ds.child("id").getValue()+"/follow");
                    final DataSnapshot snap = ds;

//                    Toast.makeText(context,"/users/"+ds.child("id").getValue()+"/follow/
                    query2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshots){
                                //Toast.makeText(context, ds2.getKey(), Toast.LENGTH_SHORT).show();
                                if(dataSnapshots.child(userId).getValue() != null){
                                    String userId = snap.child("id").getValue().toString();
                                    String email = snap.child("email").getValue().toString();
                                    String username = snap.child("username").getValue().toString();
                                    String photoUrl = snap.child("photoUrl").getValue().toString();
                                    UserExploreModel currUser = new UserExploreModel(userId, email, username, photoUrl);
                                    exploreListViewAdapter.addItem(currUser);
                                }

                        }
//
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
//
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setAdapter(exploreListViewAdapter);

    }
}
