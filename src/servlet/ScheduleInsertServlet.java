package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ScheduleInsertServlet
 */
@WebServlet("/ScheduleInsertServlet")
public class ScheduleInsertServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScheduleInsertServlet() {
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

        // 文字化け対策
        request.setCharacterEncoding("UTF-8");

        // フォームからの送信で送られてきたのを取得する
        int year = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month"));
        int day = Integer.parseInt(request.getParameter("day"));
        int s_hour = Integer.parseInt(request.getParameter("s_hour"));
        int s_minute = Integer.parseInt(request.getParameter("s_minute"));
        int e_hour = Integer.parseInt(request.getParameter("e_hour"));
        int e_minute = Integer.parseInt(request.getParameter("e_minute"));
        String schedule = request.getParameter("schedule");
        String scheduleMemo = request.getParameter("scheduleMemo");

        LocalDate scheduleDate = LocalDate.of(year, month, month);
        LocalTime startTime = LocalTime.of(s_hour, s_minute);
        LocalTime endTime = LocalTime.of(e_hour, e_minute);


        // まだid userId を用意してないから
        // 引数ありのコンストラクタをよぶ
        // ScheduleBean scheBean = new ScheduleBean(id, userId, scheduleDate, startTime, endTime, schedule, scheduleMemo);


    }

}
