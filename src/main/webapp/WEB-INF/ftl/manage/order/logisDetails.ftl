  
                             <#if orderLogisList??&&(orderLogisList?size>0)>
							<#list orderLogisList as item>
							
								 <h5>包裹${(item_index+1)}</h5>
                                <div class="ml20">
                                    <span>物流公司：${item.expressName!""}</span>
                                    <span class="ml20">快递单号：${item.expressNo!""}</span>
                                    <span class="ml20">物流跟踪：
                                        <a href="javascript:void(0);" class="orange trip10 nowrap">
                                        ${item.expressName!""}
										<i class="fa fa-angle-down"></i></a>
										<div class="logistics-message hide">
											<div class="f12"><div class="bg-gray pd10 bd">快递公司：${item.expressName!""}<span class="ml10">官方查询电话：400-889-5543</span></div>
                                        	 <#if item??&&item.logisticsData??&&(item.logisticsData?size>0)>
												<#list item.logisticsData as data>
													<dl class="c-h-dl dl-basic-field"><dt class="align-center">${data.time!""}</dt><dd class="mt10">${data.context!""}</dd></dl>
										 		</#list>
											 
											 </#if>	
											 </div>
										</div>
                                    </span>
                                    <div class="blank20"></div>
                                     <table class="table-ex tcenter table-padding" style="width:40%">
                                        <thead>
                                            <tr>
                                                <th>商品信息</th>
                                                <th>数量</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                             <#if item??&&item.orderConsigncommodity??&&(item.orderConsigncommodity?size>0)>
												<#list item.orderConsigncommodity as consigncommodity>
												
                                            <tr>
										 	
                                                <td class="align-left">
                                                    <dl class="detailImg nomargin">
                                                        
                                                        
                                                        <dt><img class="border-gray" src="${consigncommodity.defaultPic!""}" alt=""></dt>
                                                        
                                                        <dd>
                                                            <div>
                                                                <a href="#" class="Qing">${consigncommodity.prodName!""}</a><span class="Gray">${consigncommodity.prodSpec!""}</span>
                                                            </div>
                                                            <div><span class="Gray">货品条码：</span>321231243  <span class="Gray ml20">归属商家：</span>${consigncommodity.shopName!""}</div>
                                                        </dd>
                                                    </dl>
                                                </td>
                                                <td>${consigncommodity.num!""}</td>
                                            </tr>
                                            	</#list>
										 <#else>
											<tr><td colSpan="12" style="text-align:center">
											
											抱歉，没有您要找的数据</td></tr>
										 </#if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
							
							
                            </#list>
							 <#else>
								<tr><td colSpan="12" style="text-align:center">抱歉，没有您要找的数据</td></tr>
							 </#if>