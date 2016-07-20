 /*
 * 版本信息
 
 * 日期 2016-03-28 17:15:56
 
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.finance.api.background.IFinAlreadyIncomeBackgroundApi;
import com.yougou.wfx.finance.dto.input.FinAlreadyIncomeInputDto;
import com.yougou.wfx.finance.dto.input.FinAlreadyIncomePageInputDto;
import com.yougou.wfx.finance.dto.output.FinAlreadyIncomeOutputDto;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.commodity.controller.BagController;
import com.yougou.wfx.manage.finance.vo.FinAlreadyIncomePageVo;
import com.yougou.wfx.util.ExportXLSUtil;
import com.yougou.wfx.util.HttpUtil;

/**
 * 收款报表
 * FinAlreadyIncomeStatementController
 * @author he.xx
 * @Date 创建时间：2016-03-28 17:15:58
 */
@Controller
@RequestMapping("/finance/income/statement")
public class FinAlreadyIncomeStatementController extends BagController {
	
	@Resource
	private IFinAlreadyIncomeBackgroundApi finAlreadyIncomeBackgroundApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/alreadyIncomeStatementList")
	public String alreadyIncomeList(){
		return "/manage/finance/alreadyIncomeStatementList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryAlreadyIncomeStatementData")
	@ResponseBody
	public String queryAlreadyIncomeData(ModelMap map, FinAlreadyIncomePageVo finAlreadyIncomeVo, PageModel<FinAlreadyIncomeOutputDto> pageModel)throws Exception{
		FinAlreadyIncomePageInputDto pageInputDto = (FinAlreadyIncomePageInputDto) BeanUtil.convertBean(finAlreadyIncomeVo, FinAlreadyIncomePageInputDto.class);
		PageModel<FinAlreadyIncomeOutputDto> result = finAlreadyIncomeBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	@RequestMapping("/exportExcel")
	public void exportExcel(FinAlreadyIncomeInputDto finAlreadyIncome, HttpServletRequest request, HttpServletResponse response) {
		List<FinAlreadyIncomeOutputDto> alreadyIncomeList = this.finAlreadyIncomeBackgroundApi.queryDataList(finAlreadyIncome);
		List<Object[]> dataList = new ArrayList<Object[]>();
		String title = "财务收款报表详情";
		String[] headers = {"订单号", "收款时间", "下单时间", "订单金额", "分销商编码", "买家账号", "支付号", "支付交易金额", "银行支付交易流水号", "支付方式名称", "收款账号", "收货款金额", "收运费金额", "收款合计", "收款描述", "操作员"};
		Boolean[] amounts = {false, false, false, true, false, false, false, true, false, false, false, true, true, true, false, false};
		if (alreadyIncomeList != null && alreadyIncomeList.size() > 0) {
			for (FinAlreadyIncomeOutputDto alreadyIncome : alreadyIncomeList) {
				Object[] obj = new Object[16];
				obj[0] = alreadyIncome.getOrderNo();
				obj[1] = alreadyIncome.getTradeDate();
				obj[2] = alreadyIncome.getOrderDate();
				obj[3] = alreadyIncome.getOrderAmount();
				obj[4] = alreadyIncome.getStoreName();
				obj[5] = alreadyIncome.getCustomerNo();
				obj[6] = alreadyIncome.getOutTradeNo();
				obj[7] = alreadyIncome.getTradeAmount();
				obj[8] = alreadyIncome.getBankTradeNo();
				obj[9] = alreadyIncome.getBankName();
				obj[10] = alreadyIncome.getIncomeAccount();
				obj[11] = alreadyIncome.getProductAmount();
				obj[12] = alreadyIncome.getFareAmount();
				obj[13] = alreadyIncome.getIncomedAmount();
				obj[14] = alreadyIncome.getIncomeTypeDesc();
				obj[15] = alreadyIncome.getOperator();
				dataList.add(obj);
			}
		}
		HSSFWorkbook workbook = ExportXLSUtil.exportExcel("FinanceData", headers, dataList, amounts);
		this.logger.info("导出收款报表详情，导出总记录数："+this.finAlreadyIncomeBackgroundApi.queryDataListCount(finAlreadyIncome));
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
