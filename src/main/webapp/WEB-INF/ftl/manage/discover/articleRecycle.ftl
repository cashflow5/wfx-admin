<@compress single_line=compress_single_line?contains("true")>
<#assign head>
<style>
	#publish,#unPublish{margin-right:10px;}
</style>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/discover/articleRecycle.js"></script>
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
		                        	<label for="channelId" class="col-sm-1 control-label col-xs-12 no-padding-right">频道名称：</label>
									<div class="col-sm-2">
									    <select name="channelId" id="channelId2" style="width:120px !important">
									    	<option value="">全选</option>
									    	<#list channelList as item>
									    		<option value="${item.id!''}">${item.channelName!''}</option>
									    	</#list>
									    </select>
									</div>
									<label for="updateTimeStart" class="col-sm-1 control-label col-xs-12 no-padding-right">更新时间：</label>
									<div class="col-sm-2">
									    <input class="input-medium" name="updateTimeStart" id="updateTimeStart" readonly="readonly" value="" type="text">
									</div>
		                        	<label for="createdTimeEnd" class="col-sm-1 control-label col-xs-12 no-padding-right">—</label>
									<div class="col-sm-2">
									    <input class="input-medium" name="updateTimeEnd" id="updateTimeEnd" readonly="readonly" value="" type="text">
									</div>
									<label for="publishStatus" class="col-sm-1 control-label col-xs-12 no-padding-right">文章状态：</label>
									<div class="col-sm-2">
									    <select name="publishStatus" id="publishStatus2" style="width:100%;">
									    	<option value="">全选</option>
									    	<option value="2">已发布</option>
									    	<option value="1">未发布</option>
									    </select>
									</div>
		                        </div>
		                        <div class="form-group">
		                        	<label for="recommendFlag" class="col-sm-1 control-label col-xs-12 no-padding-right">是否推荐：</label>
									<div class="col-sm-2">
									    <select name="recommendFlag" id="recommendFlag2" style="width:100%;">
									    	<option value="">全选</option>
									    	<option value="2">推荐</option>
									    	<option value="1">不推荐</option>
									    </select>
									</div>
									<label for="title" class="col-sm-1 control-label col-xs-12 no-padding-right">文章标题：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="title" id="title" value="" type="text">
									</div>
									<label for="authorType" class="col-sm-1 control-label col-xs-12 no-padding-right">作者：</label>
									<div class="col-sm-2">
									    <select name="authorType" id="authorType2" style="width:100%;">
									    	<option value="">全选</option>
									    	<option value="1">优购微零售</option>
									    	<option value="2">非优购微零售</option>
									    </select>
									</div>
									<label for="authorAccount" class="col-sm-1 control-label col-xs-12 no-padding-right">作者账号：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="authorAccount" id="authorAccount2" value="" type="text">
									</div>
		                        </div>
		                        <div class="form-group">
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
	<div class="space-6"></div>
	<button id="publish" class="btn btn-sm btn-yougou" onclick="batchDelete();">
    	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
    	批量删除
    </button>
	<button id="unPublish" class="btn btn-sm btn-yougou" onclick="batchRestore();">
    	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
    	批量还原
    </button>
	<div class="space-6"></div>
		<table id="grid-table" class="mmg"></table>
	    <div id="grid-pager" style="text-align:right;" class=""></div>
    </div>
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>