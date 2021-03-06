<#if (param_isPjax?? && param_isPjax == "true") || (ajax_error?? && ajax_error)>
<#if head??>
${head}
</#if>
<#if body??>
${body}
</#if>
<#if footer??>
${footer}
</#if>
	<#else>
<!DOCTYPE html>
<html lang="en">
<!-- Head -->
<#include "/include/head.ftl" >
<!-- /Head -->
<body class="no-skin">
	<script>
		YouGou.UI.progressLoading();
	</script>
	<!-- Loading Container -->
    <div class="loading-container hide">
        <div class="loading-progress">
            <div class="rotator">
                <div class="rotator">
                    <div class="rotator colored">
                        <div class="rotator">
                            <div class="rotator colored">
                                <div class="rotator colored"></div>
                                <div class="rotator"></div>
                            </div>
                            <div class="rotator colored"></div>
                        </div>
                        <div class="rotator"></div>
                    </div>
                    <div class="rotator"></div>
                </div>
                <div class="rotator"></div>
            </div>
            <div class="rotator"></div>
        </div>
    </div>
    <!-- /Loading Container -->
    
 	<!-- Navbar -->
	<#include "/include/navbar.ftl" >
	<!-- /Navbar -->
	
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<script type="text/javascript">
			try{ace.settings.check('main-container' , 'fixed')}catch(e){}
		</script>

		<!-- Page Sidebar -->
		<#if nosidebar??>
		<#else>
        <#include "/include/sidebar.ftl" >
        </#if>
        <!-- /Page Sidebar -->
			
		<div class="main-content">
			<div class="main-content-inner">
			
				<!-- Page Breadcrumb -->
                <#include "/include/breadcrumb.ftl" >
                <!-- /Page Breadcrumb -->
				
				<div class="page-content" id="page-content">
					
					<!-- Page Breadcrumb -->
                	<#--<#include "/include/settingbox.ftl" >-->
                	<!-- /Page Breadcrumb -->
					
					 <!-- Page Header -->
	                <#include "/include/header.ftl" >
	                <!-- /Page Header -->
					
					<!-- Page Body -->
	                <#if body??>
	                	${body}
	                </#if>
	                <!-- /Page Body -->
	                
				</div><!-- /.page-content -->
			</div>
		</div><!-- /.main-content -->

		<!-- Footer Container -->
	    <#include "/include/footer.ftl" >
	    <!--  /Footer Container -->

		<a href="javascript:void(0);" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-yougou<#--btn-inverse-->">回到顶部
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>
	</div><!-- /.main-container -->
</body>
</html>
</#if>