 /*
 * 版本信息
 
 * 日期 2016-03-23 18:39:33
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.cms.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.commodity.api.background.ICommodityBrandBackgroundApi;
import com.yougou.wfx.commodity.dto.input.CommodityBrandInputDto;
import com.yougou.wfx.commodity.dto.input.CommodityBrandPageInputDto;
import com.yougou.wfx.commodity.dto.output.CommodityBrandOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.commodity.vo.CommodityBrandPageVo;
import com.yougou.wfx.system.api.IWFXSystemApi;
import com.yougou.wfx.util.UserSecurityUtil;

/**
 * HotBrandController
 * @author li.j1
 * @Date 创建时间：2016-03-23 18:39:34
 *
 */
@Controller
@RequestMapping("/cms")
public class HotBrandController extends BaseController {

	@Resource
	private ICommodityBrandBackgroundApi commodityBrandBackgroundApi;
	
	@Resource
	private IWFXSystemApi wfxSystemApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/hotBrandList")
	public String brandList(ModelMap map, String hotFlag){
		map.put("hotFlag", hotFlag);
		return "/manage/cms/hotBrandList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryHotBrandData")
	@ResponseBody
	public String queryBrandData(ModelMap map, CommodityBrandPageVo commodityBrandVo, PageModel<CommodityBrandOutputDto> pageModel)throws Exception{
		CommodityBrandPageInputDto pageInputDto = (CommodityBrandPageInputDto) BeanUtil.convertBean(commodityBrandVo, CommodityBrandPageInputDto.class);
		pageInputDto.setStatus(1);
		pageInputDto.setUseFlag(1);
		PageModel<CommodityBrandOutputDto> result = commodityBrandBackgroundApi.findPage(pageInputDto, pageModel);
		
		for(CommodityBrandOutputDto outputDto : result.getItems()){
			//兼容新老数据
			if(StringUtils.isNotBlank(outputDto.getMobilePic()) && StringUtils.indexOf(outputDto.getMobilePic(), "http") < 0){
				outputDto.setMobilePic(wfxSystemApi.obtainImgBaseUrl()+outputDto.getMobilePic());
			}
		}
		
		return JSONObject.toJSONString(result);
	}
	
	
	@RequestMapping("/removeHotBrand")
	@ResponseBody
	public String removeHotBrand(String brandIds) {
		try {
			String[] brandIdArray = brandIds.split(",");
			commodityBrandBackgroundApi.batchUpdateHotBrand(brandIdArray,UserSecurityUtil.getSystemUserName(), 2);
			
			
		} catch (Exception e) {
			logger.error("删除热门品牌数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	@RequestMapping("/addHotBrand")
	@ResponseBody
	public String addHotBrand(String brandIds) {
		try {
			String[] brandIdArray = brandIds.split(",");
//			CommodityBrandPageInputDto pageInputDto = new CommodityBrandPageInputDto();
//			pageInputDto.setHotFlag(1);
//			pageInputDto.setStatus(1);
//			pageInputDto.setUseFlag(1);
//			int count = commodityBrandBackgroundApi.findPageCount(pageInputDto);
//			if(count + brandIdArray.length > 24){
//				return super.jsonResult(StateCode.ERROR, "最多还能添加"+(24-count)+"个热门品牌");
//			}
			commodityBrandBackgroundApi.batchUpdateHotBrand(brandIdArray, UserSecurityUtil.getSystemUserName(), 1);
		} catch (Exception e) {
			logger.error("新增热门品牌数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	@RequestMapping("/saveBrandHotSn")
	@ResponseBody
	public String saveBrandHotSn(CommodityBrandInputDto commodityBrand) {
		try {
			commodityBrand.setHotFlag(1);
			String id = commodityBrand.getId();		
			commodityBrand.setUpdatePerson(UserSecurityUtil.getSystemUserName());
			commodityBrand.setUpdateTime(new Date(System.currentTimeMillis()));
			if (StringUtils.isNotBlank(id)) {
				commodityBrandBackgroundApi.update(commodityBrand);
			}
		} catch (Exception e) {
			logger.error("保存数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
}
