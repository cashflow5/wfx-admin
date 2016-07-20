<@compress single_line=compress_single_line?contains("true")>
<#assign head>
	<link rel="stylesheet" type="text/css" href="/static/plugin/webuploader/webuploader.css">
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/plugin/webuploader/webuploader.js"></script>
	<script src="/static/js/manage/basicset/appVersionList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		<button class="btn btn-sm btn-yougou" onclick="showForm();">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	新增
        </button>
        <div class="space-6"></div>
	</div>
	
	<div class="space-6"></div>
	<div class="col-xs-12">
		<table id="grid-table" class="mmg"></table>
	    <div id="grid-pager" style="text-align:right;" class=""></div>
    </div>
</div>



<!-- 编辑页面 -->
<div class="row">
	<div class="col-xs-12">
		<div id="appVersionNavbar" class="hide message-navbar clearfix">
			<div class="message-bar">
				<div class="message-toolbar">
					<button type="button" class="btn btn-xs btn-white btn-yougou" onclick="saveAppVersion();">
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
		<form id="appVersionForm" class="hide form-horizontal message-form col-xs-12">
			<input type="hidden" name="id" id="id" />
			<div>
				<div class="blank6" />
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="versionName2"><span style="color:red;">*</span>版本名称：</label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-medium" name="versionName" id="versionName2"  maxlength="50" placeholder="" />
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="versionCode2"><span style="color:red;">*</span>版本编号：</label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-medium" name="versionCode" id="versionCode2"  maxlength="50" placeholder="" />
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="versionUrl2"><span style="color:red;">*</span>版本APK存放地址：</label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-medium" name="versionUrl" id="versionUrl2"  maxlength="50" placeholder=""  readonly="readonly"/>
						<dd>
                    	<a id="selectAppApk" href="javascript:void(0);">上传apk</a>
					</dd>    
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="forceUpdate2"><span style="color:red;">*</span>是否强制更新：</label>
					<div class="col-sm-3">
						<input type="radio" value='1' name="forceUpdate" >是</input>&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" value='2' name="forceUpdate" >否</input>
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="versionContent2">版本描述：</label>
					<div class="col-sm-3">
						<textarea name="versionContent" id="versionContent2" maxlength="400" class="autosize-transition form-control" style="overflow: hidden; word-wrap: break-word; resize: horizontal; height: 200px;  width: 400px;"></textarea>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>