<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>スケジュール管理</title>
</head>
<body>
<h1>スケジュール管理</h1>


<!-- <a href="./display.jsp" >月一覧表の表示</a> -->
<!-- JSPからサーブレットへ呼び出しも aリンクでできます ?以降のクエリーパラメータも使える  "/プロジェクト名/サーブレット名" -->
<a href="/LocalDateTimeSchedule/MonthDisplayServlet?mon=current" >今月表示</a>


</body>
</html>