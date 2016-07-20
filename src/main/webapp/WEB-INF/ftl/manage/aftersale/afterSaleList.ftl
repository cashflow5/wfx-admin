<@compress single_line=compress_single_line?contains("true")>
<#assign head>
<!--#include file="../layouts/head.shtml"-->
<!-- Navbar -->
<!--#include file="../layouts/navbar.shtml" -->
<!-- Page Sidebar -->
<!--#include file="../layouts/sidebar.shtml" -->
<!-- /Page Sidebar -->
</#assign>

<#assign footer>
	<!-- this page -->
	<script src="/static/js/manage/aftersale/afterSaleList.js"></script>
	<script src="/static/plugin/mmGrid/wfxPaginator.js"></script>
	<#if pageData??>
		<script>
			//分页器
			$('#grid-pager').wfxPaginator({
				formId :  "searchForm",// 表单id
				page : ${pageData.page},//第几页
				totalCount : ${pageData.totalCount},
				limit : ${pageData.limit}//一页多少条
			}).init();
		</script>
	</#if>
</#assign>

<#assign body>
<div class="page-content">
	<form id="searchForm" action="/afterSale/queryAfterSaleList.sc" method="post">
	    <div class="search-content-wrap">
	        <ul class="c-h-ul search-ul query-ul">
	            <li>
	                <label class="c-h-ul-li-title">退款单号：</label>
	                <input type="text" class="form-control input-small" name="refundNo" id="refundNo" value="${(vo.refundNo)!''}" />
	            </li>
	            <li>
	                <label class="c-h-ul-li-title">订单号：</label>
	                <input type="text" class="form-control input-small"  name="wfxOrderNo" id="wfxOrderNo" value="${(vo.wfxOrderNo)!''}" />
	            </li>
	            <li>
	                <label class="c-h-ul-li-title">订单来源：</label>
	                <input type="text" class="form-control input-small"  name="shopName"  id="shopName" value="${(vo.shopName)!''}" />
	            </li>
	            <li>
	                <label class="c-h-ul-li-title">订单状态：</label>
	                <select class="form-control input-small"  name="orderStatus"  id="orderStatus" >
	                    <option value="">请选择</option>
	                    <option <#if (vo.orderStatus)??&&vo.orderStatus=='WAIT_PAY'>selected</#if> value="WAIT_PAY">待付款</option>
	                    <option <#if (vo.orderStatus)??&&vo.orderStatus=='WAIT_DELIVER'>selected</#if> value="WAIT_DELIVER">待发货</option>
	                    <option <#if (vo.orderStatus)??&&vo.orderStatus=='PART_DELIVERED'>selected</#if> value="PART_DELIVERED">部分发货</option>
	                    <option <#if (vo.orderStatus)??&&vo.orderStatus=='DELIVERED'>selected</#if> value="DELIVERED">已发货</option>
	                    <option <#if (vo.orderStatus)??&&vo.orderStatus=='TRADE_SUCCESS'>selected</#if> value="TRADE_SUCCESS">交易成功</option>
	                    <option <#if (vo.orderStatus)??&&vo.orderStatus=='TRADE_CLOSED'>selected</#if> value="TRADE_CLOSED">交易关闭</option>
	                </select>
	            </li>
	            <li>
	                <label class="c-h-ul-li-title">售中/售后：</label>
	                <select class="form-control input-small"  name="isAfterReceived"  id="isAfterReceived" >
	                    <option value="">请选择</option>
	                    <option <#if (vo.isAfterReceived)??&&vo.isAfterReceived=='BEFORE_DELIVERD_REFUND'>selected</#if> value="BEFORE_DELIVERD_REFUND">售中</option>
	                    <option <#if (vo.isAfterReceived)??&&vo.isAfterReceived=='AFTER_DELIVERD_REFUND'>selected</#if> value="AFTER_DELIVERD_REFUND">售后</option>
	                  
	                </select>
	            </li>
	            <li>
	                <label class="c-h-ul-li-title">退款类型：</label>
	                <select class="form-control input-small"  name="refundType"   id="refundType">
	                    <option value="">请选择</option>
	                    <option <#if (vo.refundType)??&&vo.refundType=='ONLY_REFUND'>selected</#if> value="ONLY_REFUND">仅退款</option>
	                    <option <#if (vo.refundType)??&&vo.refundType=='DELIVERD_REFUND'>selected</#if> value="DELIVERD_REFUND">已发货仅退款</option>
	                    <option <#if (vo.refundType)??&&vo.refundType=='REJECTED_REFUND'>selected</#if> value="REJECTED_REFUND">退货退款</option>
	               </select>
	            </li>
	            <li>
	                <label class="c-h-ul-li-title">退款状态：</label>
	                <select class="form-control input-small"  name="status"  id="status">
	                    <option value="">请选择</option>
	                    <option <#if (vo.status)??&&vo.status=='APPLYING'>selected</#if> value="APPLYING">退款申请中</option>
	                    <option <#if (vo.status)??&&vo.status=='PENDING_DELIVERD'>selected</#if> value="PENDING_DELIVERD">待卖家确认收货</option>
	                    <option <#if (vo.status)??&&vo.status=='UNDER_REFUND'>selected</#if> value="UNDER_REFUND">退款处理中</option>
	                    <option <#if (vo.status)??&&vo.status=='REJECT_REFUND'>selected</#if> value="REJECT_REFUND">卖家拒绝退款</option>
	                    <option <#if (vo.status)??&&vo.status=='SUCCESS_REFUND'>selected</#if> value="SUCCESS_REFUND">退款成功</option>
	                    <option <#if (vo.status)??&&vo.status=='CLOSE_REFUND'>selected</#if> value="CLOSE_REFUND">退款关闭</option>
	                    
	                </select>
	            </li>
	           
	            <li>
	                <label class="c-h-ul-li-title">发货供货商：</label>
	                <input type="text" class="form-control input-small"  name="supplierName"  id="supplierName" value="${(vo.supplierName)!''}" />
	            </li>
	            
	            
	            <li class="w480">
	                <label class="c-h-ul-li-title">申请时间：</label>
	                <input type="text" class="input-small"  name="createTimeStart"  id="createTimeStart" value="${(vo.createTimeStart)!''}" readonly="readonly"/>
	                <span class="time-text">至</span>
	                <input type="text" class="input-small"  name="createTimeEnd" id="createTimeEnd" value="${(vo.createTimeEnd)!''}" readonly="readonly"/>
	            </li>
	            <li class="w480">
	                <label class="c-h-ul-li-title">退款时间：</label>
	                <input type="text" class="input-small"  name="payTimeStart"  id="payTimeStart" value="${(vo.payTimeStart)!''}" readonly="readonly"/>
	                <span class="time-text">至</span>
	                <input type="text" class="input-small"  name="payTimeEnd" id="payTimeEnd" value="${(vo.payTimeEnd)!''}" readonly="readonly"/>
	            </li>
	            <li>
	                <label class="c-h-ul-li-title"></label>
	                <a  id="mySubmit" href="javascript:void(0);" class="btn btn-sm btn-yougou" onclick="mySubmit()"  >查询</a>
	            </li>
	        </ul>
	    </div>
    </form>
    <div class="blank20"></div>
    <table class="table-basic table-padding">
       
            <col width="300"/>
            <col width="100"/>
            <col width="100"/>
            <col width="100"/>
            <col width="150"/>
            <col width="80"/>
            <col width="100"/>
            <col width="100"/>
            <col width="60"/>
      
        <thead>
        <tr>
            <th>商品信息</th>
            <th>买家账号</th>
            <th>退款类型</th>
            <th>退款状态</th>
            <th>退款金额</th>
            <th>退款原因</th>
            <th>申请时间</th>
            <th>退款时间</th>
            <th>操作</th>
        </tr>
        </thead>
        </table>
        
        
         <#if pageData?? && (pageData.items)?? && pageData.items?size gt 0> 
				<#list pageData.items as item>
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
                <col width="60"/>
            </colgroup>
            <thead>
                <tr >
                 <th colspan="10" class="align-left">
                      <span class="light-grey"><#if (item.isAfterReceived)??&&(item.isAfterReceived)=='BEFORE_DELIVERD_REFUND'
                      >(售中)</#if><#if (item.isAfterReceived)??&&(item.isAfterReceived)=='AFTER_DELIVERD_REFUND'
                      >(售后)</#if>退款单号：<a  href="#" onClick="javascript:view('${(item.refundNo)?default('')}');">${item['refundNo']?default('')}</a></span>
                      <span class="light-grey ml50">订单号：<a href="/order/orderDetails.sc?orderId=${item['orderId']?default('')}&&src=afterSale" >${item['wfxOrderNo']?default('')}</a></span>
                      <span class="light-grey ml50">订单来源：${item['shopName']?default('')}</span>
                      <span class="light-grey ml50">商品编号：${item['wfxCommodityNo']?default('')}</span>
                 </th>
                </tr>
            </thead>
            <tbody>
				<tr>
                    <td>
                        <dl class="detailImg nomargin">
                            <dt><img class="border-gray" src="${(item.picSmall)?default('')}" alt=""></dt>
                            <dd>
                                <div>
                                    ${item['commodityName']?default('')}
                                </div>
                                <div><span class="light-grey">${item['attribute']?default('')}</span></div>
                            </dd>
                        </dl>
                    </td>
                    <td>${(item.buyerLoginName)?default('')}</td>
                    <td> <#if (item.refundType)??&&item.refundType=='ONLY_REFUND'>仅退款</#if>
  <#if (item.refundType)??&&item.refundType=='DELIVERD_REFUND'>已发货仅退款</#if>
   <#if (item.refundType)??&&item.refundType=='REJECTED_REFUND'>退货退款</#if></td>
                   
                    <td> <#if (item.status)??&&item.status=='APPLYING'>退款申请中</#if>
  <#if (item.status)??&&item.status=='PENDING_DELIVERD'>待卖家确认收货</#if>
   <#if (item.status)??&&item.status=='REJECT_REFUND'>卖家拒绝退款</#if>
   <#if (item.status)??&&item.status=='SUCCESS_REFUND'>退款成功</#if>
   <#if (item.status)??&&item.status=='CLOSE_REFUND'>退款关闭</#if>
   <#if (item.status)??&&(item.status)=='UNDER_REFUND'>退款处理中</#if></td>
                    <td class="align-center">￥${item.refundFee?default('')}<p class="light-grey">（交易￥${item.totalFee?default('')}）</p>
            		</td>
                    <td>${item.reason?default('')}</td>
                    <td>${item.stringCreateTime?default('')}</td>
                    <td>${item.stringPayTime?default('')}</td>
                    <td>
                       <a class="Qing" href="#" onClick="javascript:view('${(item.refundNo)?default('')}');" >查看</a>
                    </td>
                </tr>
               </tbody>
            </table>
		  </#list>
			<#else>
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
	                <col width="60"/>
	            </colgroup>
	            <tbody>
					<tr>
						<td colspan="12" class="td-no">暂无记录！</td>
					</tr>
				</tbody>
			</table>
			</#if>
      
   	<!--列表end-->
</div>
<!--分页start-->
<div class="col-xs-12"><div id="grid-pager" style="text-align:right;" class=""></div></div>
<!--分页end-->
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>