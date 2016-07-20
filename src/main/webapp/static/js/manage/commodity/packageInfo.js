/***********************************分销包新增/编辑****************************************/
//上传小图
var uploader_small = WebUploader.create({
	//选择完文件之后自动上传
	auto : true,
	swf : '/static/js/webuploader/Uploader.swf',
	server : '/commodity/uploadImg.sc',
	pick : '#selectSmallPic',
	duplicate : true,
	formData : {'imgType' : 1},
	accept : {
		title : 'Images',
		extensions : 'gif,jpg,jpeg,bmp,png',
		mimeTypes: 'image/*'
	}
});

uploader_small.on('uploadSuccess', function(file, response) {//上传图片
	uploader_small.makeThumb(file, function(error, src) {//放图片缩略图
		if (error) {
			return;
		}
		$(".smallImg").attr('src', response.data.imgBasePath + '/' + response.data.imgPath);
		$("#bagSmallPic").val(response.data.imgPath);
	}, 300, 180);
});

//上传大图
var uploader_big = WebUploader.create({
	//选择完文件之后自动上传
	auto : true,
	swf : '/static/js/webuploader/Uploader.swf',
	server : '/commodity/uploadImg.sc?imgType=2',
	pick : '#selectBigPic',
	duplicate : true,
	formData : {'imgType' : 2},
	accept : {
		title : 'Images',
		extensions : 'gif,jpg,jpeg,bmp,png',
		mimeTypes: 'image/*'
	}
});

uploader_big.on('uploadSuccess', function(file, response) {//上传图片
	uploader_big.makeThumb(file, function(error, src) {//放图片缩略图
		if (error) {
			return;
		}
		$(".bigImg").attr('src', response.data.imgBasePath + '/' + response.data.imgPath);
		$("#bagBigPic").val(response.data.imgPath);
	}, 640, 240 );
});

//图片
var m1_renderer_pic = function(val,item,rowIndex){
	var html = [];
	if(val!=''){
		var img='<img width="40" height="39" alt="" src="'+val+'"/>';
		html.push(img);
	}else{
		html.push('<img width="40" height="39" alt="" src="${BasePath}/yougou/images/nopic.jpg"/>');
	}
	
	return html.join('');
};

//操作列
var m1_renderer_action = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="delete">取消</a>&nbsp;&nbsp;');
	return html.join('');
};

//列集合
var m1_cols = [
   { title:'ID', name:'id', align:'left',hidden:true,lockDisplay:true },
   { title:'图片', name:'defaultPic', align:'left', renderer : m1_renderer_pic },
   { title:'商品名称', name:'commodityName', align:'left'},
   { title:'分类名称', name:'supplierCode', align:'left'},
   { title:'品牌名称', name:'brandName', align:'left'},
   { title:'款色编码', name:'supplierCode', align:'left'},
   { title:'商品编码', name:'no', align:'left'},
   { title:'市场价', name:'publicPrice', align:'center'},
   { title:'可售库存', name:'no', align:'left'},
   { title:'操作', name:'' ,width:100, align:'center',lockWidth:true,  renderer : m1_renderer_action }
];



// 表格	
var m1_mmGrid = $('#select-commodity-table').mmGrid({
	height: 'auto',
	cols: m1_cols,
	fullWidthRows: true,
	autoLoad: false,
	multiSelect: true,
	checkCol: true
});

//本地分页器
var m1_mmPaginator = $('#select-commodity-table-pager').wfxLocalPaginator({
	page : 1,// 第几页
	totalCount : 0,// 总记录数
	limit : 5 ,// 一页多少条
	items : [],// 本地数据
	mmGrid : m1_mmGrid
});

m1_mmPaginator.init();

m1_mmPaginator.update(commodityList);

//表格事件
m1_mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    //取消
    if(action == "delete"){
    	var array = [item];
    	removeCommodity(array);
    }
    e.stopPropagation();  //阻止事件冒泡
});

//增加商品
function addCommodity(array){
	if(array.length<=0){
		YouGou.UI.Dialog.alert({message:"请选择要添加的微分销商品"});
		return ;
	}
	var list = m1_mmPaginator.opts.items;
	for(var i=0;i<array.length;i++){
		var flag = true;
		for(var j=0;j<m1_mmPaginator.opts.items.length;j++){
			if(m1_mmPaginator.opts.items[j] && array[i].id == m1_mmPaginator.opts.items[j].id){
				flag = false;
			}
		}
		if(flag){
			list.push(array[i]);
		}
	}
	m1_mmPaginator.update(list);
	YouGou.UI.Dialog.alert({message:"添加商品成功"});
}

//移除商品
function removeCommodity(array){
	if(array.length<=0){
		YouGou.UI.Dialog.alert({message:"请选择要删除的商品"});
		return ;
	}
	var list = m1_mmPaginator.opts.items;
	for(var i=0;i<array.length;i++){
		var index = list.indexOf(array[i]);
		if (index > -1) {
			list.splice(index, 1);
		}
	}
	m1_mmPaginator.update(list);
}

//批量删除商品
$('.delete-commodity').on('click', function(){
	var array = m1_mmGrid.selectedRows();
	removeCommodity(array);
});
//保存分销包信息
$('.save-package').on('click', function(){
	if(!validateForm()) {
		return;
	}
	var list = [];
	var rows = m1_mmPaginator.opts.items;
	for(var i=0;i<rows.length;i++){
		if(rows[i]&&rows[i].id){
			list.push(rows[i].id);
		}
	}
	$("#packageInfoForm #commodityIds").val(list.join(','));
	YouGou.Ajax.doPost({
		successMsg: '分销包'+ (YouGou.Util.isEmpty($("#id").val())?"创建":"修改") +'成功!',
		url: '/commodity/savePackageData.sc',
	  	data: $("#packageInfoForm").serializeArray(),
	  	success : function(data){
	  		window.location.href="/commodity/packageList.sc";
		}
	});
});

function validateForm(){
	//分销包名称非空
	var bagName = $("#packageInfoForm #bagName").val();
	bagName = bagName.replace(/(^\s*)|(\s*$)/g, "");//去左右空格
	if(YouGou.Util.isEmpty(bagName)) {
		YouGou.UI.tip("请填写分销包名称",'error');
		return false;
	}
	
	//分销包小图非空
	var bagSmallPic = $("#bagSmallPic").val();
	bagSmallPic = bagSmallPic.replace(/(^\s*)|(\s*$)/g, "");//去左右空格
	if(YouGou.Util.isEmpty(bagSmallPic)) {
		YouGou.UI.tip("请填上传分销包小图",'error');
		return false;
	}
	
	//分销包打图非空
	var bagBigPic = $("#bagBigPic").val();
	bagSmallPic = bagSmallPic.replace(/(^\s*)|(\s*$)/g, "");//去左右空格
	if(YouGou.Util.isEmpty(bagBigPic)) {
		YouGou.UI.tip("请填上传分销包大图",'error');
		return false;
	}
	
	//序号非空
	var sortNo = $("#sortNo").val();
	sortNo = sortNo.replace(/(^\s*)|(\s*$)/g, "");
	if(YouGou.Util.isEmpty(sortNo)) {
		YouGou.UI.tip("请填上传分销包序号",'error');
		return false;
	}
	return true;
}


/***********************************分销包商品导入操作********************************************/
//导入商品
$('.inport-commodity').on('click', function(){
	$('#inportCommodityPanel').modal('show');
	
});

$('#inportCommodityPanel').on('shown.bs.modal', function () {
	//解决WebUploader在display none无法运行的问题
	$(window).resize();
	m2_uploader_excel.reset();
})

$('#inportCommodityPanel').on('hide.bs.modal', function () {
	// 执行一些动作...
	$("#fileName").html("请选择导入的文件");
})
//下载模板
$('#downloadTemp').click(function(){
	window.location.href = "reportTemplete.sc";
});


//导入分销包商品excel文件
var m2_uploader_excel = WebUploader.create({
	auto : false,
	swf : '/static/js/webuploader/Uploader.swf',
	server : '/commodity/importPackageData.sc',
	pick : '#selectExecleFile',
	fileSizeLimit : 1024*1024,
	fileSingleSizeLimit:1024*1024,
	accept : {
		title : 'EXCEL',
		extensions : 'xls,xlsx',
		mimeTypes: 'application/vnd.ms-excel'
	}
});

m2_uploader_excel.on('uploadSuccess', function(file, response) {
	if(response.data.length>0){
		var array = response.data;
		addCommodity(array);
	}
	m2_uploader_excel.removeFile( file );
});

m2_uploader_excel.on( 'uploadError', function( file ) {
	YouGou.UI.tip("上传商品数据失败！",'error');
	m2_uploader_excel.removeFile( file );
});

m2_uploader_excel.on( 'fileQueued', function( file ) {
	$("#fileName").html(file.name);
});


$('#importCommodity').click(function(){
	m2_uploader_excel.upload();
});

/***********************************分销包商品操作********************************************/
//添加商品
$('.add-commodity').on('click', function(){
	$('#commodityList').modal('show');
	m3_mmGrid.load();
});

$('#commodityList').on('show.bs.modal', function () {
	YouGou.Ajax.doPost({
		tips: false,
		url: '/commodity/queryBasicList.sc',
	  	success : function(result){
			var html = '<option value="">请选择</option>';
			if(result.state=='success'){
				for(var i=0; i < result.data.length; i++){
					var item = result.data[i];
					html += '<option value="'+item.id+'">'+item.catName+'</#if></option>'
				}
			}
	  		$('#levelone').html(html);
		}
	});
})

$('#commodityList').on('hide.bs.modal', function () {
	// 执行一些动作...
	YouGou.UI.resetForm("searchCommodityForm");	
})

//搜索微分销商品
$('#searchCommodityForm .search').click(function(){
	m3_mmGrid.load();
});
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

//操作列
var m3_renderer_pic = function(val,item,rowIndex){
	var html = [];
	if(val!=''){
		var img='<img width="40" height="39" alt="" src="'+val+'"/>';
		html.push(img);
	}else{
		html.push('<img width="40" height="39" alt="" src="${BasePath}/yougou/images/nopic.jpg"/>');
	}
	
	return html.join('');
};

//操作列
var m3_renderer_action = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="add">添加</a>&nbsp;&nbsp;');
	return html.join('');
};

//列集合
var m3_cols = [
 { title:'ID', name:'id', align:'left',hidden:true,lockDisplay:true },
 { title:'图片', name:'defaultPic', align:'left', renderer : m3_renderer_pic },
 { title:'商品名称', name:'commodityName', align:'left'},
 { title:'分类名称', name:'catName', align:'left'},
 { title:'品牌名称', name:'brandName', align:'left'},
 { title:'款色编码', name:'supplierCode', align:'left'},
 { title:'商品编码', name:'no', align:'left'},
 { title:'市场价', name:'publicPrice', align:'center'},
 { title:'可售库存', name:'stock', align:'left'},
 { title:'操作', name:'' ,width:100, align:'center',lockWidth:true,  renderer : m3_renderer_action }
];
//分页器
var m3_mmPaginator = $('#commodity-pager').mmPaginator({});
//搜索表单属性
var m3_mmFormParams = new MMSearchFormParams("searchCommodityForm");

//表格	
var m3_mmGrid = $('#commodity-table').mmGrid({
	height: 320,
	cols: m3_cols,
	url: '/commodity/queryWfxCommodityData.sc',
	fullWidthRows: true,
	autoLoad: false,
	multiSelect: true,
	checkCol: true,
	plugins: [m3_mmPaginator,m3_mmFormParams]
});

//表格事件
m3_mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
  //添加
  if(action == "add"){
  	var array = [item];
  	addCommodity(array);
  }
  e.stopPropagation();  //阻止事件冒泡
});

$('a.batch-add-commodity').on('click',function(){
	var array = m3_mmGrid.selectedRows();
	addCommodity(array);
});


