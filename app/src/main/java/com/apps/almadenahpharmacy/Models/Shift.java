package com.apps.almadenahpharmacy.Models;

import java.time.LocalTime;

public class Shift {

    String timeAttendance;
    long timeStampAttendance;
    String imageAttendance;

    String timeLeave;
    long timeStampLeave;
    String imageLeave;

    String  spendingTimeInWork;
    long  minutesCountInWork;
      int month ;
      int day ;
    public Shift() {
    }

    public Shift(String timeAttendance, long timeStampAttendance, String imageAttendance, String timeLeave, long timeStampLeave,
                 String imageLeave, String spendingTimeInWork, long minutesCountInWork,int month , int day) {
        this.timeAttendance = timeAttendance;
        this.timeStampAttendance = timeStampAttendance;
        this.imageAttendance = imageAttendance;
        this.timeLeave = timeLeave;
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



    public String getTimeAttendance() {
        return timeAttendance;
    }

    public void setTimeAttendance(String timeAttendance) {
        this.timeAttendance = timeAttendance;
    }

    public String getImageAttendance() {
        return imageAttendance;
    }

    public void setImageAttendance(String imageAttendance) {
        this.imageAttendance = imageAttendance;
    }

    public String getTimeLeave() {
        return timeLeave;
    }

    public void setTimeLeave(String timeLeave) {
        this.timeLeave = timeLeave;
    }

    public String getImageLeave() {
        return imageLeave;
    }

    public void setImageLeave(String imageLeave) {
        this.imageLeave = imageLeave;
    }

}
