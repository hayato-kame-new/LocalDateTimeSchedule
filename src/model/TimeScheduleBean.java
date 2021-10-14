package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class TimeScheduleBean implements Serializable {

    /**
     * シリアル番号UID
     */
    private static final long serialVersionUID = 1L;

    int year;
    int month;
    int day;
    int hour;
    int minute;
     private int thisMonthlastDay;  // 今月が何日までか



    public TimeScheduleBean() {
        super();
    }


    public TimeScheduleBean(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.thisMonthlastDay = LocalDate.of(this.year, this.month, this.day).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
    }


    public int getYear() {
        return year;
    }
    public int getMonth() {
        return month;
    }
    public int getDay() {
        return day;
    }
    public int getHour() {
        return hour;
    }
    public int getMinute() {
        return minute;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getThisMonthlastDay() {
        return thisMonthlastDay;
    }

    public void setThisMonthlastDay(int thisMonthlastDay) {
        this.thisMonthlastDay = thisMonthlastDay;
    }





}
