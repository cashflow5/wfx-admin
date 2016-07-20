<option value="">请选择</option>
<#list catList as item>
<option value="<#if item.id??>${item.id}</#if>"><#if item.catName??>${item.catName}</#if></option>
</#list>