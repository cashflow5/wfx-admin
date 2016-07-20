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
	<script src="/static/js/manage/commodity/commodityList.js"></script>-->
	<script src="/static/js/math.js"></script>
	<script src="/static/js/manage/commodity/wfxCommodityOperateList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		<div class="tabbable">	
		<ul class="nav nav-tabs" id="myTab">
				<li class="active"><a  href="/commodity/wfxCommodityOperateList.sc" >微分销商品库</a></li>
		</ul>
     
		<div class="tab-content">
			<!-- 微分销 -->
			<div   id="wfxCommodityList"  class="tab-pane active">
				<!--<div class="bg-gray area-btn">
			    	<a class="btn btn-xs btn-yougou mr5"     onclick="commodityExcel()"  >导出</a>
			    	<a class="btn btn-xs btn-yougou mr5"     onclick="importExcel()"  >调价</a>
			    	<a class="btn btn-xs btn-yougou mr5"     onclick="batchUpdateShelvesStatus(1)"  >批量上架</a>
			    	<a class="btn btn-xs btn-yougou mr5"     onclick="batchUpdateShelvesStatus(2)"  >批量下架</a> 
			    </div>-->
				<!-- 搜索表单，需自定义 -->
				<form class="form-horizontal" id="wfxCommodityForm">
		            <fieldset>
		                <div class="row search-box" >
		                    <div class="col-sm-12">
		                        <div class="form-group">
		                        	<input name="postFee" id="postFee" value="${postFee!''}" type="hidden">
									<input name="taxRate" id="taxRate" value="${taxRate!''}" type="hidden">
									<input name="vatRate" id="vatRate" value="${vatRate!''}" type="hidden">
									
		                        	<label for="memberShip" class="col-sm-1 control-label col-xs-12 no-padding-right"> 商品分类：</label>
									<div class="col-sm-5">
							              <select class="input-sm" id="wfxLevelOne" name="wfxLevelOne" onchange="wfxCatChange('1',this);">
											<option value="">请选择</option>
											<#list basicList as item>
												<option value="${item.id}"><#if item.catName??>${item.catName!''}</#if></option>
											</#list>
										</select>
							           
										<select class="input-sm" id="wfxLevelTwo" name="wfxLevelTwo" onchange="wfxCatChange('2',this);">
											<option value="">请选择</option>
										</select>
									
							            
										<select class="input-sm" id="wfxLevelThree" name="wfxLevelThree" onchange="wfxCatChange('3',this);">
											<option value="">请选择</option>
										</select>
							        </div>
							        
									<div class="col-sm-2">
							        	<label for="operator" class="control-label no-padding-right">品牌：</label>
									    <input class="form-control input-sm inline" name="brandName" id="brandName" value="" type="text">
									</div>
									<div class="col-sm-2">
										<label for="operator" class="control-label no-padding-right">商品款号：</label>
									    <input class="form-control input-sm inline" name="styleNo" id="styleNo" value="" type="text">
									</div>
									<div class="col-sm-2">
										<label for="operator" class="control-label no-padding-right">款色ID：</label>
									    <input class="form-control input-sm inline" name="no" id="no" value="" type="text">
									</div>
									<div class="col-sm-2">
										<label for="operator" class="control-label no-padding-right">款色编码：</label>
									    <input class="form-control input-sm inline" name="supplierCode" id="supplierCode" value="" type="text">
									</div>
									<div class="col-sm-2">
									<label for="memberShip" class="control-label no-padding-right"> 商品状态：</label>
							            
										 <select class="form-control input-sm inline"  id="isOnsale"  name="isOnsale">
							                <option value="">请选择</option>
							                <option value="3">未上架</option>
							                <option value="1">已上架</option>
							                <option value="2">已下架</option>
										</select>
							        </div>
									<div class="col-sm-2">
			                            <input type="button" value="搜索" class="btn btn-sm btn-yougou" onclick="doWfxQuery();"/>
									</div>
		                        </div>
		                    </div>
		                </div>
		            </fieldset>
	        	</form>
					<table id="wfxCommodity-table" class="mmg"></table>
				    <div id="wfxCommodity-pager" style="text-align:right;" class=""></div>
			</div>
			<!-- end微分销 -->
		</div>
	</div>
	</div>
	
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>