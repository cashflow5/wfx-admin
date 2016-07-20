<@compress single_line=compress_single_line?contains("true")>
<#assign head>
</#assign>

<#assign footer>
	<!-- this page -->
	
	<script type="text/javascript" src="/static/plugin/zclip/ZeroClipboard.min.js"></script>
	<script type="text/javascript" src="/static/js/manage/order/details-order-manage.js"></script>
	<!--script type="text/javascript" src="/static/js/manage/order/logisDetail.js"></script-->
	  <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    
    <style>
    .panel2.remark {width: 67%;}

	a.panel-button.fl.ml20 {
	    height: 25px;  line-height: 25px;  display: inline-block;  padding: 0 10px;  border-radius: 3px;  text-align: center;  color: #000;  border: 1px solid #d3d3d3;  background: #fefefe;  background: -webkit-gradient(linear,left top,left bottom,from(#fefefe),to(#eeeeee));
	}
	.panel-header {
	    border: none;  background: #e4e4e4;  height: 45px;  line-height: 45px;
	    border-top: 1px dotted #ccc;
	}
	.panel-title {
		font-weight: bold;
		padding: 0 10px 0 16px;
		margin-top: -6px;
		float: left;
		background: none;
		margin-top: 0;
		margin-bottom: 0;
		font-size: 16px;
		color: inherit;
		}
    </style>
</#assign>

<#assign body>
				<input type="hidden" id="orderId" value="${orderId!''}">	
				<ul class="nav nav-tabs">
					<li class="active"><a  href="javascript:void(0);" >订单详情</a></li>
					<li class=""><a  href="/order/orderLogList.sc?src=${src!''}&orderNo=${orderNo!''}&orderId=${orderId!''}" >订单日志 </a></li>
				</ul>
                <!-- Page Body -->
                <div class="row bg-gray pd10 bd">
                    <div class="col-md-12">
                        <span>订单号：${orderApply.wfxOrderNo!''}</span>
                        <span class="ml20">买家账号：${orderApply.buyerAccount!''}</span>
                        <span class="ml20">订单来源：${orderApply.shopName!''}</span>
                    </div>
                </div>
                <div class="blank20"></div>
                <div class="row">
                    <div class="col-md-3 col-xs-3">下单时间：<#if orderApply.createdTime??>${orderApply.createdTime?datetime}</#if></div>
                    <div class="col-md-3 col-xs-3">支付方式：
	                    <#if orderApply??&&orderApply.payType??&&orderApply.payType=='alipay'> 
	                   		 支付宝
	                     <#elseif orderApply??&&orderApply.payType??&&orderApply.payType=='wechatpay'> 
	                     	微信支付
	                    <#else>
	                    	其它
	                    </#if>
                    </div>
                    <div class="col-md-3 col-xs-3">支付时间：<#if orderApply.payTime??>${orderApply.payTime?datetime}</#if></div>
                    <div class="col-md-3 col-xs-3">发货供货商：${orderApply.supplierName!''}</div>
                </div>
                <div class="blank10"></div>
                <div class="row">
                    <div class="col-md-3 col-xs-3">订单状态：
                    <#if orderApply??&&orderApply.status??&&orderApply.status=='WAIT_PAY'> 
                   		 待付款
                     <#elseif orderApply??&&orderApply.status??&&orderApply.status=='WAIT_DELIVER'> 
                    	 待发货
                    
                    <#elseif orderApply??&&orderApply.status??&&orderApply.status=='PART_DELIVERED'> 
                     	部分发货
                    
                     <#elseif orderApply??&&orderApply.status??&&orderApply.status=='DELIVERED'> 
                   		  已发货	
                   
                     <#elseif orderApply??&&orderApply.status??&&orderApply.status=='TRADE_SUCCESS'>
                     	交易成功 
                     <#elseif orderApply??&&orderApply.status??&&orderApply.status=='TRADE_CLOSED'> 
                    		 交易关闭
                    <#else>
                    	其它
                    </#if></div>
                    <div class="col-md-3 col-xs-3">分销商账号：${sellerAccount!''}</div>
                    <div class="col-md-3 col-xs-3">上级分销商：${orderApply.parentLoginName!''}</div>
                </div>
                <div class="blank20"></div>
                <table class="table-ex tcenter table-padding" width="100%">
                    <colgroup>
                        <col width="400"/>
                        <col width=""/>
                        <col width=""/>
                        <col width="150"/>
                        <col width=""/>
                        <col width=""/>
                        <col width=""/>
                        <col width=""/>
                    </colgroup>
                    <thead>
                    <tr>
                        <th>商品信息</th> 
                        <th>数量</th>
                        <th>分销价</th>
                        <th>单价</th>
                        <th>一级佣金</th>
                        <th>二级佣金</th>
                        <th>三级佣金</th>
                        <th>优惠券</th>
                        <th>运费</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if orderApply??&&orderApply.orderDetail??&&(orderApply.orderDetail?size>0)>
							<#list orderApply.orderDetail as item>
                    <tr>
                        <td class="tleft">
                            <dl class="detailImg nomargin">
                                <dt><img class="border-gray" src="${item.picSmall!''}" alt=""></dt>
                                <dd>
                                    <div>
                                        <a href="${h5Domain!''}/yougoushop/item/${item.commodityNo!''}.sc" class="Qing" target="blank">${item.prodName} </a><span class="Gray">${item.prodSpec}</span>
                                    </div>
                                    <div><span class="Gray">货品条码：</span>${item.thirdPartyCode}  <span class="Gray ml20">归属商家：</span> ${item.shopName}</div>
                                </dd>
                            </dl>
                        </td>
                        <td class="align-center">${item.num!""} </td>
                        <td class="align-center">${item.num!""}</td>
                        <td class="align-center">${item.price!""}</td>
                        <td class="align-center">${item.commissionLevel1!"0"}(<#if item.commissionLevel1Percent??>${item.commissionLevel1Percent?string.percent}<#else>0%</#if>)</td>
                        <td class="align-center">${item.commissionLevel2!"0"}(<#if item.commissionLevel2Percent??>${item.commissionLevel2Percent?string.percent}<#else>0%</#if>)</td>
                        <td class="align-center">${item.commissionLevel3!"0"}(<#if item.commissionLevel3Percent??>${item.commissionLevel3Percent?string.percent}<#else>0%</#if>)</td>
                        <td class="align-center">${item.discountFee!""}</td> 
                        <td class="align-center">${item.postfee!"0"}</td>             
                    </tr>
                     </#list>
					 <#else>
						<tr><td colSpan="12" style="text-align:center">抱歉，没有您要找的数据</td></tr>
					 </#if>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="12">商品总金额：${orderApply.totalFee!""} - 优惠券：${orderApply.discountFee!""} + 运费：${orderApply.postFee!""} = 实付金额：<span class="red">${orderApply.payment!""}</span></td>
                        </tr>
                    </tfoot>
                </table>
                <div class="blank20"></div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="border-bottom-dotted ptb10">
                            <p><strong>配送地址</strong></p>
                            <p>
                                ${orderApply.receiverName!""}，${orderApply.receiverMobile!""}，${orderApply.receiverState!""} ${orderApply.receiverCity!""} ${orderApply.receiverDistrict!""} ${orderApply.receiverAddress!""}
                                <a class="Qing btn-copy"   data-clipboard-text=" ${orderApply.receiverName!""}，${orderApply.receiverMobile!""}，${orderApply.receiverState!""} ${orderApply.receiverCity!""} ${orderApply.receiverDistrict!""} ${orderApply.receiverAddress!""}" href="javascript:void();">复制信息</a>
                            </p>
                        </div>
                        <div class="blank20"></div>
                        <div class="border-bottom-dotted ptb10">
                            <p><strong>物流信息</strong></p>
                            <div id="logisDetails"> 
                            	<img src="/assets/ace/img/loading.gif" />
                            </div>
                        </div>
                        
                        <div class="panel2 mt20 remark fl">
			                <div class="panel-header">
			                    <div class="panel-title">
			                        	订单备注
			                    </div>
			                </div>
			                <div class="panel-body pd10">
			                    <div class="clearfix">
			                        <textarea id="remarkTextarea" class="inputtxt fl" style="width:430px;height:90px;" name="markNote"></textarea>
			                        <a href="javascript:;" class="panel-button fl ml20" style="margin-top: 80px; width: 50px; margin-left: 30px;" onclick="saveRemark();">提交</a>
			                    </div>
			                   <!-- <div class="mt30" style="border-bottom: 1px solid rgb(221, 221, 221); height: 37px;">
			                        	顾客留言：
			                        <span class="c5f" style="line-height:20px">
				          			没有数据显示
			                        </span>
			                    </div> -->
			                    <#if orderApply??&&orderApply.orderRemark??&&(orderApply.orderRemark?size>0)>
									<#list orderApply.orderRemark as item>
									<dl class="sr-remark mt5" >
									<dt><b>${item.operator!""}</b>
									<span class="ml5 cgray">${item.createTime?datetime}</span></dt>
									<dd><span class="c5f">${item.markNote!""}</span></dd></dl>
				                    </#list>
								 <#else>
									<dl class="sr-remark mt5" id="orderRemarkInfo">没有数据显示</dl>
								 </#if>
			                </div>
			            </div>
                        
                        <div class="blank20"></div>
                        <div class="col-md-12">
                        	<a href="#" onclick="DetailsOrderManage.Back('${src!''}');return false;" class="btn btn-sm btn-yougou">返回</a>
                  	 	</div>
                    </div>
                    
                </div>
                <!-- /Page Body -->
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>