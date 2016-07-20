<table class="com_modi_table">
	<tr>
		<th align="left">
			分销商账号：
		</th>
		<td >
			${commissionDetailVo.loginName?default('')}
		</td>
	</tr>
	<tr>
		<th style="height:15px;"></th>
		<td style="vertical-align:top;padding-top:5px;">
		<span id="commissionDetailVo_stip" style="color:red"></span>
		</td>
	</tr>
	<tr>
		<th align="left">账户余额：</th>
		<td >
			${commissionDetailVo.accountBalance?default('')}
			<input type="hidden" value="${commissionDetailVo.accountBalance?default(0)?c}" id="accountBalance" name="accountBalance"/> 
		</td>
	</tr>
	<tr>
		<th style="height:15px;"></th>
		<td style="vertical-align:top;padding-top:5px;">
		<span id="accountBalance_stip" style="color:red"></span>
		</td>
	</tr>
	<tr>
		<th align="left">订单号：</th>
		<td >
			${commissionDetailVo.wfxOrderDetailNo?default('')}
		</td>
	</tr>
	<tr>
		<th style="height:15px;"></th>
		<td style="vertical-align:top;padding-top:5px;">
		<span id="commissionDetailVo_stip" style="color:red"></span>
		</td>
	</tr>
	<tr>
		<th align="left">佣金收益：</th>
		<td >
			${commissionDetailVo.commissionAmount?default('')}
			<input type="hidden" value="${commissionDetailVo.commissionAmount?default(0)?c}" id="commissionAmount" name="commissionAmount"/> 
		</td>
	</tr>
	<tr>
		<th style="height:15px;"></th>
		<td style="vertical-align:top;padding-top:5px;">
		<span id="commissionAmount_stip" style="color:red"></span>
		</td>
	</tr>
	<tr>
		<th align="left">处理方式：</th>
		<td >
			<input type="radio" value="1" name="dealType" 
				<#if commissionDetailVo.dealType??&&commissionDetailVo.dealType =='1'>checked</#if>
				 onclick="validate();">重新结算&nbsp;&nbsp;&nbsp;
			<input type="radio" value="2" name="dealType" 
				<#if commissionDetailVo.dealType??&&commissionDetailVo.dealType =='2'>checked</#if>
				onclick="validate();">异常关闭
		</td>
	</tr>
	<tr>
		<th style="height:15px;"></th>
		<td style="vertical-align:top;padding-top:5px;">
		<span id="dealType_stip" style="color:red"></span>
		</td>
	</tr>
	<tr>
		<th align="left">备注：</th>
		<td >
			<input maxlength="50" id="remark" name="remark" style="width:300px;" onblur="validate();" value="${commissionDetailVo.remark?default('')}"/>
		</td>
	</tr>
	<tr>
		<th style="height:15px;"></th>
		<td style="vertical-align:top;padding-top:5px;">
		<span id="remark_stip" style="color:red"></span>
		</td>
	</tr>
	<tr>
		<th>
		</th>
			<td style="padding-top:10px;">
				<input type="hidden" value="${commissionDetailVo.id?default('')}" id="id" name="id"/> 
			</td>
		</tr>
</table>
<script type="text/javascript">
function validate(){
	var dealType = $('input[name="dealType"]:checked').val();
	if(YouGou.Util.isEmpty(dealType)){
		$("#dealType_stip").html("处理方式必须选择！");
	}else{
		$("#dealType_stip").html("");
	}
	var remark = $("#remark").val();
	if(YouGou.Util.isEmpty(remark)){
		$("#remark_stip").html("备注不能为空！");
	}else{
		$("#remark_stip").html("");
	}
}
</script>
