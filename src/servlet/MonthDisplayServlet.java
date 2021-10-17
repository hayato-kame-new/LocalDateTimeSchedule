package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ScheduleDao;
import model.MonthBean;
import model.ScheduleBean;

/**
 * Servlet implementation class MonthDisplayServlet
 */
@WebServlet("/MonthDisplayServlet")
public class MonthDisplayServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MonthDisplayServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 文字化け対策
        request.setCharacterEncoding("UTF-8");

        // aリンクのについてた?以降のクエリーパラメータからの取り出し リクエストスコープから取り出す "current" "next" "before" どれかが入ってる
        String mon = request.getParameter("mon");

        String msg = "";
        // この辺で ログインしてきたユーザのuserIdを取得したい

        //  MonthBeanのBeanは、セッションスコープから取り出す、次の次の次の月など使うため  スケジュール登録してリダイレクトしてきたあともセッションスコープから取得する
        HttpSession session = request.getSession(true);  // 引数のセッション生成フラグにtrueを指定すると、現在セッションが存在しない場合は、生成して返します
        MonthBean monthBean = (MonthBean) session.getAttribute("monthBean");
        session.removeAttribute("monthBean");  // 取り出したら、消しておくセッションから

        // スケジュール登録したあとにリダイレクトしてきた セッションスコープから取得する
           ScheduleBean scheBean = (ScheduleBean) session.getAttribute("scheBean") ;
           if(scheBean != null ) {
               mon = (String) session.getAttribute("mon");  // "scheduleResult"が入ってる
               msg = (String) session.getAttribute("msg");
               session.removeAttribute("scheBean");  // 取り出したら、消しておくセッションから
           }

        // そして、月カレンダーのセルに、その日のスケジュールの全件を表示するためのリストを
           // でもリストジャなくてMapにするかも
           List<ScheduleBean> dayScheduleList = new ArrayList<ScheduleBean>(); // まずnewして確保

           ScheduleDao scheDao = new ScheduleDao();

        switch(mon) {
        case "current":
            // 今月を表示するために新しくインスタンスを生成する 今月は、引数なしのコンストラクタを呼ぶ
            monthBean = new MonthBean();  // newして現在日付の月のインスタンス生成
            //今月のリストを取得する 引数に必要なものに useridもある とりあえず、 1にしてテストする
           //  int[] CalendarDay = monthBean.getCalendarDay();  // [26, 27, 28, 29, 30, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 1, 2, 3, 4, 5, 6]
            int year = monthBean.getYear();
            int month = monthBean.getMonth();
            // その月が何日あるのか
            int thisMonthlastDay = monthBean.getThisMonthlastDay();


             dayScheduleList = scheDao.getDayScheduleList(1 , year,  month , thisMonthlastDay);


            break; // switch文抜ける
        case "before":
            // セッションから取得したBeanインスタンスを利用する   １ヶ月前にする
            LocalDate beforeLocalDate = LocalDate.of(monthBean.getYear(), monthBean.getMonth(), 1).minusMonths(1);
            // 新しくインスタンスを生成する 引数ありのコンストラクタをよぶ １ヶ月前に変更したlocaldateインスタンスを実引数にする
            monthBean = new MonthBean(beforeLocalDate);
            break; // switch文抜ける
        case "next":
             // セッションから取得したBeanインスタンスを利用する １ヶ月後にする
            LocalDate nextLocalDate = LocalDate.of(monthBean.getYear(), monthBean.getMonth(), 1).plusMonths(1);
            // 新しくインスタンスを生成する引数ありのコンストラクタをよぶ １ヶ月後に変更したlocaldateインスタンスを実引数にする
            monthBean = new MonthBean(nextLocalDate);
            break; // switch文抜ける
        case "scheduleResult":

            LocalDate scheduleResultLocalDate = LocalDate.of(scheBean.getScheduleDate().getYear(), scheBean.getScheduleDate().getMonthValue(), scheBean.getScheduleDate().getDayOfMonth());
            // 新しいインスタンスを生成する
            monthBean = new MonthBean(scheduleResultLocalDate);

            break;
        }

     // リクエストスコープに保存する。リクエストスコープは、フォワードできる(リダイレクトはできない)
        // リクエストスコープに保存できるのは、参照型 クラス型のインスタンスだけ。自分で作ったクラスは、JavaBeansのクラスにすること
        request.setAttribute("monthBean", monthBean);
        request.setAttribute("mon", mon);

        // dayScheduleListも保存
        request.setAttribute("dayScheduleList", dayScheduleList);


        // スケジュール登録した後にリダイレクトしてくる時のメッセージを表示するため
        request.setAttribute("msg", msg);

        //   フォワードする
   //   フォワードする 直接HTTPのURLを打ち込んでも、アクセスされないようにするにはWEB-INF配下にする WEB-INFの直下にjspフォルダを自分で作ってその中にフォワード先のjspファイルを置く
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/display.jsp");
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
