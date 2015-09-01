package com.example.michaelwaterworth.r_kit;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by michaelwaterworth on 15/08/15. Copyright Michael Waterworth

 */
public class StepCounterTask extends IntentService {
    private final String TAG = "StepCounterTask";
    private final SensorManager mSensorManager;
    private final Sensor mStepSensor;

    public StepCounterTask(Context context){
        super("");

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
