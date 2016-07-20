<#-- 分销包页面  -->
<@compress single_line=compress_single_line?contains("true")>
<#-- =========head========-->
<#assign head>

</#assign>

<#-- =========footer===== -->
<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/commodity/packageList.js"></script>
</#assign>

<#-- =========body======= -->
<#assign body>
<!-- 分销包列表信息 -->
<div class="row">
	<div class="col-xs-12">
        <a class="btn btn-yougou add-package" href="javascript:void(0);">新增</a>
        <div class="row">
			<div class="col-xs-12">
				<form class="form-horizontal" id="searchPackageForm">
		            <fieldset>
		                <div class="row">
		                    <div class="col-sm-12">
		                        <div class="form-group">
		                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">分销包名称：</label>
									<div class="col-sm-1">
									    <input class="form-control input-sm" name="bagName" id="bagName" value="" type="text">
									</div>
									<label for="memberLevel" class="col-sm-1 control-label col-xs-12 no-padding-right"> 状态：</label>
									<div class="col-sm-1">
									    <select class="form-control input-sm" name="status" id="status">
							                <option value="-1" selected>全部</option>
											<option value="1">待审核</option>
											<option value="2">审核拒绝</option>
											<option value="3">已发布</option>
											<option value="4">已取消</option>
							            </select>
									</div>
									<label for="startTime" class="col-sm-2 control-label col-xs-12 no-padding-right">创建时间：</label>
									<div class="col-sm-2">
									    <input class="input-medium" name="startTime" id="startTime" readonly="readonly" value="" type="text">
									</div>
		                        	<label for="endTime" class="col-sm-1 control-label col-xs-12 no-padding-right">至：</label>
									<div class="col-sm-2">
									    <input class="input-medium" name="endTime" id="endTime" readonly="readonly" value="" type="text">
									</div>
		                        
									<div class="col-sm-1">
			                            <input type="button" value="搜索" class="btn btn-yougou" onclick="doQuery();"/>
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
	<table id="package-table" class="mmg"></table>
    <div id="package-pager" style="text-align:right;"></div>
</div>
</#assign>

<#-- =========引入模板======= -->
<#include "/include/pageBuilder.ftl" />
</@compress>