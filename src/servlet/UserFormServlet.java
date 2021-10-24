package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.UserBean;

/**
 * Servlet implementation class UserFormServlet
 */
@WebServlet("/UserFormServlet")
public class UserFormServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserFormServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          // 文字化け対策  今回はフィルターを作ったので、書かなくても大丈夫だが
        request.setCharacterEncoding("UTF-8");
        HttpSession  session = request.getSession();

        // セッションスコープのチェック
        if (session == null) {
            // セッションがなかったら index.jspへ フォワード
            request.getRequestDispatcher("./").forward(request, response);
            return;
        } else {

        UserBean userBean = new UserBean(); // 空
        session.setAttribute("userBean", userBean);

        // このサーブレットでは、登録画面にフォワードするだけです
        //   フォワードする 直接HTTPのURLを打ち込んでも、アクセスされないようにするにはWEB-INF配下にする WEB-INFの直下にjspフォルダを自分で作ってその中にフォワード先のjspファイルを置く
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/user_registration.jsp");
        dispatcher.forward(request, response);
    }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     // TODO Auto-generated method stub
        doGet(request, response);
    }

}
