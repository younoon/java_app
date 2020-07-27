<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<!DOCTYPE html>
<html:html lang ="true">
<head>
<title>StrutsでHelloworld</title>
</head>
<body>
<h2>HelloWorld！！</h2>
<html:form action="action.do" method="post">
	ユーザＩＤ<html:text property="in_id" /> <br/>
	パスワード<html:password property="in_pass" />
	<html:submit value="ログイン" />
	メッセージ<html:text property="out_msg" readonly="true" />
</html:form>
</body>
</html:html>
