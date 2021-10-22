<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page
  import="model.MonthBean, java.util.*, java.time.LocalDateTime , model.ScheduleBean"%>
<%
// 文字化け対策
request.setCharacterEncoding("UTF-8");
// リクエストスコープから取り出す
MonthBean monthBean = (MonthBean) request.getAttribute("monthBean");
String mon = (String) request.getAttribute("mon");
String msg = (String) request.getAttribute("msg");

int weekCount = monthBean.getWeekCount();
int[] calendarDay = monthBean.getCalendarDay();
int year = monthBean.getYear();
int month = monthBean.getMonth();
// セッションに保存しないとだめ、リクエストスコープでは、aリンク越しに渡せないので 先月 翌月 のために
// 翌月の翌月も表示させるために 表示してるインスタンスを送る セッションスコープを使う session は、JSPで使える暗黙オブジェクト セッションは、後で明示的に消すことが大事残ってるから
session.setAttribute("monthBean", monthBean);


// monthScheduleListもリクエストスコープから取り出す そのユーザのその表示したい月のスケジュールの全件が入ったリストを MonthDisplayServletでリクエストスコープに保存して送ってきてる
List<ScheduleBean> monthScheduleList = new ArrayList<ScheduleBean>();
monthScheduleList = (List<ScheduleBean>) request.getAttribute("monthScheduleList");

// 今現在の日時
LocalDateTime now = LocalDateTime.now();
int now_year = now.getYear();
int now_month = now.getMonthValue();
int now_day = now.getDayOfMonth();
int now_hour = now.getHour();
int now_minute = now.getMinute();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%=year%>年<%=month%>月</title>
<style>
table {
  border: 1px solid #a9a9a9;
  width: 90%;
  padding: 0px;
  margin: 0px;
  border-collapse: collapse;

}

td {
  width: 12%;
  border-top: 1px solid #a9a9a9;
  border-left: 1px solid #a9a9a9;
  vertical-align: top;
  margin: 0px;
  padding: 2px;
  overflow: scroll;
}

td.week {
  background-color: #f0f8ff;
  text-align: center;
}

td.day {
  background-color: #f5f5f5;
  text-align: right;
  font-size: 0.75em;
}

td.otherday {
  background-color: #f5f5f5;
  color: #d3d3d3;
  text-align: right;
  font-size: 0.75em;
}

td.stamp {
  background-color: #fffffff;
  text-align: left;
  height: 17px;
}

td.sche {
  background-color: #fffffff;
  text-align: left;
  height: 80px;
  border-top: none;
  /* 追加 */
 line-height: 1.1;
}

.current_day {
  border-radius: 50%;
}

p {
  font-size: 0.75em;
}
ul {padding: 0; margin: 0;}
li {
  list-style:none;
}
.msg {
  color: orange; font-weight:bold;
}
span {color: #333; font-size: 80%;}
.schedule {
  font-weight:bold;
}
.schedule span {
  color: darkgreen;
}
.count {
  font-size: 70%;
  color: font-style:bold;
}
.fontAwesome a {
  font-size: 120%;
}
</style>
</head>
<body>
  <!--  aリンクだと、インスタンスを送りたい時には、セッションスコープへ入れないとダメ session は、JSPで使える暗黙オブジェクト
上のスクリプトレットでセッションに保存している session.setAttribute("monthBean", monthBean); -->
  <a href="/LocalDateTimeSchedule/MonthDisplayServlet?mon=before">&lang;&lang;
    前月表示</a>
  <small>&emsp;</small>
  <a href="/LocalDateTimeSchedule/MonthDisplayServlet?mon=next">翌月表示
    &rang;&rang;</a>
  <br />
  <%
  if (!mon.equals("current")) {
  %>
  <a href="/LocalDateTimeSchedule/MonthDisplayServlet?mon=current">今月の表示に戻る</a>
  <%
  }
  %>
  <hr />

  <h3><%=year%>年<%=month%>月のカレンダー
  </h3>
  <%
  if (msg != null) {
  %>
  <p class="msg">
    <%=msg%>
  </p>
  <%
  }
  %>
  <p>
    現在は<%=now_year%>年<%=now_month%>月<%=now_day%>日<%=now_hour%>時<%=now_minute%>分です
  </p>

  <table>
    <tr>
      <td class="week">日</td>
      <td class="week">月</td>
      <td class="week">火</td>
      <td class="week">水</td>
      <td class="week">木</td>
      <td class="week">金</td>
      <td class="week">土</td>
    </tr>

    <%
    // 今月のものだけを表示したい
    String cssDisplay = "day";

    for (int i = 0; i < weekCount; i++) {
      for (int j = i * 7; j < i * 7 + 7; j += 7) { // ２重ループ
    %>

    <tr class="fontAwesome">
      <%
      for (int k = 0; k < 7; k++) {
        // 今月分だけ表示するものと、区別してる タイムスケジュールのリンクや画像リンクは、今月のだけに表示する 日付は今月だけ黒にする
        if (i == 0 && calendarDay[j + k] > 7) {
          cssDisplay = "otherday";
        } else if (i == weekCount - 1 && calendarDay[j + k] < 22) {
          cssDisplay = "otherday";
        } else {
          cssDisplay = "day";
        }
      %>
      <td class=<%=cssDisplay%>><%=calendarDay[j + k]%> <%
 if (now_year == year && now_month == month && now_day == calendarDay[j + k]) {
 %>
        <img class="current_day" src="./img/IMG_1044.JPG" width="14" height="14"
        ><br /> <%
 }
 %> <%
 if (cssDisplay.equals("day")) {
 %>
   <!-- スケジュールを新規に作成するためのリンク ログインしてきたユーザID userId も必要とりあえず、1で送る userId=1
         注意 主キーのidではない -->
   <a
        href="/LocalDateTimeSchedule/NewScheduleServlet?action=add&userId=1&year=<%=year%>&month=<%=month%>&day=<%=calendarDay[j + k]%>"><i
          class="fas fa-clipboard-list"></i></a> <%
 }
 %></td>
      <%
      }
      %>
    </tr>


    <tr>
      <%
      for (int k = 0; k < 7; k++) {
        // 今月分だけ表示するものと、区別してる タイムスケジュールのリンクや画像リンクは、今月のだけに表示する 日付は今月だけ黒にする
        if (i == 0 && calendarDay[j + k] > 7) {
          cssDisplay = "otherday";
        } else if (i == weekCount - 1 && calendarDay[j + k] < 22) {
          cssDisplay = "otherday";
        } else {
          cssDisplay = "day";
        }
      %>
      <td class="sche">
        <%
        if (cssDisplay.equals("day") && monthScheduleList.size() != 0) {
        %>

        <ul class=<%=cssDisplay%> >
          <%
          //  ulタグの cssDisplayのCSSのクラス属性によって、当月分だけ表示できるようになってる
         int count = 0;
          for (ScheduleBean scheBean : monthScheduleList) {
            if (scheBean.getScheduleDate().getDayOfMonth() == calendarDay[j + k]) {
              int id = scheBean.getId();  // 主キープライマリーキーが編集に必要
              String schedule = scheBean.getSchedule();
             count++;
              if(schedule.length() >= 7 ) {
                schedule = schedule.substring(0,8) + "...";
              }
              String scheduleMemo = scheBean.getScheduleMemo();
              if(scheduleMemo.length() >= 7 ) {
                scheduleMemo = scheduleMemo.substring(0,8) + "...";
              }
          %>
          <!-- 編集画面表示のためのリンクになってる 主キーの id を送る  注意 userId ではない 主キーは、ユニークだから主キーで検索すること  -->
          <li >
            <a href="/LocalDateTimeSchedule/NewScheduleServlet?action=edit&id=<%=id %>">
              <small class="schedule">[<%=scheBean.createStrStartTime() %>-<%=scheBean.createStrEndTime() %>]&nbsp;<span><%=schedule%></span>:&nbsp;</small>
            </a>
            <small><span><%=scheduleMemo%></span></small>
          </li>

          <%
          }
          }
          %>
        </ul>
        <%if(count != 0) { %>
         <span class="count">&lang;<%= count%>件です&rang;</span>
        <%
        }
     }
     %>
      </td>
      <%
      }
      }
      }
      %>
    </tr>

  </table>

  <!-- Font Awesomeのための -->
  <script defer
    src="https://use.fontawesome.com/releases/v5.7.2/js/all.js"></script>
</body>
</html>