package com.example.michaelwaterworth.r_kit;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

/**
 * Created by michaelwaterworth on 30/07/15. Copyright Michael Waterworth
 */
public class SchedulerService extends BroadcastReceiver {
    String TAG = "Scheduler";

    //Only allow uploading to start once
    private Boolean hasStartedUpload = false;

    public SchedulerService() {
    }

    public static void startScheduler(Context context) {
        startOnBoot(context);
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SchedulerService.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar cal = Calendar.getInstance();

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                1000 * 10, alarmIntent);
    }

    public static void stopScheduler(Context context) {
        //TODO Fill in the rest - Stop pending schedules?
        killOnBoot(context);
    }

    private static void startOnBoot(Context context) {
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private static void killOnBoot(Context context) {
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d(TAG, "Service");
        //Check in Db - see if there are any upcoming Task
        long numTasks = Task.count(Task.class, "hasnotified = 0", new String[0]);
        long numData = Task.count(Data.class, "", new String[0]);
        Log.d(TAG, "Number of tasks: " + numTasks);
        if(numTasks == 0 && numData > 0 && !hasStartedUpload){
            hasStartedUpload = true;
            Log.d(TAG, "starting Upload Task");
            Task uploadTask = new Task();
            uploadTask.setIsService();
            uploadTask.setClassName("UploadTask");
            uploadTask.setDate(Calendar.getInstance());
            uploadTask.setNotifDesc("");
            uploadTask.setNotificationTitle("");
            createService(context, uploadTask);
            return;
        }

        Calendar cal = Calendar.getInstance();

        long sTime = cal.getTimeInMillis() / 1000;

        cal.add(Calendar.MINUTE, 5);
        long eTime = cal.getTimeInMillis() / 1000;
        List<Task> tasks = Task.find(Task.class, "date > ? and date < ? and hasnotified = 0", "" + sTime, "" + eTime);
        Log.d(TAG, tasks.toString());
        for (Task task : tasks) {
            if (task.getIsService()) {
                createService(context, task);
            } else {
                createNotification(context, task);
            }
        }
    }

    private void createService(Context context, Task task) {
        //TODO Start service
        Intent resultIntent = new Intent();
        resultIntent.setClassName(context, context.getPackageName() + "." + task.getClassName());
        resultIntent.putExtra("task", task);
        context.startService(resultIntent);
    }
//
//    public PendingIntent existAlarm(int id) {
//        Intent intent = new Intent(this, alarmreceiver.class);
//        intent.setAction(Intent.ACTION_VIEW);
//        PendingIntent test = PendingIntent.getBroadcast(this, id + selectedPosition, intent, PendingIntent.FLAG_NO_CREATE);
//        return test;
//    }

    private void createNotification(Context context, Task task) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.rkit_silhouette)
                        .setContentTitle(task.getNotificationTitle())
                        .setContentText(task.getNotifDesc());

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent();
        resultIntent.setClassName(context, context.getPackageName() + "." + task.getClassName());
        resultIntent.putExtra("task", task);
        mBuilder.setAutoCancel(true);//Automatically dismiss on click

        mBuilder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        mBuilder.setVibrate(pattern);
        mBuilder.setStyle(new NotificationCompat.InboxStyle());
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

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


        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;

        // mId allows you to update the notification later on.
        mNotificationManager.notify(Integer.parseInt(task.getId() + ""), notification);

        // Update the task making sure it isn't used a second time.
        task.setHasnotified(true);
        task.save();
    }
}
