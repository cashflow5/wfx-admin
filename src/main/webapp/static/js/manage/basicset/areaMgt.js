var loadTable = function(parentId){
	if(parentId == 0){
		$("#goodsCategoryList").hide();
		return;
	}
	$("#goodsCategoryList").show();
	YouGou.Ajax.doPost({
		url: '/basicSet/getAreaMgtById.sc',
	  	data: { "id" : parentId  },
	  	dataType:'json',
	  	tips:false,
	  	success : function(data){
	  		data = data.data;
	  		$("#id").val(data.id);
			$("#name").val(data.name);
			$("#sort").val(data.sort);
			$("#code").val(data.code);
			$("#post").val(data.post);
			$("#no").val(data.no);
			if(data.isUsable == 1){
				document.getElementById("isUsable1").checked = 'checked';
			}else{
				document.getElementById("isUsable2").checked = 'checked';
			}
		}
	});
}

//更新状态时使用
function doSaveArea(){
	var id = $.trim($("#id").val());
	var name = $.trim($("#name").val());
	var sort = $.trim($("#sort").val());
	var code = $.trim($("#code").val());
	var post = $.trim($("#post").val());
	var no = $.trim($("#no").val());
	var isUsable = $("input:radio[name=isUsable]:checked").val();
	if(!name){
		YouGou.UI.Dialog.alert({message:"区域名称不能为空!"});
		return;
	}
	var r = /^\+?[1-9][0-9]*$/;
	if(!r.test(sort)){
		YouGou.UI.Dialog.alert({message:"排序号只能为正整数！"});
		return;
	}
	YouGou.Ajax.doPost({
		successMsg: '更新成功！',
		url: '/basicSet/saveAreaMgt.sc',
	  	data: { "id" : id ,"name" : name,"sort":sort,"code":code,"post":post,"no":no,"isUsable":isUsable },
	  	success : function(data){
	  		var treeObj = $.fn.zTree.getZTreeObj("SettingNodeList");
	  		var nodes = treeObj.getNodesByParam("id", no+','+id, null);
	  		if(nodes[0]){
	  			nodes[0].name = name;
	  			treeObj.refresh();
	  		}
		}
	});
}
