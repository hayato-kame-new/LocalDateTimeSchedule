<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="model.UserBean" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%
/* LoginCheckServlet で ユーザの名前とパスワードが会ってれば、このwelcome.jsp ページにくる */
  // セッションスコープからログインユーザ情報を取得  フィルターで判断するために必要 セッションスコープから取り出す
  UserBean userBean = (UserBean) session.getAttribute("userBean");
%>
<html>
<head>
<meta charset="UTF-8">
<title>スケジュール帳へようこそ</title>
</head>
<body>
<h2>スケジュール帳へようこそ</h2>
<hr>
<% if( userBean != null ) { %>
  <p>ログインに成功しました。</p>
  <p>ようこそ<%= userBean.getScheduleUser() %>さん</p>
  <a href="/LocalDateTimeSchedule/MonthDisplayServlet?mon=current" >今月表示</a>
<% } %>
</body>
</html>