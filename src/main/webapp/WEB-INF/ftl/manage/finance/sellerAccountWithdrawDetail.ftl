<div id="girdContent" class="row" >
	<div class="col-xs-12">
		<form name="auditform" id="auditform" method="Post" action="">
			<input name="id" id="idAudit" type="hidden" value="${(widthDrawVo.id)!""}" >
			<table >
				<tr>
					<td style="text-align: right;">提现申请单号：</td><td>&nbsp;&nbsp;<font style="font-weight:bold">${(widthDrawVo.withdrawApplyNo)!""}</font></td>
				</tr>
				<tr>
					<td style="text-align: right;">店铺名称：</td><td>&nbsp;&nbsp;<font style="font-weight:bold">${(widthDrawVo.shopName)!""}</font></td>
				</tr>
				<tr>
					<td style="text-align: right;">分销商账号：</td><td>&nbsp;&nbsp;<font style="font-weight:bold">${(widthDrawVo.sellerAccount)!""}</font></td>
				</tr>
				<tr>
					<td style="text-align: right;">账户余额：</td><td>&nbsp;&nbsp;<font style="font-weight:bold" color="red">${(widthDrawVo.accountBalance)!""}</font></td>
				</tr>
				<tr>
					<td style="text-align: right;">提现金额：</td><td>&nbsp;&nbsp;<font style="font-weight:bold" color="red">${(widthDrawVo.withdrawAmount)!""}</font></td>
				</tr>
				<tr>
					<td style="text-align: right;">服务费：</td><td>&nbsp;&nbsp;<font style="font-weight:bold" color="red">${(widthDrawVo.serviceAmount)!""}</font></td>
				</tr>
				<tr>
					<td style="text-align: right;">实收：</td><td>&nbsp;&nbsp;<font style="font-weight:bold" color="red">${(widthDrawVo.actualReceivedAmount)!""}</font></td>
				</tr>
				<tr>
					<td style="text-align: right;">支付方式：</td><td>&nbsp;&nbsp;<font style="font-weight:bold">${(widthDrawVo.accountBankName)!""}</font></td>
				</tr>
				<tr>
					<td style="text-align: right;">对方开户行：</td><td>&nbsp;&nbsp;<font style="font-weight:bold">${(widthDrawVo.accountBankAllName)!""}</font></td>
				</tr>
				<tr>
					<td style="text-align: right;">开户名：</td><td>&nbsp;&nbsp;<font style="font-weight:bold">${(widthDrawVo.accountName)!""}</font></td>
				</tr>
				<tr>
					<td style="text-align: right;">账户：</td><td>&nbsp;&nbsp;<font style="font-weight:bold">${(widthDrawVo.accountNo)!""}</font></td>
				</tr>
				
				<tr>
					<td style="text-align: right;">申请人：</td><td>&nbsp;&nbsp;<font style="font-weight:bold">${(widthDrawVo.applyer)!""}</font></td>
				</tr>
				<tr>
					<td style="text-align: right;">申请时间：</td><td>&nbsp;&nbsp;<font style="font-weight:bold">${(widthDrawVo.getStringApplyTime())!""}</font></td>
				</tr>
				<tr>
					<td style="text-align: right;">申请原因说明：</td><td>&nbsp;&nbsp;<font style="font-weight:bold">${(widthDrawVo.applyReason)!""}</font></td>
				</tr>
				<#if widthDrawVo.billStatus ?? && (widthDrawVo.billStatus == '2' || widthDrawVo.billStatus == '5')>
					<tr>
						<td style="text-align: right;">审核人：</td><td>&nbsp;&nbsp;<font style="font-weight:bold">${(widthDrawVo.operater)!""}</font></td>
					</tr>
					<tr>
						<td style="text-align: right;">审核时间：</td><td>&nbsp;&nbsp;<font style="font-weight:bold">${(widthDrawVo.getStringOperaterTime())!""}</font></td>
					</tr>
			    </#if>
			    <tr>
					<td style="text-align: right;">备注说明：</td><td>&nbsp;&nbsp;<textarea rows="2" cols="60" name="remark" id="remarkAudit" /></td>
				</tr>
			</table>
		</form>
	</div>
</div>
