<@compress single_line=compress_single_line?contains("true")>
<#assign head>
	<script>
		var baseUrl = '${baseUrl}';
	</script>
	<style>
		.hotCatPic{height:36px;width:auto;margin:5px;}
		.hotSnGroup .hotSnText{display:inline-block;width:60px;text-align:right;}
		.hotSnGroup .hotSnInput{width:40px;text-align:right;}
		.hotSnGroup a{display:inline-block;width:16px;margin-left:10px;}
		.hotSnGroup a:hover{cursor:pointer;}
		.hotSnGroup .editIcon{background:transparent url(/static/images/icon_edit.png) no-repeat center;}
		.hotSnGroup .saveIcon{background:transparent url(/static/images/icon_save.png) no-repeat center;}
		.hotSnGroup .cancelIcon{background:transparent url(/static/images/del-class.gif) no-repeat center;}
	</style>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/cms/hotCatList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		<button class="btn btn-sm btn-yougou" onclick="showTable();">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	新增
        </button>&nbsp;&nbsp;
        <button class="btn btn-sm btn-yougou" onclick="batchRemove();">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	批量删除
        </button>
        <div class="space-6"></div>
		<div class="row">
			<div class="col-xs-12">
				<div class="tab-content">
				<!-- 搜索表单，需自定义 -->
				<form class="form-horizontal" id="searchForm1">
		            <fieldset>
		                <div class="row">
		                    <div class="col-sm-12">
		                        <div class="form-group">
		                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">分类名称：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="name" id="saleName" value="${name!''}" type="text">
									</div>
									<div class="col-sm-3">
			                            <input type="button" value="查询" class="btn btn-sm btn-yougou" onclick="doQuery();"/>
									</div>
		                        </div>
		                    </div>
		                </div>
		            </fieldset>
	        	</form>
			</div>
			</div>
		</div>
	</div>
	
	<div class="space-6"></div>
	<div class="col-xs-12">
		<table id="grid-table" class="mmg"></table>
	    <div id="grid-pager1" style="text-align:right;" class=""></div>
    </div>
</div>

<div id="saleCatTable" class="hide row">
	<div class="col-xs-12">
		<button class="btn btn-sm btn-yougou" onclick="batchAdd();">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	批量添加
        </button>
        <button class="btn btn-sm btn-yougou" onclick="hideForm();">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	返回
        </button>
		<div class="row">
			<div class="col-xs-12">
				<div class="tab-content">
				<!-- 搜索表单，需自定义 -->
				<form class="form-horizontal" id="searchForm2">
		            <fieldset>
		                <div class="row">
		                    <div class="col-sm-12">
		                        <div class="form-group">
		                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">分类名称：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="name" id="name" value="${name!''}" type="text">
									</div>
									<div class="col-sm-3">
			                            <input type="button" value="查询" class="btn btn-sm btn-yougou" onclick="saleCatDoQuery();"/>
									</div>
		                        </div>
		                    </div>
		                </div>
		            </fieldset>
	        	</form>
			</div>
			</div>
		</div>
	</div>
	
	<div class="space-6"></div>
	<div class="col-xs-12">
		<table id="saleCat-table" class="mmg"></table>
	    <div id="grid-pager2" style="text-align:right;" class=""></div>
    </div>
</div>

</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>