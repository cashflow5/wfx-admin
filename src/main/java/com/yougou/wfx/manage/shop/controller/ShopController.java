 /*
 * 版本信息
 
 * 日期 2016-04-06 17:14:31
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.shop.controller;

import java.util.Date;

import javax.annotation.Resource;

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
import com.yougou.wfx.manage.shop.vo.ShopPageVo;
import com.yougou.wfx.shop.api.background.IShopBackgroundApi;
import com.yougou.wfx.shop.dto.input.ShopInputDto;
import com.yougou.wfx.shop.dto.input.ShopPageInputDto;
import com.yougou.wfx.shop.dto.output.DiscoverShopOutputDto;
import com.yougou.wfx.shop.dto.output.ShopOutputDto;
import com.yougou.wfx.util.Constant;
import com.yougou.wfx.util.UserSecurityUtil;

/**
 * ShopController
 * @author luoq
 * @Date 创建时间：2016-04-06 17:14:32
 */
@Controller
@RequestMapping("/shop")
public class ShopController extends BaseController{
	
	@Resource
	private IShopBackgroundApi shopBackgroundApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/shopList")
	public String shopList(){
		return "/manage/shop/shopList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryShopData")
	@ResponseBody
	public String queryShopData(ModelMap map, ShopPageVo shopVo, PageModel<ShopOutputDto> pageModel)throws Exception{
		ShopPageInputDto pageInputDto = (ShopPageInputDto) BeanUtil.convertBean(shopVo, ShopPageInputDto.class);
		PageModel<ShopOutputDto> result = shopBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果,供发现功能的富文本使用
	 */
	@RequestMapping("/queryArticleShopData")
	@ResponseBody
	public String queryArticleShopData(ModelMap map, ShopPageVo shopVo, PageModel pageModel)throws Exception{
		DiscoverShopOutputDto disShop = (DiscoverShopOutputDto) BeanUtil.convertBean(shopVo, DiscoverShopOutputDto.class);
		PageModel<DiscoverShopOutputDto> result = shopBackgroundApi.findInfoPage(disShop, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	@RequestMapping("/getShopById")
	public String getShopById(ModelMap map,String id) throws Exception {
		ShopOutputDto shop = new ShopOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			shop = shopBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		map.put("vo", shop);
		return "/manage/shop/shopDetailInfo";
	}
	
//	@RequestMapping("/removeShop")
//	@ResponseBody
//	public String removeShop(@RequestParam("id") String id) {
//		try {
//			Assert.hasText(id, "参数id不能为空");
//			shopBackgroundApi.removeById(id);
//		} catch (Exception ex) {
//			logger.error("删除数据异常", ex);
//			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
//		}
//		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
//	}
	
	@RequestMapping("/openShop")
	@ResponseBody
	public String openShop(@RequestParam("id") String id) {
		int count = 0;
		try {
			Assert.hasText(id, "参数id不能为空");
			String user = UserSecurityUtil.getSystemUserName();
			ShopInputDto shopDto = new ShopInputDto();
			shopDto.setId(id);
			shopDto.setStatus(Constant.SHOP_OPEN);
			shopDto.setUpdateUser(user);
			shopDto.setUpdateTime(new Date());
			count = shopBackgroundApi.update(shopDto);
		} catch (Exception ex) {
			logger.error("开启店铺异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS,count);
	}
	
	@RequestMapping("/closeShop")
	@ResponseBody
	public String closeShop(@RequestParam("id") String id) {
		int count = 0;
		try {
			Assert.hasText(id, "参数id不能为空");
			String user = UserSecurityUtil.getSystemUserName();
			ShopInputDto shopDto = new ShopInputDto();
			shopDto.setId(id);
			shopDto.setStatus(Constant.SHOP_CLOSE);
			shopDto.setUpdateUser(user);
			shopDto.setUpdateTime(new Date());
			count = shopBackgroundApi.update(shopDto);
		} catch (Exception ex) {
			logger.error("关闭店铺异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS,count);
	}
}
