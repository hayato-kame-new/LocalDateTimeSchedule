<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%
 /*  Map<String,String> userMap = (HashMap<String,String>) session.getAttribute("userMap");
  String scheduleUser = userMap.get("scheduleUser"); */
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>スケジュール管理</title>
</head>
<body>
<h1>スケジュール管理</h1>

<a href="./login.jsp">ログイン画面へ</a>

<br />
<a href="./user_registration.jsp">新規ユーザ登録画面へ</a>

<!-- <a href="./display.jsp" >月一覧表の表示</a> -->
<!-- JSPからサーブレットへ呼び出しも aリンクでできます ?以降のクエリーパラメータも使える  "/プロジェクト名/サーブレット名" -->
<!-- <a href="/LocalDateTimeSchedule/MonthDisplayServlet?mon=current" >今月表示</a> -->


</body>
</html>