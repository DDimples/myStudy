<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <title>${title!""}</title>
    <link rel="icon" href="/resources/img/favicon.ico">
    <link href="/resources/static/bootstrap-3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/static/offcanvas/offcanvas.css" rel="stylesheet">

<@block name="css"></@block>
</head>
<body role="document">
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

    <div class="container">
        <div class="row row-offcanvas row-offcanvas-left">
            <div class="col-xs-6 col-sm-3 sidebar-nav">
                <div class="list-group">
                    <a href="#" class="list-group-item active" data-toggle="offcanvas">link</a>
                    <a href="#" class="list-group-item" data-toggle="offcanvas">Link</a>
                    <a href="#" class="list-group-item" data-toggle="offcanvas">Link</a>
                    <a href="#" class="list-group-item" data-toggle="offcanvas">Link</a>
                    <a href="#" class="list-group-item" data-toggle="offcanvas">Link</a>
                    <a href="#" class="list-group-item" data-toggle="offcanvas">Link</a>
                </div>
            </div>
            <div class="col-xs-12 col-sm-9">
                <div class="jumbotron">
                    <h1>Hello, world!</h1>
                    <p>This is a template for a simple marketing or informational website. It includes a large callout called the hero unit and three supporting pieces of content. Use it as a starting point to create something more unique.</p>
                    <p><a href="#" class="btn btn-primary btn-large">Learn more »</a></p>
                </div>
                <div class="row">
                    <div class="col-xs-6 col-lg-4">
                        <h2>Heading</h2>
                        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
                        <p><a class="btn" href="#">View details »</a></p>
                    </div><!--/span-->
                    <div class="col-xs-6 col-lg-4">
                        <h2>Heading</h2>
                        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
                        <p><a class="btn" href="#">View details »</a></p>
                    </div><!--/span-->
                    <div class="col-xs-6 col-lg-4">
                        <h2>Heading</h2>
                        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
                        <p><a class="btn" href="#">View details »</a></p>
                    </div><!--/span-->
                </div>
                <div class="row">
                    <div class="col-xs-6 col-lg-4">
                        <h2>Heading</h2>
                        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
                        <p><a class="btn" href="#">View details »</a></p>
                    </div><!--/span-->
                    <div class="col-xs-6 col-lg-4">
                        <h2>Heading</h2>
                        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
                        <p><a class="btn" href="#">View details »</a></p>
                    </div><!--/span-->
                    <div class="col-xs-6 col-lg-4">
                        <h2>Heading</h2>
                        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
                        <p><a class="btn" href="#">View details »</a></p>
                    </div><!--/span-->
                </div>
            </div>
        </div>
        <hr>
        <footer>
            <p>© 2016 myStudy, Inc.</p>
        </footer>
    </div>

    <@block name="main"></@block>

    <#--========MVC  end=======-->

    <@block name="js"></@block>
    <@block name="jsb"></@block>



    <script src="/resources/static/jquery/jquery-1.12.0.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="/resources/static/bootstrap-3.3.5/js/bootstrap.js"></script>
    <script src="/resources/static/offcanvas/offcanvas.js"></script>
</body>
</html>