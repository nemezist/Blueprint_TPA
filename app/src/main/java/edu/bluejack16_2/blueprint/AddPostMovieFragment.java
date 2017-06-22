package edu.bluejack16_2.blueprint;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostMovieFragment extends Fragment implements DataResponse{

    ListView listView;
    PostMovieListViewAdapter postMovieListViewAdapter;
    ProgressDialog mProgressDialog ;

    EditText etSearch;
    public AddPostMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_post_movie, container, false);

        listView = (ListView) v.findViewById(R.id.movieListView);


        mProgressDialog= new ProgressDialog(getContext());
        mProgressDialog.setMessage("Getting Movie Data");
        mProgressDialog.setIndeterminate(true);

        Button buttonSearch = (Button) v.findViewById(R.id.buttonSearchGame);
        etSearch = (EditText) v.findViewById(R.id.etSearch);

        final DataResponse dataResponse = this;

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMovieListViewAdapter = new PostMovieListViewAdapter(getContext());
                String requestUrl = null;
                try {
                    String query = URLEncoder.encode(etSearch.getText().toString(),"UTF-8");
                    requestUrl = "https://api.themoviedb.org/3/search/movie?api_key=8db47054a6c0db44fa27694f3b7ebf08&query=" + query + "&page=1&include_adult=false";
                    RequetsData req = new RequetsData();
                    req.resp = dataResponse;
                    req.execute(requestUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

    @Override
    public void processFinish(JSONObject obj) {
        mProgressDialog.dismiss();

        try {
            JSONArray arr = obj.getJSONArray("results");
            Toast.makeText(getContext(),arr.length() + "", Toast.LENGTH_SHORT).show();

            StringBuilder build = new StringBuilder();

            for(int i = 0; i < arr.length(); i++){
                JSONObject temp = arr.getJSONObject(i);
                String str = temp.getString("id") + " : " + temp.getString("title") + "\n";
                build.append(str);
                postMovieListViewAdapter.addItem(temp.getString("id"), temp.getString("title"), temp.getString("release_date"));
            }

            listView.setAdapter(postMovieListViewAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getContext(), ((Movie) postMovieListViewAdapter.getItem(position)).movieTitle, Toast.LENGTH_SHORT).show();
                    Post newPost = new Post(FirebaseAuth.getInstance().getCurrentUser().getUid(), ((Movie) postMovieListViewAdapter.getItem(position)).movieId, Post.POST_MOVIE);
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
    }
}
