package com.example.michaelwaterworth.r_kit;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.Calendar;

/**
 * Created by michaelwaterworth on 30/07/15. Copyright Michael Waterworth

 */
class Data extends SugarRecord {
    private Long taskId;
    private String data;
    private Date date;

    public Data(){
        Calendar calendar = Calendar.getInstance();
        date =  calendar.getTime();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long aTaskId) {
        taskId = aTaskId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
