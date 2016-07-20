/*************
	CommissionPercent
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	if(item.type!=null&&item.type!=1){
		html.push('<a href="javascript:void(0);" action="edit">编辑</a>&nbsp;&nbsp;');
		html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
	}
    return html.join('');
};

//品牌分类佣金比例设置 列集合
var cols2 = [
	{ title:'品牌名称', name:'brandName', align:'center'},
	{ title:'分类名称', name:'catName',align:'center'},
	{ title:'分类级别', name:'baseCatLevel',align:'center',renderer: function(val,item,rowIndex){
		var str = "";
		if (item.baseCatLevel=='1'){
			str="一级分类";
		}else if(item.baseCatLevel=='2'){
			str="二级分类";
		}else{
			str="————";
		}
		return str;
	}},
	{ title:'一级佣金比例', name:'commissionLevel1Percent',align:'center',renderer: function(val,item,rowIndex){
		var str = item.commissionLevel1Percent+'%';
		return str;
	}},
	{ title:'二级佣金比例', name:'commissionLevel2Percent',align:'center',renderer: function(val,item,rowIndex){
		var str = item.commissionLevel2Percent+'%';
		return str;
	}},
	{ title:'三级佣金比例', name:'commissionLevel3Percent',align:'center',renderer: function(val,item,rowIndex){
		var str = item.commissionLevel3Percent+'%';
		return str;
	}},
	{ title:'创建用户', name:'createUser', align:'center'},
	{ title:'创建时间', name:'createTime',width:125, lockWidth:true, align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'最后更新用户', name:'updateUser', align:'center'},
	{ title:'最后更新时间', name:'updateTime',width:125, lockWidth:true, align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'操作', name:'id' ,width:80, align:'center',lockWidth:true, renderer: actionFixed}
];

//单品佣金比例设置列集合
var cols3 = [
	{ title:'款色编码', name:'supplierCode', align:'center'},
	{ title:'商品名称', name:'commodityName',align:'center',width:250, align:'center',lockWidth:true},
	{ title:'一级佣金比例', name:'commissionLevel1Percent',align:'center',width:80, align:'center',lockWidth:true,renderer: function(val,item,rowIndex){
		var str = item.commissionLevel1Percent+'%';
		return str;
	}},
	{ title:'二级佣金比例', name:'commissionLevel2Percent',align:'center',width:80, align:'center',lockWidth:true,renderer: function(val,item,rowIndex){
		var str = item.commissionLevel2Percent+'%';
		return str;
	}},
	{ title:'三级佣金比例', name:'commissionLevel3Percent',align:'center',width:80, align:'center',lockWidth:true,renderer: function(val,item,rowIndex){
		var str = item.commissionLevel3Percent+'%';
		return str;
	}},
	{ title:'创建用户', name:'createUser', align:'center'},
	{ title:'创建时间', name:'createTime',width:125, lockWidth:true, align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'最后更新用户', name:'updateUser', align:'center'},
	{ title:'最后更新时间', name:'updateTime',width:125, lockWidth:true, align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'操作', name:'id' ,width:80, align:'center',lockWidth:true, renderer: actionFixed}
];
var cols;
if($('#type').val()==3){
	cols = cols3;
}else{
	cols = cols2;
}
//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("categoryCommissionForm");

// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/finance/queryCommissionSetData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});


// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    //编辑
    if(action == "edit"){
    	if($('#type').val()==3){//跳转单品佣金比例设置编辑页面
    		window.location.href="/finance/commissionSetCommodiy.sc?id=" +item.id;
    	}else{//跳转品牌分类佣金比例设置编辑页面
    		window.location.href="/finance/commissionSetBrand.sc?id=" +item.id;
    	}
    }else if(action == "delete"){// 删除
   		YouGou.UI.Dialog.confirm({
   			message : "确定要删除吗？"
   		},function(result){
   			if(result) {
                removeCategoryCommission(item.id);
            }
   		});
    }
    e.stopPropagation();  //阻止事件冒泡
});

function removeCategoryCommission(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/finance/removeCommissionSet.sc',
	  	data: { "id" : id },
	  	success : function(data){
	  		doQuery();
		}
	});
}

$('#add-quick').click(function(){
	if($('#type').val()==3){//跳转单品佣金比例设置编辑页面
		window.location.href="/finance/commissionSetCommodiy.sc";
	}else{//跳转品牌分类佣金比例设置编辑页面
		window.location.href="/finance/commissionSetBrand.sc";
	}
});

//默认佣金比例设置和保存
function defaultCommissionSetBtn(){
	var defaultCommission1=$("#defaultCommission1").val();//没有设置过的佣金比例
	var defaultCommission2=$("#defaultCommission2").val();
	var defaultCommission3=$("#defaultCommission3").val();
	 $("#setBtn").empty();
	 $("#commissionTD1").empty();
	 $("#commissionTD2").empty();
	 $("#commissionTD3").empty();
	 $("#commissionTD1").append('<input  name="commissionLevel1Percent" id="commissionLevel1Percent" value="'+
			 defaultCommission1+'" type="text"/>%');
	 $("#commissionTD2").append('<input  name="commissionLevel2Percent" id="commissionLevel2Percent" value="'+
			 defaultCommission2+'" type="text"/>%');
	 $("#commissionTD3").append('<input  name="commissionLevel3Percent" id="commissionLevel3Percent" value="'+
			 defaultCommission3+'" type="text"/>%');
	 $("#setBtn").append('<input type="button" value="保存" class="btn btn-yougou" onclick="defaultCommissionSaveBtn();"/>');
}
function defaultCommissionSaveBtn(){
	var commissionLevel1Percent=$("#commissionLevel1Percent").val();
	var commissionLevel2Percent=$("#commissionLevel2Percent").val();
	var commissionLevel3Percent=$("#commissionLevel3Percent").val();
	if(YouGou.Util.isEmpty(commissionLevel1Percent)||YouGou.Util.isEmpty(commissionLevel2Percent)
	  ||YouGou.Util.isEmpty(commissionLevel3Percent)){
		YouGou.UI.Dialog.alert({message:"佣金比例不能为空"});
		return;
	}
	var re = /^[1-9]{0,1}\d{0,1}$|^[1-9]{0,1}\d{0,1}\.\d{1,2}$/;
	if(commissionLevel1Percent<0||commissionLevel2Percent<0||commissionLevel3Percent<0||
	  !re.test(commissionLevel1Percent)||!re.test(commissionLevel2Percent)||!re.test(commissionLevel3Percent)){
		YouGou.UI.Dialog.alert({message:"佣金比例只能为不超过两位的正整数或者保留两位的小数"});
		return;
	}
	var id=$("#defaultCommissionId").val();
	YouGou.Ajax.doPost({
		successMsg: "设置成功！",
		url: '/finance/saveDefaultCommission.sc',
	  	data: { "commissionLevel1Percent" : commissionLevel1Percent,"commissionLevel2Percent":commissionLevel2Percent
	  		    ,"commissionLevel3Percent":commissionLevel3Percent,"id":id },
	  	success : function(){
	  		 $("#setBtn").empty();
	  		 $("#commissionTD1").empty();
	  		 $("#commissionTD2").empty();
	  		 $("#commissionTD3").empty();
	  		 $("#defaultCommission1").val(commissionLevel1Percent);
	  		 $("#defaultCommission2").val(commissionLevel2Percent);
	  		 $("#defaultCommission3").val(commissionLevel3Percent);
	  		 $("#commissionTD1").append('<font>'+commissionLevel1Percent+'</font>%');
	  		 $("#commissionTD2").append('<font>'+commissionLevel2Percent+'</font>%');
	  		 $("#commissionTD3").append('<font>'+commissionLevel3Percent+'</font>%');
	  		 $("#setBtn").append('<input type="button" value="设置" class="btn btn-yougou" onclick="defaultCommissionSetBtn();"/>');
		}
	});
}

function doQuery(){
	mmGrid.load();
}

