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
        // aリンクのについてた?以降のクエリーパラメータからの取り出し リクエストスコープから取り出す
        String mon = request.getParameter("mon");
        // セッションスコープからの取り出しには、requestから、セッションを呼び出して使う
        // すでにある場合もあるので、引数に trueを指定すること  引数のセッション生成フラグにtrueを指定すると、現在セッションが存在しない場合は、生成して返します。
        HttpSession session = request.getSession(true);
        // aリンクでは、?以降のクエリーパラメータではオブジェクトは送れないから セッションにBeanとしてインスタンスを保存してる
        // 自分で作成したクラスのインスタンスをスコープにおくには、Beanのクラスにして作らないとだめ
        MonthBean monthBean = (MonthBean) session.getAttribute("monthBean"); // "current"の時は nullになってるはずだが、セッションが残ってるとある

        // スケジュール登録した後のリダイレクトではセッションに保存して送ってくる つまりすでにセッションはあるから、上では request.getSession(true);でセッションを生成してる
        ScheduleBean scheBean = (ScheduleBean) session.getAttribute("scheBean") ;
        String msg = "";
        // "scheduleResult"  が入ってるなら switch文で処理する
        if(session.getAttribute("mon") != null  &&  session.getAttribute("mon").equals("scheduleResult")) {
            mon = (String)session.getAttribute("mon");  // "scheduleResult"  が入ってる
            msg = (String) session.getAttribute("msg");
        }
        // そして、月カレンダーのセルに、スケジュールの全件を表示する
        Map<Integer, String> scheduleMap = new HashMap();  // 登録順じゃないのでLinkedHashMapじゃない



        switch(mon) {
        case "current":
            monthBean = new MonthBean();  // セッションで残ってるので、ここでnewして今月のインスタンスにする
            break; // switch文抜ける
        case "before":
            // セッションから取得したBeanインスタンスを使う   １ヶ月前にする
            LocalDate beforeLocalDate = LocalDate.of(monthBean.getYear(), monthBean.getMonth(), 1).minusMonths(1);
            // 引数ありのコンストラクタをよぶ １ヶ月前に変更したlocaldateインスタンスを実引数にする
            monthBean = new MonthBean(beforeLocalDate);
            break; // switch文抜ける
        case "next":
             // セッションから取得したBeanインスタンスを使う  １ヶ月後にする
            LocalDate nextLocalDate = LocalDate.of(monthBean.getYear(), monthBean.getMonth(), 1).plusMonths(1);
            // 引数ありのコンストラクタをよぶ １ヶ月後に変更したlocaldateインスタンスを実引数にする
            monthBean = new MonthBean(nextLocalDate);
            break; // switch文抜ける
        case "scheduleResult":
            // リダイレクトした後に、このサーブレットに来て、スケジュール登録した月のカレンダーを表示するようにする セッションから取り出すか、クエリーパラメータから取り出すのか
            // スケジュール登録成功したのか、失敗したのかのmsgも渡ってくるので取得して、"display.jsp"で表示をさせる
            // セッションスコープを使って取得したscheBeanを元にして、月表示のための MonthBeanインスタンスを生成する
            LocalDate scheduleResultLocalDate = LocalDate.of(scheBean.getScheduleDate().getYear(), scheBean.getScheduleDate().getMonthValue(), scheBean.getScheduleDate().getDayOfMonth());

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
