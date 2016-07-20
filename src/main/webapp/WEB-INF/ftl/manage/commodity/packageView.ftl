<#-- 分销包查看页面  -->
<@compress single_line=compress_single_line?contains("true")>
<#-- =========head========-->
<#assign head></#assign>

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
	<script src="/static/plugin/mmGrid/wfxLocalPaginator.js"></script>
	<script src="/static/js/manage/commodity/packageView.js"></script>
</#assign>

<#-- =========body======= -->
<#assign body>
<!-- 分销包明细信息 -->
<div class="row">
	<div class="col-xs-12">
		<div>
			<dl class="c-h-dl dl-basic-field">
				<dt><label class="control-label no-padding-right" for="form-field-recipient">分销包名称：</label></dt>                    
				<dd>
					<span>${(packageInfo.bagName)!''}</span>
				</dd>                
			</dl>
			
			<dl class="c-h-dl dl-basic-field">
				<dt><label class="control-label no-padding-right" for="form-field-subject">分销包小图：</label></dt>                    
				<dd>
                	<div>
                		<img class="smallImg" src="${(imgBaseUrl)!''}/${(packageInfo.bagSmallPic)!''}" width="300" height="180"/>
                	</div>
                	<div>上传图片大小为300*180</div>
				</dd>      
			</dl>

			<dl class="c-h-dl dl-basic-field">
				<dt><label class="control-label no-padding-right" for="form-field-subject"><font color="red">*</font>分销包大图：</label></dt>                    
				<dd>
					<div>
                		<img class="bigImg" src="${(imgBaseUrl)!''}/${(packageInfo.bagBigPic)!''}" width="640" height="240"/>
                	</div>
                	<div>上传图片大小为640*240</div>
				</dd>       
			</dl>

			<dl class="c-h-dl dl-basic-field">
				<dt><label class="control-label no-padding-right" for="form-field-recipient">分销包说明：</label></dt>                    
				<dd>
					<span>${(packageInfo.comments)!''}</span>
				</dd>            
			</dl>
			
			<dl class="c-h-dl dl-basic-field">
				<dt>
					<label class="control-label no-padding-right" for="form-field-recipient">序号：</label>
				</dt>                    
				<dd>
					<span>${(packageInfo.sortNo)!''}</span>
				</dd>                
			</dl>
			
			<dl class="c-h-dl dl-basic-field">
				<dt>
					<label class="control-label no-padding-right" for="form-field-recipient"><font color="red">*</font>分销包商品：</label>
				</dt>                    
				<dd style="width:930px;">
					<table id="select-commodity-table"></table>
					<div id="select-commodity-table-pager" style="text-align:right;"></div>
				</dd>    
			</dl>
			
			<dl class="c-h-dl dl-basic-field">
				<dt> </dt>                    
				<dd>
					<a href="javascript:history.back()" class="btn btn-sm btn-yougou return">
						<i class="ace-icon fa fa-arrow-left"></i>
						返回
					</a>
				</dd>                
			</dl>		
		</div>
	</div>
</div>
</#assign>

<#-- =========引入模板======= -->
<#include "/include/pageBuilder.ftl" />
</@compress>