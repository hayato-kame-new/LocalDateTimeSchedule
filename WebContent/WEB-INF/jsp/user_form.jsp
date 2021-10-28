<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.UserBean, java.util.List" %>

<%
//  request も session も、JSPで使える暗黙オブジェクトです
// UserFormサーブレットでは、セッションスコープに、UserBean空のインスタンス(各フィールドは、各データ型の規定値になってる) が保存されてますので
// セッションスコープから、取り出して、フォームに使う セッションスコープにUserBeanインスタンスがあることで、フィルターでindex.jspへ転送されない

 UserBean userBean = (UserBean)session.getAttribute("userBean");


String action = (String)request.getAttribute("action");  // "add" "edit" "re_enter"  が入ってる

// 後で actionによって title h4 を切り替えるようにすること

// もし、action が "re_enter"  なら  再入力を行うようにします
String form_msg = "";
String name = "";
Integer str_roll = 0;
int roll = -1;  // 初期値
String mail = "";
List<String> errMsgList = null;
if(action != null && action.equals("re_enter")) {
  // 失敗した時のメッセージ UserServletから、フォワードしてくる時にリクエストスコープに保存したので、取得する
   form_msg = (String)request.getAttribute("form_msg");
  // 失敗した時にフォームに入力してあったのを表示
   name = (String)request.getAttribute("name");
  // String flat_password = (String)request.getAttribute("flat_password");  // これは表示しないでおく要らない
  str_roll = (Integer)request.getAttribute("roll");
   roll = str_roll.intValue();
  mail = (String)request.getAttribute("mail");
  errMsgList = (List<String>)request.getAttribute("errMsgList");  // バリデーションに引っかかったエラーのメッセージが入ってる

}


%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザ新規登録</title>
<style>
.err {
  color: red;
}
</style>
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
  <%
    if(errMsgList != null) {
    for(String errMsg : errMsgList) {
  %>
    [&nbsp;<span class="err"><%=errMsg %>&ensp;</span>&nbsp;]
  <%
    }
    }
  %>
    <form action="/LocalDateTimeSchedule/UserServlet" method="post">

      <input type="hidden" name="action" value="<%=action %>" />
      <table>
        <tr>
          <th >ユーザ名</th>
          <td ><input type="text" name="name" value="<%=name %>" size="32"  ></td>
        </tr>
        <tr>
          <th >パスワード</th>
          <td>
             <small>※パスワードは 6文字以上、10文字以下の半角英数字で入力してください</small><br />
              <input type="password" name="flat_password" value="" size="32">
          </td>
        </tr>
        <tr>
          <th >メールアドレス</th>
          <td>
              <small>※メールアドレスの形式で入力してください(例: aaa@bbb.com)</small><br />
              <input type="email" name="mail" value="<%=mail %>" size="32">
          </td>
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
               <option value="-1" >選択してください</option>
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