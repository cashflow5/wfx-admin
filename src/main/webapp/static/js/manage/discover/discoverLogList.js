/*************
	DiscoverLog
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="select">查看</a>&nbsp;&nbsp;');
	html.push('<a href="javascript:void(0);" action="edit">编辑</a>&nbsp;&nbsp;');
	html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
    return html.join('');
};

//状态
var businessType = function(val,item,rowIndex){
	var html = [];
	var businessType =item.businessType;
	if(businessType==1){
		html.push("<span>频道管理</span>&nbsp;&nbsp;");
	}else if(businessType==2){
		html.push("<span>文章管理</span>&nbsp;&nbsp;");
	}else if(businessType==3){
		html.push("<span>轮播图管理</span>&nbsp;&nbsp;");
	}else if(businessType==4){
		html.push("<span>回收站管理</span>&nbsp;&nbsp;");
	}
    return html.join('');
};
//状态
var operateType = function(val,item,rowIndex){
	var html = [];
	var operateType =item.operateType;
	if(operateType==1){
		html.push("<span>新增</span>&nbsp;&nbsp;");
	}else if(operateType==2){
		html.push("<span>删除</span>&nbsp;&nbsp;");
	}else if(operateType==3){
		html.push("<span>修改</span>&nbsp;&nbsp;");
	}
    return html.join('');
};
// 列集合
var cols = [

	{ title:'业务类型', name:'', align:'left',renderer: businessType},
	{ title:'日志类型', name:'', align:'left',renderer: operateType},
	{ title:'操作人', name:'operateUser', align:'left'},
	{ title:'操作时间', name:'operateDate', align:'center',renderer: YouGou.Util.timeFixed},
	{ title:'操作内容', name:'operateContent', align:'left'},
];
J('#createTimeStart').calendar({maxDate:'#createTimeEnd',format:'yyyy-MM-dd HH:mm:ss'}); 
J('#createTimeEnd').calendar({minDate:'#createTimeStart',format:'yyyy-MM-dd HH:mm:ss'});
//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");

// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/discoverLog/queryDiscoverLogData.sc',
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

function doQuery(){
	mmGrid.load();
}