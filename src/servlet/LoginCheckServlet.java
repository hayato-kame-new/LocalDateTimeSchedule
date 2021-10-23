package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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



         // ここでは書かないかも
//         HttpSession session = request.getSession(true);
//
//            /* 認証失敗から呼び出されたのかどうか */
//            Object status = session.getAttribute("status");
//
//            String msg = "";
//            if (status != null){
//                msg = "認証に失敗しました 再度ユーザー名とパスワードを入力して下さい";
//                session.setAttribute("status", null);
//            }


    }

}
