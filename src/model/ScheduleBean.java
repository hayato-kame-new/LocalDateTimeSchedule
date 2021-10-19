package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class ScheduleBean implements Serializable {

    /**
     *  シリアル番号UID
     */
    private static final long serialVersionUID = -2992636428327453702L;

    private int id;    // データベースでは主キー 自動採番  さらにインデックスもついてる
    private int userId;  // PostgreSQLでは、 カラム名が全て小文字になっています userid です
    private LocalDate scheduleDate; // scheduledate
    private LocalTime startTime;  // starttime
    private LocalTime endTime; // endtime
    private String schedule;
    private String scheduleMemo; // schedulememo


    public ScheduleBean() {
        super();
        // TODO 自動生成されたコンストラクター・スタブ
    }

    // 引数にidはいらない 自動採番なので、データベースに登録されるときに自動で生成されるので
    public ScheduleBean( int userId, LocalDate scheduleDate, LocalTime startTime, LocalTime endTime,
            String schedule, String scheduleMemo) {
        super();
        // 主キーの idはデータベースに登録されるときに自動生成されるので、ここではいらない
        this.userId = userId;
        this.scheduleDate = scheduleDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.schedule = schedule;
        this.scheduleMemo = scheduleMemo;
    }

    // startTimeを文字列にする 引数なしです 自分自身のインスタンスのフィールドの値を使います
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
