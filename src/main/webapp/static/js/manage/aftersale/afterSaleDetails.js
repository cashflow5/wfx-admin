/*************
	OrderRefundDetails
**************/

function handleRefund(action,refundNo){

    if(action == "agree"){// 
   		YouGou.UI.Dialog.confirm({
   			message : "确定同意退款吗？"
   		},function(result){
   			if(result) {
   				agree(refundNo);
            }
   		});
    }else if(action=="deliverd"){
    	YouGou.UI.Dialog.confirm({
   			message : "确定收货吗？"
   		},function(result){
   			if(result) {
   				deliverdCompleted(refundNo);
            }
   		});
    }else if(action=="deny"){
    	YouGou.UI.Dialog.show({
    		title : '拒绝退款',
    		message: function(dialog) {
    			var msg = "<div>拒绝原因：<textarea id='denyReason' style='width:300px;height:200px' /></div>";
    			return msg;
    		},
    		buttons: [
    		    {
    				label: '确定',
    				cssClass: 'btn-primary',
    				action: function(dialog) {
    					var denyReason = $("#denyReason").val();
    					if(!denyReason || $.trim(denyReason)=="" ){
    						YouGou.UI.Dialog.alert({message:"拒绝原因不能为空!"});
    						return;
    					}else{
    						denyReason = $.trim(denyReason);
    						doRefuseAjax(refundNo,denyReason,dialog);
    					}
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
}
//
function deliverdCompleted(refundNo){
	YouGou.Ajax.doPost({
		successMsg: '确定收货成功！',
		url: '/afterSale/auditRefund.sc',
	  	data: { "refundNo" : refundNo ,"manualRefundFlag" : 2 },
	  	errorMsg:'确定收货失败！',
	  	success:function(){
	  		parent.location.reload();
	  	}
	});
}
function agree(refundNo){
	YouGou.Ajax.doPost({
		successMsg: '同意退款成功！',
		url: '/afterSale/auditRefund.sc',
	  	data: { "refundNo" : refundNo ,"manualRefundFlag" : 1 },
	  	errorMsg:'同意退款失败！',
	  	success:function(){
	  		parent.location.reload();
	  	}
	});
}
//ajax 审核拒绝
function doRefuseAjax(refundNo,denyReason,dialog){	
	
	YouGou.Ajax.doPost({
   		successMsg: '拒绝退款成功!',
   		errorMsg:'拒绝退款失败!',
   		url: '/afterSale/auditRefund.sc',
   		dataType:"json",
   	  	data: { "refundNo" :refundNo ,"denyReason":denyReason,"manualRefundFlag":3},
   	  	success : function(data){
   	  		dialog.close();
   	  		window.location.reload();
   		}
   	});
}