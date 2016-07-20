<@compress single_line=compress_single_line?contains("true")>
<#assign head>
<!-- Head -->
<!--#include file="/layouts/head.shtml"-->
<!-- /Head -->
<!-- Navbar -->
<!--#include file="/layouts/navbar.shtml" -->
<!-- /Navbar -->

</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/aftersale/afterSaleDetails.js"></script>
	<script>
    YouGou.UI.progressLoading();
	</script>
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
                <div class="row bg-gray pd10 bd">
                    <div class="col-md-12">
                        <span>
                        <#if (vo.isAfterReceived)??&&(vo.isAfterReceived)=='BEFORE_DELIVERD_REFUND'>(售中)</#if>
                        <#if (vo.isAfterReceived)??&&(vo.isAfterReceived)=='AFTER_DELIVERD_REFUND' >(售后)</#if>退款单号：${(vo.refundNo)!''}</span>
                        <span class="ml20">买家账号：${(vo.buyerLoginName)!''}</span>
                        <span class="ml20">订单来源：${(vo.shopName)!''}</span>
                        <span class="ml20">商品编码：${(vo.wfxCommodityNo)!''}</span>
                        <span class="ml20">发货供货商：${(vo.supplierName)!''}</span>
                    </div>
                </div>
                <div class="blank10"></div>
                <div class="row">
                    <div class="col-md-4 col-xs-4">
                         <h4><strong>退款单基本信息</strong></h4>
                        <dl class="c-h-dl dl-basic-105">
                            <dt>订单号：</dt>
                            <dd>${(vo.wfxOrderNo)!''}</dd>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                            <dt>退款原因：</dt>
                            <dd>${(vo.reason)!''}</dd>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                            <dt>订单状态：</dt>
                            <dd> <#if (vo.orderStatus)??&&vo.orderStatus=='WAIT_PAY'>待付款</#if>               
	                     <#if (vo.orderStatus)??&&vo.orderStatus=='WAIT_DELIVER'>待发货</#if>                  
	                     <#if (vo.orderStatus)??&&vo.orderStatus=='PART_DELIVERED'>部分发货</#if>                 
	                     <#if (vo.orderStatus)??&&vo.orderStatus=='DELIVERED'>已发货</#if>                 
	                     <#if (vo.orderStatus)??&&vo.orderStatus=='TRADE_SUCCESS'>交易成功</#if>                  
	                     <#if (vo.orderStatus)??&&vo.orderStatus=='TRADE_CLOSED'>交易关闭</#if>                
	                		</dd>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                            <dt>退款类型：</dt>
                            <dd><#if (vo.refundType)??&&vo.refundType=='ONLY_REFUND'>仅退款</#if>
								  <#if (vo.refundType)??&&vo.refundType=='DELIVERD_REFUND'>已发货仅退款</#if>
								   <#if (vo.refundType)??&&vo.refundType=='REJECTED_REFUND'>退货退款</#if>
	              			 </dd>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                            <dt>退款状态：</dt>
                            <dd><#if (vo.status)??&&(vo.status)=='APPLYING'>退款申请中</#if>
  <#if (vo.status)??&&(vo.status)=='PENDING_DELIVERD'>待卖家确认收货</#if>
   <#if (vo.status)??&&(vo.status)=='REJECT_REFUND'>卖家拒绝退款</#if>
   <#if (vo.status)??&&(vo.status)=='SUCCESS_REFUND'>退款成功</#if>
   <#if (vo.status)??&&(vo.status)=='CLOSE_REFUND'>退款关闭</#if>
      <#if (vo.status)??&&(vo.status)=='UNDER_REFUND'>退款处理中</#if></dd>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                            <dt>退款金额：</dt>
                            <dd>${(vo.refundFee)?default('')}元</dd>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                            <dt>申请时间：</dt>
                            <dd>${(vo.stringCreateTime)?default('')}</dd>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                            <dt>退款说明：</dt>
                            <dd>${(vo.description)?default('')}</dd>
                        </dl>
                    </div>
                    <div class="col-md-4 col-xs-4">
                        <h4><strong>退款信息</strong></h4>
                        <dl class="c-h-dl dl-basic-105">
                            <dt>退款时间：</dt>
                            <dd>${(vo.stringPayTime)?default('')}</dd>
                        </dl>
                    </div>
                    <div class="col-md-4 col-xs-4">
                        <h4><strong>寄回物流信息</strong></h4>
                        <dl class="c-h-dl dl-basic-105">
                            <dt>物流公司：</dt>
                            <dd>${(vo.companyName)?default('')}</dd>
                        </dl>
                        <dl class="c-h-dl dl-basic-105">
                            <dt>快递单号：</dt>
                            <dd>${(vo.sid)?default('')}</dd>
                        </dl>
                    </div>
                </div>
                <div class="pd10"><h4><strong>申请退款商品信息</strong></h4></div>
                <table class="table-ex tcenter table-padding" width="100%">
                    <colgroup>
                        <col width="30%"/>
                        <col width="10%"/>
                        <col width="10%"/>
                        <col width="10%"/>
                        <col width="10%"/>
                        <col width="10%"/>
                        <col width="5%"/>
                        <col width="5%"/>
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
                                <dt><img class="border-gray" src="${vo['picSmall']?default('')}" alt=""></dt>
                                <dd>${(vo.commodityName)?default('')}</dd>
                            </dl>
                        </td>
                        <td>
                            <div>${(vo.attribute)?default('')}</div>
                        </td>
                        <td>${(vo.thirdPartyCode)?default('')}</td>
                        <td>${(vo.wfxPrice)?default('')}</td>
                        <td class="align-center">
                            ${(vo.commissionLevel1)?default('0')}
                            <p>(<#if (vo.commissionLevel1Percent)??>
                            ${(vo.commissionLevel1Percent)?string.percent}<#else>0%</#if>)</p>
                        </td>
                        <td class="align-center">
                           ${(vo.commissionLevel2)?default('0')}
                            <p>(<#if (vo.commissionLevel2Percent)??>
                            ${(vo.commissionLevel2Percent)?string.percent}<#else>0%</#if>)</p>
                        </td>
                         <td class="align-center">
                           ${(vo.commissionLevel3)?default('0')}
                            <p>(<#if (vo.commissionLevel3Percent)??>
                            ${(vo.commissionLevel3Percent)?string.percent}<#else>0%</#if>)</p>
                        </td>
                        <td> ${(vo.buyNum)?default('')}</td>
                        <td> ${(vo.proNum)?default('')}</td>
                    </tr>
                    </tbody>
                </table>
                <div class="blank10"></div>
                <div class="plr10"><h4><strong>日志信息</strong></h4></div>
                <#list logList as log>
                	<#if log.logType==2>
	                <div class="plr10">
	                    <div class="border-bottom-dotted ptb10 plr30">
	                        <strong><#if (log.optBelong)??&&(log.optBelong)==1>买家</#if>
	                        <#if (log.optBelong)??&&(log.optBelong)==2>卖家</#if></strong><span class="ml20">${(log.stringCreateTime)?default('')}</span>
	                    </div>
	                    <#if (log.logInfo)??>
	                    <div class="pt10 plr30">
	                    <#list (log.logInfo)?split('#') as item>
	                        <p>${item!''}</p>
	                    </#list>
	                    </div>
	                    </#if>
	                </div>
	                </#if>
               </#list>
                </div>
                <div class="blank20"></div>
                <div >
                 	<ul class="c-h-ul search-ul query-ul">
                 	    <li>
			            </li>
			            <#if (vo.refundType)??&&(vo.refundType=='ONLY_REFUND'||vo.refundType=='DELIVERD_REFUND')&&(vo.status)??&&(vo.status)=='APPLYING'>
			            <!--仅退款\已发货仅退款：退款状态为“退款申请中”时显示【同意退款】 -->
			            <li>
			                <a href="javascript:handleRefund('agree','${(vo.refundNo)}');" class="btn btn-sm btn-yougou">同意退款</a>
			            </li>
			            </#if>
			            <#if (vo.refundType)??&&(vo.status)??&&((vo.status=='APPLYING'&&vo.refundType=='DELIVERD_REFUND')||(vo.status=='PENDING_DELIVERD'&&vo.refundType=='REJECTED_REFUND'))>
			            <!-- 已发货仅退款：退款状态为“退款申请中”时显示【拒绝退款】 ,退货退款：退款状态为待卖家确认收货时显示【拒绝退款】-->
			            <li>
			                <a <a href="javascript:handleRefund('deny','${(vo.refundNo)}');" class="btn btn-sm btn-yougou">拒绝退款</a>
			            </li>
			            </#if>
			             <#if (vo.refundType)??&&(vo.status)??&&(vo.status=='PENDING_DELIVERD'&&vo.refundType=='REJECTED_REFUND')>
			             <!--退货退款：退款状态为待卖家确认收货时显示【同意收货】-->
			             <li>
			                <a <a href="javascript:handleRefund('deliverd','${(vo.refundNo)}');" class="btn btn-sm btn-yougou">确认收货</a>
			            </li>
			            </#if>
			            <li>
			                <a href="javascript:history.back();" class="btn btn-sm btn-yougou">返回</a>
			            </li>
			            
		        	</ul>
                    <!--dv class="col-md-12">
                        <a href="javascript:history.back();" class="btn btn-sm btn-yougou">返回</a>
                    </dv-->
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