 /*
 * 版本信息
 
 * 日期 2016-04-07 18:29:33
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.basicset.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.basicset.api.background.ISystemmgAreaBackgroundApi;
import com.yougou.wfx.basicset.dto.input.SystemmgAreaInputDto;
import com.yougou.wfx.basicset.dto.input.SystemmgAreaPageInputDto;
import com.yougou.wfx.basicset.dto.output.SystemmgAreaOutputDto;
import com.yougou.wfx.commodity.api.background.ISysLogBackgroundApi;
import com.yougou.wfx.commodity.dto.input.SysLogInputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.enums.BusinessTypeEnum;
import com.yougou.wfx.enums.OperateTypeEnum;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.basicset.vo.SystemmgAreaPageVo;
import com.yougou.wfx.system.model.BaseZtree;
import com.yougou.wfx.util.Constant;

/**
 * SystemmgAreaController
 * @author wfx
 * @Date 创建时间：2016-04-07 18:29:33
 */
@Controller
@RequestMapping("/basicSet")
public class SystemmgAreaController extends BaseController{
	
	@Resource
	private ISystemmgAreaBackgroundApi systemmgAreaBackgroundApi;
	@Resource
	private ISysLogBackgroundApi sysLogBackgroundApi;
	/**
	 * 进入菜单
	 */
	@RequestMapping("/areaMgt")
	public String areaMgtList(){
		return "/manage/basicset/areaMgt";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryAreaMgtData")
	@ResponseBody
	public String queryAreaMgtData(ModelMap map, SystemmgAreaPageVo systemmgAreaVo, PageModel pageModel)throws Exception{
		SystemmgAreaPageInputDto pageInputDto = (SystemmgAreaPageInputDto) BeanUtil.convertBean(systemmgAreaVo, SystemmgAreaPageInputDto.class);
		PageModel<SystemmgAreaOutputDto> result = systemmgAreaBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getAreaMgtById")
	@ResponseBody
	public String getAreaMgtById(String id) throws Exception {
		SystemmgAreaOutputDto systemmgArea = new SystemmgAreaOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			systemmgArea = systemmgAreaBackgroundApi.getById(id.split(",")[1]);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, systemmgArea);
	}
	
	/**
	 * 通过主键id删除数据
	 */
	@RequestMapping("/removeAreaMgt")
	@ResponseBody
	public String removeAreaMgt(@RequestParam("id") String id,HttpServletRequest request) {
		try {
			Assert.hasText(id, "参数id不能为空");
			SystemmgAreaOutputDto systemmgAreaOld = systemmgAreaBackgroundApi.getById(id.split(",")[1]);
			if(systemmgAreaOld == null){
				return super.jsonResult(StateCode.ERROR, "发生异常,地区不存在");
			}
			systemmgAreaBackgroundApi.removeCurrentAndSubArea(systemmgAreaOld.getId(),systemmgAreaOld.getNo());
			//记录日志
			String content ="删除地区：【"+systemmgAreaOld.getName()+"】";
			addSystemLog(content, OperateTypeEnum.DELETE.getKey(), request);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 保存或修改数据
	 */
	@RequestMapping("/saveAreaMgt")
	@ResponseBody
	public String saveAreaMgt(SystemmgAreaInputDto systemmgArea) {
		try {
			String id = systemmgArea.getId();
			if (StringUtils.isNotBlank(id)) {
				SystemmgAreaInputDto inputDto = new SystemmgAreaInputDto();
				String no = systemmgArea.getNo();
				inputDto.setNo(StringUtils.substring(no, 0, no.lastIndexOf("-")+1));
				List<SystemmgAreaOutputDto>  lstSystemArea = systemmgAreaBackgroundApi.queryList(inputDto);
				for(SystemmgAreaOutputDto outputDto : lstSystemArea){
					if(!outputDto.getId().equals(id) && outputDto.getSort().equals(systemmgArea.getSort())){
						return super.jsonResult(StateCode.ERROR, "同级地区排序号不能重复");
					}
				}
				
				systemmgAreaBackgroundApi.update(systemmgArea);
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
	@RequestMapping("/insertAreaMgt")
	@ResponseBody
	public String insertAreaMgt(SystemmgAreaInputDto systemmgArea) {
		try {
			systemmgAreaBackgroundApi.insert(systemmgArea);
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/updateAreaMgt")
	@ResponseBody
	public String updateAreaMgt(SystemmgAreaInputDto systemmgArea,HttpServletRequest request) {
		try {
			Assert.hasText(systemmgArea.getId(), "参数id不能为空");
			systemmgArea.setId(systemmgArea.getId().split(",")[1]);
			SystemmgAreaOutputDto systemmgAreaOld = systemmgAreaBackgroundApi.getById(systemmgArea.getId());
			
			systemmgAreaBackgroundApi.update(systemmgArea);
			//添加操作日志
			if(!StringUtils.equals(systemmgAreaOld.getName(), systemmgArea.getName())){
				String content ="原地区：【"+systemmgAreaOld.getName()+"】\r\n修改后：【"+systemmgArea.getName()+"】";
				addSystemLog(content, OperateTypeEnum.EDIT.getKey(), request);
			}
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 查询销售分类的数据，并组装成Ztree需要的格式
	 */
	@RequestMapping("/getAreaJsonForZtree")
	@ResponseBody
	public String getCatJsonForZtree(String id, String name,Integer level)throws Exception{
		List<BaseZtree> ztreeDtoList = new ArrayList<BaseZtree>();
		SystemmgAreaInputDto inputDto = new SystemmgAreaInputDto();
		if(StringUtils.isBlank(id)){
			//地区顶层根目录
			BaseZtree ztreeDto = new BaseZtree("0", null, "全部", "全部", "root", 
					"true", "false", "root", "", "根目录", "1", null);
			ztreeDtoList.add(ztreeDto);
			inputDto.setLevel("1");
		}else{
			String[] areaInfo = id.split(",");
			String no = areaInfo[0];
			int len = no.split("-").length;
			inputDto.setNo(no+"-");
			inputDto.setLevel(len+"");
		}
		List<SystemmgAreaOutputDto> outputList = systemmgAreaBackgroundApi.queryList(inputDto);
		List<BaseZtree> ztreeCatDtoList = this.catToZtree(outputList);
		ztreeDtoList.addAll(ztreeCatDtoList);
		return JSONObject.toJSONString(ztreeDtoList);
	}
	
	/**
	 * 将销售分类列表转换为json对象列表
	 * @param commodityCatb2cList
	 * @return
	 */
	public List<BaseZtree> catToZtree(List<SystemmgAreaOutputDto> areaList){
		List<BaseZtree> ztreeDtoList = new ArrayList<BaseZtree>();
		if(null != areaList && areaList.size() > 0){
			BaseZtree ztreeDto = null;
			SystemmgAreaInputDto inputDto = null;
			for(SystemmgAreaOutputDto catDto:areaList){
				ztreeDto = new BaseZtree();
				inputDto = new SystemmgAreaInputDto();
				//用于查询该分类下面是否有子分类
				ztreeDto.setId(catDto.getNo()+","+catDto.getId());
				if("1".equals(catDto.getLevel())){
					ztreeDto.setPId("0");
				}
				ztreeDto.setName(catDto.getName());
				ztreeDto.setT(catDto.getName());
				String level = catDto.getLevel();
				String type = "folder";
				String isParent = "true";
				String isFlage = "1";
				
				if(null != level && level.compareTo("2") > 0){
					type = "menu";
					isParent = "false";
					isFlage = "0";
				}else{
					//查询每个分类下的子分类数量
					inputDto.setNo(catDto.getNo()+"-");
					int num = systemmgAreaBackgroundApi.findPageCount(inputDto);
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
				ztreeDto.setRtype(type);
				ztreeDto.setClick("false");
				ztreeDto.setIconSkin(type);
				ztreeDto.setUrl("");
				ztreeDto.setDescription(catDto.getName());
				ztreeDto.setIsFlag(isFlage);//是否启用
				ztreeDto.setIsParent(isParent);
				ztreeDtoList.add(ztreeDto);
			}
		}
		return ztreeDtoList;
	}
	
	@RequestMapping("/addArea")
	@ResponseBody
	public String addArea(String pId, String name,String strLevel,HttpServletRequest request){
		
		//添加操作日志
		String content ="新增地区：【"+name+"】";
		addSystemLog(content, OperateTypeEnum.ADD.getKey(), request);
		
		SystemmgAreaInputDto inputDto = new SystemmgAreaInputDto();
		pId = "0".equals(pId) ? "root" : pId;
		//设置新生成分类的等级
		Integer level = 1;
		if(StringUtils.isNotBlank(strLevel)){
			level = Integer.parseInt(strLevel) + 1;
		}
		
		inputDto.setLevel(level+"");
		inputDto.setNo(pId.split(",")[0]+"-");
		List<SystemmgAreaOutputDto> outputList = systemmgAreaBackgroundApi.queryList(inputDto);
		int bigestNo = 0;
		int bigestSort = 0;
		for(SystemmgAreaOutputDto outputDto : outputList){
			String no = StringUtils.substring(outputDto.getNo(), StringUtils.lastIndexOf(outputDto.getNo(), "-")+1);
			if(StringUtils.isNotBlank(no)){
				int noIntVal =  Integer.parseInt(no);
				bigestNo = bigestNo > noIntVal ? bigestNo : noIntVal;
			}
			if(StringUtils.isNotBlank(outputDto.getSort())){
				int sortIntVal =  Integer.parseInt(outputDto.getSort());
				bigestSort = bigestSort > sortIntVal ? bigestSort : sortIntVal;
			}
		}
		inputDto.setNo(inputDto.getNo()+(bigestNo+1));
		inputDto.setSort((bigestSort+1)+"");
		inputDto.setName(name);
		if(level ==3){
			inputDto.setIsleaf("1");
		}else{
			inputDto.setIsleaf("0");
		}
		inputDto.setIsUsable(1);
		inputDto.setId("");
		String id = systemmgAreaBackgroundApi.insert(inputDto);
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", inputDto.getNo()+","+id);
		map.put("rtype", "menu");
		map.put("level", level+"");
		return JSONObject.toJSONString(map);
	}

	private void addSystemLog(String content,String type, HttpServletRequest request) {
		try{
			SysLogInputDto sysLogDto = new SysLogInputDto();
			sysLogDto.setModule(Constant.MODULE_BASIC_SET);
			sysLogDto.setBusinessType(BusinessTypeEnum.SYSTEM_AREA.getKey());
			sysLogDto.setOperateUser(getSysUserName());
			sysLogDto.setOperateAccount(getSysUserName());
			sysLogDto.setOperateType(type);
			sysLogDto.setOperateDate(new Date());
			sysLogDto.setOperatorIp(request.getRemoteAddr());		
			sysLogDto.setOperateContent(content);
			sysLogBackgroundApi.insertSysLog(sysLogDto);
		}catch(Exception e){
			//暂不处理
		}
	}
}
