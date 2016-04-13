<@block name="layout"></@block>
<@block name="include"></@block>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <title>${title!""}</title>
    <link rel="icon" href="/resources/img/favicon.ico">
    <link href="/resources/static/bootstrap-3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <style type="text/css">
        body {
            min-height: 2000px;
            padding-top: 70px;
        }

    </style>
<@block name="css"></@block>
</head>
<body role="document">
<#--========页头=========-->
<!-- Static navbar -->
<#include "head.ftl"/>
<@head></@head>
<#--========MVC主展示页面========-->
<@block name="main"></@block>

<#--========MVC  end=======-->

<@block name="js"></@block>
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
</body>
</html>