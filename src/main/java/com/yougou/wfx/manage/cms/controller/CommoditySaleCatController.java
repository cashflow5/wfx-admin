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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.cms.api.background.ICommoditySaleCatBackgroundApi;
import com.yougou.wfx.cms.api.background.ICommoditySaleCatRelaBackgroundApi;
import com.yougou.wfx.cms.dto.input.CommoditySaleCatInputDto;
import com.yougou.wfx.cms.dto.input.CommoditySaleCatPageInputDto;
import com.yougou.wfx.cms.dto.input.CommoditySaleCatRelaInputDto;
import com.yougou.wfx.cms.dto.output.CommoditySaleCatOutputDto;
import com.yougou.wfx.cms.dto.output.CommoditySaleCatRelaOutputDto;
import com.yougou.wfx.commodity.api.background.ICommodityCatb2cBackgroundApi;
import com.yougou.wfx.commodity.dto.input.CommodityCatb2cInputDto;
import com.yougou.wfx.commodity.dto.output.CommodityCatb2cOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.dto.base.WFXResult;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.cms.vo.CommoditySaleCatPageVo;
import com.yougou.wfx.system.api.IFileUploadApi;
import com.yougou.wfx.system.api.IWFXSystemApi;
import com.yougou.wfx.system.model.BaseZtree;
import com.yougou.wfx.util.UserSecurityUtil;

/**
 * CommoditySaleCatController
 * @author wzf
 * @Date 创建时间：2016-03-24 17:49:14
 */
@Controller
@RequestMapping("/cms")
public class CommoditySaleCatController extends BaseController{
	
	@Resource
	private ICommoditySaleCatBackgroundApi commoditySaleCatBackgroundApi;
	@Resource
	private ICommoditySaleCatRelaBackgroundApi commoditySaleCatRelaBackgroundApi;
	@Resource
	private ICommodityCatb2cBackgroundApi commodityCatb2cBackgroundApi;
	@Resource
	private IFileUploadApi fileUploadApi;
	@Resource
	private IWFXSystemApi wfxSystemApi;
	/**
	 * 进入菜单
	 */
	@RequestMapping("/saleCatList")
	public String saleCatList(){
		return "/manage/cms/saleCatList";
	}
	
	/**
	 * 查询销售分类的数据，并组装成Ztree需要的格式
	 */
	@RequestMapping("/getCatJsonForZtree")
	@ResponseBody
	public String getCatJsonForZtree(String id,String name,Integer level)throws Exception{
		List<BaseZtree> baseZtreeList = new ArrayList<BaseZtree>();
		CommoditySaleCatInputDto commoditySaleCat = new CommoditySaleCatInputDto();
		if(StringUtils.isBlank(id)){
			//基础分类顶层根目录
			BaseZtree baseZtree = new BaseZtree("0", null, "销售分类管理", "销售分类管理", "root", 
					"true", "false", "root", "", "根目录", "1", null);
			baseZtreeList.add(baseZtree);
			commoditySaleCat.setLevel(1);
		}else{
			commoditySaleCat.setParentId(id);
		}
		commoditySaleCat.setDeleteFlag(2);
		List<CommoditySaleCatOutputDto> outPutList = commoditySaleCatBackgroundApi.queryList(commoditySaleCat);
		List<BaseZtree> ztreeCatDtoList = this.catToZtree(outPutList);
		baseZtreeList.addAll(ztreeCatDtoList);
		return JSONObject.toJSONString(baseZtreeList);
	}
	
	@RequestMapping("/querySaleCatById")
	public String querySaleCatById(ModelMap map,String parentId){
		CommoditySaleCatOutputDto pOutputDto = commoditySaleCatBackgroundApi.getById(parentId);
		if(!"0".equals(parentId) && null != pOutputDto.getLevel() && pOutputDto.getLevel() == 2){
			//一级基础分类
			CommodityCatb2cInputDto basicCatInputDto = new CommodityCatb2cInputDto();
			basicCatInputDto.setParentId("0");
			List<CommodityCatb2cOutputDto> basicList = commodityCatb2cBackgroundApi.queryList(basicCatInputDto);
			//查询销售分类绑定的基础分类
			CommoditySaleCatRelaInputDto relaInputDto = new CommoditySaleCatRelaInputDto();
			relaInputDto.setSaleCatId(parentId);
			List<CommoditySaleCatRelaOutputDto> relaList = commoditySaleCatRelaBackgroundApi.queryList(relaInputDto);
			relaList = commoditySaleCatRelaBackgroundApi.fillCatNames(relaList);
			Integer comNum = commoditySaleCatRelaBackgroundApi.queryStyleNum(relaList);
			map.put("basicList", basicList);
			map.put("parentCat", pOutputDto);
			map.put("relaList",relaList);
			map.put("comNum",comNum);
			return "/manage/cms/saleCatLevelThree";
		}else{
			CommoditySaleCatInputDto inputDto = new CommoditySaleCatInputDto();
			inputDto.setParentId(parentId);
			inputDto.setDeleteFlag(2);
			List<CommoditySaleCatOutputDto> outputList = commoditySaleCatBackgroundApi.queryList(inputDto);
			map.put("parentCat", pOutputDto);
			map.put("catList", outputList);
			map.put("imgBaseUrl", wfxSystemApi.obtainImgBaseUrl());
			return "/manage/cms/saleCatTable";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/uploadSaleCatImg", method = RequestMethod.POST)
	public String uploadImg(@RequestParam MultipartFile file, HttpServletRequest request, String name, String saleCatId) throws Exception {
		try {
			WFXResult<String> result = fileUploadApi.uploadSaleCatImg(saleCatId, name,UserSecurityUtil.getSystemUserName(), file.getInputStream());
			int code = result.getResultCode();
			if(code == 0){
				return jsonResult(StateCode.SUCCESS, code+"");
			}else{
				return jsonResult(StateCode.ERROR, code+"");
			}
		} catch (Exception e) {
			logger.error("上传图片出错。",e);
			return jsonResult(StateCode.ERROR, "20");
		}
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/querySaleCatData")
	@ResponseBody
	public String querySaleCatData(ModelMap map, CommoditySaleCatPageVo commoditySaleCatVo, PageModel<CommoditySaleCatOutputDto> pageModel)throws Exception{
		CommoditySaleCatPageInputDto pageInputDto = (CommoditySaleCatPageInputDto) BeanUtil.convertBean(commoditySaleCatVo, CommoditySaleCatPageInputDto.class);
		PageModel<CommoditySaleCatOutputDto> result = commoditySaleCatBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	@RequestMapping("/getSaleCatById")
	@ResponseBody
	public String getSaleCatById(String id) throws Exception {
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
	
	@RequestMapping("/removeSaleCat")
	@ResponseBody
	public String removeSaleCat(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			commoditySaleCatBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	@RequestMapping("/saveSaleCat")
	@ResponseBody
	public String saveSaleCat(CommoditySaleCatInputDto commoditySaleCat) {
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
	
	@RequestMapping("/saveBasicSaleRela")
	@ResponseBody
	public String saveBasicSaleRela(CommoditySaleCatRelaInputDto inputDto) {
		try{
			String basicCatNo = inputDto.getBaseCatNo();
			if(StringUtils.isBlank(basicCatNo)){
				String basicCatId = inputDto.getBaseCatId();
				CommodityCatb2cOutputDto basicCat = commodityCatb2cBackgroundApi.getById(basicCatId);
				inputDto.setBaseCatNo(basicCat.getNo());
			}
			List<CommoditySaleCatRelaOutputDto> relaList = commoditySaleCatRelaBackgroundApi.queryList(inputDto);
			inputDto.setUpdateTime(new Date());
			inputDto.setUpdateUser(UserSecurityUtil.getSystemUserName());
			//避免重复绑定
			if(relaList != null && relaList.size() > 0){
				inputDto.setId(relaList.get(0).getId());
				commoditySaleCatRelaBackgroundApi.update(inputDto);
			}else{
				commoditySaleCatRelaBackgroundApi.insert(inputDto);
			}
		}catch(Exception e){
			logger.error("绑定基础分类异常",e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	@RequestMapping("/removeBasicSaleRela")
	@ResponseBody
	public String removeBasicSaleRela(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			commoditySaleCatRelaBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	@RequestMapping("/addSaleCat")
	@ResponseBody
	public String addSaleCat(String pId, String name,String strLevel){
		CommoditySaleCatInputDto saleCatDto = new CommoditySaleCatInputDto();
		saleCatDto.setName(name);
		saleCatDto.setParentId(pId);
		//设置新生成分类的等级
		Integer level = 1;
		if(StringUtils.isNotBlank(strLevel)){
			level = Integer.parseInt(strLevel) + 1;
		}
		saleCatDto.setLevel(level);
		saleCatDto.setUpdateUser(UserSecurityUtil.getSystemUserName());
		Date date = new Date();
		saleCatDto.setUpdateTime(date);
		saleCatDto.setCreateTime(date);
		//设置是否为热门分类：1：是，2：否。
		saleCatDto.setHotFlag(2);
		saleCatDto.setDeleteFlag(2);
		String id = commoditySaleCatBackgroundApi.insert(saleCatDto);
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("rtype", "menu");
		map.put("level", level+"");
		return JSONObject.toJSONString(map);
	}
	
	@RequestMapping("/insertSaleCat")
	@ResponseBody
	public String insertSaleCat(CommoditySaleCatInputDto commoditySaleCat) {
		try {
			commoditySaleCatBackgroundApi.insert(commoditySaleCat);
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	@RequestMapping("/updateSaleCat")
	@ResponseBody
	public String updateSaleCat(CommoditySaleCatInputDto commoditySaleCat) {
		Integer deleteFlag = commoditySaleCat.getDeleteFlag();
		//1表示删除，删除时要先判断该分类下是否有商品，如果有商品则不能删除
		if(null != deleteFlag && deleteFlag == 1){
			CommoditySaleCatOutputDto outputDto = commoditySaleCatBackgroundApi.getById(commoditySaleCat.getId());
			CommoditySaleCatInputDto inputDto = (CommoditySaleCatInputDto)BeanUtil.convertBean(outputDto, CommoditySaleCatInputDto.class);
			boolean hasBindCat = commoditySaleCatBackgroundApi.hasBindCat(inputDto);
			if(hasBindCat){
				return super.jsonResult(StateCode.ERROR, "该销售分类有绑定的基础分类，不能删除");
			}
			//删除子分类
			Integer level = outputDto.getLevel();
			if(null != level && level == 1){
				CommoditySaleCatInputDto deleteDto = new CommoditySaleCatInputDto();
				deleteDto.setParentId(outputDto.getId());
				deleteDto.setDeleteFlag(1);
				deleteDto.setUpdateUser(UserSecurityUtil.getSystemUserName());
				deleteDto.setUpdateTime(new Date());
				commoditySaleCatBackgroundApi.updateChildren(deleteDto);
			}
		}
		commoditySaleCat.setUpdateUser(UserSecurityUtil.getSystemUserName());
		commoditySaleCat.setUpdateTime(new Date());
		commoditySaleCatBackgroundApi.update(commoditySaleCat);
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 将销售分类列表转换为json对象列表
	 * @param commodityCatb2cList
	 * @return
	 */
	public List<BaseZtree> catToZtree(List<CommoditySaleCatOutputDto> commoditySaleCatList){
		List<BaseZtree> baseZtreeList = new ArrayList<BaseZtree>();
		if(null != commoditySaleCatList && commoditySaleCatList.size() > 0){
			BaseZtree baseZtree = null;
			CommoditySaleCatInputDto inputDto = null;
			for(CommoditySaleCatOutputDto catDto:commoditySaleCatList){
				baseZtree = new BaseZtree();
				inputDto = new CommoditySaleCatInputDto();
				//用于查询该分类下面是否有子分类
				baseZtree.setId(catDto.getId());
				baseZtree.setPId(catDto.getParentId());
				baseZtree.setName(catDto.getName());
				baseZtree.setT(catDto.getName());
				Integer level = catDto.getLevel();
				String type = "folder";
				String isParent = "true";
				String isFlage = "1";
				
				if(null != level && level > 1){
					type = "menu";
					isParent = "false";
					isFlage = "0";
				}else{
					//查询每个分类下的子分类数量
					inputDto.setParentId(catDto.getId());
					inputDto.setDeleteFlag(2);
					int num = commoditySaleCatBackgroundApi.findPageCount(inputDto);
					if(num > 0){
						type = "folder";
						isParent = "true";
						isFlage = "1";
					}else{
						type = "menu";
						isParent = "false";
						isFlage = "0";
					}
				}
				baseZtree.setRtype(type);
				baseZtree.setClick("false");
				baseZtree.setIconSkin(type);
				baseZtree.setUrl("");
				baseZtree.setDescription(catDto.getName());
				baseZtree.setIsFlag(isFlage);//是否启用
				baseZtree.setIsParent(isParent);
				baseZtreeList.add(baseZtree);
			}
		}
		return baseZtreeList;
	}
}
