/**
 * 该函数库用于全局
**/
(function() {
	if (typeof YouGou == 'undefined') {
		// 声明框架命名空间
		YouGou = {
			Biz:{},
			Base : {},
			Util : {},
			UI : {},
			Ajax : {}
		};
	}

	// 基础函数库
	YouGou.Base = {
		// 继承
		apply : function(C, D, B) {
			if (B) {
				YouGou.Base.apply(C, B);
			}
			if (C && D && typeof D == "object") {
				for (var A in D) {
					C[A] = D[A];
				}
			}
			return C;
		}
	};

	// String扩展去除空格
	String.prototype.trim = function(){
		var str = this;
		str = str.replace(/^\s\s*/, "");
		var ws = /\s/;
		var i = str.length;
		while (ws.test(str.charAt(--i)));
		return str.slice(0, i + 1);
	};

	/*扩展string，添加format方法
	 add by guoran 20140811
	 两种调用方法：
	 eg1:
	 var template1="我是{0}，今年{1}了";
	 var result1=template1.format("loogn",22);
	 eg2:
	 var template2="我是{name}，今年{age}了";
	 var result2=template2.format({name:"loogn",age:22});
	 两个结果都是"我是loogn，今年22了"
	 */
	String.prototype.format = function (args) {
		var result = this;
		if (arguments.length > 0) {
			if (arguments.length == 1 && typeof(args) == "object") {
				for (var key in args) {
					if (args[key] != undefined) {
						var reg = new RegExp("({" + key + "})", "g");
						result = result.replace(reg, args[key]);
					}
				}
			} else {
				for (var i = 0; i < arguments.length; i++) {
					if (arguments[i] != undefined) {
						var reg = new RegExp("({)" + i + "(})", "g");
						result = result.replace(reg, arguments[i]);
					}
				}
			}
		}
		return result;
	};

	String.prototype.len = function () {
		return this.replace('[^\x00-\xff]/g', 'aa').length;
	};
	
	/*扩展string，添加format方法
	 add by guoran 20140811
	 两种调用方法：
	 eg1:
	 var template1="我是{0}，今年{1}了";
	 var result1=template1.format("loogn",22);
	 eg2:
	 var template2="我是{name}，今年{age}了";
	 var result2=template2.format({name:"loogn",age:22});
	 两个结果都是"我是loogn，今年22了"
	 */
	String.prototype.format = function (args) {
		var result = this;
		if (arguments.length > 0) {
			if (arguments.length == 1 && typeof(args) == "object") {
				for (var key in args) {
					if (args[key] != undefined) {
						var reg = new RegExp("({" + key + "})", "g");
						result = result.replace(reg, args[key]);
					}
				}
			} else {
				for (var i = 0; i < arguments.length; i++) {
					if (arguments[i] != undefined) {
						var reg = new RegExp("({)" + i + "(})", "g");
						result = result.replace(reg, arguments[i]);
					}
				}
			}
		}
		return result;
	};

	String.prototype.len = function () {
		return this.replace('[^\x00-\xff]/g', 'aa').length;
	};
	
	/** 
	 * 时间对象的格式化; 
	 */  
	Date.prototype.format = function(format) {  
	    /* 
	     * eg:format="yyyy-MM-dd hh:mm:ss"; 
	     */  
	    var o = {  
	        "M+" : this.getMonth() + 1, // month  
	        "d+" : this.getDate(), // day  
	        "h+" : this.getHours(), // hour  
	        "m+" : this.getMinutes(), // minute  
	        "s+" : this.getSeconds(), // second  
	        "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter  
	        "S" : this.getMilliseconds()  
	        // millisecond  
	    }  
	    if (/(y+)/.test(format)) {  
	        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4  - RegExp.$1.length));  
	    }  
	    for (var k in o) {  
	        if (new RegExp("(" + k + ")").test(format)) {  
	            format = format.replace(RegExp.$1, RegExp.$1.length == 1   ? o[k]  : ("00" + o[k]).substr(("" + o[k]).length));  
	        }  
	    }  
	    return format;  
	}  
    
	// 工具辅助函数库
	YouGou.Util = {
		// 空字符串判断
		isEmpty : function(v, allowBlank) {
			return v === null || v === undefined || (!allowBlank ? v === "" : false);
		},
		// 空对象判断
		isNull : function(obj) {
			if (typeof obj == "undefined" || obj == null)
				return true;
			else
				return false;
		},
		// print Object
		inspect : function(obj){
			/*var s = obj + "\n";
			for (var a in obj) {
				if (typeof obj[a] != "function") {
					s += a + "=" + obj[a] + ",\n";
				}
			}*/
			$.when(
				$('<link href="/static/plugin/objtree/otv.css" rel="stylesheet" type="text/css"/>').appendTo('head'),
			 	$.getScript('/static/plugin/objtree/otv.js')
			).then(function(a, b, c) {
				var html = [];
				html.push('<div id="objTree"></div>');
				YouGou.UI.Dialog.show({
					title: '对象查看',
					message: html.join(''),
					cssClass: 'message-obj-inspect',
					onshown : function(){
						var cont = document.getElementById("objTree");
						objectTreeView.expand("Object", obj, cont);
					}
				});
			});
		},
		// 对象转换成json字符串
		toJsonString : function(arg) {
		    return YouGou.Util.toJsonStringArray(arg).join('');
		},
		// 对象转换成json字符串数组
		toJsonStringArray : function(arg, out) {
		    out = out || new Array();
		    var u;
		    switch (typeof arg) {
		    case 'object':
		        if (arg) {
		            if (arg.constructor == Array) {
		                out.push('[');
		                for (var i = 0; i < arg.length; ++i) {
		                    if (i > 0)
		                        out.push(',\n');
		                    YouGou.Util.toJsonStringArray(arg[i], out);
		                }
		                out.push(']');
		                return out;
		            } else if (typeof arg.toString != 'undefined') {
		                out.push('{');
		                var first = true;
		                for (var i in arg) {
		                    var curr = out.length;
		                    if (!first)
		                        out.push(',\n');
		                    YouGou.Util.toJsonStringArray(i, out);
		                    out.push(':');
		                    YouGou.Util.toJsonStringArray(arg[i], out);
		                    if (out[out.length - 1] == u)
		                        out.splice(curr, out.length - curr);
		                    else
		                        first = false;
		                }
		                out.push('}');
		                return out;
		            }
		            return out;
		        }
		        out.push('null');
		        return out;
		    case 'unknown':
		    case 'undefined':
		    case 'function':
		        out.push(u);
		        return out;
		    case 'string':
		        out.push('"')
		        out.push(arg.replace(/(["\\])/g, '\\$1').replace(/\r/g, '').replace(/\n/g, '\\n'));
		        out.push('"');
		        return out;
		    default:
		        out.push(String(arg));
		        return out;
		    }
		},
		// 微型模板引擎
		tpl : function(str, data){
			var fn = !/\W/.test(str) ?
			  cache[str] = cache[str] ||
				YouGou.Util.tpl(document.getElementById(str).innerHTML) :
			  new Function("obj",
				"var p=[],print=function(){p.push.apply(p,arguments);};" +
				"with(obj){p.push('" +
				str.replace(/[\r\t\n]/g, " ")
				  .split("<%").join("\t")
				  .replace(/((^|%>)[^\t]*)'/g, "$1\r")
				  .replace(/\t=(.*?)%>/g, "',$1,'")
				  .split("\t").join("');")
				  .split("%>").join("p.push('")
				  .split("\r").join("\\'")
			  + "');}return p.join('');");
			return data ? fn( data ) : fn;
		},
		emptyFixed : function (val){
			if(YouGou.Util.isEmpty(val)){
                return '';
            }
            return val;
		},
		moneyFixed : function(val){
			if(YouGou.Util.isEmpty(val)){
				return "";
			}else{
				return "￥" + val;
			}
		},
		integralFixed : function(val){
			if(YouGou.Util.isEmpty(val)){
				return "";
			}else{
				return val + "分";
			}
		},
		gotoSku : function(commodityId){//打开商品url
			$.ajax({
			       type: "post",
			       url: "/comment/gotoSku.sc",
			       async:false,
			       data: "commodityId="+commodityId,
			       success : function(url) {
			    	   window.open(url); 
				  }
		    }); 
		},
		timeFixed : function(val){
			if(!YouGou.Util.isEmpty(val)){
				if(val.toString().indexOf('-') != -1){
					if(val.toString().indexOf('.0') != -1){
						try{
							val = val.substring(0,val.indexOf('.'));
						}catch(e){}
					}
				}else{
					try {
						val = new Date(val).format('yyyy-MM-dd hh:mm:ss');
					} catch (e) {}
				}
			}
			return val;
		},
		percentFixed : function(val){
			return val + "%";
		},
		memberShipNameFixed : function(val){
			if(!YouGou.Util.isEmpty(val)){
		    	if(val == 'A'){
		    		return '白名单'
		    	}else if(val == 'B'){
		    		return '新会员'
		    	}else if(val == 'G'){
		    		return '会员'
		    	}else if(val =='C'){
		    		return '红名单'
		    	}else if(val =='D'){
		    		return '灰名单'
		    	}else if(val =='E'){
		    		return '黄名单'
		    	}else if(val =='F'){
		    		return '黑名单'
		    	}else{
		    		return '未知'
		    	}
		    }else{
		    	return '未知'
		    }
		},
		platFormTypeFixed : function(val){
			if(!YouGou.Util.isEmpty(val)){
				if(val == 'yougou'){
					return 'pc网站';
				}else if(val == 'mall_ios'){
					return '苹果App';
				}else if(val == 'mall_android'){
					return '安卓App';
				}else if(val == 'mobile_touch'){
					return '无线-H5';
				}else{
					return '未知['+ val +']';
				}
			}else{
				return '空';
			}
		},
		regTypeFixed : function(val){
			if(!YouGou.Util.isEmpty(val)){
				if(val == 'yougou'){
					return '用户名密码';
				}else if(val == 'taoxiao'){
					return '淘秀网';
				}else if(val == 'alipay'){
					return '支付宝';
				}else if(val == 'quick-qq'){
					return 'QQ';
				}else if(val == 'quick-renren'){
					return '人人网';
				}else if(val == 'quick-sinaWeibo'){
					return '新浪微博';
				}else if(val == 'quick-weixin'){
					return '微信';
				}else if(val == 'quick-xiaomi'){
					return '小米';
				}else if(val == 'union-qqfanli'){
					return 'QQ返利';
				}else if(val == 'union-139fanli'){
					return '139返利';
				}else if(val == 'union-51fanli'){
					return '51返利';
				}else if(val == 'union-360cps'){
					return '360';
				}else if(val == 'union-cmbstore'){
					return '招商银行cmbstore';
				}else{
					return '未知['+ val +']';
				}
			}else{
				return '空';
			}
		},
		numberIdFixed : function(val){// 需要废除旧numberId字段
			if(!YouGou.Util.isEmpty(val)){
				if(val == 'yougou'){
					return 'PC优购';
				}else if(val == 'mobile_ios'){
					return '苹果App';
				}else if(val == 'mobile_android'){
					return '安卓App';
				}else if(val == 'taoxiao'){
					return '淘秀网';
				}else if(val == 'alipay'){
					return '支付宝';
				}else if(val == 'qq'){
					return 'QQ';
				}else if(val == 'renren'){
					return '人人网';
				}else if(val == 'sinaWeibo'){
					return '新浪微博';
				}else if(val == 'weixin'){
					return '微信';
				}else if(val == 'xiaomi'){
					return '小米';
				}else if(val == 'qqfanli'){
					return 'QQ返利';
				}else if(val == '139fanli'){
					return '139返利';
				}else if(val == '51fanli'){
					return '51返利';
				}else if(val == '360cps'){
					return '360';
				}else if(val == 'cmbstore'){
					return '招商银行cmbstore';
				}else{
					return '未知['+ val +']';
				}
			}else{
				return '空';
			}
		},
		foreignMemberSourceFixed : function(val){// 外部平台会员来源
			if(!YouGou.Util.isEmpty(val)){
				if(val.indexOf('TB')==0){
					return '淘宝'
				}else if(val.indexOf('WBPT-DD')==0){
					return '外部平台-当当'
				}else if(val.indexOf('WBPT-JD')==0){
					return '外部平台-京东'
				}else if(val.indexOf('WBPT-YT')==0){
					return '外部平台-银泰'
				}else if(val.indexOf('WBPT-1HD')==0){
					return '外部平台-1号店'
				}else if(val.indexOf('WBPT-PP')==0){
					return '外部平台-拍拍'
				}else if(val.indexOf('WBPT-FKVjia')==0){
					return '外部平台-凡客Vjia'
				}else if(val.indexOf('WBPT-FKTM')==0){
					return '外部平台-凡客特卖'
				}else if(val.indexOf('WBPT-YDSC')==0){
					return '外部平台-移动商城'
				}else if(val.indexOf('WBPT-QQ')==0){
					return '外部平台-QQ网购'
				}else if(val.indexOf('WBPT-WXSC')==0){
					return '外部平台-微信商城'
				}else if(val.indexOf('WBPT-YMX')==0){
					return '外部平台-亚马逊'
				}else if(val.indexOf('WBPT-SN')==0){
					return '外部平台-苏宁'
				}else{
					return val;
				}
			}else{
				return '空';
			}
		},
		//结果：startDate与endDate相比，如果startDate大于endDate,则返回true,否返回false,默认返回false　格式：yyyy-MM-dd HH:mm:ss
		compareTime : function(startDate,endDate) {
			if (startDate.length > 0 && endDate.length > 0) {
				var startDateTemp = startDate.split(" ");
				var endDateTemp = endDate.split(" ");
				var arrStartDate = startDateTemp[0].split("-");
				var arrEndDate = endDateTemp[0].split("-");
				var arrStartTime = startDateTemp[1].split(":");
				var arrEndTime = endDateTemp[1].split(":");
				var allStartDate = new Date(arrStartDate[0], arrStartDate[1],arrStartDate[2], arrStartTime[0], arrStartTime[1],arrStartTime[2]);
				var allEndDate = new Date(arrEndDate[0], arrEndDate[1], arrEndDate[2],arrEndTime[0], arrEndTime[1], arrEndTime[2]);
				if (allStartDate.getTime() > allEndDate.getTime()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}  
		},
		// TaskRunner任务器
	    TaskRunner : function(interval){
			interval = interval || 10;
		    var tasks = [],
		    removeQueue = [],
		    id = 0,
		    running = false,
		    // private
		    stopThread = function() {
		        running = false;
		        clearInterval(id);
		        id = 0;
		    },
		    // private
		    startThread = function() {
		        if (!running) {
		            running = true;
		            id = setInterval(runTasks, interval);
		        }
		    },
		    // private
		    removeTask = function(t) {
		        removeQueue.push(t);
		        if (t.onStop) {
		            t.onStop.apply(t.scope || t);
		        }
		    },
		    // private
		    runTasks = function() {
		        var rqLen = removeQueue.length,now = new Date().getTime(),i;
		        if (rqLen > 0) {
		            for (i = 0; i < rqLen; i++) {
		                tasks.remove(removeQueue[i]);
		            }
		            removeQueue = [];
		            if (tasks.length < 1) {
		                stopThread();
		                return;
		            }
		        }
		        i = 0;
		        var t,itime,rt,len = tasks.length;
		        for (; i < len; ++i) {
		            t = tasks[i];
		            itime = now - t.taskRunTime;
		            if (t.interval <= itime) {
		                rt = t.run.apply(t.scope || t, t.args || [++t.taskRunCount]);
		                t.taskRunTime = now;
		                if (rt === false || t.taskRunCount === t.repeat) {
		                    removeTask(t);
		                    return;
		                }
		            }
		            if (t.duration && t.duration <= (now - t.taskStartTime)) {
		                removeTask(t);
		            }
		        }
		    };
		    this.start = function(task) {
		        tasks.push(task);
		        task.taskStartTime = new Date().getTime();
		        task.taskRunTime = 0;
		        task.taskRunCount = 0;
		        startThread();
		        return task;
		    };
		    this.stop = function(task) {
		        removeTask(task);
		        return task;
		    };
		    this.stopAll = function() {
		        stopThread();
		        for (var i = 0, len = tasks.length; i < len; i++) {
		            if (tasks[i].onStop) {
		                tasks[i].onStop();
		            }
		        }
		        tasks = [];
		        removeQueue = [];
		    };
	    },
        Debug: function () {
            if (location.search.indexOf('debugmode') > -1) {
                $('<script src="/assets/ace/js/mock/mock-min.js"></script>').appendTo('body');
                $('<script src="/static/js/debug-data.js"></script>').appendTo('body');
            }
        }
	};

	// 提供页面端函数
	YouGou.UI = {
		checkFormEmpty : function(formId){
			var isEmpty = true;
			var formParam = $("#"+formId).serializeArray();
			for (var i=0; i < formParam.length; i++){
          		var obj = formParam[i];
          		if(!YouGou.Util.isEmpty($.trim(obj.value))){
          			isEmpty = false;
          			break;
          		}
		  	}
		  	return isEmpty;
		},
		buildSecondSourceSelect : function(secondSource1,secondSource2){
			//会员来源二级菜单联动
			var select1 = $('#'+secondSource1);
			var select2 = $('#'+secondSource2);
			select1.append($("<option>").val('').text("请选择"));
			select1.append($("<option>").val('YG').text("优购"));
			select1.append($("<option>").val('TB').text("淘宝"));
			select1.append($("<option>").val('WBPT').text("外部平台"));
			select2.append($("<option>").val('').text("请选择"));
			select1.change(function(){
				var val1 = $(this).val();
				select2.empty();
				if(val1 == 'TB'){
					select2.append($("<option>").val('TB-TBZXD').text("淘宝直销店"));
				}else if(val1 == 'WBPT'){
					select2.append($("<option>").val('').text("请选择"));
					select2.append($("<option>").val('WBPT-DD').text("当当"));
					select2.append($("<option>").val('WBPT-JD').text("京东"));
					select2.append($("<option>").val('WBPT-1HD').text("1号店"));
					select2.append($("<option>").val('WBPT-SN').text("苏宁"));
					select2.append($("<option>").val('WBPT-YT').text("银泰"));
					select2.append($("<option>").val('WBPT-YMX').text("亚马逊"));
					select2.append($("<option>").val('WBPT-FKVjia').text("凡客Vjia"));
					select2.append($("<option>").val('WBPT-FKTM').text("凡客特卖"));
					select2.append($("<option>").val('WBPT-YDSC').text("移动商城"));
					select2.append($("<option>").val('WBPT-PP').text("拍拍"));
					select2.append($("<option>").val('WBPT-QQ').text("QQ网购"));
					select2.append($("<option>").val('WBPT-WXSC').text("微信商城"));
				}else{
					select2.append($("<option>").val('').text("无"));
				}
			});
		},
		getFormParams : function(formId){
			// 初始化分页
          	var param = {};
          	// 点击查询初始化page
      		$.extend(param, eval("({page:1})"));
          	// 获取form参数array
          	var formParam = $("#"+formId).serializeArray();
          	for (var i=0; i < formParam.length; i++){
          		var obj = formParam[i];
          		var attrObj = eval("({'"+ obj.name +"':'"+ obj.value.trim() +"'})");
          		$.extend(param, attrObj);
		  	}
          	return param;
		},
		Dialog : {
			Type : {
				DEFAULT : 'type-default',INFO : 'type-info',PRIMARY : 'type-primary',SUCCESS : 'type-success',WARNING : 'type-warning',DANGER : 'type-danger'
			},
			Size : {
				NORMAL : 'size-normal',SMALL : 'size-small',WIDE : 'size-wide',LARGE : 'size-large'
			},
			alert : function(param){
				param.type = this.Type.WARNING;
				param.closable = true;
				param.closeByBackdrop = false;
				param.cssClass = 'message-alert';
				param.buttons = [{
	                label: '关闭',
	                action: function(dialogRef){
	                    dialogRef.close();
	                }
	            }];
				this.show(param);
			},
			show : function(param){
				param.title = param.title || '提示';
				param.type = param.type || this.Type.PRIMARY;
				param.size = param.size || this.Size.NORMAL;
				param.draggable = param.draggable || true;
				param.isConfirm = param.isConfirm || false;
				param.callback = param.callback || function(dialog){};
				var _showDialog = function(){
					// I18N
					BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_DEFAULT] = '通知';
			        BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_INFO] = '通知';
			        BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_PRIMARY] = '通知';
			        BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_SUCCESS] = '成功';
			        BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_WARNING] = '警告';
			        BootstrapDialog.DEFAULT_TEXTS[BootstrapDialog.TYPE_DANGER] = '危险';
			        BootstrapDialog.DEFAULT_TEXTS['OK'] = '确定';
			        BootstrapDialog.DEFAULT_TEXTS['CANCEL'] = '取消';
			        BootstrapDialog.DEFAULT_TEXTS['CONFIRM'] = '确认';
			        var dialog = null;
			        if(param.isConfirm){
			        	param.title = param.title!='提示'?param.title:'操作确认';
			        	//param.type = param.type!=YouGou.UI.Dialog.Type.PRIMARY?YouGou.UI.Dialog.Type.PRIMARY : YouGou.UI.Dialog.Type.WARNING;
			        	dialog = BootstrapDialog.confirm(param,param.callback);
			        }else{
			        	dialog = BootstrapDialog.show(param);
			        	 if(param.callback != null){
				        	 param.callback(dialog);
				        }
			        }
					return dialog;
				};
				if ((typeof BootstrapDialog == 'undefined')){
					$.when(
						$('<link href="/static/plugin/bootstrap-dialog/bootstrap-dialog.min.css" rel="stylesheet" type="text/css"/>').appendTo('head'),
						$('<link href="/static/plugin/bootstrap-dialog/fix.css" rel="stylesheet" type="text/css"/>').appendTo('head'),
					 	$.getScript('/static/plugin/bootstrap-dialog/bootstrap-dialog.min.js')
					).then(function(a, b, c) {
						return _showDialog();
					});
				}else{
					return _showDialog();
				}
			},
			confirm : function(param,callback){
				param.isConfirm = true;
				param.callback = callback;
				param.cssClass = 'message-confirm';
				this.show(param);
			},
			autoCloseTip:function(message,time){
				var param = {};
				param.message = message;
				param.type = this.Type.WARNING;
				param.closable = true;
				param.closeByBackdrop = false;
				param.cssClass = 'message-alert';
				param.callback = function(dialog){
            		setTimeout(function(){dialog.close();},time||1500);
            	}
				this.show(param);
			}
		},
		tipError : function(msg){
			YouGou.UI.tip(msg,'error');
		},
		tipSuccess : function(msg){
			YouGou.UI.tip(msg,'success');
		},
		tip : function(msg,type){
			var _tip = function(){
				var conf = {
					message:msg,
					type:type,
					showCloseButton:true
				};
				if(this.msgObj == null){
					this.msgObj = Messenger().post(conf);
				}else{
					this.msgObj.update(conf);
				}
			};
			
			if ((typeof window.Messenger == 'undefined')){
				$.when(
					$('<link href="/static/plugin/messenger/css/messenger.css" rel="stylesheet" type="text/css"/>').appendTo('head'),
					$('<link href="/static/plugin/messenger/css/messenger-theme-future.css" rel="stylesheet" type="text/css"/>').appendTo('head'),
				 	$.getScript('/static/plugin/messenger/js/messenger.min.js'),
				 	$.getScript('/static/plugin/messenger/js/messenger-theme-future.js')
				).then(function(a, b, c) {
					_tip();
				});
			}else{
				_tip();
			}
		},
		progressLoading :function(){
			var loading = function(){
				NProgress.start();
			    NProgress.set(0.45);
			    NProgress.inc();
			};
			if (typeof NProgress == 'undefined'){
				$.when(
						$('<link href="/static/plugin/nprogress/nprogress.css" rel="stylesheet" type="text/css"/>').appendTo('head'),
					 	$.getScript('/static/plugin/nprogress/nprogress.js')
				).then(function(a, b, c) {
						loading();
				});
			}else{
				loading();
			}
		},
		progressStop :function(){
			var done = function(){
				NProgress.done();
			};
			if (typeof NProgress == 'undefined'){
				$.when(
						$('<link href="/static/plugin/nprogress/nprogress.css" rel="stylesheet" type="text/css"/>').appendTo('head'),
					 	$.getScript('/static/plugin/nprogress/nprogress.js')
				).then(function(a, b, c) {
					done();
				});
			}else{
				done();
			}
		},
		fullScreenLoading : function(){
			setTimeout(function () {
				$('.loading-container').removeClass("loading-inactive").removeClass("hide");
				$('.loading-container').click(function(){
					$(this).addClass('loading-inactive').addClass("hide");
				});
			}, 0);
		},
		fullScreenStopLoading : function(){
			setTimeout(function () {
				$('.loading-container').addClass('loading-inactive').addClass("hide");
			}, 0);
		},
		loading : function(){
			setTimeout(function () {
				$('.loading-container').removeClass("loading-inactive");
				YouGou.UI.progressLoading();
			}, 0);
		},
		stopLoading : function(){
			setTimeout(function () {
				$('.loading-container').addClass('loading-inactive');
				YouGou.UI.progressStop();
			}, 0);
		},
		loadingWithClickEvent : function(){
			setTimeout(function () {
				$('.loading-container').removeClass("loading-inactive");
				YouGou.UI.progressLoading();
				$('.loading-container').click(function(){
					$(this).addClass('loading-inactive');
					YouGou.UI.progressStop();
				});
			}, 0);
		},
		loadingClickEvent : function(){
			$('.loading-container').click(function(){
				$(this).addClass('loading-inactive');
			});
		},
		menuInit : function(){
			// 根据url菜单反选
			$("a[data-value='menu']").each(function(i){
				var href = $(this).attr("href");
	        	var value = YouGou.Biz.cookie("YG-WFX-Menu-Node");
	        	if(!value){
	        		var pathname = window.location.pathname;
	    			if(pathname.indexOf("?") != -1){
	    				pathname = pathname.substring(0,pathname.indexOf("?"));
	    			}
	        		if(href.indexOf("?") != -1){
						href = href.substring(0,href.indexOf("?"));
					}
					if(href == pathname){
						$(this).parent().addClass("active");
						$(this).parent().parent().parent().addClass("active").addClass("open");
					}
	        	}else if(href == value){
	        		$(this).parent().addClass("active");
					$(this).parent().parent().parent().addClass("active").addClass("open");
	        	}
			});
			$("a[data-value='menu']").click(function(i){
				$("a[data-value='menu']").each(function(i){
					$(this).parent().removeClass("active");
					$(this).parent().parent().parent().removeClass("active").removeClass("open");
				});
				$(".submenu").hide();
				YouGou.UI.menuExpand();
				var value = $(this).attr('href');
				YouGou.Biz.cookie("YG-WFX-Menu-Node", value, {path: "/"});
				$(this).parent().addClass("active");
				$(this).parent().parent().parent().addClass("active").addClass("open");
				$(this).parent().parent().show();
			});
		},
		resetForm : function(formId){
			$("#"+formId)[0].reset();
			$(':input','#'+formId).not(':button,:submit,:reset,:radio').val('');
			
			// 清除validate
			$('.form-group').removeClass('has-error');
	        $('span[generated="true"]').remove();
		},
		initForm : function(formId, data){
			if(YouGou.Util.isEmpty(formId)){
				alert("初始化表单时formId为空!");
				return;
			}else if($("#"+formId).length == 0){
				alert("指定formId为表单元素不存在!");
				return;
			}
			for (var attr in data) {
				if (typeof (data[attr]) == 'function') {
					continue;
				}
				var $input = $("#" + formId + " :input[name='"+ attr +"']");
				if($input.length == 0){
					continue;
				}
				var type = $input.attr("type");
				if(type == "checkbox") {
					var avalues = data[attr].split(",");
					for(var v = 0; v < avalues.length; v++) {
						$input.each(function(i, n) {
							var value = $(n).val();
							if(value == avalues[v]) {
								$(n).attr("checked", "checked");
							}else{
								$(n).attr("checked", false);
							}
						});
					}
				}else if(type == "radio") {
					$input.each(function(i, n) {
						$(n).attr("disabled",false);
						var value = $(n).val();
						if (value == data[attr]) {
							$(n).attr("checked", "checked").click();
						}else{
							$(n).attr("checked", false);
						}
					});
				}else if(type == "text" || type == "hidden" || type == "password") {
					var $inputtext = $("#" + formId + " :input[name='" + attr + "']");
					$inputtext.val("" + data[attr]);
				}else{
					$input.val("" + data[attr]);
				}
			}
		},
		bindFormValidator : function(formId,rules,messages){
			return $('#'+formId).validate({
			    errorElement : 'span',
			    errorClass : 'help-block',
			    onkeyup : false,
			    focusInvalid: true,
			    ignore: "",
			    rules : rules,
			    messages : messages,
			    highlight : function(element) {  
			        $(element).closest('.form-group').addClass('has-error');
			    },
			    success : function(label) {
			        label.closest('.form-group').removeClass('has-error');
			        label.remove();
			    },
			    errorPlacement : function(error, element) {
			    	if(element[0].type == "radio"){
			    		element.parent().parent('div').append(error);
			    	}else{
			    		element.parent('div').append(error);
			    	}
			    },
			    submitHandler : function(form) {
			    }
			});
		},
		menuExpand : function(){ // 菜单展开
			// 当前为收缩
			if($(".fa-angle-double-right").length >0){
				$('#sidebar-collapse').click();
				YouGou.Biz.cookie("ace_settings","%7B%22sidebar-collapsed%22%3A%7D",{expires : 2});
			}
		},
		menuContract : function(){ // 菜单收缩
			// 当前为展开
			if($(".fa-angle-double-left").length >0){
				$('#sidebar-collapse').click();
				YouGou.Biz.cookie("ace_settings","%7B%22sidebar-collapsed%22%3A-1%7D",{expires : 2});
			}
		}
	};

	// 业务相关函数库
	YouGou.Biz = {
		// Cookie操作
		cookie : function(name, value, options){
			if (typeof value != 'undefined') {
		        options = options || {};
		        if (value === null) {
		            value = '';
		            options.expires = -1;
		        }
		        var expires = '';
		        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
		            var date;
		            if (typeof options.expires == 'number') {
		                date = new Date();
		                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
		            } else {
		                date = options.expires;
		            }
		            expires = '; expires=' + date.toUTCString();
		        }
		        var path = options.path ? '; path=' + (options.path) : '';
		        var domain = options.domain ? '; domain=' + (options.domain) : '';
		        var secure = options.secure ? '; secure' : '';
		        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
		    } else {
		        var cookieValue = null;
		        if (document.cookie && document.cookie != '') {
		            var cookies = document.cookie.split(';');
		            for (var i = 0; i < cookies.length; i++) {
		                var cookie = jQuery.trim(cookies[i]);
		                if (cookie.substring(0, name.length + 1) == (name + '=')) {
		                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
		                    break;
		                }
		            }
		        }
		        return cookieValue;
		    }
		}
	};

	// Ajax函数库
	YouGou.Ajax = {
		doPost : function(conf){
			if (this && typeof this == "object") {
				for (var item in this) {
					if(item != "doPost"){
						this[item] = null;
					}
				}
			}
			YouGou.Base.apply(this,conf);
			var _url = this.url;
			var _type = this.type || "POST";
			var _dataType = this.dataType || "json";
			var _data = this.data || {};
			var _tips = true;
			if(!this.tips && !YouGou.Util.isNull(this.tips)){_tips = false}
			var _fullScreenLoading = true;
			if(!this.fullScreenLoading && !YouGou.Util.isNull(this.fullScreenLoading)){_fullScreenLoading = false};
			var _successMsg = this.successMsg || "数据操作成功!";
			var _progressMsg = this.progressMsg || "正在数据操作，请稍候...";
			var _errorMsg = this.errorMsg || "数据操作失败!";
			var _successCallBack = this.success || function(data){
			};
			var _errorCallBack = this.error || function(XmlHttpRequest, textStatus, errorThrown){
			};
			var _this = this;
			
			
			var _tipProgress = function(){
				if(_fullScreenLoading) YouGou.UI.loadingWithClickEvent();
				if(!_tips) return;
				if(_this.msg == null){
					_this.msg = Messenger().post({message:_progressMsg,type:'error'});
				}else{
					_this.msg.update({message:_progressMsg,type:'error'});
				}
			};
			var _tipSuccess = function(){
				if(_fullScreenLoading) YouGou.UI.stopLoading();
				if(!_tips) return;
				if(_this.msg == null){
					_this.msg = Messenger().post({message:_successMsg,type:'success',showCloseButton:true});
				}else{
					_this.msg.update({message:_successMsg,type:'success',showCloseButton:true});
				}
			};
			var _tipError = function(XmlHttpRequest, textStatus, errorThrown){
				if(_fullScreenLoading) YouGou.UI.stopLoading();
				if(_this.msg == null){
					_this.msg = Messenger().post({message:_errorMsg,type:'error',showCloseButton:true});
				}else{
					_this.msg.update({message:_errorMsg,type:'error',showCloseButton:true});
				}
				YouGou.UI.Dialog.show({
                	type : YouGou.UI.Dialog.Type.WARNING,
    				closable : true,
    				closeByBackdrop : false,
                	cssClass : 'message-mmgrid-load-error',
                	title : '500，抱歉，服务器内部异常！',
                	message : XmlHttpRequest.responseText.replace(/(\n)+|(\r\n)+/g, ""),
                	onshown : function(dialogRef){
                		window.top.mmgridLoadErrorDialog = dialogRef;
                	}
                });
			};
			
			var _doAjax = function(){
				_tipProgress();
				$.ajax({
					type : _type,
					url : _url,
					data : _data,
					dataType : _dataType,
					success :function(result){
						if(result.state == "success"){
				  			_tipSuccess();
							_successCallBack(result);
				  		}else if(result.state == "error"){
				  			YouGou.UI.Dialog.show({
								message : "操作失败,msg=" + result.msg,
								type : YouGou.UI.Dialog.Type.DANGER
							});
				  			_tipError();
				  		}
					},
					error :function(XmlHttpRequest, textStatus, errorThrown){
						var statusCode = XmlHttpRequest.status;
		                var message = "";
		                var title = "";
		                if(statusCode == 500){
		                	_tipError(XmlHttpRequest, textStatus, errorThrown);
							_errorCallBack(XmlHttpRequest, textStatus, errorThrown);
		                }else if(statusCode == 404){
		                	title = '404，您操作的请求未找到！';
		                	var html = [];
		                	html.push('<div class="row"><div class="col-xs-12"><div class="error-container"><div class="well"><h1 class="grey lighter smaller"><span class="bigger-125" style="color: #3DA8B9 !important;"><i class="ace-icon fa fa-sitemap"></i>404</span>您操作的请求未找到！</h1><hr style="margin-top: 10px;margin-bottom: 10px;border: 0;border-top: 1px solid #eee;"><h3 class="lighter smaller">请求地址：'+this.url+'</h3></div></div></div></div>');
		                	message = html.join('');
		                	YouGou.UI.Dialog.show({
			                	type : YouGou.UI.Dialog.Type.WARNING,
			    				closable : true,
			    				closeByBackdrop : false,
			                	cssClass : 'message-mmgrid-load-404',
			                	title : title,
			                	message : message,
			                	onshown : function(dialogRef){
			                		window.top.mmgridLoadErrorDialog = dialogRef;
			                	}
			                });
		                }else if(statusCode == 401){
		                	title = '您的登陆会话已过期，请重新登陆！';
		                	var html = [];
		                	html.push('<div class="row"><div class="col-xs-12"><div class="error-container"><div class="well"><h1 class="grey lighter smaller"><span class="bigger-125" style="color: #3DA8B9 !important;"><i class="ace-icon fa fa-sitemap"></i>401</span>您的登陆会话已过期，请重新登陆！</h1></div><div class="center"><a href="javascript:void(0)" onclick="window.location.reload();" class="btn btn-primary"><i class="ace-icon fa fa-sign-in"></i>重新登陆</a></div></div></div></div>');
		                	message = html.join('');
		                	YouGou.UI.Dialog.show({
			                	type : YouGou.UI.Dialog.Type.WARNING,
			    				closable : true,
			    				closeByBackdrop : false,
			                	cssClass : 'message-mmgrid-load-404',
			                	title : title,
			                	message : message,
			                	onshown : function(dialogRef){
			                		window.top.mmgridLoadErrorDialog = dialogRef;
			                	}
			                });
		                }
					}
				});
			};
			
			if ((typeof window.Messenger == 'undefined') && _tips){
				$.when(
					$('<link href="/static/plugin/messenger/css/messenger.css" rel="stylesheet" type="text/css"/>').appendTo('head'),
					$('<link href="/static/plugin/messenger/css/messenger-theme-future.css" rel="stylesheet" type="text/css"/>').appendTo('head'),
				 	$.getScript('/static/plugin/messenger/js/messenger.min.js'),
				 	$.getScript('/static/plugin/messenger/js/messenger-theme-future.js')
				).then(function(a, b, c) {
				 	_doAjax();
				});
			}else{
				_doAjax();
			}
		}
	}
})();

(function() {
	$(window).load(function () {
		// 取消loading
		YouGou.UI.stopLoading();
		// 根据url菜单反选
		YouGou.UI.menuInit();
        // 调试模式
        YouGou.Util.Debug();
	});
})();
