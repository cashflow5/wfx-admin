<@compress single_line=compress_single_line?contains("true")>
<#assign head>
	<!-- zTree -->
	<link rel="stylesheet" href="/static/plugin/zTree/css/bbyStyle/bbyStyle.css">
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/plugin/zTree/js/jquery.ztree.all-3.5.min.js"></script>
	<script src="/static/plugin/layer/layer.js"></script>
	<script src="/static/plugin/layer/extend/layer.ext.js"></script>
	<script src="/static/js/manage/commodity/basicCatList.js"></script>
	<script src="/static/js/manage/commodity/basicCatZTree.js"></script>
</#assign>

<#assign body>
	<!-- Page Body -->
	<a class="btn btn-sm" href="javascript:void(0);">同步分类</a>
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