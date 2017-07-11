package edu.bluejack16_2.blueprint;


import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostMusicFragment extends Fragment implements DataResponse{

    PostMusicListViewAdapter postListViewAdapter;
    ListView listView;
    EditText etSearch;
    ProgressDialog mProgressDialog ;

    MediaPlayer player;


    public AddPostMusicFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_post_music, container, false);

        Button btnSearch = (Button) v.findViewById(R.id.btnSearchMusic);

        etSearch = (EditText) v.findViewById(R.id.etSearchMusic);
        listView = (ListView) v.findViewById(R.id.listViewMusic);

        final DataResponse dataResponse = this;

        mProgressDialog= new ProgressDialog(getContext());
        mProgressDialog.setMessage("Getting Game Data");
        mProgressDialog.setIndeterminate(true);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequetsData rd = new RequetsData();
                rd.resp = dataResponse;

                postListViewAdapter = new PostMusicListViewAdapter(getContext());

                try {
                    String query = URLEncoder.encode(etSearch.getText().toString(),"UTF-8");
                    String apiKey = "";
                    rd.execute("https://api.spotify.com/v1/search?q="+query+"&type=track","asd");
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                //String key = KeyManager.getInstance().getKey(getContext());
                //Toast.makeText(getContext(), key, Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    @Override
    public void processFinish(JSONObject obj) {
        mProgressDialog.dismiss();
        try {
            JSONObject objTemp = obj.getJSONObject("tracks");
            JSONArray arr = objTemp.getJSONArray("items");
            Log.v("JSON OBJECT : ",""+arr.length());

            for (int i = 0 ; i < arr.length(); i++){
                JSONObject artist = arr.getJSONObject(i).getJSONArray("artists").getJSONObject(0);
                postListViewAdapter.addItem(arr.getJSONObject(i).getString("id"),arr.getJSONObject(i).getString("name"),artist.getString("name"),arr.getJSONObject(i).getString("preview_url"));
            }

            listView.setAdapter(postListViewAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getContext(),((Music) postListViewAdapter.getItem(position)).musicId, Toast.LENGTH_SHORT).show();
                    Post newPost = new Post(FirebaseAuth.getInstance().getCurrentUser().getUid(), ((Music) postListViewAdapter.getItem(position)).musicId,Post.POST_MUSIC);
                    Post.addPost(newPost,getContext());
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
