<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>R01_input</title>
</head>

<%
	//コンテキストパスの取得
	String strPath = request.getContextPath();
	//↑	/2019_JV32
%>

<body>
	<form action="<%= strPath %>/servlet/R01_Servlet" method="GET">
	ID：<input type="text" name="txtId"><br>
	名前：<input type="text" name="txtName"><br>
	年齢：<input type="text" name="txtAge"><br>
		<input type="hidden" name="txtMod" value="input">
		<input type ="submit" name="subExec" value="GET">
	</form>
	<form action="<%= strPath %>/servlet/R01_Servlet" method="POST">
		<input type ="text" name="txtName">
		<input type ="submit" name="" value="POST">
	</form>

</body>
</html>