/*************
	SysLog 模块操作日志
**************/

var transferBusinessType = function(val,item,rowIndex){
	var businessType = item.businessType;
	var businessTypeDesc = "";
	if(businessType=="COMMODITY_CREATE"){
		businessTypeDesc = "添加商品";
	}else if(businessType=="COMMODITY_ON_SHELVES"){
		businessTypeDesc = "商品上下架";
	}else if(businessType=="COMMODITY_PRICE_UP"){
		businessTypeDesc = "商品调价";
	}else if(businessType=="COMMODITY_EXPORT"){
		businessTypeDesc = "商品导出";
	}
	var html = [];
	html.push('<span>'+businessTypeDesc+'</span>');
   return html.join('');
}

var transferOperateType = function(val,item,rowIndex){
	var operateType = item.operateType;
	if(operateType=="ADD"){
		operateTypeDesc = "添加";
	}else if(operateType=="EDIT"){
		operateTypeDesc = "修改";
	}else if(operateType=="DELETE"){
		operateTypeDesc = "删除";	
	}else if(operateType=="EXPORT"){
		operateTypeDesc = "导出";	
	}
	var html = [];
	html.push('<span>'+operateTypeDesc+'</span>');
   return html.join('');
}
// 列集合
var cols = [
	{ title:'业务类型', name:'businessType', align:'left',renderer:transferBusinessType},
	{ title:'日志类型', name:'operateType', align:'left',renderer:transferOperateType},
	{ title:'操作人', name:'operateUser', align:'left'},
	{ title:'操作时间', name:'operateDate', align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'款色编码', name:'supplierCode', align:'left'},
    { title:'款色ID', name:'commodityNo', align:'left'},
    { title:'商品款号', name:'styleNo', align:'left'},
	{ title:'操作内容', name:'operateContent', align:'left'},
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
	url: '/commodity/queryOperationLogData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});



function doQuery(){
	mmGrid.load();
}

J('#operateDateStart').calendar({maxDate:'#operateDateEnd',format:'yyyy-MM-dd HH:mm:ss'});
J('#operateDateEnd').calendar({minDate:'#operateDateStart',format:'yyyy-MM-dd HH:mm:ss'});