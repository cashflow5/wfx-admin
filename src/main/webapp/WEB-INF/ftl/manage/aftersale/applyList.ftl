<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/aftersale/applyList.js"></script>
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
		                        	<label for="wfxOrderNo" class="col-sm-1 control-label col-xs-12 no-padding-right">订单号：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="wfxOrderNo" id="wfxOrderNo" value="" type="text">
									</div>
		                        	<label for="buyerAccount" class="col-sm-1 control-label col-xs-12 no-padding-right">买家账号：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="buyerAccount" id="buyerAccount" value="" type="text">
									</div>
		                        	<label for="shopName" class="col-sm-1 control-label col-xs-12 no-padding-right">订单来源：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="shopName" id="shopName" value="" type="text">
									</div>
		                        	<label for="receiverName" class="col-sm-1 control-label col-xs-12 no-padding-right">收货人：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="receiverName" id="receiverName" value="" type="text">
									</div>
		                        </div>
		                        <div class="form-group">
		                        	<label for="receiverMobile" class="col-sm-1 control-label col-xs-12 no-padding-right">收货人电话：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="receiverMobile" id="receiverMobile" value="" type="text">
									</div>
		                        	<label for="supplierName" class="col-sm-1 control-label col-xs-12 no-padding-right">发货供应商：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="supplierName" id="supplierName" value="" type="text">
									</div>
		                        	<label for="createdTimeStart" class="col-sm-1 control-label col-xs-12 no-padding-right">下单时间：</label>
									<div class="col-sm-2">
									    <input class="input-medium" name="createdTimeStart" id="createdTimeStart" readonly="readonly" value="" type="text">
									</div>
		                        	<label for="createdTimeEnd" class="col-sm-1 control-label col-xs-12 no-padding-right">至：</label>
									<div class="col-sm-2">
									    <input class="input-medium" name="createdTimeEnd" id="createdTimeEnd" readonly="readonly" value="" type="text">
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