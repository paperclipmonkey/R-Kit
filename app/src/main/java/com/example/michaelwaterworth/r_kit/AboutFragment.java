package com.example.michaelwaterworth.r_kit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by michaelwaterworth on 16/08/15. Copyright Michael Waterworth
 */
public class AboutFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    public AboutFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AboutFragment newInstance(int sectionNumber) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Home", "Inflating AboutFragment");
        View base = inflater.inflate(R.layout.fragment_about, container, false);
        return base;
    }

}