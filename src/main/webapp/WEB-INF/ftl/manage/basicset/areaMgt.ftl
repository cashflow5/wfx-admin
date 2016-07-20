<@compress single_line=compress_single_line?contains("true")>
<#assign head>
	<!-- zTree -->
	<link rel="stylesheet" href="/static/plugin/zTree/css/bbyStyle/bbyStyle.css">
	<link rel="stylesheet" type="text/css" href="/static/plugin/webuploader/webuploader.css">
	<link rel="stylesheet" href="/static/plugin/fileUpload/ajaxfileupload.css" />
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/plugin/zTree/js/jquery.ztree.all-3.5.min.js"></script>
	<script src="/static/plugin/layer/layer.js"></script>
	<script src="/static/plugin/layer/extend/layer.ext.js"></script>
	<script src="/static/js/manage/basicset/areaMgt.js"></script>
	<script src="/static/js/manage/basicset/areaMgtZTree.js"></script>
</#assign>

<#assign body>
	<!-- Page Body -->
	<div class="blank10"></div>
	<div class="g-layout-column-left250">
	<div class="g-layout-left">
	    <ul id="SettingNodeList" class="ztree" style="height:350px;">
			<!-- tree start -->
			
			<!-- tree end -->
	    </ul>
	</div>
	<!-- 列表区域 -->
	<div class="g-layout-main">
	    <div id="goodsCategoryList" class="g-layout-container">
	              
            <div class="tab-content no-border padding-24">
				<div id="home" class="tab-pane active">
					<div class="row">
						<div class="col-xs-12 col-sm-6">
							<div class="widget-box transparent">
								<div class="profile-user-info">
									<div class="profile-info-row">
										<div class="profile-info-name"><span style="color:red">*</span>区域编码：</div>
										<div class="profile-info-value">
											<input type="hidden" name="id" value="" id="id" />
											<span><input class="form-control input-sm" name="no" id="no" value="" type="text" readonly="readOnly"></span>
										</div>
									</div>
									<div class="profile-info-row">
										<div class="profile-info-name"><span style="color:red">*</span>区域名称：</div>
										<div class="profile-info-value">
											<span><input class="form-control input-sm" name="name" id="name" value="" type="text"></span>
										</div>
									</div>
									<div class="profile-info-row">
										<div class="profile-info-name"><span style="color:red">*</span>排序号：</div>
										<div class="profile-info-value">
											<span><input class="form-control input-sm" name="sort" id="sort" value="" type="text"></span>
										</div>
									</div>
									<div class="profile-info-row">
										<div class="profile-info-name">邮编：</div>
										<div class="profile-info-value">
											<span><input class="form-control input-sm" name="post" id="post" value="" type="text"></span>
										</div>
									</div>
									<div class="profile-info-row">
										<div class="profile-info-name">区号：</div>
										<div class="profile-info-value">
											<span><input class="form-control input-sm" name="code" id="code" value="" type="text"></span>
										</div>
									</div>
									<div class="profile-info-row">
										<div class="profile-info-name">是否显示：</div>
										<div>
											<span><input type="radio" name="isUsable" id="isUsable1"  value="1" > 显示   </input>
											<input type="radio" name="isUsable" id="isUsable2" value="0" > 不显示 </input></span> 
										</div>
									</div>
									<div class="profile-info-row">
										<div class="profile-info-name"></div>
										<div class="profile-info-value">
											<span><input type="button" value="保存" class="btn btn-sm btn-yougou" onclick="doSaveArea();"/></span>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
	    </div>
	</div>
	
	
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>