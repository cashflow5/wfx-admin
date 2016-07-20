/*************
	FinAlreadyIncomeStatement
**************/

// 查询条件
J('#tradeStartTime').calendar({maxDate:'#tradeEndTime',format:'yyyy-MM-dd'});
J('#tradeEndTime').calendar({minDate:'#tradeStartTime',format:'yyyy-MM-dd'});

// 列集合
var cols = [
	{ title:'订单号', name:'orderNo', width: 126, align:'left'},
	{ title:'收款时间', name:'tradeDate', align:'center', width: 126, renderer: YouGou.Util.timeFixed},
	{ title:'下单时间', name:'orderDate', align:'center', width: 126, renderer: YouGou.Util.timeFixed},
	{ title:'订单金额', name:'orderAmount', align:'right'},
	{ title:'分销商编码', name:'storeName', align:'left', width: 135},
	{ title:'买家账号', name:'customerNo', align:'left'},
	{ title:'支付号', name:'outTradeNo', width: 126, align:'left'},
	{ title:'支付交易金额', name:'tradeAmount', align:'right'},
	{ title:'银行支付交易号流水号', name:'bankTradeNo', width: 160 ,align:'left'},
	{ title:'支付方式名称', name:'bankName', align:'left', width: 135},
	{ title:'收款账号', name:'incomeAccount', align:'left', width: 125},
	{ title:'收货款金额', name:'productAmount', align:'right'},
	{ title:'收运费金额', name:'fareAmount', align:'right'},
	{ title:'收款合计', name:'incomedAmount', align:'right'},
	{ title:'收款类型描述', name:'incomeTypeDesc', width: 140, align:'left'},
	{ title:'操作员', name:'operator', align:'left'},
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
	url: '/finance/income/statement/queryAlreadyIncomeStatementData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

function doQuery(){
	mmGrid.load();
}

/*
 * 导出excel
 */
function exportExcel() {
	$('#searchForm').attr('action', '/finance/income/statement/exportExcel.sc');
	$('#searchForm').submit();
}
