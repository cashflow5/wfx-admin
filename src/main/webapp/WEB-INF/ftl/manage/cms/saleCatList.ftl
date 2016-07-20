<@compress single_line=compress_single_line?contains("true")>
<#assign head>
	<!-- zTree -->
	<link rel="stylesheet" href="/static/plugin/zTree/css/bbyStyle/bbyStyle.css">
	<link rel="stylesheet" type="text/css" href="/static/plugin/webuploader/webuploader.css">
	<link rel="stylesheet" href="/static/plugin/fileUpload/ajaxfileupload.css" />
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/plugin/zTree/js/jquery.ztree.all-3.5.min.js"></script>
	<script src="/static/plugin/layer/layer.js"></script>
	<script src="/static/plugin/layer/extend/layer.ext.js"></script>
	<script src="/static/plugin/webuploader/webuploader.js"></script>
	<script src="/static/plugin/fileUpload/ajaxfileupload.js"></script>
	<script src="/static/js/manage/cms/saleCatList.js"></script>
	<script src="/static/js/manage/cms/saleCatZTree.js"></script>
	<style>
		/**定义销售分类表格的图片及文字样式*/
		.imgDiv{height:57px;width:57px;position:relative;margin:0 auto;}
		.catPic{height:57px;}
		img.catPic{height:57px;width:57px;}
		.imgLayer{height:57px;width:57px;display:none;position:absolute;top:0px;left:0px;border:1px solid #333333;
			background-color:#cccccc;filter:alpha(opacity=50);opacity:0.5;color:#FF0000}
		.picTable #sample-table-1 td{line-height:56px;}
		.catName{float:left;}
		.catNum{float:right;}
		#clearCat{clear:both;}
		#bindBasicDiv select.input-sm{width:180px;width:180px!important;margin:3px 10px 3px 0px;}
		.tips{color:#6e6e6e}
		a.saleCatA:link{color:#3da8b9;text-decoration: none;}
		a.saleCatA:hover{color:#3da8b9;text-decoration: underline;}
	</style>
</#assign>

<#assign body>
	<!-- Page Body -->
	<div class="blank10"></div>
	<div class="g-layout-column-left250">
	<div class="g-layout-left">
	    <ul id="SettingNodeList" class="ztree" style="height:350px;">
			<!-- tree start -->
			
			<!-- tree end -->
	    </ul>
	</div>
	<!-- 列表区域 -->
	<div class="g-layout-main">
	    <div id="goodsCategoryList" class="g-layout-container">
	    	<!-- table start -->
			<div id="girdContent" class="row">
				<div class="space-6"></div>
				<div class="col-xs-12">
					<table id="grid-table" class="mmg"></table>
				    <div id="grid-pager" style="text-align:right;" class=""></div>
			    </div>
			</div>
			<!-- table end -->
	    </div>
	</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>