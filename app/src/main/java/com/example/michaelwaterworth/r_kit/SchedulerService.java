package com.example.michaelwaterworth.r_kit;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

/**
 * Created by michaelwaterworth on 30/07/15.
 */
public class SchedulerService extends BroadcastReceiver {
    private final String TAG = "Scheduler";

    public SchedulerService() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Service");
        //Check in Db - see if there are any upcoming Task
        Calendar cal = Calendar.getInstance();

        long sTime = cal.getTimeInMillis() / 1000;

        cal.add(Calendar.MINUTE, 5);
        long eTime = cal.getTimeInMillis() / 1000;
        List<Task> tasks = Task.find(Task.class, "datetime > ? and datetime < ?", "" + sTime, "" + eTime);
        for(Task task: tasks){
            if(task.getIsService()){
                createService(context, task);
            } else {
                createNotification(context, task);
            }
        }
    }

    private void createService(Context context, Task task) {
        //TODO Start service
        Intent resultIntent = new Intent();
        resultIntent.setClassName(context, task.getClassName());
        resultIntent.putExtra("task", task);
        context.startService(resultIntent);
    }

    private void createNotification(Context context, Task task){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_drawer)
                        .setContentTitle("Upload Successful")
                        .setContentText("Click to see uploaded view");

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent();

        resultIntent.setClassName(context, task.getClassName());

        resultIntent.putExtra("task", task);

        mBuilder.setAutoCancel(true);//Automatically dismiss on click

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)

//        stackBuilder.addParentStack(TheirViewActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //TODO - Fix issue of replacing previous notification

        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
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

    private static void startOnBoot(Context context){
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private static void killOnBoot(Context context){
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
