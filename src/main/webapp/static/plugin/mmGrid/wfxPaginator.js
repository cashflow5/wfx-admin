!function($){
    var WFXPaginator = function(element, options){
        this.$el = $(element);
        this.opts = options;
    };

    WFXPaginator.prototype = {
        _initLayout: function(){
            var that = this;
            var $el = this.$el;
            var opts = this.opts;

            $el.addClass("mmPaginator");
            var pgHtmls = [
                '<div class="totalCountLabel"></div>',
                '<ul class="pageList"></ul>',
                '<div class="gotopage">到第<input type="text" size="2" class="pageInput" style="height:28px;text-align:center;"/>页<input type="button" class="btn btn-xs btn-yougou btnToPage" value="确定"/></div><div class="limit"><select></select></div>'
            ];
            $el.append($(pgHtmls.join('')));

            this.$totalCountLabel = $el.find('.totalCountLabel');
            this.$pageList = $el.find('.pageList');
            this.$limitList = $el.find('.limit select');

             var $limitList = this.$limitList
            $.each(opts.limitList, function(){
                var $option = $('<option></option>')
                    .prop('value',this)
                    .text(that.formatString(opts.limitLabel,[this]));
                $limitList.append($option);
            });

            $limitList.on('change', function(){
            	$el.data('page', 1);
            	var limit = $el.find('.limit select').val();
            	$el.data('limit', limit);
                that.search();
            });

        }

        , _plain: function(page, totalCount, limit){
            var that = this;
            var $el = this.$el;
            var $pageList = this.$pageList;

            var totalPage = totalCount % limit === 0 ? parseInt(totalCount/limit) : parseInt(totalCount/limit) + 1;
            totalPage = totalPage ? totalPage : 0;
            if(totalPage === 0){
                page = 1;
            }else if(page > totalPage){
                page = totalPage;
            }else if(page < 1 && totalPage != 0){
                page = 1;
            }
            //
            var $prev = $('<li class="prev"><a>«</a></li>');
            if(page<=1){
                $prev.addClass('disable');
            }else{
                $prev.find('a').on('click', function(){
                    $el.data('page', page-1);
                    that.search();
                });
            }
            $pageList.append($prev);
            /////
            var list = [1];
            if(page > 4 ){
                list.push('...');
            }
            for(var i= 0; i < 5; i++){
                var no = page - 2 + i;
                if(no > 1 && no <= totalPage-1){
                    list.push(no);
                }
            }
            if(page+1 < totalPage-1){
                list.push('...');
            }
            if(totalPage>1){
                list.push(totalPage);
            }
            $.each(list, function(index, item){
                var $li = $('<li><a></a></li>');
                if(item === '...'){
                    $li.addClass('').html('<span>...</span>');
                }else if(item === page){
                    $li.addClass('active').find('a').text(item);
                }else{
                    $li.find('a').text(item).prop('title','第'+item+'页').on('click', function(e){
                        $el.data('page', item);
                        that.search();
                    });
                }
                $pageList.append($li);
            });
            //
            var $next = $('<li class="next"><a title="下一页">»</a></li>');
            if(page>=totalPage){
                $next.addClass('disable');
            }else{
                $next.find('a').on('click', function(){
                    $el.data('page', page+1);
                    that.search();
                });
            }
            
            $pageList.append($next);
            
            $el.find('.btnToPage').click(function(){
            	var page = $(this).prev().val();
            	if(!isNaN(page)){
            		page = parseInt(page);
            		if(page <=0 ){
            			YouGou.UI.Dialog.autoCloseTip("页码不能小于1");
            			return false;
            		}
            		
            		if(page > 0 && page <= totalPage){
            			$el.data('page', page);
            			that.search();
            		}else if(page > totalPage){
            			$el.data('page', totalPage);
            			that.search();
            		}
            	}
            });
        }

        , _search: function(page, totalCount, limit){

        }

        , load: function(params){
            var $el = this.$el;
            var $limitList = this.$limitList;
            var opts = this.opts;
            var that = this;

            var page = params[opts.pageParamName];
            if(page === undefined || page === null){
                page = $el.data('page');
            }
            $el.data('page', page);

            var totalCount = params[opts.totalCountName];
            
            if(totalCount === undefined){
                totalCount = 0;
            }
            $el.data('totalCount', totalCount);

            var limit = params[opts.limitParamName];
            if(!limit){
                limit = $limitList.val();
            }
            this.$limitList.val(limit);

            var totalPage = totalCount % limit === 0 ? parseInt(totalCount/limit) : parseInt(totalCount/limit) + 1;
            this.$totalCountLabel.html(this.formatString(opts.totalCountLabel,[totalCount,totalPage ? totalPage : 0]));

            this.$pageList.empty();

            this._plain(page, totalCount, this.$limitList.val());
            
            try{
        	    // 回车查询
        	    $('#'+ opts.formId +' input').keypress(function(e) {
        	        if (e.which == 13){
        	        	that.search();
        	            return false;
        	        }
        	    });
        	}catch(e){}
        }

        , formatString: function(text, args){
            return text.replace(/{(\d+)}/g, function(match, number) {
                return typeof args[number] != 'undefined'
                    ? args[number]
                    : match
                    ;
            });
        }

        , params: function(){
            var opts = this.opts;
            var $el = this.$el;
            var $limitList = this.$limitList;

            var params = {};
            params[opts.pageParamName] = $el.data('page');
            params[opts.limitParamName] = $limitList.val();
            params[opts.totalCountName] = $el.data('totalCount');
            return params;
        }

        , init: function(){
            var that = this;
            var opts = that.opts;
            
            if(YouGou.Util.isEmpty(opts.formId)){
           		YouGou.UI.Dialog.alert({message:"formId未设置"});
           		return;
           	}
            
            this._initLayout();

            var params = {};
            params[opts.totalCountName] = opts.totalCount;
            params[opts.pageParamName] = opts.page;
            params[opts.limitParamName] = opts.limit;
            this.load(params);
        }
        , search : function(){
        	var that = this;
        	var $el = this.$el;
        	var opts = that.opts;
        	var page = $el.data('page');
        	var limit = $el.find('.limit select').val();
        	var formId = opts.formId;
        	
          	var param = {};
          	// 获取form参数array
          	var formParam = $("#"+formId).serializeArray();
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
          	// form条件
          	var paramstring = "";
          	for (var i=0; i < formParam.length; i++){
          		var obj = formParam[i];
          		if(!YouGou.Util.isEmpty(obj.name)){
        	  		var attrObj = eval("({'"+ obj.name +"':'"+ obj.value.trim() +"'})");
        	  		if(i == 0){
        	  			paramstring += "?";
        	  		}
        	  		paramstring += obj.name + "=" + obj.value.trim() + "&";
        	  		$.extend(param, attrObj);
          		}
          	}
          	// 分页条件
          	paramstring += "page="+page+"&limit="+limit;
          	
          	var form = $("#"+formId);
          	var actionUrl = form.attr("action");
          	var method = form.attr("method") || "post";
          	if(YouGou.Util.isEmpty(actionUrl)){
          		YouGou.UI.Dialog.alert({message:"form action未设置"});
          		return;
          	}
          	if(method == "get" || method == "GET"){
          		window.location.href = actionUrl + paramstring;
          	}else{
          		form.append("<input name='page' value='"+ page +"'/>");
          		form.append("<input name='limit' value='"+ limit +"'/>");
          		form.submit();
          	}
        }
    };

    $.fn.wfxPaginator = function(){

        if(arguments.length === 0 || typeof arguments[0] === 'object'){
            var option = arguments[0]
                , data = this.data('mmPaginator')
                , options = $.extend(true, {}, $.fn.wfxPaginator.defaults, option);
            if (!data) {
                data = new WFXPaginator(this[0], options);
                this.data('mmPaginator', data);
            }
            return $.extend(true, this, data);
        }
        if(typeof arguments[0] === 'string'){
            var data = this.data('mmPaginator');
            var fn =  data[arguments[0]];
            if(fn){
                var args = Array.prototype.slice.call(arguments);
                return fn.apply(data,args.slice(1));
            }
        }
    };

    $.fn.wfxPaginator.defaults = {
         style: 'plain'
        , totalCountName: 'totalCount'
        , page: 1
        , pageParamName: 'page'
        , limitParamName: 'limit'
        , limitLabel: '每页{0}条'
        , totalCountLabel: '共<span>{0}</span>条记录，共<span>{1}</span>页'
        , limit: 0
        , limitList: [15, 20, 30, 40, 50]
    	, totalCount :  0
    	, formId : ''
    };

    $.fn.wfxPaginator.Constructor = WFXPaginator;

}(window.jQuery);