<@compress single_line=compress_single_line?contains("true")>
<#assign head>
<style>
.commInfo{
	height:34px;
	overflow:hidden;
}
.commInfo a:hover{
	text-decoration:none;
	cursor:default;
}
</style>
</#assign>

<#assign footer>
	<!-- this page -->
    <script src="/static/plugin/layer/laytpl.js"></script>
	<script src="/static/js/manage/aftersale/applyEditInit.js"></script>
	<script src="/static/js/manage/aftersale/applyEdit.js"></script>
</#assign>

<#assign body>
<div class="main-content">
    <div class="main-content-inner">
        <div class="page-content" id="page-content">
            <!-- Page Body -->
            <div class="row bg-gray pd10 bd">
                <div class="col-md-12">
                    <span>订单号：${wfxOrderNo!''}</span>
                    <span class="ml20">买家账号：${buyerAccount!''}</span>
                    <span class="ml20">订单来源：${shopName!''}</span>
                </div>
            </div>
            <div class="blank20"></div>
            <form method="post" id="actionForm" action="#">
	           <input type="hidden" name="orderId" id="orderId" value="${id!''}" />
	           <input type="hidden" name="orderDetailId" id="orderDetailId"/>
	           <input type="hidden" id="postFee" value="${postFee!'0'}"/>
               <div class="row">
                    <div class="col-md-12 col-xs-12"> 退款类型：
                        <label><input type="radio" name="refundType" value="DELIVERD_REFUND"> 已发货仅退款 </label>
                        <label><input type="radio" name="refundType" value="REJECTED_REFUND"> 退货退款 </label></div>
                </div>
                <div class="f16 bolder ptb10">选择退款商品</div>
                
				<table class="table-ex tcenter table-padding" width="100%">
                    <colgroup>
                        <col width="30">
                        <col>
                        <col width="70">
                        <col width="70">
                        <col width="70">
                        <col width="120">
                        <col width="120">
                        <col width="70">
                        <col width="70">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>&nbsp;</th>
                        <th>商品信息</th>
                        <th>数量</th>
                        <th>分销价</th>
                        <th>单价</th>
                        <th>一级佣金</th>
                        <th>二级佣金</th>
                        <th>三级佣金</th>
                        <th>可退数量</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if detailList?size &gt; 0>
                    	<#list detailList as item>
	                    <tr>
	                        <td>
	                        <input type="hidden" value="${((item.canReturnNum)!'')?c}" />
	                        <input type="radio" name="goodsselect"
	                        <#if (item.canReturnNum &lt;= 0) || (item.canReturnFee &lt;= 0)>
	                        disabled
	                        </#if>
	                        value="${(item.id)!''}">
	                        <input type="hidden" value="${((item.canReturnFee)!'')?c}" />
	                        <input type="hidden" value="${((item.price)!'')?c}" />
	                        <input type="hidden" value="${(item.refundAddress)!''}" /></td>
	                        <td class="tleft">
	                            <dl class="detailImg nomargin">
	                                <dt><img class="border-gray" src="${(item.style.picSmall)!''}" alt=""></dt>
	                                <dd>
	                                    <div class="commInfo">
	                                       <a href="javasscript:void(0);" class="Qing">${(item.prodName)!''}&nbsp;&nbsp;(${(item.prodSpec)!''})</a>
	                                    </div>
	                                    <div><span class="Gray">商家编码：</span>${(item.product.insideCode)!''}
	                                    </div>
	                                </dd>
	                            </dl>
	                        </td>
	                        <td class="align-center">${(item.num)!''}</td>
	                        <td class="align-center">${(item.price)!''}</td>
	                        <td class="align-center">${(item.price)!''}</td>
	                        <td class="align-center">${(item.commissionLevel1)!'0'}（<#if item.commissionLevel1Percent??>${item.commissionLevel1Percent?string.percent}<#else>0%</#if>）</td>
	                        <td class="align-center">${(item.commissionLevel2)!'0'}（<#if item.commissionLevel2Percent??>${item.commissionLevel2Percent?string.percent}<#else>0%</#if>）</td>
	                        <td class="align-center">${(item.commissionLevel3)!'0'}（<#if item.commissionLevel3Percent??>${item.commissionLevel3Percent?string.percent}<#else>0%</#if>）</td>
	                        <td class="align-center">${(item.canReturnNum)!''}</td>
	                    </tr>
	                    </#list>
                    <#else>
                    	<tr><td colspan="8" style="text-align:center;">没有数据</td></tr>
                    </#if>
                    </tbody>
                </table>
                <div class="space-6"></div>
                <div id="returnForm" class="form-body info-pb10 ml20"></div>
            </form>
            <!-- /Page Body -->
        </div>
    	<br/><br/><br/><br/><br/><br/>
        <!-- /.page-content -->
    </div>
    
    <script id="formtemplate" type="text/html">
        <input name="id" type="hidden" value="{{d.id}}"/>
        {{# if(d.refundType == 'REJECTED_REFUND') {}}
        <dl class="c-h-dl dl-basic-field">
            <dt class="tright"><em class="red">* </em> 退货数量：</dt>
            <dd>
                <input type="text" name="proNum" id="proNum" class="form-control input-small">
            </dd>
        </dl>
        {{# }}}
        <dl class="c-h-dl dl-basic-field">
            <dt class="tright"><em class="red">* </em>退款原因：</dt>
            <dd>
                <select name="reason" id="reason" class="form-control input-medium inline-block">
                    <option value="七天无理由">七天无理由</option>
                    <option value="不想要了">不想要了</option>
                    <option value="质量问题">质量问题</option>
                </select>
            </dd>
        </dl>
        <dl class="c-h-dl dl-basic-field">
            <dt class="tright"><em class="red">* </em>退款金额：</dt>
            <dd>
                <input type="text" name="refundFee" id="refundFee" class="form-control input-small">
            </dd>
        </dl>
        {{# if(d.refundType == 'REJECTED_REFUND') {}}
        <dl class="c-h-dl dl-basic-field">
            <dt class="tright"><em class="red">* </em>快递公司：</dt>
            <dd>
                <input type="text" name="companyName" id="companyName" class="form-control input-small">
            </dd>
        </dl>
        <dl class="c-h-dl dl-basic-field">
            <dt class="tright"><em class="red">* </em>快递单号：</dt>
            <dd>
                <input type="text" name="sid" id="sid" class="form-control input-small">
            </dd>
        </dl>
        {{# }}}
        <dl class="c-h-dl dl-basic-field">
            <dt class="tright"><em class="red">* </em>退货说明：</dt>
            <dd>
                <textarea class="form-control input-large" name="description" id="description" rows="5"></textarea>
            </dd>
        </dl>
        {{# if(d.refundType == 'REJECTED_REFUND') {}}
        <dl class="c-h-dl dl-basic-field">
            <dt class="tright">退货地址：</dt>
            <dd><span id="refundAddr"></span></dd>
        </dl>
        {{# }}}
        <dl class="c-h-dl dl-basic-field">
            <dt class="tright">&nbsp;</dt>
            <dd>
                <button type="button" onclick="applyRefund();" class="btn btn-yougou btn-sm">申请</button>
                <a href="javascript:void(0);" onclick="back();" class="btn btn-yougou btn-sm">返回</a>
            </dd>
        </dl>
    </script>
</div>
</#assign>

<#include "/include/pageBuilder.ftl" />
</@compress>