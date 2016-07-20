/*
 * 版本信息

 * 日期 2016-05-19 09:43:26

 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.basicset.controller;

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
import com.yougou.wfx.basicset.api.background.IAppVersionBackgroundApi;
import com.yougou.wfx.basicset.api.front.IVersionManageFrontApi;
import com.yougou.wfx.basicset.dto.input.AppVersionInputDto;
import com.yougou.wfx.basicset.dto.input.AppVersionPageInputDto;
import com.yougou.wfx.basicset.dto.output.AppVersionOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.dto.base.WFXResult;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.basicset.vo.AppVersionPageVo;
import com.yougou.wfx.system.api.IFileUploadApi;

/**
 * AppVersionController
 * 
 * @author wfx
 * @Date 创建时间：2016-05-19 09:43:27
 */
@Controller
@RequestMapping("/basicSet")
public class AppVersionController extends BaseController {

	@Resource
	private IAppVersionBackgroundApi appVersionBackgroundApi;
	@Resource
	private IFileUploadApi fileUploadApi;
	/*@Resource
	private IVersionManageFrontApi versionManageFrontApi;*/

	/**
	 * 进入菜单
	 */
	@RequestMapping("/appVersion")
	public String appVersionList() {
		//AppVersionOutputDto AppVersionOutputDto = versionManageFrontApi.getAndroidNewestVersion();
		return "/manage/basicset/appVersionList";
	}

	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryAppVersionData")
	@ResponseBody
	public String queryAppVersionData(ModelMap map, AppVersionPageVo appVersionVo, PageModel pageModel) throws Exception {
		AppVersionPageInputDto pageInputDto = (AppVersionPageInputDto) BeanUtil.convertBean(appVersionVo, AppVersionPageInputDto.class);
		PageModel<AppVersionOutputDto> result = appVersionBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}

	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getAppVersionById")
	@ResponseBody
	public String getAppVersionById(String id) throws Exception {
		AppVersionOutputDto appVersion = new AppVersionOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			appVersion = appVersionBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, appVersion);
	}

	/**
	 * 通过主键id删除数据
	 */
	@RequestMapping("/removeAppVersion")
	@ResponseBody
	public String removeAppVersion(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			appVersionBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}

	/**
	 * 保存或修改数据
	 */
	@RequestMapping("/saveAppVersion")
	@ResponseBody
	public String saveAppVersion(AppVersionInputDto appVersion) {
		try {
			String id = appVersion.getId();
			if (StringUtils.isNotBlank(id)) {
				appVersionBackgroundApi.update(appVersion);
			} else {
				appVersionBackgroundApi.insert(appVersion);
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
	@RequestMapping("/insertAppVersion")
	@ResponseBody
	public String insertAppVersion(AppVersionInputDto appVersion) {
		try {
			appVersionBackgroundApi.insert(appVersion);
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}

	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/updateAppVersion")
	@ResponseBody
	public String updateAppVersion(AppVersionInputDto appVersion) {
		try {
			Assert.hasText(appVersion.getId(), "参数id不能为空");
			appVersionBackgroundApi.update(appVersion);
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}

	@ResponseBody
	@RequestMapping(value = "/uploadAppApk", method = RequestMethod.POST)
	public String uploadImg(@RequestParam MultipartFile file, HttpServletRequest request, String name, String saleCatId) throws Exception {
		try {
			WFXResult<String> result = fileUploadApi.appApkUpload(name, file.getInputStream());
			int code = result.getResultCode();
			if (code == 0) {
				return jsonResult(StateCode.SUCCESS, result.getResult());
			} else {
				return jsonResult(StateCode.ERROR, code + "");
			}
		} catch (Exception e) {
			logger.error("上传apk出错。", e);
			return jsonResult(StateCode.ERROR, "20");
		}
	}
}
