 /*
 * 版本信息
 
 * 日期 2016-03-31 10:10:25
 
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

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.finance.api.background.IFinSellerAccountWithdrawBackgroundApi;
import com.yougou.wfx.finance.dto.input.FinSellerAccountWithdrawInputDto;
import com.yougou.wfx.finance.dto.input.FinSellerAccountWithdrawPageInputDto;
import com.yougou.wfx.finance.dto.output.FinSellerAccountWithdrawOutputDto;
import com.yougou.wfx.finance.dto.output.MessageOutputDto;
import com.yougou.wfx.finance.enums.FinSellerAccountWithdrawalStatusEnum;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.finance.vo.FinSellerAccountWithdrawPageVo;
import com.yougou.wfx.util.ExportXLSUtil;
import com.yougou.wfx.util.HttpUtil;

/**
 * FinSellerAccountWithdrawController
 * @author wfx
 * @Date 创建时间：2016-03-31 10:10:25
 */
@Controller
@RequestMapping("/finance/accountwithdraw")
public class FinSellerAccountWithdrawController extends BaseController{
	
	@Resource
	private IFinSellerAccountWithdrawBackgroundApi finSellerAccountWithdrawBackgroundApi;
	
	/**
	 * 进入提现审核菜单
	 */
	@RequestMapping("/sellerList")
	public String sellerList(ModelMap modelMap, String billStatus) {
		if (StringUtils.isBlank(billStatus)) { //默认待审核，即未审核状态
			billStatus = FinSellerAccountWithdrawalStatusEnum.ACCOUNT_WITHDRAWAL_UNAUDIT.getCode();
		}
		modelMap.addAttribute("billStatus", billStatus);	
		return "/manage/finance/sellerAccountWithdrawList";
	}
	
	/** 
	 * 进入提现确认付款菜单
	 */
	@RequestMapping("/withdrawalConfirmList")
	public String withdrawalConfirmList(String billStatus, String queryFlag, ModelMap modelMap) {
		if (StringUtils.isBlank(billStatus)) { //默认待确认列表，即已审核状态
			billStatus = FinSellerAccountWithdrawalStatusEnum.ACCOUNT_WITHDRAWAL_AUDITED.getCode();
		}
		if (StringUtils.isBlank(queryFlag)) {
			queryFlag = "only";
		}
		modelMap.addAttribute("billStatus", billStatus);
		modelMap.addAttribute("queryFlag", queryFlag);
		return "/manage/finance/sellerAccountWithdrawConfirmList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/querySellerData")
	@ResponseBody
	public String querySellerData(ModelMap map, FinSellerAccountWithdrawPageVo finSellerAccountWithdrawVo, PageModel pageModel)throws Exception{
		FinSellerAccountWithdrawPageInputDto pageInputDto = (FinSellerAccountWithdrawPageInputDto) BeanUtil.convertBean(finSellerAccountWithdrawVo, FinSellerAccountWithdrawPageInputDto.class);
		PageModel<FinSellerAccountWithdrawOutputDto> result = finSellerAccountWithdrawBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getById")
	@ResponseBody
	public String getSellerById(String id) throws Exception {
		FinSellerAccountWithdrawOutputDto finSellerAccountWithdraw = new FinSellerAccountWithdrawOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			finSellerAccountWithdraw = finSellerAccountWithdrawBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, finSellerAccountWithdraw);
	}
		
	/**
	 * 查询提现详情
	 * @param id
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryDetailById")
	public String queryDetailById(String id, ModelMap map) throws Exception {
		FinSellerAccountWithdrawOutputDto finSellerAccountWithdraw = new FinSellerAccountWithdrawOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			finSellerAccountWithdraw = finSellerAccountWithdrawBackgroundApi.getById(id);
			map.put("widthDrawVo", finSellerAccountWithdraw);
		} catch (Exception e) {
			this.logger.error("获取提现详情出错，错误：", e.getMessage());
		}
		return "/manage/finance/sellerAccountWithdrawDetail";
	}
	
	/**
	 * 提现申请单，付款拒绝，废弃
	 */
	@Deprecated
	@RequestMapping("/refusedModifyApplyBill")
	@ResponseBody
	public MessageOutputDto refusedModifyApplyBill(FinSellerAccountWithdrawInputDto finSellerAccountWithdraw) {
		MessageOutputDto dto = new MessageOutputDto();
		String id = finSellerAccountWithdraw.getId();
		if (StringUtils.isNotBlank(id)) {
		  //将当前的提现申请单单据状态变更为审核拒绝
		  finSellerAccountWithdraw.setBillStatus(FinSellerAccountWithdrawalStatusEnum.ACCOUNT_WITHDRAWAL_REFUSED.getCode());
		  finSellerAccountWithdraw.setModifier(this.getSysUserName());
		  finSellerAccountWithdraw.setModifyTime(new Date());
		  try{
			 dto=finSellerAccountWithdrawBackgroundApi.refusedModifyApplyBill(finSellerAccountWithdraw);
		  }catch(Exception error){
			 dto.setCode("500");
			 dto.setMessage("系统调用接口,数据提交失败,请重试");
			 logger.info("系统调用付款审核拒绝接口出错！"+error.getMessage());
		  } 
		 }
		return dto;
	}
	
	/**
	 * 提现申请单，确认付款
	 */
	@RequestMapping("/modifyApplyBill")
	@ResponseBody
	public MessageOutputDto modifyApplyBill(FinSellerAccountWithdrawInputDto finSellerAccountWithdraw) {
		FinSellerAccountWithdrawOutputDto outputWidthdrawDto = null;
		MessageOutputDto dto = new MessageOutputDto();
		String remarkStr = "";
		try {
			Assert.hasText(finSellerAccountWithdraw.getId(), "参数id不能为空");
			outputWidthdrawDto = this.finSellerAccountWithdrawBackgroundApi.getById(finSellerAccountWithdraw.getId());
			if (outputWidthdrawDto == null) {
				dto.setCode("500");
			    dto.setMessage("操作失败，不存在此提现单！");
			    return dto;
			}
			//将当前的提现申请单单据状态变更为已提现
			finSellerAccountWithdraw.setBillStatus(FinSellerAccountWithdrawalStatusEnum.ACCOUNT_WITHDRAWAL_SUCCESS.getCode());
			finSellerAccountWithdraw.setModifier(this.getSysUserName());
			finSellerAccountWithdraw.setModifyTime(new Date());
			if (StringUtils.isNotBlank(outputWidthdrawDto.getRemark()) && StringUtils.isNotBlank(finSellerAccountWithdraw.getRemark())) {
				remarkStr = outputWidthdrawDto.getRemark() + ";" + this.getSysUserName() + "," + finSellerAccountWithdraw.getRemark();
			} else if (StringUtils.isNotBlank(finSellerAccountWithdraw.getRemark())) {
				remarkStr = this.getSysUserName() + "," + finSellerAccountWithdraw.getRemark();
			}
			finSellerAccountWithdraw.setRemark(remarkStr);
			dto = finSellerAccountWithdrawBackgroundApi.modifyApplyBill(finSellerAccountWithdraw);
		} catch (Exception e) {
			dto.setCode("500");
		    dto.setMessage("系统调用接口,数据提交失败,请重试");
		    logger.info("系统调用付款审核提现申请单接口出错！"+e.getMessage());
		}
		return dto;  
	}
	
	/**
	 * 提现申请单，审核通过
	 */
	@RequestMapping("/auditApplyBill")
	@ResponseBody
	public MessageOutputDto auditApplyBill(FinSellerAccountWithdrawInputDto finSellerAccountWithdraw) {
	    MessageOutputDto dto = new MessageOutputDto();
	    FinSellerAccountWithdrawOutputDto outputWidthdrawDto = null;
	    try {
	    	Assert.hasText(finSellerAccountWithdraw.getId(), "参数id不能为空");
	    	outputWidthdrawDto = this.finSellerAccountWithdrawBackgroundApi.getById(finSellerAccountWithdraw.getId());
	    	if (outputWidthdrawDto == null) {
	    		dto.setCode("500");
			    dto.setMessage("操作失败，不存在此提现单！");
	    		return dto;
	    	}
	    	//将当前的提现申请单单据状态变更为已审核
			finSellerAccountWithdraw.setBillStatus(FinSellerAccountWithdrawalStatusEnum.ACCOUNT_WITHDRAWAL_AUDITED.getCode());
			finSellerAccountWithdraw.setOperater(this.getSysUserName());
			finSellerAccountWithdraw.setOperaterTime(new Date());
			if (StringUtils.isNotBlank(finSellerAccountWithdraw.getRemark())) {
				finSellerAccountWithdraw.setRemark(this.getSysUserName()+","+finSellerAccountWithdraw.getRemark());
			}
			dto = finSellerAccountWithdrawBackgroundApi.auditApplyBill(finSellerAccountWithdraw);
	    } catch (Exception e) {
	    	dto.setCode("500");
			dto.setMessage("系统调用接口,数据提交失败,请重试");
			logger.info("系统调用审核提现申请单接口出错！"+e.getMessage());
	    }
		return dto;
	}
	
	/**
	 * 提现申请单，审核拒绝
	 * @param finSellerAccountWithdraw
	 * @return
	 */
	@RequestMapping("/auditRefuseApplyBill")
	@ResponseBody
	public MessageOutputDto auditRefuseApplyBill(FinSellerAccountWithdrawInputDto finSellerAccountWithdraw){
		MessageOutputDto messageOutPut = new MessageOutputDto();
		FinSellerAccountWithdrawOutputDto outputWidthdrawDto = null;
		try {
			Assert.hasText(finSellerAccountWithdraw.getId(), "参数id不能为空");
			outputWidthdrawDto = this.finSellerAccountWithdrawBackgroundApi.getById(finSellerAccountWithdraw.getId());
			if (outputWidthdrawDto != null && StringUtils.isNotBlank(outputWidthdrawDto.getId())) {
				//将当前的提现申请单单据状态变更为审核拒绝
				finSellerAccountWithdraw.setBillStatus(FinSellerAccountWithdrawalStatusEnum.ACCOUNT_WITHDRAWAL_REFUSED.getCode());
				finSellerAccountWithdraw.setOperater(this.getSysUserName());
				finSellerAccountWithdraw.setOperaterTime(new Date());
				messageOutPut = this.finSellerAccountWithdrawBackgroundApi.auditRefuseApplyBill(finSellerAccountWithdraw);
			} else {
				messageOutPut.setCode("500");
				messageOutPut.setMessage("不存在此提现申请单，请核实！");
			}
		} catch (Exception e) {
			messageOutPut.setCode("500");
			messageOutPut.setMessage("");
			this.logger.error("提现申请单["+ outputWidthdrawDto.getWithdrawApplyNo() +"]审核拒绝异常，错误：", e);
		}
		return messageOutPut;
	}
	
	/** 确认提现申请单 */
	@RequestMapping("/auditConfirmApplyBill")
	@ResponseBody
	public MessageOutputDto auditConfirmApplyBill(FinSellerAccountWithdrawInputDto withdrawInputDto) {
		MessageOutputDto messageDto = new MessageOutputDto();
		FinSellerAccountWithdrawOutputDto widthdrawOutputDto = null;
		try {
			Assert.hasText(withdrawInputDto.getId(), "参数id不能为空");
			widthdrawOutputDto = this.finSellerAccountWithdrawBackgroundApi.getById(withdrawInputDto.getId());
			if (widthdrawOutputDto == null) {
				messageDto.setCode("500");
				messageDto.setMessage("操作失败，不存在此提现单！");
				return messageDto;
			}
			//确认提现申请单，状态由已审核变成已确认
			withdrawInputDto.setBillStatus(FinSellerAccountWithdrawalStatusEnum.ACCOUNT_WITHDRAWAL_CONFIRM.getCode());
			StringBuffer strBuffer = new StringBuffer();
			if (StringUtils.isNotBlank(widthdrawOutputDto.getRemark()) && StringUtils.isNotBlank(withdrawInputDto.getRemark())) {
				strBuffer.append(widthdrawOutputDto.getRemark());
				strBuffer.append(";");
				strBuffer.append(this.getSysUserName()+","+withdrawInputDto.getRemark());
			} else if (StringUtils.isNotBlank(withdrawInputDto.getRemark())) {
				strBuffer.append(this.getSysUserName()+","+withdrawInputDto.getRemark());
			}
			withdrawInputDto.setRemark(strBuffer.toString());
			messageDto = finSellerAccountWithdrawBackgroundApi.auditConfirmApplyBill(withdrawInputDto);
		} catch (Exception ex) {
			messageDto.setCode("500");
			messageDto.setMessage("系统调用接口,数据提交失败,请重试");
			this.logger.error("确认提现申请单"+widthdrawOutputDto.getWithdrawApplyNo()+"失败，错误：", ex.getCause());
		}
		return messageDto;
	}
	
	/**
	 * 导出提现待审核列表
	 */
	@RequestMapping("/exportDetailExcel")
	public void exportDetailExcel(FinSellerAccountWithdrawInputDto finSellerAccountWithdraw, HttpServletRequest request, HttpServletResponse response) {
		List<Object[]> dataList = new ArrayList<Object[]>();
		String title = "提现待审核列表";
		String[] headers = {"提现申请单号", "申请时间", "店铺编码", "分销商账号", "提现金额", "支付方式", "开户名"};
		Boolean[] amounts = {false, false, false, false, true, false, false};
		OutputStream outputStream = null;
		HSSFWorkbook workbook = null;
		List<FinSellerAccountWithdrawOutputDto> accountWithdrawalList = null;
		try {
			accountWithdrawalList = this.finSellerAccountWithdrawBackgroundApi.queryWithdrawalList(finSellerAccountWithdraw);
			if (accountWithdrawalList != null && accountWithdrawalList.size() > 0) {
				for (FinSellerAccountWithdrawOutputDto dto : accountWithdrawalList) {
					Object[] obj = new Object[7];
					obj[0] = dto.getWithdrawApplyNo();
					obj[1] = dto.getApplyTime();
					obj[2] = dto.getShopName();
					obj[3] = dto.getSellerAccount();
					obj[4] = dto.getWithdrawAmount();
					obj[5] = dto.getAccountBankName();
					obj[6] = dto.getAccountName();
					dataList.add(obj);
				}
			}
			workbook = ExportXLSUtil.exportExcel("FinanceData", headers, dataList, amounts);
			this.logger.info("导出提现待审核列表报表，导出总记录数："+ (accountWithdrawalList == null ? 0 : accountWithdrawalList.size()));
			if (workbook == null) {
				return ;
			}
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
		} catch (Exception e) {
			e.printStackTrace();
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
	
	/**
	 * 导出提现待确认或待付款列表
	 */
	@RequestMapping("/exportConfirmDetailExcel")
	public void exportConfirmDetailExcel(FinSellerAccountWithdrawInputDto finSellerAccountWithdraw, HttpServletRequest request, HttpServletResponse response) {
		List<Object[]> dataList = new ArrayList<Object[]>();
		String title = "提现全部列表";
		if (StringUtils.equals(FinSellerAccountWithdrawalStatusEnum.ACCOUNT_WITHDRAWAL_AUDITED.getCode(), finSellerAccountWithdraw.getBillStatus())) {
			title = "提现待确认列表";
		} else if (StringUtils.equals(FinSellerAccountWithdrawalStatusEnum.ACCOUNT_WITHDRAWAL_CONFIRM.getCode(), finSellerAccountWithdraw.getBillStatus())) {
			title = "提现待付款列表";
		}
		String[] headers = {"提现申请单号", "申请时间", "店铺编码", "分销商账号", "账户余额", "提现金额", "支付方式", "开户名", "对方开户行", "账号", "申请原因说明", "申请人", "审核人", "审核时间", "付款人","提现时间", "单据状态", "备注说明"};
		Boolean[] amounts = {false, false, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false};
		OutputStream outputStream = null;
		HSSFWorkbook workbook = null;
		List<FinSellerAccountWithdrawOutputDto> accountWithdrawalList = null;
		try {
			accountWithdrawalList = this.finSellerAccountWithdrawBackgroundApi.queryWithdrawalList(finSellerAccountWithdraw);
			if (accountWithdrawalList != null && accountWithdrawalList.size() > 0) {
				for (FinSellerAccountWithdrawOutputDto dto : accountWithdrawalList) {
					Object[] obj = new Object[18];
					obj[0] = dto.getWithdrawApplyNo();
					obj[1] = dto.getApplyTime();
					obj[2] = dto.getShopName();
					obj[3] = dto.getSellerAccount();
					obj[4] = dto.getAccountBalance();
					obj[5] = dto.getWithdrawAmount();
					obj[6] = dto.getAccountBankName();
					obj[7] = dto.getAccountName();
					obj[8] = dto.getAccountBankAllName();
					obj[9] = dto.getAccountNo();
					obj[10] = dto.getApplyReason();
					obj[11] = dto.getApplyer();
					obj[12] = dto.getOperater();
					obj[13] = dto.getOperaterTime();
					obj[14] = dto.getModifier();
					obj[15] = dto.getModifyTime();
					obj[16] = FinSellerAccountWithdrawalStatusEnum.getName(dto.getBillStatus());
					obj[17] = dto.getRemark();
					dataList.add(obj);
				}
			}
			workbook = ExportXLSUtil.exportExcel("FinanceData", headers, dataList, amounts);
			this.logger.info("导出"+title+"，导出总记录数："+ (accountWithdrawalList == null ? 0 : accountWithdrawalList.size()));
			if (workbook == null) {
				return ;
			}
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
		} catch (Exception e) {
			e.printStackTrace();
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
