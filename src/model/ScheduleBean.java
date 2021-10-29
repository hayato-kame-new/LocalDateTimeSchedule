package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
//   自分で作成したクラスのインスタンスをスコープにおくには、Beanのクラスにして作らないとだめ
//Beanのクラスのルールにしたがってクラスを作ること スコープには、Beanクラスにしないと保存できないので 普通のString List Map など参照系のクラスのオブジェクトはスコープに置けますが、プリミティブ型は置けない
// 自分で作成したクラスをインスタンスにしてスコープに置くには、Beanにしないといけない
public class ScheduleBean implements Serializable {

    /**
     *  シリアル番号UID
     */
    private static final long serialVersionUID = -2992636428327453702L;

    private int id;    // データベースでは主キー 自動採番  さらにインデックスもついてる
    // 主キーでオートインクリメント(自動採番)なので 新規に作成する時には、INSERTで値を入れなくとも、自動で採番されるカラムです

    private int userId;  // ユーザの idです usertableの主キーの idカラムとリレーションがある PostgreSQLでは、 カラム名が全て小文字になっています userid です
    private LocalDate scheduleDate; // 年月日  scheduledate  PostgreSQLでは、 カラム名が全て小文字
    private LocalTime startTime;  // 開始時間と分  starttime   PostgreSQLでは、 カラム名が全て小文字
    private LocalTime endTime; // 終了時間と分  endtime  PostgreSQLでは、 カラム名が全て小文字

    private String schedule;  //  件名  PostgreSQL でデータ型は varchar(67) にしてる これだと、日本語の漢字だと、66文字まで保存できます
    private String scheduleMemo; // メモ schedulememo  nullを許可する  PostgreSQL でデータ型は varchar(67) にしてる これだと、日本語の漢字だと、66文字まで保存できます


    public ScheduleBean() {
        super();
        // TODO 自動生成されたコンストラクター・スタブ
    }

    // 新規登録の時だけ使うコンストラクタ 6つの引数を使う 新規に登録する際には、引数にidはいらない 自動採番なので、データベースに登録されるときに自動で生成されるので
    public ScheduleBean( int userId, LocalDate scheduleDate, LocalTime startTime, LocalTime endTime,
            String schedule, String scheduleMemo) {
        super();
        // 主キーの idはデータベースに新規に登録されるときに自動生成されるので、ここではいらない 新規登録に使うコンストラクタではいらない
        this.userId = userId;
        this.scheduleDate = scheduleDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.schedule = schedule;
        this.scheduleMemo = scheduleMemo;
    }


    // 検索メソッドに必要な 引数が全てのフィールド(7つの引数)のコンストラクタ 検索するときに、データベースから取得するときに、主キーのidも引数に入れてインスタンスを生成して返すメソッドに使うため、引数に、主キーでユニークのidも必要となる
    // 1日分のリストを作る時に使う
    public ScheduleBean(int id, int userId, LocalDate scheduleDate, LocalTime startTime, LocalTime endTime,
            String schedule, String scheduleMemo) {
        super();
        this.id = id;
        this.userId = userId;
        this.scheduleDate = scheduleDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.schedule = schedule;
        this.scheduleMemo = scheduleMemo;
    }

    // 表示のためにstartTimeを文字列にする 引数なしです 自分自身のインスタンスのフィールドの値を使います
    public String createStrStartTime() {
        int hour = this.startTime.getHour(); // 0 とか  17 とかが取れる 自分自身のインスタンスのフィールドの値を使います
        int minute = this.startTime.getMinute(); // 0  とか  30  注意  0 だったら 00 にすべき
        String strHour = String.valueOf(hour);
        String strMinute = "";
        // 分で 0 が取れた時には、00 としないといけない
        if(minute == 0) {
            strMinute = String.format("%02d", minute);
        } else {
            strMinute = String.valueOf(minute);
        }
        return strHour + ":" + strMinute;  // 0:00 とか 17:30
    }



 // endTimeを文字列にする 引数なしです 自分自身のインスタンスのフィールドの値を使います
    public String createStrEndTime() {
          int hour = this.endTime.getHour(); // 0 とか  17 とかが取れる 自分自身のインスタンスのフィールドの値を使います
          int minute =  this.endTime.getMinute(); // 0  とか  30  注意  0 だったら 00 にすべき
          String strHour = String.valueOf(hour);
          String strMinute = "";
          // 分で 0 が取れた時には、00 としないといけない
          if(minute == 0) {
              strMinute = String.format("%02d", minute);
          } else {
              strMinute = String.valueOf(minute);
          }
          return strHour + ":" + strMinute;  // 0:00 とか 17:30

    }

    // 時間の差分を求める引数なし 自分自身のインスタンスのフィールドの値を使う
    public int rowCount() {
        long minutes = ChronoUnit.MINUTES.between(this.startTime, this.endTime);
        double hours = minutes / 60.0;
        double row =  hours * 2 ;
        return (int)row;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDate getScheduleDate() {
        return scheduleDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getScheduleMemo() {
        return scheduleMemo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setScheduleDate(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setScheduleMemo(String scheduleMemo) {
        this.scheduleMemo = scheduleMemo;
    }

}
