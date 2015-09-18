package com.example.michaelwaterworth.r_kit;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by michaelwaterworth on 30/07/15. Copyright Michael Waterworth
 */
class Data extends SugarRecord {
    private Long taskId;//ID of Task
    private String data;//Data to be stored
    private Date date;//Timestamp of when the data was stored.

    public Data() {
        Calendar calendar = Calendar.getInstance();
        date = calendar.getTime();
    }

    static List<Data> serialiseAll() {
        return Data.listAll(Data.class);
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

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("date", date);
            jsonObject.put("taskId", taskId);
            jsonObject.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonObject;
    }
}
