<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/basicset/sysConfigList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		<button class="btn btn-sm btn-yougou" onclick="showForm();">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	新增
        </button>
        <div class="space-6"></div>
		<div class="row">
			<div class="col-xs-12">
				<div class="tab-content">
				<!-- 搜索表单，需自定义 -->
				<form class="form-horizontal" id="searchForm">
		            <fieldset>
		                <div class="row">
		                    <div class="col-sm-12">
		                        <div class="form-group">
		                        	<label for="configName" class="col-sm-1 control-label col-xs-12 no-padding-right">配置名称：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="configName" id="configName" value="" type="text">
									</div>
		                        	<label for="configKey" class="col-sm-1 control-label col-xs-12 no-padding-right">配置键：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="configKey" id="configKey" value="" type="text">
									</div>
		                        	<label for="configValue" class="col-sm-1 control-label col-xs-12 no-padding-right">配置键值：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="configValue" id="configValue" value="" type="text">
									</div>
									<div class="col-sm-3">
			                            <input type="button" value="查询" class="btn btn-sm btn-yougou" onclick="doQuery();"/>
									</div>
		                        </div>
		                    </div>
		                </div>
		            </fieldset>
	        	</form>
			</div>
			</div>
		</div>
	</div>
	<div class="space-6"></div>
	<div class="col-xs-12">
		<table id="grid-table" class="mmg"></table>
	    <div id="grid-pager" style="text-align:right;" class=""></div>
    </div>
</div>

<div class="row">
	<div class="col-xs-12">
		<div id="sysConfigNavbar" class="hide message-navbar clearfix">
			<div class="message-bar">
				<div class="message-toolbar">
					<button type="button" class="btn btn-xs btn-white btn-yougou" onclick="saveSysconfig();">
						<i class="ace-icon fa fa-floppy-o bigger-175"></i>
						<span class="bigger-110">保存</span>
					</button>
					<button type="button" class="btn btn-xs btn-white btn-yougou" onclick="hideForm();">
						<i class="ace-icon fa fa-times bigger-175 orange2"></i>
						<span class="bigger-110">取消</span>
					</button>
				</div>
			</div>
			<div>
				<div class="messagebar-item-left">
					<a href="javascript:void(0);" class="btn-back-message-list" onclick="hideForm();">
						<i class="ace-icon fa fa-arrow-left bigger-110 middle blue"></i>
						<b class="middle bigger-110">返回列表</b>
					</a>
				</div>
			</div>
		</div>
		<form id="sysConfigForm" class="hide form-horizontal message-form col-xs-12">
			<input type="hidden" name="id" id="id" />
			<div>
				<div class="blank6" />
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="configName2"><span style="color:red;">*</span>配置名称：</label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-medium" name="configName" id="configName2"  maxlength="50" placeholder="" />
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="configKey2"><span style="color:red;">*</span>配置键：</label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-medium" name="configKey" id="configKey2"  maxlength="50" placeholder="" />
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="configValue2"><span style="color:red;">*</span>配置键值：</label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-medium" name="configValue" id="configValue2"  maxlength="100" placeholder="" />
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="remark2">备注：</label>
					<div class="col-sm-3">
						<textarea name="remark" id="remark2" maxlength="200" class="autosize-transition form-control" style="overflow: hidden; word-wrap: break-word; resize: horizontal; height: 65px;"></textarea>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>