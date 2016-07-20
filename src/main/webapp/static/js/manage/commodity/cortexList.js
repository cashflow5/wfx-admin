/*************
	CommodityCortex
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	//html.push('<a href="javascript:void(0);" action="select">查看</a>&nbsp;&nbsp;');
	html.push('<a href="javascript:void(0);" action="edit">编辑</a>&nbsp;&nbsp;');
	//html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
    return html.join('');
};
var isNotDescriptionFixed = function(val){
	var str = '';
	if('100' == val){
		str = '是';
	}else if('101' == val){
		str = '否';
	}
	return str;
}
// 列集合
var cols = [
	{ title:'皮质名', name:'name', align:'left',renderer:function(val,item,rowIndex){
		var showValue=item.name;
		var lastOperateUser = item.lastOperateUser;
		if(!lastOperateUser){
			return "<font color='red'>"+showValue+"<font>"
		}else{
			return showValue;
		}
	}},
	{ title:'是否有说明', name:'isNotDescription', align:'center',renderer:function(val,item,rowIndex){
		var showValue=item.isNotDescription;
		if('100' == showValue){
			showValue = '是';
		}else if('101' == showValue){
			showValue = '否';
		}
		var lastOperateUser = item.lastOperateUser;
		if(!lastOperateUser){
			return "<font color='red'>"+showValue+"<font>"
		}else{
			return showValue;
		}
	}},
	{ title:'最后更新人', name:'lastOperateUser', align:'left'},
	{ title:'最后更新时间', name:'lastOperateTime', align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'操作', name:'' ,width:200, align:'center',lockWidth:true, renderer: actionFixed},
    { title:'ID', name:'id', hidden: true}
];


function showForm(){
	$("#girdContent").addClass("hide");
	$("#cortexNavbar,#cortexForm").removeClass("hide");
	YouGou.UI.resetForm("cortexForm");
	$(window).resize();
}
function hideForm(){
	$("#girdContent").removeClass("hide");
	$("#cortexNavbar,#cortexForm").addClass("hide");
}

//编辑
function showEditForm(id,name){
	YouGou.Ajax.doPost({
		tips : false,
		successMsg: '皮质名【'+ name +'】修改成功!',
		url : "/commodity/getCortexById.sc",
		data : {"id" : id},
	  	success : function(data){
  			showForm();
  			$("#showName").html(data.data.name);
  		    YouGou.UI.initForm("cortexForm", data.data);
  		    CORTEXT_NEW.Editor().html($('#description').val());
		}
	});
}
//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");

// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/commodity/queryCortexData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    //编辑
    if(action == "edit"){
    	showEditForm(item.id,item.name);
    }else if(action == "delete"){// 删除
   		YouGou.UI.Dialog.confirm({
   			message : "确定要删除吗？"
   		},function(result){
   			if(result) {
                removeProp(item.id);
            }
   		});
    }else if(action == "select"){//查看
    	YouGou.UI.Dialog.alert({message:"查看"});
    }
    e.stopPropagation();  //阻止事件冒泡
});
//删除
function removeProp(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/commodity/removeCortex.sc',
	  	data: { "id" : id },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}
//更新状态时使用
function updateProp(id,status){
	YouGou.Ajax.doPost({
		successMsg: '更新成功！',
		url: '/commodity/saveCortex.sc',
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

function saveCortex(){
	var description = $('#description').val();
	if(!description){
		YouGou.UI.Dialog.autoCloseTip("请编辑皮质说明");
		return false;
	}
	YouGou.Ajax.doPost({
		successMsg: '修改成功!',
		url: '/commodity/saveCortex.sc',
	  	data: $("#cortexForm").serializeArray(),
	  	async: false,
	  	success : function(data){
  			mmGrid.load();
  			hideForm();
		}
	});
}
