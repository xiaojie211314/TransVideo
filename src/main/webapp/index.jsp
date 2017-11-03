<%--
  Created by IntelliJ IDEA.
  User: jieli
  Date: 2017/10/13
  Time: 16:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <meta http-equiv="charset" content="utf-8" >
    <meta http-equiv="content-type" content="application/json" >
</head>
<body>
<form action="/video/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="file" />
    <input type="text" name="callUrl" value="http://www.baidu.com">
    <input type="text" name="sign" value="83e89d7d87aec36ca1f4149fffe9b8ec">
    <button >Submit</button>
</form>
</body>
</html>
