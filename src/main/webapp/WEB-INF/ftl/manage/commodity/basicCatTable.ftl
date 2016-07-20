<div class="pb10">${basicCat.catName}</div>
<div class="table-responsive">
    <table id="sample-table-1" class="table table-striped table-bordered table-hover">
        <thead>
        <tr>
            <th class="align-center">类别名称</th>
            <th class="align-center">分类等级</th>
            <th class="align-center">状态</th>
            <th class="align-center">类别编码</th>
            <th class="align-center">SKU数量</th>
            <th class="align-center">最后更新人</th>
            <th class="align-center">最后更新时间</th>
            <th class="align-center">操作</th>
        </tr>
        </thead>
        <tbody>
        <#list catList as item>
        <tr>
            <td><#if item.catName??>${item.catName!''}</#if></td>
            <td class="align-center">
            <#if item.level??>
            <#if item.level == 1>一级<#elseif item.level == 2>二级<#elseif item.level == 3>三级</#if>
            </#if>
            </td>
            <td style="text-align:center"><#if item.status??><#if item.status == 0>停用<#else>启用</#if><#else>启用</#if></td>
            <td><#if item.no??>${item.no!''}</#if></td>
            <td class="align-center"><#if item.no??>${item.skuNum!''}</#if></td>
            <td><#if item.updatePerson??>${item.updatePerson!''}</#if></td>
            <td class="align-center"><#if item.updateDate??>${item.updateDate?string('yyyy-MM-dd HH:mm:ss')}</#if></td>
            <td class="align-center"">
	            <#if item.status??>
		            <#if item.status == 0>
		            	<a href="javascript:;" onclick="updateStatus('${item.id!''}',1,'${item.parentId!''}')">启用</a></td>
		            <#else>
		            <a href="javascript:;" onclick="updateStatus('${item.id!''}',0,'${item.parentId!''}')">停用</a></td>
		            </#if>
	            <#else>
	            <a href="javascript:;" onclick="updateStatus('${item.id!''}',0,'${item.parentId!''}')">停用</a>
	            </td>
	            </#if>
        </tr>
        </#list>
        </tbody>
    </table>
</div>