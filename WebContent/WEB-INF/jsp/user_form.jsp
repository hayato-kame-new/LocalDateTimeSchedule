<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.UserBean" %>

<%
// request も session も、JSPで使える暗黙オブジェクトです
// UserFormサーブレットでは、セッションスコープに、UserBean空のインスタンス(各フィールドは、各データ型の規定値になってる) が保存されてますので
// セッションスコープから、取り出して、フォームに使う セッションスコープにUserBeanインスタンスがあることで、フィルターでindex.jspへ転送されない

 UserBean userBean = (UserBean)session.getAttribute("userBean");


String action = (String)request.getAttribute("action");  // "add" "edit" "re_enter"  が入ってる

// 後で actionによって title h4 を切り替えるようにすること

// もし、action が　再入力だったら、行うようにします
String form_msg = "";
String scheduleUser = "";
Integer str_roll = 0;
int roll = -1;  // 初期値
String mail = "";
if(action != null && action.equals("re_enter")) {
// 失敗した時のメッセージ UserServletから、フォワードしてくる時にリクエストスコープに保存したので、取得する
 form_msg = (String)request.getAttribute("form_msg");
// 失敗した時にフォームに入力してあったのを表示
 scheduleUser = (String)request.getAttribute("scheduleUser");
// String flat_password = (String)request.getAttribute("flat_password");  // これは表示しないでおく要らない
str_roll = (Integer)request.getAttribute("roll");
 roll = str_roll.intValue();
mail = (String)request.getAttribute("mail");

}


%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザ新規登録</title>
</head>
<body>

   <div >
  <h4>ユーザ登録画面</h4>
  <%
    if (form_msg != null) {
  %>
  <p><%= form_msg %></p>
  <%
    }
  %>
    <form action="/LocalDateTimeSchedule/UserServlet" method="post">

      <input type="hidden" name="action" value="<%=action %>" />
      <table>
        <tr>
          <th >ユーザ名</th>
        <%--   <td ><input type="text" name="scheduleUser" value="<%=userBean.getScheduleUser() %>" size="32"  ></td> --%>
          <td ><input type="text" name="scheduleUser" value="<%=scheduleUser %>" size="32"  ></td>
        </tr>
        <tr>
          <th >パスワード</th>
          <td><input type="password" name="flat_password" value="" size="32"></td>
        </tr>
        <tr>
          <th >メールアドレス</th>
        <%--   <td><input type="email" name="mail" value="<%=userBean.getMail() %>" size="32"></td> --%>
          <td><input type="email" name="mail" value="<%=mail %>" size="32"></td>
        </tr>
        <tr>
          <th >権限</th>
          <td >
            <select name="roll">
              <% if (roll == 0) { %>
              <option value="0" selected>一般</option>
              <option value="1">管理者</option>
              <% } else if(roll == 1) { %>
               <option value="0" >一般</option>
               <option value="1" selected>管理者</option>
              <% } else {%>
               <option value="" >選択してください</option>
               <option value="0" >一般</option>
               <option value="1" >管理者</option>
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