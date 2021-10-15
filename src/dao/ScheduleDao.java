package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.ScheduleBean;

public class ScheduleDao {

    final String DRIVER_NAME = "org.postgresql.Driver";
    final String JDBC_URL = "jdbc:postgresql://localhost:5432/localdatetimeschedule";
    final String DB_USER = "postgres";
    final String DB_PASS = "postgres";

    public boolean addSchedule(ScheduleBean scheBean) {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
             // JDBCドライバを読み込み
            Class.forName(DRIVER_NAME);
            // データベースへ接続
            conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
            // PostgreSQLだと、全部小文字でカラム名やテーブル名を書くこと
            String sql = "insert into schedule (userid, scheduledate, starttime, endtime, schedule, schedulememo) values (?::integer, ?::date, ?::time, ?::time, ?, ?)";

            pstmt = conn.prepareStatement(sql);
            // LocalDate を java.sql.Dateに変換する
            java.sql.Date sqlScheduleDate = java.sql.Date.valueOf(scheBean.getScheduleDate());
            // LocalDateTime を java.sql.Timeに変換する   java.sql.Timeには、LocalTimeとの間で変換を行うメソッドがあります
            // ただ、これらメソッドは秒までしか対象にしておらず、ミリ秒が破棄されています 今回はやらないが LocalDateTimeなどを経由させて変換することによって、ミリ秒を維持したまま変換できます
            java.sql.Time sqlStartTime = java.sql.Time.valueOf(scheBean.getStartTime());
            java.sql.Time sqlEndTime = java.sql.Time.valueOf(scheBean.getEndTime());

            // ? のパラメータにセットする
            pstmt.setInt(1, scheBean.getUserId());
            pstmt.setDate(2, sqlScheduleDate);
            pstmt.setTime(3, sqlStartTime);
            pstmt.setTime(4, sqlEndTime);
            pstmt.setString(5, scheBean.getSchedule());
            pstmt.setString(6, scheBean.getScheduleMemo());

            // executeUpdateメソッドの戻り値は、更新された行数を表します
            int result = pstmt.executeUpdate();
            if(result != 1) {
                return false; // 失敗したら false返す
            }

        } catch (SQLException | ClassNotFoundException e) {
            // データベース接続やSQL実行失敗時の処理
            // JDBCドライバが見つからなかったときの処理
            e.printStackTrace();
            return false; // 失敗したら false返す
        } finally {
            // PrepareStatementインスタンスのクローズ処理
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    // クローズ処理失敗時の処理
                    e.printStackTrace();
                    return false; // 失敗したら false返す
                }
            }
            // データベース切断
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // データベース切断失敗時の処理
                    e.printStackTrace();
                    return false; // 失敗したら false返す
                }
            }
        }
        return true;
    }
}
