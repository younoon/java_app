<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>R01_check</title>
</head>

<%
	//コンテキストパスの取得
	String strPath = request.getContextPath();
	//↑	/2019_JV32

	//サーブレットから値を受け取る
	String strId      = (String)request.getAttribute("userid");
	String strName    = (String)request.getAttribute("username");
	String strAge     = (String)request.getAttribute("userage");
	String strMessage = (String)request.getAttribute("message");

%>

<body>
	<%= strMessage %>
	<form action="<%= strPath %>/servlet/R01_Servlet" method="GET">
	ID：<input type="text" name="txtId" value ="<%= strId %>"><br>
	名前：<input type="text" name="txtName" value ="<%= strName %>"><br>
	年齢：<input type="text" name="txtAge" value ="<%= strAge %>"><br>
		<input type="hidden" name="txtMod" value ="cheak">
		<input type ="submit" name="subExec" value="insert">
	</form>
	<form action="<%= strPath %>/servlet/R01_Servlet" method="POST">
		<input type ="text" name="txtName">
		<input type ="submit" name="" value="POST">
	</form>

</body>
</html>