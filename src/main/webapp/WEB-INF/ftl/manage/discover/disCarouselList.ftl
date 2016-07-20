<@compress single_line=compress_single_line?contains("true")>
<#assign head>
	<link rel="stylesheet" type="text/css" href="/static/plugin/webuploader/webuploader.css">
	<style>
		img.carImp{
			margin:5px;
			width:128px;
			height:66px;
		}
	</style>
	<script>
		// 操作列动作
		var picFixed = function(val,item){
			var html = '';
			html += '<img class="carImp" src="${picUrl!''}'+val+'" alt="" />';
			return html;
		};
		var picBaseUlr = "${picUrl!''}";
		var count = 1;
	</script>
</#assign>

<#assign footer>
	<script src="/static/plugin/webuploader/webuploader.js"></script>
	<script src="/static/js/manage/discover/disCarouselList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		<button class="btn btn-sm btn-yougou" onclick="showForm();">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	新增
        </button>&nbsp;&nbsp;&nbsp;&nbsp;
        <button class="btn btn-sm btn-yougou" onclick="batchDelete();">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	批量删除
        </button>
        <div class="space-6"></div>
	</div>
	
	<div class="space-6"></div>
	<div class="col-xs-12">
		<table id="grid-table" class="mmg"></table>
	    <div id="grid-pager" style="text-align:right;" class=""></div>
    </div>
</div>

<div class="row">
	<div class="col-xs-12">
		<div id="carouselEdit" class="hide message-navbar clearfix">
			<div class="message-bar">
				<div class="message-toolbar">
					<button type="button" class="btn btn-xs btn-white btn-yougou" onclick="saveCarousel();">
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
		<form id="carouselForm" class="hide form-horizontal message-form col-xs-12">
			<input type="hidden" name="id" id="id" />
			<input type="hidden" name="articleNo" id="articleNo" />
			<input type="hidden" name="articleMark" id="articleMark" />
			<input type="hidden" name="picture" id="picture" />
			<div>
				<div class="blank6" />
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="name"><span style="color:red;">*</span>轮播图名称：</label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-medium" name="name" id="name"  maxlength="200" placeholder="" />
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="picture"><span style="color:red;">*</span>上传图片：</label>
					<div class="col-sm-6 col-xs-12">
                        <div id="filePicker">上传</div>
					</div>
					<input type="hidden" name="picUrl" id="picUrl" value="" />
				</div>
				<div class="form-group" style="height:315px;">
					<div class="col-sm-3"></div>
					<div class="col-sm-9 img" style="height:300px;"></div>
					<div class="col-sm-3"></div>
					<div class="col-sm-9">温馨提示：图片尺寸为640*300，大小不能超过1.5M</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" style="padding-top:0px;">
						<span style="color:red;">*</span>
			        	<button onclick="addArticle();return false;" style="height:29px;">添加链接文章</button>
        			</label>
					<div class="col-sm-3">
						<span id="articleTips" style="font-size:20;line-height:29px;"></span>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>