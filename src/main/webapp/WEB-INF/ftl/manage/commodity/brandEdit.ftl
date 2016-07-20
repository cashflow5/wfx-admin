<link rel="stylesheet" type="text/css" href="/static/plugin/webuploader/webuploader.css"/>
<link rel="stylesheet" href="/static/plugin/zTree/css/bbyStyle/bbyStyle.css">
<div class="page-content" id="page-content">
    <div class="row">
        <div class="col-md-8 col-xs-8">
            <div class="bd clearfix">
            	<input type="hidden" id="brandId" value='${(commodityBrand.id)!""}'/>
            	<input type="hidden" id="brandNoStr" value='${(commodityBrand.brandNo)!""}'/>
                <dl class="c-h-dl dl-basic-field">
                    <dt>品牌名称：</dt>
                    <dd><input type="text" id="brandNameStr" class="form-control input-small" value="${(commodityBrand.brandName)!""}"/></dd>
                </dl>
                <dl class="c-h-dl dl-basic-field">
                    <dt>品牌英文名称：</dt>
                    <dd><input type="text" id="englishNameStr" class="form-control input-small" value="${(commodityBrand.englishName)!""}"/></dd>
                </dl>
                <dl class="c-h-dl dl-basic-field">
                    <dt>状态：</dt>
                    <dd>
                        <label><input type="radio" name="statusStr" class="middle" value="1" <#if (commodityBrand.status)?? && commodityBrand.status==1>checked</#if>/> 启用</label>
                        <label class="ml20"><input type="radio" name="statusStr" value="2" class="middle" <#if (commodityBrand.status)?? && commodityBrand.status!=1>checked</#if>/> 停用</label>
                    </dd>
                </dl>
                <div class="blank10"></div>
                <ul class="ul-img-list">
                    <li>
                        <div id="fileList1" class="img-box mb10">
                        	<#if (commodityBrand.logoNameUrl)?? >
                        	<img src="${commodityBrand.logoNameUrl}" width="118" height="118"/>
                        	<p class="img-size" style="display:none">74*64</p>
                        	<#else>
                            <div class="mt40">大图</div>
                            <p class="img-size">74*64</p>
                            </#if>
                        </div>
                        <a id="fileUploadBtn1" class="file-btn" href="javascript:void(0);">添加图片</a>
                    </li>
                    <li>
                       <div id="fileList2" class="img-box mb10">
                       		<#if (commodityBrand.logoMiddleUrl)?? >
                        	<img src="${commodityBrand.logoMiddleUrl}" width="118" height="118"/>
                        	<p class="img-size" style="display:none">85*40</p>
                        	<#else>
                            <div class="mt40">中图</div>
                            <p class="img-size">85*40</p>
                            </#if>
                        </div>
                        <a id="fileUploadBtn2" class="file-btn" href="javascript:void(0);">添加图片</a>
                    </li>
                    <li>
                       <div id="fileList3" class="img-box mb10">
                       		<#if (commodityBrand.logoSmallUrl)?? >
                        	<img src="${commodityBrand.logoSmallUrl}" width="118" height="118"/>
                        	<p class="img-size" style="display:none">110*50</p>
                        	<#else>
                            <div class="mt40">小图</div>
                            <p class="img-size">110*50</p>
                            </#if>
                        </div>
                        <a id="fileUploadBtn3" class="file-btn" href="javascript:void(0);">添加图片</a>
                    </li>
                    <li>
                       <div id="fileList4" class="img-box mb10">
                       		<#if (commodityBrand.mobilePic)?? >
                        	<img src="${commodityBrand.mobilePic}" width="118" height="118"/>
                        	<p class="img-size" style="display:none">140*120</p>
                        	<#else>
                            <div class="mt40">手机图</div>
                            <p class="img-size">140*120</p>
                            </#if>
                        </div>
                        <a id="fileUploadBtn4" class="file-btn" href="javascript:void(0);">添加图片</a>
                    </li>
                    <li>
                       <div id="fileList5" class="img-box mb10">
                       		<#if (commodityBrand.logoLeastUrl)?? >
                        	<img src="${commodityBrand.logoLeastUrl}" width="118" height="118"/>
                        	<p class="img-size" style="display:none">74*64</p>
                        	<#else>
                            <div class="mt40">微图</div>
                            <p class="img-size">74*64</p>
                            </#if>
                        </div>
                        <a id="fileUploadBtn5" class="file-btn" href="javascript:void(0);">添加图片</a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="col-md-4 col-xs-4">
            <div class="bd">                
                <ul id="elementList" class="ztree" style="height:350px;"></ul>
            </div>
        </div>
    </div>
</div>
<!-- this page-->
<script type="text/javascript" src="/static/plugin/webuploader/webuploader.js"></script>
<script type="text/javascript" src="/static/js/manage/commodity/brandUpload.js"></script>
<script type="text/javascript" src="/static/js/manage/commodity/brandEdit.js"></script>