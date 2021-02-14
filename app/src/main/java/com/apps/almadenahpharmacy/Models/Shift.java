package com.apps.almadenahpharmacy.Models;

import java.time.LocalTime;

public class Shift {
    int empId;
    String name ;
    int month ;
    int day ;
    long timeStampComing;
    long timeStampLeave;
    String imageComing;
    String imageLeave;
     int shiftPeriod;
     double shiftPrice;
    //String  spendingTimeInWork;
    public Shift() {
    }

    public Shift(int empId, String name, int month, int day, long timeStampComing, long timeStampLeave, String imageComing, String imageLeave, int shiftPeriod, double shiftPrice) {
        this.empId = empId;
        this.name = name;
        this.month = month;
        this.day = day;
        this.timeStampComing = timeStampComing;
        this.timeStampLeave = timeStampLeave;
        this.imageComing = imageComing;
        this.imageLeave = imageLeave;
        this.shiftPeriod = shiftPeriod;
        this.shiftPrice = shiftPrice;
    }

    public Shift(int empId, int month, int day, long timeStampComing, long timeStampLeave, String imageComing, String imageLeave, int shiftPeriod) {
        this.empId = empId;
        this.month = month;
        this.day = day;
        this.timeStampComing = timeStampComing;
        this.timeStampLeave = timeStampLeave;
        this.imageComing = imageComing;
        this.imageLeave = imageLeave;
        this.shiftPeriod = shiftPeriod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getShiftPrice() {
        return shiftPrice;
    }

    public void setShiftPrice(double shiftPrice) {
        this.shiftPrice = shiftPrice;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
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

    public long getTimeStampComing() {
        return timeStampComing;
    }

    public void setTimeStampComing(long timeStampComing) {
        this.timeStampComing = timeStampComing;
    }

    public long getTimeStampLeave() {
        return timeStampLeave;
    }

    public void setTimeStampLeave(long timeStampLeave) {
        this.timeStampLeave = timeStampLeave;
    }

    public String getImageComing() {
        return imageComing;
    }

    public void setImageComing(String imageComing) {
        this.imageComing = imageComing;
    }

    public String getImageLeave() {
        return imageLeave;
    }

    public void setImageLeave(String imageLeave) {
        this.imageLeave = imageLeave;
    }

    public int getShiftPeriod() {
        return shiftPeriod;
    }

    public void setShiftPeriod(int shiftPeriod) {
        this.shiftPeriod = shiftPeriod;
    }
}

