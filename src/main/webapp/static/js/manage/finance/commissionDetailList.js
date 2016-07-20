/*************
	CommissionDetail
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="edit">处理</a>&nbsp;&nbsp;');
    return html.join('');
};

// 列集合
var cols1 = [
	{ title:'订单号', name:'wfxOrderDetailNo', align:'center'},
	{ title:'下单时间', name:'orderTime', width:125, align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'分销商编码', name:'wfxShopName', width:125,align:'center'},
	{ title:'分销商账号', name:'loginName', width:125,align:'center'},
	{ title:'商品编码', name:'commodityNo', align:'center'},
	{ title:'商家编码', name:'supplierCode', align:'center'},
	{ title:'品牌', name:'brandNo', align:'center'},
	{ title:'一级分类', name:'baseCatId1', align:'center'},
	{ title:'二级分类', name:'baseCatId2', align:'center'},
	{ title:'确认收货/退货时间', name:'confirmTime', width:125, align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'结算类型', name:'settlementType',align:'center',renderer: function(val,item,rowIndex){
		var str = "";
		if (item.settlementType=='0'){
			str="妥投结算";
		}else if(item.settlementType=='1'){
			str="退货结算";
		}
		return str;
	}},
	{ title:'数量', name:'commodityQuantity', align:'center'},
	{ title:'单价', name:'goodsPrice', align:'center'},
	{ title:'商品金额', name:'goodsAmount', align:'center'},
	{ title:'运费', name:'freightCharges', align:'center'},
	{ title:'扣佣比例', name:'deductRatio',align:'center',renderer: function(val,item,rowIndex){
		return item.deductRatio+'%';
	}},
	{ title:'佣金收益', name:'commissionAmount', align:'center'},
	{ title:'状态', name:'status',align:'center',renderer: function(val,item,rowIndex){
		var str = "";
		if (item.status=='0'){
			str="未结算";
		}else if(item.status=='1'){
			str="已结算";
		}else if(item.status=='2'){
			str="异常挂起";
		}else if(item.status=='3'){
			str="已关闭";
		}
		return str;
	}},
	{ title:'结算时间', name:'updateTime', width:125, align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'创建时间', name:'createTime', width:125, align:'center', renderer: YouGou.Util.timeFixed}
];

//列集合
var cols2 = [
	{ title:'订单号', name:'wfxOrderDetailNo', align:'center'},
	{ title:'下单时间', name:'orderTime', width:125, align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'分销商编码', name:'wfxShopName',width:125, align:'center'},
	{ title:'分销商账号', name:'loginName',width:125, align:'center'},
	{ title:'商品编码', name:'commodityNo', align:'center'},
	{ title:'商家编码', name:'supplierCode', align:'center'},
	{ title:'品牌', name:'brandNo', align:'center'},
	{ title:'一级分类', name:'baseCatId1', align:'center'},
	{ title:'二级分类', name:'baseCatId2', align:'center'},
	{ title:'确认收货/退货时间', name:'confirmTime', width:125, align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'结算类型', name:'settlementType',align:'center',renderer: function(val,item,rowIndex){
		var str = "";
		if (item.settlementType=='0'){
			str="妥投结算";
		}else if(item.settlementType=='1'){
			str="退货结算";
		}
		return str;
	}},
	{ title:'数量', name:'commodityQuantity', align:'center'},
	{ title:'单价', name:'goodsPrice', align:'center'},
	{ title:'商品金额', name:'goodsAmount', align:'center'},
	{ title:'运费', name:'freightCharges', align:'center'},
	{ title:'扣佣比例', name:'deductRatio',align:'center',renderer: function(val,item,rowIndex){
		return item.deductRatio+'%';
	}},
	{ title:'佣金收益', name:'commissionAmount', align:'center'},
	{ title:'状态', name:'status',align:'center',renderer: function(val,item,rowIndex){
		var str = "";
		if (item.status=='0'){
			str="未结算";
		}else if(item.status=='1'){
			str="已结算";
		}else if(item.status=='2'){
			str="异常挂起";
		}else if(item.status=='3'){
			str="已关闭";
		}
		return str;
	}},
	{ title:'操作时间', name:'updateTime', width:125, align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'创建时间', name:'createTime', width:125, align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'备注', name:'remark', align:'center'},
	{ title:'操作', name:'' , align:'center',lockWidth:true, renderer: actionFixed}
];

var cols;
if($('#status').val()=='2'){
	cols = cols2;
}else{
	cols = cols1;
}

//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");

// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols:cols,
	url: '/finance/queryCommissionDetailData.sc',
	fullWidthRows: true,
	autoLoad: true,
	nowrap:true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    if(action == "edit"){//处理
    	commissionExceptionPage(item.id);
    }
    e.stopPropagation();  //阻止事件冒泡
});

J('#confirmStartTime').calendar({maxDate:'#confirmEndTime',format:'yyyy-MM-dd HH:mm:ss'});
J('#confirmEndTime').calendar({minDate:'#confirmStartTime',format:'yyyy-MM-dd HH:mm:ss'});
J('#settleStartTime').calendar({maxDate:'#settleEndTime',format:'yyyy-MM-dd HH:mm:ss'});
J('#settleEndTime').calendar({minDate:'#settleStartTime',format:'yyyy-MM-dd HH:mm:ss'});

function commissionExceptionPage(id) {
	if(id==undefined||id==null||id==''){
		return;
	}
 	YouGou.UI.progressLoading();
 	$.ajax({
 	       type: "post",
 	       url: '/finance/commissionExceptionPage.sc',
 	       data: {"id":id},
 	       async: false,
 	       success : function(data) {
 	    	   YouGou.UI.progressStop();
 	    	   if (data) {
 	    		  YouGou.UI.Dialog.show({
	 					title: "异常挂起-处理界面",
	 					message : data.replace(/(\n)+|(\r\n)+/g, ""),
	 					buttons: [{
			 		                label: '取消',
			 		                action: function(dialog) {
			 		                    dialog.close();
			 		                }
			 		            }, 
			 		            {
			 		                label: '提交',
			 		                cssClass: 'btn-yougou',
			 		                action: function(dialog) {
			 		                	commissionExceptionDeal(dialog);
			 		                }
		 		            }]
	 				});
 	    	   }
 		  }
   }); 
}

function commissionExceptionDeal(dialog){
	var dealType = $('input[name="dealType"]:checked').val();
	if(YouGou.Util.isEmpty(dealType)){
		$("#dealType_stip").html("处理方式必须选择！");
		return false;
	}
	var remark = $("#remark").val();
	if(YouGou.Util.isEmpty(remark)){
		$("#remark_stip").html("备注不能为空！");
		return false;
	}
	
	var accountBalance = parseFloat($("#accountBalance").val());
	var commissionAmount = parseFloat($("#commissionAmount").val());
	if((accountBalance+commissionAmount)<0 && dealType=='1'){
		$("#accountBalance_stip").html("账户余额不足，不能结算！");
		return false;
	}
	var id = $("#id").val();
	YouGou.Ajax.doPost({
		successMsg: "异常挂起处理成功",
		url: '/finance/commissionExceptionDeal.sc',
	    data: {"dealType":dealType,"id":id,"remark":remark,"accountBalance":accountBalance,"commissionAmount":commissionAmount},
	  	success : function() {
			mmGrid.load();
	  		dialog.close();
  		}
	});
}

//佣金明细汇总生成调度
function dispatchCreateAccountBalance() {
	$('#searchForm').attr('action','/finance/dispatchCreateAccountBalance.sc');
	$("#searchForm").submit();
}
//导出数据
function exportCommissionDetail() {
	$('#searchForm').attr('action','/finance/exportCommissionDetail.sc');
	$("#searchForm").submit();
}
//查询
function doQuery() {
	mmGrid.load();
}
