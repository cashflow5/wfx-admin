/*************
	SysLog 基础设置模块操作日志
**************/
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
	{ title:'日志类型', name:'operateType', align:'left',renderer:transferOperateType},
	{ title:'操作人', name:'operateUser', align:'left'},
	{ title:'操作时间', name:'operateDate', align:'center', renderer: YouGou.Util.timeFixed},
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
	url: '/basicSet/queryOperationLogData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});



function doQuery(){
	mmGrid.load();
}

J('#operateDateStart').calendar({maxDate:'#operateDateEnd',format:'yyyy-MM-dd HH:mm:ss'});
J('#operateDateEnd').calendar({minDate:'#operateDateStart',format:'yyyy-MM-dd HH:mm:ss'});