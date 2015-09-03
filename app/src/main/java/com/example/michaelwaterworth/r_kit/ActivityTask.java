package com.example.michaelwaterworth.r_kit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by michaelwaterworth on 03/09/15. Copyright Michael Waterworth
 */
public class ActivityTask extends Activity{
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        task = intent.getParcelableExtra("task");
    }

    public Task getTask() {
        return task;
    }
}
