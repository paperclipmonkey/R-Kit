package com.example.michaelwaterworth.r_kit;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * Created by michaelwaterworth on 15/08/15. Copyright Michael Waterworth
 */
public class DiaryTask extends FlipperActivityTask {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        flipper = (ViewFlipper) findViewById(R.id.switcher);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Called when the user touches the button
     */
    public void save(View view) {
        EditText editText = (EditText) findViewById(R.id.diary_text);
        Data data = new Data();//Create new Data record
        data.setData(editText.getText().toString()); //Add text
        data.setTaskId(getTask().getId());
        data.save();//Save
        Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
        //this.finish();//Close activity
        hideKeyboard();
        pageNext();
    }
}