package com.example.michaelwaterworth.r_kit;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.util.Calendar;

/**
 * Base class for a task object in the database
 * Relies on SugarRecord from SugarORM
 * Created by michaelwaterworth on 30/07/15. Copyright Michael Waterworth
 */
public class Task extends SugarRecord implements Parcelable {
    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
    private Long date;//Date & Time to be completed
    private String className;//Name of class to be run
    private String notificationTitle;//Title to be used in notification
    private String notifDesc;//Description to be used in notification
    private String extras;//Any extras to be sent to the Task class
    private Boolean isService;//Bool is this a serivce. Services started directly without notification
    private Boolean hasnotified;//Bool has this Task already been used as a notification

    public Task() {
        hasnotified = false;
    }

    public Task(Parcel in) {
        date = in.readLong();
        className = in.readString();
        extras = in.readString();
        isService = in.readInt() == 1;
        hasnotified = in.readInt() == 1;
    }

    public Boolean getIsService() {
        return isService;
    }

    public void setIsService() {
        this.isService = false;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getNotifDesc() {
        return notifDesc;
    }

    public void setNotifDesc(String notifDesc) {
        this.notifDesc = notifDesc;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public Boolean getHasnotified() {
        return hasnotified;
    }

    public void setHasnotified(Boolean hasnotified) {
        this.hasnotified = hasnotified;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public Calendar getDate() {
        Calendar rDate = Calendar.getInstance();
        rDate.setTimeInMillis(date * 1000);

        return rDate;
    }

    public void setDate(Calendar rDate) {
        this.date = rDate.getTimeInMillis() / 1000;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeLong(date);
        out.writeString(className);
        out.writeString(extras);
        out.writeInt(isService ? 0 : 1);
        out.writeInt(hasnotified ? 0 : 1);
    }
}
