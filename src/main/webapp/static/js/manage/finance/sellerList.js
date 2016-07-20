/*************
	FinSellerInfo
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="viewDetailList">详情</a>&nbsp;&nbsp;');
    return html.join('');
};

// 列集合
var cols = [
    { title:'分销商账号', name:'sellerAccount', align:'left'},        
	{ title:'店铺编码', name:'shopName', width:90, align:'left'},
	{ title:'分销商姓名', name:'sellerName', align:'left'}, 
	{ title:'注册时间', name:'registerTime', align:'left', width: 126, renderer: YouGou.Util.timeFixed},
	{ title:'最近交易时间', name:'latelyTransactionTime', align:'left', width: 126, renderer: YouGou.Util.timeFixed},
	{ title:'账户余额', name:'accountBalance', align:'right',renderer:function(val,item,rowIndex){
		var accountBalance = item.accountBalance;
		if(accountBalance !='' && accountBalance !=null){
			return "<font color='red'>"+accountBalance+"<font>"
		}else{
			return "<font color='red'>0<font>"
		}
	}},
	{ title:'累计提现总额', name:'cashedTotalAmount', align:'right',renderer:function(val,item,rowIndex){
		return "<font color='red'>"+item.cashedTotalAmount+"<font>"
	}},
	{ title:'提现中总额', name:'cashingTotalAmount', align:'right',renderer:function(val,item,rowIndex){
		return "<font color='red'>"+item.cashingTotalAmount+"<font>"
	}},
	{ title:'佣金收入总额', name:'commissionAllTotalAmount', align:'right',renderer:function(val,item,rowIndex){
		return "<font color='red'>"+item.commissionAllTotalAmount+"<font>"
	}},
	{ title:'操作', name:'' ,width:80, align:'center',lockWidth:true, renderer: actionFixed},
    { title:'ID', name:'id', hidden: true}
];

//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");

// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/finance/querySellerData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    //查看详情列表
    if (action == "viewDetailList") {
    	queryDetailList(item.id);
    }
    e.stopPropagation();  //阻止事件冒泡
});

/**
 * 查看详情列表
 */
function queryDetailList(sellerId) {
	YouGou.UI.progressLoading();
	$.ajax({
		type: "post",
        url: '/finance/sellerDetailList.sc',
        data: {"id": sellerId},
        async: true,
        success: function (data) {
        	YouGou.UI.progressStop();
        	YouGou.UI.Dialog.show({
        		title: "账户余额明细",
        		closable: true,
        		closeByBackdrop: false,
        		cssClass: 'message-info-list',
        		message : data.replace(/(\n)+|(\r\n)+/g, ""),
        		buttons: [
        		    {
        		    	label: '关闭',
        		    	cssClass: 'btn-yougou',
 		                action: function (dialog) {
 		                	dialog.close();
 		                }
        		    }
	            ]
        	});
        }
	});
}

//查询
function doQuery(){
	if ($('#amountCheck')[0].checked) {
		$('#amountCheckFlag').val('TRUE');
	} else {
		$('#amountCheckFlag').val('');
	}
	$("#searchForm").attr("action", "/finance/sellerList.sc");
	$("#searchForm").submit();
}
