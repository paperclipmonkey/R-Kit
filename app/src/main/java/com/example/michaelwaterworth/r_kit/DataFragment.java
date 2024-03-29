package com.example.michaelwaterworth.r_kit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.Calendar;

/**
 * Created by michaelwaterworth on 16/08/15. Copyright Michael Waterworth
 */
public class DataFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView listView;

    public DataFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DataFragment newInstance(int sectionNumber) {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Home", "Inflating DataFragment");
        View base = inflater.inflate(R.layout.fragment_data, container, false);


        // Create a progress bar to display while the list loads
        ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);

        listView = (ListView) base.findViewById(R.id.datalist);
        listView.setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        //ViewGroup root = (ViewGroup) getActivity().findViewById(android.R.id.content);
        //root.addView(progressBar);


        //Check in Db - see if there are any upcoming Task
        Calendar cal = Calendar.getInstance();

        long sTime = cal.getTimeInMillis() / 1000;
        DataAdapter adapter = new DataAdapter(Data.find(Data.class, ""));
        Log.d("Home", "data: " + adapter.getCount());
        // Assign adapter to ListView
        listView.setAdapter(adapter);

        return base;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

}