/*************
	DiscoverArticle
**************/
J('#updateTimeStart').calendar({maxDate:'#updateTimeEnd',format:'yyyy-MM-dd HH:mm:ss'});
J('#updateTimeEnd').calendar({minDate:'#updateTimeStart',format:'yyyy-MM-dd HH:mm:ss'});
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	var publishStatus = item.publishStatus;
	var recommendFlag = item.recommendFlag;
	var id = item.id;
	if(publishStatus == '2'){
		html.push('<a href="javascript:void(0);" action="unPublish">取消发布</a>&nbsp;&nbsp;');
		if(recommendFlag == '2'){
			html.push('<a href="javascript:void(0);" action="unRecommend">撤销推荐</a>&nbsp;&nbsp;');
		}else{
			html.push('<a href="javascript:void(0);" action="recommend">推荐文章</a>&nbsp;&nbsp;');
		}
	}else{
		html.push('<a href="javascript:void(0);" action="publish">发布文章</a>&nbsp;&nbsp;');
		html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
		html.push('<a href="javascript:void(0);" action="edit">编辑</a>&nbsp;&nbsp;');
	}
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

var publishFixed = function(val){
	var str = '';
	if('1' == val){
		str = '未发布';
	}else if('2' == val){
		str = '已发布';
	}
	return str;
}

var recommendFixed = function(val){
	var str = '';
	if('1' == val){
		str = '否';
	}else if('2' == val){
		str = '是';
	}
	return str;
}

var snFixed = function(val,item){
	var html = '';
	html += '<div class="hotSnGroup">';
	html += '<span class="hotSnText">'+val+'</span>';
	html += '<input class="hide hotSnInput" type="text" value="'+val+'"/>';
	html += '<a javascript:void(0) class="editIcon" onclick="editHotSn(this);" title="编辑">&nbsp;</a>';
	html += '<a javascript:void(0) class="hide saveIcon" onclick="saveHotSn(this,\''+item.id+'\');" title="保存">&nbsp;</a>';
	html += '<a javascript:void(0) class="hide cancelIcon" onclick="cancelHotSn(this);" title="取消修改"">&nbsp;</a>';
	html += '</div>';
	return html;
}

// 列集合
var cols = [
	{ title:'id', name:'no', align:'center'},
	{ title:'标题', name:'title', align:'left',renderer:titleFixed},
	{ title:'作者', name:'authorAccount', width:90,align:'left',renderer:authorFixed},
	{ title:'更新时间', name:'updateTime',width:130, align:'center',lockWidth:true, renderer: YouGou.Util.timeFixed},
	{ title:'文章状态', name:'publishStatus', align:'center',renderer:publishFixed},
	{ title:'是否推荐', name:'recommendFlag',width:50, align:'center',renderer:recommendFixed},
	{ title:'序号', name:'sortNo',width:110, align:'center',renderer:snFixed},
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
	url: '/discover/queryArticleData.sc',
	fullWidthRows: true,
	checkCol: true,
	multiSelect: true,
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
                moveToRecycle(item.id);
            }
   		});
    }else if(action == "unPublish"){//取消发布
    	publishArticle(item.id,1);
    }else if(action == 'publish'){//发布文章
    	publishArticle(item.id,2);
    }else if(action == "unRecommend"){//取消推荐
    	recommendArticle(item.id,1);
	}else if(action == 'recommend'){//推荐文章
		recommendArticle(item.id,2);
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
//删除后放入回收站
function moveToRecycle(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/discover/updateArticle.sc',
	  	data: {"id":id,"deleteFlag":"2","publishStatus":"1","recommendFlag":"1","type":"article_delete"},
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

function showForm(){
	$('#girdContent').addClass('hide');
	$('#articleNavbar,#articleForm').removeClass('hide');
	YouGou.UI.resetForm("articleForm");
	$(".img").html('');
	Article_New.Editor().html('');
	$('#authorType option:first').prop('selected',true);
	$('#channelId option:first').prop('selected',true);
	$('#tjDiv :radio:last').prop('checked',true);
	if(count == 1){
		initUploadIMg();
		count = 2;
	}
	$('#authorAccount').prop('readonly',true);
}

//编辑
function showEditForm(id,title){
	YouGou.Ajax.doPost({
		tips : false,
		successMsg: '文章标题【'+ title +'】修改成功!',
		url : "/discover/getArticleById.sc",
		data : {"id" : id},
	  	success : function(data){
  			showForm();
  			YouGou.UI.initForm("articleForm", data.data);
  			var imgHtml = '<img id="uploadPic" src="'+picBaseUlr + data.data.picCover+'" style="width:290px;height:224px;" />';
			$(".img").html(imgHtml);
			Article_New.Editor().html($('#content').val());
			var authorType = $('#authorType').val();
			if(authorType == '2'){
				$('#authorAccount').prop('readonly',false);
			}
		}
	});
}

function hideForm(){
	$('#girdContent').removeClass('hide');
	$('#articleNavbar,#articleForm').addClass('hide');
}

function saveArticle(status){
	var title = $('#title').val();
	var authorType = $('#authorType option:selected').val();
	var authorAccount = $('#authorAccount').val();
	var channelId = $('#channelId option:selected').val();
	var picCover = $('#picCover').val();
	var content = $('#content').val();
	if(title == ''){
		YouGou.UI.Dialog.autoCloseTip("请输入标题");
		return false;
	}
	if(authorType == '' || authorType == undefined){
		YouGou.UI.Dialog.autoCloseTip("请选择作者类型");
		return false;
	}
	if(authorType == '2' && authorAccount == ''){
		YouGou.UI.Dialog.autoCloseTip("请输入作者账号");
		return false;
	}
	if(channelId == '' || channelId == undefined){
		YouGou.UI.Dialog.autoCloseTip("请选择频道");
		return false;
	}
	if(picCover == ''){
		YouGou.UI.Dialog.autoCloseTip("请上传封面");
		return false;
	}
	if(content == ''){
		YouGou.UI.Dialog.autoCloseTip("请编辑文章内容");
		return false;
	}
	if(status == 2){
		$('#publishStatus').val(2);
	}
	YouGou.Ajax.doPost({
		successMsg: '文章'+ (YouGou.Util.isEmpty($("#id").val())?"创建":"修改") +'成功!',
		url: '/discover/saveArticle.sc',
	  	data: $("#articleForm").serializeArray(),
	  	async: false,
	  	success : function(data){
  			mmGrid.load();
  			hideForm();
		}
	});
}

/**function publishArticle(status){
	var str = '';
	var type = "";
	if(status ==2){
		str = '发布文章';
		type = "article_publish";
	}else{
		str = '取消发布';
		type = "article_unPublish";
	}
	YouGou.UI.Dialog.confirm({
		message : "确定要"+str+"吗？"
	},function(result){
		if(result) {
			YouGou.Ajax.doPost({
				successMsg: str + '成功！',
				url: '/discover/updateArticle.sc',
			  	data: { "id" : id , "publishStatus" : status,"type" : type},
			  	success : function(data){
		  			mmGrid.load();
				}
			});
		}
	});
}*/

function publishArticle(id,status){
	var str = '';
	var flag = '';
	var type = "";
	if(status == 2){
		str = '发布文章';
		type = "article_publish";
	}else{
		str = '取消发布';
		var flag = '1';
		type = "article_unPublish";
	}
	YouGou.UI.Dialog.confirm({
		message : "确定要"+str+"吗？"
	},function(result){
		if(result) {
			YouGou.Ajax.doPost({
				successMsg: str + '成功！',
				url: '/discover/updateArticle.sc',
			  	data: { "id" : id,publishStatus : status,"recommendFlag" : flag,"type" : type},
			  	success : function(data){
		  			mmGrid.load();
				}
			});
		}
	});
}

function recommendArticle(id,flag){
	var str = '';
	var type = '';
	if(flag == 2){
		str = '推荐文章';
		type = "article_recommend";
	}else{
		str = '撤销推荐';
		type = "article_unRecommend";
	}
	YouGou.UI.Dialog.confirm({
		message : "确定要"+str+"吗？"
	},function(result){
		if(result) {
			YouGou.Ajax.doPost({
				successMsg: str + '成功！',
				url: '/discover/updateArticle.sc',
			  	data: { "id" : id , "recommendFlag" : flag,"type" : type},
			  	success : function(data){
		  			mmGrid.load();
				}
			});
		}
	});
}

//批量发布/取消发布
function batchPublish(status){
	var tips = '';
	var flag = '';
	var type = "";
	if(status == 2){
		tips = '发布';
		type ='article_publish';
	}else{
		tips = '取消发布';
		flag = '1';
		type = 'article_unPublish';
	}
	var checkObjs = $('#grid-table input:checkbox:checked');
	if(checkObjs.length <= 0){
		YouGou.UI.Dialog.autoCloseTip("请勾选要"+tips+"的文章");
		return;
	}
	YouGou.UI.Dialog.confirm({
		message : "确定要批量"+tips+"文章吗？"
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
				successMsg: '批量' + tips + '文章成功！',
				url: '/discover/batchUpdateArticle.sc',
			  	data: { "id" : ids ,"publishStatus":status, "recommendFlag" : flag ,"type" : type},
			  	success : function(data){
		  			mmGrid.load();
				}
			});
		}
	});
}
//批量删除
function batchDelete(){
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
				url: '/discover/batchUpdateArticle.sc',
			  	data: {"id":ids,"deleteFlag":"2","publishStatus":"1","recommendFlag":"1","type":"article_delete"},
			  	success : function(data){
		  			mmGrid.load();
				}
			});
		}
	});
}

var editHotSn = function(obj){
	var hotSnGroup = $(obj).parent();
	hotSnChange(hotSnGroup,'edit');
	var hotSnInput = hotSnGroup.find('.hotSnInput');
	hotSnInput.select();
}

var saveHotSn = function(obj,id){
	var hotSnGroup = $(obj).parent();
	var hotSnText = hotSnGroup.find('.hotSnText');
	var hotSnInput = hotSnGroup.find('.hotSnInput');
	var oldHotSn = hotSnText.text();
	var hotSn = hotSnInput.val();
	if(hotSn==""){
		YouGou.UI.Dialog.autoCloseTip("序号不能为空");
		return;
	}
	if(hotSn != oldHotSn){
		var r = /^\+?[1-9][0-9]*$/;
		if(!r.test(hotSn)){
			YouGou.UI.Dialog.autoCloseTip("序号必须是正整数");
			return;
		}
		YouGou.Ajax.doPost({
			successMsg: '修改成功！',
			url: '/discover/updateArticle.sc',
		  	data: { "id" : id,"sortNo": hotSn ,"type" : "article_sort"},
		  	success : function(data){
	  			mmGrid.load();
	  			hotSnChange(hotSnGroup,'save');
			}
		});
	}else{
		hotSnChange(hotSnGroup,'save');
	}
}

var cancelHotSn = function(obj){
	var hotSnGroup = $(obj).parent();
	hotSnChange(hotSnGroup,'cancel');
	var hotSnText = hotSnGroup.find('.hotSnText');
	var hotSnInput = hotSnGroup.find('.hotSnInput');
	hotSnInput.val(hotSnText.text());
}

var hotSnChange = function(hotSnGroup,action){
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

function initUploadIMg(){
	//图片上传尺寸限制
	var ratio = window.devicePixelRatio || 1;
	var thumbnailWidth = 290 * ratio;
	var thumbnailHeight = 224 * ratio;
	//图片上传功能
	var uploader = WebUploader.create({
		//选择完文件之后自动上传
		auto : true,
		swf : '/static/js/webuploader/Uploader.swf',
		server : '/discover/uploadArticleCover.sc',
		pick : '#filePicker',
		duplicate : true,
		formData : '',
		accept : {
			title : 'Images',
			extensions : 'gif,jpg,jpeg,bmp,png',
			mimeTypes: 'image/*'
		}
	});

	uploader.on('uploadSuccess', function(file, response) {//上传图片
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
		uploader.makeThumb(file, function(error, src) {//放图片缩略图
			if (error) {
				return;
			}
			var imgHtml = '<img id="uploadPic" src="'+response.data.imgBasePath + response.data.imgPath+'" style="width:290px;height:224px;" />';
			$(".img").html(imgHtml);
			$("#picCover").val(response.data.imgPath);
			$("#picUrl").val(response.data.imgPath);
		}, thumbnailWidth, thumbnailHeight );
	});

	// 文件上传失败，现实上传出错。
	uploader.on( 'uploadError', function( file ) {
		YouGou.UI.Dialog.autoCloseTip("图片上传失败");
	});

	uploader.on('beforeFileQueued', function( file ) {
		var size = 1*1024*800;
		if(file.size > size){
			YouGou.UI.Dialog.autoCloseTip("图片大小不能超过800k，请重新选择");
			return false;
		}else{
			uploader.reset();
			return true;
		}
	});
	uploader.on('fileQueued', function( file ) {
	    
	});
}

$(function(){
	$('#authorType2').change(function(){
		var authorType = $(this).val();
		var accountObj = $('#authorAccount2');
		if(authorType == '1'){
			accountObj.prop('readonly',true);
			accountObj.val('');
		}else{
			accountObj.prop('readonly',false);
		}
	});
	
	$('#authorType').change(function(){
		var authorType = $(this).val();
		var accountObj = $('#authorAccount');
		if(authorType == '1'){
			accountObj.prop('readonly',true);
			accountObj.val('');
		}else{
			accountObj.prop('readonly',false);
		}
	});
});
