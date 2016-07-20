<@compress single_line=compress_single_line?contains("true")>
<#assign head>

</#assign>

<#assign footer>
	<!-- this page -->
	
</#assign>

<#assign body>
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <!-- Page Sidebar -->
    <!--#include file="/layouts/sidebar.shtml" -->
    <!-- /Page Sidebar -->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="page-content">
                <div class="row">        
                    <#if successFlag ?? && successFlag == "false">
	    				<div class="col-md-12 col-xs-12 ">
	    					<div class="mt40 pt10 ml50">
	                            <i class="fa fa-exclamation-circle blue f32 middle"></i>
	    						<#if message ??>
	                            	<span class="f16">${message}</span><br/>
                            	<#else>
                            		<span class="f16">很抱歉！出错啦，没有成功！</span>
                        		</#if>
	                        </div>
	    			<#else>
	    				<div class="col-md-12 col-xs-12 ">
	    					<div class="mt40 pt10 ml50">
	                            <i class="fa fa-exclamation-circle blue f32 middle"></i>
			    				<#if message ??>
		                            <span class="f16">${message}</span><br/>
		                        <#else>
		                        	<span class="f16">恭喜，操作已成功！</span>
								</#if>
	    					</div>
	    			</#if>
			    			<div class="mt10 ml50">
			    				<#if redirectionUrl ??>
		                        	<a href="javascript:void(0);" onclick="window.location.href='${redirectionUrl}'" class="btn btn-xs btn-yougou">确&nbsp;&nbsp;定</a><br>
		                        </#if>
		                    </div>
                    	</div>
                </div>
            </div>
            <!-- /.page-content -->
        </div>
    </div>
    <!-- /.main-content -->
    <!-- Footer Container -->
    <!--#include file="/layouts/footer.shtml" -->
    <!--  /Footer Container -->
   
</div>

</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>