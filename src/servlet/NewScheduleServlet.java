package servlet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ScheduleDao;
import model.DayBean;
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
        // display.jspの画面のaリンク(HTTPメソッドはGET)でアクセスしてくるので ?以降のクエリーパラメーターから取得する  年月日が取れる
        int year = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month"));
        int day = Integer.parseInt(request.getParameter("day"));
        // 年月日をBeanインスタンスに格納 スケジュールのまだ一件もない日でも、表示のために必要
        DayBean dayBean = new DayBean(year, month, day);

        // さらに、そのユーザーのその一日のスケジュールをリクエストスコープに格納すればいい
        ScheduleDao scheDao = new ScheduleDao();
        // ユーザIDはとりあえず 1として呼び出しています。。後で、修正します。そのユーザの指定した日の一日分のスケジュールのリスト取得
        List<ScheduleBean> oneDayScheduleList = scheDao.GetOneDaySchedule(1, year, month, day);

        // ビューを作るためのクラス TimeScheduleView からメソッドを呼び出し
        LinkedList<String> timeStack = TimeScheduleView.makeTimeStack();



     // リクエストスコープに保存する。リクエストスコープは、フォワードできる(リダイレクトはできない)
        // リクエストスコープに保存できるのは、参照型 クラス型のインスタンスだけ。自分で作ったクラスは、JavaBeansのクラスにすること
        request.setAttribute("dayBean", dayBean);
        request.setAttribute("oneDayScheduleList", oneDayScheduleList);
        request.setAttribute("timeStack", timeStack);  // 失敗

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
