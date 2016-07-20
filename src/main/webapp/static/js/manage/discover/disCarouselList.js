/*************
	DiscoverCarouselFigure
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="edit">编辑</a>&nbsp;&nbsp;');
	html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
    return html.join('');
};
// 修改顺序列动作
var actionSortFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="toTop">置顶</a>&nbsp;&nbsp;');
	html.push('<a href="javascript:void(0);" action="setUp">↑</a>&nbsp;&nbsp;');
	html.push('<a href="javascript:void(0);" action="setDown">↓</a>&nbsp;&nbsp;');
	return html.join('');
};
// 操作列动作
var showFixed = function(val,item){
	var html = '';
	if(val == '10'){
		html = '显示';
	}else{
		html = '隐藏';
	}
	return html;
};

// 列集合
var cols = [
    { title:'序号', name:'sortNo',width:50, align:'center',lockWidth:true },
	{ title:'轮播图名称', name:'name',width:120, align:'left'},
	{ title:'轮播图片', name:'picture', width:143,align:'center',renderer:picFixed},
	{ title:'链接文章id', name:'articleNo',width:100, align:'left'},
	{ title:'文章标题', name:'articleTitle', align:'left'},
	{ title:'文章作者', name:'articleAuthor', align:'left',width:100, lockWidth:true},
	{ title:'状态', name:'showFlag', align:'center',width:50, lockWidth:true,renderer:showFixed},
	{ title:'最近更新人', name:'operUser', align:'left'},
	{ title:'最近更新时间', name:'updateTime', width:130,align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'操作', name:'' ,width:100, align:'center',lockWidth:true, renderer: actionFixed},
	{ title:'修改顺序', name:'' ,width:100, align:'center',lockWidth:true, renderer: actionSortFixed},
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
	url: '/discover/queryCarouselData.sc',
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
                removeCarousel(item.id);
            }
   		});
    }else if(action == 'toTop'){
    	YouGou.Ajax.doPost({
    		successMsg: '置顶成功！',
    		url: '/discover/setTop.sc',
    	  	data: { "id" : item.id },
    	  	success : function(data){
      			mmGrid.load();
    		}
    	});
    }else if(action == 'setUp'){
    	YouGou.Ajax.doPost({
    		successMsg: '上移成功！',
    		url: '/discover/toUp.sc',
    	  	data: { "id" : item.id },
    	  	success : function(data){
      			mmGrid.load();
    		}
    	});
    }else if(action == 'setDown'){
    	YouGou.Ajax.doPost({
    		successMsg: '下移成功！',
    		url: '/discover/toDown.sc',
    	  	data: { "id" : item.id },
    	  	success : function(data){
      			mmGrid.load();
    		}
    	});
    }
    e.stopPropagation();  //阻止事件冒泡
});
//删除
function removeCarousel(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/discover/removeCarousel.sc',
	  	data: { "id" : id },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}
//更新状态时使用
function updateCarousel(id,status){
	YouGou.Ajax.doPost({
		successMsg: '更新成功！',
		url: '/discover/saveCarousel.sc',
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
function showForm(){
	$('#girdContent').addClass('hide');
	$('#carouselEdit,#carouselForm').removeClass('hide');
	YouGou.UI.resetForm("carouselForm");
	$('#articleTips').text('');
	$(".img").html('');
	if(count == 1){
		initUploadIMg();
		count = 2;
	}
}

//返回列表
function hideForm(){
	$('#carouselEdit,#carouselForm').addClass('hide');
	$('#girdContent').removeClass('hide');
}
//返回列表
function saveCarousel(){
	var name = $('#name').val();
	var picture = $('#picture').val();
	var articleNo = $('#articleNo').val();
	if(name == ''){
		YouGou.UI.Dialog.autoCloseTip("请输入轮播图名称");
		return false;
	}
	if(picture == ''){
		YouGou.UI.Dialog.autoCloseTip("请上传图片");
		return false;
	}
	if(articleNo == ''){
		YouGou.UI.Dialog.autoCloseTip("请输添加文章");
		return false;
	}
	
	YouGou.Ajax.doPost({
		successMsg: '轮播图'+ (YouGou.Util.isEmpty($("#id").val())?"创建":"修改") +'成功!',
		url: '/discover/saveCarousel.sc',
	  	data: $("#carouselForm").serializeArray(),
	  	async: false,
	  	success : function(data){
  			mmGrid.load();
  			hideForm();
		}
	});
}

//批量删除
function batchDelete(){
	var checkObjs = $('#grid-table input:checkbox:checked');
	if(checkObjs.length <= 0){
		YouGou.UI.Dialog.autoCloseTip("请勾选要删除的轮播图");
		return;
	}
	YouGou.UI.Dialog.confirm({
		message : "确定要批量删除轮播吗？"
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
				successMsg: '批量删除轮播图成功！',
				url: '/discover/batchRemoveCarousel.sc',
			  	data: {"ids":ids},
			  	success : function(data){
		  			mmGrid.load();
				}
			});
		}
	});
}

//编辑
function showEditForm(id,name){
	YouGou.Ajax.doPost({
		tips : false,
		successMsg: '轮播图名称【'+ name +'】修改成功!',
		url : "/discover/getCarouselInfoById.sc",
		data : {"id" : id},
	  	success : function(data){
  			showForm();
  			YouGou.UI.initForm("carouselForm", data.data);
  			//初始化文章说明的显示
  			$('#articleTips').text($('#articleMark').val());
  			var imgHtml = '<img id="uploadPic" src="'+picBaseUlr + data.data.picture+'" style="width:640px;height:300px;" />';
			$(".img").html(imgHtml);
		}
	});
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
		server : '/discover/uploadCarImg.sc',
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
			var imgHtml = '<img id="uploadPic" src="'+response.data.imgBasePath + response.data.imgPath+'" style="width:640px;height:300px;" />';
			$(".img").html(imgHtml);
			$("#picture").val(response.data.imgPath);
			$("#picUrl").val(response.data.imgPath);
		}, thumbnailWidth, thumbnailHeight );
	});

	// 文件上传失败，现实上传出错。
	uploader.on( 'uploadError', function( file ) {
		YouGou.UI.Dialog.autoCloseTip("图片上传失败");
	});

	uploader.on('beforeFileQueued', function( file ) {
		var size = 1536*1024;
		if(file.size > size){
			YouGou.UI.Dialog.autoCloseTip("图片大小不能超过1.5M，请重新选择");
			return false;
		}else{
			uploader.reset();
			return true;
		}
	});
	uploader.on('fileQueued', function( file ) {
	    
	});
}