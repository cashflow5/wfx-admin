//审核通过
$('a.pass').click(function(){
	var id = $(this).attr('authorId');
	if(id){
		YouGou.Ajax.doPost({
	   		successMsg: '操作成功',
	   		errorMsg: '操作失败',
	   		url: '/user/updateAuthorizeStatus.sc',
	   		dataType:"json",
	   	  	data: { "id" : id, "status": 2 },
	   	  	success : function(result){
	   	  		if(result.state=="success"){
	   	  			$('#authorizeStatus').html("审核通过");
	   	  		}else{
	   	  			YouGou.UI.Dialog.autoCloseTip(result.msg);
	   	  		}
	   	  		
	   		}
	   	});
	}else{
		YouGou.UI.Dialog.autoCloseTip("没有获取到参数！");
	}
	
});

//审核拒绝
$('a.unpass').click(function(){
	var id = $(this).attr('authorId');
	if(id){
		YouGou.Ajax.doPost({
	   		successMsg: '操作成功',
	   		errorMsg: '操作失败',
	   		url: '/user/updateAuthorizeStatus.sc',
	   		dataType:"json",
	   	  	data: { "id" : id, "status": 3 },
	   	  	success : function(result){
		   	  	if(result.state=="success"){
		   	  		$('#authorizeStatus').html("审核不通过");
	   	  		}else{
	   	  			YouGou.UI.Dialog.autoCloseTip(result.msg);
	   	  		}
	   		}
	   	});
	}else{
		YouGou.UI.Dialog.autoCloseTip("没有获取到参数！");
	}
});

//操作列动作
var fixRemark = function(val,item,rowIndex){
	if(!val){
		val = "";
	}else{
		val = val.replace(/([0-9]{4})([0-9]+)([0-9]{4})/g, function(str, m1, m2, m3){ return m1+ m2.replace(/[0-9]/g, '*') + m3 });
	}
    return val;
};

//列集合
var cols = [
	{ title:'修改时间', name:'optTime', align:'left', renderer: YouGou.Util.timeFixed},
	{ title:'修改内容', name:'remark', align:'left', renderer: fixRemark}
];


//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});

// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/user/querySellerBankLog.sc',
	fullWidthRows: true,
	autoLoad: true,
	params: function(){
		var loginAccountId = $('#loginAccountId').val();
		return {loginAccountId: loginAccountId}
	},
	plugins: [mmPaginator]
});
