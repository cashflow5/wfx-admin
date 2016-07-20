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
	<script src="/static/js/manage/notice/noticeList.js"></script>
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
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">标题：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="title" id="title" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">跳转类型：</label>
									<div class="col-sm-2">
										 <select class="form-control input-small"  name="redirectType"  id="redirectType" >
											 <option value="">全部</option>
											 <#list noticeRedirectTypeEnumList as item>
											   <option value="${item.key}">${item.desc}</option>
											</#list>		
						                </select>
									</div>
									<label for="updateTimeStart" class="col-sm-1 control-label col-xs-12 no-padding-right">有效时间：</label>
									<div class="col-sm-2">
									    <input class="input-medium" name="effectiveTime" id="effectiveTime_1" readonly="readonly" value="" type="text">
									</div>
		                        	<label for="createdTimeEnd" class="col-sm-1 control-label col-xs-12 no-padding-right">—</label>
									<div class="col-sm-2">
									    <input class="input-medium" name="failureTime" id="failureTime_1" readonly="readonly" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">状态：</label>
									<div class="col-sm-2">
										 <select class="form-control input-small"  name="status"  id="status" >
										     <option value="0">全部</option>
											 <#list noticeStatusEnumList as item>
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



<!-- 编辑页面 -->
<div class="row">
	<div class="col-xs-12">
		<div id="noticeNavbar" class="hide message-navbar clearfix">
			<div class="message-bar">
				<div class="message-toolbar">
					<button type="button" class="btn btn-xs btn-white btn-yougou" onclick="saveNotice();">
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
		
		<form id="noticeForm" class="hide form-horizontal message-form col-xs-12">
			<input type="hidden" name="id" id="id" />
			<div>
				<div class="blank6" />
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" ><span style="color:red;">*</span>标题：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control input-medium" name="title" id="title_"  maxlength="50" placeholder="建议不要超过15个汉字" />
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
							<input type="text" class="form-control input-medium" name="redirectUrl" id="redirectUrl"  maxlength="100" placeholder="请输入以http://开头的跳转链接" />
						</div>
					</div>
				</div>
				
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" ><span style="color:red;">*</span>排序：</label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-medium" name="sort" id="sort_"  maxlength="50" placeholder="请填数字，数字越大，排名越靠前" />
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" ><span style="color:red;">*</span>有效时间：</label>
					<div class="col-sm-2">
						 <input name="stringEffectiveTime" id="effectiveTime_" readonly="readonly"   class="input-medium" type="text">
						 <input name="effectiveTime" id="effectiveTime"  readonly="readonly"   class="input-medium" type="hidden">
					</div>
					<div class="col-sm-1">
					<label>—</label>
					</div>
					<div class="col-sm-2">
						 <input name="stringFailureTime" id="failureTime_" readonly="readonly"   class="input-medium" type="text">
						 <input name="failureTime" id="failureTime" readonly="readonly"   class="input-medium" type="hidden">
					</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>