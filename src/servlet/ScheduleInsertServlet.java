package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ScheduleDao;
import model.ScheduleBean;

/**
 * Servlet implementation class ScheduleInsertServlet
 */
@WebServlet("/ScheduleInsertServlet")
public class ScheduleInsertServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScheduleInsertServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 文字化け対策  今回はフィルターを作ったので、書かなくても大丈夫だが
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(); // 引数なしは 引数にtrueと同じ
        // セッションから取り出したものは、バリデーションエラーなどで、time_schedule.jspにフォワードする時に、リクエストスコープへ保存して使う
        List<ScheduleBean> oneDayScheduleList = (List<ScheduleBean>)session.getAttribute("oneDayScheduleList");
        LinkedList<String> timeStack = (LinkedList<String>)session.getAttribute("timeStack");
        // セッションから取り出したら、セッションスコープから削除をしておく
        session.removeAttribute("oneDayScheduleList");
        session.removeAttribute("timeStack");

        // actionの値が hiddenフィールドで送られてくる "add" か "edit" か "delete" "re_enter" 入ってる
        // 削除 "delete"　の時には、フォームからは action と id だけしか 送ってこない
        String action = request.getParameter("action");
        // 編集と削除の時に使う新規では、主キーidの値は int型の規定値(デフォルト値)の 0  編集  削除 では、主キーの値が入ってる hiddenフィールドで送られてくる
        int id = Integer.parseInt(request.getParameter("id"));

        int userId = 0;
        int year = 0;
        int month = 0;
        int day = 0;
        int s_hour = 0;
        int s_minute = 0;
        int e_hour = 0;
        int e_minute = 0;

        LocalDate scheduleDate = null;
        LocalTime startTime = null;
        LocalTime endTime = null;
        String schedule = null;
        String scheduleMemo = null;

        // 削除の時には、渡って来ないので、例外発生を防ぐためにifが必要
        // time_schedule.jspの 新規"add"と編集"edit" 再入力"re_enter"のフォーム からの送信で送られてきたのを取得する
        if(action.equals("add") || action.equals("edit") || action.equals("re_enter")) {
             userId =  Integer.parseInt(request.getParameter("userId"));
             year = Integer.parseInt(request.getParameter("year"));
             month = Integer.parseInt(request.getParameter("month"));
             day = Integer.parseInt(request.getParameter("day"));
             s_hour = Integer.parseInt(request.getParameter("s_hour"));
             s_minute = Integer.parseInt(request.getParameter("s_minute"));
            e_hour = Integer.parseInt(request.getParameter("e_hour"));
             e_minute = Integer.parseInt(request.getParameter("e_minute"));
            schedule = request.getParameter("schedule");
            scheduleMemo = request.getParameter("scheduleMemo");

             scheduleDate = LocalDate.of(year, month, day);
            startTime = LocalTime.of(s_hour, s_minute);
             endTime = LocalTime.of(e_hour, e_minute);

             // action が "add" "edit" "re_enter" の時だけ入力チェックする バリデーションする
             // バリデーションのエラーリストのインスタンスを newで確保
          List<String> errMsgList = new ArrayList<String>(); // エラーなければ、空のリスト  [] と表示されます
             // フォームの s_hour   e_hour  は intなので比較する Javascriptで、s_hourを選択した時に e_hourがそれ以降の時間だけoptionタグを生成するようにしてもいい
             if(s_hour > e_hour) {
                 errMsgList.add("開始時間と終了時間を確認してください");
             }
             if(s_hour == e_hour && s_minute > e_minute) {
                 errMsgList.add("開始時間と終了時間を確認してください");
             }
             //  scheduleとscheduleMemoのカラムは PostgreSQL でデータ型は varchar(67) にしてる これだと、日本語の漢字だと、66文字まで保存できます
             // scheduleMemoは、nullを許可する PostgreSQL でデータ型は varchar(67) にしてる これだと、日本語の漢字だと、66文字まで保存できます
             if(schedule == null || schedule.length() == 0) {
                 errMsgList.add("スケジュールの件名を入力してください");
             } else if (schedule.length() > 66){
                 errMsgList.add("スケジュールの件名は66文字までで入力してください");
             }

             if(scheduleMemo.length() > 66) {
                 errMsgList.add("メモは66文字までで入力してください");
             }

             // ここで、エラーリストに入ってればtime_schedule.jspへ戻すreturnする
             if(errMsgList.size() > 0) {  // エラーがあった
                // リクエストスコープへ保存する
                 request.setAttribute("errMsgList", errMsgList);  // エラーリストを送ります
                 request.setAttribute("scheduleFailureMsg", "スケジュールを登録できませんでした");
                 // 入力値を表示したいので、リスエストスコープへ保存する
                 request.setAttribute("id", id);
                 request.setAttribute("userId", userId);
                 request.setAttribute("year", year);
                 request.setAttribute("month", month);
                 request.setAttribute("day", day);
                 request.setAttribute("s_hour", s_hour);
                 request.setAttribute("s_minute", s_minute);
                 request.setAttribute("e_hour", e_hour);
                 request.setAttribute("e_minute", e_minute);
                 // 上でセッションスコープから取得した、oneDayScheduleList  timeStack もリクエストスコープに保存して送らないといけない
                 request.setAttribute("oneDayScheduleList", oneDayScheduleList);
                 request.setAttribute("timeStack", timeStack);

                 // 再入力の キーaction 値re_enter もリクエストスコープに送る
                 request.setAttribute("action", "re_enter");
                 // フォワードする WebContentからの ルート相対パス  初め/ を書いておくこと  index.jspなら  /  だけでも大丈夫
                 request.getRequestDispatcher("/WEB-INF/jsp/time_schedule.jsp").forward(request, response);
                return;
             }
        }
    //  バリデーションでエラーが無かったので処理を進める
        ScheduleDao scheDao = new ScheduleDao();


        String msg = "";
        boolean success = true; // trueならデータベース処理が成功
        ScheduleBean scheBean = null;

        switch(action) {
        case "add":
            // 新規登録の時だけ使うコンストラクタ(6つの引数のコンストラクタ)を使う  新規登録の時には、主キーの idの値は、データベースに登録される時に、自動採番で生成して登録されますので、idが引数には要りません
            scheBean = new ScheduleBean( userId, scheduleDate, startTime, endTime, schedule, scheduleMemo);

            // データベースに新規登録
            success = scheDao.add(scheBean); // addメソッドの戻り値は boolean型です
            if(success == false) {  // falseだったら失敗 失敗のメッセージ 、time_schedule.jspで表示するフォワードする
                msg = year + "年" + month + "月" + day + "日" + "開始時間" + scheBean.createStrStartTime() + "終了時間" + scheBean.createStrEndTime() + "のスケジュールを新規登録できませんでした。";
             // できなかったら、time_schedule.jspへ戻すこと、入力をしたものを表示できるようにリクエストスコープに保存をしてから
                // フォワードをして、returnを書くこと



            } else { // 成功のメッセージ 成功したら、リダイレクトをするので、下でセッションスコープに入れている
                msg = year + "年" + month + "月" + day + "日" + "開始時間" + scheBean.createStrStartTime() + "終了時間" + scheBean.createStrEndTime() +  "のスケジュールを新規登録しました。";

            }
            break; // switch文を抜ける
        case "edit":
            // 編集の時には、id 主キーの値が必要 hiddenフィールドから取得したので idを元に検索する
            scheBean = scheDao.find(id);
            // scheBeanの idとuserId以外の 5つのフィールド をフォームの値で更新してから、そのインスタンスをデータベースを更新する
            scheBean.setScheduleDate(scheduleDate);
            scheBean.setStartTime(startTime);
            scheBean.setEndTime(endTime);
            scheBean.setSchedule(schedule);
            scheBean.setScheduleMemo(scheduleMemo);
            // データベース更新
            success = scheDao.update(scheBean); // updateメソッドの戻り値は boolean型です
            if (success == false) { // 失敗のメッセージ これは、time_schedule.jspに表示をする
                msg = year + "年" + month + "月" + day + "日" + "開始時間" + scheBean.createStrStartTime() + "終了時間" + scheBean.createStrEndTime() + "のスケジュールを更新できませんでした。";
           // できなかったら、time_schedule.jspへ戻すこと、入力をしたものを表示できるようにリクエストスコープに保存をしてから
                // フォワードをして、returnを書くこと


            } else { // 成功のメッセージ 成功したら、リダイレクトをするので、下でセッションスコープへ入れている
                msg = year + "年" + month + "月" + day + "日" + "開始時間" + scheBean.createStrStartTime() + "終了時間" + scheBean.createStrEndTime() + "のスケジュールを更新しました。";
            }
            break; // switch文を抜ける
        case "delete":
            // 削除の時には、id の値が必要 削除が成功したらリダイレクト後は、削除した月を表示する
            // 削除成功した後や削除をした月(削除しようとした月)の表示をするため
            // 削除の前に idからインスタンスを取得しておくそれからセッションに保存しておく
            scheBean = scheDao.find(id);  // 削除しようとした月を表示するためにセッションにおく

            // このインスタンスをデータベースから削除する
            success = scheDao.delete(id); // updateメソッドの戻り値は boolean型です
            if(success == false) {  // 削除に失敗
                 // できなかったら、time_schedule.jspへ戻すこと、入力をしたものを表示できるようにリクエストスコープに保存をしてから
                // フォワードをして、returnを書くこと


                msg = scheBean.getScheduleDate().getYear() + "年" + scheBean.getScheduleDate().getMonthValue() + "月" + scheBean.getScheduleDate().getDayOfMonth() + "日" + "開始時間" + scheBean.createStrStartTime() + "終了時間" + scheBean.createStrEndTime() + "のスケジュールの削除に失敗しました。";
            } else {
                // 成功のメッセージ 成功したら、リダイレクトをするので、下でセッションスコープへ入れている
                msg = scheBean.getScheduleDate().getYear() + "年" + scheBean.getScheduleDate().getMonthValue() + "月" + scheBean.getScheduleDate().getDayOfMonth() + "日" + "開始時間" + scheBean.createStrStartTime() + "終了時間" + scheBean.createStrEndTime() + "のスケジュールを削除しました。";
            }
            break; // switch文を抜ける
        }

        /*
         * MonthDisplayServletへリダイレクトします。リダイレクトの際に、year month dayの情報が必要なので、ScheduleBeanインスタンスをセッションスコープに保存します。
         * リダイレクトの時には、リクエストスコープ使えないので、セッションスコープを使う
         * セッションは、requestから呼び出します。 サーブレットでは、セッションは明示的に破棄することが大切 (情報:SpringBootだと自動でフレームワークが行ってくれてるが、明示的に破棄することが大切)
         * スコープに置けるのはインスタンスのみです。String List Map などの参照型のインスタンスは置ける。プリミティブ型は置けない。
         * クラス型のインスタンスは置けるが、自分で作ったクラスのインスタンスをスコープへ置けるようにするには、Beanとして作らないといけない。Beanを作るルール
         */

         //  HttpSession session = request.getSession(true);
         session.setAttribute("msg", msg);
         // この ScheduleBeanのインスタンスは、再度月を表示する際に、表示する年月日を情報として、送りたいので、これをセッションに保存してる
         // リダイレクト後は、変更したことを確認するために、変更したスケジュールの月を表示するようにしてる
         session.setAttribute("scheBean", scheBean);

         session.setAttribute("mon", "scheduleResult");  // switch文で必要どの月を表示するのかcaseで切り替えるのに必要

         response.sendRedirect("/LocalDateTimeSchedule/MonthDisplayServlet");
         // クエリーパラメータとして、クエリー文字列を送ってもいい
         // response.sendRedirect("/MonthDisplayServlet?year=year&month=month&day=day&mon=scheduleResult");


    }

}
