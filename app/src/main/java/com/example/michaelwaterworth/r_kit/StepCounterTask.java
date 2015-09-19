package com.example.michaelwaterworth.r_kit;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;

/**
 * A background service that logs the number of steps taken on the device.
 * The API only provides the number of steps since started.
 * By checking periodically it's possible to have more and more fine-grained detail on no. of steps
 */
public class StepCounterTask extends IntentService {
    private final String TAG = "StepCounterTask";
    private SensorManager mSensorManager;
    private Sensor mStepSensor;

    public StepCounterTask() {
        super("");
        initialise(getApplicationContext());
    }

    public StepCounterTask(Context context) {
        super("");
        initialise(context);
    }

    /**
     * Initialise the Task and register an event listener
     * This Task only works on Android KitKat or higher
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initialise(Context context) {
        //hasSystemFeature()
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        SensorEventListener mSensorEventListener = new SensorEventListener() {
            private float mStepOffset;

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                if (mStepOffset == 0) {
                    mStepOffset = event.values[0];
                }
                Log.d(TAG, "" + event.values[0]);
                //mTextView.setText(Float.toString(event.values[0] - mStepOffset));
            }
        };
        mSensorManager.registerListener(mSensorEventListener, mStepSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
