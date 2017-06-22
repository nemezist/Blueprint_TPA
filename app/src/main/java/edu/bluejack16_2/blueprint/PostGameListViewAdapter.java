package edu.bluejack16_2.blueprint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by BAGAS on 22-Jun-17.
 */

public class PostGameListViewAdapter extends BaseAdapter {

    ArrayList<Game>games;
    Context context;

    PostGameListViewAdapter(Context ctx){
        this.context = ctx;
        games = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int position) {
        return games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.game_row, parent, false);
        }

        TextView gameTitle = (TextView) convertView.findViewById(R.id.tvGameTitle);
        TextView gameReleaseYear = (TextView) convertView.findViewById(R.id.tvReleaseYear);

        gameTitle.setText(games.get(position).gameTitle);
        gameReleaseYear.setText(games.get(position).gameReleaseYear);

        return convertView;
    }

    public void addItem(String gameId, String gameTitle, String gameReleaseYear){
        Game game = new Game(gameId,gameTitle,gameReleaseYear);
        this.games.add(game);
    }
}
