<div id="girdContent" class="row">
	<div class="col-xs-12">
		<div class="row">
			<div class="col-xs-12">
				<!-- 搜索表单，需自定义 -->
				<form class="form-horizontal" id="searchForm">
		            <fieldset>
		            	<table>
		            		<tr>
			            		<th>品牌中文名：<th>
			            		<td><input class="form-control input-sm" name="brandName" id="brandName" value="" type="text"></td>
		            			<td>&nbsp;&nbsp;&nbsp;</td>
		            			<th>品牌英文名：<th>
		            			<td><input class="form-control input-sm" name="englishName" id="englishName" value="" type="text"></td>
		            			<th><input class="form-control input-sm" name="status" id="status" value="1" type="hidden"><th>
		            			<td>&nbsp;&nbsp;&nbsp;</td>
		            			<td><input type="button" value="搜索" class="btn btn-sm btn-yougou" onclick="doQuery();"/></td>
		            		</tr>
		            	</table>
		            </fieldset>
	        	</form>
			</div>
		</div>
	</div>
	
	<div class="space-6"></div>
	<div class="col-xs-12">
		<table id="brandList-grid-table" class="mmg"></table>
		<div id="grid-pager" style="text-align:right;" class=""></div>
    </div>
</div>
<script >
$(function(){
	loadBrandPage();
});
</script>