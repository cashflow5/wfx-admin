 /*
 * 版本信息
 
 * 日期 2016-03-28 10:58:34
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.finance.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.finance.api.background.IFinSellerInfoBackgroundApi;
import com.yougou.wfx.finance.dto.input.FinSellerInfoInputDto;
import com.yougou.wfx.finance.dto.input.FinSellerInfoPageInputDto;
import com.yougou.wfx.finance.dto.output.FinSellerInfoOutputDto;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.finance.vo.FinSellerInfoPageVo;

/**
 * FinSellerInfoController
 * @author wfx
 * @Date 创建时间：2016-03-28 10:58:34
 */
@Controller
@RequestMapping("/finance")
public class FinSellerInfoController extends BaseController{
	
	@Resource
	private IFinSellerInfoBackgroundApi finSellerInfoBackgroundApi;
	
	/**
	 * 进入分销商账户菜单
	 */
	@RequestMapping("/sellerList")
	public String sellerList(ModelMap modelMap, FinSellerInfoInputDto finSellerInfoInputDto) {
		Map<String, Object> resultMap = finSellerInfoBackgroundApi.queryAccountSummary(finSellerInfoInputDto);
		modelMap.addAttribute("dataMap", resultMap);
		modelMap.addAttribute("queryVo", finSellerInfoInputDto);
		return "/manage/finance/sellerList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/querySellerData")
	@ResponseBody
	public String querySellerData(ModelMap map, FinSellerInfoPageVo finSellerInfoVo, PageModel pageModel)throws Exception{
		FinSellerInfoPageInputDto pageInputDto = (FinSellerInfoPageInputDto) BeanUtil.convertBean(finSellerInfoVo, FinSellerInfoPageInputDto.class);
		PageModel<FinSellerInfoOutputDto> result = finSellerInfoBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 进入分销商账户交易明细列表
	 * @param id 分销商ID
	 * @param map
	 * @return
	 */
	@RequestMapping("/sellerDetailList")
	public String sellerAccountDetail(String id, ModelMap map) {
		FinSellerInfoOutputDto sellerInfo = new FinSellerInfoOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			sellerInfo = this.finSellerInfoBackgroundApi.getById(id);
			map.put("sellerInfo", sellerInfo);
		} catch (Exception e) {
			this.logger.error("查询分销商信息异常", e.getMessage());
		}
		return "/manage/finance/sellerDetailList";
	}
	
	
	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getSellerById")
	@ResponseBody
	public String getSellerById(String id) throws Exception {
		FinSellerInfoOutputDto finSellerInfo = new FinSellerInfoOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			finSellerInfo = finSellerInfoBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, finSellerInfo);
	}
	
	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/updateSeller")
	@ResponseBody
	public String updateSeller(FinSellerInfoInputDto finSellerInfo) {
		try {
			Assert.hasText(finSellerInfo.getId(), "参数id不能为空");
			finSellerInfoBackgroundApi.update(finSellerInfo);
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
}
