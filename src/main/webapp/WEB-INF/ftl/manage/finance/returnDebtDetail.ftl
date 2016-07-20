<@compress single_line=compress_single_line?contains("true")>
<#assign head>

</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/finance/returnDebtList.js"></script>
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
            	<form id="refundForm" action="" type="post">
            		<input type="hidden" name="id" value="${(returnDebt.id)!''}"> 
            		<input type="hidden" name="orderNo" value="${(returnDebt.orderNo)!''}">
            		<input type="hidden" name="backNo" value="${(returnDebt.backNo)!''}">
            		<input type="hidden" name="tabQueryFlag" value="${queryVo.tabQueryFlag}">
            	</form> 
                <div class="row bg-gray pd10 bd">
                    <div class="col-md-12">
                        <span>订单号：${returnDebt.orderNo?default('')}</span>
                        <span class="ml20">分销商编码：${returnDebt.storeName?default('')}</span>
                        <span class="ml20">商品编号：${orderRefund.wfxCommodityNo?default('')}</span>
                        <span class="ml20">发货供应商：${orderRefund.supplierName?default('')}</span>
                    </div>
                </div>
                <div class="blank10"></div>
                <div class="row">
                   <div class="col-md-4 col-xs-4 border-right">
                        <div class="row pb10 border-bottom">
                            <div class="fl w105 align-right"><strong>退款申请</strong></div>
                            <div class="fr mr5"> <strong class="fr orange"><i class="fa fa-lightbulb-o f16 mr5"></i>待处理</strong></div>
                        </div>
                        <dl class="c-h-dl dl-basic-105">
                            <dt class="grey">退款申请单：</dt>
                            <dd class="grey">${returnDebt.backNo?default('')}</dd>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                            <dt class="grey">退款类型：</dt>
                            <dd>
                            	<#list refundTypeEnum as typeItem>
	                        		<#if (returnDebt.refundType) ?? && returnDebt.refundType == typeItem.key>
	                        		${typeItem.desc}
	                        		</#if>
	                        	</#list>
                            </dd>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                            <dt class="grey">退款金额：</dt>
                            <dd class="grey"><strong class="red">${(returnDebt.refundAmount)!''}</strong> 元</dd>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                            <dt class="grey">退款方式：</dt>
                            <dd class="grey">${returnDebt.bankName?default('')}</dd>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                            <dt class="grey">退款原因：</dt>
                            <dd class="grey">${returnDebt.refundNote?default('')}</dd>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                            <dt class="grey">买家账号：</dt>
                            <dd class="green">${returnDebt.customerNo?default('')}</dd>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                            <dt class="grey">申请时间：</dt>
                            <dd class="grey">${returnDebt.getStringApplyDate()?default('')}</dd>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                            <dt class="grey">退款说明：</dt>
                            <dd class="grey">${returnDebt.refundDesc?default('')}</dd>
                        </dl>
                    </div>
                    <#if (returnDebt.refundType) ?? && (returnDebt.refundType == 'ONLY_REFUND' || returnDebt.refundType == 'DELIVERD_REFUND')>
                    <div class="col-md-8 col-xs-8">
                    	<#if returnDebt.refundStatus ?? && returnDebt.refundStatus == 2>
	                        <div class="mt40 pt10 ml50">
	                            <i class="fa fa-exclamation-circle blue f32 middle"></i>
	                            <span class="f16">请处理退款申请</span><br/>
	                        </div>
	                        <div class="mt40 ml50">
	                            <a href="javascript:void(0);" onclick="agreeRefund('${returnDebt.id}', '${returnDebt.backNo}')" class="btn btn-xs btn-yougou">确认退款</a>
	                        </div>
                        <#elseif returnDebt.refundStatus ?? && returnDebt.refundStatus == 3>
                        	<div class="mt40 pt10 ml50">
	                            <i class="fa fa-exclamation-circle blue f32 middle"></i>
	                            <span class="f16">退款处理中</span><br/>
	                        </div>
	                        <#-- 
	                        <div class="mt40 ml50">
	                            <a href="javascript:void(0);" onclick="changeStatus('${returnDebt.id}', '${returnDebt.backNo}')" class="btn btn-xs btn-yougou">手动退款</a>
	                        </div> -->
                        <#elseif returnDebt.refundStatus ?? && returnDebt.refundStatus == 4>
                        	<div class="mt40 pt10 ml50">
	                            <i class="fa fa-exclamation-circle blue f32 middle"></i>
	                            <span class="f16">退款成功</span><br/>
	                        </div>
                        </#if>
                    </div>
                    </#if> 
                    <#if (returnDebt.refundType) ?? && (returnDebt.refundType == 'REJECTED_REFUND')>
                     <div class="col-md-8 col-xs-8">
                     	<#if returnDebt.refundStatus ?? && returnDebt.refundStatus == 2>
	                        <div class="mt40 pt10 ml50">
	                            <i class="fa fa-exclamation-circle blue f32 middle"></i>
	                            <span class="f16">请处理退款申请</span><br/>
	                            <span class="f16">-买家已退货&nbsp;&nbsp;&nbsp;&nbsp;物流公司：<font class="green"> ${orderRefund.companyName?default('')}</font>&nbsp;&nbsp;&nbsp;&nbsp;快递单号：<font class="green">${orderRefund.sid?default('')}</font></span>
	                        </div>
	                        <div class="mt40 ml50">
	                            <a href="javascript:void(0);" onclick="agreeRefund('${returnDebt.id}', '${returnDebt.backNo}')" class="btn btn-xs btn-yougou">确认退款</a>
	                        </div>
                        <#elseif returnDebt.refundStatus ?? && returnDebt.refundStatus == 3>
                        	<div class="mt40 pt10 ml50">
	                            <i class="fa fa-exclamation-circle blue f32 middle"></i>
	                            <span class="f16">处理退款中</span><br/>
	                            <span class="f16">-买家已退货&nbsp;&nbsp;&nbsp;&nbsp;物流公司：<font class="green"> ${orderRefund.companyName?default('')}</font>&nbsp;&nbsp;&nbsp;&nbsp;快递单号：<font class="green">${orderRefund.sid?default('')}</font></span>
	                        </div>
	                        <#-- 
	                        <div class="mt40 ml50">
	                            <a href="javascript:void(0);" onclick="changeStatus('${returnDebt.id}', '${returnDebt.backNo}')" class="btn btn-xs btn-yougou">手动退款</a>
	                        </div> -->
                        <#elseif returnDebt.refundStatus ?? && returnDebt.refundStatus == 4>
                        	<div class="mt40 pt10 ml50">
	                            <i class="fa fa-exclamation-circle blue f32 middle"></i>
	                            <span class="f16">退款成功</span><br/>
	                            <span class="f16">-买家已退货&nbsp;&nbsp;&nbsp;&nbsp;物流公司：<font class="green"> ${orderRefund.companyName?default('')}</font>&nbsp;&nbsp;&nbsp;&nbsp;快递单号：<font class="green">${orderRefund.sid?default('')}</font></span>
	                        </div>
                        </#if>
                    </div>
                    </#if> 
                </div>
                <div class="pd10"><strong>退款商品信息</strong></div>
                <table class="table-ex tcenter table-padding align-left" width="100%">
                    <colgroup>
                        <col width="300"/>
                        <col width="100"/>
                        <col width="100"/>
                        <col width="80"/>
                        <col width="80"/>
                        <col width="80"/>
                        <col width="80"/>
                        <col width="100"/>
                        <col width="100"/>
                    </colgroup>
                    <thead>
                    <tr>
                        <th class="align-left">商品名称</th>
                        <th class="align-left">商品规格</th>
                        <th class="align-left">商家编码</th>
                        <th class="align-left">分销价</th>
                        <th>一级佣金</th>
                        <th>二级佣金</th>
                        <th>三级佣金</th>
                        <th class="align-left">购买数量</th>
                        <th class="align-left">申请数量</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>
                            <dl class="detailImg nomargin">
                                <dt><img class="border-gray" src="${(orderRefund.picSmall)!''}" alt=""></dt>
                                <dd><a href="javascript:void(0);" class="Qing">${(orderRefund.commodityName)!''}</a></dd>
                            </dl>
                        </td>
                        <td>${orderRefund.specName?default('')}/${orderRefund.sizeNo?default('')}</td>
                        <td>${orderRefund.thirdPartyCode?default('')} </td>
                        <td><span class="green">${orderRefund.wfxPrice?default(0.00)}</span></td>
                        <td class="align-center">
                           	 ￥${orderRefund.commissionLevel1?default(0.00)}
                            <p>（<#if orderRefund.commissionLevel1Percent ??>${orderRefund.commissionLevel1Percent?string.percent}<#else>0%</#if>）</p>
                        </td>
                        <td class="align-center">
                           	 ￥${orderRefund.commissionLevel2?default(0.00)}
                            <p>（<#if orderRefund.commissionLevel2Percent ??>${orderRefund.commissionLevel2Percent?string.percent}<#else>0%</#if>）</p>
                        </td>
                        <td class="align-center">
                           	 ￥${orderRefund.commissionLevel3?default(0.00)}
                            <p>（<#if orderRefund.commissionLevel3Percent ??>${orderRefund.commissionLevel3Percent?string.percent}<#else>0%</#if>）</p>
                        </td>
                        <td><span class="green">${orderRefund.buyNum?default(0)}</span></td>
                        <td><span class="green">${(orderRefund.proNum)!''}</span></td>
                    </tr>
                    </tbody>
                </table>
                <div class="blank10"></div>
                
                <div class="pd10"><strong>收款信息</strong></div>
                <div class="row">
                   <div class="col-md-12 col-xs-12">
                   		<div class="row border-bottom">
                        </div>
                        <dl class="c-h-dl dl-basic-105">
                        	<div class="col-md-5 col-xs-5">
                        		<dt class="grey">付款人：</dt>
                            	<dd class="grey">${alreadyIncome.customerNo!''}</dd>
                        	</div>
                            <div class="col-md-4 col-xs-4">
                        		<dt class="grey">收款日期：</dt>
                            	<dd class="grey">
                            		<#if alreadyIncome.tradeDate ??>
                            			${alreadyIncome.tradeDate?string('yyyy-MM-dd HH:mm:ss')}
                            		</#if>
                            	</dd>
                        	</div>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                        	<div class="col-md-5 col-xs-5">
                        		<dt class="grey">支付方式：</dt>
                            	<dd class="grey">${alreadyIncome.bankName!''}</dd>
                        	</div>
                            <div class="col-md-4 col-xs-4">
                            	<dt class="grey">收款金额：</dt>
                            	<dd class="grey">${alreadyIncome.incomedAmount?default(0)?string('###,##0.00')}</dd>
                            </div>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                        	<div class="col-md-5 col-xs-5">
                        		<dt class="grey">收款账号：</dt>
                            	<dd class="grey">
                            		<#if alreadyIncome.bankName ??>
                            			${alreadyIncome.bankName!''}（${alreadyIncome.incomeAccount!''}）
                            		</#if>
                        		</dd>
                        	</div>
                        	<div class="col-md-4 col-xs-4">
                        		<dt class="grey">已退金额：</dt>
                            	<dd class="grey">${refundedAmount?default(0)?string('###,##0.00')}</dd>
                        	</div>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                        	<div class="col-md-5 col-xs-5">
                        		<dt class="grey">收款交易号：</dt>
                            	<dd class="grey">${returnDebt.bankTradeNo!''}</dd>
                        	</div>
                        	<div class="col-md-4 col-xs-4">
                        		<dt class="grey">可退余额：</dt>
                            	<dd class="grey">${remainAmount?default(0)?string('###,##0.00')}</dd>
                        	</div>
                        </dl>
                   </div>
                </div>
                
                <div class="blank20"></div>
                <div class="plr10"><strong>日志信息</strong></div>
               <#list orderRefundLogs as item> 
                <div class="plr10">
                    <div class="border-bottom-dotted ptb10 plr30">
                        <strong><#if item.optBelong==1>买家<#elseif item.optBelong==2> 卖家</#if></strong><span class="ml20"> ${item.getStringCreateTime()?default('')}</span>
                    </div>
                    <div class="pt10 plr30">
					      <#list item.logInfo ?split("#") as log>
					        <p> ${log}</p>
					      </#list>
                    </div>
                </div>
               </#list>
               
                <div class="blank20"></div>
                <div class="row">
                    <div class="col-md-12">
                        <a href="javascript:void(0);" onclick="toListIndex(${queryVo.tabQueryFlag})" class="btn btn-sm btn-yougou">返回</a>
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
    <a href="javascript:void(0);" id="btn-scroll-up" class="btn-scroll-up btn btn-sm">回到顶部
        <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>
</div>

</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>