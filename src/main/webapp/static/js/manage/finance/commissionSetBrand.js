/*************
	CommissionPercentSetUpdate
**************/
var mmFormParams,mmGrid,mmPaginator;
function getBrandList(){
 	$.ajax({
 	       type: "post",
 	       url: '/finance/queryBrandList.sc',
 	       async: true,
 	       success : function(data) {
 		   if (data) {
 				YouGou.UI.Dialog.show({
     			   title: "查询品牌",
     			   cssClass: 'detail-dialog',
     			   message : data.replace(/(\n)+|(\r\n)+/g, ""),
     			   buttons: [{
     				   label: '取消',
     				   action: function(dialog) {
     					   dialog.close();
     				   }
     			   },{
     				   label: '确定',
     				   cssClass: 'btn-yougou',
     				   action: function(dialog) {
     					  var brandNo=$('#brandList-grid-table input:checkbox:checked').parents('#brandList-grid-table tr').find('span').last().text();
     					  if(brandNo){
     						 var brandName=$('#brandList-grid-table input:checkbox:checked').parents('#brandList-grid-table tr').find('span')[1].innerHTML;
     						 $("#saveBrandNo").val(brandNo);
        					 $("#saveBrandName").val(brandName);
        					 getBaseCatId("","1",brandNo);
        					 dialog.close();
     					  }else{
     						 YouGou.UI.Dialog.alert({message:"必须选择一个品牌！"});
     					  }
     				   }
     			   }],
     			  callback: function(){
     				  
     			  }
     		   });
 			}
 		  }
 	   }); 
}

function loadBrandPage(){
	// 列集合
	 var cols = [
	 	{ title:'中文名称', name:'brandName', align:'center'},
	 	{ title:'英文名称', name:'englishName', align:'center'},
	    { title:'品牌编码', name:'brandNo', hidden: true}
	 ];
	 // 搜索表单属性
	 mmFormParams = new MMSearchFormParams("searchForm");
	//分页器
	 mmPaginator = $('#grid-pager').mmPaginator({});
	 // 表格	
	 mmGrid = $('#brandList-grid-table').mmGrid({
	 	height: 'auto',
	 	cols: cols,
	 	url: '/finance/queryBrandData.sc',
	 	fullWidthRows: true,
	 	autoLoad: true,
	 	checkCol: true,
	 	multiSelect: false, //多选,默认false单选
	 	plugins: [mmPaginator,mmFormParams]
	 });
}
function doQuery(){
	mmGrid.load();
}


function saveCommission(){	
	var saveBrandNo = $("#saveBrandNo").val();
	if(YouGou.Util.isEmpty(saveBrandNo)){
		$("#brandNo_stip").html("必须选择一个品牌！");
		return;
	}else{
		$("#brandNo_stip").html("");
	}
	
	var baseCatId = $("#baseCatId1").val();
	if(!YouGou.Util.isEmpty(baseCatId)){
		var obj=document.getElementById('baseCatId1');  
		var index=obj.selectedIndex; //序号，取当前选中选项的序号  
		var catName=obj.options[index].text; 
		var baseCatLevel="1";
	}
	//有选择二级分类的时候：保存二级分类Id
	if(!YouGou.Util.isEmpty($("#baseCatId2").val())){
		baseCatId=$("#baseCatId2").val();
		obj=document.getElementById('baseCatId2');  
		index=obj.selectedIndex; //序号，取当前选中选项的序号  
		catName=obj.options[index].text; 
		baseCatLevel="2";
	}
	var commissionLevel1Percent = $("#commissionLevel1Percent").val();
	if(YouGou.Util.isEmpty(commissionLevel1Percent)){
		$("#commissionLevel1Percent_stip").html("一级佣金比例不能为空！");
		content.focus();
		return false;
	}else{
		if(parseFloat(commissionLevel1Percent)==0){
			$("#commissionLevel1Percent_stip").html("一级佣金比例不能为0");
			content.focus();
			return false;
		}
	}
	var commissionLevel2Percent = $("#commissionLevel2Percent").val();
	if(YouGou.Util.isEmpty(commissionLevel2Percent)){
		$("#commissionLevel2Percent_stip").html("二级级佣金比例不能为空！");
		content.focus();
		return false;
	}else{
		if(parseFloat(commissionLevel2Percent)==0){
			$("#commissionLevel2Percent_stip").html("二级佣金比例不能为0");
			content.focus();
			return false;
		}
	}
	var commissionLevel3Percent = $("#commissionLevel3Percent").val();
	if(YouGou.Util.isEmpty(commissionLevel3Percent)){
		$("#commissionLevel3Percent_stip").html("三级级佣金比例不能为空！");
		content.focus();
		return false;
	}else{
		if(parseFloat(commissionLevel3Percent)==0){
			$("#commissionLevel3Percent_stip").html("三级佣金比例不能为0");
			content.focus();
			return false;
		}
	}
	var re = /^[1-9]{0,1}\d{0,1}$|^[1-9]{0,1}\d{0,1}\.\d{1,2}$/;
	if(commissionLevel1Percent<0||commissionLevel2Percent<0||commissionLevel3Percent<0||
	  !re.test(commissionLevel1Percent)||!re.test(commissionLevel2Percent)||!re.test(commissionLevel3Percent)){
		$("#commissionLevel3Percent_stip").html("佣金比例为不超过两位的正整数或者保留两位的小数");
		return false;
	}
	YouGou.Ajax.doPost({
		url: '/finance/saveCommission.sc',
        data: {"baseCatId":baseCatId,"id":$("#id").val(),"brandNo":saveBrandNo,"brandName":$("#saveBrandName").val(),"catName":catName,
    	   "commissionLevel1Percent":commissionLevel1Percent,"baseCatLevel":baseCatLevel,
    	   "commissionLevel2Percent":commissionLevel2Percent,"commissionLevel3Percent":commissionLevel3Percent},
	  	success : function(data) {
	  		var msg;
	  		if(data.state=='success'){
	  			if(!YouGou.Util.isEmpty(data.msg)&&'success'!=data.msg){
	  				msg=data.msg;
	  			}else{
	  				msg="商品佣金比例设置成功！";
	  			}
	  		}else{
	  			msg="商品佣金比例设置失败！";
	  		}
	  		YouGou.UI.Dialog.confirm({
  	   			message : msg
  	   		},function(result){
  	   			if(result) {
  	   				history.go(-1);
  	            }
  	   		});
	  	}
	});
}

function validate(){
	var saveBrandNo = $("#saveBrandNo").val();
	if(YouGou.Util.isEmpty(saveBrandNo)){
		$("#brandNo_stip").html("必须选择一个品牌！");
		return;
	}else{
		$("#brandNo_stip").html("");
	}
	
	var re = /^[1-9]{0,1}\d{0,1}$|^[1-9]{0,1}\d{0,1}\.\d{1,2}$/;
	var commissionLevel1Percent = $("#commissionLevel1Percent").val();
	if(YouGou.Util.isEmpty(commissionLevel1Percent)){
		$("#commissionLevel1Percent_stip").html("一级佣金比例不能为空！");
		return;
	}else{
		$("#commissionLevel1Percent_stip").html("");
		if(commissionLevel1Percent<0||!re.test(commissionLevel1Percent)||parseFloat(commissionLevel1Percent)==0){
			$("#commissionLevel1Percent_stip").html("佣金比例为不超过两位的正整数或者保留两位的小数");
			return;
		}
	}
	
	var commissionLevel2Percent = $("#commissionLevel2Percent").val();
	if(YouGou.Util.isEmpty(commissionLevel2Percent)){
		$("#commissionLevel2Percent_stip").html("二级佣金比例不能为空！");
		return;
	}else{
		$("#commissionLevel2Percent_stip").html("");
		if(!re.test(commissionLevel2Percent)||!re.test(commissionLevel2Percent)||parseFloat(commissionLevel2Percent)==0){
			$("#commissionLevel2Percent_stip").html("佣金比例为不超过两位的正整数或者保留两位的小数");
			return;
		}
	}	
	
	var commissionLevel3Percent = $("#commissionLevel3Percent").val();
	if(YouGou.Util.isEmpty(commissionLevel3Percent)){
		$("#commissionLevel3Percent_stip").html("三级佣金比例不能为空！");
		return;
	}else{
		$("#commissionLevel3Percent_stip").html("");
		if(!re.test(commissionLevel3Percent)||!re.test(commissionLevel3Percent)||parseFloat(commissionLevel3Percent)==0){
			$("#commissionLevel3Percent_stip").html("佣金比例为不超过两位的正整数或者保留两位的小数");
			return;
		}
	}	
}

function getBaseCatId(baseCatId,level,brandNo){
	validate();
	if(!brandNo){
		brandNo=$('#saveBrandNo').val();
	}
 	$.ajax({
 	       type: "post",
 	       url: '/finance/getBaseCatById.sc',
 		   data: { "baseCatId" : baseCatId,"level":level,"brandNo":brandNo},
 	       async: true,
 	       success : function(data) {
    		var jsonobj=eval('('+data+')');  
 			 var objects=eval(jsonobj.list);
 			 if(level=='2'){
 				$("#baseCatId2").empty();
 		  		 var temp=$("#baseCatId2").append('<option value="">请选择</option>');
 		  		 $.each(objects, function(i, item){  
 		  			temp.append('<option value="'+item.id+'">'+item.catName+'</option>');
 		  		 });  
 			 }else{
 				$("#baseCatId1").empty();
		  		 var temp=$("#baseCatId1").append('<option value="">请选择</option>');
		  		 $.each(objects, function(i, item){  
		  			temp.append('<option value="'+item.id+'">'+item.catName+'</option>');
		  		 });  
 			 }
	  	}
	});
}


