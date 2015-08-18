package com.example.michaelwaterworth.r_kit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by michaelwaterworth on 18/08/15.
 */
public class Tapping extends Activity{
    Task task;
    int lastTapTarget;
    int counter;
    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapping);

        Intent intent = getIntent();
        task = intent.getParcelableExtra("task");
        counter = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /** Called when the user touches the button */
    public void save(View view) {
        Data data = new Data();//Create new Data record
        data.setData("" + counter); //Add text
        data.setTaskId(task.getId());
        data.save();//Save
        Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
        this.finish();//Close activity
    }

    public void tapped(View view){
        if(!isRunning) return;
        if(view.getId() != lastTapTarget){
            counter++;
            updateCounter(counter);
        }
        lastTapTarget = view.getId();
    }

    public void buttonStartStop(View v){
        if(counter > 0){
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
