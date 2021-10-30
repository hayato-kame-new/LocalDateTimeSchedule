<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.ScheduleBean,
java.util.List, java.util.LinkedList, viewComposer.TimeScheduleView,
java.time.LocalDate, java.time.temporal.TemporalAdjusters, java.time.LocalTime" %>


<%
// 文字化け対策
request.setCharacterEncoding("UTF-8");
// NewScheduleServletサーブレットでリクエストスコープに保存してるので  リクエストスコープから取り出す 表示に必要
// action には "re_enter" も渡ってきます

String action = (String)request.getAttribute("action");  //  "action" "edit" "delete"
String title = action.equals("add") ? "新規登録" : "編集"; // ここ直す　3つの分岐にすること
String re_enter = (String)request.getAttribute("re_enter");

// リクエストスコープから フォーム用のインスタンスを取り出して
//  action が "add"の時の、ScheduleBeanインスタンスは、ユーザIDだけ入ってるあとは、データ型の規定値になってます int型なら 0 参照型なら null が入ってます
ScheduleBean scheBean = (ScheduleBean)request.getAttribute("scheBean");  // "re_enter"再入力の時にはリクエストパラメータで渡って来ない
// もし、再入力の時には、リクエストパラメータで、scheBeanは渡って来ないので nullになってるから null回避する
int id = 0;
int userId = 0;
String schedule = "";
String scheduleMemo = "";
LocalDate scheduleDate = null;
int year = 0;
int month = 0;
int day = 0;
int thisMonthlastDay = 0;  // その月が何日あるのか
LocalTime startTime = null;
LocalTime endTime = null;


if(scheBean != null) {
id = scheBean.getId();  // 新規では、 int型の規定値の 0 が入ってる  編集では、主キーの値がきちんと入ってる
userId = scheBean.getUserId();  // 新規では？
scheduleDate = scheBean.getScheduleDate(); // 新規の時も 年月日はある NewScheduleServletサーブレットでLocalDateの値は、きちんと入ってる
year =  scheduleDate.getYear(); // 新規のは   scheduleDate は入ってる
month =  scheduleDate.getMonthValue();
day =  scheduleDate.getDayOfMonth();
//その月が何日あるのか
thisMonthlastDay = LocalDate.of(year, month, day).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
startTime = scheBean.getStartTime();  // 新規の時は null
endTime = scheBean.getEndTime(); // 新規の時は null
schedule = scheBean.getSchedule(); // 新規の時は null
scheduleMemo = scheBean.getScheduleMemo(); // 新規の時は null

}


// 時間と分に分けておく
String s_hour = "";
String s_minute =  "";
String e_hour = "";
String e_minute =  "";


//if(action.equals("edit") || action.equals("delete")) { // null対策 新規の時は null  削除の時にもフォームに再表示するから
  if( action.equals("delete")) { // null対策 新規の時は null  削除の時にもフォームに再表示するから
 s_hour = String.valueOf(startTime.getHour());  // 開始時間の時間
 s_minute = String.format("%02d", startTime.getMinute());  // 開始時間の分
 e_hour = String.valueOf(endTime.getHour());  // 終了時間の時間
 e_minute = String.format("%02d", endTime.getMinute());  // 終了時間の分
}

// リクエストスコープから取り出す もし、リストの要素が 0でなかったら、表示する
List<ScheduleBean> oneDayScheduleList = (List<ScheduleBean>)request.getAttribute("oneDayScheduleList");
LinkedList<String> timeStack = (LinkedList<String>)request.getAttribute("timeStack");

// セッションに保存
// 再入力でフォワードしてくるときに、上の oneDayScheduleList timeStack も必要となるため、セッションスコープに保存しています。
// インスタンスは inputタグのhiddenでは送れないため、
session.setAttribute("oneDayScheduleList", oneDayScheduleList);
session.setAttribute("timeStack" , timeStack);

// "re_enter" の時は バリデーションエラーメッセージを表示して 前にフォームに入力したのを表示する
String scheduleFailureMsg = "";
List<String> errMsgList = null;
if(re_enter != null && re_enter.equals("re_enter")) {
 // 失敗した時のメッセージ UserServletから、フォワードしてくる時にリクエストスコープに保存したので、取得する
   scheduleFailureMsg = (String)request.getAttribute("scheduleFailureMsg");
 // バリデーションエラーメッセージのリストをリクエストスコープから取得する
 errMsgList = (List<String>)request.getAttribute("errMsgList");  // バリデーションに引っかかったエラーのメッセージが入ってる
 // 前に入力したのをフォームに表示するので リクエストスコープから取得する
 id = (Integer)request.getAttribute("id");
 userId = (Integer)request.getAttribute("userId");
 year = (Integer)request.getAttribute("year");
 month = (Integer)request.getAttribute("month");
 day = (Integer)request.getAttribute("day");
 s_hour = String.valueOf((Integer)request.getAttribute("s_hour"));
 s_minute = String.format("%02d", (Integer)request.getAttribute("s_minute"));
 e_hour = String.valueOf((Integer)request.getAttribute("e_hour"));
 e_minute = String.format("%02d", (Integer)request.getAttribute("e_minute"));
schedule = (String)request.getAttribute("schedule");
scheduleMemo = (String)request.getAttribute("scheduleMemo");

}
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
.err {color:red;}

</style>
<script src="../js/jquery-3.6.0.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>


<body>

<hr />

<h2>タイムスケジュール<%= title %></h2>

<%-- <a href="/LocalDateTimeSchedule/MonthDisplayServlet?mon=specified&userId=<%=userId %>" ><%=year %>年<%=month %>月の表示に戻る</a> --%>
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
  <a href="/LocalDateTimeSchedule/ScheduleFormServlet?action=edit&id=<%=oneDayScheduleList.get(j).getId() %>"><small class="schedule"><span><%= oneDayScheduleList.get(j).getSchedule() %></span></small></a><br />
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
<h3>スケジュール入力フォーム</h3>
<%
  if(scheduleFailureMsg != null) {
%>
 <p class="err"><%= scheduleFailureMsg%></p>
<% } %>
 <%
    if(errMsgList != null) {
    for(String errMsg : errMsgList) {
  %>
    [&nbsp;<span ><%=errMsg %>&ensp;</span>&nbsp;]
  <%
    }
    }
  %>
<form method="post" action="ScheduleInsertServlet">
  <input type="hidden" name="action" value="<%=action %>" />
 <!--  新規登録の時に必要なuserId -->
  <input type="hidden" name="userId" value="<%=userId %>" />

  <p>ユーザID:<%=userId %></p>

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
    <!-- disabled属性が存在する場合、そのoption要素は無効になる 選択できなくなる -->
   <%
    if (s_hour.equals("")) {
  //  "add"の時だけs_hourが""になってますので
    %>
      <option disabled value="" >--時
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
  <% if (s_minute.equals("")) {
    // "add" の時だけs_minuteが "" になってる
  %>
  <!-- disabled属性が存在する場合、そのoption要素は無効になる 選択できなくなる -->
      <option disabled value="" >--分
      <option value="0">00分
      <option value="30">30分
      <% } %>
    <%  if ( s_minute != null && !s_minute.equals("") && s_minute.equals("00")){ %>
   <!--    <option value="" >--分 -->
      <option value="0" selected>00分
      <option value="30">30分
    <% } else if ( s_minute != null &&  !s_minute.equals("") && s_minute.equals("30")){ %>
    <!--   <option value="" >--分 -->
      <option value="0" >00分
      <option value="30" selected>30分
      <% } %>
    </select><small>〜</small><br />

    <select name="e_hour">
 <% if (e_hour.equals("")) {
  // "add" の時だけe_hourが "" になってる
 %>
  <!-- disabled属性が存在する場合、そのoption要素は無効になる 選択できなくなる -->
      <option disabled value="" >--時
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
      <option disabled value="" >--分
      <option value="0" >00分
      <option value="30">30分
      <% } %>
    <% if ( e_minute != null && !e_minute.equals("") && e_minute.equals("00")){ %>
    <!--   <option value="" >--分 -->
      <option value="0" selected>00分
      <option value="30">30分
    <% } else if ( e_minute != null &&  !e_minute.equals("") && e_minute.equals("30")){ %>
     <!--  <option value="" >--分 -->
      <option value="0" >00分
      <option value="30" selected>30分
      <% } %>
    </select>

  <tr>
    <td nowrap>件名</td>
    <td><input type="text" name="schedule" value="<%=schedule %>" size="30" maxlength="70">
    </td>
  </tr>

  <tr>
    <td valign="top" nowrap>メモ</td>
    <td><textarea name="scheduleMemo" cols="30" rows="8" wrap="virtual"><%=scheduleMemo %></textarea></td>
  </tr>
  </table>

  <p>
    <input type="submit" name="Register" value="送信">
    <input type="reset" value="キャンセル">
  <p>
</form>

<%
if(!action.equals("add")) {
%>
<!-- 削除のボタン表示 action の値を "delete" で送る 削除にはid主キーの値が必要なのでhiddenで送る 削除後に削除した月を表示する -->
<small>※このスケジュールを削除したい時は、削除ボタンを押してください</small>
<!-- onsubmit属性を使って 確認ダイアログを出す -->
<form method="post" action="ScheduleInsertServlet" onsubmit="return beforeSubmit()">
    <input type="hidden" name="action" value="delete" />
    <!-- 削除では、主キーの値が必要 -->
    <input type="hidden" name="id" value="<%=id %>" />

    <input type="submit" value="削除"  />
</form>
<% } %>
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

<script>

function beforeSubmit() {
    if(window.confirm('本当に削除しますか？')) {
      return true;
    } else {
      return false;
    }
  }
</script>


</body>
</html>