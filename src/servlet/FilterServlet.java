package servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class FilterServlet
 */
@WebServlet("/FilterServlet")
public class FilterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FilterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         // リクエストパラメータを取得
         request.setCharacterEncoding("UTF-8");  // 文字化け防止
        String scheduleUser = request.getParameter("scheduleUser");
        String pass = request.getParameter("pass");

        // セッションスコープを取得
        HttpSession session = request.getSession();

        // セッションスコープのチェック
        if (session == null) {
          // フォワード
          request.getRequestDispatcher("./").forward(request, response);
        } else {
          // セッションスコープにユーザ名・パスワードを登録
          Map<String, String> userMap = new HashMap<String, String>();
          userMap.put("scheduleUser", scheduleUser);
          userMap.put("pass", pass);
          session.setAttribute("userMap", userMap);

          // フォワード
          request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
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
