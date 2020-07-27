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

	<jsp:useBean id="bean2" class="beans.Mybeans" scope="session"></jsp:useBean>
	<!-- Mybeans bean2 = new Mybeans(); -->

	<jsp:setProperty property="id" name="bean2" value="002" />
	<!-- bean2.setId("002"); -->

	<form action="<%= strPath %>/jsp/R03_Beans_sample_03.jsp" method="GET">
	<input type ="submit" name="subExec" value="03のJSPへ">
	</form>
</body>
</html>