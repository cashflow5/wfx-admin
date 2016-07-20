/*************
	SellerAuthorize
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="select">查看</a>&nbsp;&nbsp;');
    return html.join('');
};

var renderer_seller_access = function(val,item,rowIndex){
	var html = [];
	if(!val){
		val = "";
	}
	html.push('<a href="javascript:void(0);" action="select">'+val+'</a>&nbsp;&nbsp;');
    return html.join('');
};

var renderer_seller_status = function(val,item,rowIndex){
	if(val == "1"){
		return "待审核";
	}else if(val == "2"){
		return "审核不通过";
	}else if(val == "3"){
		return "合作中";
	}else if(val == "4"){
		return "取消合作";
	}else{
		return "";
	}			
}

var renderer_status = function(val,item,rowIndex){
	if(val == "1"){
		return "待审核";
	}else if(val == "2"){
		return "审核通过";
	}else if(val == "3"){
		return "审核不通过";
	}else{
		return "";
	}			
}

// 列集合
var cols = [
	{ title:'分销商账号', name:'sellerLoginName', align:'left', renderer: renderer_seller_access},
	{ title:'微信用户名', name:'platformUsername', align:'left'},
	{ title:'姓名', name:'sellerName', align:'left'},
	{ title:'分销商状态', name:'sellerStatus', align:'left', renderer: renderer_seller_status},
	{ title:'佣金收入总额(元)', name:'commissionTotalAmount', align:'center'},
	{ title:'资质审核状态', name:'status', align:'center', renderer: renderer_status},
	{ title:'操作', name:'' ,width:200, align:'center',lockWidth:true, renderer: actionFixed},
    { title:'分销商ID', name:'sellerId', hidden: true},
	{ title:'分销商授权ID', name:'id', hidden: true}
];

//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");

// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/user/queryAuthorizeData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    if(action == "select"){//查看
    	window.location.href="/user/authorizeInfo.sc?sellerId=" + item.sellerId
    }
    e.stopPropagation();  //阻止事件冒泡
});

//查询
function doQuery(){
	mmGrid.load();
}