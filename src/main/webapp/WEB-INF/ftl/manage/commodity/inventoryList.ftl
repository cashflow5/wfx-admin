<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/commodity/inventoryList.js"></script>
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
		                        	<label for="commodityName" class="col-sm-1 control-label col-xs-12 no-padding-right">商品名称：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="commodityName" id="commodityName" value="" type="text">
									</div>
		                        	<label for="wfxCommodityNo" class="col-sm-1 control-label col-xs-12 no-padding-right">款色ID：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="commodityNo" id="commodityNo" value="" type="text">
									</div>
		                        	<label for="supplierCode" class="col-sm-1 control-label col-xs-12 no-padding-right">款色编码：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="supplierCode" id="supplierCode" value="" type="text">
									</div>
		                        	<label for="thirdPartyCode" class="col-sm-1 control-label col-xs-12 no-padding-right">商家编码：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="thirdPartyCode" id="thirdPartyCode" value="" type="text">
									</div>
								</div>
								<div class="form-group">
									<label for="isOnsale" class="col-sm-1 control-label col-xs-12 no-padding-right"> 状态：</label>
									<div class="col-sm-2">
							            <select class="form-control input-sm" name="isOnsale" id="isOnsale">
							                <option value="">所有</option>
											<option value="3">未上架</option>
											<option value="1">已上架</option>
											<option value="2">已下架</option>
							            </select>
							        </div>
							        <label for="inventoryNumStart" class="col-sm-1 control-label col-xs-12 no-padding-right">商品库存：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="inventoryNumStart" id="inventoryNumStart" value="" type="text">
									</div>
									 <label for="inventoryNumEnd" class="col-sm-1 control-label col-xs-12 no-padding-right">至</label>
									 <div class="col-sm-2">
									    <input class="form-control input-sm" name="inventoryNumEnd" id="inventoryNumEnd" value="" type="text">
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