package edu.bluejack16_2.blueprint;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostGameFragment extends Fragment implements DataResponse{

    EditText searchText;
    ProgressDialog mProgressDialog ;
    ListView listView;
    PostGameListViewAdapter postGameListViewAdapter;
    public AddPostGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_post_game, container, false);
        listView = (ListView) v.findViewById(R.id.listViewGame);

        Button buttonSearch = (Button) v.findViewById(R.id.buttonSearchGame);
        searchText = (EditText) v.findViewById(R.id.searchEt);
        final DataResponse dataResponse = this;

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Kepanggil", Toast.LENGTH_SHORT).show();
                postGameListViewAdapter = new PostGameListViewAdapter(getContext());
                try {
                    String query = URLEncoder.encode(searchText.getText().toString(),"UTF-8");
                    String requestUrl = "https://www.giantbomb.com/api/search/?api_key=66b227b052a6060c25085ca5dc9b9d53de6895db&format=json&query=" + query + "&resources=game&field_list=name,id,image,expected_release_year,original_release_date";
                    Log.v("URL",requestUrl);
                    RequetsData req = new RequetsData();
                    req.resp = dataResponse;
                    req.execute(requestUrl);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        mProgressDialog= new ProgressDialog(getContext());
        mProgressDialog.setMessage("Getting Game Data");
        mProgressDialog.setIndeterminate(true);

        return v;
    }

    @Override
    public void processFinish(JSONObject obj) {
        mProgressDialog.dismiss();
        try {
            JSONArray arr = obj.getJSONArray("results");
            for(int i = 0; i < arr.length(); i++){
                JSONObject temp = arr.getJSONObject(i);
                Log.d(("expected_release_year"), temp.getString("expected_release_year"));

                if(temp.getString("expected_release_year") != "null") {
                    postGameListViewAdapter.addItem(temp.getString("id"), temp.getString("name"), temp.getString("expected_release_year"));
                }
                else if(temp.getString("expected_release_year") == "null"){
                    postGameListViewAdapter.addItem(temp.getString("id"), temp.getString("name"), temp.getString("original_release_date").substring(0,4));
                }
            }


            listView.setAdapter(postGameListViewAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getContext(), ((Game) postGameListViewAdapter.getItem(position)).gameTitle, Toast.LENGTH_SHORT).show();
                    Post newPost = new Post(FirebaseAuth.getInstance().getCurrentUser().getUid(), ((Game)postGameListViewAdapter.getItem(position)).gameID, Post.POST_GAME);
                    Post.addPost(newPost, getContext());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processRunning() {
        mProgressDialog.show();
        Toast.makeText(getContext(), "Keluar", Toast.LENGTH_SHORT).show();
    }
}
