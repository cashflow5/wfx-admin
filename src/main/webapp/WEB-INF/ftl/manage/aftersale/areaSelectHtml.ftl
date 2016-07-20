<option value=""><#if level =='1'>省</#if><#if level =='2'>市</#if><#if level =='3'>区/县</#if></option>
<#list areaList as item>
		<option value="${item.no}|${item.name}"><#if item.name??>${item.name!''}</#if></option>
</#list>