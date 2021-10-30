package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ScheduleDao;
import model.MonthBean;
import model.ScheduleBean;

/**
 * Servlet implementation class MonthDisplayServlet
 */
@WebServlet("/MonthDisplayServlet")
public class MonthDisplayServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MonthDisplayServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 文字化け対策  今回はフィルターを作ったので、書かなくても大丈夫だが
        request.setCharacterEncoding("UTF-8");

        // aリンクのについてた?以降のクエリー文字列からの取り出し リクエストスコープから取り出す
        String mon = request.getParameter("mon");
        // idはwelcome画面から  ログインしたときや ユーザ新規登録した時に渡ってくる UserBeanインスタンスの id です スケジュール新規では このidを、後でScheduleBeanインスタンスの、userIdフィールドへ代入する   リダイレクトできた時は、idは null になってる
        String id = request.getParameter("id"); // UserBeanの主キー id  int型だけど、クエリー文字列としてString型で送られてくる

        ScheduleDao scheDao = new ScheduleDao();
        int year = 0;
        int month = 0;
        // その月が何日あるのか
        int thisMonthlastDay = 0;

        List<ScheduleBean> monthScheduleList = new ArrayList<ScheduleBean>(); // まずnewして確保  そのユーザのその月にあるスケジュール一覧リスト
        MonthBean monthBean = null;

        HttpSession session = request.getSession(true); // 引数のセッション生成フラグにtrueを指定すると、現在セッションが存在しない場合は、生成して返します 注意nullを代入しちゃだめ　なくなってしまうから
        if (session.getAttribute("mon") != null) {
            mon = (String) session.getAttribute("mon");
            // 取り出したら、すぐに消しておく 何かのエラーで途中でプログラムが止まった時に、前のセッションが残ってしまうので取り出して、その後すぐにセッションから消すことが大切
            session.removeAttribute("mon");
        }
        int userId = 0; // 新規との時は使わない
        String msg = "";
        switch (mon) {
        case "current":
            // welcome画面から今月を表示する 今月は、aリンクでアクセスしてくる
            // welcome画面からのaリンクのクエリー文字列からそのUserBeanインスタンスのidフィールドの値 を取得してきてScheduleBeanインスタンスのuserIdの値にセットする
            // welcomeからきた時 ログインしてきた時 ユーザ新規の時には idがある
            userId = Integer.parseInt(id); // ここでjava.lang.NumberFormatException: null

            // 今月を表示するために新しくインスタンスを生成する 今月は、引数なしのコンストラクタを呼ぶ
            // 引数なしのコンストラクタは、現在の日時を基準にしてフィールドを初期化するようにしてある
            monthBean = new MonthBean(); // newして現在日付の月のインスタンス生成
            year = monthBean.getYear();
            month = monthBean.getMonth();
            thisMonthlastDay = monthBean.getThisMonthlastDay(); // // その月が何日あるのか

            // そのユーザのその月にあるスケジュール全てをリストにして取得 新規では第1引数は、ユーザのidを取得したので、それを入れる
            monthScheduleList = scheDao.getMonthScheduleList(userId, year, month, thisMonthlastDay);
            break; // switch文抜ける

        case "before":
            // 前の月を表示する
            // aリンクの 前の月を表示 からくるが、インスタンスは、aリンクのクエリー文字列では送れないため、
            // display.jspでは session.setAttribute("monthBean", monthBean); というふうにセッションスコープにに保存しておいてるのでセッションスコープから取得する
            monthBean = (MonthBean) session.getAttribute("monthBean");
            // 取り出したら、消しておくセッションから 消すこと SpringBootでも
            session.removeAttribute("monthBean");
            userId = Integer.parseInt(request.getParameter("userId")); // 先月へのaリンクのクエリー文字列から取得

            // 1つ前の月の初日を生成してる   セッションから取得したmonthBeanインスタンスを利用してます
            LocalDate beforeLocalDate = LocalDate.of(monthBean.getYear(), monthBean.getMonth(), 1).minusMonths(1);
            // monthBeanを上書きする 新しくインスタンスを生成して上書きする 引数ありのコンストラクタをよぶ １ヶ月前に変更したlocaldateインスタンスを実引数にする
            monthBean = new MonthBean(beforeLocalDate); // 1つ前の月の初日からmonthBeanを生成して上書きしてます
            year = monthBean.getYear();
            month = monthBean.getMonth();
            thisMonthlastDay = monthBean.getThisMonthlastDay(); // その月が何日あるのか
            monthScheduleList = scheDao.getMonthScheduleList(userId, year, month, thisMonthlastDay); // そのユーザのその月のスケジュール全部
            break; // switch文抜ける
        case "next":
            // 次の月を表示する
            // aリンクの 前の月を表示 からくるが、インスタンスは、aリンクのクエリー文字列では送れないため、
            // display.jspでは session.setAttribute("monthBean", monthBean); というふうにセッションスコープにに保存しておいてるのでセッションスコープから取得する
            monthBean = (MonthBean) session.getAttribute("monthBean");
            // 取り出したら、消しておくセッションから
            session.removeAttribute("monthBean");
            userId = Integer.parseInt(request.getParameter("userId")); // 次の月へのaリンクのクエリー文字列から取得
            // 1つ後ろの月の初日を生成してる   セッションから取得したmonthBeanインスタンスを利用してます
            LocalDate nextLocalDate = LocalDate.of(monthBean.getYear(), monthBean.getMonth(), 1).plusMonths(1);
            // monthBeanを上書きする 新しくインスタンスを生成して上書きする 引数ありのコンストラクタをよぶ １ヶ月後に変更したlocaldateインスタンスを実引数にする
            monthBean = new MonthBean(nextLocalDate); // 1つ後ろの月の初日からmonthBeanを生成して上書きしてます
            year = monthBean.getYear();
            month = monthBean.getMonth();
            // その月が何日あるのか
            thisMonthlastDay = monthBean.getThisMonthlastDay(); // その月が何日あるのか

            // そのユーザのその月にあるスケジュール全てをリストにして取得
            monthScheduleList = scheDao.getMonthScheduleList(userId, year, month, thisMonthlastDay);
            break; // switch文抜ける
        case "return_current": // scheBean mon msg
            // 今月の表示に戻る  display.jspのaリンクの 今月の表示に戻る から、きた時 クエリー文字列から userIdが取得できる
            // インスタンスは、aリンクのクエリー文字列では送れないため、 display.jspでは session.setAttribute("monthBean", monthBean); というふうにセッションスコープにに保存しておいてるのでセッションスコープから取得する
            monthBean = (MonthBean) session.getAttribute("monthBean");
            // 取り出したら、消しておくセッションから
            session.removeAttribute("monthBean");
            userId = Integer.parseInt(request.getParameter("userId")); // aリンクのクエリー文字列から取得
            // 今月を表示するために新しくインスタンスを生成する 今月は、引数なしのコンストラクタを呼ぶ
            // monthBeanを現在の日時にして上書きする 引数なしのコンストラクタは、現在の日時を基準にしてフィールドを初期化するようにしてある
            monthBean = new MonthBean(); // newして現在日付の月のインスタンス生成 下でリクエストスコープに保存する
            year = monthBean.getYear();
            month = monthBean.getMonth();
            thisMonthlastDay = monthBean.getThisMonthlastDay(); // // その月が何日あるのか

            // そのユーザのその月にあるスケジュール全てをリストにして取得 新規では第1引数は、ユーザのidを取得したので、それを入れる
            monthScheduleList = scheDao.getMonthScheduleList(userId, year, month, thisMonthlastDay);
            break; // switch文抜ける

        case "scheduleResult": // リダイレクト後
            // スケージュール登録や編集 削除後にリダイレクトしてきたら、ScheduleInsertServletから リダイレクトしてくるので、登録や編集をした月を表示する
            // ScheduleInsertServletで、セッションスコープに保存したので セッションスコープから取得する
            ScheduleBean scheBean = (ScheduleBean) session.getAttribute("scheBean"); // 新規登録のあとnullでエラー

            userId = scheBean.getUserId(); // これが必要
            msg = (String) session.getAttribute("msg");

            // 取り出したら、消しておくセッションから
            session.removeAttribute("scheBean");
            session.removeAttribute("msg");

            // 削除した年月日を取得してる ScheduleBeanの 削除をした後に、再度表示する月は削除した月なので
            LocalDate resultLocalDate = LocalDate.of(scheBean.getScheduleDate().getYear(),
                    scheBean.getScheduleDate().getMonthValue(), scheBean.getScheduleDate().getDayOfMonth());
            // 新しいインスタンスを生成する 下でリクエストスコープに保存してる
            monthBean = new MonthBean(resultLocalDate);

            year = monthBean.getYear();
            month = monthBean.getMonth();
            thisMonthlastDay = monthBean.getThisMonthlastDay(); // その月が何日あるのか

            // そのユーザのその月にあるスケジュール全てをリストにして取得
            monthScheduleList = scheDao.getMonthScheduleList(scheBean.getUserId(), year, month, thisMonthlastDay);
            break; // switch文を抜ける
        }

        // リクエストスコープに保存する。リクエストスコープは、フォワードできる(リダイレクトはできない)
        // リクエストスコープに保存できるのは、参照型 クラス型のインスタンスだけ。自分で作ったクラスは、JavaBeansのクラスにすること
        request.setAttribute("monthBean", monthBean);
        request.setAttribute("mon", mon);
        // 必要です！！！
        request.setAttribute("userId", userId); // ユーザの id 必要
        request.setAttribute("monthScheduleList", monthScheduleList);

        // スケジュール登録した後にリダイレクトしてくる時のメッセージを表示するため
        request.setAttribute("msg", msg);

        //   フォワードする 直接HTTPのURLを打ち込んでも、アクセスされないようにするにはWEB-INF配下にする
        // WEB-INFの直下にjspフォルダを自分で作ってその中にフォワード先のjspファイルを置く このパスはルート相対ぱす /  から始めてる WebContentからの ルート相対パスです (相対パス ではない)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/display.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
