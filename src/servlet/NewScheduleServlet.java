package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ScheduleDao;
import model.ScheduleBean;
import viewComposer.TimeScheduleView;

/**
 * Servlet implementation class NewScheduleServlet
 */
@WebServlet("/NewScheduleServlet")
public class NewScheduleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewScheduleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 文字化け対策
        request.setCharacterEncoding("UTF-8");
        // display.jspの画面のaリンク(HTTPメソッドはGET)でアクセスしてくるので ?以降のクエリーパラメーターから取得する  新規では年月日が取れる
        String action = request.getParameter("action");  // 新規の時は add  編集の時は edit が入ってくる

        ScheduleDao scheDao = new ScheduleDao();

        ScheduleBean formScheBean = null;
        LocalDate localDate = null;
        switch(action) {
        case "add":
            // 新規の場合 aリンクのクエリー文字列から取得する必要
             int year = Integer.parseInt(request.getParameter("year"));
             int month = Integer.parseInt(request.getParameter("month"));
             int day = Integer.parseInt(request.getParameter("day"));
            // LocalDate型にする
             localDate = LocalDate.of(year, month, day);
            // 新規の時には、ログインしたユーザのIDを送るようにしてる 注意 主キーのidではない とりあえず、 userId=1 としてクエリー文字列で送ってきてる 後で、ログイン機能作った時にaリンクのところを修正します
            int userId = Integer.parseInt(request.getParameter("userId"));
             // とりあえず userId=1 で送られてきてる  idは、この時点では規定値の 0 としてフィールドの値がなってる
            // コンストラクタは引数6つのを呼び出す
            formScheBean = new ScheduleBean(1, localDate , null, null, null, null);
            // このインスタンスをフォームに送って表示させる
            break;  // switch文から抜ける
        case "edit":
            // 編集の時には ScheduleBeanインスタンスを取得するための主キーで id(プライマリーキー)の値が aリンクの ?以降のクエリーパラメータで送られてくる
            int id = Integer.parseInt(request.getParameter("id"));
            // 入力フォームに表示するために 渡ってきたidから、インスタンスを取得する
            // するとuserIdも取得できる

            formScheBean = scheDao.find(id);  // 主キーで検索する

            localDate = formScheBean.getScheduleDate();

            break;  // switch文から抜ける
        }
        // ユーザIDはとりあえず 1として呼び出しています。。後で、修正します。そのユーザの指定した日の一日分のスケジュールのリスト取得
        // 表示させるために
        List<ScheduleBean> oneDayScheduleList = scheDao.GetOneDaySchedule(1, localDate);


        // 年月日をBeanインスタンスに格納 スケジュールのまだ一件もない日でも、表示のために必要
        // 後でいらなくなるかも
       //  DayBean dayBean = new DayBean(year, month, day);

        // ビューを作るためのクラス TimeScheduleView からメソッドを呼び出し
        LinkedList<String> timeStack = TimeScheduleView.makeTimeStack();

     // リクエストスコープに保存する。リクエストスコープは、フォワードできる(リダイレクトはできない)
        // リクエストスコープに保存できるのは、参照型 クラス型のインスタンスだけ。自分で作ったクラスは、JavaBeansのクラスにすること
     //   request.setAttribute("dayBean", dayBean); // 後でいらなくなるかも
        //  そのユーザーのその一日のスケジュールをリクエストスコープに格納
        request.setAttribute("oneDayScheduleList", oneDayScheduleList);
        request.setAttribute("timeStack", timeStack);
        request.setAttribute("formScheBean", formScheBean);
        request.setAttribute("action", action);

   //   フォワードする 直接HTTPのURLを打ち込んでも、アクセスされないようにするにはWEB-INF配下にする WEB-INFの直下にjspフォルダを自分で作ってその中にフォワード先のjspファイルを置く
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/time_schedule.jsp");
        dispatcher.forward(request, response);

    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
