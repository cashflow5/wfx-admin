<@compress single_line=compress_single_line?contains("true")>
<#assign head>
	<link rel="stylesheet" type="text/css" href="/static/plugin/webuploader/webuploader.css">
	<!--<link type="text/css" rel="stylesheet" href="/static/plugin/kindeditor/themes/default/default.css"/>-->
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
		input.ke-input-text{height:30px;}
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
	<script src="/static/js/jquery.zclip.min.js"></script>
	<script src="/static/js/manage/discover/articleEdit.js"></script>
	<script src="/static/js/manage/discover/articleList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		<button class="btn btn-sm btn-yougou" onclick="showForm();">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	新增文章
        </button>
        <div class="space-6"></div>
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
									    <input class="form-control input-sm" name="title" id="title2" value="" type="text">
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
	<button id="publish" class="btn btn-sm btn-yougou" onclick="batchPublish(2);">
    	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
    	发布文章
    </button>
	<button id="unPublish" class="btn btn-sm btn-yougou" onclick="batchPublish(1);">
    	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
    	取消发布
    </button>
	<button class="btn btn-sm btn-yougou" onclick="batchDelete();">
    	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
    	删除文章
    </button>
	<div class="space-6"></div>
		<table id="grid-table" class="mmg"></table>
	    <div id="grid-pager" style="text-align:right;" class=""></div>
    </div>
</div>

<div class="row">
	<div class="col-xs-12">
		<form id="articleForm" class="hide form-horizontal message-form col-xs-12">
			<input type="hidden" name="id" id="id" />
			<input type="hidden" name="publishStatus" id="publishStatus" />
			<input type="hidden" name="picCover" id="picCover" />
			<input type="hidden" name="content" id="content" />
			<div>
				<div class="blank6" />
				<div class="form-group">
					<label class="col-sm-1 control-label no-padding-right" for="title"><span style="color:red;">*</span>标题：</label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-medium" name="title" id="title"  maxlength="30" placeholder="" />
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-1 control-label no-padding-right" for="authorType"><span style="color:red;">*</span>作者：</label>
					<div class="col-sm-3" style="width:100px;">
						<select name="authorType" id="authorType">
							<option value="1">优购微零售</option>
							<option value="2">非优购微零售</option>
						</select>
					</div>
					<label class="col-sm-1 control-label no-padding-right" for="authorAccount" style="left:30px;width:130px;"><span style="color:red;">*</span>作者账号：</label>
					<div class="col-sm-3" style="left:30px">
						<input type="text" class="form-control input-medium" name="authorAccount" id="authorAccount"  maxlength="50" placeholder="" />
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-1 control-label no-padding-right" for="channelId"><span style="color:red;">*</span>所属频道：</label>
					<div class="col-sm-3">
						<select name="channelId" id="channelId">
							<option value="">请选择</option>
					    	<#list channelList as item>
					    		<option value="${item.id!''}">${item.channelName!''}</option>
					    	</#list>
						</select>
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-1 control-label no-padding-right" for="picCover"><span style="color:red;">*</span>文章封面：</label>
					<div class="col-sm-6 col-xs-12">
                        <div id="filePicker">上传</div>
					</div>
					<input type="hidden" name="picUrl" id="picUrl" value="" />
				</div>
				<div class="form-group" style="height:230px;">
					<div class="col-sm-1"></div>
					<div class="col-sm-9 img" style="height:224px;"></div>
					<div class="col-sm-1"></div>
					<div class="col-sm-9">温馨提示：图片尺寸为290*224，大小不能超过800k</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-1 control-label no-padding-right" for="recommendFlag">是否推荐：</label>
					<div class="col-sm-3" id="tjDiv">
						<input type="radio" name="recommendFlag" value="2">推荐</input>
						<input type="radio" name="recommendFlag" value="1">不推荐</input>
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<!--
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="contentA"><span style="color:red;">*</span>文章内容：</label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-medium" name="contentA" id="contentA"  maxlength="200" placeholder="" />
					</div>
				</div>
				-->
			   <!-- 富文本开始-->
			   <div class="form-group">
               <div class="article-box">
                   <div class="mb10 editor-group">
                       <!--<a href="javascript:;" class="btn btn-yougou btn-sm" data-type="text">添加文字</a>-->
                       <!--<a href="javascript:;" class="btn btn-yougou btn-sm" data-type="image">添加图片</a>-->
                       <!--<a href="javascript:;" class="btn btn-yougou btn-sm" data-type="video">添加视频</a>-->
                       <a href="javascript:;" class="btn btn-yougou btn-sm" data-type="goods">添加商品</a>
                       <a href="javascript:;" class="btn btn-yougou btn-sm" data-type="shop">添加店铺</a>
                       <a href="javascript:;" class="btn btn-yougou btn-sm" data-type="goodsLink">复制商品链接</a>
                   </div>
                   <textarea id="articleEditor" class="form-control input-xlarge" name="articleEditor" cols="80" rows="5"></textarea>
               </div>
               </div>
			   <!-- 富文本结束-->
			</div>
		</form>
		<div id="articleNavbar" style="margin-top:10px;width:500px;text-align:center">
			<button class="btn btn-sm btn-yougou saveArticle2" style="margin-right:30px;">
	        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
	        	发布文章
	        </button>
	        <button class="btn btn-sm btn-yougou saveArticle1" style="margin-right:30px;">
	        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
	        	保存文章
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