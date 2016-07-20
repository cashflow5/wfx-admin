<form class="form-horizontal" id="commodityForm">
    <fieldset>
        <div class="row search-box" style="margin: 0">
            <div class="form-group" style="margin: 10px 0">
                <div class="col-sm-3">
                    <label class="control-label no-padding-right">品牌名称：</label><input
                        class="form-control input-sm inline" name="brandName" id="brandName" value="" type="text">
                </div>
                <div class="col-sm-3">
                    <label class="control-label no-padding-right">商品名称：</label><input
                        class="form-control input-sm inline" name="commodityName" id="commodityName" value="" type="text">
                </div>
                <div class="col-sm-3">
                    <label class="control-label no-padding-right">商品款号：</label><input
                        class="form-control input-sm inline" name="styleNo" id="styleNo" value="" type="text">
                </div>
                <div class="col-sm-3">
                    <label class="control-label no-padding-right">款色编码：</label><input
                        class="form-control input-sm inline" name="supplierCode" id="supplierCode" value="" type="text">
                </div>
                <div class="col-sm-3">
                    <label class="control-label no-padding-right">款色ID：</label><input
                        class="form-control input-sm inline" name="no" id="no" value="" type="text">
                </div>
                <div class="col-sm-12 mt10">
                    <span class="ml10"></span>
                    <input class="btn btn-sm btn-yougou ml50" id="searchBtn" type="button" value="搜索">
                </div>
            </div>
        </div>
    </fieldset>
</form>
<table id="mmgrid_table" class="mmgrid">
</table>
<div id="pager" style="text-align:right;" class=""></div>
<script>
    (function () {
            /*
             * 初始化函数
             **/
            function Init() {
                mmgridTable();//表格控件渲染函数
            }

            /*
             * 表格控件函数
             **/
            function mmgridTable() {
	            //分页器
				var mmPaginator = $('#pager').mmPaginator({});
				// 搜索表单属性
				var mmFormParams = new MMSearchFormParams("commodityForm");
				
                var cols = [
                    {
                        title: '商品图片',
                        name: 'picSmall',
                        align: 'center',
                        sortable: true,
                        width: 60,
                        lockWidth: true,
                        renderer: function (val) {
                            return '<img src="' + val + '" width="50" />';
                        }
                    },
                    {title: '商品名称', name: 'commodityName', align: 'center', sortable: true},
                    {title: '分类名称', name: 'catName', align: 'center', sortable: true, width: 60, lockWidth: true},
                    {
                        title: '品牌名称',
                        name: 'brandName',
                        align: 'center',
                        sortable: true,
                        width: 60,
                        lockWidth: true
                    },
                    {
                        title: '商品款号',
                        name: 'styleNo',
                        align: 'center',
                        sortable: false,
                        width: 90,
                        lockWidth: true
                    },
                    {
                        title: '款色编码',
                        name: 'supplierCode',
                        align: 'center',
                        sortable: true,
                        width: 90,
                        lockWidth: true
                    },
                    {title: '款色ID', name: 'no', align: 'center', sortable: true, width: 70, lockWidth: true}
                ],
                mmgrid = $('#mmgrid_table').mmGrid({//表格控件渲染
                    height: 'auto',
                    multiSelect: false,//是否可以多选
                    checkCol: true,//是否显示复选框
                    cols: cols,
                    url: '/commodity/queryDisStyleInfo.sc',
                    sortName: "time",//默认排序字段
                    sortStatus: 'desc',//排序方向
                    fullWidthRows: true,//第一次加载表格时，列充满表格自动
                    remoteSort: true,
                    method: 'get',
                    plugins: [mmPaginator,mmFormParams]
                });
            
	            $('#searchBtn').click(function(){
	            	mmgrid.load();
	            });
            }

            $(function () {
                Init();
            });
        }()
    );
</script>