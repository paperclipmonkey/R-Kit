package com.example.michaelwaterworth.r_kit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by michaelwaterworth on 18/08/15. Copyright Michael Waterworth

 */
public class TappingTask extends Activity{
    private Task task;
    private int lastTapTarget;
    private int counter;
    private boolean isRunning = false;
    private boolean isDone = false;
    private final List<TappingTaskTap> taps = new ArrayList<>();
    private ViewFlipper flipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapping);

        Intent intent = getIntent();
        task = intent.getParcelableExtra("task");
        counter = 0;

        flipper = (ViewFlipper) findViewById(R.id.tapping_switcher);
    }

    public void buttonNext(View view) {
        flipper.showNext();  // Switches to the next view
    }

        @Override
    protected void onPause() {
        super.onPause();
    }

    public void tapped(View view){
        if(isDone) return;

        if(!isRunning){
            startTapping();
        }
        TappingTaskTap tappingTaskTap = new TappingTaskTap(new Date().getTime(), view.getId());
        taps.add(tappingTaskTap);
        if(view.getId() != lastTapTarget){
            counter++;
            updateCounter(counter);
        }
        lastTapTarget = view.getId();
    }

    private void startTapping(){
        isRunning = true;
        //Set up countdown timer.
        //final TextView mTextView = (TextView) findViewById(R.id.tapping_countdown);
        CountDownTimer countDownTimer = new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                //mTextView.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                isRunning = false;
                isDone = true;
                //mTextView.setText("Finished!");
                Log.d("Tapping,", taps.toString());
                save();
                flipper.showNext();
                //    Button button = (Button) findViewById(R.id.tapping_start_stop);
            //    button.setVisibility(View.VISIBLE);
            //    button.setText("Save");
            }
        }.start();
    }

    public void buttonDone(View v){
        this.finish();
    }

    private void updateCounter(int iCount){
        TextView textView = (TextView) findViewById(R.id.tapping_counter);
        textView.setText("" + iCount);
    }

    /** Called when the user touches the button */
    private void save() {
        Data data = new Data();//Create new Data record
        data.setData(taps.toString()); //Save tap list
        data.setTaskId(task.getId());
        data.save();//Save
        Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
    }


    // Tapping task tap - Used to store taps
    public class TappingTaskTap {
        final Long timeStamp; //Millis
        final int buttonNo; //ID of button - unique to the two buttons
        public TappingTaskTap(Long pTimeStamp, int pButtonNo){
            timeStamp = pTimeStamp;
            buttonNo = pButtonNo;
        }

        @Override
        public String toString() {
            return "timeStamp:" + timeStamp + "\n" +
                    "buttonNo:" + buttonNo;
        }
    }
}
