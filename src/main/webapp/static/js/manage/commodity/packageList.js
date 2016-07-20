/********************************* 分销包 ************************************/
J('#startTime').calendar({maxDate:'#endTime',format:'yyyy-MM-dd HH:mm:ss'});
J('#endTime').calendar({minDate:'#startTime',format:'yyyy-MM-dd HH:mm:ss'});

function doQuery(){
	mmGrid.load();
}

var renderer_sort = function(val,item,rowIndex){
	var html = [];
	html.push('<span class="fa fa-minus"></span>&nbsp;&nbsp;');
	html.push('<input type="text" value="'+val+'" size="2" readonly="true">');
	html.push('&nbsp;&nbsp;<span class="fa fa-plus"></span>');
	return html.join('');
};
//分销包状态
var renderer_status = function(val,item,rowIndex){
	var status = parseInt(val);
    if(status == 1){
		return '待审核';
	}else if(status == 2){
		return '审核拒绝';
	}else if(status == 3){
		return '已发布';
	}else if(status == 4){
		return '已取消';
	}
};

//操作列
var renderer_action = function(val,item,rowIndex){
	var html = [];
	if(item.status == 1){
		html.push('<a href="javascript:void(0);" action="audit">审核</a>&nbsp;&nbsp;');
		html.push('<a href="javascript:void(0);" action="edit">编辑</a>&nbsp;&nbsp;');
		html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
		html.push('<a href="javascript:void(0);" action="view">查看</a>&nbsp;&nbsp;');
	}else if(item.status == 2){
		html.push('<a href="javascript:void(0);" action="edit">编辑</a>&nbsp;&nbsp;');
		html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
		html.push('<a href="javascript:void(0);" action="view">查看</a>&nbsp;&nbsp;');
	}else if(item.status == 3){
		html.push('<a href="javascript:void(0);" action="cancle">取消</a>&nbsp;&nbsp;');
		html.push('<a href="javascript:void(0);" action="view">查看</a>&nbsp;&nbsp;');
	}else if(item.status == 4){
		html.push('<a href="javascript:void(0);" action="edit">编辑</a>&nbsp;&nbsp;');
		html.push('<a href="javascript:void(0);" action="delete">删除</a>&nbsp;&nbsp;');
		html.push('<a href="javascript:void(0);" action="view">查看</a>&nbsp;&nbsp;');
	}
    return html.join('');
};
//列集合
var cols = [
    { title:'分销包名称', name:'bagName', align:'left'},
    { title:'包含商品数', name:'commodityNum', lockWidth:true, width:80, align:'center'},
	{ title:'排序', sortable: false, sortName:'sortNo', name:'sortNo' , width:60, align:'center', lockDisplay: true, renderer: renderer_sort},
    { title:'最后更新时间', name:'updateTime' ,width:135, lockWidth:true, align:'center', renderer : YouGou.Util.timeFixed},
	{ title:'最后更新人', name:'updateUser' ,width:85,lockWidth:true, align:'center'},
    { title:'状态', name:'status' ,width:80, align:'center', lockWidth:true, lockDisplay: true, renderer: renderer_status},
    { title:'操作', name:'' ,width:200, align:'center',lockWidth:true, renderer: renderer_action},
    { title:'ID', name:'id', hidden: true, lockDisplay:true}
];
//分页器
var mmPaginator = $('#package-pager').mmPaginator({});
// 搜索表单属性
var mmFormParams = new MMSearchFormParams("searchPackageForm");
// 表格	
var mmGrid = $('#package-table').mmGrid({
	height: 'auto',
	cols: cols,
	url: '/commodity/queryPackageListData.sc',
	fullWidthRows: true,
	autoLoad: true,
	plugins: [mmPaginator,mmFormParams]
});

// 表格事件
mmGrid.on('cellSelected', function(e, item, rowIndex, colIndex){
	var action = $(e.target).attr("action");
    if(action == "audit"){
    	//审核
    	YouGou.UI.Dialog.confirm({
   			message : "确认审核【"+ item.bagName +"】分销包吗？"
   		},function(result){
   			if(result) {
   				YouGou.Ajax.doPost({
   	    			url : '/commodity/updatePackageData.sc',
   	    			data : {"id": item.id, "status": 3},
   	    			success : function(data) {
   	    				mmGrid.load();
   	    			}
   	    		 });
            }else{
            	YouGou.Ajax.doPost({
   	    			url : '/commodity/updatePackageData.sc',
   	    			data : {"id": item.id, "status": 2},
   	    			success : function(data) {
   	    				mmGrid.load();
   	    			}
   	    		 });
            }
   		});
    } else if(action == "edit"){
    	//编辑
    	window.location.href="/commodity/packageInfo.sc?id=" + item.id + "&type=edit";
    } else if(action == "delete"){
    	//删除
   		YouGou.UI.Dialog.confirm({
   			message : "确认删除名为【"+ item.bagName +"】分销包吗？"
   		},function(result){
   			if(result) {
   				YouGou.Ajax.doPost({
   					successMsg: '分销包删除成功!',
   					url: '/commodity/deletePackageData.sc',
   				  	data: {id:item.id},
   				  	success : function(data){
   				  		mmGrid.load();
   					}
   				});
            }
   		});
    } else if(action == "cancle"){
    	//取消
   		YouGou.UI.Dialog.confirm({
   			message : "确认取消名为【"+ item.bagName +"】分销包吗？"
   		},function(result){
   			if(result) {
   				YouGou.Ajax.doPost({
   	    			url : '/commodity/updatePackageData.sc',
   	    			data : {"id": item.id, "status": 4},
   	    			success : function(data) {
   	    				mmGrid.load();
   	    			}
   	    		 });
            }
   		});
    } else if(action == "view"){
    	//查看
    	window.location.href="/commodity/packageInfo.sc?id=" + item.id + "&type=view";
    } else if(colIndex == 2){
    	var sortObj = $(e.target).siblings().filter('input');
    	if(sortObj.length == 1){
    		var sort = parseInt(sortObj.val());
    		if ($(e.target).hasClass('fa-plus')) {
    			sortObj.val(sort + 1);
    		} else if ($(e.target).hasClass('fa-minus')) {
    			sort = sort - 1;
    			sortObj.val(sort);
    		}
    		YouGou.Ajax.doPost({
    			url : '/commodity/updatePackageData.sc',
    			data : {"id": item.id, "sortNo": sortObj.val()},
    			success : function(data) {
    				mmGrid.load();
    			}
    		 });
    	}
    }
    e.stopPropagation();  //阻止事件冒泡
}).on('loadSuccess', function(e, data){
	$('.fa-minus,.fa-plus').hover(function(){
		$(this).css("cursor","pointer");
	});
});

$('#minus,#plus').click(function() {
	var sort = "";
	if ($(this).hasClass('fa-plus')) {
		var val = $('#sortNo').val();
		sort = parseInt(val) + 1;
		$('#sortNo').val(sort);
	}
	if ($(this).hasClass('fa-minus')) {
		var val = $('#sortNo').val();
		if (parseInt(val) > 0) {
			sort = parseInt(val) - 1;
			$('#sortNo').val(sort);
		}
	}
});


//新增分销包
$('a.add-package').click(function() {
	window.location.href="/commodity/packageInfo.sc?type=add";
});




