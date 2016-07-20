<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/discover/discoverList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		<button class="btn btn-yougou" onclick="addChannel()">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	新增
        </button>
        <button class="btn btn-yougou" onclick="batchRemove()">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	批量删除
        </button>
        <div class="space-6"></div>
		<div class="row">
			<div class="col-xs-12">
				<!-- 搜索表单，需自定义 -->
				<form class="form-horizontal" id="searchForm">
					<input name="orderMarkCode" id="orderMarkCode" value="${orderMarkCode!''}" type="hidden">
		           
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