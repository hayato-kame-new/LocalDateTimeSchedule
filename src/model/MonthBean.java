package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.Map;

public class MonthBean implements Serializable {  //   自分で作成したクラスのインスタンスをスコープにおくには、Beanのクラスにして作らないとだめ
    //Beanのクラスのルールにしたがってクラスを作ること スコープには、Beanクラスにしないと保存できないので 普通のString List Map など参照系のクラスのオブジェクトはスコープに置けますが、プリミティブ型は置けない
    // 自分で作成したクラスをインスタンスにしてスコープに置くには、Beanにしないといけない

    /**
     * シリアル番号UID
     */
    private static final long serialVersionUID = 1L;

    private int year;
    private int month;
    private int day;
    private int startWeek; // 今月が何曜日から開始されているか
    private int beforeMonthlastDay;  // 先月が何日までだったか
    private int thisMonthlastDay;  // 今月が何日までか
    private int[] calendarDay;  // カレンダーに載せる日数   最大で7日×6週 要素は最大で42個 最大で [0]から[41]まで    [26, 27, 28, 29, 30, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 1, 2, 3, 4, 5, 6]
    private int weekCount; // 今月は何週あるか


    /**
     * コンストラクタ Beanになるには明示的に 引数なしのコンストラクタが必要
     * 現在の日時を基準にしてフィールドを初期化する
     */
    public MonthBean() {
        LocalDate localdate = LocalDate.now();
       // System.out.println(LocalDate.of(localdate.getYear(), localdate.getMonthValue(), 1).getDayOfWeek().getValue());
//        System.out.println(localdate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth());
//        System.out.println(localdate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth());
        this.year = localdate.getYear();
        this.month = localdate.getMonthValue();
        this.day = localdate.getDayOfMonth();
        this.startWeek = LocalDate.of(this.year, this.month, 1).getDayOfWeek().getValue();
        this.beforeMonthlastDay = localdate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        this.thisMonthlastDay = localdate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        Map<Integer, int[]> map = this.creatCalendarDay( this.startWeek,  this.beforeMonthlastDay,  this.thisMonthlastDay);
        for(Map.Entry<Integer, int[]> entry : map.entrySet()) {
            this.calendarDay = entry.getValue();
            this.weekCount = entry.getKey();
        }
    }

    /**
     * 引数ありコンストラクタ 先月 翌月のインスタンスを作成するために使うコンストラクタ
     * @param localdate １ヶ月前のLocalDateインスタンスや、１ヶ月後のLocalDateインスタンスが引数として渡ってくる
     */
    public MonthBean(LocalDate localdate) {
        this.year = localdate.getYear();
        this.month = localdate.getMonthValue();
        this.day = localdate.getDayOfMonth();
        this.startWeek = LocalDate.of(this.year, this.month, 1).getDayOfWeek().getValue();
        this.beforeMonthlastDay = localdate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        this.thisMonthlastDay = localdate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        Map<Integer, int[]> map = this.creatCalendarDay( this.startWeek,  this.beforeMonthlastDay,  this.thisMonthlastDay);
        for(Map.Entry<Integer, int[]> entry : map.entrySet()) {
            this.calendarDay = entry.getValue();
            this.weekCount = entry.getKey();
        }
    }



    /**
     * 先月と今月と来月の日付を格納して返すメソッド キーがint型今週何週あるのか 値が日付を格納したint型配列
     * @param startWeek
     * @param beforeMonthlastDay
     * @param thisMonthlastDay
     * @return Map<Integer, int[]>    Integer: weekCount<br /> int[]: calendarDay
     */
    public Map<Integer, int[]> creatCalendarDay(int startWeek, int beforeMonthlastDay, int thisMonthlastDay) {
        Map<Integer, int[]> map = new LinkedHashMap<>(); // 格納した順番を守る LinkedHashMapを使う
        int weekCount = 0;
        int[] calendarDay = new int[42];  //  最大で7日×6週
        int count = 0;
        /* 先月分の日付を格納する  　　 -2 じゃなくて -1 に変更した　　　　　*/
        for (int i = startWeek - 1 ; i >= 0 ; i--){
          calendarDay[count++] = beforeMonthlastDay - i;
        }
        /* 今月分の日付を格納する */
        for (int i = 1 ; i <= thisMonthlastDay ; i++){
          calendarDay[count++] = i;
        }
        /* 翌月分の日付を格納する */
        int nextMonthDay = 1;
        while (count % 7 != 0){
          calendarDay[count++] = nextMonthDay++;
        }
        weekCount = count / 7;
       map.put(weekCount, calendarDay);
        return map;
    }





        // アクセッサ ゲッターセッター
        public int getYear() {
            return year;
        }
        public int getMonth() {
            return month;
        }
        public int getDay() {
            return day;
        }
        public int getStartWeek() {
            return startWeek;
        }
        public int getBeforeMonthlastDay() {
            return beforeMonthlastDay;
        }
        public int getThisMonthlastDay() {
            return thisMonthlastDay;
        }
        public int[] getCalendarDay() {
            return calendarDay;
        }
        public int getWeekCount() {
            return weekCount;
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
        public void setStartWeek(int startWeek) {
            this.startWeek = startWeek;
        }
        public void setBeforeMonthlastDay(int beforeMonthlastDay) {
            this.beforeMonthlastDay = beforeMonthlastDay;
        }
        public void setThisMonthlastDay(int thisMonthlastDay) {
            this.thisMonthlastDay = thisMonthlastDay;
        }
        public void setCalendarDay(int[] calendarDay) {
            this.calendarDay = calendarDay;
        }
        public void setWeekCount(int weekCount) {
            this.weekCount = weekCount;
        }


}
