/*************
	Shop
**************/
var transferStatus = function(val,item,rowIndex){
	var status = item.status;
	var statusDesc="";
	if(status=="1"){
		statusDesc = "开启";
	}else if(status=="2"){
		statusDesc = "关闭";
	}
	var html = [];
	html.push('<span>'+statusDesc+'</span>');
   return html.join('');
}
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	var statusValue = item.status;
	if(statusValue==1){
		html.push('<a href="javascript:void(0);" action="close">关闭</a>&nbsp;&nbsp;');
	}else{
		html.push('<a href="javascript:void(0);" action="open">开启</a>&nbsp;&nbsp;');
	}
	html.push('<a href="javascript:void(0);" action="select">查看</a>&nbsp;&nbsp;');
	
    return html.join('');
};

// 列集合
var cols = [
	
	{ title:'店铺名称', name:'name', align:'left'},
	{ title:'分销商账号', name:'loginName', align:'left'},
	{ title:'创建时间', name:'createTime', align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'最近修改时间', name:'updateTime', align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'店铺状态', name:'status', align:'center',renderer:transferStatus},
	{ title:'操作', name:'' ,width:200, align:'center',lockWidth:true, renderer: actionFixed},
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
	url: '/shop/queryShopData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    //编辑
   if(action == "open"){// 开启
   		YouGou.UI.Dialog.confirm({
   			message : "确定要开启吗？"
   		},function(result){
   			if(result) {
                openShop(item.id);
            }
   		});
   }else if(action == "close"){// 关闭
    		YouGou.UI.Dialog.confirm({
    			message : "确定要关闭吗？"
    		},function(result){
    			if(result) {
    			closeShop(item.id);
             }
    		});
    }else if(action == "select"){//查看
    	window.location.href="/shop/getShopById.sc?id="+item.id
    }
    e.stopPropagation();  //阻止事件冒泡
});

function closeShop(id){
	YouGou.Ajax.doPost({
		successMsg: '关闭店铺成功！',
		url: '/shop/closeShop.sc',
	  	data: { "id" : id },
	  	success : function(result){
	  		if(result.data >0 ){
   	  			mmGrid.load();
   	  		}
		},
		errorMsg: '关闭店铺失败！'
	});
}
function openShop(id){
	YouGou.Ajax.doPost({
		successMsg: '开启店铺成功！',
		url: '/shop/openShop.sc',
	  	data: { "id" : id },
	  	success : function(result){
	  		if(result.data >0 ){
   	  			mmGrid.load();
	  		}
	  	},
	  	errorMsg: '开启店铺失败！'
	});
}


function doQuery(){
	mmGrid.load();
}

J('#createTimeStart').calendar({maxDate:'#createTimeEnd',format:'yyyy-MM-dd HH:mm:ss'});
J('#createTimeEnd').calendar({minDate:'#createTimeStart',format:'yyyy-MM-dd HH:mm:ss'});