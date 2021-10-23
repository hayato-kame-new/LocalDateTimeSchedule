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
  %>
 <!--  <p>スケジュール帳をご利用頂くにはまずログインして頂く必要があります。ユーザー名とパスワードを入力してログインして下さい。</p>
 -->
  <%
  /* sessionはjspの暗黙オブジェクトの１つなので、いきなり使える */
 //  session = request.getSession(true);

  /* 認証失敗から呼び出されたのかどうか */
 //  Object status = session.getAttribute("status");

 /*  String str = "";
  if (status != null){
    str = "認証に失敗しました 再度ユーザー名とパスワードを入力して下さい";
      session.setAttribute("status", null);
  } */
  %>

 <%--  <p><%=str %></p> --%>



  <div align="center">
    <form action="/LocalDateTimeSchedule/LoginCheckServlet" method="post">
     <input type="hidden" name="action" value="action.LoginLogic" />
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