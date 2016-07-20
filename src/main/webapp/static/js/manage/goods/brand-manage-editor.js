/**
* by lijunfang 20160324
**/
var BandManageEditor = function(){
	/**
	* 初始化函数
	**/
	function Init(){
		
		FunZtreen();
		$('.img-box').each(function(index,ele){
			var arr = [],width,height,
				imgId = '#'+$(ele).attr('id'),
				btnId = '#'+$(ele).siblings('.file-btn').attr('id'),
				sizeString = $(ele).find('.img-size').html();
			if(sizeString){
				arr = sizeString.split('*');
				width = arr[0];
				height = arr[1];
				Upload.fileUpload(imgId,btnId,width,height);
				console.log(imgId);
			}else{
				Upload.fileUpload(imgId,btnId,118,118);
			}
		})	
	};

	/**
	* ztree函数
	**/
	function FunZtreen(){
		var setting = {
	        check: {
	            enable: true
	        },
	        view: {
	            showLine: true
	        },
	        data: {
	            key: {
	                title: "title"
	            },
	            simpleData: {
	                enable: true
	            }
	        }
	    },
	    nodes = [
	            {'id':'1',  'name': "全部",'pId':'0'}	           
	        ];
		YouGou.Ajax.doPost({
			url: '/commodity/queryBasicCat.sc?id='+$("#brandId").val(),
		  	data: {},
		  	tips:false,
		  	success : function(data){
		  		nodes = JSON.parse(data.data);
	  			var treeObj = $.fn.zTree.init($("#elementList"), setting, nodes);
	  			if (treeObj != null) {
	  				var nodes = treeObj.getCheckedNodes();
	  				for(var idx in nodes){
		  				treeObj.checkNode(nodes[idx],true,true);
		  			}
	  			}
			}
		});
	    
	};

	/**
	* upload 函数
	**/

	$(function(){
		Init();
	});
	return{

	}
}();
