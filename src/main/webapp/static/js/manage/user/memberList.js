/*************
	MemberAccount
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	var memberMgt = $("#memberMgt").val();
	if(memberMgt == "1"){
		html.push('<a href="javascript:void(0);" action="updateToSeller">升级为分销商</a>');
	}else{
		html.push('<a href="javascript:void(0);" action="select">查看</a>');
	}
   return html.join('');
};
var addViewLink = function(val,item){
	var html = [];
	if(!val){
		val = "";
	}
	html.push('<a href="javascript:void(0);" action="select">'+val+'</a>');
	return html.join('');
};

// 列集合
var cols = [
	{ title:'会员账号', name:'loginName', align:'left',renderer: addViewLink},
	{ title:'微信用户名', name:'platformUsername', align:'left'},
	{ title:'上级分销商账号', name:'parentLoginName', align:'left'},
	{ title:'上级微信用户名', name:'parentPlatformUsername', align:'left'},
	{ title:'姓名', name:'memberName', align:'left'},
	{ title:'订单总数', name:'orderCount', align:'center'},
	{ title:'订单总金额(元)', name:'orderAmount', align:'center'},
	{ title:'注册时间', name:'registerDate' ,lockWidth:true,width:125, align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'最后登录时间', name:'lastLoginTime',lockWidth:true,width:125, align:'center', renderer: YouGou.Util.timeFixed},
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
	url: '/user/queryMemberData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    //编辑
    if(action == "select"){//查看
    	viewInfo(item);
    }else if(action == "updateToSeller"){
    	YouGou.UI.Dialog.confirm({
   			message : "确定要升级用户为分销商吗？"
   		},function(result){
   			if(result) {
   				updateToSeller(item.id,item.loginName,item.memberName);
            }
   		});
    }
    e.stopPropagation();  //阻止事件冒泡
});

//升级成为分销商
function updateToSeller(id, loginName,memberName){
	YouGou.Ajax.doPost({
		successMsg: '升级成为分销商成功！',
		errorMsg:'升级成为分销商失败!',
		url: '/user/applyToSeller.sc',
	  	data: { "id" : id,"loginName":loginName,"memberName":memberName },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}

//查看会员信息
function viewInfo(item){
	showForm(item.area);
	sexFixed(item);
	YouGou.UI.initForm("viewForm", item);
}

function doQuery(){
	mmGrid.load();
}
var sexFixed = function(item){
	var val = item.memberSex;
	var sexStr = '';
	if(val == '1'){
		sexStr = '男';
	}else if(val == '2'){
		sexStr = '女';
	}
	item.memberSex = sexStr;
};

//隐藏表格显示表单
function showForm(area){
	$("#girdContent").addClass("hide");
	$("#viewNavbar,#viewForm").removeClass("hide");
	YouGou.UI.resetForm("viewForm");
}

// 隐藏表单显示表格
function hideForm(){
	$("#viewNavbar,#viewForm").addClass("hide");
	$("#girdContent").removeClass("hide");
}

J('#registerDateStart').calendar({maxDate:'#registerDateEnd',format:'yyyy-MM-dd HH:mm:ss'});
J('#registerDateEnd').calendar({minDate:'#registerDateStart',format:'yyyy-MM-dd HH:mm:ss'});
J('#lastLoginTimeStart').calendar({maxDate:'#lastLoginTimeEnd',format:'yyyy-MM-dd HH:mm:ss'});
J('#lastLoginTimeEnd').calendar({minDate:'#lastLoginTimeStart',format:'yyyy-MM-dd HH:mm:ss'});
