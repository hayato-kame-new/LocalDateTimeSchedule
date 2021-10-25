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
  <h4>ログインフォーム</h4>
    <form action="/LocalDateTimeSchedule/LoginCheckServlet" method="post">
      <table>
  <!--       <tr>
          <th >ユーザ名</th>
          <td ><input type="text" name="scheduleUser" value="" size="32"  ></td>
        </tr> -->
         <tr>
          <th >メールアドレス</th>
          <td><input type="email" name="mail" value="" size="32"></td>
        </tr>
        <tr>
          <th >パスワード</th>
          <td><input type="password" name="flat_password" value="" size="32"></td>
        </tr>
        <tr>
          <td colspan="2">
            <input type="submit" value="ログイン">
            <input type="reset" value="リセット">
          </td>
        </tr>
      </table>
    </form>
  </div>

  <div>
  <!-- フォームの  method属性のデフォルト値は GET です  method="get" は書かなくても良い
  formタグで送る理由は、サーブレットに行ってから、jspファイルにアクセスしたいから。
  aリンクでは送れないので、(GETメソッドでuser_registration.jspにリンクでできないため aリンクは HTTPメソッドのGET)
  フィルターで全てのサーブレットとjspに、ログイン前にアクセスすると、index.jspへ転送するようにしてるのでaリンクでアクセスできないだめ -->
    <p>まだ、ユーザー登録されていない方は登録してください</p>
    <form action="/LocalDateTimeSchedule/UserFormServlet" >
       <input type="submit" value="新規登録画面へ">
    </form>
  </div>
</body>
</html>