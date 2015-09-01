package com.example.michaelwaterworth.r_kit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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

    public View getView(int position, View convertView, ViewGroup parent) {
        View V = convertView;

        if(V == null) {
            LayoutInflater vi = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            V = vi.inflate(R.layout.task_row, null);
        }

        Task ti = mTasks.get(position);
        ImageView icon = (ImageView)V.findViewById(R.id.task_image);
        TextView title = (TextView)V.findViewById(R.id.task_title);
        TextView artist = (TextView)V.findViewById(R.id.task_description);

//            if(ti.isSelected()) {
//                icon.setImageResource(R.drawable.item_selected);
//            }
//            else {
//                icon.setImageResource(R.drawable.item_unselected);
//            }

        title.setText(ti.getNotificationTitle());
        artist.setText("by " + ti.getNotifDesc());

        return V;
    }
}