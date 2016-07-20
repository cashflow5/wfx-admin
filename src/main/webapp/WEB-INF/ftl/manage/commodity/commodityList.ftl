<@compress single_line=compress_single_line?contains("true")>
<#assign head>
<style>
.search-box [class*="col-sm-"] {
    padding: 5px 0 0;
}
.search-box .form-group{
	margin: 5px 10px;
}
</style>
</#assign>

<#assign footer>
	<!-- this page 
	<script src="/static/js/manage/commodity/wfxCommodityList.js"></script>-->
	<script src="/static/js/math.js"></script>
	<script src="/static/js/manage/commodity/commodityList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		<div class="tabbable">	
		<ul class="nav nav-tabs" id="myTab">
				<li class=""><a href="/commodity/wfxCommodityList.sc" >微分销商品库</a></li>
				<li class="active"><a  href="/commodity/commodityList.sc" >优购商品库 </a></li>
		</ul>
     
		<div class="tab-content">
			<!-- 优购库 -->
			<div  id="commodityList"   class="tab-pane active">
			    <div class="bg-gray area-btn">
			    	<a class="btn btn-xs btn-yougou mr5"     onclick="batchCommodityAddToWfx()"  >批量添加至微分销库</a>
			    </div>
				<!-- 搜索表单，需自定义 -->
				<form    class="form-horizontal" id="commodityForm">
					
		            <fieldset>
		                <div class="row search-box">

		                        <div class="form-group">
									<input name="postFee" id="postFee" value="${postFee!''}" type="hidden">
									<input name="taxRate" id="taxRate" value="${taxRate!''}" type="hidden">
									<input name="vatRate" id="vatRate" value="${vatRate!''}" type="hidden">
									<input name="distributableMoney" id="distributableMoney"  type="hidden">
									<input name="commodityFlag" id="commodityFlag" value="${commodityFlag!''}" type="hidden">
									
									<div class="col-sm-5">
										<label for="memberShip" class="control-label no-padding-right"> 商品分类：</label>
							            <select class="input-sm" id="levelone" name="levelone" onchange="catChange('1',this);">
											<option value="">请选择</option>
											<#list basicList as item>
												<option value="${item.id}"><#if item.catName??>${item.catName!''}</#if></option>
											</#list>
										</select>
						           
										<select class="input-sm" id="leveltwo" name="leveltwo" onchange="catChange('2',this);">
											<option value="">请选择</option>
										</select>
										 
							            <select class="input-sm" id="levelthree" name="levelthree" onchange="catChange('3',this);">
											<option value="">请选择</option>
										</select>
							        </div>
							        
									<div class="col-sm-2">
										<label for="operator" class="control-label no-padding-right">品牌：</label><input class="form-control input-sm inline" name="brandName" id="brandName" value="" type="text">
									</div>
									
									<div class="col-sm-2">
										<label for="operator" class="control-label no-padding-right">商品款号：</label><input class="form-control input-sm inline" name="styleNo" id="styleNo" value="" type="text">
									</div>
									
									<div class="col-sm-2">
										<label for="operator" class="control-label no-padding-right">款色ID：</label><input class="form-control input-sm inline" name="no" id="no" value="" type="text">
									</div>
									
									<div class="col-sm-2">
										<label for="operator" class="control-label no-padding-right">款色编码：</label><input class="form-control input-sm inline" name="supplierCode" id="supplierCode" value="" type="text">
									</div>
									
									<div class="col-sm-2">
			                            <input type="button" value="搜索" class="btn btn-sm btn-yougou" onclick="doQuery();"/>
									</div>
									
		                        
		                </div>
		            </fieldset>
	        	</form>
				<table id="commodity-table" class="mmg"></table>
			    <div id="commodity-pager" style="text-align:right;" class="">
			    </div>
			</div>
			<!-- end 优购库 -->
			
		</div>
	</div>
	</div>
	
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>