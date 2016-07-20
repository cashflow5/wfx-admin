var loadTable = function(parentId){
	$.get('/cms/querySaleCatById.sc?parentId='+parentId, function (data) {
		$('#goodsCategoryList').html(data);
		loadPic();
	});
}

/*************
	CommoditySaleCat
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="update">删除</a>&nbsp;&nbsp;');
    return html.join('');
};

// 列集合
var cols = [
	{ title:'子分类', name:'name', align:'left'},
	{ title:'图片', name:'picUrl', align:'center',renderer:function(val,item){
		var html;
		if(val){
			html = "<img class='saleCatPic' src='" + val + "' />";
		}else{
			html = "<span class='saleCatPic' style='color:red;display:block;'>请上传图片</span>";
		}
		return html;
	}},
	{ title:'产品数', name:'productNum', align:'center'},
	{ title:'最后更新人', name:'updateUser', align:'left'},
	{ title:'最后更新时间', name:'updateTime', align:'center', renderer: YouGou.Util.timeFixed},
	{ title:'操作', name:'' ,width:200, align:'center',lockWidth:true, renderer: actionFixed},
    { title:'ID', name:'id', hidden: true}
];

//删除
function removeSaleCat(id,parentId,name){
	YouGou.UI.Dialog.confirm({
		message : "确定要删除（"+name+"）吗？"
	},function(result){
		if(result) {
			YouGou.Ajax.doPost({
				successMsg: '删除成功！',
				url: '/cms/updateSaleCat.sc',
			  	data: { "id" : id , "deleteFlag" : 1 },
			  	success : function(data){
			  		location.reload();
			  		removeHoverDom('',{tid:id});
				}
			});
		}
	});
}
//更新状态时使用
function updateSaleCat(id,deleteFlag){
	YouGou.Ajax.doPost({
		successMsg: '更新成功！',
		url: '/cms/saveSaleCat.sc',
	  	data: { "id" : id ,"deleteFlag" : deleteFlag },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}
//查询
function doQuery(){
	mmGrid.load();
}

function bindBtn(){
	var bindCatDiv = $('#bindCatDiv');
	bindCatDiv.hide();
	var bindBasicDiv = $('#bindBasicDiv');
	bindBasicDiv.show();
}

function toBind(){
	var levelone = $('#levelone option:selected').val();
	var leveltwo = $('#leveltwo option:selected').val();
	var levelthree = $('#levelthree option:selected').val();
	if(levelthree == '' || leveltwo == '' || levelone == ''){
		YouGou.UI.Dialog.autoCloseTip('请选择基础分类');
		return false;
	}
	var baseCatId = $('baseCatId');
	$.ajax({
		url:'/cms/saveBasicSaleRela.sc',
		type:'post',
		data:{
				"saleCatId":$('#saleCatId').val(),
				"saleCatNo":$('#saleCatNo').val(),
				"baseCatId":levelthree
			},
		dataType:'json',
		success:function(data){
			if(data.state == 'success'){
				loadTable($('#saleCatId').val());
			}else{
				YouGou.UI.Dialog.autoCloseTip("绑定基础分类出错");
			}
		}
	});
}
//分类联动及选择功能
function catChange(level,obj){
	var levelone = $('#levelone');
	var leveltwo = $('#leveltwo');
	var levelthree = $('#levelthree');
	var initHtml = '<option value="">请选择</option>';
	var parentId = $(obj).find('option:selected').val();
	if(level != '3'){
		$.ajax({
			url:'/commodity/getCatHtmlByParentId.sc',
			type:'post',
			data:{"parentId":parentId},
			dataType:'html',
			success:function(data){
				if(level == '1'){
					leveltwo.html(data);
					levelthree.html(initHtml);
				}else if(level == '2'){
					levelthree.html(data);
				}
			}
		});
	}
}

function removeRela(id,name){
	YouGou.UI.Dialog.confirm({
		message : "确定要解除基础分类（"+name+"）的绑定吗？"
	},function(result){
		if(result) {
			YouGou.Ajax.doPost({
				successMsg: '解除绑定成功！',
				url: '/cms/removeBasicSaleRela.sc',
			  	data: { "id" : id },
			  	success : function(data){
			  		loadTable($('#saleCatId').val());
				}
			});
		}
	});
}

function loadPic(){
    $('.upload-img').click(function(){
    	var uploader = '';
    	$('#saleCatId').val($(this).attr('saleCatId'));
        layer.open({
            title:"请上传图片",
            type:1,
            content:'<div style="height:20px;"></div><div style="height:50px;margin:0 0 0 5px" id="filePicker">&nbsp;&nbsp;选择文件</div><div class="tips">&nbsp;&nbsp;说明：图片尺寸为80*80，大小不能超过1M</div><span id="fileName" class="file-text ml10"></span><div class="blank10"></div>',
            area:["350px","auto"],
            btn:['确定',"取消"],
            yes:function(index){
            	uploader.upload();
            	uploader.on( 'uploadSuccess', function( file,response ) {
                    layer.close(index);
                    var state = response.state;
                    if(state == 'success'){
                    	loadTable($('#parentId').val());
                    }else{
                    	var code = response.msg;
                    	if(code == '1'){
                    		YouGou.UI.Dialog.autoCloseTip("请先选择图片");
                    	}else if(code == '2'){
                    		YouGou.UI.Dialog.autoCloseTip("图片大小不能超过1M");
                    	}else if(code == '6'){
                    		YouGou.UI.Dialog.autoCloseTip("图片尺寸必须是80*80");
                    	}else{
                    		YouGou.UI.Dialog.autoCloseTip("上传文件"+file.name+"出错，请联系系统管理员");
                    	}
                    }
            	});
            	uploader.on( 'uploadError', function( file) {
            		YouGou.UI.Dialog.autoCloseTip("上传文件"+file.name+"出错");
            	});
            },
            cancel:function(){
                
            },
            success:function(){
				//上传图片
				uploader = WebUploader.create({
					//选择完文件之后自动上传
					auto : false,
					swf : '/static/js/webuploader/Uploader.swf',
					server : '/cms/uploadSaleCatImg.sc',
					pick : '#filePicker',
					duplicate : true,
					formData : {'saleCatId' : $('#saleCatId').val()},
					accept : {
						title : 'Images',
						extensions : 'gif,jpg,jpeg,bmp,png',
						mimeTypes: 'image/*'
					}
				});
				uploader.on('beforeFileQueued', function( file ) {
					var size = 1*1024*1024;
					if(file.size > size){
						YouGou.UI.Dialog.autoCloseTip("图片大小不能超过1M，请重新选择");
						return false;
					}else{
						uploader.reset();
						return true;
					}
				});
				uploader.on('fileQueued', function( file ) {
				    $('#fileName').text(file.name);
				});
            }
        });
    });
    
    $('.imgDiv').mouseover(function(){
		$(this).find('.imgLayer').show();
	});
    
    $('.imgDiv').mouseout(function(){
    	$(this).find('.imgLayer').hide();
    });
}
