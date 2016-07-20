<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/finance/commissionDetailList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		<div class="tabbable">
		<ul class="nav nav-tabs" id="myTab">
		    <!-- <li class="active"><a data-toggle="tab" href="" aria-expanded=true>未结算</a></li>
			<li class=""><a data-toggle="tab" href="" aria-expanded="false">已结算</a></li>
			<li class=""><a data-toggle="tab" href="" aria-expanded="false">异常挂起</a></li> -->
			
		   <li  <#if status=='0'>class="active"</#if> ><a href="/finance/commissionDetailList.sc?status=0" >未结算</a></li>
		   <li  <#if status=='1'>class="active"</#if> ><a href="/finance/commissionDetailList.sc?status=1">已结算</a></li>
		   <li  <#if status=='2'>class="active"</#if> ><a href="/finance/commissionDetailList.sc?status=2">异常挂起</a></li>
		</ul>
		</ul>
		<div class="tab-content">
			<form class="form-horizontal" id="searchForm">
			 <input name="status" id="status" value="${status?default('0')}" type="hidden">
	            <fieldset>
	                <div class="row">
	                    <div class="col-sm-12">
	                        <div class="form-group">
	                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">分销商账号：</label>
								<div class="col-sm-2">
								    <input class="form-control input-sm" name="loginName" id="loginName" value="" type="text">
								</div>
								<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">收货/退货时间：</label>
								<div class="col-sm-4">
								    <input type="text" name="confirmStartTime" id="confirmStartTime" readonly="readonly" value="" class="input-medium" style="width:125px !important">
								    <label>至</label>
								    <input type="text" name="confirmEndTime" id="confirmEndTime" readonly="readonly" value="" class="input-medium" style="width:125px !important">
								</div>
								<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">订单号：</label>
								<div class="col-sm-2">
								    <input class="form-control input-sm" name="wfxOrderDetailNo" id="wfxOrderDetailNo" value="" type="text">
								</div>
							</div>
							<div class="form-group">
								<label for="memberLevel" class="col-sm-1 control-label col-xs-12 no-padding-right">结算类型：</label>
								<div class="col-sm-2">
								    <select class="form-control input-sm" name="settlementType" id="settlementType">
						                <option value="">全部</option>
						                <option value="0">妥投结算</option>
						                <option value="1">退货结算</option>
						            </select>
								</div>
								<#if status=='2'>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">操作时间：</label>
								<#else>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">结算时间：</label>
								</#if>
								<div class="col-sm-4">
								    <input type="text" name="settleStartTime" id="settleStartTime" readonly="readonly" value="" class="input-medium" style="width:125px !important">
								    <label>至</label>
								    <input type="text" name="settleEndTime" id="settleEndTime" readonly="readonly" value="" class="input-medium" style="width:125px !important">
								</div>
								<#if status=='1'>
								<label for="memberLevel" class="col-sm-1 control-label col-xs-12 no-padding-right">结算状态：</label>
								<div class="col-sm-2"><!-- 已结算（含1已结算、3已关闭） -->
								    <select class="form-control input-sm" name="dealType" id="dealType">
						                <option value="9">全部</option>
						                <option value="1">已结算</option>
						                <option value="2">已关闭</option>
						            </select>
								</div>
								</#if>
								<div class="col-sm-4">
		                            <input type="button" value="搜索" class="btn btn-sm btn-yougou" onclick="doQuery();"/>
		                            <input type="button" value="导出" class="btn btn-sm btn-yougou" onclick="exportCommissionDetail();"/>
								</div>
							</div>
	                    </div>
	                </div>
	            </fieldset>
        	</form>
		</div>
	</div>
	</div>
	<div class="space-1"></div>&nbsp;
	<div class="col-xs-12">
		<table id="grid-table" class="mmg"></table>
	    <div id="grid-pager" style="text-align:right;" class=""></div>
	    <div id="grid-pager1" style="text-align:right;" class=""></div>
    </div>
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>