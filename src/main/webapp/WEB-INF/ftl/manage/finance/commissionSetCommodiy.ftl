<@compress single_line=compress_single_line?contains("true")>
<#-- =========head========-->
<#assign head>

</#assign>
	<!-- this page -->
	<script src="/static/js/manage/finance/commissionSetCommodity.js"></script>
<#assign footer>

</#assign>
<#assign body>
	<div class="tab-content no-border padding-24">
		<div class="row">
			<div class="col-xs-12 col-sm-12">
				<form class="form-horizontal" id="commoditySaveForm">
					<table class="com_modi_table">
					<#if commissionPercentVo.id??>
						<tr>
							<td  colspan="3">
								 <table class="table-ex tcenter table-padding" width="100%">
									 <thead>
                   						 <tr>
										<th >
											 缩略图
										</th>
										<th >
											款色编码
										</th>
										<th >
											商品名称
										</th>
									</tr>
									</thead>
									<tr>
										<td >
										<img width="40" height="39" alt="" src="${commissionPercentVo.picBig?default('')}"/>
										</td>
										<td >
											${commissionPercentVo.supplierCode?default('')}
										</td>
										<td >
											${commissionPercentVo.commodityName?default('')}
										</td>
									</tr>
								</table>
							</td>
						</tr>
					<#else>	
						<tr>
							<th style="vertical-align:top;">
							款色编码：
							</th>
							<td style="vertical-align:top;">
								<textarea rows="5" cols="60"  id="supplierCode" name="supplierCode" placeholder="多个款色编码请用   , 隔开款色编码填写不能超过200个。" >
								</textarea>
							</td>
							<td align="left">
								<input type="button" value="查询" class="btn btn-sm btn-yougou" onclick="doQuery();"/>
							</td>
						</tr>
						<tr>
							<th style="height:15px;"></th>
							<td style="vertical-align:top;padding-top:5px;">
							<span id="commodityId_stip" style="color:red"></span>
							</td>
						</tr>
						<tr>
							<th style="vertical-align:top;">
							&nbsp;&nbsp;&nbsp;
							</th>
							<td colspan="2" style="width:700px">
								<div style="width:800px">
									<table id="commodityList-grid-table" class="mmg"></table>
							    </div>
							</td>
						</tr>
					</#if>	
						<tr>
							<th style="height:15px;"></th>
							<td style="vertical-align:top;padding-top:5px;" colspan="2">
							<span id="baseCatId1_stip" style="color:red"></span>
							</td>
						</tr>
						<tr>
							<th>一级佣金比例：</th>
							<td colspan="2">
								<input type="text" id="commissionLevel1Percent" name="commissionLevel1Percent" placeholder="不超过两位的正整数或保留两位的小数,且不为0" 
								style="width:220px;" 
								value="${commissionPercentVo.commissionLevel1Percent?default('')}" onblur="validate();"/>%
							</td>
						</tr>
						<tr>
							<th style="height:15px;"></th>
							<td style="vertical-align:top;padding-top:5px;" colspan="2">
							<span id="commissionLevel1Percent_stip" style="color:red"></span>
							</td>
						</tr>
						<tr>
							<th>二级佣金比例：</th>
							<td colspan="2">
								<input type="text" id="commissionLevel2Percent" name="commissionLevel2Percent" placeholder="不超过两位的正整数或保留两位的小数,且不为0" 
								style="width:220px;"
								value="${commissionPercentVo.commissionLevel2Percent?default('')}" onblur="validate();"/>%
							</td>
						</tr>
						<tr>
							<th style="height:15px;"></th>
							<td style="vertical-align:top;padding-top:5px;" colspan="2">
							<span id="commissionLevel2Percent_stip" style="color:red"></span>
							</td>
						</tr>
						<tr>
							<th>三级佣金比例：</th>
							<td colspan="2">
								<input type="text" id="commissionLevel3Percent" name="commissionLevel3Percent" placeholder="不超过两位的正整数或保留两位的小数,且不为0" 
								style="width:220px;"
								value="${commissionPercentVo.commissionLevel3Percent?default('')}" onblur="validate();"/>%
							</td>
						</tr>
						<tr>
							<th style="height:15px;"></th>
							<td style="vertical-align:top;padding-top:5px;" colspan="2">
							<span id="commissionLevel3Percent_stip" style="color:red"></span>
							</td>
						</tr>
						<tr>
							<th>
							&nbsp;
							</th>
							<td style="padding-top:10px;" colspan="2">
								<input type="hidden" value="${commissionPercentVo.id?default('')}" id="id" name="id"/> 
							</td>
						</tr>
						<tr>
							<th>&nbsp;&nbsp;
							</th>
							<td style="padding-top:10px;" >
								<input type="button" value="提交" class="btn btn-sm btn-yougou" onclick="saveCommission();"/>
	                            &nbsp;&nbsp;&nbsp;&nbsp;
	                            <input type="button" value="返回列表" class="btn btn-sm btn-yougou" onclick="history.go(-1)"/>
							</td>
							<td>&nbsp;&nbsp;</td>
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
