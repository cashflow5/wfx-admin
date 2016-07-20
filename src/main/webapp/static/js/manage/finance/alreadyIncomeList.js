/*************
	FinAlreadyIncome
**************/

// 列集合
var cols = [
	{ title:'创建时间', name:'createDate', align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'订单号', name:'orderNo', align:'left'},
	{ title:'客户名称', name:'customerName', align:'left'},
	{ title:'客户账号', name:'customerNo', align:'left'},
	{ title:'收款时间', name:'incomeDate', align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'下单时间', name:'orderDate', align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'订单金额', name:'orderAmount', align:'left'},
	{ title:'店铺名', name:'storeName', align:'left'},
	{ title:'店铺id', name:'storeId', align:'left'},
	{ title:'订单来源', name:'orderSource', align:'left'},
	{ title:'订单二级来源编码', name:'orderSourceId', align:'left'},
	{ title:'收款类型,1-正常收款,2-重复收款,0-收款异常', name:'incomeType', align:'center'},
	{ title:'收款账号', name:'incomeAccount', align:'left'},
	{ title:'在线支付交易号', name:'onlinePayNumber', align:'left'},
	{ title:'在线支付方式名称', name:'onlinePayStyleName', align:'left'},
	{ title:'在线支付方式编码', name:'onlinePayStyleNo', align:'left'},
	{ title:'收货款金额', name:'productAmount', align:'left'},
	{ title:'收运费金额', name:'fareAmount', align:'left'},
	{ title:'收款合计', name:'incomedAmount', align:'left'},
	{ title:'操作员', name:'operator', align:'left'},
	{ title:'备注', name:'note', align:'left'}
];

//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");

// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/finance/income/queryAlreadyIncomeData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

function doQuery(){
	mmGrid.load();
}