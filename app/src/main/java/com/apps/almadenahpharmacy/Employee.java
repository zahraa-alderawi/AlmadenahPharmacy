package com.apps.almadenahpharmacy;

import java.io.Serializable;
import java.util.ArrayList;

public class Employee implements Serializable {
    String name;
    int hoursCount;
    int daysCount;
    ArrayList<Day> daysNames ;

    public Employee(String name, int hoursCount, int daysCount, ArrayList<Day> daysNames) {
        this.name = name;
        this.hoursCount = hoursCount;
        this.daysCount = daysCount;
        this.daysNames = daysNames;
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

    public ArrayList<Day> getDaysNames() {
        return daysNames;
    }

    public void setDaysNames(ArrayList<Day> daysNames) {
        this.daysNames = daysNames;
    }
}
