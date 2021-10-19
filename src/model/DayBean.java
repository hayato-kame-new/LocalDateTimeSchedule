//package model;
//
//import java.io.Serializable;
//import java.time.LocalDate;
//import java.time.temporal.TemporalAdjusters;
//
//public class DayBean implements Serializable {
//
//    /**
//     * シリアル番号UID
//     */
//    private static final long serialVersionUID = -3873086726870704775L;
//
//    private int year;
//    private int month;
//    private int day;
//    private int thisMonthlastDay;  // 今月が何日までか
//
//    public DayBean() {
//        super();
//    }
//
//    public DayBean(int year, int month, int day) {
//        this.year = year;
//        this.month = month;
//        this.day = day;
//        this.thisMonthlastDay = LocalDate.of(this.year, this.month, this.day).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
//    }
//
//    public int getYear() {
//        return year;
//    }
//
//    public int getMonth() {
//        return month;
//    }
//
//    public int getDay() {
//        return day;
//    }
//
//    public int getThisMonthlastDay() {
//        return thisMonthlastDay;
//    }
//
//    public void setYear(int year) {
//        this.year = year;
//    }
//
//    public void setMonth(int month) {
//        this.month = month;
//    }
//
//    public void setDay(int day) {
//        this.day = day;
//    }
//
//    public void setThisMonthlastDay(int thisMonthlastDay) {
//        this.thisMonthlastDay = thisMonthlastDay;
//    }
//
//
//
//}
