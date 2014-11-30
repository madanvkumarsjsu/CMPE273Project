<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.io.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Etcd</title>
</head>
<body>
<h1>Etcd Service Registry</h1>
<Table BORDER="1" CELLPADDING="3" CELLSPACING="1">
    <tr>
        <th>Key</th>
        <th>Value</th>
    </tr>    
<c:forEach var="entry" items="${map}">
	<tr>
    	<td>${entry.key}</td>
    	<td>${entry.value}</td>   	
	</tr>
</c:forEach>
</Table>
</body>
</html>