<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/shop/shopList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		
        <div class="space-6"></div>
		<div class="row">
			<div class="col-xs-12">
				<!-- 搜索表单，需自定义 -->
				<form class="form-horizontal" id="searchForm">
		            <fieldset>
		                <div class="row">
		                    <div class="col-sm-12">
		                        <div class="form-group">
		                        	<label for="name" class="col-sm-1 control-label col-xs-12 no-padding-right">店铺名称：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="name" id="name" value="" type="text">
									</div>
									<label for="loginName" class="col-sm-1 control-label col-xs-12 no-padding-right">分销商账号：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="loginName" id="loginName" value="" type="text">
									</div>
									<label for="createTimeStart" class="col-sm-1 control-label col-xs-12 no-padding-right">创建时间：</label>
									<div class="col-sm-2">
									    <input name="createTimeStart" id="createTimeStart" readonly="readonly" value="" class="input-medium" type="text">
									</div>
									<label for="createTimeEnd" class="col-sm-1 control-label col-xs-12 no-padding-right">至</label>
									<div class="col-sm-2">
									    <input name="createTimeEnd" id="createTimeEnd" readonly="readonly" value="" class="input-medium" type="text">
									</div>
								</div>
								<div class="form-group">
									<label for="operateType" class="col-sm-1 control-label col-xs-12 no-padding-right">店铺状态：</label>
										<div class="col-sm-2">
									    <select class="form-control input-sm" name="status">
									    	<option value="">请选择</option>
							                <option value="1">开启</option>
							                <option value="2">关闭</option>
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