package com.example.michaelwaterworth.r_kit;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;

/**
 * Created by michaelwaterworth on 03/09/15. Copyright Michael Waterworth
 */
public abstract class FlipperActivityTask extends ActivityTask {
    protected ViewFlipper flipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flipper = (ViewFlipper) findViewById(R.id.switcher);
    }

    protected void setTaskProgress(int percentage){
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.task_progressbar);
        progressBar.setProgress(percentage);
    }

    public void buttonNext(View view) {
        pageNext();
    }

    public void pageNext(){
        flipper.showNext();  // Switches to the next view
    }
}