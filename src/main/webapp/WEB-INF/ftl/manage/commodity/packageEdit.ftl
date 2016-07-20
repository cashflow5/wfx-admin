<#-- 分销包新增/编辑页面  -->
<@compress single_line=compress_single_line?contains("true")>
<#-- =========head========-->
<#assign head>
	<link rel="stylesheet" type="text/css" href="/static/plugin/webuploader/webuploader.css">
	<link rel="stylesheet" href="/static/plugin/fileUpload/ajaxfileupload.css" />
</#assign>

<#-- =========footer===== -->
<#assign footer>
	<!-- this page -->
	<script type="text/javascript">
		var commodityList = new Array();//关联商品列表
		<#if commodityList??>
	  		<#list commodityList as commodity>
	  			commodityList.push({
	  				"id" : "${commodity.id !}",
	  				"defaultPic" : "${commodity.defaultPic !}",
	  				"commodityName" : "${commodity.commodityName !}",
	  				"catName" : "${commodity.catName !}",
	  				"brandName" : "${commodity.brandName !}",
	  				"supplierCode" : "${commodity.supplierCode !}",
	  				"no" : "${commodity.no !}",
	  				"publicPrice" : "${commodity.publicPrice !}",
	  				"stock" : "${commodity.stock !}",
	  			});
	  		</#list>
  		</#if>
	</script>
	
	<script src="/static/plugin/webuploader/webuploader.js"></script>
	<script src="/static/plugin/fileUpload/ajaxfileupload.js"></script>
	<script src="/static/plugin/mmGrid/wfxLocalPaginator.js"></script>
	<script src="/static/js/manage/commodity/packageInfo.js"></script>
</#assign>

<#-- =========body======= -->
<#assign body>
<!-- 分销包明细信息 -->
<div class="row">
	<div class="col-xs-12">
		<form id="packageInfoForm" class="form-horizontal message-form col-xs-12">
			<input type="hidden" name="id" id="id" value="${(packageInfo.id)!''}"/>
			<div>
				<dl class="c-h-dl dl-basic-field">
					<dt><label class="control-label no-padding-right" for="form-field-recipient"><font color="red">*</font>分销包名称：</label></dt>                    
					<dd>
						<input type="text" id="bagName" name="bagName"  value="${(packageInfo.bagName)!''}" class="form-control input-medium" placeholder="请输入分销包名称"/>
					</dd>                
				</dl>
				
				<dl class="c-h-dl dl-basic-field">
					<dt><label class="control-label no-padding-right" for="form-field-subject"><font color="red">*</font>分销包小图：</label></dt>                    
					<dd>
                    	<a id="selectSmallPic" href="javascript:void(0);">选择图片</a>
					</dd>      
				</dl>
				<dl class="c-h-dl dl-basic-field">
	                <dt></dt>
	                <dd>
	                	<div>
	                		<input id="bagSmallPic" type="hidden" name="bagSmallPic" class="form-control input-medium" value="${(packageInfo.bagSmallPic)!''}"/>
	                		<img class="smallImg" src="${(imgBaseUrl)!''}/${(packageInfo.bagSmallPic)!''}" width="300" height="180"/>
	                	</div>
	                	<div>上传图片大小为300*180</div>
	                </dd>   
				</dl>        

				<dl class="c-h-dl dl-basic-field">
					<dt><label class="control-label no-padding-right" for="form-field-subject"><font color="red">*</font>分销包大图：</label></dt>                    
					<dd>
                    	<a id="selectBigPic" href="javascript:void(0);">选择图片</a>
					</dd>       
				</dl>
				<dl class="c-h-dl dl-basic-field">
					<dt></dt>
	                <dd>
	                	<div>
	                		<input id="bagBigPic" type="hidden" name="bagBigPic" class="form-control input-medium" value="${(packageInfo.bagBigPic)!''}" />
	                		<img class="bigImg" src="${(imgBaseUrl)!''}/${(packageInfo.bagBigPic)!''}" width="640" height="240"/>
	                	</div>
	                	<div>上传图片大小为640*240</div>
                	</dd>    
				</dl>        
				
				<dl class="c-h-dl dl-basic-field">
					<dt><label class="control-label no-padding-right" for="form-field-recipient">分销包说明：</label></dt>                    
					<dd>
						<textarea id="comments" name="comments" maxlength="100" class="form-control" style="overflow: hidden; margin: 0px;width: 400px; height: 60px; resize: horizontal;">
							${(packageInfo.comments)!''}
						</textarea>
					</dd>            
				</dl>
				
				<dl class="c-h-dl dl-basic-field">
					<dt>
						<label class="control-label no-padding-right" for="form-field-recipient"><font color="red">*</font>序号：</label>
					</dt>                    
					<dd>
						<input type="text" id="sortNo" name="sortNo" class="form-control input-medium" placeholder="请输入序号" value="${(packageInfo.sortNo)!''}" />
					</dd>                
				</dl>
				
				<dl class="c-h-dl dl-basic-field">
					<dt>
						<label class="control-label no-padding-right" for="form-field-recipient"><font color="red">*</font>分销包商品：</label>
					</dt>                    
					<dd class="commodity-field-toolbar">
						<a class="btn btn-xs btn-yougou mr5 add-commodity" href="javascript:void(0);">添 加</a>
						<a class="btn btn-xs btn-yougou mr5 inport-commodity">导 入</a>
						<a class="btn btn-xs btn-yougou mr5 delete-commodity">批量取消</a>
					</dd>                
				</dl>
				<dl class="c-h-dl dl-basic-field">
					<dt></dt>  
					<dd style="width:930px;">
						<input type="hidden" name="commodityIds" id="commodityIds" value=""/>
						<table id="select-commodity-table"></table>
						<div id="select-commodity-table-pager" style="text-align:right;"></div>
					</dd> 
				</dl>
				
				<dl class="c-h-dl dl-basic-field">
					<dt> </dt>                    
					<dd>
						<a class="btn btn-sm btn-yougou mr5 save-package" href="javascript:void(0);">提 交</a>
						<a href="javascript:history.back()" class="btn btn-sm btn-yougou return">
							<i class="ace-icon fa fa-arrow-left"></i>
							返回
						</a>
					</dd>                
				</dl>		
			</div>
		</form>
	</div>
</div>

<!-- 分销包的商品操作 -->
<div id="commodityList" class="modal fade bs-example-modal-lg" role="dialog">
	<div class="modal-dialog modal-lg" role="document" style="width:1250px">
		<div class="modal-content" >
      		<div class="modal-header"> 
      			<div style="height:50px;">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h5 class="modal-title" id="myModalLabel">添加商品</h5>
		      	</div>
      			<!-- 搜索表单，需自定义 -->
				<form class="form-horizontal" id="searchCommodityForm">
					<input class="form-control input-sm" name="isWfxCommodity" value="1" type="hidden">
			        <fieldset>
			            <div class="row">
			                <div>
			                    <div class="form-group">
			                    	<label for="memberShip" class="col-sm-1 control-label no-padding-right"> 商品分类：</label>
									<div class="col-sm-2">
							            <select class="input-sm" id="levelone" name="wfxLevelOne" onchange="catChange('1',this);">
											<option value="">请选择</option>
										</select>
							        </div>
							        <div class="col-sm-2">
							           
										<select class="input-sm" id="leveltwo" name="wfxLevelTwo" onchange="catChange('2',this);">
											<option value="">请选择</option>
										</select>
										 
							        </div>
							        <div class="col-sm-1">
							            <select class="input-sm" id="levelthree" name="wfxLevelThree" onchange="catChange('3',this);">
											<option value="">请选择</option>
										</select>
							        </div>
							        <label for="operator" class="col-sm-1 control-label no-padding-right">品牌：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="brandName" id="brandName" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label no-padding-right">商品款号：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="styleNo" id="styleNo" value="" type="text">
									</div>
								</div>								
								<div class="form-group">
									<label for="operator" class="col-sm-1 control-label no-padding-right">商品编码：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="no" id="no" value="" type="text">
									</div>
									<label for="operator" class="col-sm-1 control-label no-padding-right">款色编码：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="supplierCode" id="supplierCode" value="" type="text">
									</div>

									<div class="col-sm-2">
			                            <input type="button" value="搜索" class="btn btn-sm search"/>
									</div>
			                    </div>
			                </div>
			            </div>
			        </fieldset>
				</form>
				
      		</div>
      		<div class="modal-body">
      			<div>
					<table id="commodity-table" class="mmg"></table>
    				<div id="commodity-pager" style="text-align:right;"></div>
				</div>
			</div>
			<div class="modal-footer">
				<a class="btn mr5 batch-add-commodity" data-dismiss="modal" href="javascript:void(0);">批量添加</a>
        		<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      		</div>
		</div>
	</div>
</div>

<!-- 分销包的导入商品操作 -->
<div id="inportCommodityPanel" class="modal fade bs-example-modal-lg" role="dialog">
	<div class="modal-dialog modal-lg" role="document" style="width:500px">
		<div class="modal-content" >
      		<div class="modal-header"> 
      			<div style="height:50px;">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h5 class="modal-title" id="myModalLabel">导入商品</h5>
		      	</div>
      		</div>
      		<div class="modal-body">
      			<form class="form-horizontal" id="importCommodityForm">
			        <dl class="c-h-dl dl-basic-field">
						<dt><label class="control-label no-padding-right" for="form-field-subject"><font color="red">*</font>分销包小图：</label></dt>                    
						<dd>
							<div id="selectExecleFile" style="float:left;">选择文件</div>
							<div id="fileName" style="float:left;"></div>
	                    	<a id="downloadTemp" href="javascript:void(0);" style="float:left;margin-top:8px;">下载模板</a>
						</dd>      
					</dl>
					<a id="importCommodity" class="btn btn-yougou mr5" data-dismiss="modal" href="javascript:void(0);">导入</a>
				</form>
			</div>
			<div class="modal-footer">
				注意：导入的excel的大小不能超过1M,建议一个excel商品数量不大于500个
      		</div>
		</div>
	</div>
</div>
</#assign>

<#-- =========引入模板======= -->
<#include "/include/pageBuilder.ftl" />
</@compress>