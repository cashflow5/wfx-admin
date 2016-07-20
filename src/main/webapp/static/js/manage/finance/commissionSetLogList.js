/*************
	CommissionSetLog
**************/
// 列集合
var cols = [
	{ title:'业务类型', name:'commissionType',align:'center', width:150,lockWidth:true,renderer: function(val,item,rowIndex){
		var str = "";
		if (item.commissionType=='1'){
			str="默认佣金";
		}else if (item.commissionType=='2'){
			str="品牌分类佣金";
		}else {
			str="单品佣金";
		}
		return str;
	}},
	{ title:'日志类型', name:'operateType',align:'center', width:80,lockWidth:true,renderer: function(val,item,rowIndex){
		var str = "";
		if (item.operateType=='1'){
			str="新增";
		}else if(item.operateType=='2'){
			str="修改";
		}else if(item.operateType=='3'){
			str="删除";
		}
		return str;
	}},
	{ title:'操作人', name:'createUser', width:100,lockWidth:true, align:'center'},
	{ title:'操作时间', name:'createTime', width:125,lockWidth:true, align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'操作内容', name:'remark', align:'left'}
];

J('#startTime').calendar({maxDate:'#endTime',format:'yyyy-MM-dd HH:mm:ss'});
J('#endTime').calendar({minDate:'#startTime',format:'yyyy-MM-dd HH:mm:ss'});
//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");

// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/finance/queryCommissionSetLogData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

function doQuery(){
	mmGrid.load();
}