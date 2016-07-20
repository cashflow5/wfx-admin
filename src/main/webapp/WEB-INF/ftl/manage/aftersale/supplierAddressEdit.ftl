<script type="text/javascript">
$(document).ready(function(){ 
	$("#supplierCode").val($("#supplierCode_").val());
	$("#font_supplierCode").text($("#supplierCode_").val());
	var id=$('#id').val();
	if(id!=''){
	  var provinceNo=$('#provinceNo').val()+"|"+$('#province').val();
	  var cityNo=$('#cityNo').val()+"|"+$('#city').val();
	  var areaNo=$('#areaNo').val()+"|"+$('#area').val();
	  getAreaOptionByPNo('1','');
	  getAreaOptionByPNo('2',provinceNo);
	  getAreaOptionByPNo('3',cityNo);
	  getAreaOptionByPNo('4',areaNo);
	}else{
		getAreaOptionByPNo('1','');
	}
}); 
</script>
<form class="form-horizontal" id="saveForm">
	<table class="com_modi_table">
		<tr>
			<th>供应商编码：</th>
			<td ><font id='font_supplierCode'></font></td>
		</tr>
		<#if supplierAddressVo.id??>
			<tr>
				<th>地址编码：</th>
				<td>${supplierAddressVo.outsideNo?default('')}</font></td>
			</tr>
		</#if>
		<tr>
		<th>所在地区：</th>
		<td>
			<select class="input-sm" id="levelone" name="provinceNo_" onchange="getAreaOptionByPNo('2',this.value);">
				<option value="">省</option>
			</select>
			<select class="input-sm" id="leveltwo" name="cityNo_" onchange="getAreaOptionByPNo('3',this.value);">
				<option value="">市</option>
			</select>
			<select class="input-sm" id="levelthree" name="areaNo_" onchange="getAreaOptionByPNo('4',this.value);">
				<option value="">区/县</option>
			</select>
		</td>
		</tr>				           
		<tr>
			<th style="height:15px;"></th>
			<td style="vertical-align:top;padding-top:5px;"><span id="provinceNo_cityNo_areaNo_stip" style="color:red"></span></td>
		</tr>
		
		<tr>
			<th></th>
			<td ><input type="text" id="address" name="address" style="width:450px;" value="${supplierAddressVo.address?default('')}"/></td>
		</tr>
		<tr>
			<th style="height:15px;"></th>
			<td style="vertical-align:top;padding-top:5px;"><span id="address_stip" style="color:red"></span></td>
		</tr>
		
		<tr>
			<th>联系人：</th>
			<td ><input type="text" id="contact" name="contact" style="width:220px;" value="${supplierAddressVo.contact?default('')}"/></td>
		</tr>
		<tr>
			<th style="height:15px;"></th>
			<td style="vertical-align:top;padding-top:5px;"><span id="contact_stip" style="color:red"></span></td>
		</tr>
		<tr>
			<th>联系电话：</th>
			<td ><input type="text" id="phone" name="phone" style="width:220px;" value="${supplierAddressVo.phone?default('')}"/></td>
		</tr>
		<tr>
			<th style="height:15px;"></th>
			<td style="vertical-align:top;padding-top:5px;"><span id="phone_stip" style="color:red"></span></td>
		</tr>
		<tr>
		<th>
		</th>
			<td style="padding-top:10px;">
				<input type="hidden" value="${supplierAddressVo.id?default('')}" id="id" name="id"/> 
				<input type="hidden" value="${supplierAddressVo.outsideNo?default('')}" id="outsideNo" name="outsideNo"/> 
				<input type="hidden" value="${supplierAddressVo.supplierCode?default('')}" id="supplierCode" name="supplierCode"/> 
				
				<input type="hidden" value="${supplierAddressVo.provinceNo?default('')}" id="provinceNo" name="provinceNo"/>
				<input type="hidden" value="${supplierAddressVo.province?default('')}" id="province" name="province"/> 
				<input type="hidden" value="${supplierAddressVo.cityNo?default('')}" id="cityNo" name="cityNo"/>
				<input type="hidden" value="${supplierAddressVo.city?default('')}" id="city" name="city"/> 
				<input type="hidden" value="${supplierAddressVo.areaNo?default('')}" id="areaNo" name="areaNo"/>
				<input type="hidden" value="${supplierAddressVo.area?default('')}" id="area" name="area"/> 
			</td>
		</tr>
	</table>
</form>	

