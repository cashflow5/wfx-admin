
<div id="girdContent" class="row" style="width:1245px">
	<div class="col-xs-12">
	<form name="queryForm" id="queryForm" method="post" action="" class="form-horizontal" >
		<div class="search-content-wrap">
			<div class="add_detail_box">
	    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;分销商账户：&nbsp;<font color='red'><label id="selleraccountlable">${(sellerInfo.sellerAccount)!""}</label></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		          账户余额：<font color='red'>￥<label id="accountbalancelable">${(sellerInfo.accountBalance)!""}</label></font>元
			    <input name= "sellerId" id="sellerId" type="hidden" value="${(sellerInfo.id)!''}" />
	    	</div>
	    	<ul class="c-h-ul search-ul query-ul">
	            <li>
	                <label class="c-h-ul-li-title">交易时间：</label>
	                <input type="text" id="transactionTimeStart" name="transactionTimeStart" readonly="readonly" value="" class="input-medium" style="width:120px !important"/>
	            </li>
	            <li>
					至<input type="text" id="transactionTimeEnd" name="transactionTimeEnd" readonly="readonly" value="" class="input-medium" style="width:120px !important"/>
	            </li>
	            <li>
	                <label class="c-h-ul-li-title">交易类型：</label>
	                <select name="transactionType" id="transactionType" style="width:120px">
	                    <option value ="">全部</option>
	                    <option value ="1">佣金收益</option>
	                    <option value="2">提现</option>
	                </select>
	            </li>
	            <li>
	            	<label class="c-h-ul-li-title">状态：</label>
				    <select name="billState" id="billState" style="width:120px">
	                    <option value ="">全部</option>
	                    <option value ="1">交易成功</option>
	                    <option value="2">处理中</option>
	                    <option value="3">交易关闭</option>
	                    <option value="4">交易失败</option>
	                </select>
	            </li>
	            <li>
	                <label class="c-h-ul-li-title"></label>
	                <input type="button" value="查询" class="btn btn-sm btn-yougou" onclick="querySellerDetaillist();"/>&nbsp;&nbsp;
	                <input type="button" value="下载" class="btn btn-sm btn-yougou" onclick="exportDetailExcel();"/>
	            </li>
	        </ul>
		</div>
	</form>
	<div class="space-6"></div>
	<table id="grid-table-detail" class="mmg"></table>
	<div id="grid-pager-detail" style="text-align:right;" class=""></div>
	<div>
</div>
	
<script src="/static/js/manage/finance/sellerDetailList.js"></script>

