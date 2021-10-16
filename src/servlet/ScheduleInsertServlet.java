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

        // 文字化け対策
        request.setCharacterEncoding("UTF-8");

        // フォームからの送信で送られてきたのを取得する
        int year = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month"));
        int day = Integer.parseInt(request.getParameter("day"));
        int s_hour = Integer.parseInt(request.getParameter("s_hour"));
        int s_minute = Integer.parseInt(request.getParameter("s_minute"));
        int e_hour = Integer.parseInt(request.getParameter("e_hour"));
        int e_minute = Integer.parseInt(request.getParameter("e_minute"));
        String schedule = request.getParameter("schedule");
        String scheduleMemo = request.getParameter("scheduleMemo");

        LocalDate scheduleDate = LocalDate.of(year, month, month);
        LocalTime startTime = LocalTime.of(s_hour, s_minute);
        LocalTime endTime = LocalTime.of(e_hour, e_minute);


        // idは、自動採番です  userId を用意してないから仮に 1 で練習に登録する
        int userId = 1;  // 仮に練習のために
        // 引数ありのコンストラクタをよぶ
         ScheduleBean scheBean = new ScheduleBean( userId, scheduleDate, startTime, endTime, schedule, scheduleMemo);

         ScheduleDao scheDao = new ScheduleDao();
         String msg = year + "年" + month + "月" + day + "日" + "のスケジュールを登録しました。";
         boolean success = true; // trueならデータベース処理が成功
         // データベースに新規登録
         success = scheDao.addSchedule(scheBean);
         if(!success) {  // falseだったら失敗
             msg = "スケジュールを登録できませんでした。";
         }
         // MonthDisplayServletへリダイレクトします。リダイレクトの際に、year month dayの情報が必要なので、ScheduleBeanインスタンスをセッションスコープに保存します。
         // リダイレクトの時には、リクエストスコープ使えないので、セッションスコープを使う
         // セッションは、requestから呼び出します。 サーブレットでは、セッションは明示的に破棄することが大切　(SpringBootだと自動でフレームワークが行ってくれてるが、明示的に破棄することが大切)
         // スコープに置けるのはインスタンスのみです。Stringなどの参照型のインスタンスは置ける。プリミティブ型は置けない。
         // クラス型のインスタンスは置けるが、自分で作ったクラスのインスタンスをスコープへ置けるようにするには、Beanとして作らないといけない。Beanを作るルール
         HttpSession session = request.getSession(true);
         session.setAttribute("msg", msg);
         session.setAttribute("scheBean", scheBean);
         session.setAttribute("mon", "scheduleResult");  // switch文で必要どの月を表示するのかcaseで切り替えるのに必要

         response.sendRedirect("/LocalDateTimeSchedule/MonthDisplayServlet");
         // クエリーパラメータとして、クエリー文字列を送ってもいい  こっちでもできることを確認すること
         // response.sendRedirect("/MonthDisplayServlet?year=year&month=month&day=day&mon=scheduleResult");


    }

}
