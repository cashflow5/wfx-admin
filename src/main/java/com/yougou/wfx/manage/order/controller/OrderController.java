 /*
 * 版本信息
 
 * 日期 2016-03-25 10:43:03
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.order.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.aftersale.dto.output.OrderApplyOutputDto;
import com.yougou.wfx.basicset.api.background.ISysConfigBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityProductBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityStyleBackgroundApi;
import com.yougou.wfx.commodity.dto.output.CommodityProductOutputDto;
import com.yougou.wfx.commodity.dto.output.CommodityStyleOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.enums.OrderStatusEnum;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.order.vo.OrderPageVo;
import com.yougou.wfx.order.api.background.IOrderBackgroundApi;
import com.yougou.wfx.order.api.background.IOrderRemarkBackgroundApi;
import com.yougou.wfx.order.dto.input.OrderInputDto;
import com.yougou.wfx.order.dto.input.OrderPageInputDto;
import com.yougou.wfx.order.dto.input.OrderRemarkInputDto;
import com.yougou.wfx.order.dto.output.OrderConsignOutputDto;
import com.yougou.wfx.order.dto.output.OrderDetailDto;
import com.yougou.wfx.order.dto.output.OrderOutputDto;
import com.yougou.wfx.seller.api.background.ISellerInfoBackgroundApi;
import com.yougou.wfx.seller.dto.output.SellerInfoOutputDto;
import com.yougou.wfx.util.ApiConstant;

/**
 * OrderController
 * @author wfx
 * @Date 创建时间：2016-03-25 10:43:03
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController{
	
	@Resource
	private IOrderBackgroundApi orderBackgroundApi;
	@Resource
	private ISysConfigBackgroundApi sysConfigBackgroundApi;
	@Resource
	private ICommodityStyleBackgroundApi commodityStyleBackgroundApi;
	@Resource
	private IOrderRemarkBackgroundApi orderRemarkBackgroundApi;
	@Resource
	private ISellerInfoBackgroundApi sellerInfoBackgroundApi;
	@Resource
	private ICommodityProductBackgroundApi commodityProductBackgroundApi;
	/**
	 * 进入菜单
	 */
	@RequestMapping("/orderList")
	public String orderList(ModelMap map){
		map.put("orderStatus", OrderStatusEnum.values());
		return "/manage/order/orderList";
	}
	
	
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryOrderData")
	@ResponseBody
	public String queryOrderData(ModelMap map, OrderPageVo orderVo, PageModel<OrderOutputDto> pageModel)throws Exception{
		OrderPageInputDto pageInputDto = (OrderPageInputDto) BeanUtil.convertBean(orderVo, OrderPageInputDto.class);
		PageModel<OrderOutputDto> result = orderBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getOrderById")
	@ResponseBody
	public String getOrderById(String id) throws Exception {
		OrderOutputDto order = new OrderOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			order = orderBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, order);
	}
	
	/**
	 * 通过主键id删除数据
	 */
	@RequestMapping("/removeOrder")
	@ResponseBody
	public String removeOrder(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			orderBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 保存或修改数据
	 */
	@RequestMapping("/saveOrder")
	@ResponseBody
	public String saveOrder(OrderInputDto order) {
		try {
			String id = order.getId();
			if (StringUtils.isNotBlank(id)) {
				orderBackgroundApi.update(order);
			}else{
				orderBackgroundApi.insert(order);
			}
		} catch (Exception e) {
			logger.error("保存数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 插入一条新数据，id会自动生成
	 */
	@RequestMapping("/insertOrder")
	@ResponseBody
	public String insertOrder(OrderInputDto order) {
		try {
			orderBackgroundApi.insert(order);
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/updateOrder")
	@ResponseBody
	public String updateOrder(OrderInputDto order) {
		try {
			Assert.hasText(order.getId(), "参数id不能为空");
			orderBackgroundApi.update(order);
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}

	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/orderDetails")
	public String queryOrderDetails(ModelMap map, OrderPageVo orderVo,String src, HttpServletRequest request)throws Exception{
		String  orderId = request.getParameter("orderId");
		OrderApplyOutputDto   orderApply = null;
		String h5Domain = sysConfigBackgroundApi.getValueBykey(ApiConstant.WFX_DOMAIN_H5);
		if( StringUtils.isNotEmpty(orderId) ){
			orderApply = orderBackgroundApi.queryOrderDetails(orderId);
			map.put("orderId", orderId);
			map.put("orderNo", orderApply.getWfxOrderNo());
			SellerInfoOutputDto sellerInfo = sellerInfoBackgroundApi.getById(orderApply.getSellerId());
			map.put("sellerAccount", sellerInfo == null ? "" :sellerInfo.getLoginName());
		}
		List<OrderDetailDto> detailList = orderApply.getOrderDetail();
		if(null != detailList && detailList.size() > 0){
			for(OrderDetailDto detail:detailList){
				String commodityId = detail.getCommodityId();
				CommodityStyleOutputDto style = commodityStyleBackgroundApi.getById(commodityId);
				if(null != style){
					detail.setCommodityNo(style.getNo());
				}
				CommodityProductOutputDto product = commodityProductBackgroundApi.getById(detail.getProdId());
				detail.setThirdPartyCode(product==null ? "" : product.getThirdPartyCode());
			}
		}
		map.put("orderApply", orderApply);
		map.put("src", src);
		map.put("h5Domain", h5Domain);
		
		return "/manage/order/orderDetails";
	}
	@RequestMapping("/logisDetails")
	public String logisDetails(ModelMap map,String orderId, HttpServletRequest request)throws Exception{
		List<OrderConsignOutputDto> orderLogisList  = orderBackgroundApi.getOrderLogisDetail(orderId);
		map.put("orderLogisList", orderLogisList);
		return "/manage/order/logisDetails";
	}
	
	/**
	 * 保存备注
	 * @param orderId
	 * @param markNote
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveRemark")
	public String saveRemark(String orderId,String markNote, HttpServletRequest request){
		
		if( StringUtils.isEmpty(orderId) || StringUtils.isEmpty(markNote) ){
			return super.jsonResult(StateCode.ERROR, ERROR);
		}
		OrderRemarkInputDto dto = new OrderRemarkInputDto();
		String operator =  getSysUserName();
		dto.setCreateTime( new Date() );
		dto.setOperator( operator );
		dto.setOrderId(orderId);
		dto.setType(1);// 优购的备注
		dto.setMarkNote( markNote );
		orderRemarkBackgroundApi.insert(dto);
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}

}
