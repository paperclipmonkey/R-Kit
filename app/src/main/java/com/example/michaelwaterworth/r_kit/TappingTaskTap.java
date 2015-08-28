package com.example.michaelwaterworth.r_kit;

/**
 * Created by michaelwaterworth on 27/08/15.
 */
public class TappingTaskTap {
    Long timeStamp; //Millis
    int buttonNo; //ID of button - unique to the two buttons
    public TappingTaskTap(Long pTimeStamp, int pButtonNo){
        timeStamp = pTimeStamp;
        buttonNo = pButtonNo;
    }

    @Override
    public String toString() {
        return "timeStamp:" + timeStamp + "\n" +
                "buttonNo:" + buttonNo;
    }
}