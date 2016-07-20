// 操作列动作
var actionFixed = function(val,item,rowIndex){
	var html = [];
	html.push('<a href="javascript:void(0);" action="add">添加</a>&nbsp;&nbsp;');
    return html.join('');
};

var titleFixed = function(val,item){
	var html = '';
	html += '<a href="/discover/articleList.sc?id='+item.id+'&type=view">'+val+'</a>';
	return html;
};

var authorFixed = function(val,item){
	var str = '优购微零售';
	if('2' == item.authorType){
		str = val;
	}
	return str;
}

// 列集合
var cols = [
	{ title:'id', name:'no', align:'center'},
	{ title:'标题', name:'title', align:'left'},
	{ title:'作者', name:'authorAccount', width:90,align:'left',renderer:authorFixed},
	{ title:'更新时间', name:'updateTime',width:130, align:'center',lockWidth:true, renderer: YouGou.Util.timeFixed},
	{ title:'所属频道', name:'channelName', align:'left'},
	{ title:'操作', name:'' ,width:200, align:'center',lockWidth:true, renderer: actionFixed},
    { title:'ID', name:'id', hidden: true}
];

//分页器
var mmPaginator = $('#article-grid-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("articleForm");

// 表格	
var articleMmGrid = $('#article-grid-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/discover/queryArticleSelectedData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
articleMmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    if(action == "add"){// 选中
   		selectArticle(item.no,item.title,item.authorType,item.authorAccount);
    }
    e.stopPropagation();  //阻止事件冒泡
});
//生成跳转链接
function buildRedirectUrl(no){
	YouGou.Ajax.doPost({
		tips : false,
		successMsg: '修改成功!',
		url : "/notice/buildRedirectUrl.sc",
		data : {"redirectType" : 3,"value": no},
	  	success : function(data){
	  		if(data.msg=="success"){
	  			//将此值赋值到跳转链接文本框
	  			var redirectUrl=data.data;
	  			$("#redirectUrl").val(redirectUrl);
	  			$("#errorMsg").html('');
	  		}else{
	  			//错误信息用红字显示出来
	  			var error=data.msg;
	  			$("#errorMsg").html(error);
	  		}
		}
	});
}

function selectArticle(no,title,type,account){
	var authorAccount = '优购微零售';
	if(type == 2){
		authorAccount = account;
	}
	$('#articleNo').val(no);
	var tips = '《'+title+'》  by  ' + authorAccount;
	$('#articleTips').text(tips);
	//生成链接（用于公告，轮播图）
	buildRedirectUrl(no);
	$('button.articleClose').click();
}

//查询
function doQuery(){
	articleMmGrid.load();
}