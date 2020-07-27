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

	<!-- JavaBeansの操作 -->

	<jsp:useBean id="bean1" class="beans.Mybeans" scope="page"></jsp:useBean>
	<!-- Mybeans bean1 = new Mybeans(); -->

	<jsp:setProperty property="id" name="bean1" value="001" />
	<!-- bean1.setId("001"); -->

	<jsp:getProperty property="id" name="bean1"/>
	<!-- out.println( bean1.getId() ); -->


	<form action="<%= strPath %>/servlet/R02_Servlet" method="GET">
	名前：<input type="text" name="txtName"><br>
	<input type ="submit" name="subExec" value="GET">
	</form>
</body>
</html>