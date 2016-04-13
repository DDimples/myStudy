/**
 * Created by chengxiang on 16/3/27.
 */

var WebTable = function(){

    var initTable = function(options){

        var table = $("#"+options.tableId);
        var processing = options.processing==null?true:options.processing;
        var searching = options.searching==null?false:options.searching;
        var rowId = options.rowId;
        var url = options.url;
        var order = options.order;
        var columnDefs = options.columnDefs;
        var columns = options.columns;



        var dataTable = table.DataTable({
            processing: processing,
            searching: searching,
            serverSide: true,
            rowId:rowId,
            ajax: {
                "url": url,
                "type": "POST"
            },
            "order": order,
            "columnDefs": columnDefs,
            columns: columns,
            select: {
                style:    'os',
                selector: 'td:first-child'
            },//select:true,
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
        });
        return dataTable;
    }

    var initEdit = function(options){

    }
    return {
        initTable:function(options) {
            return initTable(options);
        }
    }
}();
