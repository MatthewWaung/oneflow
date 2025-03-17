<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <title>Title</title>
    <style>
        body {
            font-family: Source Han Serif SC, Source Han Sans CN, serif;
        }
        .el-descriptions {
            box-sizing: border-box;
            font-size: 12px;
            color: #303133;
            width: 100%;
            margin: 50px auto;
        }
        .myTable {
            width: 100%;
            border: 1px solid #9BE6D6;
            text-align: left;
        }

        /*设置页眉页脚*/
        @page  {
            size: A4 portrait; /*设置纸张大小：A4(210mm 297mm) 横向则反过来*/
            @bottom-center {
                content: "OneFlow 版权所有";
                font-family: SimSun, serif;
                font-size: 12px;
                color: #000;
            };
            @top-left {
                content: url("https://www.baidu.com");
                background-repeat: no-repeat;
                background-position: left top;
            };
            @bottom-right {
                content: "第" counter(page) "页 共" counter(pages) "页";
                font-family: SimSun, serif;
                font-size: 12px;
                color: #000;
            }
        }
    </style>
</head>

<body>
<div class="el-descriptions">
    <div>基础信息</div>

    <#-- 分页符 -->
    <div style="page-break-after: always"></div>

    <div>审批历史</div>

</div>

</body>
</html>
