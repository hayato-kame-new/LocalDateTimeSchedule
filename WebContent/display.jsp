<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.MonthBean, java.util.*"%>
 <%
    // 文字化け対策
   request.setCharacterEncoding("UTF-8");
 // リクエストスコープから取り出す
 MonthBean monthBean = (MonthBean)request.getAttribute("monthBean");

int weekCount = monthBean.getWeekCount();
int[] calendarDay = monthBean.getCalendarDay();

// セッションに保存しないとだめ、リクエストスコープでは、aリンク越しに渡せないので 先月 翌月 のために
// セッションスコープを使う session は、JSPで使える暗黙オブジェクト セッションは、後で明示的に消すことが大事残ってるから
session.setAttribute("monthBean", monthBean);


int year = monthBean.getYear();
int month = monthBean.getMonth();


 %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
table{
border:1px solid #a9a9a9;width:90%;padding:0px;margin:0px;border-collapse:collapse;
}
td{
width:12%;border-top:1px solid #a9a9a9;border-left:1px solid #a9a9a9;vertical-align:top;margin:0px;padding:2px;
}
td.week{
background-color:#f0f8ff;text-align:center;
}
td.day{
background-color:#f5f5f5;text-align:right;font-size:0.75em;
}
td.otherday{
background-color:#f5f5f5;color:#d3d3d3;text-align:right;font-size:0.75em;
}
td.stamp{
background-color:#fffffff;text-align:left;height:17px;
}
td.sche{
background-color:#fffffff;text-align:left;height:80px; border-top:none;
}
img{border:0px;}
p{font-size:0.75em;}
</style>
</head>
<body>

<h3><%=year %>年<%=month %>月のカレンダー</h3>
<table>
<tr><td class="week">日</td><td class="week">月</td><td class="week">火</td><td class="week">水</td><td class="week">木</td><td class="week">金</td><td class="week">土</td></tr>
<%
boolean other = false;  // 他の月かどうか
String css = "day"; // 後で条件分岐させて、表示をかえる
String time = "";
for (int i = 0 ; i < weekCount ; i++){
    for (int j = i * 7 ; j < i * 7 + 7 ; j += 7){

%>


<tr>
<%for (int k = 0 ; k < 7 ; k++){

 if (i == 0 && calendarDay[j+k] > 7 ) {
        css = "otherday";
      }else if(i == weekCount - 1 && calendarDay[j+k] < 22) {
        css = "otherday";
      }else {
        css = "day";
      }
 %>
   <td class=<%=css %>>
    <%=calendarDay[j + k] %><img src="./img/IMG.JPG" width="14" height="14"><br />
    <a href="/LocalDateTimeSchedule/NewScheduleServlet"><i class="fas fa-clipboard-list"></i></a>
  </td>
<% } %>
</tr>



<%-- <tr>
  <td class=<%=css %>>
    <%=calendarDay[j] %><img src="./img/IMG.JPG" width="14" height="14"><br />
    <a href="/LocalDateTimeSchedule/NewScheduleServlet"><i class="fas fa-clipboard-list"></i></a>
  </td>
  <td class=<%=css %>>
    <%=calendarDay[j+1] %><img src="./img/IMG.JPG" width="14" height="14"><br />
    <i class="fas fa-clipboard-list"></i>
  </td>
  <td class=<%=css %>>
    <%=calendarDay[j+2] %><img src="./img/IMG.JPG" width="14" height="14"><br />
    <i class="fas fa-clipboard-list"></i>
  </td>
  <td class=<%=css %>>
    <%=calendarDay[j+3] %><img src="./img/IMG.JPG" width="14" height="14"><br />
    <i class="fas fa-clipboard-list"></i>
  </td>
  <td class=<%=css %>>
    <%=calendarDay[j+4] %><img src="./img/IMG.JPG" width="14" height="14"><br />
    <i class="fas fa-clipboard-list"></i>
  </td>
  <td class=<%=css %>>
    <%=calendarDay[j+5] %><img src="./img/IMG.JPG" width="14" height="14"><br />
    <i class="fas fa-clipboard-list"></i>
  </td>
  <td class=<%=css %>>
    <%=calendarDay[j+6] %><img src="./img/IMG.JPG" width="14" height="14"><br />
    <i class="fas fa-clipboard-list"></i>
  </td>
</tr> --%>

<tr>
  <td class="sche"></td><td class="sche"></td><td class="sche"></td><td class="sche"></td><td class="sche"></td><td class="sche"></td><td class="sche"></td>
</tr>
<%
            }
        }
%>
</table>

<!--  aリンクだと、インスタンスを送りたい時には、セッションスコープへ入れないとダメ session は、JSPで使える暗黙オブジェクト
上のスクリプトレットでセッションに保存している session.setAttribute("monthBean", monthBean); -->
<a href="/LocalDateTimeSchedule/MonthDisplayServlet?mon=before">前月</a>
<a href="/LocalDateTimeSchedule/MonthDisplayServlet?mon=next">翌月</a>
<!-- Font Awesomeのための -->
<script defer src="https://use.fontawesome.com/releases/v5.7.2/js/all.js"></script>
</body>
</html>