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
	<p>err.jsp</p>
</div>
<%
	//サーブレットからあたいの受け取り
	String strErr = request.getParameter("err");

	switch(Integer.parseInt(strErr)){
	case 1:
		out.println("不正アクセス");
		break;

	case 2:
		out.println("ドライバロードエラー");
		break;

	case 3:
		out.println("SQL異常"+request.getAttribute("errmsg"));
		break;

	case 4:
		out.println("Closeエラー");
		break;
	}

	out.println(strErr);

%>
</body>
</html>