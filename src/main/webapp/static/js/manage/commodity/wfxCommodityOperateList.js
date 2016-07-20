/*************
	CommodityStyle
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	var isOnsale =item.isOnsale;
	var publicPrice =item.publicPrice;
	var salePrice =item.salePrice;
	var id =item.id;
	var costPrice=item.costPrice;
	var wfxCostPrice=item.wfxCostPrice;
	var distributableMoney=item.distributableMoney;
	
	var wfxPrice=item.wfxPrice;
	if(wfxCostPrice==null || wfxCostPrice==undefined){
		wfxCostPrice=0;
	}
	if(wfxPrice==null || wfxPrice==undefined){
		wfxPrice=0;
	}
	if(distributableMoney==null || distributableMoney=="undefined"){
		distributableMoney=0;
	}
	if(isOnsale==1){
		html.push("<a  href=\"javascript:void(0);\"  onclick=\"updateShelvesStatus(\'"+id+"\',2,\'"+costPrice+"\',\'"+wfxCostPrice+"\',\'"+wfxPrice+"\');\">下架</a>&nbsp;&nbsp;");
	}else if(isOnsale==2 || isOnsale==3){
		if(wfxCostPrice!=0  &&  wfxPrice!=0){
			html.push("<a  href=\"javascript:void(0);\"  onclick=\"updateShelvesStatus(\'"+id+"\',1,\'"+costPrice+"\',\'"+wfxCostPrice+"\',\'"+wfxPrice+"\');\">上架</a>&nbsp;&nbsp;");
		}
		html.push("<a href=\"javascript:void(0);\"    onclick=\"readjustPrices(\'"+publicPrice+"\',\'"+salePrice+"\',\'"+id +"\',\'"+costPrice +"\',\'"+wfxCostPrice +"\',\'"+wfxPrice +"\',\'"+distributableMoney +"\');\">调价</a>&nbsp;&nbsp;");
	}
	
    return html.join('');
};
function wfxFormula1(costPrice){
	
	var wfxPrice = $("#wfxPrice").val();
	var postFee = $("#postFee").val();
	var taxRate = $("#taxRate").val();
	var vatRate = $("#vatRate").val();
	
	var  distributableMoney=compute1stFormula(wfxPrice,costPrice,vatRate,taxRate,postFee);
	
	if( distributableMoney ){
		   $("#distributableMoney").val(distributableMoney);
	}
}
//上下架
var shelves = function(val,item,rowIndex){
	var html = [];
	var isOnsale =item.isOnsale;
	if(isOnsale==1){
		html.push("<span>上架</span>&nbsp;&nbsp;");
	}else if(isOnsale==2){
		html.push("<span>下架</span>&nbsp;&nbsp;");
	}else if(isOnsale==3){
		html.push("<span>未上架</span>&nbsp;&nbsp;");
	}
    return html.join('');
};

function commodityExcel(){
	
}

//修改上下架状态
function updateShelvesStatus(id,shelvesStatus,costPrice,wfxCostPrice,wfxPrice){
	
	var titleName="";
	if(shelvesStatus==1){
		if(!checkShelves(costPrice,wfxCostPrice,wfxPrice)){
			return ;
		}
		titleName="确定要上架商品吗？";
	}else{
		titleName="确定要下架商品吗？";
	}
	
	YouGou.UI.Dialog.confirm({
			message : titleName
		},function(result){
			if(result) {
				doShelvesStatusAjax(id,shelvesStatus);
			}
	});
	
}
function checkShelves(costPrice,wfxCostPrice,wfxPrice){
	
	if(wfxCostPrice==null || wfxCostPrice==undefined ||  wfxCostPrice<=0){
		YouGou.UI.Dialog.alert({message:"微分销成本价不能小于0"});
		return false;
	}
	if(wfxPrice==null || wfxPrice==undefined ||  wfxPrice<=0){
		YouGou.UI.Dialog.alert({message:"微分销价不能小于0"});
		
		return false;
	}
	costPrice=costPrice-0;
	wfxCostPrice=wfxCostPrice-0;
	wfxPrice=wfxPrice-0;
	if(wfxCostPrice<costPrice){
		
		YouGou.UI.Dialog.alert({message:"微分销成本价不能小于优购成本价"});
		return false;
	}
	if(wfxPrice<wfxCostPrice){
		YouGou.UI.Dialog.alert({message:"微分销销售价不能小于微分销成本价"});
		return false;
	}
	return true;
}

//修改上下架状态
function batchUpdateShelvesStatus(shelvesStatus){
	

	var checkObjs = $('#wfxCommodity-table input:checkbox:checked');
	var idsStr = "";
	var flag=0;
	if(checkObjs.length > 0){
		checkObjs.each(function(){
			var trObj = $(this).parents('#wfxCommodity-table tr');
			var spans = trObj.find('span');
			var size = spans.length;
			var idSpan = spans.eq(size-1);
			var wfxPrice = spans.eq(size-6).text();
			var wfxCostPrice = spans.eq(size-7).text();
			var costPrice = spans.eq(size-8).text();
			if(!checkShelves(costPrice,wfxCostPrice,wfxPrice)){
				flag=1;
			}
			idsStr = idsStr+ idSpan.text()+",";
		});
	}else{
		YouGou.UI.Dialog.autoCloseTip("请选择商品");
		return;
		
	}
	if(flag==1){
		return ;
	}
	var titleName="";
	if(shelvesStatus==1){
		titleName="确定要上架商品吗？";
	}else{
		titleName="确定要下架商品吗？";
	}
	YouGou.UI.Dialog.confirm({
		message : titleName
	},function(result){
		if(result) {
			doBatchShelvesStatusAjax(idsStr,shelvesStatus);
		}
	});
}

//ajax 上下架
function doBatchShelvesStatusAjax(ids,shelvesStatus){	
	
	YouGou.Ajax.doPost({
   		successMsg: '',
   		url: '/commodity/batchUpdateShelvesStatus.sc',
   		dataType:"json",
   	  	data: { "commodityIds" : ids ,"status":shelvesStatus},
   	  	success : function(data){
   	  		//YouGou.Util.inspect(data);
	    	  	if(data.data.resultCount >0 ){
   	  			//YouGou.UI.Dialog.alert({message:"操作成功"});
   	  			mmGrid2.load();
   	  		}else{
   	  		//	YouGou.UI.Dialog.alert({message:"操作失败"});
   	  		}
     			
   		}
   	});
}


//ajax 上下架
function doShelvesStatusAjax(id,shelvesStatus){	
	YouGou.Ajax.doPost({
   		successMsg: '',
   		url: '/commodity/updateShelvesStatus.sc',
   		dataType:"json",
   		tips:false,
   	  	data: { "id" : id ,"showind":shelvesStatus},
   	  	success : function(data){
   	  		//YouGou.Util.inspect(data);
	    	  	if(data.data.resultCount >0 ){
   	  			YouGou.UI.Dialog.alert({message:"操作成功"});
   	  			mmGrid2.load();
   	  		}else{
   	  			YouGou.UI.Dialog.alert({message:"操作失败"});
   	  		}
     			
   		}
   	});
}
//调价格
function readjustPrices(publicPrice ,salePrice,id,costPrice,wfxCostPrice,wfxPrice,distributableMoney){
	YouGou.UI.Dialog.show({
		title : '调价',
		message: function(dialog) {
			var msg = '<div>市场价：'+publicPrice+' <br/>优购价：'+salePrice+'<br/>真实成本：'+costPrice+'<br/>可分配佣金金额：<input readonly="readonly"   id="distributableMoney"  value=\"'+distributableMoney+'\" /></br>微分销成本价：<input id="wfxCostPrice" value=\"'+wfxCostPrice+'\"   onblur=\"wfxFormula1('+publicPrice+');\" /></br>微分销零售价：<input id="wfxPrice"     value=\"'+wfxPrice+'\"  onblur=\"wfxFormula1('+publicPrice+');\"   /></div>';
			return msg;
		},
		buttons: [
		    {
				label: '确定',
				cssClass: 'btn-primary',
				action: function(dialog) {
					var wfxPrice = $("#wfxPrice").val();
					var wfxCostPrice = $("#wfxCostPrice").val();
					var distributableMoney = $("#distributableMoney").val();
					doWfxPriceAjax(id,wfxPrice,wfxCostPrice,dialog,costPrice,distributableMoney);
					
				}
		    }, 
			{
		    	label: '取消',
		    	action: function(dialog) {
		    		dialog.close();
		    	}
			}
	    ]
	});	
}

function  checkPriceInfo(wfxPrice,costPrice){
	if(wfxPrice=='' ||  wfxPrice==undefined){
		YouGou.UI.Dialog.alert({message:"请输入微分销价!"});
        return false;
	}
	if(costPrice=='' ||  costPrice==undefined){
		YouGou.UI.Dialog.alert({message:"请输入微分销成本价!"});
        return false;
	}
	var reg=/^(-?\d*)\.?\d{1,2}$/;
    if(!reg.test(wfxPrice)){
        YouGou.UI.Dialog.alert({message:"微分销价只能输入数字(最多二位小数)!"});
        return false;
    }
    if(!reg.test(costPrice)){
        YouGou.UI.Dialog.alert({message:"微分销成本价只能输入数字(最多二位小数)!"});
        return false;
    }
    if(costPrice<0 ||  wfxPrice<0){
		YouGou.UI.Dialog.alert({message:"微分销成本价/微分销价不能小于0!"});
        return false;
	}
    return true;
}


//ajax 调价
function doWfxPriceAjax(id,wfxPrice,wfxCostPrice,dialog,costPrice,distributableMoney){	
	if(checkShelves(costPrice,wfxCostPrice,wfxPrice)){
			if(checkPriceInfo(wfxPrice,wfxCostPrice)){
				YouGou.Ajax.doPost({
			   		successMsg: '调价',
			   		url: '/commodity/readjustPrices.sc',
			   		dataType:"json",
			   		tips:false,
			   	  	data: { "id" : id ,"wfxPrice":wfxPrice,"wfxCostPrice":wfxCostPrice,"distributableMoney":distributableMoney},
			   	  	success : function(data){
				    	if(data.data.resultCount >0 ){
				    		dialog.close();
			   	  			YouGou.UI.Dialog.alert({message:"调价成功"});
			   	  			mmGrid2.load();
			   	  		}else{
			   	  			YouGou.UI.Dialog.alert({message:"调价失败"});
			   	  		}
			     			
			   		}
			   	});
			}
	}
}

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
   { title:'分类名称', name:'catName', align:'left'},
   { title:'款色编码', name:'supplierCode', align:'left'},
   { title:'商品款号', name:'styleNo', align:'left'},
   { title:'款色ID', name:'no', align:'left'},
   { title:'一级佣金', name:'commissionLevel1',width:60, align:'left'},
   { title:'二级佣金', name:'commissionLevel2',width:60, align:'left'},
   { title:'三级佣金', name:'commissionLevel3',width:60, align:'left'},
   { title:'佣金合计', name:'totalCommission',width:60, align:'left'},
   { title:'真实成本', name:'costPrice',width:60, align:'left'},
   { title:'可分配佣金金额', name:'distributableMoney',width:100, align:'left'},
   { title:'微分销成本价', name:'wfxCostPrice',width:60, align:'left'},
   { title:'微分销零售价', name:'wfxPrice',width:60, align:'left'},
   { title:'可售库存', name:'stock',width:60, align:'left'},
   { title:'商品状态', name:'',width:60, align:'center', renderer: shelves},
   { title:'ID', name:'id', hidden: true}
   
];
//分类联动及选择功能
function wfxCatChange(level,obj){
	var levelone = $('#wfxLevelOne');
	var leveltwo = $('#wfxLevelTwo');
	var levelthree = $('#wfxLevelThree');
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
//分页器
var mmPaginator = $('#wfxCommodity-pager').mmPaginator({});
// 搜索表单属性
var wfxCommodityForm = new MMSearchFormParams("wfxCommodityForm");




// 表格	
var mmGrid2 = $('#wfxCommodity-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/commodity/queryWfxCommodityData.sc',
	fullWidthRows: true,
	autoLoad: true,
	 multiSelect: true,
	 checkCol: true,
	plugins: [mmPaginator,wfxCommodityForm]
});

// 表格事件
mmGrid2.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    //编辑
    if(action == "edit"){
    	YouGou.UI.Dialog.alert({message:"编辑"});
    }else if(action == "delete"){// 删除
   		YouGou.UI.Dialog.confirm({
   			message : "确定要删除吗？"
   		},function(result){
   			if(result) {
                removeCommodity(item.id);
            }
   		});
    }else if(action == "select"){//查看
    	YouGou.UI.Dialog.alert({message:"查看"});
    }
    e.stopPropagation();  //阻止事件冒泡
});
function importExcel(){
	 YouGou.UI.progressLoading();
	 	$.ajax({
	       type: "post",
	       url: "/commodity/importExcelcommodityPrice.sc",
	       async: false,
	       success : function(data) {
	    	    YouGou.UI.progressStop();
				if (data) {
	 				YouGou.UI.Dialog.show({
	 					title: "导入区域",
	 					message : data.replace(/(\n)+|(\r\n)+/g, ""),
	 					buttons: [{
	 	    				   label: '关闭',
	 	    				   action: function(dialog) {
	 	    					   mmGrid2.load();
	 	    					   dialog.close();
	 	    					   
	 	    				   }
	 	    			   }]
	 				});
				}
	 	    }
	   });
}

function removeCommodity(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/commodity/removeCommodity.sc',
	  	data: { "id" : id },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}


function commodityExcel(){
	var checkObjs = $('#wfxCommodity-table input:checkbox:checked');
	var idsStr = "";
	if(checkObjs.length > 0){
		checkObjs.each(function(){
			var trObj = $(this).parents('#wfxCommodity-table tr');
			var idSpan = trObj.find('span').last();
			idsStr = idsStr+ idSpan.text()+",";
		});
	}else{
		YouGou.UI.Dialog.autoCloseTip("请选择商品");
		return;
		
	}
	YouGou.UI.Dialog.confirm({
		message : '导出商品信息'
	},function(result){
		if(result) {
			excel(idsStr);
		}
	});
	
	/*YouGou.Ajax.doPost({
		url: '/commodity/exportExcel.sc',
	  	data: { "ids" : idsStr }
	});*/
}
function  excel(idsStr){
	window.open("/commodity/exportExcel.sc?ids="+idsStr);
}
function doWfxQuery(){
	mmGrid2.load();
}
