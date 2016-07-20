/*************
	CommodityCatb2c
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	var status = item.status;
	if(status == '0'){
		html.push('<a href="javascript:void(0);" action="enable">启用</a>&nbsp;&nbsp;');
	}else{
		html.push('<a href="javascript:void(0);" action="disable">停用</a>&nbsp;&nbsp;');
	}
    return html.join('');
};

var levelFixed = function(val){
	if(!val){
		return '';
	}else if(val == '1'){
		return '一级';
	}else if(val == '2'){
		return '二级';
	}else if(val == '3'){
		return '三级';
	}
}

var statusFixed = function(val){
	if(val == '0'){
		return '停用';
	}else{
		return '启用';
	}
}

// 列集合
var cols = [
	{ title:'类别名称', name:'catName',width:150, align:'left'},
	{ title:'分类等级', name:'level', align:'center',renderer:levelFixed},
	{ title:'状态', name:'status', align:'left',renderer:statusFixed},
	{ title:'类别编码', name:'no', align:'left'},
	{ title:'sku数量', name:'skuNum', align:'left'},
	{ title:'最后更新人', name:'updatePerson', align:'left'},
	{ title:'最后更新时间', name:'updateDate', width:125,align:'center',renderer : YouGou.Util.timeFixed},
	{ title:'操作', name:'' ,width:100, align:'center',lockWidth:true, renderer: actionFixed},
    { title:'ID', name:'id', hidden: true}
];


function loadTable(parentId){
	$.get('/commodity/queryBasicCatListByPid.sc?parentId='+parentId, function (data) {
        $('#goodsCategoryList').html(data);
    });
}
//停用或启用分类
function updateStatus(id,status,parentId){
	var str = '停用';
	if(status == '1'){
		str = '启用';
	}
	YouGou.UI.Dialog.confirm({
		message : "确定要"+str+"吗？"
	},function(result){
		if(result) {
			YouGou.Ajax.doPost({
				successMsg: str+'成功！',
				url: '/commodity/updateBasicCat.sc',
			  	data: { "id" : id, "status" : status },
			  	success : function(data){
			  		loadTable(parentId);
				}
			});
		}
	});
}

//删除记录
function removeBasicCat(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/commodity/removeBasicCat.sc',
	  	data: { "id" : id },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}

//查询
function doQuery(){
	mmGrid.load();
}