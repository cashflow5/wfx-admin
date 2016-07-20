<@compress single_line=compress_single_line?contains("true")>
<#assign head>
	<link rel="stylesheet" type="text/css" href="/static/plugin/webuploader/webuploader.css">
	<link rel="stylesheet" type="text/css" href="/static/plugin/kindeditor/themes/simple/simple.css"/>
	<style>
		#publish,#unPublish{margin-right:10px;}
		.hotCatPic{height:36px;width:auto;margin:5px;}
		.hotSnGroup .hotSnText{display:inline-block;width:30px;text-align:right;}
		.hotSnGroup .hotSnInput{width:40px;text-align:right;}
		.hotSnGroup a{display:inline-block;width:16px;margin-left:10px;}
		.hotSnGroup a:hover{cursor:pointer;}
		.hotSnGroup .editIcon{background:transparent url(/static/images/icon_edit.png) no-repeat center;}
		.hotSnGroup .saveIcon{background:transparent url(/static/images/icon_save.png) no-repeat center;}
		.hotSnGroup .cancelIcon{background:transparent url(/static/images/del-class.gif) no-repeat center;}
	</style>
	<script>
		var picBaseUlr = "${picUrl!''}";
		var count = 1;
		var h5Domain = '${h5Domain!''}';
	</script>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/plugin/webuploader/webuploader.js"></script>
	<script src="/static/plugin/layer/layer.js"></script>
	<script src="/static/plugin/layer/extend/layer.ext.js"></script>
	<script src="/static/plugin/kindeditor/kindeditor.min.js"></script>
	<script src="/static/js/manage/commodity/cortexEdit.js"></script>
	<script src="/static/js/manage/commodity/cortexList.js"></script>
	
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		<!-- <button class="btn btn-yougou" onclick="">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	新增
        </button> -->
        <div class="space-6"></div>
		<div class="row">
			<div class="col-xs-12">
				<!-- 搜索表单，需自定义 -->
				<form class="form-horizontal" id="searchForm">
		            <fieldset>
		                <div class="row">
		                    <div class="col-sm-12">
		                        <div class="form-group">
		                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">皮质名：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="name" id="name" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">是否有说明：</label>
									<div class="col-sm-2">
										 <select class="form-control input-small"  name="isNotDescription"  id="isNotDescription" >
											   <option value="0">全部</option>
											   <option value="100">是</option>
											   <option value="101">否</option>
						                </select>
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
<div class="row">
	<div class="col-xs-12">
	<form id="cortexForm" class="hide form-horizontal message-form col-xs-12">
			<input type="hidden" name="id" id="id" />
			<input type="hidden" name="no" id="no" />
			<input type="hidden" name="description" id="description" />
			<div>
				<div class="blank6" />
				<div class="form-group">
					<label class="col-sm-1 control-label no-padding-right" for="title"><span style="color:red;">*</span>皮质名：</label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-medium" name="name" id="name"  maxlength="30" placeholder="" readonly="readonly" />
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
			   <!-- 富文本开始-->
			   <div class="form-group">
			   <label class="col-sm-1 control-label no-padding-right" for="title"><span style="color:red;">*</span>说&nbsp;&nbsp;&nbsp;&nbsp;明：</label>
               <div class="article-box">
                   <div class="mb10 editor-group">
                       <a href="javascript:;" class="btn btn-yougou btn-sm" data-type="text">添加文字</a>
                       <a href="javascript:;" class="btn btn-yougou btn-sm" data-type="image">添加图片</a>
                   </div>
                   <textarea id="cortexEditor" class="form-control input-xlarge" name="cortexEditor" cols="80" rows="5"></textarea>
               </div>
               </div>
			   <!-- 富文本结束-->
			</div>
		</form>
		<div id="cortexNavbar" style="margin-top:10px;width:500px;text-align:center" class="hide message-navbar clearfix">
	        <button class="btn btn-sm btn-yougou saveCortext" style="margin-right:30px;">
	        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
	        	保存
	        </button>
	        <button class="btn btn-sm btn-yougou" onclick="hideForm();return false;">
	        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
	        	返回列表
	        </button>
        </div>
	</div>
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>