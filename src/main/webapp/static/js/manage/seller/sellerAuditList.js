/*************
	SellerInfo
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="pass">通过</a>&nbsp;&nbsp;');
	html.push('<a href="javascript:void(0);" action="refuse">拒绝</a>&nbsp;&nbsp;');
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

// 列集合
var cols = [
	
	{ title:'分销商账号', name:'loginName', align:'left',renderer: logingAccountFixed},
	{ title:'微信用户名', name:'platformUsername', align:'left'},
	{ title:'会员姓名', name:'sellerName', align:'left'},
	{ title:'订单总数', name:'orderCount', align:'center'},
	{ title:'订单总金额(元)', name:'orderAmount', align:'center'},
	{ title:'申请时间', name:'createTime', align:'center', renderer: YouGou.Util.timeFixed},
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
	url: '/user/querySellerAuditData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

J('#createTimeStart').calendar({maxDate:'#createTimeEnd',format:'yyyy-MM-dd HH:mm:ss'}); 
J('#createTimeEnd').calendar({minDate:'#createTimeStart',format:'yyyy-MM-dd HH:mm:ss'});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
	var id = item.id;
	var loginaccountId = item.loginaccountId;
	var loginName = item.loginName;
    //通过
    if(action == "pass"){   	
    	YouGou.UI.Dialog.confirm({
   			message : "确定要审核通过吗？"
   		},function(result){
   			if(result) {
   				doPassAjax(id,loginaccountId,loginName);
            }
   		});
    }else if(action == "refuse"){// 拒绝
    	refuse(id);
    }else if(action == "query"){//查看
    	//YouGou.UI.Dialog.alert({message:"查看"});
    	//window.location.href="/user/getSellerAuditInfoById.sc?sellerId="+id
    	querySellerDetail(id);
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


//ajax 审核通过
function doPassAjax(id,loginaccountId,loginName){
	
	YouGou.Ajax.doPost({
   		successMsg: '审核通过',
   		errorMsg:'审核失败',
   		url: '/user/sellerAuditPass.sc',
   		dataType:"json",
   	  	data: { "id" : id ,"loginaccountId":loginaccountId,"loginName":loginName,"type":"audit"},
   	  	success : function(data){  	  		
   	  		//YouGou.Util.inspect(data);
   	  		if(data.data.result == false || data.data.result == "false" ){
   	  			YouGou.UI.Dialog.autoCloseTip("操作失败，系统异常");
   	  		}
   	  		mmGrid.load();	
   		}
   	});
}

//分销商审核审核拒绝弹出框
function refuse(sellerId){
	YouGou.UI.Dialog.show({
		title : '分销商审核拒绝',
		message: function(dialog) {
			var msg = "<div>拒绝原因：<textarea id='refuseRemark' style='width:300px;height:200px' /></div>";
			return msg;
		},
		buttons: [
		    {
				label: '确定',
				cssClass: 'btn-primary',
				action: function(dialog) {
					var refuseRemark = $("#refuseRemark").val();
					if(refuseRemark == ""){
						YouGou.UI.Dialog.autoCloseTip("拒绝原因不能为空");
						return false;
					}
					doRefuseAjax(sellerId,refuseRemark,dialog);
				}
		    }, 
			{
		    	label: '取消',
		    	action: function(dialog) {
		    		dialog.close();
		    	}
			}
	    ]
	});	
}

//ajax 审核拒绝
function doRefuseAjax(sellerId,refuseRemark,dialog){	
	
	YouGou.Ajax.doPost({
   		successMsg: '拒绝成功',
   		errorMsg:'拒绝失败',
   		url: '/user/sellerAuditRefuse.sc',
   		dataType:"json",
   	  	data: { "id" : sellerId ,"refuseRemark":refuseRemark},
   	  	success : function(data){
   	  		dialog.close();
 			mmGrid.load();
   	  		//YouGou.Util.inspect(data);
	    	/*  	if(data.data.resultCount >0 ){
   	  			YouGou.UI.Dialog.autoCloseTip("拒绝成功");
   	  			dialog.close();
   	  			mmGrid.load();
   	  		}else{
   	  			YouGou.UI.Dialog.autoCloseTip("拒绝失败");
   	  		}*/
     			
   		}
   	});
}

function doQuery(){
	mmGrid.load();
}