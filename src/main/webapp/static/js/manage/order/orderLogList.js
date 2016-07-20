/*************
	OrderLog
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="select">查看</a>&nbsp;&nbsp;');
	html.push('<a href="javascript:void(0);" action="edit">编辑</a>&nbsp;&nbsp;');
	html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
    return html.join('');
};

var logTypeDesc = function(val,item,rowIndex){
	var desc = "";
	if(val ==1){
		desc = "操作日志";
	}else if(val ==2 ){
		desc = "正常流转日志";
	}else{
		desc = "其他";
	}
	return desc;
}

var optResultDesc = function(val,item,rowIndex){
	var desc = "";
	if(val ==1){
		desc = "成功";
	}else if(val ==2 ){
		desc = "失败";
	}else{
		desc = "其他";
	}
	return desc;
}

var optTypeDesc = function(val,item,rowIndex){
	var desc = "";
	if(val ==1){
		desc = "生成订单";
	}else if(val ==2 ){
		desc = "订单支付";
	}else if(val ==3 ){
		desc = "部分发货";
	}else if(val ==4 ){
		desc = "订单发货";
	}else if(val ==5 ){
		desc = "退款成功";
	}else if(val ==6 ){
		desc = "退款关闭";
	}else if(val ==7 ){
		desc = "取消订单";
	}else if(val ==8 ){
		desc = "订单拣货中";
	}else if(val ==9 ){
		desc = "确认收货";
	}else if(val ==10 ){
		desc = "修改退款";
	}else if(val ==11 ){
		desc = "取消退款";
	}else if(val ==12 ){
		desc = "申请售中退款";
	}else if(val ==13 ){
		desc = "申请售后退款";
	}else if(val ==14 ){
		desc ="拒绝退款";
	}else if(val ==15 ){
		desc ="确认退款";
	}else if(val ==16 ){
		desc ="退款失败";
	}else if(val ==17 ){
		desc ="关闭订单";
	}else{
		desc = "其他";
	}
	return desc;
}

// 列集合
var cols = [
	
	{ title:'日志类型', name:'type', align:'center' , renderer:logTypeDesc },
	{ title:'时间', name:'createTime', align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'操作人', name:'optUser', align:'center'},
	{ title:'行为', name:'optType', align:'center', renderer:optTypeDesc },
	{ title:'操作内容', name:'logInfo', align:'center'},
	{ title:'操作结果', name:'optResult', align:'center' , renderer:optResultDesc},
	/*{ title:'操作', name:'' ,width:200, align:'center',lockWidth:true, renderer: actionFixed},*/
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
	url: '/order/queryOrderLogData.sc', /*?orderNo=wfxyg2016032202*/
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    //编辑
    if(action == "edit"){
    	YouGou.UI.Dialog.alert({message:"编辑"});
    }else if(action == "delete"){// 删除
   		YouGou.UI.Dialog.confirm({
   			message : "确定要删除吗？"
   		},function(result){
   			if(result) {
                removeOrderLog(item.id);
            }
   		});
    }else if(action == "select"){//查看
    	YouGou.UI.Dialog.alert({message:"查看"});
    }
    e.stopPropagation();  //阻止事件冒泡
});
//删除
function removeOrderLog(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/order/removeOrderLog.sc',
	  	data: { "id" : id },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}
//更新状态时使用
function updateOrderLog(id,status){
	YouGou.Ajax.doPost({
		successMsg: '更新成功！',
		url: '/order/saveOrderLog.sc',
	  	data: { "id" : id ,"status" : status },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}
//查询
function doQuery(){
	mmGrid.load();
}