/*************
	InventoryList
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
    return html.join('');
};


	//上下架
	var statusFixed = function(val,item,rowIndex){
		var html = [];
		var isOnsale =item.isOnsale;
		if(isOnsale==1){
			html.push("<span>已上架</span>&nbsp;&nbsp;");
		}else if(isOnsale==2){
			html.push("<span>已下架</span>&nbsp;&nbsp;");
		}else if(isOnsale==3){
			html.push("<span>未上架</span>&nbsp;&nbsp;");
		}
	    return html.join('');
	};
var defaultPicImg = function(val,item){
	var html = [];
	if(val!=''){
		var img='<img width="40" height="39" alt="" src="'+item.defaultPic+'"/>';
		html.push(img);
	}else{
		html.push('<img width="40" height="39" alt="" src="/static/images/nopic.jpg"/>');
	}
//	html.push('<span>'+val+'</span>');
	return html.join('');
}

var specFixed = function(val,item){
	return "颜色:"+val +'<br/>尺码:'+item.sizeName;
}

//列集合
var cols = [
   { title:'图片',width:25, name:'defaultPic', align:'left',renderer:defaultPicImg},
   { title:'商品名称',width:300, name:'commodityName', align:'left'},
   { title:'商品规格', name:'specName', align:'left',renderer:specFixed},
   { title:'款色ID', name:'commodityNo', align:'center'},
   { title:'款色编码', name:'supplierCode', align:'left'},
   { title:'商家编码', name:'thirdPartyCode', align:'left'},
   { title:'库存', name:'inventoryNum', align:'center'},
   { title:'商品状态', name:'', align:'center', renderer:statusFixed}
];

//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");

// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/commodity/queryInventoryData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    //编辑
    if(action == "edit"){
    	
    }
    e.stopPropagation();  //阻止事件冒泡
});

function doQuery(){
	var inventoryNumStart = $.trim($('#inventoryNumStart').val());
	var inventoryNumEnd = $.trim($('#inventoryNumEnd').val());
	var r = /^\+?[1-9][0-9]*$/;
	if((inventoryNumStart && !r.test(inventoryNumStart))||(inventoryNumEnd && !r.test(inventoryNumEnd))){
		YouGou.UI.Dialog.alert({message:"商品库存必须是正整数！"});
		return;
	}
	mmGrid.load();
}
