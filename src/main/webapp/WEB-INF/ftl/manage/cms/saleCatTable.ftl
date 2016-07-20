<div id="tCatName" class="pb10"><#if parentCat??&&parentCat.name??>${parentCat.name!''}<#else>销售分类管理</#if></div>
<div class="table-responsive picTable">
	<input type="hidden" id="parentId" name="parentId" value="<#if parentCat??&&parentCat.id??>${parentCat.id!''}<#else>0</#if>"/>
	<input type="hidden" id="saleCatId" name="saleCatId" />
	<table id="sample-table-1" class="table table-striped table-bordered table-hover">
        <thead>
        <tr>
            <th class="align-center">子分类</th>
            <th class="align-center">图片</th>
            <th class="align-center">产品数</th>
            <th class="align-center">最后更新人</th>
            <th class="align-center">最后更新时间</th>
            <th class="align-center">操作</th>
        </tr>
        </thead>
        <tbody>
        <#list catList as item>
        <tr>
    		<td><#if item.name??>${item.name!''}</#if></td>
        	<td class="align-center">
        	<#if item.picUrl?? && item.picUrl != ''>
            <a href="javascript:void(0);" class="red upload-img" saleCatId="${item.id}">
            	<div class="imgDiv">
            	<img src="${imgBaseUrl!''}<#if item.picUrl??>${item.picUrl!''}</#if>" class="catPic"/>
            	<span name="imgLayer" class="imgLayer">修改</span>
            	</div>
            </a>
        	<#else>
            <a href="javascript:void(0);" class="red upload-img" saleCatId="${item.id}">
               	<div class="catPic">请上传图片</div>
            </a>
        	</#if>
            </td>
            <td class="align-center"><#if item.productNum??>${item.productNum!''}</#if></td>
            <td><#if item.updateUser??>${item.updateUser!''}</#if></td>
            <td class="align-center"><#if item.updateTime??>${item.updateTime?string('yyyy-MM-dd HH:mm:ss')}</#if></td>
            <td class="align-center"><a class="saleCatA" href="javascript:;" onclick="removeSaleCat('${item.id}','${item.parentId}','${item.name}');">删除</a></td>
        </tr>
    	</#list>
        </tbody>
    </table>
</div>