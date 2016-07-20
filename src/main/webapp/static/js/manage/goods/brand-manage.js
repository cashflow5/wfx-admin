/**
* by lijunfang 20160323
**/
/**
* by lijunfang 20160324
**/
var BrandManage = function(){
	/**
	* 初始化函数
	**/
	function Init(){
		EditorDialog();
	}
	function EditorDialog(){
		$('.editor-btn').click(function(){
			YouGou.UI.progressLoading();
			$.ajax({
				type:'post',
				url:'brand-manage-editor.shtml',
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
								action:function(dialog){
									dialog.close();
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
		})
	}
	$(function(){
		Init();
	});
	return{

	}
}();