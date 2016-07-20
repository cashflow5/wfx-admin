 /*
 * 版本信息
 
 * 日期 2016-03-23 18:39:33
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.commodity.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.commodity.api.background.ICommodityStyleBackgroundApi;
import com.yougou.wfx.commodity.dto.input.CommodityInventoryInputDto;
import com.yougou.wfx.commodity.dto.output.CommodityInventoryOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.commodity.vo.CommodityInventoryPageVo;

/**
 * CommodityBrandController
 * @author wuyang
 * @Date 创建时间：2016-03-23 18:39:34
 */
@Controller
@RequestMapping("/commodity")
public class CommodityInventoryController extends BaseController{
	
	@Resource
	private ICommodityStyleBackgroundApi commodityStyleBackgroundApi;
	
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/inventoryList")
	public String inventoryList(){
		return "/manage/commodity/inventoryList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryInventoryData")
	@ResponseBody
	public String queryInventoryData(ModelMap map, CommodityInventoryPageVo commodityInventoryVo, PageModel pageModel)throws Exception{
		CommodityInventoryInputDto pageInputDto = (CommodityInventoryInputDto) BeanUtil.convertBean(commodityInventoryVo, CommodityInventoryInputDto.class);
		PageModel<CommodityInventoryOutputDto> result = commodityStyleBackgroundApi.queryCommodityInventoryPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	
}
