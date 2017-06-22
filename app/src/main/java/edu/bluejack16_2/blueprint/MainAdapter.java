package edu.bluejack16_2.blueprint;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by BAGAS on 21-Jun-17.
 */

public class MainAdapter extends FragmentStatePagerAdapter {

    private ArrayList <Fragment> fragments;
    private ArrayList <String> strings;
    private ArrayList <Integer> drawables;

    public MainAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
        this.strings = new ArrayList<>();
        this.drawables = new ArrayList<>();
    }

    public void addItem(Fragment fragment, String string){
        this.fragments.add(fragment);
        this.strings.add(string);
    }

    public void addItem(Fragment fragment, String string, int icons){
        this.fragments.add(fragment);
        this.strings.add(string);
        this.drawables.add(icons);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public String getTitle(int position){
        return strings.get(position);
    }

    public int getIcon(int position){
        return drawables.get(position);
    }

}
