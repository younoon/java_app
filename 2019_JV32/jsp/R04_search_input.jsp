<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>R04_search_input</title>
</head>

<%
	//コンテキストパスの取得
	String strPath = request.getContextPath();
	//↑	/2019_JV32
%>

<body>
	<form action="<%= strPath %>/servlet/R04_Servlet" method="GET">
	名前：<input type="text" name="txtName"><br>
	<input type ="submit" name="subExec" value="GET">
	</form>
</body>
</html>