<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.DayBean" %>

<%
// 文字化け対策
request.setCharacterEncoding("UTF-8");
// リクエストスコープから取り出す
DayBean dayBean = (DayBean)request.getAttribute("dayBean");
int year = dayBean.getYear();
int month = dayBean.getMonth();
int day = dayBean.getDay();
int thisMonthlastDay =  dayBean.getThisMonthlastDay();

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>タイムスケジュール登録</title>

<style>
table.sche{border:1px solid #a9a9a9;padding:0px;margin:0px;border-collapse:collapse;}
td{vertical-align:top;margin:0px;padding:2px;font-size:0.75em;height:20px;}
td.top{border-bottom:1px solid #a9a9a9;text-align:center;}
td.time{background-color:#f0f8ff;text-align:right;border-right:1px double #a9a9a9;padding-right:5px;}
td.timeb{background-color:#f0f8ff;border-bottom:1px solid #a9a9a9;border-right:1px double #a9a9a9;}
td.contents{background-color:#ffffff;border-bottom:1px dotted #a9a9a9;}
td.contentsb{background-color:#ffffff;border-bottom:1px solid #a9a9a9;}
td.ex{background-color:#ffebcd;border:1px solid #8b0000;}
img{border:0px;}
p{font-size:0.75em;}

#contents{margin:0;padding:0;width:710px;}
#left{margin:0;padding:0;float:left;width:400px;}
#right{margin:0;padding:0;float:right;width:300px;background-color:#ffffff;}
#contents:after{content:".";display:block;height:0;clear:both;visibility:hidden;}
</style>

</head>
<body>
<a href="/LocalDateTimeSchedule/MonthDisplayServlet?mon=current">今月表示へ戻る</a>
<hr />

<h3>タイムスケジュール登録</h3>


<div id="contents">

<div id="left">

<table class="sche">
<tr><td class="top" style="width:80px">時刻</td><td class="top" style="width:300px">予定</td></tr>
<tr><td class="time">00:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">01:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">02:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">03:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">04:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">05:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">06:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">07:00</td><td class="ex" rowspan="3"></td></tr>
<tr><td class="timeb"></td></tr>
<tr><td class="time">08:00</td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">09:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">10:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">11:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">12:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">13:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">14:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">15:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">16:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">17:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">18:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">19:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">20:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">21:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">22:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">23:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>

</table>

</div>

<div id="right">



<form method="post" action="ScheduleInsertServlet">
<table>
<tr>
<td nowrap>日付</td><td>
<select name="year">
<% for (int i = year-1 ; i <= year+1 ; i++){
  if(i == year) {
%>
<option value=<%=i %> selected><%=i %>
<% } else { %>
<option value=<%=i %> ><%=i %>
<%
}
}
%>
</select>

<select name="month" >
<% for (int i = 1 ; i <= 12 ; i++){
  if(i == month) {
%>
<option value=<%=i %> selected><%=i %>
<%} else {%>
<option value=<%=i %> ><%=i %>
<%
}
}
%>
</select>

<select name="day" id="day">
<% for (int i = 1 ; i <= thisMonthlastDay ; i++){
  if(i == day) {
%>
<option value=<%=i %> selected><%=i %>
<% } else { %>
<option value=<%=i %>><%=i %>
<%
}
}
  %>
</select>

</td></tr>

<tr><td nowrap>時刻</td><td>
<select name="s_hour">
<option value="" selected>--時
<% for (int i = 0 ; i <= 23 ; i++){
%>
<option value="<%=i %>"><%=i %>時
<% } %>
</select>

<select name="s_minute">
<option value="">--分
<option value="0">00分
<option value="30">30分
</select>

<select name="e_hour">
<option value="" selected>--時
<% for (int i = 0 ; i <= 23 ; i++){
%>
<option value="<%=i %>"><%=i %>時
<% } %>
</select>


<select name="e_minute">
<option value="">--分
<option value="0">00分
<option value="30">30分
</select>
</td></tr>

<tr>
<td nowrap>予定</td>
<td><input type="text" name="schedule" value="" size="30" maxlength="70">
</td>
</tr>

<tr>
<td valign="top" nowrap>メモ</td>
<td><textarea name="scheduleMemo" cols="30" rows="8" wrap="virtual"></textarea></td>
</tr>
</table>

<p>
<input type="submit" name="Register" value="送信">
<input type="reset" value="キャンセル">
<p>
</form>

</div>

</div>

</body>
</html>