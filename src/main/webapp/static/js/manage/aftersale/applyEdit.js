//返回
function back(){
	window.location.href = '/afterSale/applyList.sc';
}

function applyRefund(){
	var productObj = $('input[name=goodsselect]:enabled:checked');
	if(productObj.length != '1'){
		YouGou.UI.Dialog.autoCloseTip('请选择商品');
		return false;
	}
	var refundType = $('input[name=refundType]:checked').val();
	var reason = $('#reason').val();
	var refundFee = $('#refundFee').val();
	var description = $('#description').val();
	var exp = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
	if(!exp.test(refundFee)){
		YouGou.UI.Dialog.autoCloseTip('退货金额必须是数字，小数点最多两位');
		return;
	}
	if(description == ''){
		YouGou.UI.Dialog.autoCloseTip('退货说明不能为空');
		return;
	}
	if(refundType == 'DELIVERD_REFUND'){//已发货仅退款
		var canReturnFee = productObj.next().val();
		if(parseFloat(refundFee) > parseFloat(canReturnFee)){
			YouGou.UI.Dialog.autoCloseTip('退货金额不能大于可退金额：' + canReturnFee);
			return;
		}
	}else{//退货退款
		var proNum = $('#proNum').val();
		var companyName = $('#companyName').val();
		var sid = $('#sid').val();
		exp = /^\+?[1-9][0-9]*$/;
		if(!exp.test(proNum)){
			YouGou.UI.Dialog.autoCloseTip('退货数量必须为正整数');
			return;
		}
		var canReturnNum = productObj.prev().val();
		if(parseInt(proNum) > parseInt(canReturnNum)){
			YouGou.UI.Dialog.autoCloseTip('退货数量不能大于可退数量：' + canReturnNum);
			return;
		}
		if(companyName == ''){
			YouGou.UI.Dialog.autoCloseTip('快递公司名称不能为空');
			return;
		}
		if(sid == ''){
			YouGou.UI.Dialog.autoCloseTip('物流单号不能为空');
			return;
		}
	}
	$('#orderDetailId').val(productObj.val());
	YouGou.Ajax.doPost({
		successMsg: '申请成功！',
		url: '/afterSale/afterSaleApply.sc',
	  	data: $('#actionForm').serialize(),
	  	success : function(data){
	  		if(data.state == 'success'){
	  			window.location.href = '/afterSale/queryAfterSaleList.sc';
	  		}
		}
	});
}