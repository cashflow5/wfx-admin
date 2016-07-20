<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/basicset/operationLogList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		<div class="row">
			<div class="col-xs-12">
				<!-- 搜索表单，需自定义 -->
				<form class="form-horizontal" id="searchForm">
		            <fieldset>
		                <div class="row">
		                    <div class="col-sm-12">
		                        <div class="form-group">
		                        	
									<label for="operateType" class="col-sm-1 control-label col-xs-12 no-padding-right">日志类型：</label>
									<div class="col-sm-2">
									    <select class="form-control input-sm" name="operateType">
									    	<option value="">请选择</option>
							                <option value="ADD">添加</option>
							                <option value="EDIT">修改</option>
							                <option value="DELETE">删除</option>
							                <option value="EXPORT">导出</option>
							            </select>
									</div>
									
									<label for="memberName" class="col-sm-1 control-label col-xs-12 no-padding-right">操作人：</label>	
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="operateUser" id="operateUser" value="" type="text">
									</div>
									<label for="operateDateStart" class="col-sm-1 control-label col-xs-12 no-padding-right">操作时间：</label>
									<div class="col-sm-2">
									    <input name="operateDateStart" id="operateDateStart" readonly="readonly" value="" class="input-medium" type="text">
									</div>
									<label for="operateDateEnd" class="col-sm-1 control-label col-xs-12 no-padding-right">至</label>
									<div class="col-sm-2">
									    <input name="operateDateEnd" id="operateDateEnd" readonly="readonly" value="" class="input-medium" type="text">
									</div>
								    <div class="col-sm-3">
			                            <input type="button" value="查询" class="btn btn-sm btn-yougou" onclick="doQuery();"/>
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