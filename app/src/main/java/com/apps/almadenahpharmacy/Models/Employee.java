package com.apps.almadenahpharmacy.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Employee implements Serializable

{
    int id ;
    String name;
    int hoursCount;
    String gender;
    ArrayList<String> daysNames ;
    int extraHourPrice ;
    String ComingHour;
    String LeftHour;
    String lastShift;
    String lastShiftFriday;
    public Employee() {
    }


    public Employee(int id, String name, int hoursCount,  String gender, ArrayList<String> daysNames, int extraHourPrice, String comingHour, String leftHour , String lastShift , String lastShiftFriday) {
        this.id = id;
        this.name = name;
        this.hoursCount = hoursCount;
        this.gender = gender;
        this.daysNames = daysNames;
        this.extraHourPrice = extraHourPrice;
        this.ComingHour = comingHour;
        this.LeftHour = leftHour;
        this.lastShift = lastShift;
        this.lastShiftFriday = lastShiftFriday;
    }

    public Employee(int id, String name, int hoursCount, String gender, ArrayList<String> daysNames, int extraHourPrice, String comingHour, String leftHour) {
        this.id = id;
        this.name = name;
        this.hoursCount = hoursCount;
        this.gender = gender;
        this.daysNames = daysNames;
        this.extraHourPrice = extraHourPrice;
        ComingHour = comingHour;
        LeftHour = leftHour;
    }

    public String getLastShiftFriday() {
        return lastShiftFriday;
    }

    public void setLastShiftFriday(String lastShiftFriday) {
        this.lastShiftFriday = lastShiftFriday;
    }

    public String getLastShift() {
        return lastShift;
    }

    public void setLastShift(String lastShift) {
        this.lastShift = lastShift;
    }

    public String getComingHour() {
        return ComingHour;
    }

    public void setComingHour(String comingHour) {
        ComingHour = comingHour;
    }

    public String getLeftHour() {
        return LeftHour;
    }

    public void setLeftHour(String leftHour) {
        LeftHour = leftHour;
    }

    public int getExtraHourPrice() {
        return extraHourPrice;
    }

    public void setExtraHourPrice(int extraHourPrice) {
        this.extraHourPrice = extraHourPrice;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHoursCount() {
        return hoursCount;
    }

    public void setHoursCount(int hoursCount) {
        this.hoursCount = hoursCount;
    }

    public ArrayList<String> getDaysNames() {
        return daysNames;
    }

    public void setDaysNames(ArrayList<String> daysNames) {
        this.daysNames = daysNames;
    }


}
