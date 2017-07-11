package edu.bluejack16_2.blueprint;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment {


    public ExploreFragment() {
        // Required empty public constructor
    }

    EditText searchText;
    ProgressDialog mProgressDialog ;
    ListView listView;
    ExploreListViewAdapter exploreListViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_explore, container, false);
        listView = (ListView) v.findViewById(R.id.listViewExplore);
        Button btnSearch = (Button) v.findViewById(R.id.buttonExplore);
        searchText = (EditText) v.findViewById(R.id.etUsername);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exploreListViewAdapter = new ExploreListViewAdapter(getContext());
                //Get Data Here
//                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//                DatabaseReference userReference = firebaseDatabase.getReference("users/");
                Query query = FirebaseDatabase.getInstance().getReference("users");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()){

                           if(ds.child("email").getValue().toString().contains(searchText.getText().toString())
                                   || ds.child("username").getValue().toString().contains(searchText.getText().toString()))
                           {
                               UserExploreModel uem = new UserExploreModel(ds.child("id").getValue().toString(),ds.child("email").getValue().toString(),ds.child("username").getValue().toString(),ds.child("photoUrl").getValue().toString());
                               exploreListViewAdapter.addItem(uem);
                           }

                           listView.setAdapter(exploreListViewAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent i = new Intent(getContext(), UserProfileActivity.class);
                                    i.putExtra("userId", ((UserExploreModel)exploreListViewAdapter.getItem(position)).userID);
                                    startActivity(i);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });

        return v;
    }

}
