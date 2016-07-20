<@compress single_line=compress_single_line?contains("true")>
<#-- =========head========-->
<#assign head>

</#assign>
	<!-- this page -->
	<script src="/static/js/manage/finance/commissionSetBrand.js"></script>
<#assign footer>

</#assign>
<#assign body>
	<div class="tab-content no-border padding-24">
		<div class="row">
			<div class="col-xs-12 col-sm-12">
				<form class="form-horizontal" id="brandSaveForm">
					<table class="com_modi_table">
						<tr>
							<th>品牌：</th>
							<td >
								<input type="hidden" name="saveBrandNo" id="saveBrandNo"  value="${commissionPercentVo.brandNo?default('')}"/>
								<#if commissionPercentVo.id??>
									<input style="width:220px;" type="text" id="saveBrandName" name="saveBrandName"  value="${commissionPercentVo.brandName?default('')}"  readonly/>
								<#else>	
									<input style="width:220px;" type="text" id="saveBrandName" name="saveBrandName"  value=""  readonly  onClick="getBrandList();"/>
								</#if>	
							</td>
						</tr>
						<tr>
							<th style="height:15px;"></th>
							<td style="vertical-align:top;padding-top:5px;">
							<span id="brandNo_stip" style="color:red"></span>
							</td>
						</tr>
						<tr>
							<th style="vertical-align:top;">
							商品分类：
							</th>
						<#if commissionPercentVo.id??>
							<td style="vertical-align:top;">
								<select  name="baseCatId1" id="baseCatId1" disabled style="width:150px">
									<#if firstCatDto??>
					                	<option value="${firstCatDto.id?default('')}" selected>${firstCatDto.catName?default('')}</option>
					                <#else>
					                	<option value="">请选择</option>	
					                </#if>
					            </select>
					            <select  name="baseCatId2" id="baseCatId2" disabled style="width:150px">
					                <#if sencondCatDto??>
					                	<option value="${sencondCatDto.id?default('')}" selected>${sencondCatDto.catName?default('')}</option>
					                <#else>
					                	<option value="">请选择</option>
					                </#if>
					            </select>
							</td>
						<#else>	
							<td style="vertical-align:top;">
								<select  name="baseCatId1" id="baseCatId1" onchange="getBaseCatId(this.value,'2','');" style="width:150px">
					                <option value="">请选择</option>
					            </select>
					            <select  name="baseCatId2" id="baseCatId2" style="width:150px">
					            	<option value="">请选择</option>
					            </select>
							</td>
						</#if>	
						</tr>
						<tr>
							<th style="height:15px;"></th>
							<td style="vertical-align:top;padding-top:5px;">
							<span id="baseCatId1_stip" style="color:red"></span>
							</td>
						</tr>
						<tr>
							<th>一级佣金比例：</th>
							<td >
								<input type="text" id="commissionLevel1Percent" name="commissionLevel1Percent" placeholder="不超过两位的正整数或保留两位的小数,且不为0" 
								style="width:220px;" 
								value="${commissionPercentVo.commissionLevel1Percent?default('')}" onblur="validate();"/>%
							</td>
						</tr>
						<tr>
							<th style="height:15px;"></th>
							<td style="vertical-align:top;padding-top:5px;">
							<span id="commissionLevel1Percent_stip" style="color:red"></span>
							</td>
						</tr>
						<tr>
							<th>二级佣金比例：</th>
							<td >
								<input type="text" id="commissionLevel2Percent" name="commissionLevel2Percent" placeholder="不超过两位的正整数或保留两位的小数,且不为0" 
								style="width:220px;"
								value="${commissionPercentVo.commissionLevel2Percent?default('')}" onblur="validate();"/>%
							</td>
						</tr>
						<tr>
							<th style="height:15px;"></th>
							<td style="vertical-align:top;padding-top:5px;">
							<span id="commissionLevel2Percent_stip" style="color:red"></span>
							</td>
						</tr>
						<tr>
							<th>三级佣金比例：</th>
							<td >
								<input type="text" id="commissionLevel3Percent" name="commissionLevel3Percent" placeholder="不超过两位的正整数或保留两位的小数,且不为0" 
								style="width:220px;"
								value="${commissionPercentVo.commissionLevel3Percent?default('')}" onblur="validate();"/>%
							</td>
						</tr>
						<tr>
							<th style="height:15px;"></th>
							<td style="vertical-align:top;padding-top:5px;">
							<span id="commissionLevel3Percent_stip" style="color:red"></span>
							</td>
						</tr>
						<tr>
						<th>
						</th>
							<td style="padding-top:10px;">
								<input type="hidden" value="${commissionPercentVo.id?default('')}" id="id" name="id"/> 
							</td>
						</tr>
						<tr>
						<th>
						</th>
							<td style="padding-top:10px;">
								<input type="button" value="提交" class="btn btn-sm btn-yougou" onclick="saveCommission();"/>
	                            &nbsp;&nbsp;&nbsp;&nbsp;
	                            <input type="button" value="返回列表" class="btn btn-sm btn-yougou" onclick="history.go(-1)"/>
							</td>
						</tr>
					</table>
				</form>	
			</div>
		</div>
	</div>		
</#assign>
<#-- =========引入模板======= -->
<#include "/include/pageBuilder.ftl" />
</@compress>	
