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
<div class="container bs-docs-container">

    <div class="row">
        <div class="col-md-9" role="main">
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

        </div>


    </div>
</div>


<@block name="main"></@block>

<#--========MVC  end=======-->

<@block name="js"></@block>
<@block name="jsb"></@block>

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
<script type="application/javascript">

    $(document).ready(function() {

        editor = new $.fn.dataTable.Editor( {
            ajax: {
                "url": "/bootstrap/updateTableData",
                "type": "POST"
            },
            idSrc:  'id',
            table: "#example",
            i18n: {
                create: {
                    button: "新增",
                    title:  "新增",
                    submit: "提交"
                },
                edit: {
                    button: "修改",
                    title:  "修改",
                    submit: "提交"
                },
                remove: {
                    button: "删除",
                    title:  "删除",
                    submit: "确认",
                    confirm: {
                        _: "确认要删除 %d 行?",
                        1: "您确认要删除 1 行?"
                    }
                },
                error: {
                    system: "Une erreur s’est produite, contacter l’administrateur système"
                },
                multi: {
                    title: "确认",
                    info: "multi????",
                    restore: "Annuler les modifications"
                },
                datetime: {
                    previous: 'pre',
                    next:     'next',
                    months:   [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
                    weekdays: [ '星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六' ]
                }
            },
            fields: [ {
                label: "姓名:",
                name: "name"
            }, {
                label: "职位:",
                name: "position"
            }, {
                label: "公司:",
                name: "office"
            }, {
                label: "开始日期:",
                name: "start_date",
                type: 'datetime'
            }, {
                label: "薪水:",
                name: "salary"
            }
            ]

        } );

        var table = $('#example').DataTable( {
            processing: true,
            searching: false,
            serverSide: true,
            ajax: {
                "url": "/bootstrap/getTableData",
                "type": "POST"
            },
            "order": [[ 3, 'asc' ], [ 4, 'desc' ]],
            "columnDefs": [
                { "orderable": false, "targets": 0 }
            ],
            columns: [
                { "data": "name" },
                { "data": "position"},
                { "data": "office","orderable":false },
                { "data": "start_date" },
                { "data": "salary" }
            ],
            select: true,
            language: {
                "sProcessing": "处理中...",
                "sLengthMenu": "显示 _MENU_ 项结果",
                "sZeroRecords": "没有匹配结果",
                "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
                "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                "sInfoPostFix": "",
                "sSearch": "搜索:",
                "sUrl": "",
                "sEmptyTable": "表中数据为空",
                "sLoadingRecords": "载入中...",
                "sInfoThousands": ",",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "上页",
                    "sNext": "下页",
                    "sLast": "末页"
                },
                "oAria": {
                    "sSortAscending": ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            }
        } );
        // Display the buttons
        new $.fn.dataTable.Buttons( table, [
            { extend: "create", editor: editor },
            { extend: "edit", editor: editor },
            { extend: "remove", editor: editor }
        ] );

        table.buttons().container()
                .appendTo( $('.col-sm-6:eq(0)', table.table().container() ) );
    } );
</script>
</body>
</html>