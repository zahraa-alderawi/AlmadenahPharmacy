package com.apps.almadenahpharmacy.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Employee implements Serializable

{
    int id ;
    String name;
    int hoursCount;
    int daysCount;
    String gender;
    ArrayList<String> daysNames ;
    int extraHourPrice ;
    public Employee() {
    }


    public Employee(int id, String name, int hoursCount, int daysCount, ArrayList<String> daysNames, String gender,int extraHourPrice) {
        this.id = id;
        this.name = name;
        this.hoursCount = hoursCount;
        this.daysCount = daysCount;
        this.daysNames = daysNames;
        this.gender = gender;
        this.extraHourPrice = extraHourPrice;
    }
    public Employee(int id, String name, int hoursCount, int daysCount, ArrayList<String> daysNames, String gender) {
        this.id = id;
        this.name = name;
        this.hoursCount = hoursCount;
        this.daysCount = daysCount;
        this.daysNames = daysNames;
        this.gender = gender;
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

    public int getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(int daysCount) {
        this.daysCount = daysCount;
    }

    public ArrayList<String> getDaysNames() {
        return daysNames;
    }

    public void setDaysNames(ArrayList<String> daysNames) {
        this.daysNames = daysNames;
    }


}
