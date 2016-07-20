 /*
 * 版本信息
 
 * 日期 2016-03-23 18:39:33
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.commodity.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

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
import com.yougou.wfx.commodity.api.background.ICommodityBrandBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityBrandCatb2cBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityCatb2cBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityStyleBackgroundApi;
import com.yougou.wfx.commodity.dto.input.CommodityBrandInputDto;
import com.yougou.wfx.commodity.dto.input.CommodityBrandPageInputDto;
import com.yougou.wfx.commodity.dto.input.CommodityCatb2cInputDto;
import com.yougou.wfx.commodity.dto.output.CommodityBrandOutputDto;
import com.yougou.wfx.commodity.dto.output.CommodityCatb2cOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.dto.base.WFXResult;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.commodity.vo.BaseCatZtreeVo;
import com.yougou.wfx.manage.commodity.vo.CommodityBrandPageVo;
import com.yougou.wfx.system.api.IFileUploadApi;
import com.yougou.wfx.system.api.IWFXSystemApi;
import com.yougou.wfx.util.UserSecurityUtil;

/**
 * CommodityBrandController
 * @author wuyang
 * @Date 创建时间：2016-03-23 18:39:34
 */
@Controller
@RequestMapping("/commodity")
public class CommodityBrandController extends BaseController{
	
	@Resource
	private ICommodityBrandBackgroundApi commodityBrandBackgroundApi;
	
	@Resource
	private ICommodityCatb2cBackgroundApi commodityCatb2cBackgroundApi;
	
	@Resource
	private ICommodityBrandCatb2cBackgroundApi commodityBrandCatb2cBackgroundApi;
	
	@Resource
	private ICommodityStyleBackgroundApi commodityStyleBackgroundApi;
	
	@Resource
	private IFileUploadApi fileUploadApi;
	
	@Resource
	private IWFXSystemApi wfxSystemApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/brandList")
	public String brandList(){
		return "/manage/commodity/brandList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryBrandData")
	@ResponseBody
	public String queryBrandData(ModelMap map, CommodityBrandPageVo commodityBrandVo, PageModel<CommodityBrandOutputDto> pageModel)throws Exception{
		CommodityBrandPageInputDto pageInputDto = (CommodityBrandPageInputDto) BeanUtil.convertBean(commodityBrandVo, CommodityBrandPageInputDto.class);
		PageModel<CommodityBrandOutputDto> result = commodityBrandBackgroundApi.findPage(pageInputDto, pageModel);
		
		for(CommodityBrandOutputDto outputDto : result.getItems()){
			int commodityCount =  commodityStyleBackgroundApi.getCommodityCountByBrandNo(outputDto.getBrandNo());
			outputDto.setCommodityCount(commodityCount);
			//兼容新老数据
			if(StringUtils.isNotBlank(outputDto.getMobilePic()) && StringUtils.indexOf(outputDto.getMobilePic(), "http") < 0){
				outputDto.setMobilePic(wfxSystemApi.obtainImgBaseUrl()+outputDto.getMobilePic());
			}
		}
		
		return JSONObject.toJSONString(result);
	}
	
	@RequestMapping("/getBrandById")
	@ResponseBody
	public String getBrandById(String id) throws Exception {
		CommodityBrandOutputDto commodityBrand = new CommodityBrandOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			commodityBrand = commodityBrandBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, commodityBrand);
	}
	
	@RequestMapping("/removeBrand")
	@ResponseBody
	public String removeBrand(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			commodityBrandBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	@RequestMapping("/saveBrand")
	@ResponseBody
	public String saveBrand(CommodityBrandInputDto commodityBrand, String catIds) {
		try {
			String id = commodityBrand.getId();
			//如果status=2 停用，则校验商品数量
			int commodityCount =  commodityStyleBackgroundApi.getCommodityCountByBrandNo(commodityBrand.getBrandNo());
			if(commodityCount > 0 && 2 == commodityBrand.getStatus()){
				return super.jsonResult(StateCode.ERROR, "商品数量大于0，不能停用");
			}
			
			commodityBrand.setUpdatePerson(UserSecurityUtil.getSystemUserName());
			if (StringUtils.isNotBlank(id)) {
				commodityBrand.setUpdateTime(new Date(System.currentTimeMillis()));
				commodityBrandBackgroundApi.update(commodityBrand);
			}else{
				commodityBrand.setCreateTime(new Date(System.currentTimeMillis()));
				commodityBrand.setUpdateTime(new Date(System.currentTimeMillis()));
				commodityBrandBackgroundApi.insert(commodityBrand);
			}
			if(StringUtils.isNotBlank(catIds)){
				commodityBrandCatb2cBackgroundApi.batchInsert(commodityBrand.getId(),catIds.split("_"));
			}
		} catch (Exception e) {
			logger.error("保存数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/brandEdit")
	public String brandEdit(ModelMap map, @RequestParam("id") String id){
		CommodityBrandOutputDto commodityBrand = new CommodityBrandOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			commodityBrand = commodityBrandBackgroundApi.getById(id);
			//兼容新老数据
			if(StringUtils.isNotBlank(commodityBrand.getLogoLeastUrl()) && StringUtils.indexOf(commodityBrand.getLogoLeastUrl(), "http") < 0){
				commodityBrand.setLogoLeastUrl(wfxSystemApi.obtainImgBaseUrl()+commodityBrand.getLogoLeastUrl());
			}
			if(StringUtils.isNotBlank(commodityBrand.getLogoSmallUrl()) && StringUtils.indexOf(commodityBrand.getLogoSmallUrl(), "http") < 0){
				commodityBrand.setLogoSmallUrl(wfxSystemApi.obtainImgBaseUrl()+commodityBrand.getLogoSmallUrl());
			}
			if(StringUtils.isNotBlank(commodityBrand.getLogoNameUrl()) && StringUtils.indexOf(commodityBrand.getLogoNameUrl(), "http") < 0){
				commodityBrand.setLogoNameUrl(wfxSystemApi.obtainImgBaseUrl()+commodityBrand.getLogoNameUrl());
			}
			if(StringUtils.isNotBlank(commodityBrand.getLogoMiddleUrl()) && StringUtils.indexOf(commodityBrand.getLogoMiddleUrl(), "http") < 0){
				commodityBrand.setLogoMiddleUrl(wfxSystemApi.obtainImgBaseUrl()+commodityBrand.getLogoMiddleUrl());
			}
			if(StringUtils.isNotBlank(commodityBrand.getMobilePic()) && StringUtils.indexOf(commodityBrand.getMobilePic(), "http") < 0){
				commodityBrand.setMobilePic(wfxSystemApi.obtainImgBaseUrl()+commodityBrand.getMobilePic());
			}
			
		} catch (Exception e) {
			logger.error("查询数据异常", e);
		}
		map.put("commodityBrand", commodityBrand);
		return "/manage/commodity/brandEdit";
	}
	
	@RequestMapping("/queryBasicCat")
	@ResponseBody
	public String queryBasicCat(ModelMap map, @RequestParam("id") String id){
		CommodityCatb2cInputDto catInputDto = new CommodityCatb2cInputDto();
		catInputDto.setDeleteFlag(1);
		List<String>  cat2cIdList = commodityBrandCatb2cBackgroundApi.queryCatb2cIdsByBrandId(id);
		List<CommodityCatb2cOutputDto> result = commodityCatb2cBackgroundApi.queryList(catInputDto);
		BaseCatZtreeVo ztreeVo = new BaseCatZtreeVo();
		List<BaseCatZtreeVo> resultList = new ArrayList<BaseCatZtreeVo>();
		ztreeVo.setId("1");
		ztreeVo.setName("全部");
		ztreeVo.setPId("0");
		ztreeVo.setChecked(false);
		ztreeVo.setOpen(true);
		ztreeVo.setLevel(0);
		ztreeVo.setTitle("全部");
		resultList.add(ztreeVo);
		for(CommodityCatb2cOutputDto outputDto : result){
			ztreeVo = new BaseCatZtreeVo(); 
			ztreeVo.setId(outputDto.getId());
			ztreeVo.setPId("0".equals(outputDto.getParentId())? "1" : outputDto.getParentId());
			ztreeVo.setName(outputDto.getCatName());
			if(cat2cIdList.contains(outputDto.getId())){
				
				ztreeVo.setChecked(true);
			}
			ztreeVo.setOpen(false);
			ztreeVo.setLevel(outputDto.getLevel());
			ztreeVo.setTitle(outputDto.getCatName());
			
			resultList.add(ztreeVo);
		}
		
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, JSONObject.toJSONString(resultList));
	}
	
	@ResponseBody
	@RequestMapping(value = "uploadBrandImg", method = RequestMethod.POST)
	public String uploadImg(@RequestParam MultipartFile file, String name, @RequestParam(defaultValue="1") Integer imgType, String brandId) throws Exception {
		JSONObject json = new JSONObject();
		try {
			WFXResult<String> result = fileUploadApi.uploadBrandImg(imgType, name, file.getInputStream());
			if(result.getResultCode() == 0){				
				if(StringUtils.isNotBlank(brandId)) {//更新数据库信息
					//如果传过来的brandId不为空的话，那么上传完了之后还要跟数据库表进行绑定
					CommodityBrandInputDto brand = new CommodityBrandInputDto();
					brand.setUpdateTime(new Date(System.currentTimeMillis()));
					brand.setUpdatePerson(UserSecurityUtil.getSystemUserName());
					brand.setId(brandId);
					switch(imgType){
					case 5: 
						brand.setLogoLeastUrl(result.getResult());
						break;
					case 2:
						brand.setLogoMiddleUrl(result.getResult());
						break;
					case 1:
						brand.setLogoNameUrl(result.getResult());
						break;
					case 3:
						brand.setLogoSmallUrl(result.getResult());
						break;
					case 4:
						brand.setMobilePic(result.getResult());
						break;
					}
											
					commodityBrandBackgroundApi.update(brand);
				}
				
				json.put("result", "success");
				json.put("url", wfxSystemApi.obtainImgBaseUrl()+result.getResult());
				return json.toString();
			}else{
				json.put("result", "fail");
				json.put("code", result.getResultCode());
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		json.put("result", "fail");
		json.put("msg", "服务器出现异常，请重试");
		return json.toString();
	}
}
