/*************
	OrderRefund
**************/


//$("#createTimeStart").calendar({maxDate:'#qaTimeEnd'});
//$("#qaTimeEnd").calendar({minDate:'#createTimeStart'});
J('#createTimeStart').calendar({maxDate:'#createTimeEnd',format:'yyyy-MM-dd HH:mm:ss'});
J('#createTimeEnd').calendar({minDate:'#createTimeStart',format:'yyyy-MM-dd HH:mm:ss'});
J('#payTimeStart').calendar({maxDate:'#payTimeEnd',format:'yyyy-MM-dd HH:mm:ss'});
J('#payTimeEnd').calendar({minDate:'#payTimeStart',format:'yyyy-MM-dd HH:mm:ss'});

//提交表单查询
function mySubmit() {
	var searchForm = document.getElementById("searchForm");
	searchForm.submit();
}

function view(refundNo){
	window.location.href="/afterSale/getRefundDetails.sc?refundNo="+refundNo
}
