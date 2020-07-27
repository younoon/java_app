<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
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
	<p>R02_search_disp.jsp</p>
</div>
<%
	//サーブレットから値の受け取り
	String strName    = (String)request.getAttribute("username");
	ArrayList<HashMap<String,String>> ary =(ArrayList<HashMap<String,String>>)request.getAttribute("data");
	out.println(strName);

	//DB内容を表示
	for(int i=0;i<ary.size();i++){
		HashMap<String,String>map = new HashMap<String,String>();
		map = ary.get(i);
		out.println(map.get("f_id"));
		out.println(ary.get(i).get("f_name"));
		out.println("<br />");
	}

	//php foreac配列 as 変数)
	//拡張for文
	//    for(クラス　変数　配列)
	for(HashMap<String,String>map:ary){
		out.println(map.get("f_id"));
		out.println("<br />");
	}
%>
</body>
</html>