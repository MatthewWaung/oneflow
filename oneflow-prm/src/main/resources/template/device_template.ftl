<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UFT-8">
    <title>网络拓扑结构表</title>
    <style>

        .vertical-line {
            height: 26px;
            border-right: 2px solid #d0d0d0;
            float: left;
            margin-top: -10px;
            margin-left: -15px;
        }

        .horizontal-line {
            width: 15px;
            border-top: 2px solid #d0d0d0;
            float: left;
            margin-top: 16px;
            margin-left: -15px;
        }
    </style>
</head>

<style type="text/css">
    table {
        border-collapse: collapse;
        margin: 0 auto;
        text-align: center;
    }

    .table td,
    .table th {
        border: 1px solid #cad9ea;
        color: #666;
        height: 30px;
    }

    .table thead th {
        background-color: #cce8eb;
        width: 100px;
    }

    .table tr:nth-child(odd) {
        background: #fff;
    }

    .table tr:nth-child(even) {
        background: #f5fafa;
    }

    .tableB th {
        border: 1px solid #cad9ea;
        color: #666;
        height: 30px;
    }

    .tableB thead th {
        background-color: #e7f1ef;
        width: 100px;
    }
</style>

<!-- Table goes in the document BODY -->

<body>
<div>

    <h2>参考此文章的代码和模板：https://www.jianshu.com/p/07e28e2ec562</h2>

    <table width="90%" class="table">
        <caption>
            <h2>网络拓扑结构表</h2>
        </caption>
        <thead>
        <tr>
            <th>设备名称</th>
            <th>符号</th>
            <th>端口</th>
            <th>地址</th>
            <th>网络中的单元</th>
        </tr>
        </thead>
        <tbody>

        <#if topDeviceList??>
            <#list topDeviceList as top_device>
                <tr>
                    <td><#if top_device.deviceName??>${top_device.deviceName}</#if></td>
                    <td><#if top_device.mark??>${top_device.mark}</#if></td>
                    <td><#if top_device.port??>${top_device.port}</#if></td>
                    <td><#if top_device.ip??>${top_device.ip}</#if></td>
                    <td><#if top_device.unit??>${top_device.unit}</#if></td>
                </tr>
            </#list>
        </#if>

        </tbody>
    </table>
</div>
</body>
</html>
