/*************
	CommoditySaleCat
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
    return html.join('');
};

// 操作列动作
var scActionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="add">添加</a>&nbsp;&nbsp;');
	return html.join('');
};

var levelFixed = function(val){
	var levelStr = '';
	if(val == '1'){
		levelStr = '一级';
	}else if(val == '2'){
		levelStr = '二级';
	}else if(val == '3'){
		levelStr = '三级';
	}
	return levelStr;
}

var picFixed = function(val){
	var html = '';
	if(val){
		html = '<img class="hotCatPic" name="picImg" src="'+baseUrl+val+'" />';
	}else{
		html = '<img class="hotCatPic" name="picImg" src=""/>';
	}
	return html;
}

var hotSnFixed = function(val,item){
	var html = '';
	html += '<div class="hotSnGroup">';
	html += '<span class="hotSnText">'+val+'</span>';
	html += '<input class="hide hotSnInput" type="text" value="'+val+'"/>';
	html += '<a javascript:void(0) class="editIcon" onclick="editHotSn(this);" title="编辑">&nbsp;</a>';
	html += '<a javascript:void(0) class="hide saveIcon" onclick="saveHotSn(this,\''+item.id+'\');" title="保存">&nbsp;</a>';
	html += '<a javascript:void(0) class="hide cancelIcon" onclick="cancelHotSn(this);" title="取消修改"">&nbsp;</a>';
	html += '</div>';
	return html;
}

var editHotSn = function(obj){
	var hotSnGroup = $(obj).parent();
	hotSnChange(hotSnGroup,'edit');
	var hotSnInput = hotSnGroup.find('.hotSnInput');
	hotSnInput.select();
}

var saveHotSn = function(obj,id){
	var hotSnGroup = $(obj).parent();
	var hotSnText = hotSnGroup.find('.hotSnText');
	var hotSnInput = hotSnGroup.find('.hotSnInput');
	var oldHotSn = hotSnText.text();
	var hotSn = hotSnInput.val();
	if(hotSn==""){
		YouGou.UI.Dialog.autoCloseTip("序号不能为空");
		return;
	}
	if(hotSn != oldHotSn){
		var r = /^\+?[1-9][0-9]*$/;
		if(!r.test(hotSn)){
			YouGou.UI.Dialog.autoCloseTip("序号必须是正整数");
			return;
		}
		YouGou.Ajax.doPost({
			successMsg: '修改成功！',
			url: '/cms/updateHotCatSn.sc',
		  	data: { "id" : id,hotSn: hotSn },
		  	success : function(data){
	  			mmGrid.load();
	  			hotSnChange(hotSnGroup,'save');
			}
		});
	}else{
		hotSnChange(hotSnGroup,'save');
	}
}

var cancelHotSn = function(obj){
	var hotSnGroup = $(obj).parent();
	hotSnChange(hotSnGroup,'cancel');
	var hotSnText = hotSnGroup.find('.hotSnText');
	var hotSnInput = hotSnGroup.find('.hotSnInput');
	hotSnInput.val(hotSnText.text());
}

var hotSnChange = function(hotSnGroup,action){
	var hotSnText = hotSnGroup.find('.hotSnText');
	var hotSnInput = hotSnGroup.find('.hotSnInput');
	var editIcon = hotSnGroup.find('.editIcon');
	var saveIcon = hotSnGroup.find('.saveIcon');
	var cancelIcon = hotSnGroup.find('.cancelIcon');
	if(action == 'edit'){
		hotSnText.addClass('hide');
		hotSnInput.removeClass('hide');
		editIcon.addClass('hide');
		saveIcon.removeClass('hide');
		cancelIcon.removeClass('hide');
	}else{
		hotSnText.removeClass('hide');
		hotSnInput.addClass('hide');
		editIcon.removeClass('hide');
		saveIcon.addClass('hide');
		cancelIcon.addClass('hide');
	}
}

// 列集合
var cols = [
    { title:'分类图片', name:'picUrl', align:'center',renderer:picFixed},
	{ title:'分类名称', name:'name', align:'left'},
	{ title:'分类等级', name:'level', align:'center',renderer:levelFixed},
	{ title:'排序号', name:'hotSn', align:'center',renderer:hotSnFixed},
	{ title:'操作', name:'' ,width:200, align:'center',lockWidth:true, renderer: actionFixed},
    { title:'ID', name:'id', hidden: true}
];

//列集合
var saleCatcols = [
    { title:'分类图片', name:'picUrl',width:230, align:'center',renderer:picFixed},
	{ title:'分类名称', name:'name',width:230, align:'left'},
	{ title:'分类等级', name:'level',width:230, align:'center',renderer:levelFixed},
	{ title:'操作', name:'' ,width:230, align:'center',lockWidth:true, renderer: scActionFixed},
    { title:'ID', name:'id', hidden: true}
];

//分页器
var mmPaginator1 = $('#grid-pager1').mmPaginator({});
//分页器
var mmPaginator2 = $('#grid-pager2').mmPaginator({});
// 搜索表单属性
var mmFormParams1 = new MMSearchFormParams("searchForm1");
// 搜索表单属性
var mmFormParams2 = new MMSearchFormParams("searchForm2");

// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/cms/queryHotCatData.sc',
	checkCol: true,
	multiSelect: true,
	fullWidthRows: true,
	autoLoad: true,
	remoteSort: true,
	plugins: [mmPaginator1,mmFormParams1]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    if(action == "delete"){// 删除
   		YouGou.UI.Dialog.confirm({
   			message : "确定要删除吗？"
   		},function(result){
   			if(result) {
                removeHotCat(item.id);
            }
   		});
    }
    e.stopPropagation();  //阻止事件冒泡
});

//销售分类表格	
var saleCatGrid = $('#saleCat-table').mmGrid({
	height: 'auto',
	cols: saleCatcols,
	url: '/cms/queryLevelOneData.sc',
	checkCol: true,
	multiSelect: true,
	fullWidthRows: true,
	autoLoad: true,
	remoteSort: true,
	plugins: [mmPaginator2,mmFormParams2]
});

//销售分类表格事件
saleCatGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
	if(action == "add"){// 添加
	YouGou.UI.Dialog.confirm({
		message : "确定要添加吗？"
	},function(result){
		if(result) {
			YouGou.Ajax.doPost({
				successMsg: '添加热门分类成功！',
				url: '/cms/insertHotCat.sc',
			  	data: { "id" : item.id },
				  	success : function(data){
			  			hideForm();
					}
				});
			}
		});
	}
	e.stopPropagation();  //阻止事件冒泡
});

//删除
function removeHotCat(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/cms/removeHotCat.sc',
	  	data: { "id" : id },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}
//更新状态时使用
function updateHotCat(id,status){
	YouGou.Ajax.doPost({
		successMsg: '更新成功！',
		url: '/cms/saveHotCat.sc',
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
//查询
function saleCatDoQuery(){
	saleCatGrid.load();
}

var showTable = function(){
	saleCatGrid.load();
	$('#girdContent').addClass('hide');
	$('#saleCatTable').removeClass('hide');
}

var hideForm = function(){
	mmGrid.load();
	$('#girdContent').removeClass('hide');
	$('#saleCatTable').addClass('hide');
}
//批量删除热门分类
var batchRemove = function(){
	var checkObjs = $('#grid-table input:checkbox:checked');
	if(checkObjs.length <= 0){
		YouGou.UI.Dialog.autoCloseTip("请勾选要删除的分类");
		return;
	}
	YouGou.UI.Dialog.confirm({
		message : "确定要批量删除分类吗？"
	},function(result){
		if(result) {
			var idArr = [];
			checkObjs.each(function(){
				var trObj = $(this).parents('#grid-table tr');
				var idSpan = trObj.find('span:last');
				idArr.push(idSpan.text());
			});
			var hotCatIds = idArr.join();
			YouGou.Ajax.doPost({
				successMsg: '批量删除分类成功！',
				url: '/cms/batchRemoveHotCat.sc',
				data: { "hotCatIds" : hotCatIds },
				success : function(data){
					mmGrid.load();
				}
			});
		}
	});
}
//批量添加销售分类至热门分类
var batchAdd = function(){
	var checkObjs = $('#saleCat-table input:checkbox:checked');
	if(checkObjs.length <= 0){
		YouGou.UI.Dialog.autoCloseTip("请勾选要添加的分类");
		return;
	}
	YouGou.UI.Dialog.confirm({
		message : "确定要批量添加分类吗？"
	},function(result){
		if(result) {
			var checkObjs = $('#saleCat-table input:checkbox:checked');
			var idArr = [];
			checkObjs.each(function(){
				var trObj = $(this).parents('#saleCat-table tr');
				var idSpan = trObj.find('span:last');
				idArr.push(idSpan.text());
			});
			var saleCatIds = idArr.join();
			YouGou.Ajax.doPost({
				successMsg: '批量添加分类成功！',
				url: '/cms/batchInsertHotCat.sc',
				data: { "saleCatIds" : saleCatIds },
				success : function(data){
					hideForm();
				}
			});
		}
	});
}