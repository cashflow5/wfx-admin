/*************
	SellerInfo
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var state = item.state;
	var html = [];
	if(state == "3"){
		html.push('<a href="javascript:void(0);" action="stop">停止合作</a>&nbsp;&nbsp;');
	}else if(state == "4"){
		html.push('<a href="javascript:void(0);" action="cooperate">开启合作</a>&nbsp;&nbsp;');
	}
	html.push('<a href="javascript:void(0);" action="query">查看</a>&nbsp;&nbsp;');
    return html.join('');
};

//操作列动作
var logingAccountFixed = function(val,item,rowIndex){
	var html = [];
	if(!val){
		val = "";
	}
	html.push('<a href="javascript:void(0);" action="query">'+val+'</a>&nbsp;&nbsp;');
    return html.join('');
};

var stateDesc = function(val,item,rowIndex){
	if(val == "3"){
		return "合作中";
	}else if(val == "4"){
		return "已停止";
	}else{
		return "其他";
	}
				
}

// 列集合
var cols = [
	
	{ title:'分销商账号', name:'loginName', align:'left',renderer: logingAccountFixed},
	{ title:'微信用户名', name:'platformUsername', align:'left'},
	{ title:'上级分销商账号', name:'parentLoginName', align:'left'},
	{ title:'上级微信用户名', name:'parentPlatformUsername', align:'left'},
	{ title:'会员姓名', name:'sellerName', align:'left'},	
	{ title:'佣金收入总额(元)', name:'commissionTotalAmount', align:'center'},
	{ title:'状态', name:'state', align:'center', renderer:stateDesc},
	{ title:'审核通过时间', name:'passDate', align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'操作', name:'' ,width:200, align:'center',lockWidth:true, renderer: actionFixed},
    { title:'ID', name:'id', hidden: true},
    { title:'loginaccountId', name:'loginaccountId', hidden: true},
    { title:'loginName', name:'loginName', hidden: true}
];

//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");

// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/user/querySellerData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

J('#registerDateStart').calendar({maxDate:'#registerDateEnd',format:'yyyy-MM-dd HH:mm:ss'}); 
J('#registerDateEnd').calendar({minDate:'#registerDateStart',format:'yyyy-MM-dd HH:mm:ss'});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
	var id = item.id;
	var loginaccountId = item.loginaccountId;
	var loginName = item.loginName;
    if(action == "stop"){// 停止合作
    	stop(id,loginaccountId,loginName);
    }else if(action == "query"){//查看
    	//YouGou.UI.Dialog.alert({message:"查看"});
    	//window.location.href="/user/getSellerInfoById.sc?sellerId="+id
    	querySellerDetail(id);
    }else if(action == "cooperate"){
    	cooperate(id,loginaccountId,loginName);
    }
    e.stopPropagation();  //阻止事件冒泡
});

//查询分销商详情
function querySellerDetail(id){
	YouGou.UI.Dialog.show({
		title : '分销商详情',
		message: function(dialog) {
			var sellerDetail = $("<div id='sellerDetail'> </div>");
			var msg = $(sellerDetail).load("/user/getSellerAuditInfoById.sc?sellerId="+id);
			return msg;
		}
	});	
}


//分销商停止合作弹出框
function stop(sellerId,loginaccountId,loginName){
	YouGou.UI.Dialog.confirm({
			message : "确定要停止合作吗？"
		},function(result){
			if(result) {
				doAjaxStop(sellerId,loginaccountId,loginName);
        }
	});
}

function doAjaxStop(sellerId,loginaccountId,loginName){
	
	YouGou.Ajax.doPost({
   		successMsg: '停止合作成功',
   		errorMsg: '停止合作失败',
   		url: '/user/sellerStop.sc',
   		dataType:"json",
   	  	data: { "id" : sellerId,"loginaccountId":loginaccountId ,"loginName":loginName},
   	  	success : function(data){
   	  		
	   	  	if(data.data.result == false || data.data.result == "false" ){
	  			YouGou.UI.Dialog.autoCloseTip("操作失败，系统异常");
	  		}	
	   	 	mmGrid.load();
     			
   		}
   	});
}

//分销商开启合作弹出框
function cooperate(sellerId,loginaccountId,loginName){
	YouGou.UI.Dialog.confirm({
			message : "确定要开启合作吗？"
		},function(result){
			if(result) {
			doAjaxCooperate(sellerId,loginaccountId,loginName);
		}
	});
}

function doAjaxCooperate(sellerId,loginaccountId,loginName){
	
	YouGou.Ajax.doPost({
   		successMsg: '开启合作成功',
   		errorMsg: '开启合作失败',
   		url: '/user/sellerAuditPass.sc',
   		dataType:"json",
   	  	data: { "id" : sellerId,"loginaccountId":loginaccountId ,"loginName":loginName,"type":"cooperate"},
   	  	success : function(data){
   	  		
   	  		//YouGou.Util.inspect(data);
	   	 	if(data.data.result == false || data.data.result == "false" ){
	  			YouGou.UI.Dialog.autoCloseTip("操作失败，系统异常");
	  		}	
	   	 	mmGrid.load();
   		}
   	});
}



function doQuery(){
	mmGrid.load();
}