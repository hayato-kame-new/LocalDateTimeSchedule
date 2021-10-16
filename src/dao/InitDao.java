package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.ScheduleBean;

public class InitDao {

    final String DRIVER_NAME = "org.postgresql.Driver";
    final String JDBC_URL = "jdbc:postgresql://localhost:5432/localdatetimeschedule";
    final String DB_USER = "postgres";
    final String DB_PASS = "postgres";

    public List<ScheduleBean> getDayScheduleList() {

        List<ScheduleBean> list = new ArrayList<ScheduleBean>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName(DRIVER_NAME);
            conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
            // PostgreSQLだと、order by  が無いと、データ更新すると、一番後ろになってしまうので、必要
            String sql = "select * from schedule where userid = ? and scheduledate = ? order by starttime";  // テーブル名カラム名小文字で
            pstmt = conn.prepareStatement(sql);

    }



}
