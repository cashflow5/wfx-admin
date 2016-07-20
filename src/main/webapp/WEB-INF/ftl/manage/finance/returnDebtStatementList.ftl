<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/finance/returnDebtStatementList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
        <div class="tabbable">
        	<ul class="nav nav-tabs" id="myTab">
				<li class="active"><a data-toggle="tab" href="#returnStatementList" aria-expanded="true">退款报表</a></li>
			</ul>
        </div>
        <div class="tab-content">
        	<div id="returnStatementList" class="tab-pane fade active in">
        		<!-- 搜索表单，需自定义 -->
				<form class="form-horizontal" id="searchForm" action="" method="post">
		            <fieldset>
		                <div class="row">
		                    <div class="col-sm-12">
		                        <div class="form-group">
		                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">分销商编码：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="storeName" id="storeName" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">退款时间：</label>
									<div class="col-sm-4">
									    <input type="text" name="refundStartTime" id="refundStartTime" readonly="readonly" value="" class="input-medium" style="width:120px !important">
									    <label>至</label>
									    <input type="text" name="refundEndTime" id="refundEndTime" readonly="readonly" value="" class="input-medium" style="width:120px !important">
									</div>
		                        </div>
		                        <div class="form-group">
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">客户名称：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="customerName" id="customerName" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">订单号：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="orderNo" id="orderNo" value="" type="text">
									</div>
									<div class="col-sm-3">
			                            <input type="button" value="搜索" class="btn btn-sm btn-yougou" onclick="doQuery();"/>
			                            <input type="button" value="导出" class="btn btn-sm btn-yougou" onclick="exportExcel();"/>
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
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>