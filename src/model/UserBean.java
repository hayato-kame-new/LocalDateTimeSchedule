package model;

import java.io.Serializable;

//自分で作成したクラスのインスタンスをスコープにおくには、Beanのクラスにして作らないとだめ
//Beanのクラスのルールにしたがってクラスを作ること スコープには、Beanクラスにしないと保存できないので 普通のString List Map など参照系のクラスのオブジェクトはスコープに置けますが、プリミティブ型は置けない
//自分で作成したクラスをインスタンスにしてスコープに置くには、Beanにしないといけない

// PostgreSQLは user が予約語なので、 userは "" で囲んでエスケープすれば使えますが、使わないほうがいいです いちいち select文などでも "user" と書かないとダメです。
// このモデル Beanクラスに関連するテーブルは usertable テーブルです  userが予約語なので、usertable としてます
//  ユーザーのカラムは schduleuser カラムとしてデータベースで作っています ちなみに、PostgreSQLは テーブル名カラム名全て小文字です
public class UserBean implements Serializable {

    /**
     * シリアル番号UID
     */
    private static final long serialVersionUID = -3625540505334959123L;

    private int id;  // 主キーでオートインクリメント(自動採番)なので 新規に作成する時には、INSERTで値を入れなくとも、自動で採番されるカラムです
    // idカラムには さらにインデックスもついてる
    private String scheduleUser;  // ユーザー名  カラム名は全て小文字 scheduleuser  です
    private String pass;  // パスワード 暗号化は、ハッシュ関数と呼ばれる、ある文字列から復元不可能な文字列を生成するしくみを使います。

    private int roll;  // 管理者権限を持ったユーザーは「roll」カラムに「1」を設定します  管理者なら1、それ以外は0
    private String mail;  // データベースのカラムにはユニーク一意制約をつけてあります

    public UserBean() {
        super();
        // TODO 自動生成されたコンストラクター・スタブ
    }



// id以外のカラム引数にして 新規登録の時に使う
    public UserBean(String scheduleUser, String pass, int roll, String mail) {
        super();
        this.scheduleUser = scheduleUser;
        this.pass = pass;
        this.roll = roll;
        this.mail = mail;
    }

    public int getId() {
        return id;
    }

    public String getScheduleUser() {
        return scheduleUser;
    }

//    public String getPass() {
//        return pass;
//    }

    public int getRoll() {
        return roll;
    }

    public String getMail() {
        return mail;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setScheduleUser(String scheduleUser) {
        this.scheduleUser = scheduleUser;
    }

//    public void setPass(String pass) {
//        this.pass = pass;
//    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

}
