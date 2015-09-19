package com.example.michaelwaterworth.r_kit;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;

/**
 * Abstract class that Tasks that use multiple pages can inherit from
 * Uses a View Flipper to switch between views inside the Activity.
 * Additionally a progress bar is used to show progress through the task.
 * Created by michaelwaterworth on 03/09/15. Copyright Michael Waterworth
 */
public abstract class FlipperActivityTask extends ActivityTask {
    protected ViewFlipper flipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setTaskProgress(int percentage){
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.task_progressbar);
        if(progressBar != null) {
            progressBar.setProgress(percentage);
        }
    }

    public void buttonNext(View view) {
        pageNext();
    }

    public void pageNext(){
        if(flipper != null) {
            flipper.showNext();  // Switches to the next view
        }
    }
}