<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/order/orderList.js"></script>
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
		                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">订单号：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="wfxOrderNo" id="wfxOrderNo" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">买家账号：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="buyerAccount" id="buyerAccount" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">订单来源：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="shopName" id="shopName" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">订单状态：</label>
									<div class="col-sm-2">							
									    <select id="status" name="status">
									    	<option value="" selected="selected">请选择</option>
									    	<#if orderStatus?? >
									    		<#list orderStatus as status>
									    			<option value="${(status.key)!''}" >${(status.desc)!''}</option>
												</#list>
									    	</#if>
									    </select>
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">收货人：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="receiverName" id="receiverName" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">下单时间：</label>
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