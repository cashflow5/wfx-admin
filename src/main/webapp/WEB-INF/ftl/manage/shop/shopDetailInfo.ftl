<@compress single_line=compress_single_line?contains("true")>
<#-- =========head========-->
<#assign head>

</#assign>


<#assign footer>

</#assign>

<#assign body>
			
	<div class="tab-content no-border padding-24">
		<div id="home" class="tab-pane active">		
			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="widget-box transparent">
						<div class="widget-header widget-header-small">
							<h4 class="widget-title smaller">
							<i class="ace-icon fa fa-check-square-o bigger-110"></i>
							店铺基本信息
							</h4>
						</div>
						<div class="profile-user-info">	
						   <div class="profile-info-row">
								<div class="profile-info-name"> 店铺名称</div>
								<div class="profile-info-value">
									<span>${(vo.name)!''}</span>
								</div>
							</div>						
							<div class="profile-info-row">
								<div class="profile-info-name"> 分销商账号 </div>
								<div class="profile-info-value">
									<span>${(vo.loginName)!''}</span>
								</div>
							</div>
							<div class="profile-info-row">
								<div class="profile-info-name"> 联系人 </div>
								<div class="profile-info-value">
									<span>${(vo.contact)!''}</span>
								</div>
							</div>
							<div class="profile-info-row">
								<div class="profile-info-name"> 联系电话 </div>
								<div class="profile-info-value">
									<span>${(vo.mobile)!''}</span>
								</div>
							</div>
							
							
							<div class="profile-info-row">
								<div class="profile-info-name"> 创建时间： </div>
								<div class="profile-info-value">
									<span>${(vo.createTime?string("yyyy-MM-dd HH:mm:ss"))!''}</span>
								</div>
							</div>
							<div class="profile-info-row">
								<div class="profile-info-name"> 最近修改时间</div>
								<div class="profile-info-value">
									<span>${(vo.updateTime?string("yyyy-MM-dd HH:mm:ss"))!''}</span>
								</div>
							</div>
							
							<div class="profile-info-row">
								<div class="profile-info-name"> 店铺LOGO </div>
								<div class="profile-info-value">
									<img src="${(vo.logoUrl)!''}"/>
								</div>
							</div>
							<div class="profile-info-row">
								<div class="profile-info-name"> 店招 </div>
								<div class="profile-info-value">
									<img src="${(vo.signUrl)!'#'}"/>
								</div>
							</div>
							<div class="profile-info-row">
								<div class="profile-info-name"> 店铺简介 </div>
								<div class="profile-info-value">
									<span>${(vo.notice)!''}</span>
								</div>
							</div>
						</div>
						
						<div class="left">
							<a href="javascript:history.back()" class="btn btn-sm btn-yougou">
								<i class="ace-icon fa fa-arrow-left"></i>
								返回
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>		
</#assign>
<#-- =========引入模板======= -->
<#include "/include/pageBuilder.ftl" />
</@compress>	