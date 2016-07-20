!function($){
    var WFXLocalPaginator = function(element, options){
        this.$el = $(element);
        this.opts = options;
    };

    WFXLocalPaginator.prototype = {
        _initLayout: function(){
            var that = this;
            var $el = this.$el;
            var opts = this.opts;

            $el.addClass("mmPaginator");
            var pgHtmls = [
                '<div class="totalCountLabel"></div>',
                '<ul class="pageList"></ul>',
                '<div class="limit"><select></select></div>'
            ];
            $el.append($(pgHtmls.join('')));

            this.$totalCountLabel = $el.find('.totalCountLabel');
            this.$pageList = $el.find('.pageList');
            this.$limitList = $el.find('.limit select');

            var $limitList = this.$limitList;
            var $option = $('<option value="'+ opts.limit +'">每页'+opts.limit+'条</option>');
            $limitList.append($option);

            $limitList.on('change', function(){
            	$el.data('page', 1);
            	var limit = $el.find('.limit select').val();
            	$el.data('limit', limit);
                that.search();
                opts.limit = limit;
                $el.data('limit', limit);
                that.init();
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
//            var $prev = $('<li class="prev"><a>«</a></li>');
//            if(page<=1){
//                $prev.addClass('disable');
//            }else{
//                $prev.find('a').on('click', function(){
//                    $el.data('page', page-1);
//                    that.search();
//                });
//            }
//            $pageList.append($prev);
            /////
            var list = [];
//            if(page > 4 ){
//                list.push('...');
//            }
//            for(var i= 0; i < 5; i++){
//                var no = page - 2 + i;
//                if(no > 1 && no <= totalPage-1){
//                    list.push(no);
//                }
//            }
//            if(page+1 < totalPage-1){
//                list.push('...');
//            }
//            if(totalPage>1){
//                list.push(totalPage);
//            }
            for(var i=1;i<=totalPage;i++){
            	list.push(i);
            }
            $.each(list, function(index, item){
                var $li = $('<li><a></a></li>');
                if(item === '...'){
                    $li.addClass('').html('...');
                }else{
                	if(item === page){
                        $li.addClass('active').find('a').text(item);
                    }
                    $li.find('a').text(item).prop('title','第'+item+'页').on('click', function(e){
                        $el.data('page', item);
                        that.search();
                        $li.siblings().removeClass('active');
                        $li.addClass('active');
                    });
                }
                $pageList.append($li);
            });
            //
//            var $next = $('<li class="next"><a title="下一页">»</a></li>');
//            if(page>=totalPage){
//                $next.addClass('disable');
//            }else{
//                $next.find('a').on('click', function(){
//                    $el.data('page', page+1);
//                    that.search();
//                });
//            }
            //$pageList.append($next);
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

            this.$totalCountLabel.html(this.formatString(opts.totalCountLabel,[totalCount]));
            this.$pageList.empty();

            this._plain(page, totalCount, this.$limitList.val());
            
            var pageData = [];
            var mmGrid = opts.mmGrid;
            var start = (page - 1) * limit;
            for (var i = start; i < (start+limit) && i<= totalCount-1; i++) {
            	pageData.push(opts.items[i]);
            }
            mmGrid.load(pageData);
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
            
            this._initLayout();

            var params = {};
            params[opts.totalCountName] = opts.totalCount;
            params[opts.pageParamName] = opts.page;
            params[opts.limitParamName] = opts.limit;
            this.load(params);
            return this;
        }
        , search : function(){
        	var that = this;
        	var $el = this.$el;
        	var opts = that.opts;
        	var page = parseInt($el.data('page'));
        	var limit = parseInt($el.find('.limit select').val());
        	
        	var pageData = [];
            var mmGrid = opts.mmGrid;
            var start = (page - 1) * limit;
            for (var i = start; i < (start+limit) && i<= opts.totalCount-1; i++) {
            	pageData.push(opts.items[i]);
            }
            mmGrid.load(pageData);
        }
        , update : function(items){
        	var that = this;
        	var $el = this.$el;
        	var opts = that.opts;
        	opts.items = items;
        	opts.totalCount = items.length;
        	opts.page = 1;
        	$el.html("");
        	this.init();
        }
    };

    $.fn.wfxLocalPaginator = function(){

        if(arguments.length === 0 || typeof arguments[0] === 'object'){
            var option = arguments[0]
                , data = this.data('mmPaginator')
                , options = $.extend(true, {}, $.fn.wfxLocalPaginator.defaults, option);
            if (!data) {
                data = new WFXLocalPaginator(this[0], options);
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

    $.fn.wfxLocalPaginator.defaults = {
         style: 'plain'
        , totalCountName: 'totalCount'
        , page: 1
        , pageParamName: 'page'
        , limitParamName: 'limit'
        , limitLabel: '每页{0}条'
        , totalCountLabel: '共<span>{0}</span>条记录'
        , limit: 0
        , limitList: [15, 20, 30, 40, 50]
    	, totalCount :  0
    	, mmGrid : null
    	, items : null
    };

    $.fn.wfxLocalPaginator.Constructor = WFXLocalPaginator;

}(window.jQuery);