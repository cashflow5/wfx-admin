<#-- 一级菜单图标 -->
<#macro menuIcon menuName>
	<#if menuName=="会员信息管理" || menuName=="会员信息">
		fa-users
	<#elseif menuName=="会员监控">
		fa-eye
	<#elseif menuName=="交易监控">
		fa-rmb
	<#elseif menuName=="商品点评管理" || menuName="评论晒单">
		fa-comments
	<#elseif menuName=="下单手机号码">
		fa-mobile
	<#elseif menuName=="短信管理" || menuName=="短信邮件">
		fa-envelope
	<#elseif menuName=="积分换卷活动" || menuName=="会员活动">
	　　fa-gift	
	<#elseif menuName=="下单数据分析" || menuName="用户问题反馈">
	　　fa-line-chart	
	<#elseif menuName=="地区管理">
	　　fa-globe
	<#else>
		fa-navicon
	</#if>
</#macro>
<#if hideMenuAndHeader?? && hideMenuAndHeader>
<#else>
<!-- #section:basics/sidebar -->
<div id="sidebar" class="sidebar responsive sidebar-fixed sidebar-scroll<#if hideMenuAndHeader?? && hideMenuAndHeader> hide</#if>">
	<script type="text/javascript">
		try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
	</script>

	<ul class="nav nav-list">
		<li>
			<a href="/index.sc" data-value="menu">
				<i class="menu-icon fa fa-tachometer"></i>
				<span class="menu-text"> 主面板 </span>
			</a>
			<b class="arrow"></b>
		</li>
		
		<!--主菜单循环展示开始-->
		<#if login_user_menu?? && (login_user_menu?size>0)>
			<#list login_user_menu as item>
				<li data-value="parentMenu" class="">
					<a <#if item.menuUrl?? &&item.menuUrl?length gt 0>href="${item.menuUrl!!}" data-value="menu"<#else>href="javascript:void(0);" class="dropdown-toggle"</#if> >
						<i class="menu-icon fa <@menuIcon menuName=item.menuName/>"></i>
						<span class="menu-text">${item.menuName!!} </span>
						<#if item.kids?? && (item.kids?size>0)>
						<b class="arrow fa fa-angle-down"></b>
						</#if>
					</a>
					<#if item.kids?? && (item.kids?size>0)>
					<b class="arrow"></b>
					<ul class="submenu">
						<#list item.kids as item>
							<li class="">
								<a href="${item.menuUrl!!}" data-value="menu">
								<i class="menu-icon fa fa-caret-right"></i>
								${item.menuName!!}
								</a>
							</li>
						</#list>
					</ul>
					</#if>
				</li>
			</#list>
		</#if>
		<!--主菜单循环展示结束-->						
	</ul><!-- /.nav-list -->

	<!-- #section:basics/sidebar.layout.minimize -->
	<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
		<i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
	</div>
	<!-- /section:basics/sidebar.layout.minimize -->
	<script type="text/javascript">
		try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
	</script>
</div>
<!-- /section:basics/sidebar -->
</#if>