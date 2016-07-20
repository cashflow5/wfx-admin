<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/finance/commissionSetLogList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
	<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab">
				<li <#if type==1>class="active"</#if>><a href="/finance/commissionSetList.sc?type=1" >默认佣金比例设置</a></li>
				<li <#if type==2>class="active"</#if>><a href="/finance/commissionSetList.sc?type=2" >品牌分类佣金比例设置</a></li>
				<li <#if type==3>class="active"</#if>><a href="/finance/commissionSetList.sc?type=3" >单品佣金比例设置</a></li>
				<li <#if type==4>class="active"</#if>><a href="/finance/commissionSetList.sc?type=4" >操作日志</a></li>
			</ul>
		<div class="tab-content">
          <form class="form-horizontal" id="searchForm">
           	 <fieldset>
               <div class="row">
                  	<div class="col-sm-12">
                        <div class="form-group">
                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">业务类型：</label>
							<div class="col-sm-2">
							    <select class="form-control input-sm" name="commissionType" id="commissionType" style="width:140px !important">
					                <option value="">请选择</option>
					                <option value="1">默认佣金</option>
					                <option value="2">品牌分类佣金</option>
					                <option value="3">单品佣金</option>
					            </select>
							</div>
							<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">日志类型：</label>
							<div class="col-sm-2">
							    <select class="form-control input-sm" name="operateType" id="operateType">
					                <option value="">请选择</option>
					                <option value="1">新增</option>
					                <option value="2">修改</option>
					                <option value="3">删除</option>
					            </select>
							</div>
							<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">操作时间：</label>
							<div class="col-sm-4">
							    <input type="text" name="startTime" id="startTime" readonly="readonly" value="" class="input-medium" style="width:125px !important">
							    <label>至</label>
							    <input type="text" name="endTime" id="endTime" readonly="readonly" value="" class="input-medium" style="width:125px !important">
							</div>
						</div>
                   		<div class="form-group">
                   			<label for="baseCatId" class="col-sm-1 control-label no-padding-right">操作人：</label>
							<div class="col-sm-2">
							    <input class="form-control input-sm" name="createUser" id="createUser" value="" type="text" style="width:140px !important">
							</div>
		                       <div class="col-sm-6">
		                           <input type="button" value="搜索" class="btn btn-sm btn-yougou" onclick="doQuery();"/>
							</div>
                   		</div>
                    </div>
                </div>
            </fieldset>
        </form>
		  <div class="space-1"></div>&nbsp;
		  <table id="grid-table" class="mmg"></table>
          <div id="grid-pager" style="text-align:right;" class=""></div>
       </div>
    </div>
    </div>
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>