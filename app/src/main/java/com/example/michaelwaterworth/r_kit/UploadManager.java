package com.example.michaelwaterworth.r_kit;

/**
 * Created by michaelwaterworth on 02/09/15. Copyright Michael Waterworth
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.alexbbb.uploadservice.AllCertificatesAndHostsTruster;
import com.alexbbb.uploadservice.UploadRequest;
import com.alexbbb.uploadservice.UploadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;

/**
 * Manage uploads from the device
 * When uploading add all images and data in
 * then purge the device
 * Created by michaelwaterworth on 21/07/15. Copyright Michael Waterworth
 */
public class UploadManager extends BroadcastReceiver {
    private final static String TAG = "Broadcast Receiver";

    static public void purgeData() {
        //Purge the database
        Data.deleteAll(Data.class);
        //TODO - Remove all saved files
    }

    static public void buildSuccessNotification(Context context, JSONObject jsonObject) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.rkit_launcher)
                        .setContentTitle("Upload Successful")
                        .setContentText("Click to see uploaded view");

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

        mBuilder.setAutoCancel(true);//Automatically dismiss on click

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
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

    /**
     * Add all of the files in the private folder to the upload
     * @param dir
     * @param request
     */
    public static void addFiles(File dir, UploadRequest request) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; ++i) {
                File file = files[i];
                if (file.isDirectory()) {
                    addFiles(file, request);
                } else {
                    // do something here with the file
                    request.addFileToUpload(file.getPath(),"file" + i, file.getName(), "image/jpeg");
                }
            }
        }
    }

    /**
     * Add Data from the SQLite db to the upload
     * @param request
     */
    public static void addData(UploadRequest request){
        JSONArray jsonArray = new JSONArray();
        Iterator dataList = Data.findAll(Data.class);

        while(dataList.hasNext()){
            Data data = (Data) dataList.next();
            jsonArray.put(data.toJsonObject());
        }
        String jsonString = jsonArray.toString();
        request.addParameter("data", jsonString);
    }

    /**
     * Start a new upload
     * @param context
     */
    static public void upload(Context context) {
        Log.d(TAG, "Uploading");
        AllCertificatesAndHostsTruster.apply();
        final UploadRequest request = new UploadRequest(context,
                "upload",
                //rmvOverlayItem.getId() + "",//Long used to keep track of db
                context.getString(R.string.base_url) + context.getString(R.string.upload_path));

    /*
     * parameter-name: is the name of the parameter that will contain file's data.
     * Pass "uploaded_file" if you're using the test PHP script
     *
     * custom-file-name.extension: is the file name seen by the server.
     * E.g. value of $_FILES["uploaded_file"]["name"] of the test PHP script
     */

        //Add all files for upload
        File filesDir = context.getFilesDir();
        addFiles(filesDir, request);

        //Add all data for upload
        addData(request);


//        //configure the notification
//        request.setNotificationConfig(R.drawable.app_icon_silhouette,
//                context.getString(R.string.app_name),
//                context.getString(R.string.uploading_toast),
//                context.getString(R.string.uploading_success),
//                context.getString(R.string.upload_failed),
//                true);//Clear on success

        // set the intent to perform when the user taps on the upload notification.
        // currently tested only with intents that launches an activity
        // if you comment this line, no action will be performed when the user taps on the notification
        request.setNotificationClickIntent(new Intent(context.getApplicationContext(), BaseApplication.class).putExtra("upload", "intent"));

        try {
            //Start upload service and display the notification
            UploadService.startUpload(request);

        } catch (Exception exc) {
            //You will end up here only if you pass an incomplete UploadRequest
            Log.e("AndroidUploadService", exc.getLocalizedMessage(), exc);
        }
    }

    /**
     * Receiving status notifcations from the system.
     * If complete call onCompleted
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            if (UploadService.getActionBroadcast().equals(intent.getAction())) {
                final int status = intent.getIntExtra(UploadService.STATUS, 0);
                final String uploadId = intent.getStringExtra(UploadService.UPLOAD_ID);

                switch (status) {
                    case UploadService.STATUS_COMPLETED:
                        final int responseCode = intent.getIntExtra(UploadService.SERVER_RESPONSE_CODE, 0);
                        final String responseMsg = intent.getStringExtra(UploadService.SERVER_RESPONSE_MESSAGE);
                        onCompleted(context, uploadId, responseCode, responseMsg);
                        break;

                    default:
                        break;
                }
            }
        }
    }

    /**
     * Once the upload has completed check the response
     * @param context
     * @param uploadId
     * @param serverResponseCode
     * @param serverResponseMessage
     */
    public void onCompleted(Context context,
                            String uploadId,
                            int serverResponseCode,
                            String serverResponseMessage) {
        Log.i(TAG, "Upload with ID " + uploadId
                + " has been completed with HTTP " + serverResponseCode
                + ". Response from server: " + serverResponseMessage);

        if (serverResponseCode == 200) {
            try {
                JSONObject jsonObject = new JSONObject(serverResponseMessage);

                buildSuccessNotification(context, jsonObject);

                purgeData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

