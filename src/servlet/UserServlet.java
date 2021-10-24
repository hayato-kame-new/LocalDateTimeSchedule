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
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
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
           // フォームに行ったら、別のサーブレットに行って、そこでデータベースに登録します。
        // ユーザーをBeanのインスタンスにしたら、それを、セッションスコープにおくこと、Loginした時と同じようにしておくこと
         // 文字化け対策  今回はフィルターを作ったので、書かなくても大丈夫だが
        request.setCharacterEncoding("UTF-8");
        String scheduleUser = request.getParameter("scheduleUser");
        String pass = request.getParameter("pass");
        int roll = Integer.parseInt(request.getParameter("roll"));

        // セッションを取得するgetSession() は　getSession(true) と同じことらしい このリクエストに関連付けられている現在のセッションを返すか、リクエストにセッションがない場合は作成します。
        HttpSession session = request.getSession();

        // セッションスコープのチェック
        if (session == null) {
            // セッションがなかったら index.jspへ フォワード
            request.getRequestDispatcher("./").forward(request, response);
            return;
        } else {
            // セッションがあったら、新規に登録して登録が成功したら、セッションスコープに UserBeanインスタンスを保存しておきます。
            UserDao userDao = new UserDao();
            UserBean userBean = new UserBean(scheduleUser, pass);

            //データベースに登録する 成功したら、セッションスコープにUserBeanインスタンスを保存しておく
            boolean result = true;
            String msg = "";
            result = userDao.add(userBean);
            if(result == false) { // 失敗
                // メッセージを書いてユーザ登録画面へ戻るかな
                // フォワードして
                // リターンを書く return で、即終了させる　この行以降は実行されない

            } else { // 成功したらセッションスコープにUserBeanインスタンスを保存しておく フィルターのために
                // このUserBeanインスタンス  が セッションスコープにあるかぎり、あればログインしてることになるから

                session.setAttribute("userBean", userBean);
                 // 成功したら、welcome画面へ行く

            }
        }



        // セッションスコープにユーザ名・パスワードを登録 ScheduleUserBeanインスタンス が セッションスコープにあるかぎり、あればログインしてることになるよ
//        ScheduleUserBean userBean = new ScheduleUserBean(scheduleUser, pass);
//        session.setAttribute("userBean", userBean);
    }

}
