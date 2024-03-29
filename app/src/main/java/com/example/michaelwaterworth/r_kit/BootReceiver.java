package com.example.michaelwaterworth.r_kit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by michaelwaterworth on 30/07/15. Copyright Michael Waterworth
 */
public class BootReceiver extends BroadcastReceiver {
    /**
     * Called whenever the device reboots. Ensures the service is running.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            SchedulerService.startScheduler(context);
        }
    }
}