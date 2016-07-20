 /*
 * 版本信息
 
 * 日期 2016-03-29 14:08:45
 
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.enums.BankCompanyEnum;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.finance.vo.FinSellerInfoDetailPageVo;
import com.yougou.wfx.util.ExportXLSUtil;
import com.yougou.wfx.util.HttpUtil;
import com.yougou.wfx.finance.api.background.IFinSellerInfoDetailBackgroundApi;
import com.yougou.wfx.finance.dto.input.FinSellerInfoDetailInputDto;
import com.yougou.wfx.finance.dto.input.FinSellerInfoDetailPageInputDto;
import com.yougou.wfx.finance.dto.output.FinSellerInfoDetailOutputDto;
import com.yougou.wfx.finance.enums.FinSellerAccountTranStatusEnum;
import com.yougou.wfx.finance.enums.FinSellerAccountTranTypeEnum;

/**
 * FinSellerInfoDetailController
 * @author wfx
 * @Date 创建时间：2016-03-29 14:08:45
 */
@Controller
@RequestMapping("/finance/sellerdetail")
public class FinSellerInfoDetailController extends BaseController{
	
	@Resource
	private IFinSellerInfoDetailBackgroundApi finSellerInfoDetailBackgroundApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/sellerList")
	public String sellerList(){
		return "/manage/finance/sellerDetailList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/querySellerData")
	@ResponseBody
	public String querySellerData(ModelMap map, FinSellerInfoDetailPageVo finSellerInfoDetailVo, PageModel pageModel)throws Exception{
		FinSellerInfoDetailPageInputDto pageInputDto = (FinSellerInfoDetailPageInputDto) BeanUtil.convertBean(finSellerInfoDetailVo, FinSellerInfoDetailPageInputDto.class);
		PageModel<FinSellerInfoDetailOutputDto> result = finSellerInfoDetailBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getSellerById")
	@ResponseBody
	public String getSellerById(String id) throws Exception {
		FinSellerInfoDetailOutputDto finSellerInfoDetail = new FinSellerInfoDetailOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			finSellerInfoDetail = finSellerInfoDetailBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, finSellerInfoDetail);
	}
	
	/**
	 * 通过主键id删除数据
	 */
	@RequestMapping("/removeSeller")
	@ResponseBody
	public String removeSeller(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			finSellerInfoDetailBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 保存或修改数据
	 */
	@RequestMapping("/saveSeller")
	@ResponseBody
	public String saveSeller(FinSellerInfoDetailInputDto finSellerInfoDetail) {
		try {
			String id = finSellerInfoDetail.getId();
			if (StringUtils.isNotBlank(id)) {
				finSellerInfoDetailBackgroundApi.update(finSellerInfoDetail);
			}else{
				finSellerInfoDetailBackgroundApi.insert(finSellerInfoDetail);
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
	@RequestMapping("/insertSeller")
	@ResponseBody
	public String insertSeller(FinSellerInfoDetailInputDto finSellerInfoDetail) {
		try {
			finSellerInfoDetailBackgroundApi.insert(finSellerInfoDetail);
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/updateSeller")
	@ResponseBody
	public String updateSeller(FinSellerInfoDetailInputDto finSellerInfoDetail) {
		try {
			Assert.hasText(finSellerInfoDetail.getId(), "参数id不能为空");
			finSellerInfoDetailBackgroundApi.update(finSellerInfoDetail);
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 导出分销商账户交易明细
	 */
	@RequestMapping("/exportDetailExcel")
	public void exportDetailExcel(FinSellerInfoDetailInputDto finSellerInfoDetail, HttpServletRequest request, HttpServletResponse response) {
		List<FinSellerInfoDetailOutputDto> sellerDetailList = null;
		List<Object[]> dataList = new ArrayList<Object[]>();
		String title = "分销商账户交易明细报表";
		String[] headers = {"交易流水号", "交易时间", "交易类型", "收入金额", "支出金额", "账户余额", "支付方式", "交易订单号", "状态", "操作人", "备注"};
		Boolean[] amounts = {false, false, false, true, true, true, false, false, false, false, false};
		OutputStream outputStream = null;
		HSSFWorkbook workbook = null;
		try {
			sellerDetailList = this.finSellerInfoDetailBackgroundApi.querySellerDetailList(finSellerInfoDetail);
			if (sellerDetailList != null && sellerDetailList.size() > 0) {
				for (FinSellerInfoDetailOutputDto sellerDetail : sellerDetailList) {
					Object[] obj = new Object[11];
					obj[0] = sellerDetail.getTransactionNumber();
					obj[1] = sellerDetail.getTransactionTime();
					obj[2] = FinSellerAccountTranTypeEnum.getName(sellerDetail.getTransactionType());
					obj[3] = sellerDetail.getIncomeAmount();
					obj[4] = sellerDetail.getExpendAmount();
					obj[5] = sellerDetail.getAccountBalance();
					if (StringUtils.isNotBlank(sellerDetail.getPaymentStyle())) {
						obj[6] = BankCompanyEnum.getDescByKey(Integer.valueOf(sellerDetail.getPaymentStyle()));
					} else {
						obj[6] = "";
					}
					obj[7] = sellerDetail.getTransactionOrderNum();
					obj[8] = FinSellerAccountTranStatusEnum.getName(sellerDetail.getBillState());
					obj[9] = sellerDetail.getOperater();
					obj[10] = sellerDetail.getOperateNote();
					dataList.add(obj);
				}
			}
			workbook = ExportXLSUtil.exportExcel("FinanceData", headers, dataList, amounts);
			this.logger.info("导出分销商收支明细报表，导出总记录数："+ (sellerDetailList == null ? 0 : sellerDetailList.size()));
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
			this.logger.error("导出分销商账户交易明细出错...", e);
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
