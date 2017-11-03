<html>
<head>
    <title>success</title>
</head>
<body>
<table width="100%" border=1>
    <tr>
        <th >id</th>
        <th>文件名称</th>
        <th>上传地址</th>
        <th>转码地址</th>
        <th>状态</th>
        <th>转码次数</th>
        <th>转码信息</th>
        <th>回调信息</th>
        <th>videokey</th>

    </tr>
<#list dataList as data>
    <tr>
            <td>${data.id }</td>
            <td>${data.filename }</td>
            <td>${data.srcurl }</td>
            <td>${data.desurl }</td>
            <td>${data.status }</td>
            <td>${data.counts }</td>
            <td>${data.msg }</td>
            <td>${data.callmsg }</td>
            <td>${data.videokey }</td>


        </tr>

</#list>

</table>
</body>
</html>
