package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        // 参考で 使ってないかもしれないdです


        String action = request.getParameter("action");
         // "login"だった場合に転送先としてWEB-INF/jsp/login.jspを指定
        String forward = null;
        if (action.equals("login")) {
             // ログイン画面の処理
            // login.jspへフォワードする
            forward = "login.jsp";
        } else {
            // 不正なアクションの場合 actionパラメータが指定されていない場合や、認識できないactionパラメータが指定された場合、 ServletException例外をスローしてエラー終了しています。
            // 後で、自分が作成したエラー画面へと差し替えます
            throw new ServletException("不正なリクエストです");
        }

        // JSPへのフォワード
        RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
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
