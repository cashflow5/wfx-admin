 /*
 * 版本信息
 
 * 日期 2016-06-21 17:57:54
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.commodity.controller;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.commodity.api.background.ICommodityCortexBackgroundApi;
import com.yougou.wfx.commodity.dto.input.CommodityCortexInputDto;
import com.yougou.wfx.commodity.dto.input.CommodityCortexPageInputDto;
import com.yougou.wfx.commodity.dto.output.CommodityCortexOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.dto.base.WFXResult;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.commodity.vo.CommodityCortexPageVo;
import com.yougou.wfx.system.api.IFileUploadApi;
import com.yougou.wfx.system.api.IWFXSystemApi;

/**
 * CommodityCortexController
 * @author wfx
 * @Date 创建时间：2016-06-21 17:57:55
 */
@Controller
@RequestMapping("/commodity")
public class CommodityCortexController extends BaseController{
	
	@Resource
	private ICommodityCortexBackgroundApi commodityCortexBackgroundApi;
	@Resource
	private IFileUploadApi fileUploadApi;
	@Resource
	private IWFXSystemApi wfxSystemApi;
	@ResponseBody
	@RequestMapping(value = "/uploadCortexEdit", method = RequestMethod.POST)
	public String uploadArticleEdit(@RequestParam MultipartFile imgFile, HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			InputStream in = imgFile.getInputStream();
			if(in.available() > 1024*800){
				resultMap.put("error", 1);
				resultMap.put("message", URLEncoder.encode("图片大小不能超过800k。","utf-8"));
				return JSONObject.toJSONString(resultMap);
			}
			WFXResult<String> result = fileUploadApi.uploadCortexImg(imgFile.getOriginalFilename(),imgFile.getInputStream());
			String imgBaseUrl = wfxSystemApi.obtainImgBaseUrl();
			int error = result.getResultCode();
			if(0==error){// resultCode：0 图片成功上传
				resultMap.put("error", error);
				resultMap.put("url",imgBaseUrl + "/" + result.getResult());
			}else{
				resultMap.put("error", 1);
				resultMap.put("message", URLEncoder.encode(result.getResultMsg(),"utf-8"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("error", 1);
			try {
				resultMap.put("message",URLEncoder.encode("上传图片出错","utf-8"));
			} catch (UnsupportedEncodingException e1) {
			}
		}
		return JSONObject.toJSONString(resultMap);
	}
	/**
	 * 进入菜单
	 */
	@RequestMapping("/cortexList")
	public String propList(){
		return "/manage/commodity/cortexList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryCortexData")
	@ResponseBody
	public String queryPropData(ModelMap map, CommodityCortexPageVo commodityCortexVo, PageModel pageModel)throws Exception{
		CommodityCortexPageInputDto pageInputDto = (CommodityCortexPageInputDto) BeanUtil.convertBean(commodityCortexVo, CommodityCortexPageInputDto.class);
		PageModel<CommodityCortexOutputDto> result = commodityCortexBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getCortexById")
	@ResponseBody
	public String getPropById(String id) throws Exception {
		CommodityCortexOutputDto commodityCortex = new CommodityCortexOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			commodityCortex = commodityCortexBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, commodityCortex);
	}
	
	/**
	 * 通过主键id删除数据
	 */
	@RequestMapping("/removeCortex")
	@ResponseBody
	public String removeProp(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			commodityCortexBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 保存或修改数据
	 */
	@RequestMapping("/saveCortex")
	@ResponseBody
	public String saveProp(CommodityCortexInputDto commodityCortex) {
		try {
			String id = commodityCortex.getId();
			commodityCortex.setLastOperateUser(getSysUserName());
			commodityCortex.setLastOperateTime(new Date());
			if (StringUtils.isNotBlank(id)) {
				if(commodityCortex.getDescription()!=null&&!commodityCortex.getDescription().equals("")){
					commodityCortex.setIsNotDescription(100);
				}
				commodityCortexBackgroundApi.update(commodityCortex);
			}else{
				commodityCortexBackgroundApi.insert(commodityCortex);
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
	@RequestMapping("/insertCortex")
	@ResponseBody
	public String insertProp(CommodityCortexInputDto commodityCortex) {
		try {
			commodityCortexBackgroundApi.insert(commodityCortex);
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/updateCortex")
	@ResponseBody
	public String updateProp(CommodityCortexInputDto commodityCortex) {
		try {
			Assert.hasText(commodityCortex.getId(), "参数id不能为空");
			commodityCortexBackgroundApi.update(commodityCortex);
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
}
