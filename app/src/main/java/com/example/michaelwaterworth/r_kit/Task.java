package com.example.michaelwaterworth.r_kit;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.util.Calendar;

/**
 * Created by michaelwaterworth on 30/07/15.
 */
public class Task extends SugarRecord implements Parcelable {
    Long taskid;
    Calendar date;
    String className;
    String extras;
    Boolean isService;

    public Boolean getIsService() {
        return isService;
    }

    public void setIsService(Boolean isService) {
        this.isService = isService;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public long getTaskid() {
        return taskid;
    }

    public void setTaskid(long taskid) {
        this.taskid = taskid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeLong(taskid);
        out.writeLong(date.getTimeInMillis());
        out.writeString(className);
        out.writeString(extras);
       //TODO out.writeByte(isService);
    }

    private Task(Parcel in) {
        taskid = in.readLong();

        date = Calendar.getInstance();
        date.setTimeInMillis(in.readLong());

        className = in.readString();
        extras = in.readString();
        //TODO isService = in.readValue();
    }
}
