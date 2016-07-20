/*************
	FinSellerAccountWithdraw
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="audit">审批</a>&nbsp;&nbsp;');
    return html.join('');
};

// 待审核列集合
var auditCols = [
	{ title:'提现申请单号', name:'withdrawApplyNo', width: 135, align:'left'},
	{ title:'申请时间', name:'applyTime', align:'left', width: 126, renderer: YouGou.Util.timeFixed},
	{ title:'店铺编码', name:'shopName', width: 180, align:'left'},
	{ title:'分销商账号', name:'sellerAccount', align:'left'},
	{ title:'提现金额', name:'withdrawAmount', align:'right'},
	{ title:'支付方式', name:'accountBankName', align:'left'},
	{ title:'开户名', name:'accountName', align:'left'},
	{ title:'操作', name:'' ,width:80, align:'center',lockWidth:true, renderer: actionFixed},
    { title:'ID', name:'id', hidden: true}
];

//初始日期格控件
J('#applyTimeStart').calendar({maxDate:'#applyTimeEnd',format:'yyyy-MM-dd'}); 
J('#applyTimeEnd').calendar({minDate:'#applyTimeStart',format:'yyyy-MM-dd'});

//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");
// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: auditCols,
	url: '/finance/accountwithdraw/querySellerData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

//表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    //提现申请单审核详情
    if (action == "audit") {
    	queryAuditDetail(item.id);
    } 
    e.stopPropagation();  //阻止事件冒泡
});

//查询
function doQuery() {
	mmGrid.load();
}

/**
 * 查询审核详情
 * @param id
 */
function queryAuditDetail(id) {
	YouGou.UI.progressLoading();
	$.ajax({
		type: "post",
        url: '/finance/accountwithdraw/queryDetailById.sc',
        data: {"id": id},
        async: false,
        success: function (data) {
        	YouGou.UI.progressStop();
        	YouGou.UI.Dialog.show({
        		title: "提现审核详情",
        		closable: true,
        		closeByBackdrop: false,
        		message : data.replace(/(\n)+|(\r\n)+/g, ""),
        		buttons: [
        		    {
        		    	label: '审核通过',
        		    	cssClass: 'btn-sm btn-yougou',
 		                action: function (dialog) {
 		                	auditBillEvent(dialog);
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

//提现申请单，审核通过事件
function auditBillEvent(dialog) {
	var id = $('#idAudit').val();
	var remark = $('#remarkAudit').val();
	YouGou.UI.progressLoading();
	$.ajax({
		type: "post",
		url: "/finance/accountwithdraw/auditApplyBill.sc",
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

//导出提现列表
function exportDetailExcel() {
	$('#searchForm').attr('action', '/finance/accountwithdraw/exportDetailExcel.sc');
	$('#searchForm').submit();
}
