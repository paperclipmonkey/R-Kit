package com.example.michaelwaterworth.r_kit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by michaelwaterworth on 18/08/15.
 */
public class SpatialMemory extends Activity{
    Task task;
    ArrayList<Button> targetList = new ArrayList<Button>();
    int currentTarget = 0;
    int levelLength = 2;

    int score = 0;

    boolean isRunning = false;
    boolean isPlayable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spatial_memory);

        Intent intent = getIntent();
        task = intent.getParcelableExtra("task");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /** Called when the user touches the button */
    public void save(View view) {
        Data data = new Data();//Create new Data record
        data.setData("" + score); //Add text
        data.setTaskId(task.getId());
        data.save();//Save
        Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
        this.finish();//Close activity
    }

    public void tapped(View view){
        if(!isRunning) return;
        if(view == targetList.get(currentTarget)){//User tapped next item in list
            if(currentTarget == targetList.size()){
                //Finished
            } else {
                currentTarget++;
            }
        }
    }

    public void buttonStartStop(View v){
        if(!isRunning){
            save(v);
        } else {
            v.setVisibility(View.INVISIBLE);
            startTapping();
        }
    }

    public void startTapping(){
        isRunning = true;
        //Set up countdown timer.
        final TextView mTextView = (TextView) findViewById(R.id.tapping_countdown);
        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                mTextView.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                isRunning = false;
                mTextView.setText("Finished!");
                Button button = (Button) findViewById(R.id.tapping_start_stop);
                button.setVisibility(View.VISIBLE);
                button.setText("Save");
            }
        }.start();
    }

    public void updateCounter(int iCount){
        TextView textView = (TextView) findViewById(R.id.tapping_counter);
        textView.setText("" + iCount);
    }
}
