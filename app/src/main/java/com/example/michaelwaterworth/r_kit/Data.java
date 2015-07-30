package com.example.michaelwaterworth.r_kit;

import com.orm.SugarRecord;

import java.sql.Date;

/**
 * Created by michaelwaterworth on 30/07/15.
 */
public class Data extends SugarRecord {
    int Taskid;
    String data;
    Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public int getTaskid() {
        return Taskid;
    }

    public void setTaskid(int taskid) {
        Taskid = taskid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
