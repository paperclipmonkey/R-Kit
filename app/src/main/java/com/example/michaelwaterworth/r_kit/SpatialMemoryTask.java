package com.example.michaelwaterworth.r_kit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;

/**
 * Created by michaelwaterworth on 18/08/15.
 */
public class SpatialMemoryTask extends Activity{
    ViewFlipper flipper;
    Task task;
    ArrayList<Button> targetList = new ArrayList<Button>();
    int currentTarget = 0;
    int levelLength = 3;

    int score = 0;

    boolean isRunning = true;
    boolean isPlayable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spatial_memory);

        Intent intent = getIntent();
        task = intent.getParcelableExtra("task");

        flipper = (ViewFlipper) findViewById(R.id.intro_switcher);
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
        try {
            if (view.equals(targetList.get(currentTarget))) {//User tapped next item in list
                score++;
                Log.d("score", "" + score);
                updateCounter(score);
                final Button b = (Button) view;
                b.setBackgroundResource(R.drawable.memory_flower_blue);
                new CountDownTimer(2000, 1000) {

                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        b.setBackgroundResource(R.drawable.memory_flower_default);
                    }
                }.start();

                if (currentTarget == targetList.size() - 1) {
                    Log.d("game", "New sequence");
                    //Finished
                    levelLength++;//Increase the level
                    newSequence();
                } else {
                    currentTarget++;
                }
            } else {
                wrongSequence(view);
            }
        } catch(Exception e){
            wrongSequence(view);
        }
    }

    public void wrongSequence(View view) {
        Log.d("game", "Wrong sequence");
        //Set background red for 1 second.
        final Button b = (Button) view;
        b.setBackgroundResource(R.drawable.memory_flower_red);
        new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                b.setBackgroundResource(R.drawable.memory_flower_default);
            }
        }.start();
    }

    public void buttonNext(View view) {

        flipper.showNext();  // Switches to the next view
        if(flipper.getCurrentView().getId() == R.id.activity_memory){
            //Start game
            startPlaying();
        }
    }


    public void newSequence(){
        //Come up with a new sequence based on the length var
        //Play the current sequence to the user
        //Enable play again
        currentTarget = 0;

        targetList = new ArrayList<Button>();//Clear old list

            int i = 1;
        while(i < 4){
            int x = 1;
            while(x < 4){
                int id  = getResources().getIdentifier("button_memory_" + i + "_" + x ,"id", getApplicationContext().getPackageName());
                targetList.add((Button) findViewById(id));
                x++;
            }
            i++;
        }
//        int i = 0;
//        while(i < levelLength){
//            int x = (int) Math.ceil(Math.random()*3);
//            int y = (int) Math.ceil(Math.random()*3);
//            int id  = getResources().getIdentifier("button_memory_" + x + "_" + y ,"id", getApplicationContext().getPackageName());
//
//            Log.d("MemSeq", "Tile no." + x + " " + y);
//
//            targetList.add((Button) findViewById(id));
//            i++;
//        }

        Log.d("MemSeq", "Level no." + levelLength);

        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                //Set previous item as default
                if(currentTarget < targetList.size()) {
                    Button b;
                    if(currentTarget != 0){
                        b = targetList.get(currentTarget - 1);
                        b.setBackgroundResource(R.drawable.memory_flower_default);
                        //Change currently selected item
                    }

                    b = targetList.get(currentTarget);
                    b.setBackgroundResource(R.drawable.memory_flower_lit);

                    currentTarget++;
                } else {
                    this.onFinish();
                    this.cancel();
                }
            }

            public void onFinish() {
                Button b = targetList.get(currentTarget - 1);
                b.setBackgroundResource(R.drawable.memory_flower_default);
                isPlayable = true;
                currentTarget = 0;
                Log.d("MemSeq", "Play on" + levelLength);
            }
        }.start();
    }

    public void startPlaying() {
        newSequence();
        startTapping();
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
