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

public class PostMovieListViewAdapter extends BaseAdapter{

    ArrayList<Movie> movieList;
    Context context;

    public PostMovieListViewAdapter(Context context){
        this.context = context;
        movieList = new ArrayList<Movie>();
    }

    public void addItem(String movieId, String movieTitle, String movieReleaseYear){
        Movie newMovie = new Movie(movieId, movieTitle, movieReleaseYear);
        movieList.add(newMovie);
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.movie_row, parent, false);
        }

        TextView movieTitleTv = (TextView) convertView.findViewById(R.id.movieTitleTv);
        TextView movieRelaseYearTv = (TextView) convertView.findViewById(R.id.movieRelaseYearTv);

        movieTitleTv.setText(movieList.get(position).movieTitle);
        movieRelaseYearTv.setText(movieList.get(position).movieRelaseYear);

        return convertView;
    }
}
