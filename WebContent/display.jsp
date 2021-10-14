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
// セッションスコープを使う session は、JSPで使える暗黙オブジェクト
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
td.sche{
background-color:#fffffff;text-align:left;height:80px;
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
String css = "";
for (int i = 0 ; i < weekCount ; i++){
   if(i == 0 && calendarDay[i] > 7){
         other = true;
         css = "otherday";
       } else {
         css = "day";
       }
            for (int j = i * 7 ; j < i * 7 + 7 ; j += 7){

%>
<tr><td class=<%=css %>><%=calendarDay[j] %></td><td class=<%=css %>><%=calendarDay[j+1] %></td><td class=<%=css %>><%=calendarDay[j+2] %></td><td class=<%=css %>><%=calendarDay[j+3] %></td><td class=<%=css %>><%=calendarDay[j+4] %></td><td class=<%=css %>><%=calendarDay[j+5] %></td><td class=<%=css %>><%=calendarDay[j+6] %></td></tr>
<tr>
<%for (int k = 0 ; k < 7 ; k++){  %>
  <td class="sche"><img src="./img/IMG_1044.JPG" width="14" height="16"></td>
<% } %>
</tr>
<%
            }
        }
%>
</table>

<!--  aリンクだと、インスタンスを送りたい時には、セッションスコープへ入れないとダメ session は、JSPで使える暗黙オブジェクト
上のスクリプトレットでセッションに保存している session.setAttribute("monthBean", monthBean); -->
<a href="/Schedule/MonthDisplayServlet?mon=before">前月</a>
<a href="/Schedule/MonthDisplayServlet?mon=next">翌月</a>
</body>
</html>