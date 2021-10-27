<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%

  String loginFailureMsg = (String) request.getAttribute("loginFailure");
  String userRegistFailureMsg =  (String) request.getAttribute("userRegistFailure");
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
<!-- このログイン画面は、ログイン処理で失敗した場合の遷移先にもなっている また、新規登録処理失敗でも遷移先となっている
  失敗するとメッセージが取得でき そのメッセージを表示しています -->
  <% if (loginFailureMsg != null) { %>
    <p><%=loginFailureMsg %></p>
  <%} %>
   <% if (userRegistFailureMsg != null) { %>
    <p><%=userRegistFailureMsg %></p>
  <%} %>
  <div >
  <h4>ログインフォーム</h4>
    <form action="/LocalDateTimeSchedule/LoginCheckServlet" method="post">
    <!-- LoginCheckServletで、セッションを作ってセッションスコープにUserBeanインスタンスを保存して、ログインをしていることにしてます -->
      <table>
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
  formタグで送る理由は、サーブレットに行ってから、jspファイルにアクセスしたいから。サーブレット経由にしないと、フィルターで戻される
  フィルターで全てのサーブレットとjspに、最初にアクセスすると、index.jspへ転送するようにしてるのでaリンクでアクセスできない
  aリンクでは送れないので、(HTTPメソッドのGETメソッドでuser_registration.jspにリンクでできない aリンクは HTTPメソッドのGET) フィルターで、index.jspに転送処理されてしまう
  -->
    <p>まだ、ユーザー登録されていない方は登録してください</p>
    <form action="/LocalDateTimeSchedule/UserFormServlet" >
      <input type="hidden" name="action" value="add" />
    <!-- UserFormServletでセッションを作って、セッションスコープに新規のUserBeanを保存することによって、ログインの状態と同じことを意味しています -->
       <input type="submit" value="新規登録画面へ">
    </form>
  </div>
</body>
</html>