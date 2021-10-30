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

    public UserBean add(String name, String pass, int roll, String mail) {
 // 戻り値の　UserBeanインスタンスには、主キーがちゃんと入ってるのをリターンする。そして、それを、セッションスコープに保存をしてログインした状態とする
       UserBean userBean = null;  // 主キーもきちんと入ってる完全形のUserBean これを後でセッションスコープに置きます
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // JDBCドライバを読み込み
            Class.forName(DRIVER_NAME);
            // データベースへ接続
            conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);

            // PostgreSQLだと、全部小文字でカラム名やテーブル名を書くこと id は、自動採番なので、書かない
            // user は PostgreSQLの予約語のため、なるべく使わない ""で囲んでエスケープすれば使えるけど、使わないほうがいい
            // 主キーの id が serial です シーケンス（データ型のserial）とは 自動採番するカラムです。
            // シーケンスとはINSERTで値を入れなくとも、自動で採番される列で、CREATE SEQUENCE文で作成することができます。またテーブル作成時、データ型に「serial」を指定した場合も同じくシーケンスとなります。シーケンスは自動で1から採番され、＋1ずつされます。
            String sql = "insert into usertable (name, pass, roll, mail) values (?, ?, ?::integer, ?)";

            // PostgresSQLは、Statement生成時に、Statement.RETURN_GENERATED_KEYSを指定するとStatement#getGeneratedKeysでそのテーブルの全カラムの情報が取得される。

           //  pstmt = conn.prepareStatement(sql);

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

             // insertする時は、主キーは自動採番 PostgreSQLで serial としてるので、書かないでも大丈夫
            pstmt.setString(1, name);
            pstmt.setString(2, pass);
            pstmt.setInt(3, roll);
            pstmt.setString(4, mail);

            // generateKeyを取得したい 解決策：追加 Statement.RETURN_GENERATED_KEYS でもつけるとエラー文法的に
            // executeUpdate() 注意  戻り値を取得するような書き方をするとエラーになります！！
            pstmt.executeUpdate();  // ここに引数を入れてはいけません 戻り値は変更したデータの数

            // 取れる自動生成した主キーの値
            rs = pstmt.getGeneratedKeys();  // この Statement オブジェクトを実行した結果として作成された自動生成キーを取得します。この Statement オブジェクトがキーを生成しなかった場合は、空の ResultSet オブジェクトが返されます。

            if(rs.next()) {
                 pstmt.getMetaData().getColumnCount();

                int id = rs.getInt(1);  // 引数は 先頭なので 1を指定する  注: 自動生成キーを表す列が指定されなかった場合、JDBC ドライバ実装では、自動生成キーを表すのに最適な列を判断します。
   //  PostgreSQLはgetGeneratedKeys()メソッドをサポートしてます  JDBC ドライバがこのメソッドgetGeneratedKeys() をサポートしない場合例外発生します PostgreSQLはサポートしてます
             //   String name = rs.getString("name");  // 取ることも可能です
             //   String pass = rs.getString("pass");  // 取ることも可能です
              //  int roll = rs.getInt("roll");
              //  String mail = rs.getString("mail");

                userBean = new UserBean(id, name, pass, roll, mail );  // idが取得できれば、これできそうだな
            }

        } catch (SQLException | ClassNotFoundException e) {
            // データベース接続やSQL実行失敗時の処理
            // JDBCドライバが見つからなかったときの処理
            e.printStackTrace();
            return null; // 失敗したら false返す
        } finally {
            // PrepareStatementインスタンスのクローズ処理
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    // クローズ処理失敗時の処理
                    e.printStackTrace();
                    return null; // 失敗したら false返す
                }
            }
            // データベース切断
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // データベース切断失敗時の処理
                    e.printStackTrace();
                    return null; // 失敗したら false返す
                }
            }
        }
        return userBean;

    }

    // 一番最後のユーザの主キーidの値を取得する データベースで何らかのエラーがあった時は 0 を返す
    public int getNewId() {
   // これはソルトが必要なので
        int newId = 0; // 0で初期化してる
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
            if (rs.next()) { //  limit 1 だから、１件しか取得しないので whileじゃなくてifでもいい
                int lastId = rs.getInt("id"); // カーソルが trueだったらつまり、次に行があったので
                //  id = rs.getInt(1);  // 引数 1  でもいいです
                newId = lastId + 1;
            } else { // カーソルが falseを示した時、つまり、何も次にない状態の時は、まだ、１つもデータがないので id = 1を入れる
                newId = 1; // 最初のデータとなる
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
            if (rs.next()) { // 一意制約のカラムを元にして検索したので、1件だけ返るので whileじゃなくて ifでもいい
                int id = rs.getInt("id");
                String name = rs.getString("name");
                // パスワードは、ハッシュ化されたものが、passカラム名でデータベースに格納されてる
                String pass = rs.getString("pass");
                int roll = rs.getInt("roll");
                // String mail = rs.getString("mail");
                userBean = new UserBean(id, name, pass, roll, mail);
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

    // 主キーから検索する
    public UserBean findById(int id) {

        UserBean userBean = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "select * from usertable where id = ?::integer";

        try {
            // JDBCドライバを読み込み
            Class.forName(DRIVER_NAME);
            // データベースへ接続
            conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();
            if (rs.next()) { // 主キーで検索してるので、得られる件数は１件のみなので、whileじゃなくて ifでいい
                String name = rs.getString("name");
                String pass = rs.getString("pass");
                int roll = rs.getInt("roll");
                String mail = rs.getString("mail");
                userBean = new UserBean(id, name, pass, roll, mail);
            }
        } catch (SQLException | ClassNotFoundException e) {
            // データベース接続やSQL実行失敗時の処理
            // JDBCドライバが見つからなかったときの処理
            e.printStackTrace();
            return null; // 失敗したらnull返す
        } finally {
            // PrepareStatementインスタンスのクローズ処理
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    // クローズ処理失敗時の処理
                    e.printStackTrace();
                    return null; // 失敗したら false返す
                }
            }
            // データベース切断
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // データベース切断失敗時の処理
                    e.printStackTrace();
                    return null; // 失敗したら false返す
                }
            }
        }
        return userBean;

    }
}
