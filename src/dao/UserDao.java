package dao;
/*
WebContent   WEB-INF  libフォルダの中に、ドライバー入れたら、ビルドパス構成で適用しないといけません。
右クリックで ビルドパス ビルドパスの構成 ライブラリタグ クラスパス  JARファイルの追加 で、postgresql-4.2.23.jarを
今のプロジェクトの中のlibフォルダに入れて適用してください
/Applications/Eclipse_2020_12.app/Contents/workspace/LocalDateTimeSchedule/Webcontent/WEB-INF/lib
  にして 適用する
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.UserBean;

public class UserDao {

    final String DRIVER_NAME = "org.postgresql.Driver";
    final String JDBC_URL = "jdbc:postgresql://localhost:5432/localdatetimeschedule";
    final String DB_USER = "postgres";
    final String DB_PASS = "postgres";


    public boolean add(UserBean userBean) {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // JDBCドライバを読み込み
            Class.forName(DRIVER_NAME);
            // データベースへ接続
            conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);

            // PostgreSQLだと、全部小文字でカラム名やテーブル名を書くこと id は、自動採番なので、書かない
            // user は PostgreSQLの予約語のため、なるべく使わない ""で囲んでエスケープすれば使えるけど、使わないほうがいい
            // 主キーの id が serial です シーケンス（データ型のserial）とは 自動採番するカラムです。
            // シーケンスとはINSERTで値を入れなくとも、自動で採番される列で、CREATE SEQUENCE文で作成することができます。またテーブル作成時、データ型に「serial」を指定した場合も同じくシーケンスとなります。シーケンスは自動で1から採番され、＋1ずつされます。
            String sql = "insert into usertable (scheduleuser, pass, roll, mail) values (?, ?, ?::integer, ?)";

            // pstmt = conn.prepareStatement(sql);
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, userBean.getScheduleUser());
            pstmt.setString(2, userBean.getPass());
            pstmt.setInt(3, userBean.getRoll());
            pstmt.setString(4, userBean.getMail());

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


    // 一番最後のユーザの主キーidの値を取得する データベースで何らかのエラーがあった時は 0 を返す
    public int getNewId() {

        int newId = 0;  // 0で初期化してる
         Connection conn = null;
         PreparedStatement pstmt = null;
         ResultSet rs = null;

         try {
             // JDBCドライバを読み込み
             Class.forName(DRIVER_NAME);
             // データベースへ接続
             conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);

             String sql = "select id from usertable order by id desc limit 1";

             pstmt = conn.prepareStatement(sql);
             rs = pstmt.executeQuery();
             if(rs.next()) {  //  limit 1 だから、１件しか取得しないので whileじゃなくてifでもいい
                  int lastId = rs.getInt("id");  // カーソルが trueだったらつまり、次に行があったので
                 //  id = rs.getInt(1);  // 引数 1  でもいいです
                  newId = lastId + 1;
             } else {  // カーソルが falseを示した時、つまり、何も次にない状態の時は、まだ、１つもデータがないので id = 1を入れる
                 newId = 1;  // 最初のデータとなる
             }
         } catch (Exception e) { // Exceptionクラスのインスタンスでキャッチする
             e.printStackTrace();
             return 0; // 失敗したら、0を返す
         } finally {
             // PrepareStatementインスタンス、ResultSetインスタンスのクローズ処理
             if (rs != null) {
                 try {
                     rs.close();
                 } catch (SQLException e) {
                     // クローズ処理失敗時の処理
                     e.printStackTrace();
                     return 0; // 失敗したら、0を返す
                 }
             }
             if (pstmt != null) {
                 try {
                     pstmt.close();
                 } catch (SQLException e) {
                     // クローズ処理失敗時の処理
                     e.printStackTrace();
                     return 0;// 失敗したら、0を返す
                 }
             }
             // データベース切断
             if (conn != null) {
                 try {
                     conn.close();
                 } catch (SQLException e) {
                     // データベース切断失敗時の処理
                     e.printStackTrace();
                     return 0;// 失敗したら、0を返す
                 }
             }
         }
         return newId;
     }


    // ログイン時はメルアドでまず検索する　UserBeanが返る メルアドはユニーク(一意制約つけたカラムなので検索できる)
    public UserBean loginFind(String mail) {
        UserBean userBean = null;
         Connection conn = null;
         PreparedStatement pstmt = null;
         ResultSet rs = null;

         try {
             // JDBCドライバを読み込み
             Class.forName(DRIVER_NAME);
             // データベースへ接続
             conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);

             String sql = "select * from usertable where mail = ?";

             pstmt = conn.prepareStatement(sql);

             pstmt.setString(1, mail);
             rs = pstmt.executeQuery();
             if(rs.next()) {  // 一意制約のカラムを元にして検索したので、1件だけ返るので whileじゃなくて ifでもいい
                int id = rs.getInt("id");
                String scheduleUser = rs.getString("scheduleUser");
                // パスワードは、ハッシュ化されたものが、passカラム名でデータベースに格納されてる
                String pass = rs.getString("pass");
                int roll = rs.getInt("roll");
               // String mail = rs.getString("mail");
                userBean = new UserBean(id, scheduleUser, pass, roll, mail);
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
         return userBean;
     }


}
