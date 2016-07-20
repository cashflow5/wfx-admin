<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/seller/authorizeList.js"></script>
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
		                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">分销商账号：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="sellerLoginName" id="sellerLoginName" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">姓名：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="sellerName" id="id" value="" type="text">
									</div>
									<label for="operator" class="col-sm-2 control-label col-xs-12 no-padding-right"> 资质审核状态：</label>
									<div class="col-sm-2">
									    <select class="form-control input-sm" name="status" id="status">
							                <option value="" selected>请选择</option>
											<option value="1">待审核</option>
											<option value="2">审核通过</option>
											<option value="3">审核不通过</option>
							            </select>
									</div>
									<div class="col-sm-1">
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