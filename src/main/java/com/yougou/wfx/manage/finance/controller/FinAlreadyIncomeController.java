 /*
 * 版本信息
 
 * 日期 2016-03-28 17:15:56
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.finance.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.finance.api.background.IFinAlreadyIncomeBackgroundApi;
import com.yougou.wfx.finance.dto.input.FinAlreadyIncomeInputDto;
import com.yougou.wfx.finance.dto.input.FinAlreadyIncomePageInputDto;
import com.yougou.wfx.finance.dto.output.FinAlreadyIncomeOutputDto;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.commodity.controller.BagController;
import com.yougou.wfx.manage.finance.vo.FinAlreadyIncomePageVo;

/**
 * 收款信息
 * FinAlreadyIncomeController
 * @author he.xx
 * @Date 创建时间：2016-03-28 17:15:58
 */
@Controller
@RequestMapping("/finance/income")
public class FinAlreadyIncomeController extends BagController {
	
	@Resource
	private IFinAlreadyIncomeBackgroundApi finAlreadyIncomeBackgroundApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/alreadyIncomeList")
	public String alreadyIncomeList(){
		return "/manage/finance/alreadyIncomeList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryAlreadyIncomeData")
	@ResponseBody
	public String queryAlreadyIncomeData(ModelMap map, FinAlreadyIncomePageVo finAlreadyIncomeVo, PageModel<FinAlreadyIncomeOutputDto> pageModel)throws Exception{
		FinAlreadyIncomePageInputDto pageInputDto = (FinAlreadyIncomePageInputDto) BeanUtil.convertBean(finAlreadyIncomeVo, FinAlreadyIncomePageInputDto.class);
		PageModel<FinAlreadyIncomeOutputDto> result = finAlreadyIncomeBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	@RequestMapping("/getAlreadyIncomeById")
	@ResponseBody
	public String getAlreadyIncomeById(String id) throws Exception {
		FinAlreadyIncomeOutputDto finAlreadyIncome = new FinAlreadyIncomeOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			finAlreadyIncome = finAlreadyIncomeBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, finAlreadyIncome);
	}
	
	@RequestMapping("/saveAlreadyIncome")
	@ResponseBody
	public String saveAlreadyIncome(FinAlreadyIncomeInputDto finAlreadyIncome) {
		try {
			String id = finAlreadyIncome.getId();
			if (StringUtils.isNotBlank(id)) {
				finAlreadyIncomeBackgroundApi.update(finAlreadyIncome);
			}else{
				for (int i = 20; i < 21; i ++) {
					finAlreadyIncome = new FinAlreadyIncomeInputDto();
					finAlreadyIncome.setCreateDate(new Date());
					finAlreadyIncome.setCustomerName("顾客名称"+i);
					finAlreadyIncome.setCustomerNo("4209912333"+i);
					finAlreadyIncome.setFareAmount(10.0);
					finAlreadyIncome.setIncomeAccount("zhifibao");
					finAlreadyIncome.setIncomedAmount(130.0);
					finAlreadyIncome.setTradeDate(new Date());
					finAlreadyIncome.setIncomeType(1);
					finAlreadyIncome.setIncomeTypeDesc("正常收款");
					finAlreadyIncome.setNote("正常收款");
					finAlreadyIncome.setBankTradeNo("201604193331313"+i);
					finAlreadyIncome.setOutTradeNo("A2324242444"+i);
					finAlreadyIncome.setBankNo("2222");
					finAlreadyIncome.setBankName("支付宝");
					finAlreadyIncome.setOperator("System");
					finAlreadyIncome.setOrderAmount(130.0+i);
					finAlreadyIncome.setProductAmount(120.0);
					finAlreadyIncome.setTradeAmount(130.0);
					finAlreadyIncome.setOrderDate(new Date());
					finAlreadyIncome.setOrderNo("A20160401444"+String.valueOf(i));
					finAlreadyIncome.setStoreName("测试店铺名称");
					finAlreadyIncome.setStoreId("223333333333");
					finAlreadyIncome.setOperateType(1);
					finAlreadyIncome.setUpdateTime(new Date());
					finAlreadyIncomeBackgroundApi.insert(finAlreadyIncome);
				}
			}
		} catch (Exception e) {
			logger.error("保存数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
}
