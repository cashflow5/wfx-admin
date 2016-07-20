/*************
	DiscoverArticle
**************/
J('#updateTimeStart').calendar({maxDate:'#updateTimeEnd',format:'yyyy-MM-dd HH:mm:ss'});
J('#updateTimeEnd').calendar({minDate:'#updateTimeStart',format:'yyyy-MM-dd HH:mm:ss'});
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
	html.push('<a href="javascript:void(0);" action="restore">还原</a>&nbsp;&nbsp;');
    return html.join('');
};

var titleFixed = function(val,item){
	var html = '';
	html += '<a href="/discover/articleList.sc?id='+item.id+'&type=view">'+val+'</a>';
	return html;
};

var authorFixed = function(val,item){
	var str = '优购微零售';
	if('2' == item.authorType){
		str = val;
	}
	return str;
}

// 列集合
var cols = [
	{ title:'id', name:'no', align:'center'},
	{ title:'标题', name:'title', align:'left',renderer:titleFixed},
	{ title:'作者', name:'authorAccount', width:90,align:'left',renderer:authorFixed},
	{ title:'更新时间', name:'updateTime',width:130, align:'center',lockWidth:true, renderer: YouGou.Util.timeFixed},
	{ title:'所属频道', name:'channelName', align:'left'},
	{ title:'操作', name:'' ,width:200, align:'center',lockWidth:true, renderer: actionFixed},
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
	url: '/discover/queryArticleRecycleData.sc',
	fullWidthRows: true,
	checkCol: true,
	multiSelect: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    if(action == "delete"){// 删除
   		YouGou.UI.Dialog.confirm({
   			message : "确定要删除吗？"
   		},function(result){
   			if(result) {
   				removeArticle(item.id);
            }
   		});
    }else if(action == "restore"){//还原
    	restoreArticle(item.id);
    }
    e.stopPropagation();  //阻止事件冒泡
});
//删除
function removeArticle(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/discover/removeArticle.sc',
	  	data: { "id" : id },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}
//更新状态时使用
function updateArticle(id,status){
	YouGou.Ajax.doPost({
		successMsg: '更新成功！',
		url: '/discover/saveArticle.sc',
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

function restoreArticle(id){
	YouGou.UI.Dialog.confirm({
		message : "确定要还原吗？"
	},function(result){
		if(result) {
			YouGou.Ajax.doPost({
				successMsg: '还原成功！',
				url: '/discover/updateArticle.sc',
			  	data: { "id" : id , "deleteFlag" : 1 , "type" : "recycle_restore"},
			  	success : function(data){
		  			mmGrid.load();
				}
			});
		}
	});
}

//批量删除
function batchDelete(status){
	var checkObjs = $('#grid-table input:checkbox:checked');
	if(checkObjs.length <= 0){
		YouGou.UI.Dialog.autoCloseTip("请勾选要删除的文章");
		return;
	}
	YouGou.UI.Dialog.confirm({
		message : "确定要批量删除文章吗？"
	},function(result){
		if(result) {
			var idArr = [];
			checkObjs.each(function(){
				var trObj = $(this).parents('#grid-table tr');
				var idSpan = trObj.find('span:last');
				idArr.push('\''+idSpan.text()+'\'');
			});
			var ids = '(' + idArr.join() + ')';
			YouGou.Ajax.doPost({
				successMsg: '批量删除文章成功！',
				url: '/discover/batchRemoveArticle.sc',
			  	data: { "ids" : ids },
			  	success : function(data){
		  			mmGrid.load();
				}
			});
		}
	});
}
//批量还原
function batchRestore(){
	var checkObjs = $('#grid-table input:checkbox:checked');
	if(checkObjs.length <= 0){
		YouGou.UI.Dialog.autoCloseTip("请勾选要还原的文章");
		return;
	}
	YouGou.UI.Dialog.confirm({
		message : "确定要批量还原文章吗？"
	},function(result){
		if(result) {
			var idArr = [];
			checkObjs.each(function(){
				var trObj = $(this).parents('#grid-table tr');
				var idSpan = trObj.find('span:last');
				idArr.push('\''+idSpan.text()+'\'');
			});
			var ids = '(' + idArr.join() + ')';
			YouGou.Ajax.doPost({
				successMsg: '批量还原文章成功！',
				url: '/discover/batchUpdateArticle.sc',
			  	data: {"id":ids,"deleteFlag":"1","type":"recycle_restore"},
			  	success : function(data){
		  			mmGrid.load();
				}
			});
		}
	});
}

$(function(){
	$('#authorType2').change(function(){
		var authorType = $(this).val();
		if(authorType == '1'){
			$('#authorAccount2').prop('readonly',true);
			$('#authorAccount2').val('');
		}else{
			$('#authorAccount2').prop('readonly',false);
		}
	});
});