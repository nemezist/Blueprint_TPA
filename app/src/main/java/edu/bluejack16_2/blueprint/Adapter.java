package edu.bluejack16_2.blueprint;


import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by BAGAS on 19-Jun-17.
 */

public class Adapter extends FragmentPagerAdapter {

    ArrayList <Fragment> fragments;
    ArrayList <String> strings;
    ArrayList <Integer> drawables;

    public Adapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
        this.strings = new ArrayList<>();
        this.drawables = new ArrayList<>();
    }

    public void addItem(Fragment fragment, String string,  int icons){
        this.fragments.add(fragment);
        this.strings.add(string);
        this.drawables.add(icons);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return strings.get(position);
    }

    public int getIconsId(int position){
        return drawables.get(position);
    }
}
