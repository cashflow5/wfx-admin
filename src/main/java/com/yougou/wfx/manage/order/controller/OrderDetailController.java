 /*
 * 版本信息
 
 * 日期 2016-04-15 11:56:30
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.order.controller;

import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.order.vo.OrderDetailPageVo;
import com.yougou.wfx.order.api.background.IOrderDetailBackgroundApi;
import com.yougou.wfx.order.dto.input.OrderDetailInputDto;
import com.yougou.wfx.order.dto.input.OrderDetailPageInputDto;
import com.yougou.wfx.order.dto.output.OrderDetailOutputDto;

/**
 * OrderDetailController
 * @author wfx
 * @Date 创建时间：2016-04-15 11:56:31
 */
@Controller
@RequestMapping("/order")
public class OrderDetailController extends BaseController{
	
	@Resource
	private IOrderDetailBackgroundApi orderDetailBackgroundApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/orderDetailList")
	public String orderDetailList(){
		return "/manage/order/orderDetailList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryOrderDetailData")
	@ResponseBody
	public String queryOrderDetailData(ModelMap map, OrderDetailPageVo orderDetailVo, PageModel pageModel)throws Exception{
		OrderDetailPageInputDto pageInputDto = (OrderDetailPageInputDto) BeanUtil.convertBean(orderDetailVo, OrderDetailPageInputDto.class);
		PageModel<OrderDetailOutputDto> result = orderDetailBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getOrderDetailById")
	@ResponseBody
	public String getOrderDetailById(String id) throws Exception {
		OrderDetailOutputDto orderDetail = new OrderDetailOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			orderDetail = orderDetailBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, orderDetail);
	}
	
	/**
	 * 通过主键id删除数据
	 */
	@RequestMapping("/removeOrderDetail")
	@ResponseBody
	public String removeOrderDetail(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			orderDetailBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 保存或修改数据
	 */
	@RequestMapping("/saveOrderDetail")
	@ResponseBody
	public String saveOrderDetail(OrderDetailInputDto orderDetail) {
		try {
			String id = orderDetail.getId();
			if (StringUtils.isNotBlank(id)) {
				orderDetailBackgroundApi.update(orderDetail);
			}else{
				orderDetailBackgroundApi.insert(orderDetail);
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
	@RequestMapping("/insertOrderDetail")
	@ResponseBody
	public String insertOrderDetail(OrderDetailInputDto orderDetail) {
		try {
			orderDetailBackgroundApi.insert(orderDetail);
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/updateOrderDetail")
	@ResponseBody
	public String updateOrderDetail(OrderDetailInputDto orderDetail) {
		try {
			Assert.hasText(orderDetail.getId(), "参数id不能为空");
			orderDetailBackgroundApi.update(orderDetail);
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
}
