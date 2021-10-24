package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

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

        // actionの値が hiddenフィールドで送られてくる "add" か "edit" か "delete" 入ってる
        // 削除 "delete"　の時には、フォームからは action と id だけしか 送ってこない
        String action = request.getParameter("action");
        // 新規では、主キーidの値は int型の規定値(デフォルト値)の 0  編集  削除 では、主キーの値が入ってる hiddenフィールドで送られてくる
        int id = Integer.parseInt(request.getParameter("id"));

        int year = 0;
        int month = 0;
        int day = 0;
        LocalDate scheduleDate = null;
        LocalTime startTime = null;
        LocalTime endTime = null;
        String schedule = null;
        String scheduleMemo = null;

        // 削除の時には、渡って来ないので、例外発生を防ぐためにifが必要
        // time_schedule.jspの新規と編集のフォーム からの送信で送られてきたのを取得する
        if(action.equals("add") || action.equals("edit")) {
             year = Integer.parseInt(request.getParameter("year"));
             month = Integer.parseInt(request.getParameter("month"));
             day = Integer.parseInt(request.getParameter("day"));
            int s_hour = Integer.parseInt(request.getParameter("s_hour"));
            int s_minute = Integer.parseInt(request.getParameter("s_minute"));
            int e_hour = Integer.parseInt(request.getParameter("e_hour"));
            int e_minute = Integer.parseInt(request.getParameter("e_minute"));
            schedule = request.getParameter("schedule");
            scheduleMemo = request.getParameter("scheduleMemo");

             scheduleDate = LocalDate.of(year, month, day);
            startTime = LocalTime.of(s_hour, s_minute);
             endTime = LocalTime.of(e_hour, e_minute);
        }

        ScheduleDao scheDao = new ScheduleDao();
        //   userId を用意してないから仮に 1 で練習に登録する
        int userId = 1;  // 仮に練習のために
        String msg = "";
        boolean success = true; // trueならデータベース処理が成功
        ScheduleBean scheBean = null;

        switch(action) {
        case "add":
            // 新規登録の時だけ使うコンストラクタ(6つの引数のコンストラクタ)を使う  新規登録の時には、主キーの idの値は、データベースに登録される時に、自動採番で生成して登録されますので、idが引数には要りません
            scheBean = new ScheduleBean( userId, scheduleDate, startTime, endTime, schedule, scheduleMemo);

            // データベースに新規登録
            success = scheDao.add(scheBean); // addメソッドの戻り値は boolean型です
            if(success == false) {  // falseだったら失敗
                msg = year + "年" + month + "月" + day + "日" + "開始時間" + scheBean.createStrStartTime() + "終了時間" + scheBean.createStrEndTime() + "のスケジュールを新規登録できませんでした。";
            }
            msg = year + "年" + month + "月" + day + "日" + "開始時間" + scheBean.createStrStartTime() + "終了時間" + scheBean.createStrEndTime() +  "のスケジュールを新規登録しました。";
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
            if (success == false) {
                msg = year + "年" + month + "月" + day + "日" + "開始時間" + scheBean.createStrStartTime() + "終了時間" + scheBean.createStrEndTime() + "のスケジュールを更新できませんでした。";
            }
            msg = year + "年" + month + "月" + day + "日" + "開始時間" + scheBean.createStrStartTime() + "終了時間" + scheBean.createStrEndTime() + "のスケジュールを更新しました。";
            break; // switch文を抜ける
        case "delete":
            // 削除の時には、id の値が必要 削除が成功したらリダイレクト後は、削除した月を表示する
            // 削除した後や削除に失敗したあと、削除をした月(削除しようとした月)の表示をするため
            // 削除の前に idからインスタンスを取得しておくそれからセッションに保存しておく
            scheBean = scheDao.find(id);

            // このインスタンスをデータベースから削除する
            success = scheDao.delete(id); // updateメソッドの戻り値は boolean型です
            if(success == false) {  // 削除に失敗
                msg = scheBean.getScheduleDate().getYear() + "年" + scheBean.getScheduleDate().getMonthValue() + "月" + scheBean.getScheduleDate().getDayOfMonth() + "日" + "開始時間" + scheBean.createStrStartTime() + "終了時間" + scheBean.createStrEndTime() + "のスケジュールの削除に失敗しました。";
            }
            msg = scheBean.getScheduleDate().getYear() + "年" + scheBean.getScheduleDate().getMonthValue() + "月" + scheBean.getScheduleDate().getDayOfMonth() + "日" + "開始時間" + scheBean.createStrStartTime() + "終了時間" + scheBean.createStrEndTime() + "のスケジュールを削除しました。";
            break; // switch文を抜ける
        }

        /*
         * MonthDisplayServletへリダイレクトします。リダイレクトの際に、year month dayの情報が必要なので、ScheduleBeanインスタンスをセッションスコープに保存します。
         * リダイレクトの時には、リクエストスコープ使えないので、セッションスコープを使う
         * セッションは、requestから呼び出します。 サーブレットでは、セッションは明示的に破棄することが大切 (情報:SpringBootだと自動でフレームワークが行ってくれてるが、明示的に破棄することが大切)
         * スコープに置けるのはインスタンスのみです。String List Map などの参照型のインスタンスは置ける。プリミティブ型は置けない。
         * クラス型のインスタンスは置けるが、自分で作ったクラスのインスタンスをスコープへ置けるようにするには、Beanとして作らないといけない。Beanを作るルール
         */

          HttpSession session = request.getSession(true);
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
