 /*
 * 版本信息
 
 * 日期 2016-04-08 13:32:02
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.user.controller;

import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.user.vo.SupplierPageVo;
import com.yougou.wfx.user.api.background.ISupplierBackgroundApi;
import com.yougou.wfx.user.dto.input.SupplierInputDto;
import com.yougou.wfx.user.dto.input.SupplierPageInputDto;
import com.yougou.wfx.user.dto.output.SupplierOutputDto;

/**
 * SupplierController
 * @author wfx
 * @Date 创建时间：2016-04-08 13:32:03
 */
@Controller
@RequestMapping("/user")
public class SupplierController extends BaseController{
	
	@Resource
	private ISupplierBackgroundApi supplierBackgroundApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/supplierList")
	public String supplierList(){
		return "/manage/user/supplierList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/querySupplierData")
	@ResponseBody
	public String querySupplierData(ModelMap map, SupplierPageVo supplierVo, PageModel pageModel)throws Exception{
		SupplierPageInputDto pageInputDto = (SupplierPageInputDto) BeanUtil.convertBean(supplierVo, SupplierPageInputDto.class);
		PageModel<SupplierOutputDto> result = supplierBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getSupplierById")
	@ResponseBody
	public String getSupplierById(String id) throws Exception {
		SupplierOutputDto supplier = new SupplierOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			supplier = supplierBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, supplier);
	}
	
	/**
	 * 通过主键id删除数据
	 */
	@RequestMapping("/removeSupplier")
	@ResponseBody
	public String removeSupplier(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			supplierBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 保存或修改数据
	 */
	@RequestMapping("/saveSupplier")
	@ResponseBody
	public String saveSupplier(SupplierInputDto supplier) {
		try {
			String id = supplier.getId();
			if (StringUtils.isNotBlank(id)) {
				supplierBackgroundApi.update(supplier);
			}else{
				supplierBackgroundApi.insert(supplier);
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
	@RequestMapping("/insertSupplier")
	@ResponseBody
	public String insertSupplier(SupplierInputDto supplier) {
		try {
			supplierBackgroundApi.insert(supplier);
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/updateSupplier")
	@ResponseBody
	public String updateSupplier(SupplierInputDto supplier) {
		try {
			Assert.hasText(supplier.getId(), "参数id不能为空");
			supplierBackgroundApi.update(supplier);
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
}
