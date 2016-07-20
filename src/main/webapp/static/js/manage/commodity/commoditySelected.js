// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="add">添加</a>&nbsp;&nbsp;');
    return html.join('');
};

var defaultPicImgs = function(val,item,rowIndex){
	var  defaultPic=item.defaultPic;
	console.log('变量：',defaultPic); 
	var html = [];
	if(defaultPic!=''){
		var img='<img width="40" height="39" alt="" src="'+defaultPic+'"/>';
		html.push(img);
	}else{
		html.push('<img width="40" height="39" alt="" src="${BasePath}/yougou/images/nopic.jpg"/>');
	}
	
	return html.join('');
}
// 列集合
var cols = [
            { title:'商品图片', name:'',width:60, align:'left',renderer:defaultPicImgs},
            { title:'商品名称', name:'commodityName',width:200, align:'left'},
            { title:'品牌名称', name:'brandName', align:'left'},
            { title:'款色编码', name:'supplierCode', align:'left'},
            { title:'商品款号', name:'styleNo', align:'left'},
            { title:'款色ID', name:'no', align:'left'},
            { title:'操作', name:'' ,width:200, align:'center',lockWidth:true, renderer: actionFixed},
            { title:'ID', name:'id', hidden: true}
            
         ];

//分页器
var mmPaginator = $('#commodity-grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("commodityForm");

// 表格	
var commodityMmGrid = $('#commodity-grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/commodity/queryCommoditySelectedData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
commodityMmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    if(action == "add"){// 选中
   		selectcommodity(item.commodityName,item.supplierCode,item.styleNo,item.no);
    }
    e.stopPropagation();  //阻止事件冒泡
});
//生成跳转链接
function buildRedirectUrl(no){
	YouGou.Ajax.doPost({
		tips : false,
		successMsg: '修改成功!',
		url : "/notice/buildRedirectUrl.sc",
		data : {"redirectType" : 2,"value": no},
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
function selectcommodity(commodityName,supplierCode,styleNo,no){
	
	var tips =commodityName;
	$('#commodityTips').text(tips);
	//生成链接(用于轮播图和公告管理)
	buildRedirectUrl(supplierCode);
	$('button.commodityClose').click();
}
//查询
function doQuery(){
	commodityMmGrid.load();
}