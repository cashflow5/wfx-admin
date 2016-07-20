<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/discover/discoverLogList.js"></script>
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
		                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">业务类型：</label>
									<div class="col-sm-2">							
									    <select id="businessType" name="businessType">
									    	<option value="" selected="selected">请选择</option>
									    	<option value="1" >频道管理</option>
									    	<option value="2" >文章管理</option>
									    	<option value="3" >轮播图管理</option>
									    	<option value="4" >回收站管理</option>
									    </select>
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">日志类型：</label>
									<div class="col-sm-2">							
									    <select id="operateType" name="operateType">
									    	<option value="" selected="selected">请选择</option>
									    	<option value="1" >新增</option>
									    	<option value="2" >删除</option>
									    	<option value="3" >修改</option>
									    </select>
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">操作时间：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="createTimeStart" id="createTimeStart" value="" type="text">
										
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">至</label>
									<div class="col-sm-2">									
									    <input class="form-control input-sm" name="createTimeEnd" id="createTimeEnd" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">操作人：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="operateUser" id="operateUser" value="" type="text">
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