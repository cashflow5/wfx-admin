 /*
 * 版本信息
 
 * 日期 2016-03-29 14:10:40
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.finance.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.yougou.pay.api.IPayForFinanceApi;
import com.yougou.pay.vo.RefundReqVo;
import com.yougou.tools.common.utils.CalculateUtils;
import com.yougou.wfx.aftersale.api.background.IOrderRefundBackgroundApi;
import com.yougou.wfx.aftersale.api.front.IAfterSaleFrontApi;
import com.yougou.wfx.aftersale.dto.input.OrderRefundInputDto;
import com.yougou.wfx.aftersale.dto.output.OrderRefundOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.dto.base.WFXResult;
import com.yougou.wfx.enums.OrderLogBelongTypeEnum;
import com.yougou.wfx.enums.OrderLogOptBelongEnum;
import com.yougou.wfx.enums.OrderLogOptEnum;
import com.yougou.wfx.enums.OrderLogOptResultEnum;
import com.yougou.wfx.enums.OrderLogTypeEnum;
import com.yougou.wfx.enums.RefundStatusEnum;
import com.yougou.wfx.enums.RefundTypeEnum;
import com.yougou.wfx.enums.RefundWhenEnum;
import com.yougou.wfx.enums.ResultCodeEnum;
import com.yougou.wfx.finance.api.background.IFinAlreadyIncomeBackgroundApi;
import com.yougou.wfx.finance.api.background.IFinRefundBackgroundApi;
import com.yougou.wfx.finance.api.background.IFinReturnDebtBackgroundApi;
import com.yougou.wfx.finance.dto.input.FinAlreadyIncomeInputDto;
import com.yougou.wfx.finance.dto.input.FinReturnDebtInputDto;
import com.yougou.wfx.finance.dto.input.FinReturnDebtPageInputDto;
import com.yougou.wfx.finance.dto.output.FinAlreadyIncomeOutputDto;
import com.yougou.wfx.finance.dto.output.FinReturnDebtDetailOutputDto;
import com.yougou.wfx.finance.dto.output.FinReturnDebtOutputDto;
import com.yougou.wfx.finance.enums.FinRefundStatusEnum;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.finance.vo.FinReturnDebtPageVo;
import com.yougou.wfx.order.api.background.IOrderLogBackgroundApi;
import com.yougou.wfx.order.dto.input.OrderLogInputDto;
import com.yougou.wfx.order.dto.input.OrderLogPageInputDto;
import com.yougou.wfx.order.dto.output.OrderLogOutputDto;
import com.yougou.wfx.util.ExportXLSUtil;
import com.yougou.wfx.util.HttpUtil;

/**
 * 退款信息
 * FinReturnDebtController
 * @author he.xx
 * @Date 创建时间：2016-03-29 14:10:41
 */
@Controller
@RequestMapping("/finance/returndebt")
public class FinReturnDebtController extends BaseController{
	
	@Resource
	private IFinReturnDebtBackgroundApi finReturnDebtBackgroundApi;
	@Resource
	private IAfterSaleFrontApi iAfterSaleFrontApi;
	@Resource
	private IOrderLogBackgroundApi iOrderLogBackgroundApi; //订单日志：售后和财务共用同一张日志表
	@Resource
	private IFinRefundBackgroundApi finRefundBackgroundApi;
	@Resource
	private IOrderRefundBackgroundApi orderRefundBackgroundApi; //售后退款API
	@Resource
	private IFinAlreadyIncomeBackgroundApi finAlreadyIncomeBackgroundApi; //收款信息API
	@Resource
	private IPayForFinanceApi payForFinanceApi; //支付退款API
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/returnDebtList")
	public ModelAndView returnDebtList(ModelMap map, FinReturnDebtPageVo finReturnDebtVo, PageModel<FinReturnDebtDetailOutputDto> pageModel){
		if (finReturnDebtVo.getTabQueryFlag() == null) { //页面选项卡标识
			finReturnDebtVo.setTabQueryFlag(1);
		}
		if (finReturnDebtVo.getTabQueryFlag() == 1) {
			finReturnDebtVo.setRefundType(RefundTypeEnum.ONLY_REFUND.getKey());
			finReturnDebtVo.setApplyStatus(RefundStatusEnum.UNDER_REFUND.getKey());
			finReturnDebtVo.setRefundStatus(FinRefundStatusEnum.待确认退款.getCode());
		} else if (finReturnDebtVo.getTabQueryFlag() == 2) {
			finReturnDebtVo.setApplyStatus(RefundStatusEnum.UNDER_REFUND.getKey());
			finReturnDebtVo.setRefundStatus(FinRefundStatusEnum.待确认退款.getCode());
		} else if (finReturnDebtVo.getTabQueryFlag() == 3) {
			if (finReturnDebtVo.getTimeFlag() == null) {
				finReturnDebtVo.setTimeFlag(1);
			} else if (finReturnDebtVo.getTimeFlag() == 1) {
				finReturnDebtVo.setApplyStartTime(finReturnDebtVo.getStartTime());
				finReturnDebtVo.setApplyEndTime(finReturnDebtVo.getEndTime());
			} else if (finReturnDebtVo.getTimeFlag() == 2) {
				finReturnDebtVo.setRefundStartTime(finReturnDebtVo.getStartTime());
				finReturnDebtVo.setRefundEndTime(finReturnDebtVo.getEndTime());
			}
		}
		
		FinReturnDebtPageInputDto pageInputDto = (FinReturnDebtPageInputDto) BeanUtil.convertBean(finReturnDebtVo, FinReturnDebtPageInputDto.class);
		PageModel<FinReturnDebtDetailOutputDto> result = finReturnDebtBackgroundApi.findDataPage(pageInputDto, pageModel);
		for (FinReturnDebtDetailOutputDto outputDto : result.getItems()) {
			OrderRefundOutputDto afterSaleInfo = this.iAfterSaleFrontApi.getDetailByRefundNo(outputDto.getBackNo());
			if (afterSaleInfo != null) {
				outputDto.setCommodityName(afterSaleInfo.getCommodityName());
				outputDto.setCommodityNo(afterSaleInfo.getWfxCommodityNo());
				outputDto.setCommodityPicUrl(afterSaleInfo.getPicSmall());
				outputDto.setCommodityColor(afterSaleInfo.getSpecName());
				outputDto.setCommoditySize(afterSaleInfo.getSizeNo());
				outputDto.setIsAfterReceived(RefundWhenEnum.getDescByKey(afterSaleInfo.getIsAfterReceived()));
			}
		}
		map.put("pageModel", result);
		map.put("queryVo", finReturnDebtVo);
		map.put("refundTypeEnum", RefundTypeEnum.values());
		return new ModelAndView("/manage/finance/returnDebtList", map);
	}
	
	@RequestMapping("/getReturnDebtDetail")
	public ModelAndView getReturnDebtDetail(FinReturnDebtInputDto queryVo, ModelMap map) throws Exception {
		FinReturnDebtOutputDto finReturnDebt = new FinReturnDebtOutputDto();
		try {
			Assert.hasText(queryVo.getId(), "id不能为空！");
			finReturnDebt = finReturnDebtBackgroundApi.getById(queryVo.getId());
			FinAlreadyIncomeInputDto alreadyIncomeInputDto = new FinAlreadyIncomeInputDto();
			alreadyIncomeInputDto.setOrderNo(finReturnDebt.getOrderNo());
			FinAlreadyIncomeOutputDto alreadyIncome = this.finAlreadyIncomeBackgroundApi.queryAlreadyIncome(alreadyIncomeInputDto);
			//获取已退金额
			Double refundedAmount = this.finReturnDebtBackgroundApi.queryRefundedAmountByOrderNo(finReturnDebt.getOrderNo());
			//可退余额
			Double remainAmount = new Double(0);
			if (alreadyIncome == null) {
				alreadyIncome = new FinAlreadyIncomeOutputDto();
			} else if (alreadyIncome.getIncomedAmount() != null && refundedAmount != null) {
				remainAmount = CalculateUtils.sub(alreadyIncome.getIncomedAmount(), refundedAmount);
			}
			
			//调用售后接口，查询商品信息
			OrderRefundOutputDto orderRefundOutputDto = iAfterSaleFrontApi.getDetailByRefundNo(finReturnDebt.getBackNo());
			//查询订单日志，退款日志和订单日志共用同一张表
			OrderLogPageInputDto orderLogPageInputDto=new OrderLogPageInputDto();
			orderLogPageInputDto.setRejectedNo(finReturnDebt.getBackNo());
			orderLogPageInputDto.setLogType(OrderLogBelongTypeEnum.REFUND_LOG.getKey());
			List<OrderLogOutputDto>  orderLogList=iOrderLogBackgroundApi.queryList(orderLogPageInputDto);
			
			map.put("returnDebt", finReturnDebt);// 退款申请信息
			map.put("orderRefund", orderRefundOutputDto == null ? new OrderRefundOutputDto() : orderRefundOutputDto);// 退款商品信息
			map.put("orderRefundLogs", orderLogList);// 退款日志信息
			map.put("alreadyIncome", alreadyIncome);
			map.put("refundedAmount", refundedAmount);
			map.put("remainAmount", remainAmount);
			map.put("refundTypeEnum", RefundTypeEnum.values());
			map.put("queryVo", queryVo);
		} catch (Exception e) {
			logger.error("查询数据异常", e.getMessage());
		}
		return new ModelAndView("/manage/finance/returnDebtDetail", map);
	}
	
	/**
	 * 检查是否已退款
	 * @param id
	 * @return
	 */
	@RequestMapping("/checkIsRefunded")
	@ResponseBody
	public String checkIsRefunded(String id) {
		FinReturnDebtOutputDto returnDebt = null;
		String flag = "false";
		try {
			Assert.hasText(id, "id不能为空！");
			returnDebt = this.finReturnDebtBackgroundApi.getById(id);
			if (returnDebt == null) {
				throw new Exception("查询异常，无此数据.");
			} else if (returnDebt.getRefundStatus() == FinRefundStatusEnum.退款处理中.getCode() || returnDebt.getRefundStatus() == FinRefundStatusEnum.退款成功.getCode()) {
				flag = "true";
			} 
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, flag);
	}
	
	/**
	 * 同意、确认退款
	 * @param inputDto
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/agreeRefund")
	public synchronized ModelAndView agreeRefund(FinReturnDebtInputDto inputDto, ModelMap map, HttpServletRequest request) {
		map.put("redirectionUrl", "/finance/returndebt/returnDebtList.sc?tabQueryFlag="+inputDto.getTabQueryFlag());
		String message = "";
		String successFlag = "false";
		String refundCallbackStatus = "fail"; //调用退款接口返回状态
		FinReturnDebtOutputDto returnDebt = null;
		try {
			returnDebt = this.finReturnDebtBackgroundApi.getById(inputDto.getId());
			if (returnDebt == null) {
				map.put("message", "操作失败，不存在此退款单！");
				map.put("successFlag", "false");
				return new ModelAndView("/manage/finance/common_info", map);
			}
			this.logger.info(this.getSysUserName() + "，操作确认退款，退款单号[" + returnDebt.getBackNo() + "]，退款金额：" + returnDebt.getRefundAmount());
			if (returnDebt.getRefundStatus() == FinRefundStatusEnum.退款处理中.getCode() || returnDebt.getRefundStatus() == FinRefundStatusEnum.退款成功.getCode()) {
				message = "退款失败，该退款单已退款！";
				successFlag = "false";
			} else {
				String orderMainNo = returnDebt.getOrderNo();
				if (StringUtils.indexOf(orderMainNo, "_") != -1) {
					orderMainNo = returnDebt.getOrderNo().substring(0, StringUtils.indexOf(orderMainNo, "_"));
				}
				FinAlreadyIncomeInputDto alreadyIncomeInputDto = new FinAlreadyIncomeInputDto();
				alreadyIncomeInputDto.setOrderNo(orderMainNo);
				FinAlreadyIncomeOutputDto alreadyIncome = this.finAlreadyIncomeBackgroundApi.queryAlreadyIncome(alreadyIncomeInputDto);
				
				FinReturnDebtInputDto tempInputDto = new FinReturnDebtInputDto();
				tempInputDto.setOperator(this.getSysUserName());
				tempInputDto.setUpdateTime(new Date());
				tempInputDto.setId(returnDebt.getId());
				this.finReturnDebtBackgroundApi.update(tempInputDto);
				
				RefundReqVo refundReqVo = new RefundReqVo();
				refundReqVo.setBizNo(alreadyIncome.getOrderNo());
				refundReqVo.setOutTradeNo(alreadyIncome.getOutTradeNo());
				refundReqVo.setBankTradeNo(alreadyIncome.getBankTradeNo());
				refundReqVo.setTradeTime(alreadyIncome.getStringTradeDate());
				refundReqVo.setBankNo(alreadyIncome.getBankNo());
				refundReqVo.setTradeAmount(String.valueOf(alreadyIncome.getTradeAmount()));
				refundReqVo.setRefundApplyReqNo(returnDebt.getBackNo());
				refundReqVo.setRefundTradeNo(alreadyIncome.getBankTradeNo());
				refundReqVo.setRefundAmount(String.valueOf(returnDebt.getRefundAmount()));
				this.logger.info("调用支付退款API开始，传递参数："+refundReqVo+",其中订单号："+returnDebt.getOrderNo() + "，退款单号：" + returnDebt.getBackNo() + "，退款金额：" + returnDebt.getRefundAmount());
				refundCallbackStatus = this.payForFinanceApi.refund(refundReqVo);
				this.logger.info("调用支付退款API结束，返回结果状态："+ refundCallbackStatus + "，其中订单号："+returnDebt.getOrderNo() + "，退款单号：" + returnDebt.getBackNo() + "，退款金额：" + returnDebt.getRefundAmount());
				
				OrderLogInputDto logInputDto = new OrderLogInputDto();
				logInputDto.setOrderNo(returnDebt.getOrderNo());
				logInputDto.setCreateTime(new Date());
				logInputDto.setRejectedNo(returnDebt.getBackNo());
				logInputDto.setOptUser(this.getSysUserName());
				logInputDto.setOptBelong(OrderLogOptBelongEnum.OPT_BELONG_SELLER.getKey());
				logInputDto.setType(OrderLogTypeEnum.ORDER_OPT_LOG.getKey());
				logInputDto.setLogType(OrderLogBelongTypeEnum.REFUND_LOG.getKey());
				logInputDto.setShowType(2);
				
				StringBuffer strBuff = new StringBuffer();
				if (StringUtils.equals("success", refundCallbackStatus)) {
					logInputDto.setOptType(OrderLogOptEnum.OPT_REFUND_CONFIRM.getKey());
					strBuff.append(OrderLogOptEnum.OPT_REFUND_CONFIRM.getDesc());
					strBuff.append("，操作人【").append(this.getSysUserName()).append("】");
					strBuff.append("#");
					strBuff.append("退款金额：").append(String.valueOf(returnDebt.getRefundAmount())).append("元");
					strBuff.append("#");
					strBuff.append("行为描述：").append("调用支付系统成功");
					logInputDto.setLogInfo(strBuff.toString());
					logInputDto.setOptResult(OrderLogOptResultEnum.OPT_SUCCESS.getKey());
					logInputDto.setUpdateTime(new Date());
					this.iOrderLogBackgroundApi.insert(logInputDto);
					
					FinReturnDebtInputDto finReturnDebtDto = new FinReturnDebtInputDto();
					finReturnDebtDto.setRefundStatus(FinRefundStatusEnum.退款处理中.getCode());
					finReturnDebtDto.setNote(this.getSysUserName()+"#操作[确认退款]#"+FinRefundStatusEnum.退款处理中.toString());
					finReturnDebtDto.setId(returnDebt.getId());
					finReturnDebtDto.setBackNo(returnDebt.getBackNo());
					finReturnDebtDto.setUpdateTime(new Date());
					finReturnDebtDto.setOperator(this.getSysUserName());
					this.finReturnDebtBackgroundApi.update(finReturnDebtDto);
					
					message = "退款处理中";
					successFlag = "true";
				} else {
					message = "调用支付系统，退款失败！";
					successFlag = "false";
					logInputDto.setOptType(OrderLogOptEnum.OPT_REFUND_CONFIRM.getKey());
					strBuff.append(OrderLogOptEnum.OPT_REFUND_CONFIRM.getDesc());
					strBuff.append("，操作人【").append(this.getSysUserName()).append("】");
					strBuff.append("#");
					strBuff.append("退款金额：").append(String.valueOf(returnDebt.getRefundAmount())).append("元");
					strBuff.append("#");
					strBuff.append("行为描述：").append("调用支付系统失败");
					logInputDto.setLogInfo(strBuff.toString());
					logInputDto.setOptResult(OrderLogOptResultEnum.OPT_FAILED.getKey());
					logInputDto.setUpdateTime(new Date());
					this.iOrderLogBackgroundApi.insert(logInputDto);
				}
			}
			map.put("message", message);
			map.put("successFlag", successFlag);
		} catch (Exception e) {
			this.logger.error("确认退款异常...", e);
			message = "确认退款失败，系统异常，请稍后重试！";
			successFlag = "false";
			map.put("message", message);
			map.put("successFlag", successFlag);
			return new ModelAndView("/manage/finance/common_info", map);
		}
		return new ModelAndView("/manage/finance/common_info", map);
	}
	
	/**
	 * 手动退款
	 * @param inputDto
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("changeRefundStatus")
	public ModelAndView changeRefundStatus(FinReturnDebtInputDto inputDto, ModelMap map, HttpServletRequest request) {
		map.put("redirectionUrl", "/finance/returndebt/returnDebtList.sc?tabQueryFlag="+inputDto.getTabQueryFlag());
		String message = "";
		String successFlag = "false";
		FinReturnDebtOutputDto returnDebt = null;
		try {
			returnDebt = this.finReturnDebtBackgroundApi.getById(inputDto.getId());
			if (returnDebt == null) {
				message = "操作失败，不存在此退款单！";
			} else if (returnDebt.getRefundStatus() == FinRefundStatusEnum.退款处理中.getCode()) {
				Date nowDate = new Date();
				FinReturnDebtInputDto finReturnDebtDto = new FinReturnDebtInputDto();
				finReturnDebtDto.setRefundStatus(FinRefundStatusEnum.退款成功.getCode());
				finReturnDebtDto.setApplyStatus(RefundStatusEnum.SUCCESS_REFUND.getKey());
				finReturnDebtDto.setNote(returnDebt.getNote()+"#"+this.getSysUserName()+"#操作[手动退款]#"+FinRefundStatusEnum.退款成功.toString());
				finReturnDebtDto.setId(returnDebt.getId());
				finReturnDebtDto.setBackNo(returnDebt.getBackNo());
				finReturnDebtDto.setUpdateTime(nowDate);
				int num = this.finReturnDebtBackgroundApi.update(finReturnDebtDto);
				if (num > 0) {
					successFlag = "true";
					message = "操作成功，[手动退款]此退款单已退款！";
					
					OrderRefundInputDto orderRefund = new OrderRefundInputDto();
					orderRefund.setRefundNo(returnDebt.getBackNo());
					orderRefund.setStatus(RefundStatusEnum.SUCCESS_REFUND.getKey());
					orderRefund.setRefundType(returnDebt.getRefundType());
					orderRefund.setPayTime(nowDate);
					orderRefund.setSupplierName(returnDebt.getOperator());
					WFXResult<Boolean> result = this.orderRefundBackgroundApi.updateStatusOfRefund(orderRefund);
					if (result != null && ResultCodeEnum.SUCCESS.getKey() == result.getResultCode()) {
						StringBuffer strBuff = new StringBuffer();
						OrderLogInputDto logInputDto = new OrderLogInputDto();
						logInputDto.setOrderNo(returnDebt.getOrderNo());
						logInputDto.setCreateTime(new Date());
						logInputDto.setRejectedNo(returnDebt.getBackNo());
						logInputDto.setOptUser(this.getSysUserName());
						logInputDto.setOptBelong(OrderLogOptBelongEnum.OPT_BELONG_SELLER.getKey());
						logInputDto.setType(OrderLogTypeEnum.ORDER_OPT_LOG.getKey());
						logInputDto.setLogType(OrderLogBelongTypeEnum.REFUND_LOG.getKey());
						logInputDto.setShowType(2);
						logInputDto.setOptType(OrderLogOptEnum.OPT_REFUND_SUCCESS.getKey());
						strBuff.append(OrderLogOptEnum.OPT_REFUND_SUCCESS.getDesc());
						strBuff.append("，操作人【").append(returnDebt.getOperator()).append("】");
						strBuff.append("#");
						strBuff.append("退款金额，").append(String.valueOf(returnDebt.getRefundAmount())).append("元");
						logInputDto.setLogInfo(strBuff.toString());
						logInputDto.setOptResult(OrderLogOptResultEnum.OPT_SUCCESS.getKey());
						logInputDto.setUpdateTime(new Date());
						this.iOrderLogBackgroundApi.insert(logInputDto);
					} 
				}
			} else {
				message = "操作失败，此退款单不在退款处理中！";
			}
			map.put("message", message);
			map.put("successFlag", successFlag);
			return new ModelAndView("/manage/finance/common_info", map);
		} catch (Exception e) {
			message = "[手动退款]操作出现异常，退款单号："+ returnDebt.getBackNo();
			this.logger.error(message +"，错误：", e.getCause());
			map.put("message", message);
			map.put("successFlag", successFlag);
			return new ModelAndView("/manage/finance/common_info", map);
		}
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryReturnDebtData")
	@ResponseBody
	public String queryReturnDebtData(ModelMap map, FinReturnDebtPageVo finReturnDebtVo, PageModel<FinReturnDebtOutputDto> pageModel)throws Exception{
		FinReturnDebtPageInputDto pageInputDto = (FinReturnDebtPageInputDto) BeanUtil.convertBean(finReturnDebtVo, FinReturnDebtPageInputDto.class);
		PageModel<FinReturnDebtOutputDto> result = finReturnDebtBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	@RequestMapping("/getReturnDebtById")
	@ResponseBody
	public String getReturnDebtById(String id) throws Exception {
		FinReturnDebtOutputDto finReturnDebt = new FinReturnDebtOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			finReturnDebt = finReturnDebtBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, finReturnDebt);
	}
	
	/**
	 * 导出报表
	 * @param finReturnDebt
	 * @param request
	 * @param response
	 */
	@RequestMapping("/exportExcel")
	public void exportExcel(FinReturnDebtInputDto finReturnDebt, HttpServletRequest request, HttpServletResponse response) {
		List<FinReturnDebtOutputDto> returnDebtList = this.finReturnDebtBackgroundApi.queryDataList(finReturnDebt);
		List<Object[]> dataList = new ArrayList<Object[]>();
		String title = "财务退款报表详情";
		String[] headers = {"退款单号","订单号", "申请退款时间", "分销商编码", "客户名称","客户账号", "退款类型", "退款金额", "退款原因", "退款说明", "退款时间", "退款状态","操作员"};
		Boolean[] amounts = {false, false, false, false, false, false, false, true, false, false, false,false,false};
		if (returnDebtList != null && returnDebtList.size() > 0) {
			for (FinReturnDebtOutputDto returnDebt : returnDebtList) {
				Object[] obj = new Object[13];
				obj[0] = returnDebt.getBackNo();
				obj[1] = returnDebt.getOrderNo();
				obj[2] = returnDebt.getApplyDate();
				obj[3] = returnDebt.getStoreName();
				obj[4] = returnDebt.getCustomerName();
				obj[5] = returnDebt.getCustomerNo();
				obj[6] = RefundTypeEnum.getDescByKey(returnDebt.getRefundType());
				obj[7] = returnDebt.getRefundAmount();
				obj[8] = returnDebt.getRefundNote();
				obj[9] = returnDebt.getRefundDesc();
				obj[10] = returnDebt.getRefundDate();
				obj[11] = FinRefundStatusEnum.getName(returnDebt.getRefundStatus());
				obj[12] = returnDebt.getOperator();		
				dataList.add(obj);
			}
		}
		HSSFWorkbook workbook = ExportXLSUtil.exportExcel("FinanceData", headers, dataList, amounts);
		this.logger.info("导出退款列表详情，导出记录数："+this.finReturnDebtBackgroundApi.queryDataListCount(finReturnDebt));
		if (workbook == null) {
			return ;
		}
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.setContentType("application/vnd.ms-excel;charset=UTF-8"); 
			if (StringUtils.isNotBlank(title)) {
				if (1 == HttpUtil.getBrowingType(request)) {
					title = URLEncoder.encode(title, "UTF-8");
				} else {
					title = new String(title.getBytes("UTF-8"), "ISO-8859-1");
				}
			}
			response.setHeader("Content-disposition","attachment; filename="+title+".xls");  
			workbook.write(outputStream);
			response.flushBuffer();
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (IOException e) {
			this.logger.error("导出收款报表异常...", e);
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
					outputStream = null;
				}
				if (workbook != null) {
					workbook = null;
				}
				if (dataList != null) {
					dataList = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
