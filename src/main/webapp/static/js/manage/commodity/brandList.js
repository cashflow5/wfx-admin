/*************
	CommodityBrand
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	if(item.status == 1){
		html.push('<a href="javascript:void(0);" action="disable">停用</a>&nbsp;&nbsp;');
	}else{
		html.push('<a href="javascript:void(0);" action="activate">启用</a>&nbsp;&nbsp;');
	}
	html.push('<a href="javascript:void(0);" action="edit">编辑</a>&nbsp;&nbsp;');
//	html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
    return html.join('');
};

var statusFixed = function(val){
	 if(val == '1') {
		 return "启用";
	 }else { 
		return "停用";
	 }
}

var imageFixed = function(val){
	return "<img width='100' height='40' src='"+val+"'/>"
}

// 列集合
var cols = [
    { title:'品牌图片', width:110, lockWidth:true, name:'mobilePic', align:'center', renderer: imageFixed},
    { title:'品牌编码', name:'brandNo', align:'left'},
	{ title:'中文名称', name:'brandName', align:'left'},
	{ title:'英文名称', name:'englishName', align:'left'},
	{ title:'商品数量', name:'commodityCount', align:'center'},
	{ title:'状态', name:'status', align:'center', renderer: statusFixed },
	{ title:'操作', name:'' ,width:200, align:'center',lockWidth:true, renderer: actionFixed},
    { title:'ID', name:'id', hidden: true}
];

//分页器
var mmPaginator = $('#grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchForm");

// 表格	
var mmGrid = $('#grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/commodity/queryBrandData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    //编辑
    if(action == "edit"){
    	YouGou.UI.progressLoading();
		$.ajax({
			type:'post',
			url:'/commodity/brandEdit.sc?id='+ item.id,
			async: false,
			success:function(data){
				YouGou.UI.progressStop();
				if(data){
					YouGou.UI.Dialog.show({
						cssClass:'w800',
						title:"品牌编辑",
						message:data.replace(/(\n)+|(\r\n)+/g,""),
						buttons:[{
							label:'确定',
							cssClass: 'btn-yougou',
							action:function(dialog){
								saveBrand(dialog);
							}
						},{
							label:'关闭',
							action:function(dialog){
								dialog.close();
							}
						}]
					});
				}
			}
		})
    }else if(action == "delete"){// 删除
   		YouGou.UI.Dialog.confirm({
   			message : "确定要删除吗？"
   		},function(result){
   			if(result) {
                removeBrand(item.id);
            }
   		});
    }else if(action == "activate"){//启用
    	YouGou.UI.Dialog.confirm({
   			message : "确定要启用品牌吗？"
   		},function(result){
   			if(result) {
   				updateBrandStatus(item.id,item.brandNO, 1);
            }
   		});
    }else if(action == "disable"){//停用
    	if(item.commodityCount > 0){
    		YouGou.UI.Dialog.alert({message:item.brandName+" 品牌有商品正在使用，不能停用!"});
    		return;
    	}
    	YouGou.UI.Dialog.confirm({
   			message : "确定要停用该品牌吗？"
   		},function(result){
   			if(result) {
   				updateBrandStatus(item.id,item.brandNo, 2);
            }
   		});
    }
    e.stopPropagation();  //阻止事件冒泡
});

function saveBrand(dialog){
	//校验
	var brandName = $.trim($('#brandNameStr').val());
	var brandNo = $.trim($('#brandNoStr').val());
	var englishName = $.trim($('#englishNameStr').val());
	var status = $('input[name="statusStr"]:checked').val();
	if(!brandName){
		YouGou.UI.Dialog.alert({message:"品牌名称不能为空！"});
		return;
	}
	if(!englishName){
		YouGou.UI.Dialog.alert({message:"品牌英文名称不能为空！"});
		return;
	}
	if(!status){
		YouGou.UI.Dialog.alert({message:"请选择状态！"});
		return;
	}
	//获取树节点
	var catIdStrs = [];
	var treeObj = $.fn.zTree.getZTreeObj("elementList");
	if (treeObj != null) {
		var nodes = treeObj.getCheckedNodes();
		for (var i=0, l=nodes.length; i < l; i++) {
			var node = nodes[i];
			if (node.level == 3) { //取第三级分类
				catIdStrs[catIdStrs.length] = node.id;
			}				
		}
	}
	if(catIdStrs.length < 1){
		YouGou.UI.Dialog.alert({message:"请选择分类！"});
		return;
	}
	var brandId = $('#brandId').val();
	var catIds = catIdStrs.join('_');
	
	YouGou.Ajax.doPost({
		successMsg: '修改成功！',
		url: '/commodity/saveBrand.sc',
	  	data: { 
	  		"id" : brandId, 
	  		"catIds" : catIds,
	  		"brandName": brandName,
	  		"brandNo":brandNo,
	  		"englishName": englishName,
	  		"status":status
	  	},
	  	success : function(data){
	  		dialog.close();
  			mmGrid.load();
		}
	});
}

//更新品牌状态
function updateBrandStatus(id,brandNo, status){
	YouGou.Ajax.doPost({
		successMsg: status == 1 ? '启用成功！' : '停用成功！',
		url: '/commodity/saveBrand.sc',
	  	data: { "id" : id, "status" : status,"brandNo":brandNo },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}

function removeBrand(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/commodity/removeBrand.sc',
	  	data: { "id" : id },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}

function doQuery(){
	mmGrid.load();
}

//同步品牌
function synBrand(){
	YouGou.UI.Dialog.confirm({
		message : "确定要同步吗？"
	},function(result){
		if(result) {
			mmGrid.load();
		}
	});
}