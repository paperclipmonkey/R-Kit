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
 * Schedules starting Tasks at the right time
 * Runs as a persistent background service
 * Created by michaelwaterworth on 30/07/15. Copyright Michael Waterworth
 */
public class SchedulerService extends BroadcastReceiver {
    String TAG = "Scheduler";

    //Only allow uploading to start once
    private Boolean hasStartedUpload = false;

    public SchedulerService() {
    }

    /**
     * Start the scheduler
     * Set repeating alarm manager to check for new tasks every n seconds
     * @param context
     */
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

    /**
     * Stop the scheduler by stopping and also stopping from starting again on boot
     * @param context
     */
    public static void stopScheduler(Context context) {
        killOnBoot(context);
    }

    private static void startOnBoot(Context context) {
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * Stop the start on boot broadcast receiver
     * @param context
     */
    private static void killOnBoot(Context context) {
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * Called by the system when the SchedulerService is started up.
     * @param context
     * @param intent
     */
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

    /**
     * Called when the user finishes the intro activity. This should be overridden to provide your own schedule.
     */
    public static void generateSchedule(Context context){
        //Add Biovici tasks
        int i = 1;//Days from now
        int x = 9;//Time
        while(i <= 14){//30 days
            while(x <= 21){//9PM
                //Create a new task record.
                Calendar cal = Calendar.getInstance();
                //Initialise Calendar to on the hour
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.MILLISECOND, 0);

                //Set Hour
                cal.set(Calendar.HOUR_OF_DAY, x);
                //Set Day
                cal.add(Calendar.DAY_OF_YEAR, i);

                //Create new task
                Task task = new Task();
                task.setDate(cal);
                task.setNotificationTitle(context.getString(R.string.notification_cortisol_title));
                task.setNotifDesc(context.getString(R.string.notification_cortisol_description));
                task.setClassName("BioviciReaderTask");
                task.save();

                x = x + 3;
            }
            x = 9;
            i++;
        }

        i = 1;//Days from now
        while(i <= 14){//30 days
                //Create a new task record.
                Calendar cal = Calendar.getInstance();
                //Initialise Calendar to midnight
                cal.set(Calendar.HOUR, 21);
                cal.set(Calendar.MINUTE, 30);
                cal.set(Calendar.MILLISECOND, 0);

                //Set Day
                cal.add(Calendar.DAY_OF_YEAR, i);

                //Create new task
                Task task = new Task();
                task.setDate(cal);
                task.setNotificationTitle(context.getString(R.string.notification_diary_title));
                task.setNotifDesc(context.getString(R.string.notification_diary_description));
                task.setClassName("DiaryTask");
                task.save();
            i++;
        }
    }
}
