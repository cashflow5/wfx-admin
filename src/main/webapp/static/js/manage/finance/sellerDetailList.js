/*************
	FinSellerInfo
**************/

J('#transactionTimeStart').calendar({maxDate:'#transactionTimeEnd',format:'yyyy-MM-dd'}); 
J('#transactionTimeEnd').calendar({minDate:'#transactionTimeStart',format:'yyyy-MM-dd'});

//明细列集合
var detailcols = [
	{ title:'交易流水号', name:'transactionNumber', align:'left', width:135},
	{ title:'交易时间', name:'transactionTime', align:'left', width: 126, renderer: YouGou.Util.timeFixed},
	{ title:'交易类型', name:'transactionType', align:'left',width:65, renderer:function(val,item,rowIndex){
		if(item.transactionType=='1'){
			return '佣金收益'
		}else if(item.transactionType=='2'){
			return '提现'
		}
	}},
	{ title:'收入金额', name:'incomeAmount', align:'right',width:65, renderer:function(val,item,rowIndex){
		return "<font color='green'>"+item.incomeAmount+"<font>"
	}},
	{ title:'支出金额', name:'expendAmount', align:'right',width:65, renderer:function(val,item,rowIndex){
		return "<font color='red'>"+item.expendAmount+"<font>"
	}},
	{ title:'账户余额', name:'accountBalance', align:'right',width:65},
	{ title:'支付方式', name:'paymentStyleName', align:'left'},
	{ title:'交易订单号', name:'transactionOrderNum', align:'left',width:120},
	{ title:'单据状态', name:'billState', align:'left',renderer:function(val,item,rowIndex){
		if(item.billState=='1'){
			return '交易成功'
		}else if(item.billState=='2'){
			return '处理中'
		}else if(item.billState=='3'){
			return '交易关闭'
		}else if(item.billState=='4'){
			return '交易失败'
		}
	}},
	{ title:'操作人', name:'operater', align:'left',width:65},
	{ title:'备注', name:'operateNote', align:'left'},
	{ title:'分销商ID', name:'sellerId', align:'left',hidden: true},
    { title:'ID', name:'id', hidden: true}
];

//分页器
var mmPaginatorDetail = $('#grid-pager-detail').mmPaginator({});
// 搜索表单属性
var mmFormParamsDetail = new MMSearchFormParams("queryForm");

// 表格	
var mmGridDetail = $('#grid-table-detail').mmGrid({
	height: 'auto',
	cols: detailcols,
	url: '/finance/sellerdetail/querySellerData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginatorDetail, mmFormParamsDetail]
});

//查询
function querySellerDetaillist() {
	mmGridDetail.load();
}

//下载明细报表
function exportDetailExcel() {
	$('#queryForm').attr('action', '/finance/sellerdetail/exportDetailExcel.sc');
	$('#queryForm').submit();
}
