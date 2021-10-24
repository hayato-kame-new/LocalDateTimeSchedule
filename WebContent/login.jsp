<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%
  String message = (String) request.getAttribute("loginFailure");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン</title>
<style>

</style>

</head>
<body>
  <h2>スケジュール帳へようこそ</h2>
  <hr>
<!-- このログイン画面は、ログイン処理で失敗した場合の遷移先にもなっているため、
メッセージが取得できた、つまりログイン処理に失敗した場合は、そのメッセージを表示しています -->
  <% if (message != null) { %>
    <p><%=message %></p>
  <%} %>
  <div align="center">
    <form action="/LocalDateTimeSchedule/LoginCheckServlet" method="post">
     <!-- <input type="hidden" name="action" value="action.LoginLogic" /> -->
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