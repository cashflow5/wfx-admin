<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/finance/sellerList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
        <div class="space-6"></div>
		<div class="row">
			<div class="col-xs-12">
				<form id="searchForm" class="form-horizontal" >
				    <div class="search-content-wrap">
				        <ul class="c-h-ul search-ul query-ul">
				            <li>
				                <label class="c-h-ul-li-title">分销商账号：</label>
				                <input type="text" class="form-control input-small" name="sellerAccount" id="sellerAccount" value="${(queryVo.sellerAccount)!''}"  />
				            </li>
				            <li>
				                <label class="c-h-ul-li-title">店铺编码：</label>
				                <input type="text" class="form-control input-small"  name="shopName" id="shopName" value="${(queryVo.shopName)!''}" />
				            </li>
				            <li>
				                <label class="c-h-ul-li-title">分销商姓名：</label>
				                <input type="text" class="form-control input-small"  name="sellerName" id="sellerName" value="${(queryVo.sellerName)!''}" />
				            </li>
				            <li>
							    <label class="c-h-ul-li-title">
				            		<input name="amountCheck" id="amountCheck" type="checkbox" <#if queryVo.amountCheckFlag ?? && queryVo.amountCheckFlag == 'TRUE'>checked</#if> />&nbsp;账户余额大于0
				            	</label>
							    <input type="hidden" name="amountCheckFlag" id="amountCheckFlag" value="${(queryVo.amountCheckFlag)!''}" />
				            </li>
				            <li>
				                <label class="c-h-ul-li-title"></label>
				                <input type="button" value="搜索" class="btn btn-sm btn-yougou" onclick="doQuery();"/>
				            </li>
				        </ul>
				    </div>
			    </form>
				<div class="space-6"></div>
				<div class="add_detail_box" style="text-align: right;">
					合计：&nbsp;
					账户余额：<font color="red">${dataMap['accountBalance']?default(0)?string('###,##0.00')}</font>元&nbsp;&nbsp;&nbsp;&nbsp;
					累计提现：<font color="red">${dataMap['cashedTotalAmount']?default(0)?string('###,##0.00')}</font>元&nbsp;&nbsp;&nbsp;&nbsp;
					提现中：<font color="red">${dataMap['cashingTotalAmount']?default(0)?string('###,##0.00')}</font>元&nbsp;&nbsp;&nbsp;&nbsp;
					佣金收入：<font color="red">${dataMap['commissionAllTotalAmount']?default(0)?string('###,##0.00')}</font>元&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div class="space-6"></div>
				
				<table id="grid-table" class="mmg"></table>
	    		<div id="grid-pager" style="text-align:right;" class=""></div>
			</div>
		</div>
	</div>
    
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>