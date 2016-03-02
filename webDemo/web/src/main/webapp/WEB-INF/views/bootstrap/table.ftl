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
        .sidebar-nav {
            padding: 9px 0;
        }

        /*
 * Main content
 */

        .main {
            padding: 20px;
        }
        @media (min-width: 768px) {
            .main {
                padding-right: 40px;
                padding-left: 40px;
            }
        }
        .main .page-header {
            margin-top: 0;
        }

        @media (max-width: 980px) {
            /* Enable use of floated navbar text */
            .navbar-text.pull-right {
                float: none;
                padding-left: 5px;
                padding-right: 5px;
            }
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
            <p class="navbar-text pull-right">
                Logged in as <a href="#" class="navbar-link">Username</a>
            </p>
        </div><!--/.nav-collapse -->

    </div>
</nav>

<div class="row">
    <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main table-responsive ">
        <table class="table table-bordered table-striped">

            <thead>
            <tr>
                <th>#</th>
                <th>Column heading</th>
                <th>Column heading</th>
                <th>Column heading</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>1</td>
                <td>column heading1</td>
                <td>column heading1</td>
                <td>column heading1</td>
            </tr>
            <tr>
                <td>1</td>
                <td>column heading1</td>
                <td>column heading1</td>
                <td>column heading1</td>
            </tr>
            <tr>
                <td>1</td>
                <td>column heading1</td>
                <td>column heading1</td>
                <td>column heading1</td>
            </tr>
            <tr>
                <td>1</td>
                <td>column heading1</td>
                <td>column heading1</td>
                <td>column heading1</td>
            </tr>
            </tbody>

        </table>
    </div>
</div>




<@block name="main"></@block>

<#--========MVC  end=======-->

<@block name="js"></@block>
<@block name="jsb"></@block>



<script src="/resources/static/jquery/jquery-1.12.0.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="/resources/static/bootstrap-3.3.5/js/bootstrap.js"></script>
</body>
</html>