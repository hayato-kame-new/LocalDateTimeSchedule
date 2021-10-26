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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 文字化け対策  今回はフィルターを作ったので、書かなくても大丈夫だが
        request.setCharacterEncoding("UTF-8");

        // aリンクのについてた?以降のクエリー文字列からの取り出し リクエストスコープから取り出す
        String mon = request.getParameter("mon");
        // idは新規の時に使う必要 新規では このidを、後でScheduleBeanインスタンスの、userIdフィールドへ代入する   リダイレクトできた時は、idは null になってる
        String id = request.getParameter("id"); // UserBeanの主キー id  int型だけど、クエリー文字列としてString型で送られてくる

           ScheduleDao scheDao = new ScheduleDao();
           int year = 0;
           int month = 0;
           // その月が何日あるのか
           int thisMonthlastDay = 0;

           List<ScheduleBean> monthScheduleList = new ArrayList<ScheduleBean>(); // まずnewして確保  そのユーザのその月にあるスケジュール一覧リスト
           MonthBean monthBean = null;
            ScheduleBean scheBean = null;
           HttpSession session = request.getSession(true);  // 引数のセッション生成フラグにtrueを指定すると、現在セッションが存在しない場合は、生成して返します 注意nullを代入しちゃだめ　なくなってしまうから
           int userId = 0;
           String msg = "";
        switch(mon) {
        case "current":  //　今月は、aリンクでアクセスしてきたり、リダイレクトできたりする
            // 新規の時は、aリンクのクエリー文字列からそのユーザのid を取得してきて　ScheduleBeanインスタンスにセットする
             userId = Integer.parseInt(id);  // ここでjava.lang.NumberFormatException: null

            // 今月を表示するために新しくインスタンスを生成する 今月は、引数なしのコンストラクタを呼ぶ
            // 引数なしのコンストラクタは、現在の日時を基準にしてフィールドを初期化するようにしてある
            monthBean = new MonthBean();  // newして現在日付の月のインスタンス生成
             year = monthBean.getYear();
            month = monthBean.getMonth();
             thisMonthlastDay = monthBean.getThisMonthlastDay(); // // その月が何日あるのか

            // そのユーザのその月にあるスケジュール全てをリストにして取得 新規では第1引数は、ユーザのidを取得したので、それを入れる
            monthScheduleList = scheDao.getMonthScheduleList(userId , year,  month , thisMonthlastDay);
            break; // switch文抜ける

            // リダイレクト後の時は MonthBeanのBeanは、セッションスコープから取り出す、次の次の次の月など使うため  スケジュール登録してリダイレクトしてきたあともセッションスコープから取得する
        case "before":   // リダイレクトでくる インスタンスは、セッションに保存しておいてるのでセッションスコープから取得する
             monthBean = (MonthBean) session.getAttribute("monthBean");  // リダイレクトしてきた時に必要だから取得する
          // 取り出したら、消しておくセッションから
            session.removeAttribute("monthBean");
          userId = Integer.parseInt(request.getParameter("userId")); // 先月へのaリンクのクエリー文字列から取得
 
            // 1つ前の月の初日を生成してる   セッションから取得したmonthBeanインスタンスを利用してます
            LocalDate beforeLocalDate = LocalDate.of(monthBean.getYear(), monthBean.getMonth(), 1).minusMonths(1);
            // 上書きする 新しくインスタンスを生成して上書きする 引数ありのコンストラクタをよぶ １ヶ月前に変更したlocaldateインスタンスを実引数にする
            monthBean = new MonthBean(beforeLocalDate);  // 1つ前の月の初日からmonthBeanを生成して上書きしてます
            year = monthBean.getYear();
            month = monthBean.getMonth();
             thisMonthlastDay = monthBean.getThisMonthlastDay(); // その月が何日あるのか
              monthScheduleList = scheDao.getMonthScheduleList(userId , year,  month , thisMonthlastDay); // そのユーザのその月のスケジュール全部
            break; // switch文抜ける
        case "next":
              //  MonthBeanのBeanは、セッションスコープから取り出す、次の次の次の月など使うため  スケジュール登録してリダイレクトしてきたあともセッションスコープから取得する
         //   session = request.getSession(true);  // 引数のセッション生成フラグにtrueを指定すると、現在セッションが存在しない場合は、生成して返します
            monthBean = (MonthBean) session.getAttribute("monthBean");  // リダイレクトしてきた時に必要だから取得する
            session.removeAttribute("monthBean");  // 取り出したら、消しておくセッションから
            // スケジュール新規登録したあとリダイレクトしてくる時や、更新した後にリダイレクトしてきた セッションスコープから取得する
             scheBean = (ScheduleBean) session.getAttribute("scheBean") ;
            if(scheBean != null ) {  // 一番最初にアクセスしてきた時は このScheduleBeanインスタンスは nullだから  リダイレクトしてきた時は セッションスコープに保存してあるので
                mon = (String) session.getAttribute("mon");  // "scheduleResult"が入ってる
                msg = (String) session.getAttribute("msg");
                // リダイレクトしてきた時は、セッションから そのユーザのidを取得する

                //　ここでセッションからscheBeanというキーの値を削除してます!!!
                session.removeAttribute("scheBean");  // 取り出したら、消しておくセッションから
            }

            // useId = Integer.parseInt(request.getParameter("getUserId")); // 次の月へのリンクのクエリー文字列を取得
            // インスタンスは、セッションに保存しておいてるのでセッションスコープから取得する
             // セッションから取得したBeanインスタンスを利用する １ヶ月後にする
            LocalDate nextLocalDate = LocalDate.of(monthBean.getYear(), monthBean.getMonth(), 1).plusMonths(1);
            // 新しくインスタンスを生成する引数ありのコンストラクタをよぶ １ヶ月後に変更したlocaldateインスタンスを実引数にする
            monthBean = new MonthBean(nextLocalDate);

            year = monthBean.getYear();
            month = monthBean.getMonth();
            // その月が何日あるのか
             thisMonthlastDay = monthBean.getThisMonthlastDay();

         // そのユーザのその月にあるスケジュール全てをリストにして取得
            monthScheduleList = scheDao.getMonthScheduleList(scheBean.getUserId() , year,  month , thisMonthlastDay);
            break; // switch文抜ける
        case "scheduleResult":
              //  MonthBeanのBeanは、セッションスコープから取り出す、次の次の次の月など使うため  スケジュール登録してリダイレクトしてきたあともセッションスコープから取得する
        //     session = request.getSession(true);  // 引数のセッション生成フラグにtrueを指定すると、現在セッションが存在しない場合は、生成して返します
             monthBean = (MonthBean) session.getAttribute("monthBean");  // リダイレクトしてきた時に必要だから取得する
            session.removeAttribute("monthBean");  // 取り出したら、消しておくセッションから
            // スケジュール新規登録したあとリダイレクトしてくる時や、更新した後にリダイレクトしてきた セッションスコープから取得する
             scheBean = (ScheduleBean) session.getAttribute("scheBean") ;
            if(scheBean != null ) {  // 一番最初にアクセスしてきた時は このScheduleBeanインスタンスは nullだから  リダイレクトしてきた時は セッションスコープに保存してあるので
                mon = (String) session.getAttribute("mon");  // "scheduleResult"が入ってる
                msg = (String) session.getAttribute("msg");
                // リダイレクトしてきた時は、セッションから そのユーザのidを取得する

                //　ここでセッションからscheBeanというキーの値を削除してます!!!
                session.removeAttribute("scheBean");  // 取り出したら、消しておくセッションから
            }

            // getUserId = Integer.parseInt(request.getParameter("getUserId")); // 先月へのリンクのクエリー文字列を取得
            // スケジュールの新規登録や更新をした後に、再度表示する 表示する月は、新規登録や更新した月を表示するようにしてる
            LocalDate scheduleResultLocalDate = LocalDate.of(scheBean.getScheduleDate().getYear(), scheBean.getScheduleDate().getMonthValue(), scheBean.getScheduleDate().getDayOfMonth());
            // 新しいインスタンスを生成する
            monthBean = new MonthBean(scheduleResultLocalDate);

            year = monthBean.getYear();
            month = monthBean.getMonth();
            // その月が何日あるのか
             thisMonthlastDay = monthBean.getThisMonthlastDay();

         // そのユーザのその月にあるスケジュール全てをリストにして取得
            monthScheduleList = scheDao.getMonthScheduleList(scheBean.getUserId() , year,  month , thisMonthlastDay);

            break;
        }

     // リクエストスコープに保存する。リクエストスコープは、フォワードできる(リダイレクトはできない)
        // リクエストスコープに保存できるのは、参照型 クラス型のインスタンスだけ。自分で作ったクラスは、JavaBeansのクラスにすること
        request.setAttribute("monthBean", monthBean);
        request.setAttribute("mon", mon);
        // 必要です！！！
     request.setAttribute("userId", userId);  // ユーザの id 必要

        // dayScheduleListも保存
        request.setAttribute("monthScheduleList", monthScheduleList);


        // スケジュール登録した後にリダイレクトしてくる時のメッセージを表示するため
        request.setAttribute("msg", msg);

        //   フォワードする
   //   フォワードする 直接HTTPのURLを打ち込んでも、アクセスされないようにするにはWEB-INF配下にする
        // WEB-INFの直下にjspフォルダを自分で作ってその中にフォワード先のjspファイルを置く このパスはルート相対ぱす /  から始めてる WebContentからの ルート相対パスです (相対パス ではない)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/display.jsp");
        dispatcher.forward(request, response);


    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
