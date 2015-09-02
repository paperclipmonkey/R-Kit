package com.example.michaelwaterworth.r_kit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by michaelwaterworth on 16/08/15. Copyright Michael Waterworth

 */
class TaskAdapter extends BaseAdapter {
    private final List<Task> mTasks;

    public TaskAdapter(List<Task> tasks) {
        mTasks = tasks;
    }

    @Override
    public int getCount() {
        return mTasks.size();
    }

    @Override
    public Task getItem(int i) {
        return mTasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
        //return mTasks.get(i).getId();
    }

    public View getView(int position, View view, ViewGroup parent) {

        if(view == null) {
            LayoutInflater vi = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.task_row, null);
        }

        Task ti = mTasks.get(position);
        ImageView icon = (ImageView)view.findViewById(R.id.task_image);
        TextView title = (TextView)view.findViewById(R.id.task_title);
        TextView description = (TextView)view.findViewById(R.id.task_description);
        TextView dateView = (TextView)view.findViewById(R.id.task_date);

//            if(ti.isSelected()) {
//                icon.setImageResource(R.drawable.item_selected);
//            }
//            else {
//                icon.setImageResource(R.drawable.item_unselected);
//            }

        //icon.setImageResource(R.drawable.rkit_launcher);
        title.setText(ti.getNotificationTitle());
        description.setText(ti.getNotifDesc());

        //SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy hh:mm a", Locale.UK);
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.UK);
        String date = format.format(ti.getDate().getTime());
        dateView.setText(date);
        return view;
    }
}