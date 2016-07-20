<@compress single_line=compress_single_line?contains("true")>
<#assign head>
<style>
em.c3 input{display:none;width:40px;}
em.c3 a.edit4HotSn{margin-left:5px;display:inline-block;width:16px;height:16px;background:url(/static/images/icon_edit.png);}
em.c3 a.save4HotSn{margin-left:5px;display:inline-block;width:16px;height:16px;background:url(/static/images/icon_save.png);display:none}
em.c3 a.cancel4HotSn{margin-left:5px;display:inline-block;width:9px;height:9px;background:url(/static/images/del-class.gif);display:none}
em.c3 span.loadimg{display:none;}
</style>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/cms/hotBrandList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		<#if hotFlag?? && hotFlag == '2'>
		<button class="btn btn-yougou" onclick="batchAddBrand();">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	批量添加
        </button>
		<button class="btn btn-yougou" onclick="switchPage(1);">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	返回
        </button>
		<#else>
		<button class="btn btn-yougou" onclick="switchPage(2);">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	新增
        </button>
		<button class="btn btn-yougou" onclick="batchRemoveBrand();">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	批量删除
        </button>
        </#if>
        <div class="space-6"></div>
		<div class="row">
			<div class="col-xs-12">
				<!-- 搜索表单，需自定义 -->
				<form class="form-horizontal" id="searchForm">
		            <fieldset>
		                <div class="row">
		                    <div class="col-sm-12">
		                        <div class="form-group">
		                        	<label for="brandName" class="col-sm-1 control-label col-xs-12 no-padding-right">中文名称：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="brandName" id="brandName" value="" type="text">
									</div>
		                        	<label for="englishName" class="col-sm-1 control-label col-xs-12 no-padding-right">英文名称：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="englishName" id="englishName" value="" type="text">
									</div>
		                        	<label for="brandNo" class="col-sm-1 control-label col-xs-12 no-padding-right">品牌编号：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="brandNo" id="brandNo" value="" type="text">
									</div>
									<input type="hidden" id="hotFlag" name="hotFlag" value="${hotFlag!'1'}"/>
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