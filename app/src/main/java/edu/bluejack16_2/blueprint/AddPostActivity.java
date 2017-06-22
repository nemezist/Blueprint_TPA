package edu.bluejack16_2.blueprint;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddPostActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        tabLayout = (TabLayout) findViewById(R.id.addPostTabLayout);
        viewPager = (ViewPager) findViewById(R.id.addPostViewPager);

        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());

        mainAdapter.addItem(new AddPostTextFragment(), "Text", R.drawable.location_icon);
        mainAdapter.addItem(new AddPostImageFragment(),"Image",R.drawable.location_icon);
        mainAdapter.addItem(new AddPostGameFragment(), "Game", R.drawable.location_icon);
        mainAdapter.addItem(new AddPostMovieFragment(), "Movie", R.drawable.location_icon);
        mainAdapter.addItem(new AddPostMusicFragment(), "Music", R.drawable.location_icon);
        mainAdapter.addItem(new AddPostLocationFragment(), "Location", R.drawable.location_icon);

        viewPager.setAdapter(mainAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //set title & icon
        for (int i=0;i<mainAdapter.getCount();i++){
            tabLayout.getTabAt(i).setIcon(mainAdapter.getIcon(i));
        }
    }
}
