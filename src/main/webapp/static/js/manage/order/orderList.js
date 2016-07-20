/*************
	Order
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	var id =item.id;
	html.push('<a href="/order/orderDetails.sc?orderId='+id+'\" >详情</a>&nbsp;&nbsp;');
    return html.join('');
};

var payTypeDesc = function(val,item,rowIndex){
	var html = "";
	if(val =="alipay"){
		html = "支付宝支付";
	}else if(val =="wechatpay"){
		html = "微信支付";
	}else{
		html = "其他支付方式";
	}
	return html;
};

var orderStatusDesc = function (val){
	var html = "";
	if(val =="WAIT_PAY"){
		html = "待付款";
	}else if(val =="WAIT_DELIVER"){
		html = "待发货";
	}else if(val =="PART_DELIVERED"){
		html = "部分发货";
	}else if(val =="DELIVERED"){
		html = "已发货";
	}else if(val =="TRADE_SUCCESS"){
		html = "交易成功";
	}else if(val =="TRADE_CLOSED"){
		html = "交易关闭";
	}else{
		html = "其他";
	}
	return html;
}

// 列集合
var cols = [
	{ title:'订单号', name:'wfxOrderNo', align:'left'},	
	{ title:'订单来源', name:'shopName', align:'left'},
	{ title:'收货人', name:'receiverName', align:'left'},
	{ title:'支付方式', name:'payType', align:'left' ,renderer:payTypeDesc},
	{ title:'实付金额', name:'payment', align:'center'},
	{ title:'订单状态', name:'status', align:'left' ,renderer:orderStatusDesc},
	/*{ title:'发货供应商', name:'status', align:'left'},2016.4.8与产品翟淑军确认去掉此列*/
	{ title:'下单时间', name:'createdTime', align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'操作', name:'' ,width:200, align:'center',lockWidth:true, renderer: actionFixed},
    { title:'ID', name:'id', hidden: true}
];

J('#createTimeStart').calendar({maxDate:'#createTimeEnd',format:'yyyy-MM-dd HH:mm:ss'}); 
J('#createTimeEnd').calendar({minDate:'#createTimeStart',format:'yyyy-MM-dd HH:mm:ss'});

//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");

// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/order/queryOrderData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
     if(action == "select"){//查看
    	YouGou.UI.Dialog.alert({message:"查看"});
    }
    e.stopPropagation();  //阻止事件冒泡
});


//查询
function doQuery(){
	mmGrid.load();
}