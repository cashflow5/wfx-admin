<@compress single_line=compress_single_line?contains("true")>
<#assign head>
	<link rel="stylesheet" type="text/css" href="/static/css/mui.css"/>
	<style>
		.yg-cms-detail .cms-content .cms-shop-box .cms-item:first-child > img {
		    border-radius: 40px !important;
		}
	</style>
</#assign>
<#assign footer>
	<script src="/static/js/manage/discover/articleView.js"></script>
</#assign>

<#assign body>
<div class="row">
	<div class="col-xs-12">
		<div id="articleNavbar" class="message-navbar clearfix">
			<div class="message-bar">
				<div class="message-toolbar">
					<button class="btn btn-sm btn-yougou" onclick="backToList();">
			        	<i class="ace-icon fa fa-pencil align-top bigger-125"></i>
			        	返回
			        </button>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="tab-content no-border padding-24">
	<div id="home" class="tab-pane active">		
		<div class="row">
			<div class="col-xs-12 col-sm-12">
				<div class="widget-box transparent">
					<div class="profile-user-info">							
						<div class="profile-info-row">
							<div class="profile-info-name">标题： </div>
							<div class="profile-info-value">
								<span>${(article.title)!''}</span>
							</div>
						</div>
					
						<div class="profile-info-row">
							<div class="profile-info-name">作者： </div>
							<div class="profile-info-value">
								<#if article.authorType?? && article.authorType == 1>
								<span>优购微零售</span>
								<#else>
								<span>${(article.authorAccount)!''}</span>
								</#if>
							</div>
						</div>
						
						<div class="profile-info-row">
							<div class="profile-info-name">所属频道： </div>
							<div class="profile-info-value">
								<span>${(article.channelName)!''}</span>
							</div>
						</div>
						
						<div class="profile-info-row">
							<div class="profile-info-name">是否推荐： </div>
							<div class="profile-info-value">
								<#if article.recommendFlag?? && article.recommendFlag == 2>
									<span>是</span>
								<#else>
									<span>否</span>
								</#if>
							</div>
						</div>
						
						<div class="profile-info-row">
							<div class="profile-info-name">是否发布： </div>
							<div class="profile-info-value">
								<#if article.publishStatus?? && article.publishStatus == 2>
									<span>是</span>
								<#else>
									<span>否</span>
								</#if>
							</div>
						</div>
						
						<div class="profile-info-row">
							<div class="profile-info-name">文章封面： </div>
							<div class="profile-info-value">
								<img src="${picUrl!''}/${article.picCover!''}" alt="封面" />
							</div>
						</div>
						
						<div class="profile-info-row">
							<div class="profile-info-name">文章内容： </div>
							<div class="profile-info-value yg-cms-detail">
								<div class="cms-content" style="width:640px">${(article.content)!''}</div>
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