/*************
	FinReturnDebt
**************/

// 查询条件
J('#applyStartTime').calendar({maxDate:'#applyEndTime',format:'yyyy-MM-dd'});
J('#applyEndTime').calendar({minDate:'#applyStartTime',format:'yyyy-MM-dd'});
J('#startTime').calendar({maxDate:'#endTime',format:'yyyy-MM-dd'});
J('#endTime').calendar({minDate:'#startTime',format:'yyyy-MM-dd'});

/**
 * 点击选项卡查询
 * @param tabQueryFlag
 * @param type 退款类型
 */
function tabSelectQuery(tabQueryFlag) {
	window.location.href = "/finance/returndebt/returnDebtList.sc?tabQueryFlag="+tabQueryFlag;
}

/*
 * 点击搜索按钮查询
 */
function doQuery() {
	$('#searchForm').attr('action', '/finance/returndebt/returnDebtList.sc');
	$('#searchForm').submit();
}

/*
 * 导出excel
 */
function exportExcel() {
	window.location.href = "/finance/returndebt/exportExcel.sc";
}

/**
 * 查看详情
 * @param action
 */
function showDetail(id, backNo) {
	var tabQueryFlag = $('#tabQueryFlag').val();
	var refundType = $('#refundType').val();
	window.location.href = "/finance/returndebt/getReturnDebtDetail.sc?id="+id+"&backNo="+backNo+"&tabQueryFlag="+tabQueryFlag+"&refundType="+refundType;
}

/**
 * 同意退款
 * @param id
 * @param backNo
 */
function agreeRefund(id, backNo) {
	var actionFlag = 'true';
	YouGou.UI.progressLoading();
	$.ajax({
	       type: "post",
	       url: '/finance/returndebt/checkIsRefunded.sc',
	       data: {"id":id},
	       async: false,
	       success : function(data) {
	    	   YouGou.UI.progressStop();
	    	   var result = $.parseJSON(data);
	    	   if (result.state == 'error') {
	    		   YouGou.UI.Dialog.alert({message: "系统发生异常，请稍后重试."});
	    	   } else if (result.state == 'success') {
	    		   if (result.data == 'true') {
	    		   	   actionFlag = 'false';
	    			   YouGou.UI.Dialog.alert({message: "此退款单已操作退款，请查看."});
	    		   }
	    	   }
		  }
	}); 
	if (actionFlag == 'false') {
		return ;
	}
	if (actionFlag == 'true') {
		YouGou.UI.Dialog.confirm({
			message: "您确认提交付款吗？"
		}, function (result) {
			if (result) {
				$('#refundForm').attr("action", "/finance/returndebt/agreeRefund.sc");
				$('#refundForm').submit();
			}
		});
	}
}

/**
 * 手动退款
 * @param id
 * @param backNo
 */
function changeStatus(id, backNo) {
	var actionFlag = 'true';
	YouGou.UI.progressLoading();
	$.ajax({
	       type: "post",
	       url: '/finance/returndebt/checkIsRefunded.sc',
	       data: {"id":id},
	       async: false,
	       success : function(data) {
	    	   YouGou.UI.progressStop();
	    	   var result = $.parseJSON(data);
	    	   if (result.state == 'error') {
	    		   YouGou.UI.Dialog.alert({message: "系统发生异常，请稍后重试."});
	    	   } else if (result.state == 'success') {
	    		   if (result.data == 'true') {
	    		   	   actionFlag = 'false';
	    		   }
	    	   }
		  }
	}); 
	if (actionFlag == 'false') {
		YouGou.UI.Dialog.confirm({
			message: "此退款单已操作确认退款，您确认手动退款吗？"
		}, function (result) {
			if (result) {
				$('#refundForm').attr("action", "/finance/returndebt/changeRefundStatus.sc");
				$('#refundForm').submit();
			}
		});
	}
}

/**
 * 退款详情页面返回到退款列表页面
 * @param tabQueryFlag
 */
function toListIndex(tabQueryFlag) {
	window.location.href = "/finance/returndebt/returnDebtList.sc?tabQueryFlag="+tabQueryFlag;
}

