<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>R01_output</title>
</head>
<body>
<div>
	<p>R01_output.jsp</p>
</div>
<%
	//サーブレットから値の受け取り
	String strId      = (String)request.getAttribute("userid");
	String strName    = (String)request.getAttribute("username");
	String strAge     = (String)request.getAttribute("userage");

	out.println(strName);
%>
</body>
</html>