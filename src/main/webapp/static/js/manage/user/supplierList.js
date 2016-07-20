/*************
	Supplier
**************/
var stateFixed = function(val,item,rowIndex){
	var stateStr = '';
	if(val == '1'){
		stateStr = '开启';
	}else if(val == '2'){
		stateStr = '关闭';
	}
	return stateStr;
};
//操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="/afterSale/supplierAddressList.sc?supplierCode='+item.supplierCode+'" action="select">地址管理</a>&nbsp;&nbsp;');
    return html.join('');
};
// 列集合
var cols = [
    { title:'供货商名称', name:'supplierName', align:'left'},
    { title:'供货商编码', name:'supplierCode', align:'center'},
	{ title:'状态', name:'state', align:'center',renderer:stateFixed},
	{ title:'创建时间', name:'createDate', align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'操作', name:'' , align:'center',lockWidth:true, renderer: actionFixed},
    { title:'ID', name:'id', hidden: true}
];

//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");

// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/user/querySupplierData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    //编辑
    if(action == "edit"){
    	YouGou.UI.Dialog.alert({message:"编辑"});
    }else if(action == "delete"){// 删除
   		YouGou.UI.Dialog.confirm({
   			message : "确定要删除吗？"
   		},function(result){
   			if(result) {
                removeSupplier(item.id);
            }
   		});
    }else if(action == "select"){//查看
    	YouGou.UI.Dialog.alert({message:"查看"});
    }
    e.stopPropagation();  //阻止事件冒泡
});
//删除
function removeSupplier(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/user/removeSupplier.sc',
	  	data: { "id" : id },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}
//更新状态时使用
function updateSupplier(id,status){
	YouGou.Ajax.doPost({
		successMsg: '更新成功！',
		url: '/user/saveSupplier.sc',
	  	data: { "id" : id ,"status" : status },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}
//查询
function doQuery(){
	mmGrid.load();
}
J('#createDateStart').calendar({maxDate:'#createDateEnd',format:'yyyy-MM-dd HH:mm:ss'});
J('#createDateEnd').calendar({minDate:'#createDateStart',format:'yyyy-MM-dd HH:mm:ss'});