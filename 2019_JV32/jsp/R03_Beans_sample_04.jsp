<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<%
    //コンテキストパスの取得
    String strPath
     = request.getContextPath();
    //↑ /2019_JV32
%>
<body>
<!-- JavaBeansの操作 -->
<jsp:useBean
 id="bean3"
 class="beans.Mybeans"
 scope="request"></jsp:useBean>
<!--  Mybeans bean3
       = (Mybeans)request.getAttribute("bean3");  -->

<jsp:getProperty
  name="bean3"
  property="id" />
<!--  out.println
       ( bean3.getId() );  -->
</body>
</html>