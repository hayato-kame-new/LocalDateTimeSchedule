<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザ新規登録</title>
</head>
<body>

	<h2>ユーザ新規登録画面</h2>
	<hr>
	<form action="./Login" method="post">
	  <label for="userName">ユーザ名：</label>
	  <input type="text" name="userName" id="userName" /><br />
	  <label for="password">パスワード：</label>
	  <input type="password" name="pass" id="password" /><br />
	  <input type="submit" value="登録" />
	</form>

</body>
</html>