<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/aftersale/supplierAddressList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		  <input type="button" value="新增" class="btn btn-sm btn-yougou" id="add-quick" onClick="toAddOrUpdateSupplierAddress('');"/>
        <div class="space-6"></div>
	</div>
	<form class="form-horizontal" id="searchForm">
		<input type="hidden" value="${supplierCode}" id="supplierCode_" name="supplierCode_"/> 
	</form>
	
	<div class="space-6"></div>
	<div class="col-xs-12">
		<table id="grid-table" class="mmg"></table>
	    <div id="grid-pager" style="text-align:right;" class=""></div>
    </div>
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>