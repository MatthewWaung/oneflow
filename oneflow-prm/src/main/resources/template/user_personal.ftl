<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="Content-Style-Type" content="text/css"/>
    <title></title>
    <style type="text/css">
        table {
            border-collapse: collapse;
            table-layout: fixed;
        }

        td {
            border: 1px solid;
            padding: 5px auto;
            text-align: center;
            word-break: break-word;
        }
    </style>
</head>
<body>
<div style="width: 100%;">

    <h2>参考项目https://gitee.com/dlqx/springboot-code-book中的export-pdf模块</h2>

    <table style="width: 100%;margin: 0 auto;" cellpadding="0" cellspacing="0">
        <tr>
            <td style="width: 13%;">用户账号</td>
            <td style="width: 14%;">${userId!}</td>
            <td style="width: 13%;">用户名称</td>
            <td style="width: 14%;">${userName!}</td>
            <td style="width: 13%;">性别</td>
            <td style="width: 14%;">${sex!}</td>
            <td rowspan="3" style="width: 19%;">
                <#if photoUrl??>
                    <img width="100%" height="100%" src="${photoUrl!}"/>
                <#else>
                    &nbsp;
                </#if>
            </td>
        </tr>
        <tr>
            <td>出生年月日</td>
            <td>${birthday!}</td>
            <td>民族</td>
            <td>${nationality!}</td>
            <td>籍贯</td>
            <td>${hometown!}</td>
        </tr>
        <tr>
            <td>联系方式</td>
            <td colspan="2">${phone!}</td>
            <td>邮箱</td>
            <td colspan="2">${email!}</td>
        </tr>
        <tr>
            <td>简介</td>
            <td colspan="6" style="text-align: left;padding-left: 5px;">
                <div style="width: 100%;height: 100%;word-wrap: break-word;word-break: break-all;">
                    ${(introduction?replace("\n","<br/>"))!}
                </div>
            </td>
        </tr>

        <tr>
            <td rowspan="${(educationList?? && educationList?size > 0) ? string(educationList?size + 1, 2)}">学历学位</td>
            <td>教育程度</td>
            <td>文化程度</td>
            <td>学位</td>
            <td colspan="2">毕业院校</td>
            <td>专业</td>
        </tr>
        <#if educationList?? && (educationList?size > 0)>
            <#list educationList as node>
                <tr>
                    <td>${node.system!}</td>
                    <td>${node.level!}</td>
                    <td>${node.degree!}</td>
                    <td colspan="2">${node.university!}</td>
                    <td>${node.major!}</td>
                </tr>
            </#list>
        <#else>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td colspan="2">&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
        </#if>

    </table>
</div>
</body>
</html>
