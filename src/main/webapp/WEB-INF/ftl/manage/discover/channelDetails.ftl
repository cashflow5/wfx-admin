<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/discover/channelDetails.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		<button class="btn btn-yougou" onclick="back()">
		
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	返回
        </button>
        <div class="space-6"></div>
		<div class="row">
			<div class="col-xs-12">
				<!-- 搜索表单，需自定义 -->
				<form class="form-horizontal" id="searchForm">
				<input name="channelId" id="channelId" value="${channelId!''}" type="hidden">
		            <fieldset>
		                <div class="row">
		                    <div class="col-sm-12">
		                        <div class="form-group">
		                        	&nbsp<label for="operator" >频道序号:频道${channelDto.channelCode!''}</label></br>
		                        	&nbsp<label for="operator" >频道名称:${channelDto.channelName!''}</label></br>
		                        	&nbsp<label for="operator" >频道状态:
		                        	<#if channelDto??&&channelDto.status??&&channelDto.status==1> 
	                   					显示<input id="status" type="radio" value="1" name="status"  checked/>隐藏<input id="status" type="radio" value="2" name="status"/>
				                    <#else>
				                    	显示<input id="status" type="radio" value="1" name="status"  />隐藏<input id="status" type="radio" value="2" name="status" checked/>
				                    </#if></label>
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