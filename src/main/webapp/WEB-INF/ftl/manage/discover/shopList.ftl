<form class="form-horizontal" id="commodityForm">
    <fieldset>
        <div class="row search-box" style="margin: 0">
            <div class="form-group" style="margin: 10px 0">
                <div class="col-sm-3">
                    <label class="control-label no-padding-right">分销商账号：</label><input
                    class="form-control input-sm inline" name="loginName" id="loginName" value="" type="text">
                </div>
                <div class="col-sm-3">
                    <label class="control-label no-padding-right">店铺名称：</label><input
                    class="form-control input-sm inline" name="name" id="name" value="" type="text">
                </div>
                <div class="col-sm-3">
                    <label class="control-label no-padding-right">分销商姓名：</label><input
                        class="form-control input-sm inline" name="sellerName" id="sellerName" value="" type="text">
                </div>
                <div class="col-sm-3">
                    <label class="control-label no-padding-right">店铺编号：</label><input
                    class="form-control input-sm inline" name="shopCode" id="shopCode" value="" type="text">
                </div>
                <div class="col-sm-12 mt10">
                    <span class="ml10"></span>
                    <input class="btn btn-sm btn-yougou ml50" id="shopQuery" type="button" value="搜索">
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
                        title: '分销商帐号',
                        name: 'loginName',
                        align: 'center',
                        sortable: true,
                        width: 120,
                        lockWidth: true
                    },
                    {title: '店铺名称', name: 'name', align: 'center', sortable: true},
                    {title: '分销商姓名', name: 'sellerName', align: 'center', sortable: true, width: 120, lockWidth: true},
                    {
                        title: '店铺编号',
                        name: 'shopCode',
                        align: 'center',
                        sortable: true,
                        width: 120,
                        lockWidth: true
                    }
                ],
                mmgrid = $('#mmgrid_table').mmGrid({//表格控件渲染
                    height: 'auto',
                    multiSelect: false,//是否可以多选
                    checkCol: true,//是否显示复选框
                    cols: cols,
                    url: '/shop/queryArticleShopData.sc',
                    sortName: "time",//默认排序字段
                    sortStatus: 'desc',//排序方向
                    fullWidthRows: true,//第一次加载表格时，列充满表格自动
                    remoteSort: true,
                    method: 'get',
                    plugins: [mmPaginator,mmFormParams]
                });
                
	            $('#shopQuery').click(function(){
	            	mmgrid.load();
	            });
            }

            $(function () {
                Init();
            });
        }()
    );
</script>