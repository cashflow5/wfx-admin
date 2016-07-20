<#--
<@compress single_line=compress_single_line?contains("true")>
<#-- =========head======== -->
<#assign head>

</#assign>


<#assign footer>

</#assign>

<#assign body> -->
			
	<div class="tab-content no-border padding-24">
		<div id="home" class="tab-pane active">		
			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<div class="widget-box transparent">
						<div class="widget-header widget-header-small">
							<h4 class="widget-title smaller">
							<i class="ace-icon fa fa-check-square-o bigger-110"></i>
							分销商基本信息
							</h4>
						</div>
						<div class="profile-user-info">							
							<div class="profile-info-row">
								<div class="profile-info-name"> 分销商账号： </div>
								<div class="profile-info-value">
									<span>${(sellerDetailInfo.loginName)!''}</span>
								</div>
							</div>
							<div class="profile-info-row">
								<div class="profile-info-name"> 店铺名称：</div>
								<div class="profile-info-value">
									<span>${(sellerDetailInfo.shopName)!''}</span>
								</div>
							</div>
							<div class="profile-info-row">
								<div class="profile-info-name"> 性别： </div>
								<div class="profile-info-value">
									<span>
										<#if (sellerDetailInfo.memberSex)?? && (sellerDetailInfo.memberSex == "1") > 
											男 
										<#elseif (sellerDetailInfo.memberSex)?? && (sellerDetailInfo.memberSex == "2") > 
											女
										<#else>
											未知
										</#if>
									</span>
								</div>
							</div>
							<div class="profile-info-row">
								<div class="profile-info-name"> 姓名：</div>
								<div class="profile-info-value">
									<span>${(sellerDetailInfo.sellerName)!''}</span>
								</div>
							</div>
							<div class="profile-info-row">
								<div class="profile-info-name"> 分销商状态： </div>
								<div class="profile-info-value">
									<span>
										<#if (sellerDetailInfo.state)?? && (sellerDetailInfo.state == "3") > 
											合作中
										<#elseif (sellerDetailInfo.state)?? && (sellerDetailInfo.state == "4") > 
											已停止
										<#else>
											其他
										</#if>
									</span>
								</div>
							</div>
							<div class="profile-info-row">
								<div class="profile-info-name"> 生日： </div>
								<div class="profile-info-value">
									<span>${(sellerDetailInfo.birthday?string("yyyy-MM-dd"))!''}</span>
								</div>
							</div>
							<div class="profile-info-row">
								<div class="profile-info-name"> 注册时间： </div>
								<div class="profile-info-value">
									<span>${(sellerDetailInfo.registerDate?string("yyyy-MM-dd HH:mm:ss"))!''}</span>
								</div>
							</div>
							<div class="profile-info-row">
								<div class="profile-info-name"> 佣金收入总额：</div>
								<div class="profile-info-value">
									<span>${(sellerDetailInfo.commosion)!'0'}</span> 
								</div>
							</div>
							<div class="profile-info-row">
								<div class="profile-info-name"> 最后开启时间： </div>
								<div class="profile-info-value">
									<span>${(sellerDetailInfo.lastActiveDate?string("yyyy-MM-dd HH:mm:ss"))!''}</span>
								</div>
							</div>
							<div class="profile-info-row">
								<div class="profile-info-name"> 审核通过时间： </div>
								<div class="profile-info-value">
									<span>${(sellerDetailInfo.passDate?string("yyyy-MM-dd HH:mm:ss"))!''}</span>
								</div>
							</div>
							
							<div class="profile-info-row">
								<div class="profile-info-name"> 最后关闭时间</div>
								<div class="profile-info-value">
									<span>${(sellerDetailInfo.lastUnactiveDate?string("yyyy-MM-dd HH:mm:ss"))!''}</span>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>		
	
<#--	
</#assign>
<#-- =========引入模板======= -->
<#include "/include/pageBuilder.ftl" />
</@compress>	-->