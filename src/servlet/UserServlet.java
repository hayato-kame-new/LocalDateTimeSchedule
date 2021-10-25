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
        String flat_password = request.getParameter("flat_password"); // 平のパスワード
        int roll = Integer.parseInt(request.getParameter("roll"));  // 0 か 1     1 は管理者
        String mail = request.getParameter("mail");
            //新規に登録して登録が成功したら、セッションスコープに UserBeanインスタンスを保存して上書きおきます。空のインスタンスがあるので上書きする

        UserDao userDao = new UserDao();
            // UserBeanをコンストラクタを呼んで生成しますが、パスワードには、平のパスワードを渡さないで、ハッシュ化されたものを渡す
            // 第2引数は、idカラムを利用する int型の数値ををString型にしたものを引数にする
            // 主キーの idカラムは、自動採番(オートインクリメント)にしてるので、データベースから、保存してある、一番最後のidの値に +1 したものを文字列にしたものを引数に渡す
            int getNewId = userDao.getNewId();
            if(getNewId == 0) {  // 失敗
                // 失敗のメッセージかな そしてユーザ登録画面へ戻るかな
                // フォワードして
                // リターンを書く return で、即終了させる　この行以降は実行されない
            } else {  // 成功
                String pass = PasswordUtil.getSafetyPassword(flat_password, Integer.toString(getNewId));
                // id 以外の引数をもつコンストラクタをよぶ idは、INSERTで値を入れなくとも、自動で採番される列なので要らない
                UserBean userBean = new UserBean(scheduleUser, pass, roll, mail);
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
                    HttpSession  session = request.getSession();
                    session.setAttribute("userBean", userBean);

                    // 新規登録成功 welcome.jspにフォワード
                    request.getRequestDispatcher("/WEB-INF/jsp/welcome.jsp").forward(request, response);
                }
            }
        }
        // セッションスコープにユーザ名・パスワードを登録 ScheduleUserBeanインスタンス が セッションスコープにあるかぎり、あればログインしてることになるよ
//        ScheduleUserBean userBean = new ScheduleUserBean(scheduleUser, pass);
//        session.setAttribute("userBean", userBean);
    }

