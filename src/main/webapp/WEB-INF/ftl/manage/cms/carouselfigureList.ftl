<@compress single_line=compress_single_line?contains("true")>
<#assign head>
	<link rel="stylesheet" type="text/css" href="/static/plugin/webuploader/webuploader.css">
	<style>
		.hotSnGroup{text-align:right;overflow:hidden;padding-right:25px}
		.hotCatPic{height:36px;width:auto;margin:5px;}
		.hotSnGroup .hotSnText{display:inline-block;width:60px;text-align:right;overflow:hidden;height:17px}
		.hotSnGroup .hotSnInput{width:40px;text-align:right;}
		.hotSnGroup a{display:inline-block;width:16px;margin-left:10px;}
		.hotSnGroup .editIcon{background:transparent url(/static/images/icon_edit.png) no-repeat right;}
		.hotSnGroup .saveIcon{background:transparent url(/static/images/icon_save.png) no-repeat right;}
		.hotSnGroup .cancelIcon{background:transparent url(/static/images/del-class.gif) no-repeat right;}
	</style>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/plugin/webuploader/webuploader.js"></script>
	<script src="/static/js/manage/cms/carouselfigureList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
		<button class="btn btn-yougou" onclick="showForm();">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	新增
        </button>
        <button class="btn btn-yougou" onclick="batchDelete();">
        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
        	批量删除
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
		                        	<label for="typeName" class="col-sm-1 control-label col-xs-12 no-padding-right">轮播图类型：</label>
									<div class="col-sm-2">
									    <select class="form-control input-small"  name="type"  >
											<option value="">全部</option>
											<#list carouseFigureTypeEnumList as item>
												<option value="${item.key}">${item.desc}</option>
											</#list>		
										</select>
									</div>
									<label for="redirectType" class="col-sm-1 control-label col-xs-12 no-padding-right">跳转类型：</label>
									<div class="col-sm-2">
										 <select class="form-control input-small"  name="redirectType"  id="redirectType" >
											 <option value="">全部</option>
											 <#list noticeRedirectTypeEnumList as item>
											   <option value="${item.key}">${item.desc}</option>
											</#list>		
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


<!-- 轮播图新增或者修改页面 -->
<div class="row">
	<div class="col-xs-12">
		<div id="packageNavbar" class="hide message-navbar clearfix">
			<div class="message-bar">
				<div class="message-toolbar">
					<button type="button" class="btn btn-xs btn-white btn-yougou" onclick="addCarouselfigure();">
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
		
		<input type="hidden" name="imgBaseUrl" id="imgBaseUrl" value="${imgBaseUrl!""}"/>
		<form id="carouselForm" class="hide form-horizontal message-form col-xs-12">
			<input type="hidden" name="id" id="id" value="${id!""}"/>
			<div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="form-field-recipient"><font color="red">*</font>轮播名称：</label>
					<div class="col-sm-2">
						 <input type="text" id="name" name="name" placeholder="请输入轮播图名称" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="form-field-recipient"><font color="red">*</font>轮播图类型：</label>
					<div class="col-sm-2">
						<select class="form-control input-small"  name="type"  id="type" onchange="showCounts(this.value)">
							<option value="">请选择</option>
							<#list carouseFigureTypeEnumList as item>
								<option value="${item.key}">${item.desc}</option>
							</#list>		
						</select>
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" ><span style="color:red;">*</span>跳转类型：</label>
					<div class="col-sm-3">
						<select class="form-control input-small"  name="redirectType"  id="redirectType_" onchange="chooseSelect();">
							<option value="">请选择</option>
							<#list noticeRedirectTypeEnumList as item>
								<option value="${item.key}">${item.desc}</option>
							</#list>		
						</select>
					</div>
				</div>	
				
				<div  id="build_redirectUrl_div_article" style="">
					<div class="hr hr-10 dotted"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" style="padding-top:0px;">
				        	<button onclick="addArticle();return false;" style="height:29px;">添加链接文章</button>
	        			</label>
						<div class="col-sm-3">
							<span id="articleTips" style="font-size:20;line-height:29px;"></span>
						</div>
					</div>
				</div>
				<div  id="build_redirectUrl_div_commodity" style="">
					<div class="hr hr-10 dotted"></div>
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right" style="padding-top:0px;">
				        	<button onclick="addCommodity();return false;" style="height:29px;">添加链接商品</button>
	        			</label>
						<div class="col-sm-3">
							<span id="commodityTips" style="font-size:20;line-height:29px;"></span>
						</div>
					</div>
					</div>
				</div>
				
				<div id="redirectUrl_div" style="">
					<div class="hr hr-10 dotted"></div>	
					<div class="form-group" >
						<label class="col-sm-3 control-label no-padding-right" ><span style="color:red;">*</span>跳转链接</label>
						<div class="col-sm-3">
							<input type="text" class="form-control input-medium" name="linkUrl" id="redirectUrl"  maxlength="100" placeholder="请输入以http://开头的跳转链接" />
						</div>
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="form-field-subject"><font color="red">*</font>上传图片：</label>
					<div class="col-sm-6 col-xs-12">
                        <div id="filePicker1">上传</div>
					</div>
					<input type="hidden" name="picUrl" id="picUrl"  />
				</div>
				<div class="form-group">
					<div class="col-sm-3"></div>
					<div class="col-sm-9 img1" style="height:190px">
						<#-- <img id="carPic" src="" style="position:absolute;left: 0px;top:0px;width:439px;height:185px;" /> -->
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="form-field-recipient"><font color="red">*</font>排序号：</label>
					<div class="col-sm-2">
						<input type="text" id="sortNo" name="sortNo" placeholder="请输入排序号" />
						<div style="color:red">当前轮播图类型最大排序号：<span id="maxSortNo" ></span> </div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>