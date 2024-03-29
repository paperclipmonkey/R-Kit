package com.example.michaelwaterworth.r_kit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Attaches Data objects to Lists.
 * Adapter supplies stylised Data
 */
class DataAdapter extends BaseAdapter {
    private final List<Data> mData;

    public DataAdapter(List<Data> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Data getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
        //return mData.get(i).getId();
    }

    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.task_row, null);
        }

        Data ti = mData.get(position);
        TextView title = (TextView) view.findViewById(R.id.task_title);
        TextView description = (TextView) view.findViewById(R.id.task_description);
        TextView dateView = (TextView) view.findViewById(R.id.task_date);

        title.setText("" + ti.getDate());
        description.setText(ti.getData());

        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.UK);
        String date = format.format(ti.getDate().getTime());
        dateView.setText(date);
        return view;
    }
}