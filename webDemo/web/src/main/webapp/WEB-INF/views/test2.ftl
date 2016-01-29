<@override name="include">
    <#include "partpage.ftl" />
</@override>

<@override name="main">
    <h1>test main 测试~~~</h1>
    <button type="button" class="btn btn-success">Success</button>
    <@partpage test>
    </@partpage>
</@override>

<@override name="css">
<link href="/resources/static/bootstrap-3.3.5/css/bootstrap.css" rel="stylesheet">
</@override>

<@extends name="/common/_layout.ftl"/>