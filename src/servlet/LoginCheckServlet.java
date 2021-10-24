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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // リクエストパラメータの取得 login.jspからくる
        // 文字化け対策  今回はフィルターを作ったので、書かなくても大丈夫だが
        request.setCharacterEncoding("UTF-8");

        String scheduleUser = request.getParameter("scheduleUser"); // ユーザー名
        String pass = request.getParameter("pass"); // パスワード

        // セッションスコープを取得 フィルターで、すでにあるかもfalseにして継続にすべきだと思う
        //    HttpSession session = request.getSession(false );
        // こっち下ジャないと思うんだよね。。。ちょっとこっちをコメントにしてみるね
        HttpSession session = request.getSession();

        // セッションスコープのチェック
        if (session == null) {
            // セッションがなかったら index.jspへ フォワード
            request.getRequestDispatcher("./").forward(request, response);
            return;
        } else {
            if (pass.equals("password")) {
                // セッションスコープにユーザ名・パスワードを登録 これがセッションスコープにあるかぎり、あればログインしてることになるよ
                ScheduleUserBean userBean = new ScheduleUserBean(scheduleUser, pass);
                session.setAttribute("userBean", userBean);

                //  ログイン成功したら welcome.jspにフォワード
                request.getRequestDispatcher("/WEB-INF/jsp/welcome.jsp").forward(request, response);
                return;
            } else {
                // ログイン失敗時のメッセージをリクエストスコープに保存
                request.setAttribute("loginFailure", "ログインに失敗しました。もう一度入力してください。");
                // ログイン画面 login.jsp にフォワードする
                // index.jspへ フォワード WebContentからのルート相対パス / から書くこと
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

        }
        // フォワード処理
        //  request.getRequestDispatcher(next).forward(request, response);

    }

}
