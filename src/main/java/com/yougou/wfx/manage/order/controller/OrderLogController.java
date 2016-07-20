 /*
 * 版本信息
 
 * 日期 2016-04-07 10:33:52
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.order.controller;

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

import com.alibaba.dubbo.common.json.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.enums.OrderLogBelongTypeEnum;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.order.vo.OrderLogPageVo;
import com.yougou.wfx.order.api.background.IOrderLogBackgroundApi;
import com.yougou.wfx.order.dto.input.OrderLogInputDto;
import com.yougou.wfx.order.dto.input.OrderLogPageInputDto;
import com.yougou.wfx.order.dto.output.OrderLogOutputDto;

/**
 * OrderLogController
 * @author wfx
 * @Date 创建时间：2016-04-07 10:33:53
 */
@Controller
@RequestMapping("/order")
public class OrderLogController extends BaseController{
	
	@Resource
	private IOrderLogBackgroundApi orderLogBackgroundApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/orderLogList")
	public String orderLogList(ModelMap map ,HttpServletRequest request){
		map.put("orderNo", request.getParameter("orderNo"));
		map.put("orderId", request.getParameter("orderId"));
		map.put("src", request.getParameter("src"));
		return "/manage/order/orderLogList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryOrderLogData")
	@ResponseBody
	public String queryOrderLogData(ModelMap map, OrderLogPageVo orderLogVo, PageModel pageModel)throws Exception{
		OrderLogPageInputDto pageInputDto = (OrderLogPageInputDto) BeanUtil.convertBean(orderLogVo, OrderLogPageInputDto.class);
		pageInputDto.setLogType(OrderLogBelongTypeEnum.ORDER_LOG.getKey());//查询订单日志
		PageModel<OrderLogOutputDto> result = orderLogBackgroundApi.findPage(pageInputDto, pageModel);
	/*	List<OrderLogOutputDto> list = orderLogBackgroundApi.queryList(pageInputDto);
		return com.alibaba.fastjson.JSONArray.toJSONString(list);*/
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getOrderLogById")
	@ResponseBody
	public String getOrderLogById(String id) throws Exception {
		OrderLogOutputDto orderLog = new OrderLogOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			orderLog = orderLogBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, orderLog);
	}
	
	/**
	 * 通过主键id删除数据
	 */
	@RequestMapping("/removeOrderLog")
	@ResponseBody
	public String removeOrderLog(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			orderLogBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 保存或修改数据
	 */
	@RequestMapping("/saveOrderLog")
	@ResponseBody
	public String saveOrderLog(OrderLogInputDto orderLog) {
		try {
			String id = orderLog.getId();
			if (StringUtils.isNotBlank(id)) {
				orderLogBackgroundApi.update(orderLog);
			}else{
				orderLogBackgroundApi.insert(orderLog);
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
	@RequestMapping("/insertOrderLog")
	@ResponseBody
	public String insertOrderLog(OrderLogInputDto orderLog) {
		try {
			orderLogBackgroundApi.insert(orderLog);
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/updateOrderLog")
	@ResponseBody
	public String updateOrderLog(OrderLogInputDto orderLog) {
		try {
			Assert.hasText(orderLog.getId(), "参数id不能为空");
			orderLogBackgroundApi.update(orderLog);
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
}
