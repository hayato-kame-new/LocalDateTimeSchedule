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
        // セッションを作って、その中にUserBeanインスタンスを置かないと、フィルターに引っかかって、index.jspへ戻されてしまうので セッションを作る
        HttpSession  session = request.getSession();  // 引数なしは 引数がtrueと同じこと

        // セッションスコープのチェック
        if (session == null) {  // 必要
            // セッションがなかったら index.jspへ フォワード
            request.getRequestDispatcher("./").forward(request, response);
            return;
        } else {
            // 空の(フィールドが規定値のままの)userBeanをセッションに置く これがセッションスコープにない nullだと、フィルターが効くので index.jspへ転送されてしまう
        UserBean userBean = new UserBean(); // 空のインスタンス生成(各フィールドの値は、各データ型の既定値になっています)にしておけばいい nullじゃなければいいので nullだと、フィルターの作用でindex.jspへ転送されてしまう
        // これ必要かなあ？？？要らないかも いや、ないと、フォワードできない？？
        // いや、セッションじゃなくて、リクエストスコープにおけばいいのでは？？？
       // request.setAttribute("userBean", userBean);
        session.setAttribute("userBean", userBean);

        // このサーブレットでは、登録画面にフォワードするだけです
        //   フォワードする 直接HTTPのURLを打ち込んでも、アクセスされないようにするにはWEB-INF配下にする WEB-INFの直下にjspフォルダを自分で作ってその中にフォワード先のjspファイルを置く
        // 現在は、フィルターをかけてるので、直接リクエストはされませんが
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/user_form.jsp");
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
