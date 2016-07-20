/**
 * Created by guo.r on 2016/4/14.
 */

var list_aftersale_apply = function(){
    function Init(){
        ReutunTypeSelect();
        $('[name="refundType"]').eq(0).click();
    }

    function ReutunTypeSelect(){
        $('[name="refundType"]').on('change', function(e){
            var data = {
                id: '3355',
                refundType: this.value
            };
            laytpl($('#formtemplate').html()).render(data, function (html) {
                $('#returnForm').html(html);
            });
            $('#proNum').on('keyup',function(){
            	setRefundFee();
        	});
        	var refundType = $('input[name=refundType]:checked').val();
        	if(refundType == 'REJECTED_REFUND'){
        		$('#refundFee').attr('readonly','readonly');
        		setRefundAddr();
        	}else{
        		$('#refundFee').removeAttr('readonly');
        	}
        });
    }

    $(function(){
        Init();
    });

    return {}
}();
$(function(){
	$('input[name=goodsselect]:enabled:first').prop('checked',true);
	$('input[name=goodsselect]').on('change',setRefundFee);
	$('input[name=goodsselect]').on('change',setRefundAddr);
})
var setRefundFee = function(){
	var productObj = $('input[name=goodsselect]:enabled:checked');
	var refundType = $('input[name=refundType]:checked').val();
	if(refundType == 'REJECTED_REFUND'){
		if(productObj.length != '1'){
			$('#refundFee').val('');
			return false;
		}
		//退款数量
		var proNum = $('#proNum').val();
		var exp = /^\+?[1-9][0-9]*$/;
		if(!exp.test(proNum)){
			$('#refundFee').val('');
			return;
		}
		//可退数量
		var canReturnNum = productObj.prev().val();
		var canReturnFee = productObj.next().val();
		var postFee = $('#postFee').val();
		if(parseInt(proNum) < parseInt(canReturnNum)){
			var price = productObj.next().next().val();
			var refundFee = parseInt(proNum) * price;
			if(parseFloat(canReturnNum) > parseFloat(refundFee)){
				refundFee = canReturnFee;
			}
			$('#refundFee').val(refundFee);
		}else{
			$('#refundFee').val(parseFloat(canReturnFee) + parseFloat(postFee));
		}
	}
}

function setRefundAddr(){
	var refundAddr = '';
	var productObj = $('input[name=goodsselect]:enabled:checked');
	if(productObj.length == '1'){
		refundAddr = productObj.parent().find(':hidden').eq(3).val();
	}
	$('#refundAddr').text(refundAddr);
}