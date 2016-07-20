<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/seller/sellerList.js"></script>
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
		                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">会员账号：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="loginName" id="loginName" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">会员姓名：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="sellerName" id="sellerName" value="" type="text">
									</div>
									
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">注册时间：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="registerDateStart" id="registerDateStart" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">至</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="registerDateEnd" id="registerDateEnd" value="" type="text">
									</div>
									
								
		                        </div>
		                        <div class="form-group">
		                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">状态：</label>
									<div class="col-sm-2">
									    <select id="state" name="state">
									    	<option value="" selected="selected">请选择</option>
									    	<option value="3">合作中</请选择>
									    	<option value="4">已停止</请选择>
									    </select>
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