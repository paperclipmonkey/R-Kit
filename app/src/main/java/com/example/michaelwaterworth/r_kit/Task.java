package com.example.michaelwaterworth.r_kit;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.sql.Date;

/**
 * Created by michaelwaterworth on 30/07/15.
 */
public class Task extends SugarRecord implements Parcelable {
    int taskid;
    Date date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //TODO
    }
}
