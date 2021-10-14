package servlet;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.MonthBean;

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
        HttpSession session = request.getSession();
        // aリンクでは、?以降のクエリーパラメータではオブジェクトは送れないから セッションにBeanとしてインスタンスを保存してる
        // 自分で作成したクラスのインスタンスをスコープにおくには、Beanのクラスにして作らないとだめ
        MonthBean monthBean = (MonthBean) session.getAttribute("monthBean"); // "current"の時は nullになってるはずだが、セッションが残ってるとある

        switch(mon) {
        case "current":
            monthBean = new MonthBean();  // セッションで残ってるので、ここでnewして今月のインスタンスにする
            break; // switch文抜ける
        case "before":
            // セッションから取得したBeanインスタンスを使う   １ヶ月前にする
            LocalDate before = LocalDate.of(monthBean.getYear(), monthBean.getMonth(), 1).minusMonths(1);
            // 引数ありのコンストラクタをよぶ １ヶ月前に変更したlocaldateインスタンスを実引数にする
            monthBean = new MonthBean(before);
            break; // switch文抜ける
        case "next":
             // セッションから取得したBeanインスタンスを使う  １ヶ月後にする
            LocalDate next = LocalDate.of(monthBean.getYear(), monthBean.getMonth(), 1).plusMonths(1);
            // 引数ありのコンストラクタをよぶ １ヶ月後に変更したlocaldateインスタンスを実引数にする
            monthBean = new MonthBean(next);
            break; // switch文抜ける
        }

     // リクエストスコープに保存する。リクエストスコープは、フォワードできる(リダイレクトはできない)
        // リクエストスコープに保存できるのは、参照型 クラス型のインスタンスだけ。自分で作ったクラスは、JavaBeansのクラスにすること
        request.setAttribute("monthBean", monthBean);
        request.setAttribute("mon", mon);

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
