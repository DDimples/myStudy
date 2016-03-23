<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <title>${title!""}</title>
    <link rel="icon" href="/resources/img/favicon.ico">
    <link href="/resources/static/bootstrap-3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/static/dataTables/1.10.11/dataTables.bootstrap.css" rel="stylesheet">

    <style type="text/css">
        body {
            padding-top: 70px;
        }
        .table-hover tbody tr:hover td, .table-hover tbody tr:hover th {
            background-color: #87CEFA;
        }
    </style>
<@block name="css"></@block>
</head>
<body>
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Bootstrap theme</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="#">Home</a></li>
                <li><a href="#about">About</a></li>
                <li><a href="#contact">Contact</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Dropdown <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">Action</a></li>
                        <li><a href="#">Another action</a></li>
                        <li><a href="#">Something else here</a></li>
                        <li role="separator" class="divider"></li>
                        <li class="dropdown-header">Nav header</li>
                        <li><a href="#">Separated link</a></li>
                        <li><a href="#">One more separated link</a></li>
                    </ul>
                </li>
            </ul>
            <div>
                <form class="navbar-form navbar-left" role="search">
                    <div class="form-group">
                        <input type="text" class="form-control" placeholder="Search">
                    </div>
                    <button type="submit" class="btn btn-default">Submit</button>
                </form>
            </div>
            <p class="navbar-text pull-right">
                Logged in as <a href="#" class="navbar-link">Username</a>
            </p>
        </div><!--/.nav-collapse -->

    </div>
</nav>

<table id="example" class="table table-striped table-hover table-bordered col-md-9" cellspacing="0" width="100%">
    <thead>
    <tr>
        <th>Name</th>
        <th>Position</th>
        <th>Office</th>
        <th>Start date</th>
        <th>Salary</th>
    </tr>
    </thead>
    <tfoot>
    <tr>
        <th>Name</th>
        <th>Position</th>
        <th>Office</th>
        <th>Start date</th>
        <th>Salary</th>
    </tr>
    </tfoot>
</table>


<@block name="main"></@block>

<#--========MVC  end=======-->

<@block name="js"></@block>
<@block name="jsb"></@block>

<script src="/resources/static/jquery/jquery-1.12.0.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="/resources/static/bootstrap-3.3.5/js/bootstrap.js"></script>
<script src="/resources/static/dataTables/1.10.11/jquery.dataTables.js"></script>
<script src="/resources/static/dataTables/1.10.11/dataTables.bootstrap.js"></script>
<script type="application/javascript">
    $(document).ready(function() {
        $('#example').dataTable( {
            "processing": true,
            "searchable": false,
            "serverSide": true,
            "ajax": {
                "url": "/bootstrap/getTableData",
                "type": "POST"
            },
            "columns": [
                { "data": "name" },
                { "data": "position" },
                { "data": "office" },
                { "data": "start_date" },
                { "data": "salary" }
            ]
        } );
    } );
</script>
</body>
</html>