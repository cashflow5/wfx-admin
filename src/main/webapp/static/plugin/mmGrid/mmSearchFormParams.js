function MMSearchFormParams(formId) { 
    this.formId = formId;
    try{
	    // 回车查询
	    $('#'+ formId +' input').keypress(function(e) {
	        if (e.which == 13){
	        	doQuery();
	            return false;
	        }
	    });
	}catch(e){}
}

MMSearchFormParams.prototype.params = function() { 
    // 初始化分页
  	var param = {};
  	// 获取form参数array
  	var formParam = $("#"+this.formId).serializeArray();
  	for (var i=0; i < formParam.length; i++){
  		for(var j=0; j < formParam.length; j++){
  			if(formParam[j] != null && !YouGou.Util.isEmpty(formParam[i].value)){
  				if(formParam[i].name == formParam[j].name && formParam[i].value != formParam[j].value){
  	  				formParam[i].value = formParam[i].value + "," + formParam[j].value;
  	  				formParam[j].name = "";
  	  				formParam[j].value = "";
  	  			}
  			}
  		}
  	}
  	for (var i=0; i < formParam.length; i++){
  		var obj = formParam[i];
  		if(!YouGou.Util.isEmpty(obj.name)){
	  		var attrObj = eval("({'"+ obj.name +"':'"+ obj.value.trim() +"'})");
	  		$.extend(param, attrObj);
  		}
  	}
    return param;
}

MMSearchFormParams.prototype.init = function() {
}

MMSearchFormParams.prototype.check = function() {
	// 检查搜索条件至少输入一项
	if(YouGou.UI.checkFormEmpty(this.formId)){
		YouGou.UI.Dialog.show({message : '搜索条件至少输入一项!'});
		return false;
	}
	return true;
}