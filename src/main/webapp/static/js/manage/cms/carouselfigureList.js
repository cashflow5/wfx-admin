/*************
	CmsCarouselFigure
**************/

//图片上传内相关
var ratio = window.devicePixelRatio || 1;
var thumbnailWidth = 100 * ratio;
var thumbnailHeight = 100 * ratio;

//上传小图
var uploader1 = WebUploader.create({
	//选择完文件之后自动上传
	auto : true,
	swf : '/static/js/webuploader/Uploader.swf',
	server : '/cms/uploadCarouselImg.sc',
	pick : '#filePicker1',
	duplicate : true,
	formData : '',
	accept : {
		title : 'Images',
		extensions : 'gif,jpg,jpeg,bmp,png',
		mimeTypes: 'image/*'
	}
});

uploader1.on('uploadSuccess', function(file, response) {//上传图片
	var resultCode = response.data.resultCode;
	
	if( resultCode == undefined){
		YouGou.UI.Dialog.autoCloseTip("图片上传失败了，原因未知");
		return;
	}
	if(resultCode != 0 ){	
		if( response.data.resultMsg != undefined){
			var resultMsg = response.data.resultMsg;
			resultMsg = decodeURI(resultMsg);
			YouGou.UI.Dialog.autoCloseTip(resultMsg);
		}else{
			YouGou.UI.Dialog.autoCloseTip("图片上传失败了，原因未知");
		}
		return;
	}
	uploader1.makeThumb(file, function(error, src) {//放图片缩略图
		if (error) {
			return;
		}
		var imgHtml = '<img id="carPic" src="'+ response.data.imgPath+'" style="position:absolute;left: 0px;top:0px;width:439px;height:185px;" />';
		$(".img1").html(imgHtml);
		$("#picUrl").val(response.data.imgPath);
	}, thumbnailWidth, thumbnailHeight );
});

// 文件上传失败，现实上传出错。
uploader1.on( 'uploadError', function( file ) {
	YouGou.UI.Dialog.autoCloseTip("图片上传失败了");
});

uploader1.on('beforeFileQueued', function( file ) {
	var size = 1*1024*1024;
	if(file.size > size){
		YouGou.UI.Dialog.autoCloseTip("图片大小不能超过1M，请重新选择");
		return false;
	}else{
		uploader1.reset();
		return true;
	}
});
uploader1.on('fileQueued', function( file ) {
    //$('#fileName').text(file.name);
});


// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="edit">编辑</a>&nbsp;&nbsp;');
	html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
    return html.join('');
};

var defaultPicImg = function(picUrl,item){
	var html = [];
	var imgBaseUrl = $("#imgBaseUrl").val();
	if(picUrl!=''){
		var img='<img width="140" height="45" alt="" src="'+picUrl+'"/>';
		var urlinput='<input id="'+item.id+'" type="hidden" value="'+picUrl+'"/>';
		html.push(img);
		html.push(urlinput);
	}else{
		html.push('<img width="140" height="45" alt="" />');
	}
	
	return html.join('');
}


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

//轮播图链接地址编辑按钮
var linkUrlFixed = function(val,item){
	var html = '';
	html += '<div class="hotSnGroup">';
	html += '<span class="hotSnText" style="width:120px;">'+val+'</span>';
	html += '<input class="hide hotSnInput" style="width:120px" type="text" value="'+val+'"/>';
	html += '<a javascript:void(0) class="editIcon" onclick="editCarFig(this);" title="编辑">&nbsp;</a>';
	html += '<a javascript:void(0) class="hide saveIcon" onclick="saveCarFig(this,\''+item.id+'\',\'linkUrl\');" title="保存">&nbsp;</a>';
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

//保存排序号或链接地址修改
var saveCarFig = function(obj,id,editType){
	var hotSnGroup = $(obj).parent();
	var hotSnText = hotSnGroup.find('.hotSnText');
	var hotSnInput = hotSnGroup.find('.hotSnInput');
	var oldHotSn = hotSnText.text();
	var hotSn = hotSnInput.val();
	
	if(editType=="sortNo"){
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
				url: '/cms/addCarouselfigure.sc',
			  	data: { "id" : id,"sortNo":hotSn },
			  	success : function(data){
		  			mmGrid.load();
		  			carFigChange(hotSnGroup,'save');
				}
			});
		}else{
			carFigChange(hotSnGroup,'save');
		}
	}else{
		if(hotSn==""){
			YouGou.UI.Dialog.autoCloseTip("图片链接地址不能为空");
			return;
		}
		//修改轮播图链接地址
		if(hotSn != oldHotSn){
		
			YouGou.Ajax.doPost({
				successMsg: '修改成功！',
				url: '/cms/addCarouselfigure.sc',
			  	data: { "id" : id,"linkUrl":hotSn },
			  	success : function(data){
		  			mmGrid.load();
		  			carFigChange(hotSnGroup,'save');
				}
			});
		}else{
			carFigChange(hotSnGroup,'save');
		}
	}
	
}

var cancelCarFig = function(obj){
	var hotSnGroup = $(obj).parent();
	carFigChange(hotSnGroup,'cancel');
	var hotSnText = hotSnGroup.find('.hotSnText');
	var hotSnInput = hotSnGroup.find('.hotSnInput');
	hotSnInput.val(hotSnText.text());
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
	{ title:'轮播图名称', name:'name', align:'left'},
	{ title:'轮播图类型', name:'typeDesc', align:'left'},
	{ title:'跳转类型', name:'redirectTypeDesc', align:'left'},
	{ title:'图片链接地址', width:200,name:'linkUrl', align:'left',renderer:linkUrlFixed},	
	{ title:'轮播图片',width:120, name:'picUrl', align:'left' , renderer:defaultPicImg},
	{ title:'序号', name:'sortNo', align:'center' ,sortable:true,sortName:'sortNo',renderer:sortNoFixed},
	{ title:'最后更新人', name:'updateUser', align:'left'},
	{ title:'最后更新时间', name:'updateTime', align:'center', renderer: YouGou.Util.timeFixed},	
	{ title:'操作', name:'' ,width:200, align:'center',lockWidth:true, renderer: actionFixed},
	{ title:'deleteFlag', name:'deleteFlag', hidden: true},
	{ title:'picUrlCopy', name:'picUrl', hidden: true},
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
	url: '/cms/queryCarouselfigureData.sc',
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
    	//YouGou.UI.Dialog.alert({message:"编辑"});
    	//showForm();
    	editCarousefigure(item);
    }else if(action == "delete"){// 删除
   		YouGou.UI.Dialog.confirm({
   			message : "确定要删除吗？"
   		},function(result){
   			if(result) {
                removeCarouselfigure(item.id);
            }
   		});
    }
    e.stopPropagation();  //阻止事件冒泡
});

function editCarousefigure(item){
	//显示隐藏域
	$("#girdContent").addClass("hide");
	$("#packageNavbar,#carouselForm").removeClass("hide");
	$(window).resize();//webuploader 加载
	YouGou.UI.resetForm("carouselForm");
	showEditHtml(item);
	showCounts(item.type);
	chooseSelect();
}

//删除轮播图
function removeCarouselfigure(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/cms/removeCarouselfigure.sc',
	  	data: { "id" : id },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}

//编辑轮播图，信息展示
function showEditHtml(item){

	var imgBaseUrl = $("#imgBaseUrl").val();
	$("#id").val(item.id);
	$("#name").val(item.name);
	$("#type").val(item.type);
	$("#redirectType_").val(item.redirectType);
	var imgHtml = '<img id="carPic" src="'+item.picUrl+'" style="position:absolute;left: 0px;top:0px;width:439px;height:185px;" />';
	$(".img1").html(imgHtml);
	$("#sortNo").val(item.sortNo);
	$("#picUrl").val(item.picUrl);
	$("#redirectUrl").val(item.linkUrl);
	
}

//批量删除
function batchDelete(){
	var array = mmGrid.selectedRows();
	if(array.length<=0){
		YouGou.UI.Dialog.alert({message:"请选择要删除的轮播图"});
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
					url: '/cms/batchRemoveCarouselfigure.sc',
				  	data: { "ids" : idsStr },
				  	success : function(data){
			  			mmGrid.load();
					}
				});
           }
	});
	
}

//新增或编辑轮播图提交保存
function addCarouselfigure(){	
	var name = $("#name").val();
	var picUrl = $("#picUrl").val();
	var linkUrl = $("#redirectUrl").val();
	var sortNo = $("#sortNo").val();
	var type = $("#type").val();
	var redirectType = $('#redirectType_').val();
	if(name == ""){
		YouGou.UI.Dialog.autoCloseTip("请输入轮播图名称");
		return ;
	}
	if(!type){
		YouGou.UI.Dialog.autoCloseTip("请选择轮播图类型");
		return ;
	}
	if(redirectType==''){
		YouGou.UI.Dialog.autoCloseTip('请选择跳转类型');
		return;
	}
	if(redirectType!=0&&linkUrl==''){
		YouGou.UI.Dialog.autoCloseTip('请输入跳转链接');
		return;
	}
	if(picUrl == ""){
		YouGou.UI.Dialog.autoCloseTip("请上传轮播图");
		return ;
	}
	if(sortNo == ""){
		YouGou.UI.Dialog.autoCloseTip("请输入轮播图排序号");
		return ;
	}
	var regExp = /^[0-9]*[1-9][0-9]*$/;
	if(!regExp.test(sortNo)){
		YouGou.UI.Dialog.autoCloseTip("请输入1-100的排序号");
		return;
	}
	if(sortNo > 100){
		YouGou.UI.Dialog.autoCloseTip("请输入轮播图排序号不能大于100");
		return ;
	}
	//$("#picUrl").val($("#"+item.id).val());
	YouGou.Ajax.doPost({
		successMsg: '操作成功',
		url: '/cms/addCarouselfigure.sc',
	  	data: $("#carouselForm").serializeArray(),
	  	success : function(data){
	  		hideForm();
  			mmGrid.load();
		}
	});	
}

//点击新增按钮显示新增页面
function showForm(){
	//显示隐藏域
	$("#girdContent").addClass("hide");
	$("#packageNavbar,#carouselForm").removeClass("hide");
	$(window).resize();
	YouGou.UI.resetForm("carouselForm");
}

function showCounts(type){
	if(!type){return;}
	YouGou.Ajax.doPost({
	tips:false,
	url: '/cms/getLimitInfo.sc?type='+type,
  	success : function(data){
  		var maxSortNo = data.data.maxSortNo;
  		var counts = data.data.counts;
  		/*if(counts >= 5){
  			YouGou.UI.Dialog.autoCloseTip("轮播图最多5张");
  			return ;
  		}*/
  		if(maxSortNo !=null && maxSortNo !=undefined && maxSortNo >0 ){
  			$("#maxSortNo").text(maxSortNo);
  		}else{
  			$("#maxSortNo").text("0");
  		}}
	});
}

//隐藏表单显示表格
function hideForm(){
	$("#packageNavbar,#carouselForm").addClass("hide");
	$("#girdContent").removeClass("hide");
	$(".img1").html("");
	$("#type").val("");
	$("#Bag_small_pic").val("");
	$(".img2").html("");
	$("#Bag_big_pic").val("");
	$("#maxSortNo").text("");
	$("#build_redirectUrl_div_article,#build_redirectUrl_div_commodity").hide();
	$('#commodityTips,articleTips').text("");
}
//选择联动
function chooseSelect(){
		var val=$("#redirectType_").val();
		if(val==0)//无,请选择
		{
			$("#redirectUrl_div,#build_redirectUrl_div").hide();
		}
		else if(val==1)//h5页面
		{
			$("#build_redirectUrl_div").hide();$("#redirectUrl_div").show();
		}
		else if(val==2)//详情页面
		{
			$("#redirectUrl_div,#build_redirectUrl_div").show();
			$("#redirectTypeParam").attr("placeholder","请输入商品款色编码");
		}
		else if(val==3)//发现文章
		{
			$("#redirectUrl_div,#build_redirectUrl_div").show();
			$("#redirectTypeParam").attr("placeholder","请输入文章ID");
		}
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
//获取最大排序号
function getMaxSortNo(){
	
}


function doQuery(){
	mmGrid.load();
}
$(function(){
	chooseSelect()
})
