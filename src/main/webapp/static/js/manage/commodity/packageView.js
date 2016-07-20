/***********************************分销包查看****************************************/

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


//列集合
var m1_cols = [
   { title:'ID', name:'id', align:'left',hidden:true,lockDisplay:true },
   { title:'图片', name:'defaultPic', align:'left', renderer : m1_renderer_pic },
   { title:'商品名称', name:'commodityName', align:'left'},
   { title:'分类名称', name:'catName', align:'left'},
   { title:'品牌名称', name:'brandName', align:'left'},
   { title:'款色编码', name:'supplierCode', align:'left'},
   { title:'商品编码', name:'no', align:'left'},
   { title:'市场价', name:'publicPrice', align:'center'},
   { title:'可售库存', name:'stock', align:'left'},
];



// 表格	
var m1_mmGrid = $('#select-commodity-table').mmGrid({
	height: 'auto',
	cols: m1_cols,
	fullWidthRows: true,
	autoLoad: false,
	multiSelect: false,
	checkCol: false
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

