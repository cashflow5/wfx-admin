<div id="tCatName" class="pb10 catName"><#if parentCat??&&parentCat.name??>${parentCat.name!''}<#else>销售分类管理</#if></div>
<div class="pb10 catNum">符合以下任一条件的产品共<span style="color:red"><#if comNum??>${comNum!'0'}<#else>0</#if></span>条</div>
<div id="clearCat">
<div id="bindBasicDiv" style="display:none">
<form id="bindForm" action="#" method="post">
	<input type="hidden" name="saleCatId" id="saleCatId" value="<#if parentCat.id??>${parentCat.id!''}</#if>">
	<input type="hidden" name="saleCatNo" id="saleCatNo" value="<#if parentCat.catNo??>${parentCat.catNo!''}</#if>">
	<input type="hidden" name="baseCatId" id="baseCatId">
	基础分类：
	<select class="input-sm" id="levelone" name="levelone" onchange="catChange('1',this);">
		<option value="">请选择</option>
		<#list basicList as item>
			<option value="${item.id}"><#if item.catName??>${item.catName!''}</#if></option>
		</#list>
	</select>
	<select class="input-sm" id="leveltwo" name="leveltwo" onchange="catChange('2',this);">
		<option value="">请选择</option>
	</select>
	<select class="input-sm" id="levelthree" name="levelthree" onchange="catChange('3',this);">
		<option value="">请选择</option>
	</select>
	<input id="bindCatBtn" type="button" value="绑定" class="btn btn-sm btn-yougou" onclick="toBind();"/>
	<div class="blank10"></div>
</form>
</div>
<div class="table-responsive">
	<table id="sample-table-1" class="table table-striped table-bordered table-hover">
        <thead>
        <tr>
            <th class="align-center">一级分类</th>
            <th class="align-center">二级分类</th>
            <th class="align-center">三级分类</th>
            <th class="align-center">最后更新人</th>
            <th class="align-center">最后更新时间</th>
            <th class="align-center">操作</th>
        </tr>
        </thead>
        <tbody>
        <#list relaList as item>
        <tr>
    		<td><#if item.levelOneName??>${item.levelOneName!''}</#if></td>
        	<td><#if item.levelTwoName??>${item.levelTwoName!''}</#if></td>
            <td><#if item.levelThreeName??>${item.levelThreeName!''}</#if></td>
            <td><#if item.updateUser??>${item.updateUser!''}</#if></td>
            <td class="align-center"><#if item.updateTime??>${item.updateTime?string('yyyy-MM-dd HH:mm:ss')}</#if></td>
            <td class="align-center"><a href="javascript:;" onclick="removeRela('${item.id}','${item.levelThreeName}');">删除</a></td>
        </tr>
   		</#list>
        </tbody>
    </table>
</div>
<div id="bindCatDiv"><input type="button" value="绑定基础分类" class="pull-right btn btn-sm btn-yougou" onclick="bindBtn();"/></div>
