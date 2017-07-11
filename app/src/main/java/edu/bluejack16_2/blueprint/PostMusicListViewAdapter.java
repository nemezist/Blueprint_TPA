package edu.bluejack16_2.blueprint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by BAGAS on 23-Jun-17.
 */

public class PostMusicListViewAdapter extends BaseAdapter {

    ArrayList<Music> musics;
    Context context;

    public void addItem(String musicId, String musicTitle, String musicArtist, String musicURL){
        Music music = new Music(musicId,musicTitle,musicArtist,musicURL);
        musics.add(music);
    }

    PostMusicListViewAdapter(Context context){
        musics = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getCount() {
        return musics.size();
    }

    @Override
    public Object getItem(int position) {
        return musics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.music_row, parent, false);
        }

        TextView musicTitle = (TextView) convertView.findViewById(R.id.tvMusicTitle);
        TextView musicArtist = (TextView) convertView.findViewById(R.id.tvMusicArtist);
        Button buttonPlay = (Button) convertView.findViewById(R.id.buttonPreviewMusic);

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMedia.getInstance().playFromUrl(musics.get(position).musicURL);
            }
        });

        musicTitle.setText(musics.get(position).musicTitle);
        musicArtist.setText(musics.get(position).musicArtist);

        return convertView;
    }
}
