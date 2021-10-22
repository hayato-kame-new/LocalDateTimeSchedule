<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.ScheduleBean,
java.util.List, java.util.LinkedList, viewComposer.TimeScheduleView,
java.time.LocalDate, java.time.temporal.TemporalAdjusters, java.time.LocalTime" %>


<%
// 文字化け対策
request.setCharacterEncoding("UTF-8");
// NewScheduleServletサーブレットでリクエストスコープに保存してるので  リクエストスコープから取り出す 表示に必要
String action = (String)request.getAttribute("action");
String title = action.equals("add") ? "新規登録" : "編集";
// リクエストスコープから フォーム用のインスタンスを取り出して
//  action が "add"の時の、formScheBeanインスタンスは、ユーザIDだけ入ってるあとは、データ型の規定値になってます int型なら 0 参照型なら null が入ってます
ScheduleBean formScheBean = (ScheduleBean)request.getAttribute("formScheBean");
int id = formScheBean.getId();  // 新規では、 int型の規定値の 0 が入ってる  編集では、主キーの値がきちんと入ってる

LocalDate scheduleDate = formScheBean.getScheduleDate(); // 新規の時も 年月日はある NewScheduleServletサーブレットでLocalDateの値は、きちんと入ってる

int year =  scheduleDate.getYear(); // 新規のは   scheduleDate は入ってる
int month =  scheduleDate.getMonthValue();
int day =  scheduleDate.getDayOfMonth();
// その月が何日あるのか
int thisMonthlastDay = LocalDate.of(year, month, day).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();


LocalTime startTime = formScheBean.getStartTime();  // 新規の時は null
LocalTime endTime = formScheBean.getEndTime(); // 新規の時は null


// 時間と分に分けておく
String s_hour = "";
String s_minute =  "";
String e_hour = "";
String e_minute =  "";
// ここ新規の時は null なので、実行しようとすると NullPointerException発生する 新規では null回避する
if(!action.equals("add")) { // null対策
 s_hour = String.valueOf(startTime.getHour());  // 開始時間の時間
 s_minute = String.format("%02d", startTime.getMinute());  // 開始時間の分
 e_hour = String.valueOf(endTime.getHour());  // 終了時間の時間
 e_minute = String.format("%02d", endTime.getMinute());  // 終了時間の分
}

// リクエストスコープから取り出す もし、リストの要素が 0でなかったら、表示する
List<ScheduleBean> oneDayScheduleList = (List<ScheduleBean>)request.getAttribute("oneDayScheduleList");
LinkedList<String> timeStack = (LinkedList<String>)request.getAttribute("timeStack");


%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>タイムスケジュール<%= title %></title>

<style>
table.sche{border:1px solid #a9a9a9;padding:0px;margin:0px;border-collapse:collapse;}
td{vertical-align:top;margin:0px;padding:2px;font-size:0.75em;height:20px;}
td.top{border-bottom:1px solid #a9a9a9;text-align:center;}
td.time{background-color:#f0f8ff;text-align:right;border-bottom:1px solid #a9a9a9;border-right:1px double #a9a9a9;padding-right:5px;}


/*  td.timeb{background-color:#f0f8ff;border-bottom:1px solid #a9a9a9;border-right:1px double #a9a9a9;} */
td.contents{background-color:#ffffff;border-bottom:1px solid #a9a9a9;}
 /* td.contentsb{background-color:#ffffff;border-bottom:1px solid #a9a9a9;} */
td.ex{background-color:#ffebcd;border:1px solid #8b0000;}
img{border:0px;}
p{font-size:0.75em;}

#contents{margin:0;padding:0;width:710px; }
#left{margin:0;padding:0;float:left;width:400px;}
#right{margin:0;padding:0;float:right;width:300px;background-color:#ffffff;}
#contents:after{content:".";display:block;height:0;clear:both;visibility:hidden;}
#contents span {color: darkgreen; font-weight:bold;}
.memo {color:#444;}
</style>
<script src="../js/jquery-3.6.0.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>


<body>
<a href="/LocalDateTimeSchedule/MonthDisplayServlet?mon=current">今月表示へ戻る</a>
<hr />

<h2>タイムスケジュール<%= title %></h2>


<div id="contents">

<div id="left">

<table class="sche">

<tr><td class="top" style="width:80px">時刻</td><td class="top" style="width:300px">予定</td></tr>

<%
  for(int i = 0; i < timeStack.size(); i++ ){
 %>
<tr>
  <td class="time"><%=timeStack.get(i) %></td>
  <td class="contents">
 <% for(int j = 0; j < oneDayScheduleList.size(); j++) {
       if(timeStack.get(i).equals(oneDayScheduleList.get(j).createStrStartTime() )){
   %>
  [<%= oneDayScheduleList.get(j).createStrStartTime()%>-<%= oneDayScheduleList.get(j).createStrEndTime()%>]
  <a href="/LocalDateTimeSchedule/NewScheduleServlet?action=edit&id=<%=oneDayScheduleList.get(j).getId() %>"><small class="schedule"><span><%= oneDayScheduleList.get(j).getSchedule() %></span></small></a><br />
  <small class="memo">メモ: <%= oneDayScheduleList.get(j).getScheduleMemo() %></small><br />
  <%
  }
 }
 %>
  </td>

</tr>
<%
    }
%>
</table>
</div>


<!-- フォーム表示 -->
<div id="right">
<h3>スケジュールを<%=title %>します</h3>
<form method="post" action="ScheduleInsertServlet">
  <input type="hidden" name="action" value="<%=action %>" />
  <!-- 編集では、主キーの値が必要 -->
  <input type="hidden" name="id" value="<%=id %>" />
  <table>
    <tr>
      <td nowrap>日付</td>
      <td>
      <select name="year" class="js-changeYear">
      <%
      for (int i = year-1 ; i <= year+1 ; i++){
        if(i == year) {
          // その年を記憶

      %>
      <option value=<%=i %> selected><%=i %>
      <% } else { %>
      <option value=<%=i %> ><%=i %>
      <%
      }
      }
      %>
      </select>

      <select name="month" class="js-changeMonth">
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

      <select name="day" class="js-changeDay">
      <%
      // ちょっとここ修正の必要がある
      // for (int i = 1 ; i <= thisMonthlastDay ; i++){
        for (int i = 1 ; i <= 31 ; i++){
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

      </td>
    </tr>

    <tr>
    <td nowrap>時刻</td>
    <td>
    <select name="s_hour">
    <% if (s_hour.equals("")) {%>
      <option value="" selected>--時
    <% } %>
    <% for (int i = 0 ; i <= 23 ; i++){
       if( s_hour != null && !s_hour.equals("") &&  Integer.parseInt(s_hour) == i ) {
    %>
    <option value="<%=i %>" selected><%=i %>時
    <%
     } else {
     %>
     <option value="<%=i %>" ><%=i %>時
      <% }
       }%>
    </select>

    <select name="s_minute">
    <% if (s_minute.equals("")) {%>
      <option value="" selected>--分
      <option value="0" selected>00分
      <option value="30">30分
    <% } else if ( s_minute != null && !s_minute.equals("") && s_minute.equals("00")){ %>
      <option value="" >--分
      <option value="0" selected>00分
      <option value="30">30分
    <% } else if ( s_minute != null &&  !s_minute.equals("") && s_minute.equals("30")){ %>
      <option value="" >--分
      <option value="0" >00分
      <option value="30" selected>30分
      <% } %>
    </select>

    <select name="e_hour">
    <% if (e_hour.equals("")) {%>
      <option value="" selected>--時
    <% } %>
    <% for (int i = 0 ; i <= 23 ; i++){
       if( e_hour != null && !e_hour.equals("") && Integer.parseInt(e_hour) == i ) {
    %>
    <option value="<%=i %>" selected><%=i %>時
    <%
     } else {
     %>
     <option value="<%=i %>" ><%=i %>時
      <% }
       }%>
    </select>


    <select name="e_minute">
    <% if (e_minute.equals("")) {%>
      <option value="" selected>--分
      <option value="0" >00分
      <option value="30">30分
    <% } else if ( e_minute != null && !e_minute.equals("") && e_minute.equals("00")){ %>
      <option value="" >--分
      <option value="0" selected>00分
      <option value="30">30分
    <% } else if ( e_minute != null &&  !e_minute.equals("") && e_minute.equals("30")){ %>
      <option value="" >--分
      <option value="0" >00分
      <option value="30" selected>30分
      <% } %>
    </select>

  <tr>
    <td nowrap>予定</td>
    <td><input type="text" name="schedule" value="<%=formScheBean.getSchedule() %>" size="30" maxlength="70">
    </td>
  </tr>

  <tr>
    <td valign="top" nowrap>メモ</td>
    <td><textarea name="scheduleMemo" cols="30" rows="8" wrap="virtual"><%=formScheBean.getScheduleMemo() %></textarea></td>
  </tr>
  </table>

  <p>
  <input type="submit" name="Register" value="送信">
  <input type="reset" value="キャンセル">
  <p>
</form>

</div>

</div>

<script>

(function($){
    function formSetDay(){
      var lastday = formSetLastDay($('.js-changeYear').val(), $('.js-changeMonth').val());
      var option = '';
      for (var i = 1; i <= lastday; i++) {
        if (i === $('.js-changeDay').val()){
          option += '<option value="' + i + '" selected="selected">' + i + '</option>\n';
        }else{
          option += '<option value="' + i + '">' + i + '</option>\n';
        }
      }
      $('.js-changeDay').html(option);
    }

    function formSetLastDay(year, month){
      var lastday = new Array('', 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
      if ((year % 4 === 0 && year % 100 !== 0) || year % 400 === 0){
        lastday[2] = 29;
      }
      return lastday[month];
    }

    $('.js-changeYear, .js-changeMonth').change(function(){
      formSetDay();
    });
  })(jQuery);

</script>

</body>
</html>