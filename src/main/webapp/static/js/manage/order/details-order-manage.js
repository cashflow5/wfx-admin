/**
* by lijunfnag 20160324
**/
var DetailsOrderManage = function(){
	/**
	* 函数初始化
	**/
	function Init(){
		CopyData();
		logisDetailsAjax();
	};

	 /**
     * 复制数据
     * add by guoran 20151201
     */
    function CopyData() {
        var client = new ZeroClipboard($('.btn-copy'));
        client.on('ready', function(event) {
            client.on('copy', function(event) {
                ZeroClipboard.clearData();
                event.clipboardData.setData('text/plain', $(event.target).data('clipboard-text'));
            }).on('aftercopy', function(event) {
               layer.msg('复制成功', {
                    offset: 0,
                    icon: 1,
                    time: 2000
                });
            });
        }).on('error', function(event) {
            ZeroClipboard.destroy();
        });
    }

    /**
     * 提示数据
     * add by lijunfang 20160413
     */
    function Tip(){
        $('.trip10').popover({
            trigger: 'hover',
            container: 'body',
            html: true,
            placement: 'bottom',
            content: function() {
            	
            	var htmlContent=$(this).siblings('.logistics-message').html();
            	console.log(htmlContent);
            	return htmlContent;
            }
        });
    }
    
    //ajax 审核通过
    function logisDetailsAjax(){
    	$.ajax({
    		cache : false,
    		type : 'POST',
    		dataType : "html",
    		data:{"orderId":$("#orderId").val()},
    		url : "/order/logisDetails.sc",
    		success : function(data) {
    			$("#logisDetails").html("");
    			$("#logisDetails").append(data);
    			Tip();
    		}
    	});
    }
	
	function back(src){
		var url = '/order/orderList.sc';
		if(src == 'refund'){
			url = '/afterSale/applyList.sc';
		}else if(src == 'afterSale'){
			url = '/afterSale/afterSaleList.sc';
		}
		window.location.href = url;
	}
	
	$(function(){
		Init();
	})

	return{
		Back: back
	}
}();



function saveRemark(){
	var markNote = $('#remarkTextarea').val().trim();
	YouGou.Ajax.doPost({
		successMsg: '保存备注成功！',
		errorMsg:'保存备注失败！',
		data:{ "orderId":$("#orderId").val(),
			   "markNote":markNote
			 },
		url : "/order/saveRemark.sc",
		dataType:"json",
		success : function(data) {
			$('#remarkTextarea').html("");
			window.location.reload();
		}
	});
}
