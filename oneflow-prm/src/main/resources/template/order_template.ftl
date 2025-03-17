<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <title>Title</title>
    <style>
        body {
            font-family: SimSun;     <#-- 注意此处字体需要和itext设置的字体一致 -->
        }
        .blue {
            color: blue;
        }
    </style>
</head>

<body>
<!--第一页开始-->
<div class="page blue">
    <div>基础信息</div>
    <div>订单编号:${orderNo}</div>
    <div>原始订单编号:${originalOrderNo}</div>
    <br/>
    <div>订单类型:${orderType}</div>
    <div>销售组织:${salesOrganization}</div>
    <div>备注:${note?replace("\n", "<br/>")}</div>
    <#--    <#list scores as item>-->
    <#--        <div>${item}</div>-->
    <#--    </#list>-->
    <br/>
</div>
<!--第一页结束-->
<!---分页标记-->
<div style="page-break-after:always;"></div>

<!--第二页开始-->
<div class="page">
    <div>第二页开始了</div>
    <#--    <img src="${imageUrl}" alt="百度图标" width="270" height="129"/>-->
</div>


<!--第二页结束-->
</body>
</html>
