/*************
	Notice
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="edit">编辑</a>&nbsp;&nbsp;');
	html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
    return html.join('');
};
//排序号编辑按钮
var sortNoFixed = function(val,item){
	var html = '';
	html += '<div class="hotSnGroup">';
	html += '<span class="hotSnText">'+val+'</span>';
	html += '<input class="hide hotSnInput" type="text" value="'+val+'"/>';
	html += '<a javascript:void(0) class="editIcon" onclick="editCarFig(this);" title="编辑">&nbsp;</a>';
	html += '<a javascript:void(0) class="hide saveIcon" onclick="saveCarFig(this,\''+item.id+'\',\'sortNo\');" title="保存">&nbsp;</a>';
	html += '<a javascript:void(0) class="hide cancelIcon" onclick="cancelCarFig(this);" title="取消修改"">&nbsp;</a>';
	html += '</div>';
	return html;
}
var editCarFig = function(obj){
	var hotSnGroup = $(obj).parent();
	carFigChange(hotSnGroup,'edit');
	var hotSnInput = hotSnGroup.find('.hotSnInput');
	hotSnInput.select();
}
var saveCarFig = function(obj,id,editType){
	var hotSnGroup = $(obj).parent();
	var hotSnText = hotSnGroup.find('.hotSnText');
	var hotSnInput = hotSnGroup.find('.hotSnInput');
	var oldHotSn = hotSnText.text();
	var hotSn = hotSnInput.val();
		if(hotSn==""){
			YouGou.UI.Dialog.autoCloseTip("序号不能为空");
			return;
		}
		var regExp = /^[0-9]*[1-9][0-9]*$/;
		if(!regExp.test(hotSn)){
			YouGou.UI.Dialog.autoCloseTip("请输入1-100的排序号");
			return;
		}
		if(hotSn > 1000){
			YouGou.UI.Dialog.autoCloseTip("请输入1-999的排序号");
			return;
		}
		//修改排序号
		if(hotSn != oldHotSn){
			YouGou.Ajax.doPost({
				successMsg: '修改成功！',
				url: '/notice/saveProp.sc',
			  	data: { "id" : id,"sort":hotSn },
			  	success : function(data){
		  			mmGrid.load();
		  			carFigChange(hotSnGroup,'save');
				}
			});
		}else{
			carFigChange(hotSnGroup,'save');
		}
}
var carFigChange = function(hotSnGroup,action){
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
	{ title:'标题', name:'title', align:'left'},
	{ title:'跳转类型', name:'redirectTypeDesc', align:'left'},
	{ title:'跳转链接', name:'redirectUrl', align:'left'},
	{ title:'排序', name:'sort', align:'center',renderer:sortNoFixed},
	{ title:'有效时间', name:'validTime', align:'center',width:200},
	{ title:'状态', name:'statusDesc', align:'center'},
	{ title:'最后更新人', name:'lastOperateUser', align:'left'},
	{ title:'最后更新时间', name:'lastOperateTime', align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'操作', name:'' ,width:200, align:'center',lockWidth:true, renderer: actionFixed},
	{ title:'deleteFlag', name:'deleteFlag', hidden: true},
    { title:'ID', name:'id', hidden: true}
];




J('#effectiveTime_').calendar({maxDate:'#failureTime_',format:'yyyy-MM-dd HH:mm:ss'}); 
J('#failureTime_').calendar({minDate:'#effectiveTime_',format:'yyyy-MM-dd HH:mm:ss'});
J('#effectiveTime_1').calendar({maxDate:'#failureTime_1',format:'yyyy-MM-dd HH:mm:ss'}); 
J('#failureTime_1').calendar({minDate:'#effectiveTime_1',format:'yyyy-MM-dd HH:mm:ss'});

function showForm(){
	$("#girdContent").addClass("hide");
	$("#noticeNavbar,#noticeForm").removeClass("hide");
	YouGou.UI.resetForm("noticeForm");
	$(window).resize();
}
function hideForm(){
	$("#girdContent").removeClass("hide");
	$("#noticeNavbar,#noticeForm").addClass("hide");
	$("#build_redirectUrl_div_article,#build_redirectUrl_div_commodity").hide();
	$('#commodityTips,articleTips').text("");
}
var cancelCarFig = function(obj){
	var hotSnGroup = $(obj).parent();
	carFigChange(hotSnGroup,'cancel');
	var hotSnText = hotSnGroup.find('.hotSnText');
	var hotSnInput = hotSnGroup.find('.hotSnInput');
	hotSnInput.val(hotSnText.text());
}
//保存及更新
function saveNotice(){
	var title = $('#title_').val();//标题
	var redirectType = $('#redirectType_').val();
	var redirectUrl = $('#redirectUrl').val();
	var sort = $('#sort_').val();
	var effectiveTime = $('#effectiveTime_').val();
	var failureTime = $('#failureTime_').val();
	var curDate =getNowFormatDate();
	
	if(title == ''){
		YouGou.UI.Dialog.autoCloseTip('请输入标题');
		return;
	}
	if(redirectType==''){
		YouGou.UI.Dialog.autoCloseTip('请选择跳转类型');
		return;
	}
	if(redirectType!=0&&redirectUrl==''){
		YouGou.UI.Dialog.autoCloseTip('请输入跳转链接');
		return;
	}
	if(sort == ''){
		YouGou.UI.Dialog.autoCloseTip('请输入排序号');
		return;
	}else if(isNaN(sort)){
		YouGou.UI.Dialog.autoCloseTip('排序号有误，请输入数字');
		return;
	}
	if(effectiveTime == ''||failureTime==''){
		YouGou.UI.Dialog.autoCloseTip('请输入有效时间');
		return;
	}else if(effectiveTime>failureTime){
		YouGou.UI.Dialog.autoCloseTip('请输入正确有效时间，生效时间不能大于失效时间');
		return;
	}else if(curDate>failureTime){
		YouGou.UI.Dialog.autoCloseTip('请输入正确有效时间，失效时间必须在大于当前时间');
		return;
	}
	
	$("#effectiveTime").val(effectiveTime);
	$("#failureTime").val(failureTime);
	
	YouGou.Ajax.doPost({
		successMsg: (YouGou.Util.isEmpty($("#id").val())?"创建":"修改") +'成功!',
		url: '/notice/saveProp.sc',
	  	data: $("#noticeForm").serializeArray(),
	  	async: false,
	  	success : function(data){
  			mmGrid.load();
  			hideForm();
		}
	});
}
//选择联动
function chooseSelect(){
		var val=$("#redirectType_").val();
		if(val==0)//无,请选择
		{
			$("#redirectUrl_div,#build_redirectUrl_div_article,#build_redirectUrl_div_commodity").hide();
		}
		else if(val==1)//h5页面
		{
			$("#build_redirectUrl_div_article,#build_redirectUrl_div_commodity").hide();$("#redirectUrl_div").show();
		}
		else if(val==2)//商品详情页面
		{
			$("#redirectUrl_div,#build_redirectUrl_div_commodity").show();
			$("#build_redirectUrl_div_article").hide();
			$('#commodityTips').text("");
		}
		else if(val==3)//发现文章
		{
			$("#redirectUrl_div,#build_redirectUrl_div_article").show();
			$("#build_redirectUrl_div_commodity").hide();
			$('#articleTips').text("");
		}
}
//生成跳转链接
function buildRedirectUrl(){
	var redirectType=$("#redirectType_").val();
	var value=$("#redirectTypeParam").val();
	YouGou.Ajax.doPost({
		tips : false,
		successMsg: '修改成功!',
		url : "/notice/buildRedirectUrl.sc",
		data : {"redirectType" : redirectType,"value": value},
	  	success : function(data){
	  		if(data.msg=="success"){
	  			//将此值赋值到跳转链接文本框
	  			var redirectUrl=data.data;
	  			$("#redirectUrl").val(redirectUrl);
	  			$("#errorMsg").html('');
	  		}else{
	  			//错误信息用红字显示出来
	  			var error=data.msg;
	  			$("#errorMsg").html(error);
	  		}
		}
	});
}
//编辑
function showEditForm(id){
	YouGou.Ajax.doPost({
		tips : false,
		successMsg: '修改成功!',
		url : "/notice/getPropById.sc",
		data : {"id" : id},
	  	success : function(data){
  			showForm();
  			YouGou.UI.initForm("noticeForm", data.data);
  			chooseSelect();
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
	url: '/notice/queryPropData.sc',
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
    	showEditForm(item.id);
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
		url: '/notice/removeProp.sc',
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
		url: '/notice/saveProp.sc',
	  	data: { "id" : id ,"status" : status },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}
//批量删除
function batchDelete(){
	var array = mmGrid.selectedRows();
	if(array.length<=0){
		YouGou.UI.Dialog.alert({message:"请选择要删除的公告"});
		return ;
	}
	YouGou.UI.Dialog.confirm({
			message : "确定要批量删除吗？"
		},function(result){
			if(result) {
				var idsStr = "";
				for(var i=0;i<array.length;i++ ) {
					var item = array[i];
					idsStr = idsStr+ item.id+",";
				}
				YouGou.Ajax.doPost({
					successMsg: '删除成功！',
					url: '/notice/batchRemoveNotice.sc',
				  	data: { "ids" : idsStr },
				  	success : function(data){
			  			mmGrid.load();
					}
				});
           }
	});
	
}
//查询
function doQuery(){
	mmGrid.load();
}

function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    return currentdate;
} 

function addArticle(){
	$.ajax({
		type: "post",
        url: '/discover/articleSelected.sc',
        data: {},
        async: false,
        success: function (data) {
        	YouGou.UI.Dialog.show({
        		title: "文章列表",
        		closable: true,
        		closeByBackdrop: false,
        		cssClass: 'message-info-list',
        		message : data.replace(/(\n)+|(\r\n)+/g, ""),
        		buttons: [
        		    {
        		    	label: '关闭',
        		    	cssClass: 'btn-yougou articleClose',
 		                action: function (dialog) {
 		                	dialog.close();
 		                }
        		    }
	            ]
        	});
        }
	});
}
function addCommodity(){
	$.ajax({
		type: "post",
        url: '/commodity/commoditySelected.sc',
        data: {},
        async: false,
        success: function (data) {
        	YouGou.UI.Dialog.show({
        		title: "商品列表",
        		closable: true,
        		closeByBackdrop: false,
        		cssClass: 'message-info-list',
        		message : data.replace(/(\n)+|(\r\n)+/g, ""),
        		buttons: [
        		    {
        		    	label: '关闭',
        		    	cssClass: 'btn-yougou commodityClose',
 		                action: function (dialog) {
 		                	dialog.close();
 		                }
        		    }
	            ]
        	});
        }
	});
}
$(function(){
	chooseSelect()
})