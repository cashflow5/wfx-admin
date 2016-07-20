 /*
 * 版本信息
 
 * 日期 2016-03-24 11:14:00
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.commodity.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.commodity.api.background.ICommodityCatb2cBackgroundApi;
import com.yougou.wfx.commodity.dto.input.CommodityCatb2cInputDto;
import com.yougou.wfx.commodity.dto.input.CommodityCatb2cPageInputDto;
import com.yougou.wfx.commodity.dto.output.CommodityCatb2cOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.commodity.vo.CommodityCatb2cPageVo;
import com.yougou.wfx.system.model.BaseZtree;
import com.yougou.wfx.util.UserSecurityUtil;

/**
 * CommodityCatb2cController
 * @author wzf
 * @Date 创建时间：2016-03-24 11:14:01
 */
@Controller
@RequestMapping("/commodity")
public class CommodityCatb2cController extends BaseController{
	
	@Resource
	private ICommodityCatb2cBackgroundApi commodityCatb2cBackgroundApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/basicCatList")
	public String basicCatList(){
		return "/manage/commodity/basicCatList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryBasicCatData")
	@ResponseBody
	public String queryBasicCatData(ModelMap map, CommodityCatb2cPageVo commodityCatb2cVo, PageModel<CommodityCatb2cOutputDto> pageModel)throws Exception{
		CommodityCatb2cPageInputDto pageInputDto = (CommodityCatb2cPageInputDto) BeanUtil.convertBean(commodityCatb2cVo, CommodityCatb2cPageInputDto.class);
		PageModel<CommodityCatb2cOutputDto> result = commodityCatb2cBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 查询父分类下的所有子分类
	 */
	@RequestMapping("/queryBasicCatListByPid")
	public String queryBasicCatListByPid(ModelMap map, String parentId)throws Exception{
		CommodityCatb2cOutputDto basicCat = new CommodityCatb2cOutputDto();
		if(StringUtils.isBlank(parentId) || "0".equals(parentId)){
			basicCat.setCatName("全部");
		}else{
			basicCat = commodityCatb2cBackgroundApi.getById(parentId);
		}
		CommodityCatb2cInputDto catInputDto = new CommodityCatb2cInputDto();
		catInputDto.setParentId(parentId);
		List<CommodityCatb2cOutputDto> catList = commodityCatb2cBackgroundApi.queryList(catInputDto);
		map.put("basicCat", basicCat);
		map.put("catList", catList);
		return "/manage/commodity/basicCatTable";
	}
	
	/**
	 * 通过父id获取html
	 */
	@RequestMapping("/getCatHtmlByParentId")
	public String getCatHtmlByParentId(ModelMap map, String parentId)throws Exception{
		CommodityCatb2cInputDto catInputDto = new CommodityCatb2cInputDto();
		catInputDto.setParentId(parentId);
		List<CommodityCatb2cOutputDto> catList = commodityCatb2cBackgroundApi.queryList(catInputDto);
		map.put("catList", catList);
		return "/manage/commodity/basicCatSelectHtml";
	}
	
	/**
	 * 查询基础分类的数据，并组装成Ztree需要的格式
	 */
	@RequestMapping("/getCatJsonForZtree")
	@ResponseBody
	public String getCatJsonForZtree(String id,String name,Integer level)throws Exception{
		List<BaseZtree> baseZtreeList = new ArrayList<BaseZtree>();
		CommodityCatb2cInputDto commodityCatb2c = new CommodityCatb2cInputDto();
		if(StringUtils.isBlank(id)){
			//基础分类顶层根目录
			BaseZtree baseZtree = new BaseZtree("0", null, "全部", "全部", "root", 
					"true", "false", "root", "", "根目录", "1", null);
			baseZtreeList.add(baseZtree);
			commodityCatb2c.setLevel(1);
		}else{
			commodityCatb2c.setParentId(id);
		}
		List<CommodityCatb2cOutputDto> commodityCatb2cList = commodityCatb2cBackgroundApi.queryList(commodityCatb2c);
		List<BaseZtree> ztreeCatDtoList = this.catToZtree(commodityCatb2cList);
		baseZtreeList.addAll(ztreeCatDtoList);
		return JSONObject.toJSONString(baseZtreeList);
	}
	
	@RequestMapping("/getBasicCatById")
	@ResponseBody
	public String getBasicCatById(String id) throws Exception {
		CommodityCatb2cOutputDto commodityCatb2c = new CommodityCatb2cOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			commodityCatb2c = commodityCatb2cBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, commodityCatb2c);
	}
	
	@RequestMapping("/removeBasicCat")
	@ResponseBody
	public String removeBasicCat(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			commodityCatb2cBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	@RequestMapping("/basicCatCheck")
	@ResponseBody
	public String basicCatCheck(@RequestParam("id") String id,String type) {
		Map<String,String> map = new HashMap<String,String>();
		//只有停用状态的分类才可能删除，先判断停用状态
		CommodityCatb2cOutputDto outputDto = commodityCatb2cBackgroundApi.getById(id);
		if(null != outputDto){
			if(null != type && "status".equals(type)){//查询订单的状态
				Integer status = outputDto.getStatus();
				if(null != status && status == 0){
					map.put("code", "enable");
					map.put("message", "该分类为停用状态");
				}else{
					map.put("code", "disable");
					map.put("message", "不能删除启用状态的分类");
				}
			}
		}else{
			map.put("code", "disable");
			map.put("message", "删除出错");
		}
		return JSONObject.toJSONString(map);
	}
	
	@RequestMapping("/addBasicCat")
	@ResponseBody
	public String addBasicCat(String pId, String name,String strLevel) {
		CommodityCatb2cInputDto commodityCatb2c = new CommodityCatb2cInputDto();
		commodityCatb2c.setParentId(pId);
		commodityCatb2c.setCatName(name);
		Integer level = 1;
		if(StringUtils.isNotBlank(strLevel)){
			level = Integer.parseInt(strLevel) + 1;
		}
		//设置等级
		commodityCatb2c.setLevel(level);
		Date date = new Date();
		commodityCatb2c.setUpdatePerson(UserSecurityUtil.getSystemUserName());
		commodityCatb2c.setCreateDate(date);
		commodityCatb2c.setUpdateDate(date);
		commodityCatb2c.setUpdateTimestamp(date.getTime());
		commodityCatb2c.setIsEnabled(1);
		commodityCatb2c.setDeleteFlag(0);
		commodityCatb2c.setInterestedRateOrder(0D);
		commodityCatb2c.setStatus(1);
		String id = commodityCatb2cBackgroundApi.insert(commodityCatb2c);
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("rtype", "menu");
		map.put("level", level+"");
		return JSONObject.toJSONString(map);
	}
	
	@RequestMapping("/updateBasicCat")
	@ResponseBody
	public String updateBasicCat(CommodityCatb2cInputDto commodityCatb2c) {
		Integer status = commodityCatb2c.getStatus();
		//status==0表示要停用分类，停用时，需要判断是否还有上架销售的商品，若有，则不能停用
		if(null != status && status == 0){
			CommodityCatb2cOutputDto outputDto = commodityCatb2cBackgroundApi.getById(commodityCatb2c.getId());
			CommodityCatb2cInputDto inputDto = (CommodityCatb2cInputDto)BeanUtil.convertBean(outputDto, CommodityCatb2cInputDto.class);
			boolean hasComm = commodityCatb2cBackgroundApi.hasCommodityOnCat(inputDto);
			if(hasComm){
				return super.jsonResult(StateCode.ERROR, "该分类下存在在售的商品，不能停用");
			}
		}
		Date date = new Date();
		commodityCatb2c.setUpdatePerson(UserSecurityUtil.getSystemUserName());
		commodityCatb2c.setUpdateDate(date);
		commodityCatb2c.setUpdateTimestamp(date.getTime());
		commodityCatb2cBackgroundApi.update(commodityCatb2c);
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	@RequestMapping("/saveBasicCat")
	@ResponseBody
	public String saveBasicCat(CommodityCatb2cInputDto commodityCatb2c) {
		try {
			String id = commodityCatb2c.getId();
			if (StringUtils.isNotBlank(id)) {
				commodityCatb2cBackgroundApi.update(commodityCatb2c);
			}else{
				commodityCatb2cBackgroundApi.insert(commodityCatb2c);
			}
		} catch (Exception e) {
			logger.error("保存数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	public List<BaseZtree> catToZtree(List<CommodityCatb2cOutputDto> commodityCatb2cList){
		List<BaseZtree> baseZtreeList = new ArrayList<BaseZtree>();
		if(null != commodityCatb2cList && commodityCatb2cList.size() > 0){
			BaseZtree baseZtree = null;
			CommodityCatb2cInputDto inputDto = null;
			for(CommodityCatb2cOutputDto catDto:commodityCatb2cList){
				baseZtree = new BaseZtree();
				inputDto = new CommodityCatb2cInputDto();
				//用于查询该分类下面是否有子分类
				baseZtree.setId(catDto.getId());
				baseZtree.setPId(catDto.getParentId());
				baseZtree.setName(catDto.getCatName());
				baseZtree.setT(catDto.getCatName());
				Integer level = catDto.getLevel();
				String type = "folder";
				String isParent = "true";
				String isFlage = "1";
				
				if(null != level && level > 2){
					type = "menu";
					isParent = "false";
					isFlage = "0";
				}else{
					//查询每个分类下的子分类数量
					inputDto.setParentId(catDto.getId());
					int num = commodityCatb2cBackgroundApi.findPageCount(inputDto);
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
				baseZtree.setDescription(catDto.getStructCatName());
				baseZtree.setIsFlag(isFlage);//是否启用
				baseZtree.setIsParent(isParent);
				baseZtreeList.add(baseZtree);
			}
		}
		return baseZtreeList;
	}
}
