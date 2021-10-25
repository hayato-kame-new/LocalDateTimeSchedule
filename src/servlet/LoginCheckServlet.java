package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDao;
import model.UserBean;

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
        String mail = request.getParameter("mail"); // メールアドレス データベースではユニーク一意制約をつけてあるので、メルアドからUserBeanインスタンスを検索する

        HttpSession  session = request.getSession();

        // セッションスコープのチェック
        if (session == null) {
            // セッションがなかったら index.jspへ フォワード
            request.getRequestDispatcher("./").forward(request, response);
            return;
        } else {


          //  if (pass.equals("password")) {
                // セッションスコープにユーザ名・パスワードを登録 これがセッションスコープにないと、フィルターが聞いて index.jspへ転送されてしまう
                 // メルアドにユニーク一意制約をつけてあるので、メルアドから、データベースに検索をかけます。
                // findByMailメソッドでuserBeanを取得して、それをセッションスコープに置きます。
               //  UserBean userBean = new UserBean(scheduleUser, pass, mail);
              UserDao userDao = new UserDao();
              UserBean userBean = userDao.findByMail(mail); // 見つからない時には nullが返る
              if(userBean == null) {
                  request.setAttribute("loginFailure", "ログインに失敗しました。もう一度入力してください。");
                  request.getRequestDispatcher("./").forward(request, response); // 以降が実行されないここでリターンされる
              }
              // ユーザが見つかったら  セッションに保存する
                session.setAttribute("userBean", userBean);
                //  ログイン成功 welcome.jspにフォワード
                request.getRequestDispatcher("/WEB-INF/jsp/welcome.jsp").forward(request, response);



//                return;
//            } else {
//                // ログイン失敗時のメッセージをリクエストスコープに保存
//                request.setAttribute("loginFailure", "ログインに失敗しました。もう一度入力してください。");
//                // 空の(フィールドが規定値のままの)userBeanをセッションに置く これがセッションスコープにないと、フィルターが聞いて index.jspへ転送されてしまう
//                UserBean userBean = new UserBean(); // 空
//                session.setAttribute("userBean", userBean);
//                // index.jsp にフォワードする
//                request.getRequestDispatcher("./").forward(request, response);
//                return;
//            }

        }

    }

}
