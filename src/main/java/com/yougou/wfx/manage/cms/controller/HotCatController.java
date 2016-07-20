 /*
 * 版本信息
 
 * 日期 2016-03-24 17:49:13
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.cms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.cms.api.background.ICommoditySaleCatBackgroundApi;
import com.yougou.wfx.cms.dto.input.CommoditySaleCatInputDto;
import com.yougou.wfx.cms.dto.input.CommoditySaleCatPageInputDto;
import com.yougou.wfx.cms.dto.output.CommoditySaleCatOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.cms.vo.CommoditySaleCatPageVo;
import com.yougou.wfx.system.api.IWFXSystemApi;
import com.yougou.wfx.util.UserSecurityUtil;

/**
 * HotCatController
 * @author wzf
 * @Date 创建时间：2016-04-06 10:16:16
 */
@Controller
@RequestMapping("/cms")
public class HotCatController extends BaseController{
	private final Integer MAX_NUM = 12;
	private Logger logger = LoggerFactory.getLogger(HotCatController.class);
	
	@Resource
	private ICommoditySaleCatBackgroundApi commoditySaleCatBackgroundApi;
	@Resource
	private IWFXSystemApi wfxSystemApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/hotCatList")
	public String hotCatList(ModelMap map){
		String baseUrl = wfxSystemApi.obtainImgBaseUrl();
		map.put("baseUrl", baseUrl);
		return "/manage/cms/hotCatList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryHotCatData")
	@ResponseBody
	public String queryHotCatData(ModelMap map, CommoditySaleCatPageVo commoditySaleCatVo, PageModel pageModel)throws Exception{
		CommoditySaleCatPageInputDto pageInputDto = (CommoditySaleCatPageInputDto) BeanUtil.convertBean(commoditySaleCatVo, CommoditySaleCatPageInputDto.class);
		PageModel<CommoditySaleCatOutputDto> result = commoditySaleCatBackgroundApi.queryHotCatPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 分页查询一级非热门的销售分类，并返回json格式的结果
	 */
	@RequestMapping("/queryLevelOneData")
	@ResponseBody
	public String queryLevelOneData(ModelMap map, CommoditySaleCatPageVo commoditySaleCatVo, PageModel pageModel)throws Exception{
		commoditySaleCatVo.setLevel(1);
		commoditySaleCatVo.setDeleteFlag(2);
		commoditySaleCatVo.setHotFlag(2);
		CommoditySaleCatPageInputDto pageInputDto = (CommoditySaleCatPageInputDto) BeanUtil.convertBean(commoditySaleCatVo, CommoditySaleCatPageInputDto.class);
		PageModel<CommoditySaleCatOutputDto> result = commoditySaleCatBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getHotCatById")
	@ResponseBody
	public String getHotCatById(String id) throws Exception {
		CommoditySaleCatOutputDto commoditySaleCat = new CommoditySaleCatOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			commoditySaleCat = commoditySaleCatBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, commoditySaleCat);
	}
	
	/**
	 * 通过主键id修改热门分类状态
	 */
	@RequestMapping("/removeHotCat")
	@ResponseBody
	public String removeHotCat(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			CommoditySaleCatInputDto inputDto = new CommoditySaleCatInputDto();
			inputDto.setId(id);
			inputDto.setHotFlag(2);
			inputDto.setHotSn(0);
			inputDto.setUpdateTime(new Date());
			inputDto.setUpdateUser(UserSecurityUtil.getSystemUserName());
			commoditySaleCatBackgroundApi.update(inputDto);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 通过主键id批量修改热门分类状态
	 */
	@RequestMapping("/batchRemoveHotCat")
	@ResponseBody
	public String batchRemoveHotCat(String hotCatIds) {
		if(StringUtils.isBlank(hotCatIds)){
			return super.jsonResult(StateCode.ERROR, "分类hotCatIds不能为空");
		}
		try {
			hotCatIds.replaceAll(" ", "");
			String[] hotCatIdArr = hotCatIds.split(",");
			CommoditySaleCatInputDto inputDto = null;
			for(int i=0;i<hotCatIdArr.length;i++){
				inputDto = new CommoditySaleCatInputDto();
				inputDto.setId(hotCatIdArr[i]);
				inputDto.setHotFlag(2);
				inputDto.setHotSn(0);
				inputDto.setUpdateTime(new Date());
				inputDto.setUpdateUser(UserSecurityUtil.getSystemUserName());
				commoditySaleCatBackgroundApi.update(inputDto);
			}
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 保存或修改数据
	 */
	@RequestMapping("/saveHotCat")
	@ResponseBody
	public String saveHotCat(CommoditySaleCatInputDto commoditySaleCat) {
		try {
			String id = commoditySaleCat.getId();
			if (StringUtils.isNotBlank(id)) {
				commoditySaleCatBackgroundApi.update(commoditySaleCat);
			}else{
				commoditySaleCatBackgroundApi.insert(commoditySaleCat);
			}
		} catch (Exception e) {
			logger.error("保存数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 将销售分类添加到热门分类中
	 */
	@RequestMapping("/insertHotCat")
	@ResponseBody
	public String insertHotCat(String id) {
		try {
			Assert.hasText(id,"参数分类id不能为空");
			int num = commoditySaleCatBackgroundApi.getHotCatCount(null);
			if(num >= MAX_NUM){
				return super.jsonResult(StateCode.ERROR, "添加热门分类出错，原因：热门分类不能超过12个");
			}
			synchronized (this) {
				int hot_sn = commoditySaleCatBackgroundApi.getMaxHotCatSn();
				CommoditySaleCatInputDto inputDto = new CommoditySaleCatInputDto();
				inputDto.setId(id);
				inputDto.setHotFlag(1);
				inputDto.setHotSn(hot_sn + 1);
				inputDto.setUpdateTime(new Date());
				inputDto.setUpdateUser(UserSecurityUtil.getSystemUserName());
				commoditySaleCatBackgroundApi.update(inputDto);
			}
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 将销售分类添加到热门分类中
	 */
	@RequestMapping("/batchInsertHotCat")
	@ResponseBody
	public String batchInsertHotCat(String saleCatIds) {
		if(StringUtils.isBlank(saleCatIds)){
			return super.jsonResult(StateCode.ERROR, "分类saleCatIds不能为空");
		}
		try {
			saleCatIds.replaceAll(" ", "");
			String[] saleCatIdArr = saleCatIds.split(",");
			int catNum = saleCatIdArr.length;
//			int num = commoditySaleCatBackgroundApi.getHotCatCount(null);
//			if(catNum + num > MAX_NUM){
//				return super.jsonResult(StateCode.ERROR, "添加热门分类出错，原因：热门分类不能超过12个,最多再添加"+(MAX_NUM - num)+"个");
//			}
			synchronized (this) {
				CommoditySaleCatInputDto inputDto = null;
				int hot_sn = commoditySaleCatBackgroundApi.getMaxHotCatSn();
				for(int i=0;i<catNum;i++){
					hot_sn++;
					inputDto = new CommoditySaleCatInputDto();
					inputDto.setId(saleCatIdArr[i]);
					inputDto.setHotFlag(1);
					inputDto.setHotSn(hot_sn);
					inputDto.setUpdateTime(new Date());
					inputDto.setUpdateUser(UserSecurityUtil.getSystemUserName());
					commoditySaleCatBackgroundApi.update(inputDto);
				}
			}
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/updateHotCatSn")
	@ResponseBody
	public String updateHotCatSn(String id,int hotSn) {
		try {
			CommoditySaleCatInputDto inputDto = new CommoditySaleCatInputDto();
			inputDto.setHotSn(hotSn);
			int num = commoditySaleCatBackgroundApi.getHotCatCount(inputDto);
			if(num > 0){
				return super.jsonResult(StateCode.ERROR, "设置排序号出错，原因：已有排序号为（"+hotSn+"）的热门分类");
			}
			inputDto = new CommoditySaleCatInputDto();
			inputDto.setId(id);
			inputDto.setHotSn(hotSn);
			inputDto.setUpdateTime(new Date());
			inputDto.setUpdateUser(UserSecurityUtil.getSystemUserName());
			commoditySaleCatBackgroundApi.update(inputDto);
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
}
