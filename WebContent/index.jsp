<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%
  String message = (String) request.getAttribute("loginFailure");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>スケジュール管理</title>
</head>
<body>

  <h2>スケジュール帳へようこそ</h2>
  <hr>
<!-- このログイン画面は、ログイン処理で失敗した場合の遷移先にもなっているため、
メッセージが取得できた、つまりログイン処理に失敗した場合は、そのメッセージを表示しています -->
  <% if (message != null) { %>
    <p><%=message %></p>
  <%} %>
  <div >
    <form action="/LocalDateTimeSchedule/LoginCheckServlet" method="post">
      <table>
        <tr>
          <th >ユーザ名</th>
          <td ><input type="text" name="scheduleUser" value="" size="32"  ></td>
        </tr>
        <tr>
          <th >パスワード</th>
          <td><input type="password" name="pass" value="" size="32"></td>
        </tr>
        <tr>
          <td colspan="2">
            <input type="submit" value="login">
            <input type="reset" value="reset">
          </td>
        </tr>
      </table>
    </form>
  </div>
</body>
</html>