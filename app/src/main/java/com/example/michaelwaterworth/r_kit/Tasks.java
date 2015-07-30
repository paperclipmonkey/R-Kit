package com.example.michaelwaterworth.r_kit;

import com.orm.SugarRecord;

import java.sql.Date;

/**
 * Created by michaelwaterworth on 30/07/15.
 */
public class Tasks extends SugarRecord{
    int taskid;
    Date date;
    String className;
    String extras;

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



}
