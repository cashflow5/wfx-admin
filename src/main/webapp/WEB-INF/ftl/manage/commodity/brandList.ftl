<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/commodity/brandList.js"></script>
	<script src="/static/plugin/zTree/js/jquery.ztree.all-3.5.min.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		<button class="btn btn-yougou" onclick="synBrand();">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	同步品牌
        </button>
        <div class="space-6"></div>
		<div class="row">
			<div class="col-xs-12">
				<!-- 搜索表单，需自定义 -->
				<form class="form-horizontal" id="searchForm">
		            <fieldset>
		                <div class="row">
		                    <div class="col-sm-12">
		                        <div class="form-group">
		                        	<label for="brandName" class="col-sm-1 control-label col-xs-12 no-padding-right">中文名称：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="brandName" id="brandName" value="" type="text">
									</div>
		                        	<label for="englishName" class="col-sm-1 control-label col-xs-12 no-padding-right">英文名称：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="englishName" id="englishName" value="" type="text">
									</div>
									<label for="status" class="col-sm-1 control-label col-xs-12 no-padding-right"> 状态：</label>
									<div class="col-sm-2">
							            <select class="form-control input-sm" name="status" id="status">
							                <option value="">请选择</option>
											<option value="1">启用</option>
											<option value="2">停用</option>
							            </select>
							        </div>
									<div class="col-sm-3">
			                            <input type="button" value="搜索" class="btn btn-sm btn-yougou" onclick="doQuery();"/>
									</div>
		                        </div>
		                    </div>
		                </div>
		            </fieldset>
	        	</form>
			</div>
		</div>
	</div>
	
	<div class="space-6"></div>
	<div class="col-xs-12">
		<table id="grid-table" class="mmg"></table>
	    <div id="grid-pager" style="text-align:right;" class=""></div>
    </div>
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>