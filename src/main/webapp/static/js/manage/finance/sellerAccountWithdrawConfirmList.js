/*************
	FinSellerAccountWithdraw
**************/
// 操作列动作
var confirmActionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="confirm">确认</a>&nbsp;&nbsp;');
    return html.join('');
};

var payActionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="pay">付款</a>&nbsp;&nbsp;');
    return html.join('');
};

//待确认列集合
var confirmCols = [
	{ title:'提现申请单号', name:'withdrawApplyNo', width: 135, align:'left'},
	{ title:'申请时间', name:'applyTime', align:'center', width: 126, renderer: YouGou.Util.timeFixed},
	{ title:'店铺编码', name:'shopName', width: 90, align:'left'},
	{ title:'分销商账号', name:'sellerAccount', align:'left'},
	{ title:'账户余额', name:'accountBalance', align:'right'},
	{ title:'实收费用', name:'actualReceivedAmount', align:'right', hidden: true},
	{ title:'服务费', name:'serviceAmount', align:'right', hidden: true},
	{ title:'提现金额', name:'withdrawAmount', align:'right'},
	{ title:'支付方式', name:'accountBankName', align:'left'},
	{ title:'开户名', name:'accountName', align:'left'},
	{ title:'对方开户行', name:'accountBankAllName', width: 160, align:'left'},
	{ title:'账号', name:'accountNo', width: 180, align:'left'},
	{ title:'申请原因说明', name:'applyReason', width: 160, align:'left'},
	{ title:'申请人', name:'applyer', align:'left'},
	{ title:'审核人', name:'operater', align:'left'},
	{ title:'审核时间', name:'operaterTime', align:'center', width: 126, renderer: YouGou.Util.timeFixed},
	{ title:'单据状态', name:'billStatus', align:'left', renderer:function(val,item,rowIndex){
  		if (item.billStatus == '1') {
  			return '待审核'
  		} else if (item.billStatus == '2') {
  			return '待确认'
  		} else if (item.billStatus=='5') {
			return '待付款'
		} else if (item.billStatus == '3') {
  			return '已提现'
  		} else if (item.billStatus == '4') {
  			return '审核拒绝'
  		}
  	}},
	{ title:'备注说明', name:'remark', align:'left'},
	{ title:'操作', name:'' ,width:80, align:'center',lockWidth:true, renderer: confirmActionFixed},
    { title:'ID', name:'id', hidden: true}
];

//待付款列集合
var payCols = [
	{ title:'提现申请单号', name:'withdrawApplyNo', width: 135, align:'left'},
	{ title:'申请时间', name:'applyTime', align:'center', width: 126, renderer: YouGou.Util.timeFixed},
	{ title:'店铺编码', name:'shopName', width: 90, align:'left'},
	{ title:'分销商账号', name:'sellerAccount', align:'left'},
	{ title:'账户余额', name:'accountBalance', align:'right'},
	{ title:'实收费用', name:'actualReceivedAmount', align:'right', hidden: true},
	{ title:'服务费', name:'serviceAmount', align:'right', hidden: true},
	{ title:'提现金额', name:'withdrawAmount', align:'right'},
	{ title:'支付方式', name:'accountBankName', align:'left'},
	{ title:'开户名', name:'accountName', align:'left'},
	{ title:'对方开户行', name:'accountBankAllName', width: 160, align:'left'},
	{ title:'账号', name:'accountNo', width: 180, align:'left'},
	{ title:'申请原因说明', name:'applyReason', width: 160, align:'left'},
	{ title:'申请人', name:'applyer', align:'left'},
	{ title:'审核人', name:'operater', align:'left'},
	{ title:'审核时间', name:'operaterTime', align:'center', width: 126, renderer: YouGou.Util.timeFixed},
	{ title:'单据状态', name:'billStatus', align:'left',renderer:function(val,item,rowIndex){
  		if (item.billStatus == '1') {
  			return '待审核'
  		} else if (item.billStatus == '2') {
  			return '待确认'
  		} else if (item.billStatus=='5') {
			return '待付款'
		} else if (item.billStatus == '3') {
  			return '已提现'
  		} else if (item.billStatus == '4') {
  			return '审核拒绝'
  		}
  	}},
	{ title:'备注说明', name:'remark', align:'left'},
	{ title:'操作', name:'' ,width:80, align:'center',lockWidth:true, renderer: payActionFixed},
    { title:'ID', name:'id', hidden: true}
];

var allCols = [
	{ title:'提现申请单号', name:'withdrawApplyNo', width: 135, align:'left'},
	{ title:'申请时间', name:'applyTime', align:'center', width: 126, renderer: YouGou.Util.timeFixed},
	{ title:'店铺编码', name:'shopName', width: 180, align:'left'},
	{ title:'分销商账号', name:'sellerAccount', align:'left'},
	{ title:'账户余额', name:'accountBalance', align:'right'},
	{ title:'实收费用', name:'actualReceivedAmount', align:'right', hidden: true},
	{ title:'服务费', name:'serviceAmount', align:'right', hidden: true},
	{ title:'提现金额', name:'withdrawAmount', align:'right'},
	{ title:'支付方式', name:'accountBankName', align:'left'},
	{ title:'开户名', name:'accountName', align:'left'},
	{ title:'对方开户行', name:'accountBankAllName', width: 160, align:'left'},
	{ title:'账号', name:'accountNo', width: 180, align:'left'},
	{ title:'申请原因说明', name:'applyReason', width: 160, align:'left'},
	{ title:'申请人', name:'applyer', align:'left'},
	{ title:'审核人', name:'operater', align:'left'},
	{ title:'审核时间', name:'operaterTime', align:'center', width: 126, renderer: YouGou.Util.timeFixed},
	{ title:'付款人', name:'modifier', align:'left'},
	{ title:'提现时间', name:'modifyTime', align:'center', width: 126, renderer: YouGou.Util.timeFixed},
	{ title:'单据状态', name:'billStatus', align:'left',renderer:function(val,item,rowIndex){
		if (item.billStatus == '1') {
			return '待审核'
		} else if (item.billStatus == '2') {
			return '待确认'
		} else if (item.billStatus == '5') {
			return '待付款'
		} else if (item.billStatus == '3') {
			return '已提现'
		} else if (item.billStatus == '4') {
			return '审核拒绝'
		}
	}},
	{ title:'备注说明', name:'remark', align:'left'}
];

//初始日期格控件
J('#applyTimeStart').calendar({maxDate:'#applyTimeEnd',format:'yyyy-MM-dd'}); 
J('#applyTimeEnd').calendar({minDate:'#applyTimeStart',format:'yyyy-MM-dd'});

var cols;
if ($('#billStatus').val() == '2') {
	cols = confirmCols;
} else if ($('#billStatus').val() == '5') {
	cols = payCols;
} else {
	cols = allCols;
	J('#withdrawalTimeStart').calendar({maxDate:'#withdrawalTimeEnd',format:'yyyy-MM-dd'}); 
	J('#withdrawalTimeEnd').calendar({minDate:'#withdrawalTimeStart',format:'yyyy-MM-dd'});
}

//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");
// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/finance/accountwithdraw/querySellerData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

//表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    //财务确认
    if (action == "confirm") {
    	queryConfirmDetail(item.id);
    } 
    //财务付款
    else if (action == "pay") {
    	queryPayDetail(item.id);
    }
    e.stopPropagation();  //阻止事件冒泡
});

//查询
function doQuery() {
	mmGrid.load();
}

/**
 * 查询确认详情
 * @param id
 */
function queryConfirmDetail(id) {
	YouGou.UI.progressLoading();
	$.ajax({
		type: "post",
        url: '/finance/accountwithdraw/queryDetailById.sc',
        data: {"id": id},
        async: false,
        success: function (data) {
        	YouGou.UI.progressStop();
        	YouGou.UI.Dialog.show({
        		title: "提现确认详情",
        		closable: true,
        		closeByBackdrop: false,
        		message : data.replace(/(\n)+|(\r\n)+/g, ""),
        		buttons: [
        		    {
        		    	label: '确认',
        		    	cssClass: 'btn-sm btn-yougou',
 		                action: function (dialog) {
 		                	auditConfirmBillEvent(dialog);
 		                }
        		    },
        		    {
        		    	label: '审核拒绝',
        		    	cssClass: 'btn-sm btn-yougou',
 		                action: function (dialog) {
 		                	auditRefusedBillEvent(dialog);
 		                }
        		    },
        		    {
        		    	label: '关闭',
        		    	cssClass: 'btn-sm btn-yougou',
 		                action: function (dialog) {
 		                	dialog.close();
 		                }
        		    }
	            ]
        	});
        }
	});
}

/**
 * 查询付款详情
 * @param id
 */
function queryPayDetail(id) {
	YouGou.UI.progressLoading();
	$.ajax({
		type: "post",
        url: '/finance/accountwithdraw/queryDetailById.sc',
        data: {"id": id},
        async: false,
        success: function (data) {
        	YouGou.UI.progressStop();
        	YouGou.UI.Dialog.show({
        		title: "提现付款详情",
        		closable: true,
        		closeByBackdrop: false,
        		message : data.replace(/(\n)+|(\r\n)+/g, ""),
        		buttons: [
        		    {
        		    	label: '确认付款',
        		    	cssClass: 'btn-sm btn-yougou',
 		                action: function (dialog) {
 		                	modifyBillEvent(dialog);
 		                }
        		    },
        		    {
        		    	label: '审核拒绝',
        		    	cssClass: 'btn-sm btn-yougou',
 		                action: function (dialog) {
 		                	auditRefusedBillEvent(dialog);
 		                }
        		    },
        		    {
        		    	label: '关闭',
        		    	cssClass: 'btn-sm btn-yougou',
 		                action: function (dialog) {
 		                	dialog.close();
 		                }
        		    }
	            ]
        	});
        }
	});
}

//提现申请单，审核确认事件
function auditConfirmBillEvent(dialog) {
	var id = $('#idAudit').val();
	var remark = $('#remarkAudit').val();
	YouGou.UI.progressLoading();
	$.ajax({
		type: "post",
		url: "/finance/accountwithdraw/auditConfirmApplyBill.sc",
		data:{'id':id,'remark':remark},
		async : false,
		success : function(data) {
			YouGou.UI.progressStop();
			if (data.code == '100') {
				//执行完审核时间重新查询
				doQuery();
				dialog.close();
				YouGou.UI.Dialog.alert({message:data.message});
			} else if (data.code == '500'){
				//执行完审核时间重新查询
				doQuery();
				dialog.close();
				YouGou.UI.Dialog.alert({message:data.message});
			}
		}
	});
}

//提现申请单，审核拒绝事件
function auditRefusedBillEvent(dialog){
	var id = $('#idAudit').val();
	var remark = $('#remarkAudit').val();
	if (YouGou.Util.isEmpty(remark, false) || $.trim(remark) == "") {
		YouGou.UI.Dialog.alert({message:"请输入备注说明！"});
		return ;
	}
	YouGou.UI.progressLoading();
	$.ajax({
		type: "post",
		url: "/finance/accountwithdraw/auditRefuseApplyBill.sc",
		data:{'id':id,'remark':remark},
		async : false,
		success : function(data) {
			YouGou.UI.progressStop();
			if (data.code == '100') {
				//执行完审核时间重新查询
				doQuery();
				dialog.close();
				YouGou.UI.Dialog.alert({message:data.message});
			} else if (data.code == '500') {
				//执行完审核时间重新查询
				doQuery();
				dialog.close();
				YouGou.UI.Dialog.alert({message:data.message});
			}
		}
	});
}

//提现申请单，确认付款事件
function modifyBillEvent(dialog) {
	var id = $('#idAudit').val();
	var remark = $('#remarkAudit').val();
	YouGou.UI.progressLoading();
	$.ajax({
		type: "post",
		url: "/finance/accountwithdraw/modifyApplyBill.sc",
		data:{'id':id,'remark':remark},
		async : false,
		success : function(data) {
			YouGou.UI.progressStop();
			if (data.code == '100') {
				//执行完付款事件重新查询
				doQuery();
				dialog.close();
				YouGou.UI.Dialog.alert({message:data.message});
			} else if (data.code == '500'){
				//执行完付款事件重新查询
				doQuery();
				dialog.close();
				YouGou.UI.Dialog.alert({message:data.message});
			}
		}
	});
}

//废弃
//付款拒绝事件，废弃
function refusedPaymentBill(){
	var id = $('#idModify').val();
	var auditRemark = $('#remarkAuditModify').val();
	var remark = auditRemark+';'+$('#remarkModify').val();
	$.ajax({
		type: "post",
		url: "/finance/accountwithdraw/refusedModifyApplyBill.sc",
		data:{'id':id,'remark':remark},
		async : false,
		success : function(data) {
			if(data.code=='100'){
				$('#modifyModal').modal('hide')
				//执行完付款事件重新查询
				doQuery();
				YouGou.UI.Dialog.alert({message:data.message});
			}else if(data.code=='500'){
				$('#modifyModal').modal('hide')
				//执行完付款事件重新查询
				doQuery();
				YouGou.UI.Dialog.alert({message:data.message});
			}
		}
	});
}

//导出提现列表
function exportConfirmDetailExcel() {
	$('#searchForm').attr('action', '/finance/accountwithdraw/exportConfirmDetailExcel.sc');
	$('#searchForm').submit();
}
