function areaInit(obj_1,val_1,obj_2,val_2,obj_3,val_3){
	//定义默认数据
	var ar = ["请选择省份","请选择城市","请选择区县"];
	//初始化
	$("<option value='-1'>"+ar[0]+"</option>").appendTo($("#"+obj_1));
	$("<option value='-1'>"+ar[1]+"</option>").appendTo($("#"+obj_2));
	$("<option value='-1'>"+ar[2]+"</option>").appendTo($("#"+obj_3));
	
	var url = "/area/queryChildById.sc";
	var data={
		"no":'root'
	};
	
	ajaxRequest(url,data,function(result){
		if(!result) return ;
		var node = result;
		
		for (i=0;i<node.length;i++){
			var area = node[i];
			if (area.no == val_1 || area.no ==('root-'+val_1)){
				$("<option selected value='"+area.no+"'>"+area.name+"</option>").appendTo($("#"+obj_1));
			}else{
				$("<option value='"+area.no+"'>"+area.name+"</option>").appendTo($("#"+obj_1));
			}
		}
		
		selectValue = $("#"+obj_1).val();
		if(selectValue != -1){
			ajaxRequest(url,{"no":selectValue},function(result){
				if(!result) return ;
				var node = result;
				
				//清空c和h
				$("#"+obj_2).empty();
				//重新给c填充内容
				$("<option value='-1'>"+ar[1]+"</option>").appendTo($("#"+obj_2));
				for (i=0;i<node.length;i++){
					var area = node[i];
					if (area.no == val_2 || area.no ==('root-'+val_2)){
						$("<option selected value='"+area.no+"'>"+area.name+"</option>").appendTo($("#"+obj_2));
					}else{
						$("<option value='"+area.no+"'>"+area.name+"</option>").appendTo($("#"+obj_2));
					}
				}
				selectValue = $("#"+obj_2).val();
				if(selectValue != -1){
					ajaxRequest(url,{"no":selectValue},function(result){
						if(!result) return ;
						var node = result;
						
						//清空c和h
						$("#"+obj_3).empty();
						//重新给c填充内容
						$("<option value='-1'>"+ar[2]+"</option>").appendTo($("#"+obj_3));
							
						for (i=0;i<node.length;i++){
							var area = node[i];
							if (area.no == val_3 || area.no ==('root-'+val_3)){
								$("<option selected value='"+area.no+"'>"+area.name+"</option>").appendTo($("#"+obj_3));
							}else{
								$("<option value='"+area.no+"'>"+area.name+"</option>").appendTo($("#"+obj_3));
							}
						}
						try{
							if(''==oldConsigneeInfo ||undefined==oldConsigneeInfo) {
								oldConsigneeInfo = $("#consigneeInfo").serialize();
							}
							
						}catch(e) {
						}
					});
				}
			});
		}
	});
	
	//响应obj_1的change事件	
	$("#"+obj_1).change(function(){
		var selectValue = $("#"+obj_1).val();
		if(selectValue == -1){
			$("#"+obj_2).empty();
			$("<option value='-1'>"+ar[1]+"</option>").appendTo($("#"+obj_2));
			$("#"+obj_3).empty();
			$("<option value='-1'>"+ar[2]+"</option>").appendTo($("#"+obj_3));
			return false;
		}
				
		ajaxRequest(url,{"no":selectValue},function(result){
			if(!result) return ;
			var node = result;
			
			//清空c和h
			$("#"+obj_2).empty();
			//重新给c填充内容
			$("<option value='-1'>"+ar[1]+"</option>").appendTo($("#"+obj_2));
			for (i=0;i<node.length;i++){
				var area = node[i];
				$("<option value='"+area.no+"'>"+area.name+"</option>").appendTo($("#"+obj_2));
			}
			//清空h
			$("#"+obj_3).empty();
			$("<option value='-1'>"+ar[2]+"</option>").appendTo($("#"+obj_3));
		});
	});
	
	//响应obj_2的change事件	
	$("#"+obj_2).change(function(){
		var selectValue = $("#"+obj_2).val();
		if(selectValue == -1){
			$("#"+obj_3).empty();
			$("<option value='-1'>"+ar[2]+"</option>").appendTo($("#"+obj_3));
			return false;
		}
		
		ajaxRequest(url,{"no":selectValue},function(result){
			if(!result) return ;
			var node = result;
			
			//清空c和h
			$("#"+obj_3).empty();
			//重新给c填充内容
			$("<option value='-1'>"+ar[2]+"</option>").appendTo($("#"+obj_3));
			for (i=0;i<node.length;i++){
				var area = node[i];
				$("<option value='"+area.no+"'>"+area.name+"</option>").appendTo($("#"+obj_3));
			}
		});
	});
	
}

//发达ajax请求
function ajaxRequest(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  dataType: 'json',
		  success: callback
	});
}