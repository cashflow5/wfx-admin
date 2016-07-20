 /*
 * 版本信息
 
 * 日期 2016-04-09 13:01:36
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.basicset.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.basicset.api.background.ISysConfigBackgroundApi;
import com.yougou.wfx.basicset.dto.input.SysConfigInputDto;
import com.yougou.wfx.basicset.dto.input.SysConfigPageInputDto;
import com.yougou.wfx.basicset.dto.output.SysConfigOutputDto;
import com.yougou.wfx.commodity.api.background.ISysLogBackgroundApi;
import com.yougou.wfx.commodity.dto.input.SysLogInputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.enums.BusinessTypeEnum;
import com.yougou.wfx.enums.OperateTypeEnum;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.basicset.vo.SysConfigPageVo;
import com.yougou.wfx.util.Constant;
import com.yougou.wfx.util.UserSecurityUtil;

/**
 * SysConfigController
 * @author wfx
 * @Date 创建时间：2016-04-09 13:01:37
 */
@Controller
@RequestMapping("/basicSet")
public class SysConfigController extends BaseController{
	
	@Resource
	private ISysConfigBackgroundApi sysConfigBackgroundApi;
	@Resource
	private ISysLogBackgroundApi sysLogBackgroundApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/sysConfig")
	public String sysConfigList(){
		return "/manage/basicset/sysConfigList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/querySysConfigData")
	@ResponseBody
	public String querySysConfigData(ModelMap map, SysConfigPageVo sysConfigVo, PageModel pageModel)throws Exception{
		SysConfigPageInputDto pageInputDto = (SysConfigPageInputDto) BeanUtil.convertBean(sysConfigVo, SysConfigPageInputDto.class);
		Map<String, String> syncMap = new HashMap<String,String>();
		syncMap.put("orderBy", "create_time desc");
		pageInputDto.setSyncMap(syncMap);
		PageModel<SysConfigOutputDto> result = sysConfigBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getSysConfigById")
	@ResponseBody
	public String getSysConfigById(String id) throws Exception {
		SysConfigOutputDto sysConfig = new SysConfigOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			sysConfig = sysConfigBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, sysConfig);
	}
	
	/**
	 * 通过主键id删除数据
	 */
	@RequestMapping("/removeSysConfig")
	@ResponseBody
	public String removeSysConfig(HttpServletRequest request,String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			//添加操作日志
			SysLogInputDto sysLogDto = new SysLogInputDto();
			sysLogDto.setModule(Constant.MODULE_BASIC_SET);
			sysLogDto.setBusinessType(BusinessTypeEnum.SYSTEM_CONFIG.getKey());
			sysLogDto.setOperateUser(getSysUserName());
			sysLogDto.setOperateAccount(getSysUserName());
			sysLogDto.setOperateType(OperateTypeEnum.DELETE.getKey());
			sysLogDto.setOperateDate(new Date());
			sysLogDto.setOperatorIp(request.getRemoteAddr());
			//获取操作内容
			SysConfigOutputDto sysConfigDto = sysConfigBackgroundApi.getById(id);
			StringBuffer content = new StringBuffer();
			content.append("删除系统配置：\r\n")
			.append("配置键：【"+sysConfigDto.getConfigKey()+"】，配置键值：【"+sysConfigDto.getConfigValue()+"】");
			sysLogDto.setOperateContent(content.toString());
			sysLogBackgroundApi.insertSysLog(sysLogDto);
			sysConfigBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 保存或修改数据
	 */
	@RequestMapping("/saveSysConfig")
	@ResponseBody
	public String saveSysConfig(HttpServletRequest request,SysConfigInputDto sysConfig) {
		try {
			String id = sysConfig.getId();
			Date date = new Date();
			SysLogInputDto sysLogDto = new SysLogInputDto();
			sysLogDto.setModule(Constant.MODULE_BASIC_SET);
			sysLogDto.setBusinessType(BusinessTypeEnum.SYSTEM_CONFIG.getKey());
			sysLogDto.setOperateUser(getSysUserName());
			sysLogDto.setOperateAccount(getSysUserName());
			sysLogDto.setOperateDate(date);
			sysLogDto.setOperatorIp(request.getRemoteAddr());
			//获取操作内容
			StringBuffer content = new StringBuffer();
			sysConfig.setUpdateUser(getSysUserName());
			sysConfig.setUpdateTime(date);
			if (StringUtils.isNotBlank(id)) {
				sysLogDto.setOperateType(OperateTypeEnum.EDIT.getKey());
				SysConfigOutputDto sysConfigDto = sysConfigBackgroundApi.getById(id);
				content.append("修改系统配置：\r\n")
				.append("原配置键：【"+sysConfigDto.getConfigKey()+"】，配置键值：【"+sysConfigDto.getConfigValue()+"】\r\n")
				.append("修改后：\r\n")
				.append("配置键：【"+sysConfig.getConfigKey()+"】，配置键值：【"+sysConfig.getConfigValue()+"】");
				sysLogDto.setOperateContent(content.toString());
				sysConfigBackgroundApi.update(sysConfig);
			}else{
				sysLogDto.setOperateType(OperateTypeEnum.ADD.getKey());
				content.append("新增系统配置：\r\n")
				.append("配置键：【"+sysConfig.getConfigKey()+"】，配置键值：【"+sysConfig.getConfigValue()+"】");
				sysLogDto.setOperateContent(content.toString());
				sysConfig.setStatus(1);
				sysConfig.setCreateTime(date);
				sysConfigBackgroundApi.insert(sysConfig);
			}
			sysLogBackgroundApi.insertSysLog(sysLogDto);
		} catch (Exception e) {
			logger.error("保存数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 插入一条新数据，id会自动生成
	 */
	@RequestMapping("/insertSysConfig")
	@ResponseBody
	public String insertSysConfig(SysConfigInputDto sysConfig) {
		try {
			Date date = new Date();
			sysConfig.setStatus(1);
			sysConfig.setCreateTime(date);
			sysConfig.setUpdateUser(getSysUserName());
			sysConfig.setUpdateTime(date);
			sysConfigBackgroundApi.insert(sysConfig);
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/updateSysConfig")
	@ResponseBody
	public String updateSysConfig(SysConfigInputDto sysConfig) {
		try {
			Assert.hasText(sysConfig.getId(), "参数id不能为空");
			Date date = new Date();
			sysConfig.setUpdateUser(getSysUserName());
			sysConfig.setUpdateTime(date);
			sysConfigBackgroundApi.update(sysConfig);
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
}
