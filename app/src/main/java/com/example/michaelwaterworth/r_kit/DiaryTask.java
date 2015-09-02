package com.example.michaelwaterworth.r_kit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by michaelwaterworth on 15/08/15. Copyright Michael Waterworth
 */
public class DiaryTask extends Activity {
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        Intent intent = getIntent();
        task = intent.getParcelableExtra("task");
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
        data.setTaskId(task.getId());
        data.save();//Save
        Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
        this.finish();//Close activity
    }
}