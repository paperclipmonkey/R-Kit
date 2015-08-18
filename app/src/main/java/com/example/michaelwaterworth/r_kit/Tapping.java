package com.example.michaelwaterworth.r_kit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
        EditText editText = (EditText) findViewById(R.id.diary_text);
        Data data = new Data();//Create new Data record
        data.setData(editText.getText().toString()); //Add text
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

    public void startTapping(){
        isRunning = true;
        //Set up countdown timer.
    }

    public void updateCounter(int iCount){
        TextView textView = (TextView) findViewById(R.id.tapping_counter);
        textView.setText("" + iCount);
    }

    public void finishTapping(){
        isRunning = false;
        //Clear countdown timer.
    }
}
