package com.example.wustls14.dy_beacon.model;

/**
 * Created by wustls14 on 2017-12-04.
 */

public class AlarmModel {

    boolean alarmtest;

    public AlarmModel( boolean alarmtest) {
     this.alarmtest = alarmtest;
    }


    public boolean isAlarmtest() {return alarmtest;}

    public void setAlarmtest(boolean alarmtest) {this.alarmtest = alarmtest;}
}
