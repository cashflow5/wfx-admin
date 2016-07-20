<@compress single_line=compress_single_line?contains("true")>

<#-- =========title========-->
<#assign title>500错误，服务器内部异常</#assign>

<#-- =========head========-->
<#assign head>
</#assign>

<#-- =========footer===== -->
<#assign footer>
</#assign>

<#-- =========body======= -->
<#assign body>
<div class="row">
	<div class="col-xs-12">
		<div class="error-container">
			<div class="well">
				<h1 class="grey lighter smaller">
					<span class="bigger-125" style="color: #3DA8B9 !important;">
						<i class="ace-icon fa fa-random"></i>
						500
					</span>
					您的请求发生服务器异常！
				</h1>
				<hr style="margin-top: 10px;margin-bottom: 10px;border: 0;border-top: 1px solid #eee;">
				<h3 class="lighter smaller">
					<i class="ace-icon fa fa-wrench icon-animated-wrench bigger-125"></i>
					您可以将此问题反馈给开发人员，我们将会及时处理，谢谢！
				</h3>
				<div style="overflow-y:auto;overflow-x:auto;height:400px;">
						<pre class="alert alert-danger"><b>异常描述：</b>${errMsg}</br><b>请求地址</b>：${method} ${curUrl!""}</br><b>参数</b>：${params!""}</br><b>请求头</b>：${headers!""}${errorMessageException!""}</pre>
				</div>
				<hr style="margin-top: 5px;margin-bottom: 5px;border: 0;border-top: 1px solid #eee;">
				<div class="center">
					<a href="javascript:history.back()" class="btn btn-grey">
						<i class="ace-icon fa fa-arrow-left"></i>
						返回上一页
					</a>&nbsp;
					<a href="/" class="btn btn-primary">
						<i class="ace-icon fa fa-tachometer"></i>
						首页
					</a>
					<#if (param_isPjax?? && param_isPjax == "true") || (ajax_error?? && ajax_error)>
						&nbsp;<a href="javascript:window.top.mmgridLoadErrorDialog.close();" class="btn btn-grey">关闭窗口</a>
					</#if>
				</div>
			</div>
		</div>
	</div>
</div>
</#assign>

<#-- =========引入模板======= -->
<#include "/include/pageBuilder.ftl" />
</@compress>