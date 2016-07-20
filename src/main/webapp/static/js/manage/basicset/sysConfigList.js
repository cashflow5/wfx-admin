/*************
	SysConfig
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="edit">编辑</a>&nbsp;&nbsp;');
	html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
    return html.join('');
};

// 列集合
var cols = [
	{ title:'配置名称', name:'configName', align:'left'},
	{ title:'配置键', name:'configKey', align:'left'},
	{ title:'配置键值', name:'configValue', align:'left'},
	{ title:'备注', name:'remark', align:'left'},
	{ title:'创建时间', name:'createTime',width:200,align:'center',lockWidth:true, renderer: YouGou.Util.timeFixed},
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
	url: '/basicSet/querySysConfigData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    //编辑
    if(action == "edit"){
    	showEditForm(item.id,item.configName);
    }else if(action == "delete"){// 删除
   		YouGou.UI.Dialog.confirm({
   			message : "确定要删除吗？"
   		},function(result){
   			if(result) {
                removeSysConfig(item.id);
            }
   		});
    }
    e.stopPropagation();  //阻止事件冒泡
});
//删除
function removeSysConfig(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/basicSet/removeSysConfig.sc',
	  	data: { "id" : id },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}
//更新状态时使用
function updateSysConfig(id,status){
	YouGou.Ajax.doPost({
		successMsg: '更新成功！',
		url: '/basicSet/saveSysConfig.sc',
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

function showForm(){
	$("#girdContent").addClass("hide");
	$("#sysConfigNavbar,#sysConfigForm").removeClass("hide");
	YouGou.UI.resetForm("sysConfigForm");
}

//编辑
function showEditForm(id,sysConfigName){
	YouGou.Ajax.doPost({
		tips : false,
		successMsg: '配置名称【'+ sysConfigName +'】修改成功!',
		url : "/basicSet/getSysConfigById.sc",
		data : {"id" : id},
	  	success : function(data){
  			showForm();
  			YouGou.UI.initForm("sysConfigForm", data.data);
		}
	});
}

function hideForm(){
	$("#girdContent").removeClass("hide");
	$("#sysConfigNavbar,#sysConfigForm").addClass("hide");
}

function saveSysconfig(){
	var configName = $('#configName2').val();
	var configKey = $('#configKey2').val();
	var configValue = $('#configValue2').val();
	if(configName == ''){
		YouGou.UI.Dialog.autoCloseTip('请输入配置名称');
		return;
	}
	if(configKey == ''){
		YouGou.UI.Dialog.autoCloseTip('请输入配置键');
		return;
	}
	if(configValue == ''){
		YouGou.UI.Dialog.autoCloseTip('请输入配置键值');
		return;
	}
	
	YouGou.Ajax.doPost({
		successMsg: '配置'+ (YouGou.Util.isEmpty($("#id").val())?"创建":"修改") +'成功!',
		url: '/basicSet/saveSysConfig.sc',
	  	data: $("#sysConfigForm").serializeArray(),
	  	async: false,
	  	success : function(data){
  			mmGrid.load();
  			hideForm();
		}
	});
}