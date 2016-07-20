<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/seller/sellerAuditList.js"></script>
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
		                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">会员账号：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="loginName" id="loginName" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">会员姓名：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="sellerName" id="sellerName" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">订单总数：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="orderCountStart" id="orderCountStart" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">至</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="orderCountEnd" id="orderCountEnd" value="" type="text">
									</div>
							
		                        </div>
		                        <div class="form-group">
		                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">订单金额：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="orderAmountStart" id="orderAmountStart" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">至</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="orderAmountEnd" id="orderAmountEnd" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">申请时间：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="createTimeStart" id="createTimeStart" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">至</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="createTimeEnd" id="createTimeEnd" value="" type="text">
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