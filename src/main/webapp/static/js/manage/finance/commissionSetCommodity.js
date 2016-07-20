function doQuery(){
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
	var cols = [
		{ title:'缩略图', name:'picBig',width:60, lockWidth:true, align:'left',renderer:defaultPicImg},
		{ title:'款色编码', name:'supplierCode',width:150, lockWidth:true,align:'center'},
		{ title:'商品名称', name:'commodityName', align:'center'},
	    { title:'ID', name:'id', hidden: true}
	];

	// 搜索表单属性
	var mmFormParams = new MMSearchFormParams("commoditySaveForm");
	//表格	
	var mmGrid = $('#commodityList-grid-table').mmGrid({
		height: 'auto',
		cols: cols,
		url: '/finance/queryCommodityList.sc',
		fullWidthRows: true,
		autoLoad: true,
		checkCol: true,
		multiSelect: true, //多选,默认false单选
		plugins: [mmFormParams]
	});
	var supplierCode=$("#supplierCode").val();	
	if(!YouGou.Util.isEmpty(supplierCode)){
		if(supplierCode.split(",").length>200){
			YouGou.UI.Dialog.alert({message:"款色编码填写不能超过200个!"});
			return;
		}
	}
	mmGrid.load();
}

function saveCommission(){	
	var id = $("#id").val();
	if(YouGou.Util.isEmpty(id)){
		var checkObjs = $('#commodityList-grid-table input:checkbox:checked');
		var commodityId = "";
		var supplierCode="";
		var commodityName="";
		if(checkObjs.length > 0){
			checkObjs.each(function(){
				var trObj = $(this).parents('#commodityList-grid-table tr');
				var spans = trObj.find('span');
				var size = spans.length;
				var id = spans.last().text();
				var code = spans.eq(2).text();
				var name = spans.eq(3).text();
				commodityId = commodityId+id+",";
				supplierCode = supplierCode+code+",";
				commodityName = commodityName+name+",";
			});
		}else{
			$("#commodityId_stip").html("必须选择一个商品！");
			 return false;
		}
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
		$("#commissionLevel3Percent_stip").html("佣金比例为不超过两位的正整数或者保留两位的小数,且不为0");
		return false;
	}
	
	YouGou.Ajax.doPost({
		successMsg: "操作成功",
		url: '/finance/saveCommission.sc',
	    data: {"commodityId":commodityId,"id":id,"supplierCode":supplierCode,"commodityName":commodityName,
	    	"commissionLevel1Percent":commissionLevel1Percent,
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
	var re = /^[1-9]{0,1}\d{0,1}$|^[1-9]{0,1}\d{0,1}\.\d{1,2}$/;
	var commissionLevel1Percent = $("#commissionLevel1Percent").val();
	if(YouGou.Util.isEmpty(commissionLevel1Percent)){
		$("#commissionLevel1Percent_stip").html("一级佣金比例不能为空！");
		return;
	}else{
		$("#commissionLevel1Percent_stip").html("");
		if(commissionLevel1Percent<0||!re.test(commissionLevel1Percent)||parseFloat(commissionLevel1Percent)==0){
			$("#commissionLevel1Percent_stip").html("佣金比例为不超过两位的正整数或者保留两位的小数,且不为0");
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
			$("#commissionLevel2Percent_stip").html("佣金比例为不超过两位的正整数或者保留两位的小数,且不为0");
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
			$("#commissionLevel3Percent_stip").html("佣金比例为不超过两位的正整数或者保留两位的小数,且不为0");
			return;
		}
	}	
}



