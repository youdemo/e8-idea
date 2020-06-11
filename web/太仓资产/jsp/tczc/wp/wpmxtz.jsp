<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="weaver.general.Util"%>
<%@ page import="java.util.*,weaver.hrm.appdetach.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.*"%>
<%@ page import="weaver.hrm.resource.ResourceComInfo" %>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="rs_dt" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="ResourceComInfo" class="weaver.hrm.resource.ResourceComInfo" scope="page" />


<jsp:useBean id="RecordSet" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="AccountType" class="weaver.general.AccountType" scope="page" />
<jsp:useBean id="LicenseCheckLogin" class="weaver.login.LicenseCheckLogin" scope="page" />
<html>

<head>
    <meta name="viewport" content="width=device-width" charset="UTF-8"/>
    <title>物品台账明细</title>

    <link href="/tczc/js/bootstrap-3.3.7-dist/css/bootstrap.css" rel="stylesheet"/>
    <link href="/tczc/js/bootstrap-3.3.7-dist/css/bootstrap-datetimepicker.min.css" rel="stylesheet"/>
    <link href="/tczc/js/bootstrap-3.3.7-dist/css/bootstrap-table.min.css" rel="stylesheet"/>

    <style>
        .table{
            table-layout: fixed;
        }
        .table th, .table td {
            max-width: 300px;
            text-align: center;
            vertical-align: middle !important;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .colStyle {
            text-overflow: ellipsis;
            overflow: hidden;
            white-space: nowrap;
        }
    </style>
</head>

<body>
<div class="panel-body" style="padding-bottom:0;" id="search">
    <div id="toolbar" class="btn-group">
       <button id="btn_add" type="button" class="btn btn-default" data-toggle="modal" data-target="#modalTable">
            高级搜索
        </button>
    </div>
    <table id="reportInfo" class="table text-nowrap" style="word-break:break-all; word-wrap:break-word;font-size:12px;">

    </table>

</div>

<div class="modal fade" id="modalTable" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">查询条件</h4>
            </div>
            <div class="modal-body">
                <div class="panel panel-default" id="advancedSearchDiv">
                    <!--                    <div class="panel-heading">查询条件</div>-->
                    <div class="container">
                        <form id="formSearch" class="form-horizontal">
                            <div class="row" style="margin-top: 15px;margin-bottom: 15px;">
                                <div class="col-sm-6">
                                    <div class="input-group">
                                        <span class="input-group-addon"
                                              style="width: 100px;text-align: left">年份</span>
                                        <input type="text" class="form-control" id="nf" style="width: 300px;">
                                    </div>
                                </div>
                            </div>
                            <div class="row" style="margin-top: 15px;margin-bottom: 15px;">
                                <div class="col-sm-6">
                                    <div class="input-group">
                                        <span class="input-group-addon"
                                              style="width: 100px;text-align: left">月份</span>
                                        <!-- 单选 -->
                                        <select name="yf" id="yf"
                                                class="form-control"
                                                style="width: 300px;">
                                            <option value=""></option>
                                            <option value="01">1月</option>
                                            <option value="02">2月</option>
                                            <option value="03">3月</option>
                                            <option value="04">4月</option>
                                            <option value="05">5月</option>
                                            <option value="06">6月</option>
                                            <option value="07">7月</option>
                                            <option value="08">8月</option>
                                            <option value="09">9月</option>
                                            <option value="10">10月</option>
                                            <option value="11">11月</option>
                                            <option value="12">12月</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <!--                            <div class="col-sm-12">-->
                            <!--                                <div class="input-group">-->
                            <!--                                    <button type="button" id="btn_query" class="btn btn-primary">查询</button>-->
                            <!--                                </div>-->
                            <!--                            </div>-->
                        </form>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" id="btn_query" class="btn btn-default" data-dismiss="modal">查询</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<script src="/tczc/js/bootstrap-3.3.7-dist/js/DateTimeUtil.js"></script>
<script src="/tczc/js/jquery-3.3.1/jquery-3.3.1.min.js"></script>
<script src="/tczc/js/bootstrap-3.3.7-dist/js/tableExport.js"></script>
<script src="/tczc/js/bootstrap-3.3.7-dist/js/bootstrap.js"></script>
<script src="/tczc/js/bootstrap-3.3.7-dist/js/bootstrap-datetimepicker.js"></script>
<script src="/tczc/js/bootstrap-3.3.7-dist/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="/tczc/js/bootstrap-3.3.7-dist/js/bootstrap-table.js"></script>
<script src="/tczc/js/bootstrap-3.3.7-dist/js/bootstrap-table-zh-CN.min.js"></script>
<script src="/tczc/js/bootstrap-3.3.7-dist/js/bootstrap-table-export.js"></script>
<script src="/tczc/js/bootstrap-3.3.7-dist/js/xlsx.core.min.js"></script>
<script src="/tczc/js/bootstrap-3.3.7-dist/js/FileSaver.min.js"></script>
<script language="javascript">
    $('.form_date').datetimepicker({
        language: 'zh-CN',
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0
    })

    $(function () {
        var monthSDate = "";//getMonthStartDate()
        jQuery('#fromDate').val(monthSDate)
        // 1.初始化Table
        var oTable = new TableInit()
        oTable.Init()

        // 2.初始化Button的点击事件
        var oButtonInit = new ButtonInit()
        oButtonInit.Init()
    })

    var TableInit = function () {
        var oTableInit = new Object()
        // 初始化Table
        oTableInit.Init = function () {
            $('#reportInfo').bootstrapTable({
                url: '/tczc/wp/getwmpxtzdatas.jsp', // 请求后台的URL（*）
                method: 'get', // 请求方式（*）
                toolbar: '#toolbar', // 工具按钮用哪个容器
                striped: true, // 是否显示行间隔色
                cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                pagination: true, // 是否显示分页（*）
                sortable: true, // 是否启用排序
                sortOrder: 'asc', // 排序方式
                showExport: true, // 是否显示导出按钮
                exportDataType: "all",
                exportTypes:['xlsx'],
                exportOptions:{
                    //ignoreColumn: [0,0],            //忽略某一列的索引
                    fileName: '物品明细台账',              //文件名称设置
                    worksheetName: 'Sheet1',          //表格工作区名称
                    tableName: '物品明细台账',
                    excelstyles: ['background-color', 'color', 'font-size', 'font-weight'],
                    //onMsoNumberFormat: DoOnMsoNumberFormat
                },
                queryParams: oTableInit.queryParams, // 传递参数（*）
                sidePagination: 'server', // 分页方式：client客户端分页，server服务端分页（*）
                dataField: "rows",
                pageNumber: 1, // 初始化加载第一页，默认第一页
                pageSize: 10, // 每页的记录行数（*）
                pageList: [10, 25, 50, 100], // 可供选择的每页的行数（*）
                search: false, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
                strictSearch: false, // 设置为 true启用 全匹配搜索，否则为模糊搜索
                showColumns: true, // 是否显示所有的列
                showRefresh: true, // 是否显示刷新按钮
                minimumCountColumns: 2, // 最少允许的列数
                clickToSelect: true, // 是否启用点击选中行
                height: 350, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
                uniqueId: 'id', // 每一行的唯一标识，一般为主键列
                showToggle: false, // 是否显示详细视图和列表视图的切换按钮
                cardView: false, // 是否显示详细视图
                detailView: false, // 是否显示父子表
                columns: [
                    [
                        {
                            field: 'wpmc',
                            title: '名称',
                            visible: true, // false表示不显示
                            width: 100,
                            rowspan: 2

                        }, {
                        field: 'xh',
                        title: '规格',
                        visible: true, // false表示不显示
                        width: 200,
                        rowspan: 2
                    }, {
                        field: 'dw',
                        title: '计量单位',
                        visible: true, // false表示不显示
                        width: 100,
                        rowspan: 2
                    }, {
                        field: '',
                        title: '上期结存',
                        sortable: false, // 启用排序
                        align: 'center',
                        width:320,
                        valign: 'middle',
                        colspan:4
                    },{
                        field: '',
                        title: '本期购入',
                        sortable: false, // 启用排序
                        align: 'center',
                        width:320,
                        valign: 'middle',
                        colspan:4
                    }, {
                        field: '',
                        title: '本期领用',
                        sortable: false, // 启用排序
                        align: 'center',
                        width:320,
                        valign: 'middle',
                        colspan:4
                    }, {
                        field: '',
                        title: '期末结存',
                        sortable: false, // 启用排序
                        align: 'center',
                        width:320,
                        valign: 'middle',
                        colspan:4
                    }, {
                        field: 'sl',
                        title: '税率',
                        visible: true, // false表示不显示
                        width: '74px',
                        rowspan: 2
                    }],
                    [
                        {
                            field: 'sqjcdj',
                            title: '单价',
                            width: 80
                        }, {
                        field: 'sqjcsl',
                        title: '数量',
                        width: 80
                    }, {
                        field: 'sqjcxj',
                        title: '小计',
                        width: 80
                    }, {
                        field: 'sqjcbhs',
                        title: '不含税',
                        width: 80
                    }, {
                        field: 'bqgrdj',
                        title: '单价',
                        width: 80
                    }, {
                        field: 'bqgrsl',
                        title: '数量',
                        width: 80
                    }, {
                        field: 'bqgrxj',
                        title: '小计',
                        width: 80
                    }, {
                        field: 'bqgrbhs',
                        title: '不含税',
                        width: 80
                    }, {
                        field: 'bqlydj',
                        title: '单价',
                        width: 80
                    }, {
                        field: 'bqlysl',
                        title: '数量',
                        width: 80
                    }, {
                        field: 'bqlyxj',
                        title: '小计',
                        width: 80
                    }, {
                        field: 'bqlybhs',
                        title: '不含税',
                        width: 80
                    }, {
                        field: 'bqjcdj',
                        title: '单价',
                        width: 80
                    }, {
                        field: 'bqjcsl',
                        title: '数量',
                        width: 80
                    }, {
                        field: 'bqjcxj',
                        title: '小计',
                        width: 80
                    }, {
                        field: 'bqjcbhs',
                        title: '不含税',
                        width: 80
                    }
                    ]
                ],onLoadSuccess: function () {
                },
                onLoadError: function () {
                    showTips("数据加载失败！");
                },
            })
        }

        // 得到查询的参数
        oTableInit.queryParams = function (params) {
            var nf = $('#nf').val();
            var yf = $('#yf').val();
            var temp = { // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                limit: params.limit, // 页面大小
                offset: params.offset, // 页码
                sort: params.sort,      //排序列名
                sortOrder: params.order, //排位命令（desc，asc）
                nf:nf,
                yf:yf
            }

            // console.log('fromDate:' + temp.fromDate)
            // console.log('toDate:' + temp.toDate)
            return temp
        }
        return oTableInit
    }

    var ButtonInit = function () {
        var oInit = new Object()
        var postdata = {}

        oInit.Init = function () {
            // 初始化页面上面的按钮事件
        }

        return oInit
    }

    function rowStyle (value, row, index) {
        var classes = ['active', 'success', 'info', 'warning', 'danger']
        if (index % 2 === 0 && index / 2 < classes.length) {
            return {
                // classes: classes[index / 2]
                css: {
                    'background-color': 'red'
                }
            }
        }
        return {}
    }

    function paramsMatter (value, row, index) {
        var span = document.createElement('span')
        span.setAttribute('title', value)
        span.innerHTML = value
        return span.outerHTML
    }

    var $table = $('#table')

    $(function () {
        $('#modalTable').on('shown.bs.modal', function () {
            $table.bootstrapTable('resetView')
        })
    })

    $('#btn_query').click(function () {
        // 不带参数，仅刷新
        $('#reportInfo').bootstrapTable('refresh')
    })
</script>
</body>

</html>