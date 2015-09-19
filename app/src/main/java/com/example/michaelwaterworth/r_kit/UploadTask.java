package com.example.michaelwaterworth.r_kit;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Called from the Scheduler Service when there are no more scheduled tasks left
 * and data to upload. Kicks off the static upload task
 */
public class UploadTask extends IntentService {
    String TAG = "UploadService";


    public UploadTask() {
        super("UploadTask");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Upload service bound");
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Upload Task");

        UploadManager.upload(getApplicationContext());
    }

}
