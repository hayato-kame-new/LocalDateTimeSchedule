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
import util.PasswordUtil;

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

        // リクエストパラメータの取得 index.jspからくる
        // 文字化け対策  今回はフィルターを作ったので、書かなくても大丈夫だが
        request.setCharacterEncoding("UTF-8");

        String mail = request.getParameter("mail"); // メールアドレス データベースではユニーク一意制約をつけてある
        String flat_password = request.getParameter("flat_password"); // 平のパスワード


        HttpSession  session = request.getSession();
        UserDao userDao = new UserDao();
        UserBean userBean = null;

        // セッションスコープのチェック 必要だこれ
        if (session == null) {
            // セッションがなかったら index.jspへ フォワード
            request.setAttribute("loginFailure", "ログインに失敗しました。もう一度入力してください。");
            request.getRequestDispatcher("./").forward(request, response);
            return;
        } else {

            userBean = userDao.loginFind(mail); // ユニークのカラムのメールアドレスで検索してきたインスタンス

            if (userBean == null) {
                request.setAttribute("loginFailure", "ログインに失敗しました。もう一度入力してください。");
                request.getRequestDispatcher("./").forward(request, response); // 以降が実行されないここでリターンされる
                return;
            } else {
                // まず、ソルトを取得しないといけないので、ユニークなパスワードから、UserBeanを取得して、主キーのidを取得する ソルトは、idを使ってますので
                String salt = String.valueOf(userBean.getId());  // ソルトを取得
                // ハッシュ化したパスワードを生成する  第2引数は、ソルトです int型のidをString型にして
                String hashed_pass = PasswordUtil.getSafetyPassword(flat_password, salt);
                if(userBean.getPass().equals(hashed_pass)) {  // パスワードが照合できたら、ログインできたとする
                    session.setAttribute("userBean", userBean);  // 無事にログインできたとして、セッションスコープに保存します
                //  ログイン成功 welcome.jspにフォワード WebContentからの ルート相対パス  初め/ を書いて
                    request.getRequestDispatcher("/WEB-INF/jsp/welcome.jsp").forward(request, response); // ここでリターン終了
                    return;
                } else { // パスワード照合できなかったら、ログインはできないので
                    request.setAttribute("loginFailure", "ログインに失敗しました。もう一度入力してください。");
                    request.getRequestDispatcher("./").forward(request, response); // 以降が実行されないここでリターンされる
                    return;
                }
            }
        }
    }
}
