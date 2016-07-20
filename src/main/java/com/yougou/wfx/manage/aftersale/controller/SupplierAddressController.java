/*
 * 版本信息

 * 日期 2016-04-26 09:34:42

 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.aftersale.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.aftersale.api.background.ISupplierAddressBackgroundApi;
import com.yougou.wfx.aftersale.dto.input.SupplierAddressInputDto;
import com.yougou.wfx.aftersale.dto.input.SupplierAddressPageInputDto;
import com.yougou.wfx.aftersale.dto.output.SupplierAddressOutputDto;
import com.yougou.wfx.basicset.api.background.ISystemmgAreaBackgroundApi;
import com.yougou.wfx.basicset.dto.input.SystemmgAreaInputDto;
import com.yougou.wfx.basicset.dto.output.SystemmgAreaOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.aftersale.vo.SupplierAddressPageVo;

/**
 * SupplierAddressController
 * 
 * @author wfx
 * @Date 创建时间：2016-04-26 09:34:43
 */
@Controller
@RequestMapping("/afterSale")
public class SupplierAddressController extends BaseController {

	@Resource
	private ISupplierAddressBackgroundApi supplierAddressBackgroundApi;
	@Resource
	private ISystemmgAreaBackgroundApi systemmgAreaBackgroundApi;

	/**
	 * 进入菜单
	 */
	@RequestMapping("/supplierAddressList")
	public String propList(String supplierCode, ModelMap map) {
		map.put("supplierCode", supplierCode);
		return "/manage/aftersale/supplierAddressList";
	}

	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/querySupplierAddressData")
	@ResponseBody
	public String queryPropData(ModelMap map, SupplierAddressPageVo supplierAddressVo, PageModel pageModel) throws Exception {
		SupplierAddressPageInputDto pageInputDto = (SupplierAddressPageInputDto) BeanUtil.convertBean(supplierAddressVo, SupplierAddressPageInputDto.class);
		PageModel<SupplierAddressOutputDto> result = supplierAddressBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}

	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getSupplierAddressById")
	@ResponseBody
	public String getPropById(String id) throws Exception {
		SupplierAddressOutputDto supplierAddress = new SupplierAddressOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			supplierAddress = supplierAddressBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, supplierAddress);
	}

	/**
	 * 通过主键id删除数据
	 */
	@RequestMapping("/removeSupplierAddress")
	@ResponseBody
	public String removeProp(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			supplierAddressBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}

	@RequestMapping("/editSupplierAddress")
	public String editSupplierAddress(String id, ModelMap map) {
		SupplierAddressOutputDto supplierAddressVo = new SupplierAddressOutputDto();
		if (StringUtils.isNotBlank(id)) {
			supplierAddressVo = supplierAddressBackgroundApi.getById(id);
		}
		map.addAttribute("supplierAddressVo", supplierAddressVo);
		return "/manage/aftersale/supplierAddressEdit";
	}

	/**
	 * 获取地址数据
	 * 
	 * @param inputDto
	 * @param map
	 * @return
	 */
	@RequestMapping("/getSystemmgAreaData")
	public String getSystemmgAreaData(ModelMap map,String level, String pNo) {
		SystemmgAreaInputDto inputDto = new SystemmgAreaInputDto();
		inputDto.setLevel(level);
		inputDto.setNo(pNo);
		List<SystemmgAreaOutputDto> areaList = systemmgAreaBackgroundApi.queryList(inputDto);
		map.put("areaList", areaList);
		map.put("level", level);
		return "/manage/aftersale/areaSelectHtml";
	}

	/**
	 * 保存或修改数据
	 */
	@RequestMapping("/saveOrUpdateSupplierAddress")
	@ResponseBody
	public String saveOrUpdateSupplierAddress(SupplierAddressInputDto supplierAddress) {
		try {
			String id = supplierAddress.getId();
			if (StringUtils.isNotBlank(id)) {
				supplierAddressBackgroundApi.update(supplierAddress);
			} else {
				supplierAddressBackgroundApi.insert(supplierAddress);
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
	@RequestMapping("/insertSupplierAddress")
	@ResponseBody
	public String insertProp(SupplierAddressInputDto supplierAddress) {
		try {
			supplierAddressBackgroundApi.insert(supplierAddress);
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}

	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/updateSupplierAddress")
	@ResponseBody
	public String updateProp(SupplierAddressInputDto supplierAddress) {
		try {
			Assert.hasText(supplierAddress.getId(), "参数id不能为空");
			supplierAddressBackgroundApi.update(supplierAddress);
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
}
