/*************
	CommodityBrand
**************/

// 列集合
var cols = [
	{ title:'中文名称', name:'brandName', align:'center'},
	{ title:'英文名称', name:'englishName', align:'center'},
    { title:'品牌编码', name:'brandNo', hidden: true}
];

// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");
// 表格	
var mmGrid = $('#brandList-grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/finance/queryBrandData.sc',
	fullWidthRows: true,
	autoLoad: true,
	checkCol: true,
	multiSelect: false, //多选,默认false单选
	plugins: [mmFormParams]
});

function doQuery(){
	mmGrid.load();
}
