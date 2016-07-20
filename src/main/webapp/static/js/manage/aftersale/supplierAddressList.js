/*************
	SupplierAddress
**************/
// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="edit">编辑</a>&nbsp;&nbsp;');
	html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
    return html.join('');
};

// 列集合
var cols = [
	{ title:'地区编号', name:'outsideNo', align:'center'},
	{ title:'联系人', name:'contact', align:'left'},
	{ title:'所在地区', name:'detailAddress', width:300,align:'left'},
	{ title:'联系电话', name:'phone', align:'left'},
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
	url: '/afterSale/querySupplierAddressData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    //编辑
    if(action == "edit"){
    	toAddOrUpdateSupplierAddress(item.id);
    }else if(action == "delete"){// 删除
   		YouGou.UI.Dialog.confirm({
   			message : "确定要删除吗？"
   		},function(result){
   			if(result) {
                removeSupplierAddress(item.id);
            }
   		});
    }else if(action == "select"){//查看
    	YouGou.UI.Dialog.alert({message:"查看"});
    }
   e.stopSupplierAddressagation();  //阻止事件冒泡
});
//删除
function removeSupplierAddress(id){
	YouGou.Ajax.doPost({
		successMsg: '删除成功！',
		url: '/afterSale/removeSupplierAddress.sc',
	  	data: { "id" : id },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}
//更新状态时使用
function updateSupplierAddress(id,status){
	YouGou.Ajax.doPost({
		successMsg: '更新成功！',
		url: '/afterSale/saveSupplierAddress.sc',
	  	data: { "id" : id ,"status" : status },
	  	success : function(data){
  			mmGrid.load();
		}
	});
}
//查询
function doQuery(){
	mmGrid.load();
}

function toAddOrUpdateSupplierAddress(id){
	var title="编辑";
	if(YouGou.Util.isEmpty(id)){
		title="新增";
	}
	YouGou.UI.progressLoading();
 	$.ajax({
 	       type: "post",
 	       url: '/afterSale/editSupplierAddress.sc',
 	       data: {"id":id},
 	       success : function(data) {
 	    	YouGou.UI.progressStop();
 			if (data) {
 				YouGou.UI.Dialog.show({
     			   title: title,
     			   cssClass: 'detail-dialog',
     			   message : data.replace(/(\n)+|(\r\n)+/g, ""),
     			   buttons: [{
     				   label: '关闭',
     				   action: function(dialog) {
     					   dialog.close();
     				   }
     			   },{
     				   label: '提交',
     				   cssClass: 'btn-yougou',
     				   action: function(dialog) {
     					  saveOrUpdateSupplierAddress(dialog);
     				   }
     			   }]
     		   });
 			}
 		  }
 	   }); 
}

function saveOrUpdateSupplierAddress(dialog){	
	var id = $("#id").val();
	var supplierCode = $("#supplierCode").val();
	var outsideNo = $("#outsideNo").val();
	var provinceNo = $("#provinceNo").val();
	var province = $("#province").val();
	var cityNo = $("#cityNo").val();
	var city = $("#city").val();
	var areaNo = $("#areaNo").val();
	var area = $("#area").val();
	var address = $("#address").val();
	var contact = $("#contact").val();
	var phone = $("#phone").val();
	var remark = $("#remark").val();
	
	if(YouGou.Util.isEmpty(provinceNo)||YouGou.Util.isEmpty(cityNo)||YouGou.Util.isEmpty(areaNo)){
		$("#provinceNo_cityNo_areaNo_stip").html("所在地区必须选择！");
		content.focus();
		return false;
	}
	
	if(YouGou.Util.isEmpty(address)){
		$("#address_stip").html("详细地址不能为空！");
		content.focus();
		return false;
	}
	if(YouGou.Util.isEmpty(contact)){
		$("#contact_stip").html("联络人不能为空！");
		content.focus();
		return false;
	}
	if(YouGou.Util.isEmpty(phone)){
		$("#phone_stip").html("电话号码不能为空");
		content.focus();
		return false;
	}
	
	YouGou.Ajax.doPost({
		successMsg: "操作成功",
		url: "/afterSale/saveOrUpdateSupplierAddress.sc",
	    data: {"id":id,"supplierCode":supplierCode,"outsideNo":outsideNo,"provinceNo":provinceNo,"province":province,"cityNo":cityNo,"city":city,"areaNo":areaNo,"area":area,"address":address,"contact":contact,"phone":phone,"remark":remark},
	  	success : function() {
			mmGrid.load();
	  		dialog.close();
  		}
	});
}


function getAreaOptionByPNo(level,pNoAndName){
	var levelone = $('#levelone');
	var leveltwo = $('#leveltwo');
	var levelthree = $('#levelthree');
	var pNo=pNoAndName;
	if(pNoAndName!=''){
		var pNo=pNo.split("|")[0];
	}
	$.ajax({
		url:'/afterSale/getSystemmgAreaData.sc',
		type:'post',
		async:false,
		data:{"level":level,"pNo":pNo},
		dataType:'html',
		success:function(data){
			if(level=='1'){
				levelone.html(data);
				setSelectOption("levelone",pNoAndName);
			}else if(level == '2'){
				leveltwo.html(data);
				if(pNoAndName!=''){
					setSelectOption("levelone",pNoAndName);
					$('#provinceNo').val(pNoAndName.split("|")[0]);
					$('#province').val(pNoAndName.split("|")[1]);
					$('#cityNo').val('');
					$('#city').val('');
					$('#areaNo').val('');
					$('#area').val('');
				}
			}else if(level == '3'){
				levelthree.html(data);
				if(pNoAndName!=''){
					setSelectOption("leveltwo",pNoAndName);
					$('#cityNo').val(pNoAndName.split("|")[0]);
					$('#city').val(pNoAndName.split("|")[1]);
					$('#areaNo').val('');
					$('#area').val('');
				}
			}else{
				if(pNoAndName!=''){
					setSelectOption("levelthree",pNoAndName);
					$('#areaNo').val(pNoAndName.split("|")[0]);
					$('#area').val(pNoAndName.split("|")[1]);
				}
			}
		}
	});
}

function setSelectOption(objId, targetValue){
	  var obj = document.getElementById(objId);
	  if(obj){
	    var options = obj.options;
	    if(options){
	      var len = options.length;
	      for(var i=0;i<len;i++){
	        if(options[i].value == targetValue){
	          options[i].defaultSelected = true;
	          options[i].selected = true;
	          return true;
	        }
	      }
	    } else {
	      alert('missing element(s)!');
	    }
	  } else {
	    alert('missing element(s)!');
	  }
	}




