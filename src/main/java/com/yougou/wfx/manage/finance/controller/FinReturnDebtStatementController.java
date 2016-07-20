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
import com.yougou.wfx.enums.RefundTypeEnum;
import com.yougou.wfx.finance.api.background.IFinReturnDebtBackgroundApi;
import com.yougou.wfx.finance.dto.input.FinReturnDebtInputDto;
import com.yougou.wfx.finance.dto.input.FinReturnDebtPageInputDto;
import com.yougou.wfx.finance.dto.output.FinReturnDebtOutputDto;
import com.yougou.wfx.finance.enums.FinRefundStatusEnum;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.finance.vo.FinReturnDebtPageVo;
import com.yougou.wfx.util.ExportXLSUtil;
import com.yougou.wfx.util.HttpUtil;

/**
 * 退款报表
 * FinReturnDebtStatementController
 * @author he.xx
 * @Date 创建时间：2016-03-29 14:10:41
 */
@Controller
@RequestMapping("/finance/returndebt/statement")
public class FinReturnDebtStatementController extends BaseController{
	
	@Resource
	private IFinReturnDebtBackgroundApi finReturnDebtBackgroundApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/returnDebtStatementList")
	public String returnDebtList(){
		return "/manage/finance/returnDebtStatementList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryReturnDebtStatementData")
	@ResponseBody
	public String queryReturnDebtData(ModelMap map, FinReturnDebtPageVo finReturnDebtVo, PageModel<FinReturnDebtOutputDto> pageModel)throws Exception{
		finReturnDebtVo.setRefundStatus(FinRefundStatusEnum.退款成功.getCode());
		FinReturnDebtPageInputDto pageInputDto = (FinReturnDebtPageInputDto) BeanUtil.convertBean(finReturnDebtVo, FinReturnDebtPageInputDto.class);
		PageModel<FinReturnDebtOutputDto> result = finReturnDebtBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	@RequestMapping("/exportExcel")
	public void exportExcel(FinReturnDebtInputDto finReturnDebt, HttpServletRequest request, HttpServletResponse response) {
		finReturnDebt.setRefundStatus(FinRefundStatusEnum.退款成功.getCode());
		List<FinReturnDebtOutputDto> returnDebtList = this.finReturnDebtBackgroundApi.queryDataList(finReturnDebt);
		List<Object[]> dataList = new ArrayList<Object[]>();
		String title = "财务退款报表详情";
		String[] headers = {"退款单号","订单号", "申请退款时间", "分销商编码", "买家账号", "退款类型", "退款金额", "退款原因", "退款说明", "退款时间", "退款状态","操作员"};
		Boolean[] amounts = {false, false, false, false, false, false, true, false, false, false,false,false};
		if (returnDebtList != null && returnDebtList.size() > 0) {
			for (FinReturnDebtOutputDto returnDebt : returnDebtList) {
				Object[] obj = new Object[12];
				obj[0] = returnDebt.getBackNo();
				obj[1] = returnDebt.getOrderNo();
				obj[2] = returnDebt.getApplyDate();
				obj[3] = returnDebt.getStoreName();
				obj[4] = returnDebt.getCustomerNo();
				obj[5] = RefundTypeEnum.getDescByKey(returnDebt.getRefundType());
				obj[6] = returnDebt.getRefundAmount();
				obj[7] = returnDebt.getRefundNote();
				obj[8] = returnDebt.getRefundDesc();
				obj[9] = returnDebt.getRefundDate();
				obj[10] = FinRefundStatusEnum.getName(returnDebt.getRefundStatus());
				obj[11] = returnDebt.getOperator();		
				dataList.add(obj);
			}
		}
		HSSFWorkbook workbook = ExportXLSUtil.exportExcel("FinanceData", headers, dataList, amounts);
		this.logger.info("导出退款报表详情，导出记录数："+this.finReturnDebtBackgroundApi.queryDataListCount(finReturnDebt));
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
