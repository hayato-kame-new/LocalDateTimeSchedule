<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.UserBean" %>

<%
// UserFormサーブレットでは、セッションスコープに、空のインスタンス(各フィールドは、各データ型の規定値になってる) が保存されてますので
// セッションスコープから、取り出して、フォームに使う
UserBean userBean = (UserBean)session.getAttribute("userBean");

%>
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
      <table>
        <tr>
          <th >ユーザ名</th>
          <td ><input type="text" name="scheduleUser" value="<%=userBean.getScheduleUser() %>" size="32"  ></td>
        </tr>
        <tr>
          <th >パスワード</th>
          <td><input type="password" name="pass" value="<%=userBean.getPass() %>" size="32"></td>
        </tr>
        <tr>
          <th >メールアドレス</th>
          <td><input type="email" name="mail" value="<%=userBean.getMail() %>" size="32"></td>
        </tr>
        <tr>
          <th >権限</th>
          <td >
            <select name="roll">
              <% if (userBean.getRoll() == 0) { %>
              <option value="0" selected>一般</option>
              <option value="1">管理者</option>
              <% } else if(userBean.getRoll() == 1) { %>
               <option value="0" >一般</option>
               <option value="1" selected>管理者</option>
              <% } %>
            </select>
          </td>
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