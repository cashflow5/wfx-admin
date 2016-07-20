/*************
	FinReturnDebt
**************/
// 操作列动作

// 查询条件
J('#refundStartTime').calendar({maxDate:'#refundEndTime',format:'yyyy-MM-dd'});
J('#refundEndTime').calendar({minDate:'#refundStartTime',format:'yyyy-MM-dd'});

// 列集合
var cols = [
	{ title:'退款单编号', name:'backNo', align:'left', width: 135},
	{ title:'订单号', name:'orderNo', align:'left', width: 135},
	{ title:'申请退款时间', name:'applyDate', align:'left', width: 126, renderer: YouGou.Util.timeFixed},
	{ title:'分销商编码', name:'storeName', align:'left'},
	{ title:'买家账号', name:'customerNo', align:'left', width: 135},
	{ title:'退款类型', name:'refundType', align:'left', renderer: function (val, item, rowIndex) {
		var str = '';
		if (item.refundType == 'ONLY_REFUND') {
			str = '仅退款';
		} else if (item.refundType == 'DELIVERD_REFUND') {
			str = '已发货仅退款';
		} else if (item.refundType == 'REJECTED_REFUND') {
			str = '退货退款';
		}
		return str;
	}},
	{ title:'退款金额', name:'refundAmount', align:'right'},
	{ title:'退款原因', name:'refundNote', align:'left', width: 200},
	{ title:'退款说明', name:'refundDesc', align:'left', width: 200},
	{ title:'退款时间', name:'refundDate', align:'center', width: 126, renderer: YouGou.Util.timeFixed},
	{ title:'退款状态', name:'refundStatus', align:'left', renderer: function (val, item, rowIndex) {
		var str = '';
		if (item.refundStatus == 2) {
			str = '待确认退款';
		} else if (item.refundStatus == 3) {
			str = '退款处理中';
		} else if (item.refundStatus == 4) {
			str = '已退款';
		} else if (item.refundStatus == 5) {
			str = '退款失败';
		}
		return str;
	}},
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
	url: '/finance/returndebt/statement/queryReturnDebtStatementData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

function doQuery(){
	mmGrid.load();
}

/*
 * 导出报表
 */
function exportExcel() {
	$('#searchForm').attr('action', '/finance/returndebt/statement/exportExcel.sc');
	$('#searchForm').submit();
}

