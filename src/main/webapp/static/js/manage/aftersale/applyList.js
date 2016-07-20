/*************
	OrderApply
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var manualRefundFlag = item.manualRefundFlag;
	var html = [];
	html.push('&nbsp;<a href="javascript:void(0);" action="apply">申请</a>&nbsp;');
    return html.join('');
};

var orderFixed = function(val,item){
	var html = '';
	html+= '<a href="javascript:void(0);" onclick="viewInfo(\''+item.id+'\');" >'+val+'</a>';
	
	return html;
}

var statusFixed = function(val){
	var statusStr = '';
	if(val == 'TRADE_SUCCESS'){
		statusStr = '交易成功';
	}
	return statusStr;
}

var payFixed = function(val){
	var payStr = '';
	if(val == 'alipay'){
		payStr = '在线支付（支付宝）';
	}else if(val == 'wechatpay'){
		payStr = '在线支付（微信）';
	}
	return payStr;
}

// 列集合
var cols = [
	{ title:'订单号', name:'wfxOrderNo', align:'center',renderer:orderFixed},
	{ title:'订单来源', name:'shopName', align:'left'},
	{ title:'收货人', name:'receiverName', align:'left'},
	{ title:'支付方式', name:'payType', align:'left',renderer:payFixed},
	{ title:'实付金额（元）', name:'payment', align:'right'},
	{ title:'订单状态', name:'status', align:'left',renderer:statusFixed},
	{ title:'发货供货商', name:'supplierName', align:'left'},
	{ title:'下单时间', name:'createdTime',width:130, align:'center',lockWidth:true, renderer: YouGou.Util.timeFixed},
	{ title:'操作', name:'' ,width:100, align:'center',lockWidth:true, renderer: actionFixed},
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
	url: '/afterSale/queryApplyData.sc',
	fullWidthRows: true,
	autoLoad: false,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    if(action == "apply"){
    	window.location.href = '/afterSale/applyList.sc?type=2&id='+item.id;
    }
    e.stopPropagation();  //阻止事件冒泡
});
//开启已关闭订单的售后状态
function updateApply(id){
	YouGou.Ajax.doPost({
		successMsg: '开启售后成功！',
		url: '/afterSale/updateApply.sc',
	  	data: { "id" : id ,"manualRefundFlag" : 2 },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}
//查看订单详情
function viewInfo(orderId){
	window.location.href = '/order/orderDetails.sc?src=refund&orderId='+orderId;
}
//查询
function doQuery(){
	mmGrid.load();
}


J('#createdTimeStart').calendar({maxDate:'#createdTimeEnd',format:'yyyy-MM-dd HH:mm:ss'});
J('#createdTimeEnd').calendar({minDate:'#createdTimeStart',format:'yyyy-MM-dd HH:mm:ss'});