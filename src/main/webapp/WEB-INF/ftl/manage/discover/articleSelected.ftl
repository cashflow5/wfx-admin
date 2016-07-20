
<div id="article-girdContent" class="row" style="width:1245px">
	<div class="col-xs-12">
	<form name="articleForm" id="articleForm" method="post" action="" class="form-horizontal" >
		<div class="search-content-wrap">
	    	<ul class="c-h-ul search-ul query-ul">
	            <li>
	                <label class="c-h-ul-li-title">文章标题：</label>
	                <input type="text" id="title" name="title" value="" class="input-medium" style="width:120px !important"/>
	            </li>
	            <li>
	                <label class="c-h-ul-li-title">文章作者：</label>
	                <input type="text" id="authorAccount" name="authorAccount" value="" class="input-medium" style="width:120px !important"/>
	            </li>
	            <li>
	                <label class="c-h-ul-li-title">频道名称：</label>
	                <select name="channelId" id="channelId2" style="width:120px !important">
				    	<option value="">全选</option>
				    	<#list channelList as item>
				    		<option value="${item.id!''}">${item.channelName!''}</option>
				    	</#list>
				    </select>
	            </li>
	            <li>
	                <label class="c-h-ul-li-title">文章ID：</label>
	                <input type="text" id="no" name="no" value="" class="input-medium" style="width:120px !important"/>
	            </li>
	            <li>
	                <label class="c-h-ul-li-title"></label>
	                <input type="button" value="查询" class="btn btn-sm btn-yougou" onclick="doQuery();"/>&nbsp;&nbsp;
	            </li>
	        </ul>
		</div>
	</form>
	
	<div class="space-6"></div>
		<table id="article-grid-table" class="mmg"></table>
		<div id="article-grid-pager" style="text-align:right;" class=""></div>
	<div>
</div>

<script src="/static/js/manage/discover/articleSelected.js"></script>