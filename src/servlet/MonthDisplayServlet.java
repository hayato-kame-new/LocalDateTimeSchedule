package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

        // そして、月カレンダーのセルに、スケジュールの全件を表示する
        Map<Integer, String> scheduleMap = new HashMap();  // 登録順じゃないのでLinkedHashMapじゃない



        switch(mon) {
        case "current":
            // 新しくインスタンスを生成する
            monthBean = new MonthBean();  // newして現在日付の月のインスタンス生成
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

        // スケジュール登録した後にリダイレクトしてくる時のメッセージを表示するため
        request.setAttribute("msg", msg);

        //   フォワードする
        RequestDispatcher dispatcher = request.getRequestDispatcher("display.jsp");
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
