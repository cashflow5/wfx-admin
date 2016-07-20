<#-- 
********** 用户中心公共宏 **************
-->

<#-- 会员身份转换 -->
<#macro shipName shipName>
	<#if shipName=="A">
		白名单
	<#elseif shipName=="B">
		新会员
	<#elseif shipName=="G">
		会员
	<#elseif shipName=="C">
		红名单
	<#elseif shipName=="D">
		灰名单
	<#elseif shipName=="E">
		黄名单
	<#elseif shipName=="F">
		黑名单
	<#else>
		未知(${shipName})
	</#if>
</#macro>

<#-- 来源平台转换 -->
<#macro platformName platform>
	<#if platform=="yougou">
		优购主站
	<#elseif platform=="mall_android">
		手机-android
	<#elseif platform=="mall_ios">
		手机-ios
	<#elseif platform=="wap_wap">
		手机-wap标准版
	<#elseif platform=="mobile_touch">
		手机-wap-H5
	<#else>
		未知平台(${platform})
	</#if>
</#macro>


<#-- 注册类型转换 -->
<#macro regTypeName regType>
	<#if regType=="yougou">
		直接注册-优购
	<#elseif regType=="taoxiu">
		直接注册-淘秀
	<#elseif regType=="quick-alipay">
		联合登录-支付宝
	<#elseif regType=="quick-renren">
		联合登录-人人网
	<#elseif regType=="quick-qq">
		联合登录-QQ
	<#elseif regType=="quick-sinaWeibo">
		联合登录-新浪微博
	<#elseif regType=="union-qqfanli">
		网盟-QQ彩贝
	<#elseif regType=="union-139fanli">
		网盟-139返利
	<#elseif regType=="union-51fanli">
		网盟-51返利
	<#elseif regType=="union-360cps">
		网盟-360cps
	<#else>
		未知类型(${regType})
	</#if>
</#macro>

<#-- 会员类型转换 -->
<#macro attentionName attention>
	<#if attention==1>
		普通会员
	<#elseif attention==2>
		重点会员
	<#elseif attention==3>
		关注会员
	<#elseif attention==4>
		外部会员
	<#else>
		未知会员类型(${attention})
	</#if>
</#macro>

<#-- 会员类型转换 -->
<#macro sexName sex>
	<#if sex==1>
		男
	<#elseif sex==2>
		女
	<#else>
		无
	</#if>
</#macro>



