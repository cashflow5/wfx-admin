<@compress single_line=compress_single_line?contains("true")>
<#-- =========head========-->
<#assign head>

</#assign>
<#-- =========footer========-->
<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/seller/authorizeInfo.js"></script>
</#assign>
<#assign body>
	<input type="hidden" id="sellerAuthorizeId" value="${(sellerAuthorizeInfo.id)!''}"/>
	<input type="hidden" id="loginAccountId" value="${(sellerInfo.loginaccountId)!''}"/>	
	<div class="tab-content no-border padding-24">
		<div id="home" class="tab-pane active">		
			<div class="row">
				<div class="col-xs-12 col-sm-12">
					<div class="widget-box transparent">
						<div class="widget-header widget-header-small">
							<h4 class="widget-title smaller">
							<i class="ace-icon fa fa-check-square-o bigger-110"></i>
							基本信息
							</h4>
						</div>
						<div>							
							<div class="col-xs-6 col-sm-6">
								<div class="profile-info-name"> 分销商账号： </div>
								<div class="profile-info-value">
									<span>${(sellerInfo.loginName)!''}</span>
								</div>
							</div>	
							<div class="col-xs-6 col-sm-6">
								<div class="profile-info-name"> 店铺名称：</div>
								<div class="profile-info-value">
									<span>${(sellerInfo.shopName)!''}</span>
								</div>
							</div>
						
							<div class="col-xs-6 col-sm-6">
								<div class="profile-info-name"> 分销商状态：</div>
								<div class="profile-info-value">
									<span>
										<#if (sellerInfo.state)?? && (sellerInfo.state == "3") > 
											合作中
										<#elseif (sellerInfo.state)?? && (sellerInfo.state == "4") > 
											已停止
										<#else>
											其他
										</#if>
									</span>
								</div>
							</div>
							<div class="col-xs-6 col-sm-6">	
								<div class="profile-info-name"> 注册时间：</div>
								<div class="profile-info-value">
									<span>${(sellerInfo.registerDate?string("yyyy-MM-dd HH:mm:ss"))!''}</span>
								</div>
							</div>
						</div>
					</div>
					
					<div class="widget-box transparent">
						<div class="widget-header widget-header-small">
							<h4 class="widget-title smaller">
							<i class="ace-icon fa fa-check-square-o bigger-110"></i>
							银行卡信息
							</h4>
						</div>
						<div>							
							<div class="col-xs-6 col-sm-6">
								<div class="profile-info-name"> 持卡人： </div>
								<div class="profile-info-value">
									<span>${(sellerBankInfo.trueName)!''}</span>
								</div>
							</div>
							<div class="col-xs-6 col-sm-6">	
								<div class="profile-info-name"> 银行卡号：</div>
								<div class="profile-info-value">
									<span>${(sellerBankInfo.bankAccount)!''}</span>
								</div>
							</div>
						
							<div class="col-xs-6 col-sm-6">
								<div class="profile-info-name"> 开户银行：</div>
								<div class="profile-info-value">
									<span>${(sellerBankInfo.bankName)!''}</span>
								</div>
							</div>
							<div class="col-xs-6 col-sm-6">
								<div class="profile-info-name"> 支行：</div>
								<div class="profile-info-value">
									<span>${(sellerBankInfo.bankSubName)!''}</span>
								</div>
							</div>
						</div>
					</div>
					
					<div class="widget-box transparent">
						<div class="widget-header widget-header-small">
							<h4 class="widget-title smaller">
							<i class="ace-icon fa fa-check-square-o bigger-110"></i>
							身份审核信息
							</h4>
						</div>
						<div>		
							<div class="col-xs-6 col-sm-6">
								<div class="profile-info-name"> 真实姓名：</div>
								<div class="profile-info-value">
									<span>${(sellerBankInfo.trueName)!''}</span>
								</div>
							</div>
							<div class="col-xs-6 col-sm-6">
								<div class="profile-info-name"> 身份证号码：</div>
								<div class="profile-info-value">
									<span>${(sellerBankInfo.idCardNo)!''}</span>
								</div>
							</div>					
							<div class="profile-info-row">
								<div class="profile-info-name" style="width:120px;"> 身份证扫描件： </div>
								<div class="profile-info-value">
									正面：<#if (sellerAuthorizeInfo.idCardPic)??&& imgBaseUrl??>
									<a href="${imgBaseUrl!''}${(sellerAuthorizeInfo.idCardPic)!''}" target="blank">${(sellerAuthorizeInfo.idCardPic)!''}</a> &nbsp;
									<#else>
									<span>无</span>
									</#if>
									<br/>
									反面：<#if (sellerAuthorizeInfo.idCardPicBack)??&& imgBaseUrl??>
									<a href="${imgBaseUrl!''}${(sellerAuthorizeInfo.idCardPicBack)!''}" target="blank">${(sellerAuthorizeInfo.idCardPicBack)!''}</a>
									<#else>
									<span>无</span>
									</#if>
								</div>
							</div>
							<!--
							<div class="profile-info-row">
								<div class="profile-info-name" style="width:150px;"> 税率优惠代办委托书：</div>
								<div class="profile-info-value">
									<#if (sellerAuthorizeInfo.authorizePic)?? && imgBaseUrl??>
									<a href="${imgBaseUrl!''}${(sellerAuthorizeInfo.authorizePic)!''}" target="blank">${(sellerAuthorizeInfo.authorizePic)!''}</a>
									<#else>
									<span>无</span>
									</#if>
								</div>
							</div>-->
							<div class="profile-info-row">
								<div class="profile-info-name"> 审核状态：</div>
								<div class="profile-info-value">
									<span id="authorizeStatus">
										<#if (sellerAuthorizeInfo.status)?? && (sellerAuthorizeInfo.status == 1) > 
										待审核
										
										<#elseif (sellerAuthorizeInfo.status)?? && (sellerAuthorizeInfo.status == 2) > 
										审核通过
										<#elseif (sellerAuthorizeInfo.status)?? && (sellerAuthorizeInfo.status == 3) > 
										审核不通过
										<#else>
										其他
										</#if>
									</span>
									<a class="btn btn-sm btn-yougou pass" authorId="${(sellerAuthorizeInfo.id)!''}" href="javascript:void(0);">审核通过</a>
									<a class="btn btn-sm btn-yougou unpass" authorId="${(sellerAuthorizeInfo.id)!''}" href="javascript:void(0);">审核拒绝</a>
								</div>
							</div>
						</div>
					</div>
					<div class="widget-box transparent">
						<div class="widget-header widget-header-small">
							<h4 class="widget-title smaller">
							<i class="ace-icon fa fa-check-square-o bigger-110"></i>
							银行卡信息修改日志
							</h4>
						</div>
						<div>
							<table id="grid-table" class="mmg"></table>
						    <div id="grid-pager" style="text-align:right;" class=""></div>
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