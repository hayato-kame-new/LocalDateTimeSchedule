<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザ新規登録</title>
</head>
<body>

   <div >
  <h4>一般ユーザ登録画面</h4>
    <form action="/LocalDateTimeSchedule/UserServlet" method="post">
      <!-- 管理者権限のないユーザの登録をするので、 0 の値を送る -->
      <input type="hidden" name="roll" value="0" />
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
            <input type="submit" value="登録">
            <input type="reset" value="リセット">
          </td>
        </tr>
      </table>
    </form>
  </div>

</body>
</html>