 /*
 * 版本信息
 
 * 日期 2016-04-07 14:34:59
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */
package com.yougou.wfx.manage.aftersale.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.aftersale.api.background.IOrderRefundBackgroundApi;
import com.yougou.wfx.aftersale.dto.RefundSatistics;
import com.yougou.wfx.aftersale.dto.input.OrderApplyInputDto;
import com.yougou.wfx.aftersale.dto.input.OrderRefundInputDto;
import com.yougou.wfx.aftersale.dto.output.OrderApplyOutputDto;
import com.yougou.wfx.aftersale.dto.output.OrderRefundOutputDto;
import com.yougou.wfx.commodity.api.background.ICommodityProductBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityStyleBackgroundApi;
import com.yougou.wfx.commodity.dto.output.CommodityProductOutputDto;
import com.yougou.wfx.commodity.dto.output.CommodityStyleOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.dto.base.WFXResult;
import com.yougou.wfx.enums.OrderStatusEnum;
import com.yougou.wfx.enums.RefundStatusEnum;
import com.yougou.wfx.enums.ResultCodeEnum;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.aftersale.vo.ApplyPageVo;
import com.yougou.wfx.manage.aftersale.vo.OrderRefundPageVo;
import com.yougou.wfx.order.api.background.IOrderBackgroundApi;
import com.yougou.wfx.order.api.background.IOrderDetailBackgroundApi;
import com.yougou.wfx.order.dto.input.OrderDetailInputDto;
import com.yougou.wfx.order.dto.input.OrderInputDto;
import com.yougou.wfx.order.dto.output.OrderDetailOutputDto;
import com.yougou.wfx.order.dto.output.OrderOutputDto;
import com.yougou.wfx.util.UserSecurityUtil;

/**
 * OrderController
 * @author wfx
 * @Date 创建时间：2016-04-07 14:34:59
 */
@Controller
@RequestMapping("/afterSale")
public class ApplyController extends BaseController{
	
	@Resource
	private IOrderBackgroundApi orderBackgroundApi;
	@Resource
	private IOrderDetailBackgroundApi orderDetailBackgroundApi;
	@Resource
	private IOrderRefundBackgroundApi orderRefundBackgroundApi;
	@Resource
	private ICommodityProductBackgroundApi commodityProductBackgroundApi;
	@Resource
	private ICommodityStyleBackgroundApi commodityStyleBackgroundApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/applyList")
	public String applyList(ModelMap map,String type,String id){
		if(StringUtils.isNotBlank(type) && "2".equals(type)){
			OrderOutputDto orderOutputDto = orderBackgroundApi.getById(id);
			OrderDetailInputDto detailInputDto = new OrderDetailInputDto();
			detailInputDto.setOrderId(id);
			List<OrderDetailOutputDto> detailList = orderDetailBackgroundApi.queryList(detailInputDto);
			if(null != detailList && detailList.size() > 0){
				for(OrderDetailOutputDto temp:detailList){
					//查询货口表数据
					CommodityProductOutputDto product = commodityProductBackgroundApi.getById(temp.getProdId());
					temp.setProduct(product);
					if(null != product){
						//查询商品表数据
						CommodityStyleOutputDto style = commodityStyleBackgroundApi.getById(product.getCommodityId());
						temp.setStyle(style);
					}
					//查询退货地址
					String address = orderRefundBackgroundApi.getRefundAddredd(temp.getId(), orderOutputDto.getSupplierId());
					temp.setRefundAddress(address);
					//查询退款表，用于确定可退数量和可退金额
					OrderRefundInputDto refund = new OrderRefundInputDto();
					refund.setOrderDetailId(temp.getId());
					RefundSatistics rs = new RefundSatistics();
					rs.setOrderDetailId(temp.getId());
					rs.initDefaultStatus();
					RefundSatistics refundSatistics = orderRefundBackgroundApi.refundSatistics(rs);
					Integer returnNum = 0;
					Double returnFee = 0.0;
					if(null != refundSatistics){
						returnNum = refundSatistics.getReturnNum()==null?0:refundSatistics.getReturnNum();
						returnFee = refundSatistics.getReturnFee()==null?0:refundSatistics.getReturnFee();
					}
					Integer num = temp.getNum()==null?0:temp.getNum();
					Double price = temp.getPrice()==null?0.0:temp.getPrice();
					temp.setCanReturnNum(num - returnNum);
					temp.setCanReturnFee(price*num - returnFee);
				}
			}
			map.put("wfxOrderNo", orderOutputDto.getWfxOrderNo());
			map.put("buyerAccount", orderOutputDto.getBuyerAccount());
			map.put("shopName", orderOutputDto.getShopName());
			map.put("postFee", orderOutputDto.getPostFee());
			map.put("detailList", detailList);
			return "/manage/aftersale/applyEdit";
		}else{
			return "/manage/aftersale/applyList";
		}
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryApplyData")
	@ResponseBody
	public String queryApplyData(ModelMap map, ApplyPageVo applyVo, PageModel pageModel)throws Exception{
		applyVo.setStatus(OrderStatusEnum.TRADE_SUCCESS.getKey());
		Integer dayNum = 7;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -dayNum);
		applyVo.setConfirmTime(cal.getTime());
		OrderApplyInputDto inputDto = (OrderApplyInputDto) BeanUtil.convertBean(applyVo, OrderApplyInputDto.class);
		PageModel<OrderApplyOutputDto> result = orderBackgroundApi.queryApplyList(inputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getApplyById")
	@ResponseBody
	public String getApplyById(String id) throws Exception {
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
	@RequestMapping("/removeApply")
	@ResponseBody
	public String removeApply(@RequestParam("id") String id) {
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
	@RequestMapping("/saveApply")
	@ResponseBody
	public String saveApply(OrderInputDto order) {
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
	@RequestMapping("/insertApply")
	@ResponseBody
	public String insertApply(OrderInputDto order) {
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
	@RequestMapping("/updateApply")
	@ResponseBody
	public String updateApply(String id,int manualRefundFlag) {
		try {
			Assert.hasText(id, "参数id不能为空");
			OrderInputDto inputDto = new OrderInputDto();
			inputDto.setId(id);
			inputDto.setManualRefundFlag(manualRefundFlag);
			inputDto.setUpdateTime(new Date());
			orderBackgroundApi.update(inputDto);
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 后台进行售后申请菜单
	 */
	@RequestMapping("/afterSaleApply")
	@ResponseBody
	public String afterSaleApply(ModelMap map,OrderRefundPageVo orderRefundVo){
		try {
			OrderRefundInputDto refundDto = (OrderRefundInputDto)BeanUtil.convertBean(orderRefundVo, OrderRefundInputDto.class);
			refundDto.setIsAdminOperate(Boolean.TRUE);
			refundDto.setBuyerLoginName(getSysUserName());
			WFXResult<OrderRefundOutputDto> rs = orderRefundBackgroundApi.createRefundItem(refundDto);
			if(null == rs ){
				return super.jsonResult(StateCode.ERROR, "申请退款单失败，请联系管理员");
			}else if( rs.getResultCode()==ResultCodeEnum.FAILURE.getKey() ){
				return super.jsonResult(StateCode.ERROR,rs.getResultMsg());
			}
		} catch (Exception e) {
			logger.error("进行售后申请异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
}
