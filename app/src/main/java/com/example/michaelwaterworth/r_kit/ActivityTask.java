package com.example.michaelwaterworth.r_kit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by michaelwaterworth on 03/09/15. Copyright Michael Waterworth
 */
abstract class ActivityTask extends Activity{
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        task = intent.getParcelableExtra("task");
    }

    /**
     * Hide the keyboard from the user
     * Useful when transitioning between UI states.
     */
    protected void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Get the task associated with this activity
     * @return The task
     */
    public Task getTask() {
        return task;
    }

    /**
     * Callable from UI XML. Finishes the Activity
     * @param view Calling UI view object
     */
    public void buttonDone(View view) {
        this.finish();
    }
}
