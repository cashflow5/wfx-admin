/*************
	DiscoverChannel
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	var id =item.id;
	var channelCode =item.channelCode;
	var channelName =item.channelName;
	var status =item.status;
	html.push("<a  href=\"javascript:void(0);\"  onclick=\"updateChannel(\'"+id+"\',\'"+channelCode+"\',\'"+channelName+"\',\'"+status+"\');\">编辑</a>&nbsp;&nbsp;");
	html.push("<a  href=\"javascript:void(0);\"  onclick=\"removeDiscover(\'"+id+"\');\">删除</a>&nbsp;&nbsp;");
	if(status==1){
		var s=2;
		html.push("<a  href=\"javascript:void(0);\"  onclick=\"saveStatus(\'"+id+"\',\'"+s+"\');\">隐藏</a>&nbsp;&nbsp;");

	}else{
		var s=1;
		html.push("<a  href=\"javascript:void(0);\"  onclick=\"saveStatus(\'"+id+"\',\'"+s+"\');\">显示</a>&nbsp;&nbsp;");
	}
    return html.join('');
};
//排序
var orderFixed = function(val,item,rowIndex){
	var html = [];
	var id =item.id;
	var channelCode =item.channelCode;
	var channelName =item.channelName;
	var status =item.status;
	html.push('<a  href="javascript:void(0);"    action="toTop">置顶</a>&nbsp;&nbsp;');
	html.push('<a  href="javascript:void(0);"   action="setUp">↑</a>&nbsp;&nbsp;');
	html.push('<a  href="javascript:void(0);"   action="setDown">↓</a>&nbsp;&nbsp;');
    return html.join('');
};
//排序
var channelNameFixed = function(val,item,rowIndex){
	var html = [];
	var id =item.id;
	var channelCode =item.channelCode;
	var channelName =item.channelName;
	var status =item.status;
	html.push('<a href="/discover/queryChannelDetails.sc?channelId='+id+'\" >'+channelName+'</a>&nbsp;&nbsp;');
    return html.join('');
};

//状态
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

function appFixed(val,item,rowIndex){
	var html = [];
	var  page =$(".pageList").find(".active").find("a").text();
	var status =item.status;
	if(page==1){
		if(rowIndex<6){
			if(status==1){
				var index=rowIndex+1;
				html.push("tab"+index);
			}else{
				html.push("--");
			}
			
			
		}else{
			html.push("--");
		}
	}else{
		html.push("--");
	}
	return html.join('');
}
//状态
var channelCode = function(val,item,rowIndex){
	var html = [];
	var channelCode =item.channelCode;

	html.push("<span>频道"+channelCode+"</span>&nbsp;&nbsp;");
	
    return html.join('');
};
//修改信息
function updateChannel(id,channelCode,channelName,status){
	var channleStatus=' <span>频道状态</span><span>:&nbsp&nbsp</span>';
	if(status==1){
		channleStatus=channleStatus+'显示<input id="status" type="radio" value="1" name="status"  checked/>隐藏<input id="status" type="radio" value="2" name="status"/>';
	}else{
		channleStatus=channleStatus+'显示<input id="status" type="radio" value="1" name="status"/>隐藏<input id="status" type="radio" value="2" name="status"  checked/>';
	}
	YouGou.UI.Dialog.show({
		title : '频道更新',
		message: function(dialog) {
			var msg = '<div>频道序号：频道'+channelCode+' </br>频道名称：<input id="channelName" value=\"'+channelName+'\"   /></br>'+channleStatus+'</div>';
			return msg;
		},
		buttons: [
		    {
				label: '确定',
				cssClass: 'btn-primary',
				action: function(dialog) {
				
					var channelName = $("#channelName").val();
					var status = $("#status").val();
					dialog.close();
					saveDiscover(id,channelCode,channelName,status)
					
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
//新增信息
function addChannel(){
	
	var orderMarkCode = $('#orderMarkCode').val();
	var orderMarkCode=(orderMarkCode-0)+1;
	
			YouGou.UI.Dialog.show({
				title : '新增频道',
				message: function(dialog) {
					var msg = '<div>频道序号：频道'+orderMarkCode+' </br>频道名称：<input id="channelName" value=""   /></br> </div>';
					return msg;
				},
				buttons: [
				    {
						label: '确定',
						cssClass: 'btn-primary',
						action: function(dialog) {
							var channelName = $("#channelName").val();
							dialog.close();
							saveDiscover('',orderMarkCode,channelName,2)
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
	{ title:'频道名字', name:'', align:'left',renderer: channelNameFixed},
	{ title:'频道状态', name:'', align:'center',renderer: discoverStatus},
	{ title:'更新时间', name:'updateDate', align:'center',renderer: YouGou.Util.timeFixed},
	{ title:'创建用户', name:'createUser', align:'left'},
	{ title:'更新用户', name:'updateUser', align:'left'},
	{ title:'排序号', name:'orderMark', align:'center'},
	{ title:'APP', name:'', align:'center',renderer:appFixed},
	{ title:'操作', name:'' ,width:120, align:'center',lockWidth:true, renderer: actionFixed},
	{ title:'排序', name:'' ,width:120, align:'center',lockWidth:true, renderer: orderFixed},
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
	url: '/discover/queryDiscoverData.sc',
	fullWidthRows: true,
	autoLoad: true,
	multiSelect: true,
	checkCol: true,
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
    }else if(action == 'toTop'){
    	YouGou.Ajax.doPost({
    		successMsg: '置顶成功！',
    		url: '/discover/setChannelTop.sc',
    	  	data: { "id" : item.id },
    	  	success : function(data){
      			mmGrid.load();
    		}
    	});
    }else if(action == 'setUp'){
    	YouGou.Ajax.doPost({
    		successMsg: '上移成功！',
    		url: '/discover/toChannelUp.sc',
    	  	data: { "id" : item.id },
    	  	success : function(data){
      			mmGrid.load();
    		}
    	});
    }else if(action == 'setDown'){
    	YouGou.Ajax.doPost({
    		successMsg: '下移成功！',
    		url: '/discover/toChannelDown.sc',
    	  	data: { "id" : item.id },
    	  	success : function(data){
      			mmGrid.load();
    		}
    	});
    }
    e.stopPropagation();  //阻止事件冒泡
});

function removeDiscover(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/discover/removeDiscover.sc',
	  	data: { "id" : id },
	  	success : function(data){
	  		getorderMark();
  			mmGrid.load();
		}
	});
}
function removeDiscover(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/discover/removeDiscover.sc',
	  	data: { "id" : id },
	  	success : function(data){
	  		getorderMark();
  			mmGrid.load();
		}
	});
}
function batchRemove(){
	
	var checkObjs = $('#grid-table input:checkbox:checked');
	var idsStr = "";
	if(checkObjs.length > 0){
		checkObjs.each(function(){
			var trObj = $(this).parents('#grid-table tr');
			var idSpan = trObj.find('span').last();
			
			idsStr = idsStr+ idSpan.text()+",";
		});
	}else{
		YouGou.UI.Dialog.autoCloseTip("请选择要删除的信息");
		return ;
	}
	
	
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/discover/batchRemoveDiscover.sc',
	  	data: { "ids" : idsStr },
	  	success : function(data){
	  		getorderMark();
  			mmGrid.load();
		}
	});
}



//更新
function saveStatus(id,status){	
	
				YouGou.Ajax.doPost({
			   		successMsg: '频道信息',
			   		url: '/discover/saveDiscover.sc',
			   		dataType:"json",
			   		tips:false,
			   		async: false,
			   	  	data: { "id" : id ,"status":status},
			   	  	success : function(data){
		   	  			YouGou.UI.Dialog.alert({message:"保存成功"});
		   	  			mmGrid.load();
			   	  		
			     			
			   		}
			   	});
			
			
}

function getorderMark(){
	YouGou.Ajax.doPost({
		url: '/discover/getorderMark.sc',
	  	data: { "id" : '1' },
	  	success : function(data){
	  		if(data.data.orderMark ){
	  			var order=data.data.orderMark;
	  			$('#orderMarkCode').val(order);
	  			mmGrid.load();
   	  		}else{
   	  			//YouGou.UI.Dialog.alert({message:"商品添加至分销商失败"});
   	  		}
	  		
		}
	});
	
}

//更新
function saveDiscover(id,channelCode,channelName,status){	
	
			if(channelName!='' && channelName!=null && channelName!='undefined'){
				
			
				YouGou.Ajax.doPost({
			   		successMsg: '频道信息',
			   		url: '/discover/saveDiscover.sc',
			   		dataType:"json",
			   		tips:false,
			   	  	data: { "id" : id ,"channelCode":channelCode,"channelName":channelName,"status":status},
			   	  	success : function(data){
		   	  			getorderMark();
		   	  			
		   	  		
			     			
			   		}
			   	});
			}else{
				YouGou.UI.Dialog.alert({message:"频道名不能为空"});
			}
			
}


function doQuery(){
	mmGrid.load();
}