<%@page import="beans.Mybeans"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>R04_search_dispt</title>
</head>
<body>
<div>
	<p>R04_search_disp.jsp</p>
</div>
<%
	//サーブレットから値の受け取り
	String strName    = (String)request.getAttribute("username");
	ArrayList<Mybeans> ary =(ArrayList<Mybeans>)request.getAttribute("data");
%>

<%
	out.println(strName);

	//DB内容を表示
	for(int i=0;i<ary.size();i++){
		Mybeans bean = new Mybeans();
		bean = ary.get(i);
		out.println(bean.getId());
		out.println(bean.getName());
		out.println(bean.getAge());
		out.println(ary.get(i).getId());
		out.println(ary.get(i).getName());
		out.println(ary.get(i).getAge());
		out.println("<br />");
	}

	//php foreac配列 as 変数)
	//拡張for文
	//    for(クラス　変数　配列)
	for(Mybeans bean :ary){
		out.println(bean.getId());
		out.println(bean.getName());
		out.println(bean.getAge());
		out.println("<br />");
	}
%>
<br />



</body>
</html>