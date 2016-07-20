 /*
 * 版本信息
 
 * 日期 2016-03-31 16:07:39
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.cms.controller;

import java.net.URLEncoder;
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
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.dto.base.WFXResult;
import com.yougou.wfx.enums.CarouseFigureTypeEnum;
import com.yougou.wfx.enums.RedirectTypeEnum;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.cms.vo.CarouselFigurePageVo;
import com.yougou.wfx.system.api.IFileUploadApi;
import com.yougou.wfx.system.api.IWFXSystemApi;
import com.yougou.wfx.util.UserSecurityUtil;
import com.yougou.wfx.cms.api.background.ICarouselFigureBackgroundApi;
import com.yougou.wfx.cms.api.background.ICarouselFigureBackgroundApi;
import com.yougou.wfx.cms.api.front.ICarouselFigureFrontApi;
import com.yougou.wfx.cms.dto.input.CarouselFigureInputDto;
import com.yougou.wfx.cms.dto.input.CarouselFigurePageInputDto;
import com.yougou.wfx.cms.dto.output.CarouselFigureOutputDto;


/**
 * CmsCarouselFigureController
 * @author zhangfeng
 * @Date 创建时间：2016-03-31 16:07:39
 */
@Controller
@RequestMapping("/cms")
public class CarouselFigureController extends BaseController{
	
	@Resource
	private ICarouselFigureBackgroundApi carouselFigureBackgroundApi;
	
	@Resource
	private IFileUploadApi fileUploadApi;
	
	@Resource
	private IWFXSystemApi wfxSystemApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/carouselFigureList")
	public String carouselfigureList(ModelMap map){
		String imgBaseUrl = wfxSystemApi.obtainImgBaseUrl();
		map.put("imgBaseUrl", imgBaseUrl);
		map.put("carouseFigureTypeEnumList", CarouseFigureTypeEnum.values());
		map.put("noticeRedirectTypeEnumList", RedirectTypeEnum.values());
		return "/manage/cms/carouselfigureList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryCarouselfigureData")
	@ResponseBody
	public String queryCarouselfigureData(ModelMap map, CarouselFigurePageVo cmsCarouselFigureVo, PageModel<CarouselFigureOutputDto> pageModel)throws Exception{
		CarouselFigurePageInputDto pageInputDto = (CarouselFigurePageInputDto) BeanUtil.convertBean(cmsCarouselFigureVo, CarouselFigurePageInputDto.class);
		PageModel<CarouselFigureOutputDto> result = carouselFigureBackgroundApi.findPage(pageInputDto, pageModel);
	
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 获取有效的最大轮播图排序号
	 */
	@RequestMapping("/getLimitInfo")
	@ResponseBody
	public String getLimitInfo(String type) {
		Map<String,Integer> limitInfo= carouselFigureBackgroundApi.getLimitInfo( type);
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, limitInfo);
	}
	
	/**
	 * 删除轮播图
	 */
	@RequestMapping("/removeCarouselfigure")
	@ResponseBody
	public String removeCarouselfigure(CarouselFigureInputDto cmsCarouselFigure) {
		try {
			Assert.hasText(cmsCarouselFigure.getId(), "参数id不能为空");
			cmsCarouselFigure.setDeleteFlag(1);// 删除标记 改为删除
			int count = carouselFigureBackgroundApi.update(cmsCarouselFigure);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 批量删除轮播图
	 * @param ids
	 * @return
	 */
	@RequestMapping("/batchRemoveCarouselfigure")
	@ResponseBody
	public String batchRemoveCarouselfigure(String ids) {
		try {
			Assert.hasText(ids, "参数id不能为空");			
			if(StringUtils.isNotBlank(ids)){
				String idsStr = ids.substring(0, ids.length()-1);
				String[] idArray = idsStr.split(",");
				for(String idStr : idArray){
					CarouselFigureInputDto carouselFigure = new CarouselFigureInputDto ();
					carouselFigure.setId(idStr);
					carouselFigure.setDeleteFlag(1);// 删除标记 改为删除
					carouselFigureBackgroundApi.update(carouselFigure);
				}
				
			}			
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	
	/**
	 * 上传轮播图图片
	 * @param file
	 * @param request
	 * @param name
	 * @param carouselFigureId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "uploadCarouselImg", method = RequestMethod.POST)
	public String uploadCarouselImg(@RequestParam MultipartFile file, HttpServletRequest request, String name, String carouselFigureId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			WFXResult<String> result = fileUploadApi.uploadCarouselFigureImg(carouselFigureId, name, file.getInputStream());
			String imgBaseUrl = wfxSystemApi.obtainImgBaseUrl();
			resultMap.put("resultCode", result.getResultCode());// resultCode：0 图片成功上传
			String msg = result.getResultMsg();
			if(StringUtils.isNotBlank(msg)){
				resultMap.put("resultMsg", URLEncoder.encode(result.getResultMsg(),"utf-8")); // resultCode非0,上传失败，msg 有值，否则无值		
			}
			resultMap.put("imgPath", result.getResult()); // resultCode：0 ，返回图片路径，否则无值
			resultMap.put("imgBasePath", imgBaseUrl);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(jsonResult(StateCode.SUCCESS, SUCCESS, resultMap));
		return jsonResult(StateCode.SUCCESS, SUCCESS, resultMap);
	}
	
	/**
	 * 新增/编辑轮播图
	 * @param cmsCarouselFigure
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addCarouselfigure")
	@ResponseBody
	public String addCarouselfigure(CarouselFigureInputDto carouselFigure, HttpServletRequest request) throws Exception {

		try {
			//carouselFigure.setUpdateTime(new Date());
			String user = UserSecurityUtil.getSystemUserName();
			//修改
			if(StringUtils.isNotBlank(carouselFigure.getId())){
				carouselFigure.setUpdateUser(user);
				carouselFigure.setUpdateTime(new Date());
				carouselFigureBackgroundApi.update(carouselFigure);				
			}else{ //新增
				carouselFigure.setDeleteFlag(2);
				carouselFigure.setStatus(2);				
				carouselFigure.setUpdateUser(user);
				carouselFigure.setUpdateTime(new Date());
				carouselFigure.setCreateTime(new Date());
				carouselFigureBackgroundApi.insert(carouselFigure);
			}
		} catch (Exception e) {
			logger.error("新增轮播图数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 修改轮播图
	 * @param cmsCarouselFigure
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping("/updateCarouselfigure")
	@ResponseBody
	public String updateCarouselfigure(CarouselFigureInputDto carouselFigure) throws Exception {

		try {
			carouselFigure.setUpdateTime(new Date());
			String user = UserSecurityUtil.getSystemUserName();
			carouselFigure.setUpdateUser(user);
			carouselFigureBackgroundApi.update(carouselFigure);
		} catch (Exception e) {
			logger.error("修改轮播图数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}*/
}
