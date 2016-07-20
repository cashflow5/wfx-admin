<@compress single_line=compress_single_line?contains("true")>
<#assign head>

</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/plugin/mmGrid/wfxPaginator.js"></script>
	<script src="/static/js/manage/finance/returnDebtList.js"></script>
	<#if pageModel ?? && pageModel.totalCount gt 0 >
		<script type="text/javascript">
			//分页器
			$('#grid-pager').wfxPaginator({
				formId :  "searchForm",// 表单id
				page : ${pageModel.page},//第几页
				totalCount : ${pageModel.totalCount},//总记录数
				limit : ${pageModel.limit}//一页多少条
			}).init();
		</script>
	</#if>
</#assign>

<#assign body>
<div id="girdContent" class="row">
	<div class="col-xs-12">
        <div class="tabbable">
        	<ul class="nav nav-tabs" id="myTab">
        		<#if queryVo.tabQueryFlag == 1>
					<li class="active"><a data-toggle="tab" href="#returnList" aria-expanded="true">仅退款</a></li>
					<li class=""><a data-toggle="tab" href="#returnList" onclick="tabSelectQuery(2)" aria-expanded="false">退货（已发货）</a></li>
					<li class=""><a data-toggle="tab" href="#returnList" onclick="tabSelectQuery(3)" aria-expanded="false">全部订单</a></li>
				<#elseif queryVo.tabQueryFlag == 2>
					<li class=""><a data-toggle="tab" href="#returnList" onclick="tabSelectQuery(1)" aria-expanded="false">仅退款</a></li>
					<li class="active"><a data-toggle="tab" href="#returnList" aria-expanded="true">退货（已发货）</a></li>
					<li class=""><a data-toggle="tab" href="#returnList" onclick="tabSelectQuery(3)" aria-expanded="false">全部订单</a></li>
				<#elseif queryVo.tabQueryFlag == 3>
					<li class=""><a data-toggle="tab" href="#returnList" onclick="tabSelectQuery(1)" aria-expanded="false">仅退款</a></li>
					<li class=""><a data-toggle="tab" href="#returnList" onclick="tabSelectQuery(2)" aria-expanded="false">退货（已发货）</a></li>
					<li class="active"><a data-toggle="tab" href="#returnList" aria-expanded="true">全部订单</a></li>
				<#else>
				</#if>
			</ul>
			<div class="tab-content">
				<div id="returnList" class="tab-pane fade active in">
					<!-- 搜索表单，需自定义 -->
					<form class="form-horizontal" id="searchForm" action="/finance/returndebt/returnDebtList.sc" type="post">
			            <fieldset>
			                <div class="row">
			                    <div class="col-sm-12">
			                    	<#if queryVo.tabQueryFlag ?? && (queryVo.tabQueryFlag == 1 || queryVo.tabQueryFlag == 2) >
			                        <div class="form-group">
			                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">退款单号：</label>
										<div class="col-sm-2">
										    <input class="form-control input-sm" name="backNo" id="backNo" value="${(queryVo.backNo)!''}" type="text">
										</div>
										<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">申请时间：</label>
										<div class="col-sm-4">
											<input type="text" name="applyStartTime" id="applyStartTime" readonly="readonly" value="${(queryVo.applyStartTime)!''}" class="input-medium" style="width:120px !important">
										    <label>至</label>
										    <input type="text" name="applyEndTime" id="applyEndTime" readonly="readonly" value="${(queryVo.applyEndTime)!''}" class="input-medium" style="width:120px !important">
										</div>
										<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">订单号：</label>
										<div class="col-sm-2">
										    <input class="form-control input-sm" name="orderNo" id="orderNo" value="${(queryVo.orderNo)!''}" type="text">
										</div>
			                        </div>
			                        <div class="form-group">
			                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">分销商编码：</label>
										<div class="col-sm-2">
										    <input class="form-control input-sm" name="storeName" id="storeName" value="${(queryVo.storeName)!''}" type="text">
										</div>
										<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">买家账号：</label>
										<div class="col-sm-2">
										    <input class="form-control input-sm" name="customerNo" id="customerNo" value="${(queryVo.customerNo)!''}" type="text">
										</div>
										<div class="col-sm-3">
				                            <input type="hidden" id="tabQueryFlag" name="tabQueryFlag" value="${queryVo.tabQueryFlag}">
				                            <input type="button" value="搜索" class="btn btn-sm btn-yougou" onclick="doQuery();"/>
				                            <input type="button" value="导出" class="btn btn-sm btn-yougou" onclick="exportExcel();"/>
										</div>
			                        </div>
			                        <#elseif queryVo.tabQueryFlag ?? && queryVo.tabQueryFlag == 3 >
		                        	<div class="form-group">
			                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">退款单号：</label>
										<div class="col-sm-2">
										    <input class="form-control input-sm" name="backNo" id="backNo" value="${(queryVo.backNo)!''}" type="text">
										</div>
										<div class="col-sm-6">
											<select class="input-medium" style="width:90px !important" name="timeFlag"  id="timeFlag" > 
												<option <#if (queryVo.timeFlag)??&&queryVo.timeFlag== 1>selected</#if> value="1">申请时间</option> 
												<option <#if (queryVo.timeFlag)??&&queryVo.timeFlag== 2>selected</#if> value="2">退款时间</option>
											</select>
											<input type="text" name="startTime" id="startTime" readonly="readonly" value="${(queryVo.startTime)!''}" class="input-medium" style="width:120px !important">
										    <label>至</label>
										    <input type="text" name="endTime" id="endTime" readonly="readonly" value="${(queryVo.endTime)!''}" class="input-medium" style="width:120px !important">
										</div>
										<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">订单号：</label>
										<div class="col-sm-2">
										    <input class="form-control input-sm" name="orderNo" id="orderNo" value="${(queryVo.orderNo)!''}" type="text">
										</div>
			                        </div>
			                        <div class="form-group">
			                        	<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">分销商编码：</label>
										<div class="col-sm-2">
										    <input class="form-control input-sm" name="storeName" id="storeName" value="${(queryVo.storeName)!''}" type="text">
										</div>
										<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">退款类型：</label>
										<div class="col-sm-2">
										    <select class="form-control input-small"  name="refundType"  id="refundType" >
										    	<option value="">全部</option>
												<option <#if (queryVo.refundType)??&&queryVo.refundType == 'ONLY_REFUND'>selected</#if> value="ONLY_REFUND">仅退款</option>
												<option <#if (queryVo.refundType)??&&queryVo.refundType == 'DELIVERD_REFUND'>selected</#if> value="DELIVERD_REFUND">已发货仅退款</option>
												<option <#if (queryVo.refundType)??&&queryVo.refundType == 'REJECTED_REFUND'>selected</#if> value="REJECTED_REFUND">退货退款</option>
											</select>
										</div>
										<label for="operator" class="col-sm-1 control-label col-xs-12 no-padding-right">退款状态：</label>
										<div class="col-sm-2">
										    <select class="form-control input-small"  name="refundStatus"  id="refundStatus" >
										    	<option value="">全部</option>
										    	<option <#if (queryVo.refundStatus)??&&queryVo.refundStatus== 2>selected</#if> value="2">待确认退款</option>
												<option <#if (queryVo.refundStatus)??&&queryVo.refundStatus== 3>selected</#if> value="3">退款处理中</option>
												<option <#if (queryVo.refundStatus)??&&queryVo.refundStatus== 4>selected</#if> value="4">退款成功</option>
												<option <#if (queryVo.refundStatus)??&&queryVo.refundStatus== 5>selected</#if> value="5">退款失败</option>
											</select>
										</div>
										<div class="col-sm-3">
				                            <input type="hidden" id="tabQueryFlag" name="tabQueryFlag" value="${queryVo.tabQueryFlag}">
				                            <input type="button" value="搜索" class="btn btn-sm btn-yougou" onclick="doQuery();"/>
				                            <input type="button" value="导出" class="btn btn-sm btn-yougou" onclick="exportExcel();"/>
										</div>
			                        </div>
			                        </#if>
			                    </div>
			                </div>
			            </fieldset>
		        	</form>
		        	
		        	<div class="blank20"></div>
		        	<table class="table-basic table-padding" width="100%">
		        		<colgroup>
	                        <col width="300"/>
	                        <col width="100"/>
	                        <col width="100"/>
	                        <col width="100"/>
	                        <col width="150"/>
	                        <col width="80"/>
	                        <col width="100"/>
	                        <col width="100"/>
	                        <col width="80"/>
	                    </colgroup>
		        		<thead>
		        			<tr>
		                        <th style="text-align: center;">商品信息</th>
		                        <th style="text-align: left;">买家账号</th>
		                        <th style="text-align: left;">退款类型</th>
		                        <th style="text-align: left;">退款状态</th>
		                        <th class="align-center">退款金额</th>
		                        <th style="text-align: left;">退款原因</th>
		                        <th style="text-align: left;">申请时间</th>
		                        <th style="text-align: left;">退款时间</th>
		                        <th class="align-center">操作</th>
		                    </tr>
		        		</thead>
		        	</table>
		        	
		        	<#if pageModel ?? && pageModel.totalCount gt 0>
			        	<#list pageModel.items as item>
			        		<div class="blank10"></div>
				        	<table class="table-ex tcenter table-padding" width="100%">
				        		<colgroup>
			                        <col width="300"/>
			                        <col width="100"/>
			                        <col width="100"/>
			                        <col width="100"/>
			                        <col width="150"/>
			                        <col width="80"/>
			                        <col width="100"/>
			                        <col width="100"/>
			                        <col width="80"/>
			                    </colgroup>
				        		<thead>
				                    <tr>
				                        <th colspan="10" class="align-left">
				                            <span class="light-grey">（${(item.isAfterReceived)!''}）退款单号：${(item.backNo)!''}</span>
				                            <span class="light-grey ml50">订单号：${(item.orderNo)!''}</span>
				                            <span class="light-grey ml50">订单来源：${(item.storeName)!''}</span>
				                            <span class="light-grey ml50">商品编码：${(item.commodityNo)!''}</span>
				                        </th> 
				                    </tr>
			                    </thead>
			                    <tbody>
			                    	<tr>
				                        <td>
				                            <dl class="detailImg nomargin">
				                                <dt><img class="border-gray" src="${(item.commodityPicUrl)!''}" alt=""></dt>
				                                <dd>
				                                    <div>
				                                        <a href="javascript:void(0);">${(item.commodityName)!''}</a>
				                                    </div>
				                                    <div><span class="light-grey">颜色分类：${(item.commodityColor)!''}</span>  <span class="light-grey ml20">尺码：${(item.commoditySize)!''}</span></div>
				                                </dd>
				                            </dl>
				                        </td>
				                        <td style="text-align: left;">${item.customerNo?default('')}</td>
				                        <td style="text-align: left;">
				                        	<#list refundTypeEnum as typeItem>
				                        		<#if item.refundType ?? && item.refundType == typeItem.key>
				                        		${typeItem.desc}
				                        		</#if>
				                        	</#list>
				                        <td style="text-align: left;">
				                        	<#if item.applyStatus ?? && item.applyStatus == 'UNDER_REFUND'>
				                        	退款处理中
				                        	<#elseif item.applyStatus ?? && item.applyStatus == 'SUCCESS_REFUND'>
				                        	退款成功
				                        	<#elseif item.applyStatus ?? && item.applyStatus == 'CLOSE_REFUND'>
				                        	退款关闭
				                        	<#else>
				                        	</#if>
			                        	</td>
				                        <td class="align-center">
				                            ￥${item.refundAmount?default('0')}
				                            <p class="light-grey">（交易￥${item.refundAmount?default('0')}）</p>
				                        </td>
				                        <td style="text-align: left;">${item.refundNote?default('')}</td>
				                        <td style="text-align: left;">${item.getStringApplyDate()?default('')}</td>
				                        <td style="text-align: left;">${item.getStringRefundDate()?default('')}</td>
				                        <td style="text-align: center;">
				                        	<#if (item.applyStatus ?? && item.applyStatus == 'UNDER_REFUND') && (item.refundStatus ?? && item.refundStatus == 2)>
				                            	<a class="Qing" href="javascript:void(0);" onclick="showDetail('${item.id}', '${item.backNo}');">处理退款</a>
				                        	<#else>
				                        		<a class="Qing" href="javascript:void(0);" onclick="showDetail('${item.id}', '${item.backNo}');">查看退款</a>
				                        	</#if>
				                        </td>
				                    </tr>
			                    </tbody>
				        	</table>
			        	</#list>
		        	<#else>
		        		<table class="table-ex tcenter table-padding" width="100%">
		        			<tr>
		        				<td colspan="9" style="text-align: center;">无数据记录</td>
		        			</tr>
		        		</table>
		        	</#if>
		        	
		        	<!--分页start-->
					<div id="grid-pager" style="text-align:right;" class=""></div>
					<!--分页end-->
				</div>
				
			</div>
        </div>
	</div>
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>