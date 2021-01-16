package com.apps.almadenahpharmacy.Models;

import java.time.LocalTime;

public class Shift {

    long timeStampAttendance;
    String imageAttendance;

    long timeStampLeave;
    String imageLeave;

    String  spendingTimeInWork;
    long  minutesCountInWork;
      int month ;
      int day ;
    public Shift() {
    }

    public Shift( long timeStampAttendance, String imageAttendance,long timeStampLeave,
                 String imageLeave, String spendingTimeInWork, long minutesCountInWork,int month , int day) {

        this.timeStampAttendance = timeStampAttendance;
        this.imageAttendance = imageAttendance;
        this.timeStampLeave = timeStampLeave;
        this.imageLeave = imageLeave;
        this.spendingTimeInWork = spendingTimeInWork;
        this.minutesCountInWork = minutesCountInWork;
        this.month = month;
        this.day = day ;

    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getTimeStampAttendance() {
        return timeStampAttendance;
    }

    public void setTimeStampAttendance(long timeStampAttendance) {
        this.timeStampAttendance = timeStampAttendance;
    }

    public long getTimeStampLeave() {
        return timeStampLeave;
    }

    public void setTimeStampLeave(long timeStampLeave) {
        this.timeStampLeave = timeStampLeave;
    }


    public String getImageAttendance() {
        return imageAttendance;
    }

    public void setImageAttendance(String imageAttendance) {
        this.imageAttendance = imageAttendance;
    }

    public String getImageLeave() {
        return imageLeave;
    }

    public void setImageLeave(String imageLeave) {
        this.imageLeave = imageLeave;
    }

}
