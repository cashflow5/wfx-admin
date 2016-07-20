<div id="commodity-girdContent" class="row" style="width:1245px">
	<div class="col-xs-12">
	<form name="commodityForm" id="commodityForm" method="post" action="" class="form-horizontal" >
		<div class="search-content-wrap">
	    	<ul class="c-h-ul search-ul query-ul">
	            <li>
	                <label class="c-h-ul-li-title">品牌：</label>
	                <input type="text" id="brandName" name="brandName" value="" class="input-medium" style="width:120px !important"/>
	            </li>
	            <li>
	                <label class="c-h-ul-li-title">款色编码</label>
	                <input type="text" id="supplierCode" name="supplierCode" value="" class="input-medium" style="width:120px !important"/>
	            </li>
	            <li>
	                <label class="c-h-ul-li-title">商品款号：</label>
	                <input type="text" id="styleNo" name="styleNo" value="" class="input-medium" style="width:120px !important"/>
	            </li>
	             <li>
	                <label class="c-h-ul-li-title">款色ID</label>
	                <input type="text" id="no" name="no" value="" class="input-medium" style="width:120px !important"/>
	            </li>
	            <li>
	                <label class="c-h-ul-li-title"></label>
	                <input type="button" value="查询" class="btn btn-sm btn-yougou" onclick="doQuery();"/>&nbsp;&nbsp;
	            </li>
	        </ul>
		</div>
	</form>
	
	<div class="space-6"></div>
		<table id="commodity-grid-table" class="mmg"></table>
		<div id="commodity-grid-pager" style="text-align:right;" class=""></div>
	<div>
</div>

<script src="/static/js/manage/commodity/commoditySelected.js"></script>