/*************
	HotBrand
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	if($("#hotFlag").val() == '2'){
		html.push('<a href="javascript:void(0);" action="add">添加</a>&nbsp;&nbsp;');
	}else{
		html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
	}
    return html.join('');
};

//修改价格
function editHotSn(val){
	$(val).hide();
	var c3 = $(val).parent();
	c3.find(".curHotSn").hide();
	c3.find("input").show();
	c3.find("input").val(c3.find(".curHotSn").text());
	c3.find("input").focus();
	c3.find(".save4HotSn").css({display:"inline-block"});
	c3.find(".cancel4HotSn").css({display:"inline-block"});
}

function cancelHotSn(val){
	$(val).hide();
	var c3 = $(val).parent();
	c3.find(".curHotSn").show();
	c3.find("input").hide();
	c3.find(".edit4HotSn").css({display:"inline-block"});
	c3.find(".save4HotSn").hide();
	c3.find(".cancel4HotSn").hide();
}

function saveHotSn(val){	
	var curObj = $(val);
	var brandId = curObj.attr("brandId");
	var hotSn = $.trim(curObj.parent().find("input").val());

	var r = /^\+?[1-9][0-9]*$/;
	if(!r.test(hotSn)){
		YouGou.UI.Dialog.alert({message:"序号必须是正整数"});
		return ;
	}

	curObj.hide();
	curObj.parent().find(".cancel4HotSn").hide();
	var load = curObj.parent().find(".loadimg");
	load.show();	
	YouGou.Ajax.doPost({
		successMsg: '修改成功！',
		url: '/cms/saveBrandHotSn.sc',
	  	data: { "id" : brandId,hotSn: hotSn,status:1 },
	  	success : function(data){
	  		load.hide();
	  		mmGrid.load();
		}
	});
}

var hotSnFixed = function(val, item){
	var html = "";
	var brandId = item.id;
	html += '<em class="c3">';
	html += '<span class="curHotSn">'+val+'</span>';
	html += '<input type="text" value="'+val+'" />';
	html += '<a href="javascript:void(0)" onclick="editHotSn(this)" class="edit4HotSn" title="修改"></a>';
	html += '<a href="javascript:void(0)" onclick="saveHotSn(this)" brandId="'+brandId+'" class="save4HotSn" title="保存"></a>';
	html += '<a href="javascript:void(0)" onclick="cancelHotSn(this)" class="cancel4HotSn" title="取消修改"></a>';
	html += '<span class="loadimg"><img src="/static/images/loading16.gif"></span>';
	html += '</em>';
	 return html;
}


var imageFixed = function(val){
	return "<img width='100' height='40' src='"+val+"'/>"
}

// 列集合
var cols = [
    { title:'品牌图片', width:110, lockWidth:true, name:'mobilePic', align:'center', renderer: imageFixed},
    { title:'品牌编码', name:'brandNo', align:'left'},
	{ title:'中文名称', name:'brandName', align:'left'},
	{ title:'英文名称', name:'englishName', align:'left'},
	{ title:'排序号', name:'hotSn', align:'center', sortable: true, sortName:'hotSn', renderer: hotSnFixed },
	{ title:'操作', name:'' ,width:200, align:'center',lockWidth:true, renderer: actionFixed},
    { title:'ID', name:'id', hidden: true}
];
if($("#hotFlag").val() == '2'){
	cols[4].hidden = true;
}

//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");
// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/cms/queryHotBrandData.sc',
	fullWidthRows: true,
	autoLoad: true,
	multiSelect: true,
	checkCol: true,	
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    if(action == "delete"){// 删除
   		YouGou.UI.Dialog.confirm({
   			message : "确定要删除吗？"
   		},function(result){
   			if(result) {
                removeHotBrand(item.id);
            }
   		});
    }else if(action == "add"){
    	YouGou.UI.Dialog.confirm({
   			message : "确定要添加到热门品牌吗？"
   		},function(result){
   			if(result) {
   				YouGou.Ajax.doPost({
					successMsg: '添加成功！',
					url: '/cms/addHotBrand.sc',
				  	data: { "brandIds" : item.id },
				  	success : function(data){
				  		switchPage(1);
					}
				});
            }
   		});
    }
    e.stopPropagation();  //阻止事件冒泡
});

function removeHotBrand(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/cms/removeHotBrand.sc',
	  	data: { "brandIds" : id },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}

function doQuery(){
	mmGrid.load();
}

//批量删除
function batchRemoveBrand(){
	var array = mmGrid.selectedRows();
	if(array.length<=0){
		YouGou.UI.Dialog.alert({message:"请选择要删除的热门品牌"});
		return ;
	}
	YouGou.UI.Dialog.confirm({
			message : "确定要批量删除吗？"
		},function(result){
			if(result) {
				var brandIds = "";
				for(var i=0;i<array.length;i++ ) {
					var item = array[i];
					brandIds = brandIds+ item.id+",";
				}
				YouGou.Ajax.doPost({
					successMsg: '删除成功！',
					url: '/cms/removeHotBrand.sc',
				  	data: { "brandIds" : brandIds },
				  	success : function(data){
			  			mmGrid.load();
					}
				});
           }
	});
	
}

//批量添加
function batchAddBrand(){
	var array = mmGrid.selectedRows();
	if(array.length<=0){
		YouGou.UI.Dialog.alert({message:"请选择要添加的品牌"});
		return ;
	}
	YouGou.UI.Dialog.confirm({
			message : "确定要批量添加到热门品牌吗？"
		},function(result){
			if(result) {
				var brandIds = "";
				for(var i=0;i<array.length;i++ ) {
					var item = array[i];
					brandIds = brandIds+ item.id+",";
				}
				YouGou.Ajax.doPost({
					successMsg: '添加成功！',
					url: '/cms/addHotBrand.sc',
				  	data: { "brandIds" : brandIds },
				  	success : function(data){
				  		switchPage(1);
					}
				});
           }
	});
	
}

function switchPage(hotFlag){
	window.location.href = "/cms/hotBrandList.sc?hotFlag="+hotFlag;
}