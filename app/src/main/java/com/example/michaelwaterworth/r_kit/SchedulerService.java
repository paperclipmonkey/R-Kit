package com.example.michaelwaterworth.r_kit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by michaelwaterworth on 30/07/15.
 */
public class SchedulerService extends BroadcastReceiver {
    public final String TAG = "Scheduler";

    public SchedulerService() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Service");
    }

    public static void startScheduler(Context context){
        startOnBoot(context);
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SchedulerService.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar cal = Calendar.getInstance();

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                1000 * 10, alarmIntent);
    }

    public static void stopScheduler(Context context){
        //TODO Fill in the rest - Stop pending schedules?
        killOnBoot(context);
    }

    public static void startOnBoot(Context context){
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void killOnBoot(Context context){
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
