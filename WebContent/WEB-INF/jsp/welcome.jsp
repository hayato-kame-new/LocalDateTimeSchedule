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

 <!--  自分が登録したスケジュールだけ見れるようにする ?以降はクエリー文字列です aリンクはGETアクセスなのでクエリー文字列で遅れます
    クエリー文字列でユーザーの 主キーidを送ります-->
  <a href="/LocalDateTimeSchedule/MonthDisplayServlet?mon=current&id=<%=userBean.getId() %>" >今月表示へ</a>


<!-- ユーザ編集画面へ 編集の時には、もうすでにセッションに自分のUserBeanインスタンスが保存されてるので(ログインの時に保存されてるし、新規登録の時にも保存されてる)
クエリー文字列にユーザの idは必要なし-->
 <%--  <a href="/LocalDateTimeSchedule/UserFormServlet?action=edit&id=<%=userBean.getId() %>" >ユーザー情報編集画面へ</a> --%>
 <a href="/LocalDateTimeSchedule/UserFormServlet?action=edit" >ユーザー情報編集画面へ</a>
 サーブレット経由ではなく、直接 user_formにリンクで行ってもいいのではないか
  <a href="/LocalDateTimeSchedule/UserFormServlet?action=edit" >ユーザー情報編集画面へ</a>
  <!-- 本人であれば、ユーザアカウントを表示して、indexでみたり、
  パスワード以外を  編集したり、パスワードを変更したり  パスワードだけは、別のフォームでそれだけを変更する使用にする
  削除して　退会みたいな処理をしたり、管理者であれば、ユーザ一覧をみれたり、変更ができたり、パスワード以外の変更ができたり、削除できたり、新規に管理画面で
  登録できたり、する機能をつけること -->
<% } %>
</body>
</html>