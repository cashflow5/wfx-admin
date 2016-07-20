/*************
	AppVersion
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="edit">编辑</a>&nbsp;&nbsp;');
	html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
    return html.join('');
};
var actionforceUpdate=function(val,item,rowIndex){
	var html = [];
	var text="";
	if(val=="1"){text="是";}
	if(val=="2"){text="否";}
	html.push(text);
    return html.join('');
};

// 列集合
var cols = [
	{ title:'版本名称',width:30, name:'versionName', align:'left'},
	{ title:'版本编号',width:5, name:'versionCode', align:'left'},
	{ title:'版本APK存放地址', name:'versionUrl', align:'left'},
	{ title:'版本描述', name:'versionContent', align:'left'},
	{ title:'是否强制更新',width:5, name:'forceUpdate', align:'center',renderer:actionforceUpdate},
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
	url: '/basicSet/queryAppVersionData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    //编辑
    if(action == "edit"){
    	showEditForm(item.id,item.versionName);
    }else if(action == "delete"){// 删除
   		YouGou.UI.Dialog.confirm({
   			message : "确定要删除吗？"
   		},function(result){
   			if(result) {
                removeAppVersion(item.id);
            }
   		});
    }else if(action == "select"){//查看
    	YouGou.UI.Dialog.alert({message:"查看"});
    }
    e.stopPropagation();  //阻止事件冒泡
});
//删除
function removeAppVersion(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/basicSet/removeAppVersion.sc',
	  	data: { "id" : id },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}
//更新状态时使用
function updateAppVersion(id,status){
	YouGou.Ajax.doPost({
		successMsg: '更新成功！',
		url: '/basicSet/saveAppVersion.sc',
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


//编辑
function showEditForm(id,versionName){
	YouGou.Ajax.doPost({
		tips : false,
		successMsg: '版本名称【'+ versionName +'】修改成功!',
		url : "/basicSet/getAppVersionById.sc",
		data : {"id" : id},
	  	success : function(data){
  			showForm();
  			YouGou.UI.initForm("appVersionForm", data.data);
		}
	});
}

function showForm(){
	$("#girdContent").addClass("hide");
	$("#appVersionNavbar,#appVersionForm").removeClass("hide");
	YouGou.UI.resetForm("appVersionForm");
	$(window).resize();
}
function hideForm(){
	$("#girdContent").removeClass("hide");
	$("#appVersionNavbar,#appVersionForm").addClass("hide");
}
function saveAppVersion(){

	var versionName = $('#versionName2').val();
	var versionCode = $('#versionCode2').val();
	var versionUrl = $('#versionUrl2').val();
	if(versionName == ''){
		YouGou.UI.Dialog.autoCloseTip('请输入版本名称');
		return;
	}
	if(versionCode == ''){
		YouGou.UI.Dialog.autoCloseTip('请输入版本编号');
		return;
	}
	if(versionUrl == ''){
		YouGou.UI.Dialog.autoCloseTip('请上传版本APK');
		return;
	}
	var val=$('input:radio[name="forceUpdate"]:checked').val();
    if(val==null){
    	YouGou.UI.Dialog.autoCloseTip('请选择是否强制更新');
        return ;
    }
	
	YouGou.Ajax.doPost({
		successMsg: 'App版本'+ (YouGou.Util.isEmpty($("#id").val())?"创建":"修改") +'成功!',
		url: '/basicSet/saveAppVersion.sc',
	  	data: $("#appVersionForm").serializeArray(),
	  	async: false,
	  	success : function(data){
  			mmGrid.load();
  			hideForm();
		}
	});
}






//上传小图
var uploader_apk = WebUploader.create({
	//选择完文件之后自动上传
	auto : true,
	swf : '/static/js/webuploader/Uploader.swf',
	server : '/basicSet/uploadAppApk.sc',
	pick : '#selectAppApk',
	duplicate : true
});

uploader_apk.on('uploadSuccess', function(file, response) {//上传图片
	$("#versionUrl2").val(response.msg);
});

