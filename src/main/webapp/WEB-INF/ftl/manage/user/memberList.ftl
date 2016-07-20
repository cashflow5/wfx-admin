<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/user/memberList.js"></script>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<input type="hidden" name="memberMgt" id="memberMgt" value="${memberMgt!""}"/>
	<div class="col-xs-12">
		
        <div class="space-6"></div>
		<div class="row">
			<div class="col-xs-12">
				<!-- 搜索表单，需自定义 -->
				<form class="form-horizontal" id="searchForm">
		            <fieldset>
		                <div class="row">
		                    <div class="col-sm-12">
		                        <div class="form-group">
		                        	<label for="loginName" class="col-sm-1 control-label col-xs-12 no-padding-right">会员账号：</label>
									<div class="col-sm-2">
									    <input class="form-control" name="loginName" id="loginName" value="" type="text">
									</div>
									<label for="memberName" class="col-sm-1 control-label col-xs-12 no-padding-right">姓名：</label>
									<div class="col-sm-2">
									    <input class="form-control" name="memberName" id="memberName" value="" type="text">
									</div>
									<label for="registerDateStart" class="col-sm-1 control-label col-xs-12 no-padding-right">注册时间：</label>
									<div class="col-sm-2">
									    <input name="registerDateStart" id="registerDateStart" readonly="readonly" value="" class="input-medium" type="text">
									</div>
									<label for="registerDateEnd" class="col-sm-1 control-label col-xs-12 no-padding-right">至</label>
									<div class="col-sm-2">
									    <input name="registerDateEnd" id="registerDateEnd" readonly="readonly" value="" class="input-medium" type="text">
									</div>
								</div>
								<div class="form-group">	
									<label for="lastLoginTimeStart" class="col-sm-1 control-label col-xs-12 no-padding-right">最后登录时间</label>
									<div class="col-sm-2">
									    <input name="lastLoginTimeStart" id="lastLoginTimeStart"  readonly="readonly" value="" class="input-medium" type="text">
									</div>
									<label for="lastLoginTimeEnd" class="col-sm-1 control-label col-xs-12 no-padding-right">至</label>
									<div class="col-sm-2">
									    <input name="lastLoginTimeEnd" id="lastLoginTimeEnd"  readonly="readonly" value="" class="input-medium" type="text">
									</div>
									<div class="col-sm-3">
			                            <input type="button" value="搜索" class="btn btn-sm btn-yougou" onclick="doQuery();"/>
									</div>
		                        </div>
		                    </div>
		                </div>
		            </fieldset>
	        	</form>
			</div>
		</div>
	</div>
	
	<div class="space-6"></div>
	<div class="col-xs-12">
		<table id="grid-table" class="mmg"></table>
	    <div id="grid-pager" style="text-align:right;" class=""></div>
    </div>
</div>

<!-- 风险库新增或者修改页面 -->
<div class="row">
	<div class="col-xs-12">
		<div id="viewNavbar" class="hide message-navbar clearfix">
			<div>
				<div class="messagebar-item-left">
					<a href="javascript:void(0);" class="btn-back-message-list" onclick="hideForm();">
						<i class="ace-icon fa fa-arrow-left bigger-110 middle blue"></i>
						<b class="middle bigger-110">返回列表</b>
					</a>
				</div>
			</div>
		</div>
		<form id="viewForm" class="hide form-horizontal message-form col-xs-12">
			<input type="hidden" name="id" id="memberId" value="${id!""}"/>
			
			<div>
				<h4 class="widget-title smaller">
					<i class="ace-icon fa fa-check-square-o bigger-110"></i>
					基本信息
				</h4>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="form-field-recipient">会员账号：</label>
					<div class="col-sm-2">
						<input type="text" name="loginName" id="loginName" size="25" maxlength="30" placeholder=""   readonly="true">
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="form-field-recipient">姓名：</label>
					<div class="col-sm-2">
						<input type="text" name="memberName" id="memberName" size="13" maxlength="20" placeholder=""   readonly="true">
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="form-field-recipient">性别：</label>
					<div class="col-sm-2">
						<input type="text" name="memberSex" id="memberSex" size="13" maxlength="20" placeholder=""   readonly="true">
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="form-field-recipient">生日：</label>
					<div class="col-sm-2">
						<input type="text" name="stringBirthday" size="18" value="" class="input-medium" readonly="true">
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="form-field-recipient">订单总数：</label>
					<div class="col-sm-2">
						<input type="text" name="orderCount"  size="13" maxlength="20"  readonly="true">
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="form-field-recipient">订单总金额：</label>
					<div class="col-sm-2">
						<input type="text" name="orderAmount" id="orderAmount" size="13"  readonly="true">
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="form-field-recipient">注册时间 ：</label>
					<div class="col-sm-2">
					<input type="text" name="stringRegisterDate"   size="18" readonly="true">
					</div>
				</div>
				<div class="hr hr-10 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right" for="form-field-recipient">最后登录时间：</label>
					<div class="col-sm-2">
						<input type="text" name="stringLastLoginTime"   size="18" readonly="true" >
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>