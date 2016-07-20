/*************
	DiscoverChannel
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	var id =item.id;

	html.push("<a  href=\"javascript:void(0);\"  onclick=\"update(\'"+id+"\');\">编辑</a>&nbsp;&nbsp;");
	html.push("<a  href=\"javascript:void(0);\"  onclick=\"removeDiscover(\'"+id+"\');\">删除</a>&nbsp;&nbsp;");
    return html.join('');
};

function back(){
	var url = '/discover/discoverList.sc';
	
	window.location.href = url;
}
//上下架
var discoverStatus = function(val,item,rowIndex){
	var html = [];
	var status =item.status;
	if(status==1){
		html.push("<span>显示</span>&nbsp;&nbsp;");
	}else if(status==2){
		html.push("<span>隐藏</span>&nbsp;&nbsp;");
	}
    return html.join('');
};


//新增信息
function addChannel(channelCode,channelName){
	
	var orderMarkCode = $('#orderMarkCode');
	orderMarkCode=orderMarkCode+1;
	YouGou.UI.Dialog.show({
		title : '调价',
		message: function(dialog) {
			var msg = '<div>频道序号：频道'+orderMarkCode+' </br>频道名称：<input id="channelName" value=\"'+channelName+'\"   /></br> /></div>';
			return msg;
		},
		buttons: [
		    {
				label: '确定',
				cssClass: 'btn-primary',
				action: function(dialog) {
					//调用入库方法
				}
		    }, 
			{
		    	label: '取消',
		    	action: function(dialog) {
		    		dialog.close();
		    	}
			}
	    ]
	});	
}
// 列集合
var cols = [
	{ title:'序号', name:'sortNo', align:'left'},
	{ title:'文章ID', name:'no', align:'left'},
	{ title:'文章题目', name:'title', align:'left'},
	{ title:'作者', name:'operUser', align:'center'},
	{ title:'发布时间', name:'createTime', align:'center'}
	
];

//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");

// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/discover/queryChannelDetailsData.sc',
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
                removeDiscover(item.id);
            }
   		});
    }else if(action == "select"){//查看
    	YouGou.UI.Dialog.alert({message:"查看"});
    }
    e.stopPropagation();  //阻止事件冒泡
});

function removeDiscover(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/discover/removeDiscover.sc',
	  	data: { "id" : id },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}

//ajax 调价
function doWfxPriceAjax(id,channelCode,channelName,status){	
	
			
				YouGou.Ajax.doPost({
			   		successMsg: '频道信息',
			   		url: '/discover/saveDiscover.sc',
			   		dataType:"json",
			   		tips:false,
			   	  	data: { "id" : id ,"channelCode":channelCode,"channelName":channelName,"status":status},
			   	  	success : function(data){
				    	if(data.data.resultCount >0 ){
				    		dialog.close();
			   	  			YouGou.UI.Dialog.alert({message:"保存成功"});
			   	  			mmGrid.load();
			   	  		}else{
			   	  			YouGou.UI.Dialog.alert({message:"保存失败"});
			   	  		}
			     			
			   		}
			   	});
}


function doQuery(){
	mmGrid.load();
}