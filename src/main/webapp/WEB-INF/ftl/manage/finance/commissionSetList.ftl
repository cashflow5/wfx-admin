<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/finance/commissionSetList.js"></script>
</#assign>

<#assign body>
<div class="row" id="girdContent">
	<div class="col-xs-12">
		<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab">
				<li <#if type==1>class="active"</#if>><a href="/finance/commissionSetList.sc?type=1" >默认佣金比例设置</a></li>
				<li <#if type==2>class="active"</#if>><a href="/finance/commissionSetList.sc?type=2" >品牌分类佣金比例设置</a></li>
				<li <#if type==3>class="active"</#if>><a href="/finance/commissionSetList.sc?type=3" >单品佣金比例设置</a></li>
				<li <#if type==4>class="active"</#if>><a href="/finance/commissionSetList.sc?type=4" >操作日志</a></li>
			</ul>
		<div class="tab-content">
		 <#if type==1>
			<!-- 默认比例设置start -->
			<div id="defaultCommissionSet" class="tab-pane active">
				<form class="form-horizontal" id="defaultCommissionForm">
				   <input name="type" id="type" value="1" type="hidden">	
		           <fieldset>
	               		<table>
	               		<input id="defaultCommission1" value="${commissionPercentVo.commissionLevel1Percent?default(0)}" type="hidden"/>
						<input id="defaultCommission2" value="${commissionPercentVo.commissionLevel2Percent?default(0)}" type="hidden"/>
						<input id="defaultCommission3" value="${commissionPercentVo.commissionLevel3Percent?default(0)}" type="hidden"/>
	               		<input id="defaultCommissionId" name="defaultCommissionId" value="${commissionPercentVo.id?default(0)}" type="hidden"/>
	               			<tr>
	               				<td colspan="2"><font style="color:red;">默认佣金比例：指默认所有商品的佣金比例设置</font></td>
	               			</tr>
	               			<tr>
	               				<td colspan="2">&nbsp;&nbsp;&nbsp;</td>
	               			</tr>
	               			<tr>
	               				<th>一级佣金比例：</th>
	               				<td align="left" id="commissionTD1">
	               					<font>${commissionPercentVo.commissionLevel1Percent?default(0)}</font>%
								</td>
	               			</tr>
	               			<tr>
	               				<td colspan="2">&nbsp;&nbsp;&nbsp;</td>
	               			</tr>
	               			<tr>
	               				<th>二级佣金比例：</th>
	               				<td align="left" id="commissionTD2">
	               				 	<font>${commissionPercentVo.commissionLevel2Percent?default(0)}</font>%
								</td>
	               			</tr>
	               			<tr>
	               				<td colspan="2">&nbsp;&nbsp;&nbsp;</td>
	               			</tr>
	               			<tr>
	               				<th>三级佣金比例：</th>
	               				<td align="left" id="commissionTD3">
	               				 	<font>${commissionPercentVo.commissionLevel3Percent?default(0)}</font>%
								</td>
	               			</tr>
	               			<tr>
	               				<td colspan="2">&nbsp;&nbsp;&nbsp;</td>
	               			</tr>
	               			<tr>
	               				<td>&nbsp;&nbsp;</td>
	               				<td align="left" id="setBtn">
	               					<input type="button" value="设置" class="btn btn-yougou" onclick="defaultCommissionSetBtn();"/>
								</td>
	               			</tr>
	               		</table>
		           </fieldset>
		       	</form>
			</div>
			<!-- 默认比例设置解锁 end -->
		</#if>
		<#if type==2>
		 	<!-- 分类比例设置start -->
			<div id="categoryCommissionSet" class="tab-pane active">
            	<form class="form-horizontal" id="categoryCommissionForm">
            	<input name="type" id="type" value="2" type="hidden">	
	            	<fieldset>
		               <div class="row">
                    		<div class="col-sm-12">
		                        <div class="form-group">
		                        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                        </div>
	                       		<div class="form-group">
	                       			<label for="baseCatId" class="col-sm-1 control-label no-padding-right">品牌名称：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="brandName" id="brandName"  type="text">
									</div>
	                       			<label for="baseCatId" class="col-sm-1 control-label no-padding-right">分类名称：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="catName" id="catName"  type="text">
									</div>
			                        <div class="col-sm-2">
			                            <input type="button" value="搜索" class="btn btn-sm btn-yougou" onclick="doQuery();"/>
			                            &nbsp;&nbsp;&nbsp;&nbsp;
			                            <input type="button" value="新增" class="btn btn-sm btn-yougou" id="add-quick"/>
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
			<!-- 分类比例设置 end -->
		</#if>
		<#if type==3>
		<!-- 单品比例设置start -->
			<div id="categoryCommissionSet" class="tab-pane active">
            	<form class="form-horizontal" id="categoryCommissionForm">
            	<input name="type" id="type" value="3" type="hidden">	
	            	<fieldset>
		               <div class="row">
                    		<div class="col-sm-12">
		                        <div class="form-group">
		                        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                        </div>
	                       		<div class="form-group">
	                       			<label for="baseCatId" class="col-sm-1 control-label no-padding-right">款色编码：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="supplierCode" id="supplierCode"  type="text">
									</div>
	                       			<label for="baseCatId" class="col-sm-1 control-label no-padding-right">商品名称：</label>
									<div class="col-sm-2">
									    <input class="form-control input-sm" name="commodityName" id="commodityName"  type="text">
									</div>
			                        <div class="col-sm-2">
			                            <input type="button" value="搜索" class="btn btn-sm btn-yougou" onclick="doQuery();"/>
			                            &nbsp;&nbsp;&nbsp;&nbsp;
			                            <input type="button" value="新增" class="btn btn-sm btn-yougou" id="add-quick"/>
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
			<!-- 单品比例设置 end -->
		</#if>
		</div>
	</div>
  </div>	
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>