package model;

import java.io.Serializable;

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



    public TimeScheduleBean() {
        super();
        // TODO 自動生成されたコンストラクター・スタブ
    }


    public TimeScheduleBean(int year, int month, int day) {
        super();
        this.year = year;
        this.month = month;
        this.day = day;
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





}
