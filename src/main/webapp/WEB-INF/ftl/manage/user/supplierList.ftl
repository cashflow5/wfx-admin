<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/user/supplierList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		<div class="row">
			<div class="col-xs-12">
				<div class="tab-content">
				<!-- 搜索表单，需自定义 -->
				<form class="form-horizontal" id="searchForm">
		            <fieldset>
		                <div class="row">
		                    <div class="col-sm-12">
		                        <div class="form-group">
		                        	<label for="supplierName" class="col-sm-1 control-label col-xs-12 no-padding-right">供货商名称：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="supplierName" id="supplierName" value="" type="text">
									</div>
		                        	<label for="supplierCode" class="col-sm-1 control-label col-xs-12 no-padding-right">供货商编码：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="supplierCode" id="supplierCode" value="" type="text">
									</div>
		                        	<label for="createDateStart" class="col-sm-1 control-label col-xs-12 no-padding-right">创建时间：</label>
									<div class="col-sm-2">
									    <input class="input-medium" name="createDateStart" id="createDateStart" readonly="readonly" value="" type="text">
									</div>
		                        	<label for="createDateEnd" class="col-sm-1 control-label col-xs-12 no-padding-right">至：</label>
									<div class="col-sm-2">
									    <input class="input-medium" name="createDateEnd" id="createDateEnd" readonly="readonly" value="" type="text">
									</div>
		                        </div>
		                        <div class="form-group">
		                        	<div class="col-sm-6">
			                            <input type="button" value="查询" class="pull-right btn btn-sm btn-yougou" onclick="doQuery();"/>
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
	    <div id="grid-pager" style="text-align:right;" class=""></div>
    </div>
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>