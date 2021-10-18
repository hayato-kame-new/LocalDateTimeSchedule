package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import model.ScheduleBean;

/*
  WebContent   WEB-INF  libフォルダの中に、ドライバー入れたら、ビルドパス構成で適用しないといけません。
 右クリックで ビルドパス ビルドパスの構成 ライブラリタグ クラスパス  JARファイルの追加 で、postgresql-4.2.23.jarを
 今のプロジェクトの中のlibフォルダに入れて適用してください
  /Applications/Eclipse_2020_12.app/Contents/workspace/LocalDateTimeSchedule/Webcontent/WEB-INF/lib
    にして 適用する
*/
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

            // pstmt = conn.prepareStatement(sql);
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            int userId = scheBean.getUserId();

            // LocalDate を java.sql.Dateに変換する
            java.sql.Date sqlScheduleDate = java.sql.Date.valueOf(scheBean.getScheduleDate());
            // LocalDateTime を java.sql.Timeに変換する   java.sql.Timeには、LocalTimeとの間で変換を行うメソッドがあります
            // ただ、これらメソッドは秒までしか対象にしておらず、ミリ秒が破棄されています 今回はやらないが LocalDateTimeなどを経由させて変換することによって、ミリ秒を維持したまま変換できます
            java.sql.Time sqlStartTime = java.sql.Time.valueOf(scheBean.getStartTime());
            java.sql.Time sqlEndTime = java.sql.Time.valueOf(scheBean.getEndTime());

            String schedule = scheBean.getSchedule();
            String scheduleMemo = scheBean.getScheduleMemo();

            // ? のパラメータにセットする
            pstmt.setInt(1, userId);
            pstmt.setDate(2, sqlScheduleDate);
            pstmt.setTime(3, sqlStartTime);
            pstmt.setTime(4, sqlEndTime);
            pstmt.setString(5, schedule);
            pstmt.setString(6, scheduleMemo);

            // executeUpdateメソッドの戻り値は、更新された行数を表します
            int result = pstmt.executeUpdate();
            if (result != 1) {
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


    // 一人のユーザーのひと月分が取れた
    public List<ScheduleBean> getMonthScheduleList(int userId, int year, int month, int thisMonthlastDay) {

        List<ScheduleBean> list = new ArrayList<ScheduleBean>();
        ScheduleBean scheBean = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName(DRIVER_NAME);
            conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);

            for (int i = 1; i <= thisMonthlastDay; i++) {
                // forの中のここに描かないとだめ  // PostgreSQLだと、order by  が無いと、データ更新すると、一番後ろになってしまうので、必要
                String sql = "select * from schedule where userid = ?::integer and scheduledate = ?::date order by starttime"; // テーブル名カラム名小文字で
                pstmt = conn.prepareStatement(sql); // これここ
                pstmt.setInt(1, userId);
                // java.sql.Date型にしてセットしないといけない
                LocalDate localdate = LocalDate.of(year, month, i);
                java.sql.Date sqlDate = java.sql.Date.valueOf(localdate);
                pstmt.setDate(2, sqlDate);

                rs = pstmt.executeQuery();

                LocalDate localdateScheduleDate = null;
                LocalTime localTimeStartTime = null;
                LocalTime localTimeEndTime = null;
                String schedule = null;
                String scheduleMemo = null;

                while (rs.next()) {
                    // java.sql.Dateから LocalDateに変換が必要です
                    localdateScheduleDate = rs.getDate("scheduledate").toLocalDate();
                    // java.sql.Timeから LocalTimeに変換が必要です
                    localTimeStartTime = rs.getTime("starttime").toLocalTime();
                    localTimeEndTime = rs.getTime("endtime").toLocalTime();
                    schedule = rs.getString("schedule");
                    scheduleMemo = rs.getString("schedulememo");
                    scheBean = new ScheduleBean(userId, localdateScheduleDate, localTimeStartTime, localTimeEndTime,
                            schedule, scheduleMemo);
                    list.add(scheBean); //ここで
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null; // エラーの時は、nullを返すようにする。
        } finally {
            if (rs != null) { //close()する順番は、逆からする
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null; // エラーの時は、nullを返すようにする。
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null; // エラーの時は、nullを返すようにする。
                }
            }
            // データベース切断
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null; // エラーの時は、nullを返すようにする。
                }
            }
        }
        return list;
    }


    // タイムスケジュール登録画面で使うそのユーザーのその日のスケジュールのリストが欲しい NewScheduleServletから呼び出す
    public List<ScheduleBean> GetOneDaySchedule(int userId, int year, int month, int day) {
        List<ScheduleBean> oneDayScheduleList = new ArrayList<ScheduleBean>();

        ScheduleBean scheBean = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName(DRIVER_NAME);
            conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);

            String sql = "select * from schedule where userid = ?::integer and scheduledate = ?::date order by starttime"; // テーブル名カラム名小文字で
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            // データベースのdate型のカラムは java.sql.Date型ですので java.sql.Date型インスタンスにしてセットする

          //   java.sql.Date sqlDate = new java.sql.Date(year, month, day);  // このコンストラクタは非推奨になったので使わない方がいい
            LocalDate localdate = LocalDate.of(year, month, day);
            java.sql.Date sqlDate = java.sql.Date.valueOf(localdate);
            pstmt.setDate(2, sqlDate);

            rs = pstmt.executeQuery();

            LocalDate localdateScheduleDate = null;
            LocalTime localTimeStartTime = null;
            LocalTime localTimeEndTime = null;
            String schedule = null;
            String scheduleMemo = null;

            while (rs.next()) {
                // java.sql.Dateから LocalDateに変換が必要です
                localdateScheduleDate = rs.getDate("scheduledate").toLocalDate();
                // java.sql.Timeから LocalTimeに変換が必要です
                localTimeStartTime = rs.getTime("starttime").toLocalTime();
                localTimeEndTime = rs.getTime("endtime").toLocalTime();
                schedule = rs.getString("schedule");
                scheduleMemo = rs.getString("schedulememo");
                scheBean = new ScheduleBean(userId, localdateScheduleDate, localTimeStartTime, localTimeEndTime,
                        schedule, scheduleMemo);
                oneDayScheduleList.add(scheBean);  // ここでリストに追加する whileの中で
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null; // エラーの時は、nullを返すようにする。
        } finally {
            if (rs != null) { //close()する順番は、逆からする
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null; // エラーの時は、nullを返すようにする。
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null; // エラーの時は、nullを返すようにする。
                }
            }
            // データベース切断
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null; // エラーの時は、nullを返すようにする。
                }
            }
        }
        return oneDayScheduleList;
    }

}