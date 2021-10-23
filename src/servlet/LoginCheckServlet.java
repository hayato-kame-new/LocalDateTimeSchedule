package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ScheduleUserBean;

/**
 * Servlet implementation class LoginCheckServlet
 */
@WebServlet("/LoginCheckServlet")
public class LoginCheckServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginCheckServlet() {
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

          // リクエストパラメータの取得 login.jspからくる
        request.setCharacterEncoding("UTF-8");  // 文字化け防止
      //   String action = request.getParameter("action");  // "login"  もしくは null  なのかしら
        String scheduleUser = request.getParameter("scheduleUser");  // ユーザー名
        String pass = request.getParameter("pass");  // パスワード

        // パスワードが、データベースのものと同じだったら、welcomeにフォワードする
        // そうでないなら、ログイン画面にフォワードする
     // 遷移先のパスを設定
        String next = "";
      // データベースに問い合わせる pass を
        // それを取得する
        // pass が、データベースから取り出したものと同じなら、もしくは passをハッシュしたのと、データベースから取り出したのをハッシュしたの？
     // ログイン処理
        if( pass.equals("password") ) {
          // ユーザ情報をセッションスコープに保存
          HttpSession session = request.getSession();
          // ユーザー名とパスから ユーザBeanインスタンスを取得する
          session.setAttribute("userBean", new ScheduleUserBean(scheduleUser, pass));
          //welcome.jspへフォワードする
         next = next + "/WEB-INF/jsp/welcome.jsp";

        }else{
          // ログイン失敗時のメッセージをリクエストスコープに保存
          request.setAttribute("loginFailure", "ログインに失敗しました。もう一度入力してください。");
           // ログイン画面 login.jsp にフォワードする
         next =  "/login.jsp";
        }

        // フォワード処理
        request.getRequestDispatcher(next).forward(request, response);


    }

}
