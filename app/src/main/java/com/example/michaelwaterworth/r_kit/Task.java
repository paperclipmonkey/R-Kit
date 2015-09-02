package com.example.michaelwaterworth.r_kit;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.util.Calendar;

/**
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
    private Long date;
    private String className;
    private String notificationTitle;
    private String notifDesc;
    private String extras;
    private Boolean isService;

    public Task() {

    }

    public Task(Parcel in) {
        date = in.readLong();
        className = in.readString();
        extras = in.readString();
        isService = in.readInt() == 1;
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
    }
}
