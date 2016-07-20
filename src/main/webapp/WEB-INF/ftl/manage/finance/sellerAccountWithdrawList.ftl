<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/finance/sellerAccountWithdrawList.js"></script>
</#assign>

<#assign body>
<div class="row" id="girdContent">
	<div class="col-xs-12">
		<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab">
				<li class="active"><a href="javascript:void(0);">待客服审核</a></li>
			</ul>
			<div class="tab-content">
				<div id="accountWithdrawList" class="tab-pane fade active in">
					<form class="form-horizontal" id="searchForm">
					<input name="billStatus" id="billStatus" value="${billStatus?default('')}" type="hidden">	
		             <fieldset>
		                <div class="row">
		                    <div class="col-sm-12">
		                        <div class="form-group">
		                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">分销商账号：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="sellerAccount" id="sellerAccount" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">申请单号：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="withdrawApplyNo" id="withdrawApplyNo" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">申请时间：</label>
									<div class="col-sm-4">
					                     <input type="text" id="applyTimeStart" name="applyTimeStart" readonly="readonly" class="input-medium" style="width:125px !important"/>
					                     <label>至</label>
					                     <input type="text" id="applyTimeEnd" name="applyTimeEnd" readonly="readonly" class="input-medium" style="width:125px !important"/>
					                </div>
		                        </div>
		                        <div class="form-group">
		                        	<div class="col-sm-3">
			                            <input type="button" value="搜索" class="btn btn-sm btn-yougou" onclick="doQuery();"/>
			                            <input type="button" value="导出" class="btn btn-sm btn-yougou" onclick="exportDetailExcel();"/>
									</div>
		                        </div>
		                    </div>
		                </div>
		             </fieldset>
		        	</form>
		        	<div class="blank20"></div>
					<table id="grid-table" class="mmg"></table>
			        <div id="grid-pager" style="text-align:right;" class=""></div>
				</div>
				
			</div>
		</div>
	</div>
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>