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

import java.util.ArrayList;

/**
 * Created by michaelwaterworth on 18/08/15.
 */
public class SpatialMemory extends Activity{
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
        if(view.equals(targetList.get(currentTarget))){//User tapped next item in list
            score++;
            Log.d("game", ""+score);
            if(currentTarget == targetList.size() - 1){
                //Finished
                levelLength++;//Increase the level
                newSequence();
            } else {
                currentTarget++;
            }
        } else {
            //Set background red for 1 second.
            final Button b = (Button) view;
            b.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
            new CountDownTimer(2000, 1000) {

                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    b.setBackgroundColor(getResources().getColor(R.color.accent_material_light));
                }
            }.start();
        }
    }

    public void newSequence(){
        //Come up with a new sequence based on the length var
        //Play the current sequence to the user
        //Enable play again

        targetList = new ArrayList<Button>();//Clear old list

        int i = 0;
        while(i < levelLength){
            int x = (int) Math.ceil(Math.random()*3);
            int y = (int) Math.ceil(Math.random()*3);
            int id  = getResources().getIdentifier("button_memory_" + x + "_" + y ,"id", getApplicationContext().getPackageName());

            Log.d("MemSeq", "Tile no." + x + " " + y);

            targetList.add((Button) findViewById(id));
            i++;
        }

        Log.d("MemSeq", "Leve no." + levelLength);

        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                //Set previous item as default
                if(currentTarget < targetList.size() - 1) {
                    Button b;
                    b = targetList.get(currentTarget);
                    b.setBackgroundColor(getResources().getColor(R.color.accent_material_dark));
                    //Change currently selected item
                    currentTarget++;

                    //Change currently selected item to item in list.

                    b = targetList.get(currentTarget);
                    b.setBackgroundColor(getResources().getColor(R.color.accent_material_light));
                } else {
                    this.onFinish();
                    this.cancel();
                }
            }

            public void onFinish() {

                Button b = targetList.get(currentTarget);
                b.setBackgroundColor(getResources().getColor(R.color.accent_material_light));
                isPlayable = true;
                Log.d("MemSeq", "Play on" + levelLength);

            }
        }.start();
    }

    public void buttonStartStop(View v){
        if(!isRunning){
            save(v);
        } else {
            v.setVisibility(View.INVISIBLE);
            newSequence();
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
