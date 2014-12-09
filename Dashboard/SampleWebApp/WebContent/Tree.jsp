<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page  import="java.util.*" %>
<%@page import="org.boon.etcd.Node" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<script src="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<link rel="stylesheet" href="/Users/iBallu/Desktop/Nice-Tree-View-Plugin-with-jQuery-Bootstrap-3-Easy-Tree/css/easyTree.css">
<script src="/Users/iBallu/Desktop/Nice-Tree-View-Plugin-with-jQuery-Bootstrap-3-Easy-Tree/src/easyTree.js"></script>
</head>
<body>

<div class="easy-tree">
<ul>
<li>Example 1</li>
<li>Example 2</li>
<li>Example 3
<ul>
<li>Example 1</li>
<li>Example 2
<ul>
<li>Example 1</li>
<li>Example 2</li>
<li>Example 3</li>
<li>Example 4</li>
</ul>
</li>
<li>Example 3</li>
<li>Example 4</li>
</ul>
</li>
<li>Example 0
<ul>
<li>Example 1</li>
<li>Example 2</li>
<li>Example 3</li>
<li>Example 4
<ul>
<li>Example 1</li>
<li>Example 2</li>
<li>Example 3</li>
<li>Example 4</li>
</ul>
</li>
</ul>
</li>
</ul>
</div>
<script>
(function ($) {
function init() {
$('.easy-tree').EasyTree({
selectable: true,
deletable: false,
editable: false,
addable: false,
});
}
 
window.onload = init();
})(jQuery)
</script>
</body>
</html>