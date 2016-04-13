<@block name="layout"></@block>
<@block name="include"></@block>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <title>${title!""}</title>
    <link rel="icon" href="/resources/img/favicon.ico">
    <link href="/resources/static/bootstrap-3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/static/dataTables/DataTables-1.10.11/css/dataTables.bootstrap.css" rel="stylesheet">
    <link href="/resources/static/dataTables/Buttons-1.1.2/css/buttons.bootstrap.css" rel="stylesheet">
    <link href="/resources/static/dataTables/Select-1.1.2/css/select.bootstrap.css" rel="stylesheet">
    <link href="/resources/static/dataTables/Editor-1.5.5/css/editor.bootstrap.css" rel="stylesheet">
    <@block name="css"></@block>
</head>
<body>
<#--========页头=========-->
<!-- Static navbar -->
<#include "/common/head.ftl"/>
<@head></@head>
<#--========MVC主展示页面========-->
<@block name="main"></@block>

<#--========MVC  end=======-->

<@block name="jsb"></@block>

<#--========MVC主展示页面end========-->
<#--=========页脚==========-->
<#if channelfooter>
${channelfooter}
<#else>
<#--<#include "/common/footer_default_zh_cn.ftl" />-->
<#--<@footer></@footer>-->
</#if>

<#if DebugUtil.isDebug()>

<#--test debug-->
footer debug

</#if>
<script src="/resources/static/jquery/jquery-1.12.0.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="/resources/static/bootstrap-3.3.5/js/bootstrap.js"></script>
<script src="/resources/static/dataTables/DataTables-1.10.11/js/jquery.dataTables.js"></script>
<script src="/resources/static/dataTables/DataTables-1.10.11/js/dataTables.bootstrap.js"></script>
<script src="/resources/static/dataTables/Buttons-1.1.2/js/dataTables.buttons.js"></script>
<script src="/resources/static/dataTables/Buttons-1.1.2/js/buttons.bootstrap.js"></script>
<script src="/resources/static/dataTables/Select-1.1.2/js/dataTables.select.js"></script>
<script src="/resources/static/dataTables/Editor-1.5.5/js/dataTables.editor.js"></script>
<script src="/resources/static/dataTables/Editor-1.5.5/js/editor.bootstrap.js"></script>
<@block name="js"></@block>
</body>
</html>