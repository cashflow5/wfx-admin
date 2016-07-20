/*************
	CommodityStyle
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	var publicPrice =item.publicPrice;
	var salePrice =item.salePrice;
	var costPrice=item.costPrice;
	var wfxCostPrice=item.wfxCostPrice;
	if(wfxCostPrice==null || wfxCostPrice=='undefined'){
		wfxCostPrice=0;
	}
	
	var  wfxPrice=item.wfxPrice;
	if(wfxPrice==null || wfxPrice=='undefined'){
		wfxPrice=0;
	}
	var id =item.id;
	html.push("<a href=\"javascript:void(0);\"       onclick=\"addWfx(\'"+publicPrice+"\',\'"+salePrice+"\',\'"+id +"\',\'"+costPrice +"\',\'"+wfxCostPrice +"\',\'"+wfxPrice +"\');\">添加至微分销</a>&nbsp;&nbsp;");
    return html.join('');
};
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
function wfxFormula(costPrice){
	var wfxPrice = $("#wfxPrice").val();
	var postFee = $("#postFee").val();
	var taxRate = $("#taxRate").val();
	var vatRate = $("#vatRate").val();

	var  distributableMoney=compute1stFormula(wfxPrice,costPrice,vatRate,taxRate,postFee);
	if( distributableMoney ){
		   $("#distributableMoney").val(distributableMoney);
	}
}
//把商品添加至分销商
function addWfx(publicPrice ,salePrice,id,costPrice,wfxCostPrice,wfxPrice){
	YouGou.UI.Dialog.show({
		title : '添加至微分销',
		message: function(dialog) {
			var msg = '<div>市场价：'+publicPrice+' <br/>优购价：'+salePrice+'</br>微分销成本价：<input id="wfxCostPrice" value=\"'+wfxCostPrice+'\" /></br>微分销零售价：<input id="wfxPrice"     value=\"'+wfxPrice+'\"  onmouseout=\"wfxFormula('+publicPrice+');\"   /></div>';
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
					dialog.close();
					ommodityAddToWfx(id,wfxPrice,wfxCostPrice,costPrice,distributableMoney);
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
//同步商品信息
function synchronizationCommodity(id){
	
	$.ajax({
		url:'/commodity/synchronizationCommodity.sc',
		type:'post',
		data:{"commodityIds":id},
		dataType:'html',
		success:function(data){
		
		}
	});
	
}
//把商品添加至分销商
function ommodityAddToWfx(id,wfxPrice,wfxCostPrice,costPrice,distributableMoney){
	
	
			if(checkPriceInfo(wfxPrice,wfxCostPrice)){
			YouGou.Ajax.doPost({
		   		successMsg: '',
		   		url: '/commodity/ommodityAddToWfx.sc',
		   		dataType:"json",
		   		
		   	  	data: { "id":id , "wfxCostPrice":wfxCostPrice, "wfxPrice":wfxPrice,"distributableMoney":distributableMoney},
		   	  	success : function(data){
		   	  		//YouGou.Util.inspect(data);
			    	  	if(data.data.resultCount >0 ){
			    	  		synchronizationCommodity(id);
		   	  			//YouGou.UI.Dialog.alert({message:"商品添加至分销商成功"});
		   	  			mmGrid1.load();
		   	  		}else{
		   	  			//YouGou.UI.Dialog.alert({message:"商品添加至分销商失败"});
		   	  		}
		     			
		   		}
		   	});
			}
			
	
}


var defaultPicImg = function(val){
	var html = [];
	if(val!=''){
		var img='<img width="40" height="39" alt="" src="'+val+'"/>';
		html.push(img);
	}else{
		html.push('<img width="40" height="39" alt="" src="${BasePath}/yougou/images/nopic.jpg"/>');
	}
	
	return html.join('');
}
// 列集合



//列集合
var cols = [
   { title:'商品图片', name:'defaultPic',width:60, align:'left',renderer:defaultPicImg},
   { title:'商品名称', name:'commodityName',width:200, align:'left'},
   { title:'分类名称', name:'catName', align:'left'},
   { title:'品牌名称', name:'brandName', align:'left'},
   { title:'款色编码', name:'supplierCode', align:'left'},
   { title:'款色ID', name:'no', align:'left'},
   { title:'商品款号', name:'styleNo', align:'left'},
   { title:'优购价',width:60, name:'salePrice', align:'left'},
  
   { title:'分销成本价',width:60, name:'wfxCostPrice', align:'left'},
   { title:'市场价',width:60, name:'publicPrice', align:'center'},
   { title:'可售库存',width:60, name:'stock', align:'left'},
   { title:'操作',width:60, name:'' ,width:100, align:'center',lockWidth:true, renderer: actionFixed},
   { title:'ID', name:'id', hidden: true}
];

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



var actionFixed = function(val,item,rowIndex){
	var html = [];
	
	html.push('<a href="javascript:void(0);" action="delete">移至微分销</a>&nbsp;&nbsp;');
    return html.join('');
};
//分页器
var mmPaginator = $('#commodity-pager').mmPaginator({});
// 搜索表单属性
var mmCommodityForm = new MMSearchFormParams("commodityForm");

// 表格	
var mmGrid1 = $('#commodity-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/commodity/queryCommodityData.sc',
	fullWidthRows: true,
	autoLoad: true,
	multiSelect: true,
	checkCol: true,
	plugins: [mmPaginator,mmCommodityForm]
});

// 表格事件
mmGrid1.on('cellSelected', function(e, item, rowIndex, colIndex){
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

function removeCommodity(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/commodity/removeCommodity.sc',
	  	data: { "id" : id },
	  	success : function(data){
	  		
		}
	});
}
//批量添加至分销库
function batchCommodityAddToWfx(){
	
	var checkObjs = $('#commodity-table input:checkbox:checked');
	var idsStr = "";
	if(checkObjs.length > 0){
		checkObjs.each(function(){
			var trObj = $(this).parents('#commodity-table tr');
			var idSpan = trObj.find('span').last();
			idsStr = idsStr+ idSpan.text()+",";
		});
	}else{
		YouGou.UI.Dialog.autoCloseTip("请选择要添加至微分销信息");
		return ;
	}
	YouGou.Ajax.doPost({
		successMsg: '添加成功！',
		url: '/commodity/batchCommodityAddToWfx.sc',
	  	data: { "commodityIds" : idsStr },
	  	success : function(data){
	  		if(data.data.resultCount >0 ){
	  			synchronizationCommodity(idsStr);
   	  			//YouGou.UI.Dialog.alert({message:"商品添加至分销商成功"});
   	  			mmGrid1.load();
   	  		}else{
   	  			//YouGou.UI.Dialog.alert({message:"商品添加至分销商失败"});
   	  		}
		}
	});
}
function doQuery(){
	mmGrid1.load();
}