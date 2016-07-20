 /*
 * 版本信息
 
 * 日期 2016-06-02 09:59:24
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.discover.controller;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.basicset.api.background.ISysConfigBackgroundApi;
import com.yougou.wfx.discover.api.background.IDiscoverArticleBackgroundApi;
import com.yougou.wfx.discover.api.background.IDiscoverCarouselFigureBackgroundApi;
import com.yougou.wfx.discover.api.background.IDiscoverChannelBackgroundApi;
import com.yougou.wfx.discover.api.background.IDiscoverLogBackgroundApi;
import com.yougou.wfx.discover.dto.input.DiscoverArticleInputDto;
import com.yougou.wfx.discover.dto.input.DiscoverArticlePageInputDto;
import com.yougou.wfx.discover.dto.input.DiscoverCarouselFigurePageInputDto;
import com.yougou.wfx.discover.dto.input.DiscoverChannelInputDto;
import com.yougou.wfx.discover.dto.input.DiscoverLogInputDto;
import com.yougou.wfx.discover.dto.output.DiscoverArticleOutputDto;
import com.yougou.wfx.discover.dto.output.DiscoverCarouselFigureOutputDto;
import com.yougou.wfx.discover.dto.output.DiscoverChannelOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.dto.base.WFXResult;
import com.yougou.wfx.enums.DisLogBusinessTypeEnum;
import com.yougou.wfx.enums.DisLogOperTypeEnum;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.discover.vo.DiscoverArticlePageVo;
import com.yougou.wfx.system.api.IFileUploadApi;
import com.yougou.wfx.system.api.IWFXSystemApi;
import com.yougou.wfx.util.UserSecurityUtil;

/**
 * DiscoverArticleController
 * @author wang.zf
 * @Date 创建时间：2016-06-02 09:59:26
 */
@Controller
@RequestMapping("/discover")
public class DiscoverArticleController extends BaseController{
	
	@Resource
	private IDiscoverArticleBackgroundApi discoverArticleBackgroundApi;
	@Resource
	private IFileUploadApi fileUploadApi;
	@Resource
	private IWFXSystemApi wfxSystemApi;
	@Resource
	private IDiscoverChannelBackgroundApi discoverChannelBackgroundApi;
	@Resource
	private ISysConfigBackgroundApi sysConfigBackgroundApi;
	@Resource
	private IDiscoverLogBackgroundApi discoverLogBackgroundApi;
	@Resource
	private IDiscoverCarouselFigureBackgroundApi discoverCarouselFigureBackgroundApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/articleList")
	public String articleList(ModelMap map,String id,String type){
		DiscoverChannelInputDto channelInput = new DiscoverChannelInputDto();
		List<DiscoverChannelOutputDto> channelOutputList = discoverChannelBackgroundApi.getChannelList(channelInput);
		String picUrl = wfxSystemApi.obtainImgBaseUrl();
		String h5Domain = sysConfigBackgroundApi.getValueBykey("wfx.domain.h5");
		if(StringUtils.isBlank(h5Domain)){
			h5Domain = "http://m.yougou.net";
		}
		map.put("picUrl", picUrl);
		map.put("channelList", channelOutputList);
		map.put("h5Domain", h5Domain);
		if(StringUtils.isNotBlank(type) && "view".equals(type)){
			DiscoverArticleOutputDto article = null;
			DiscoverArticlePageInputDto input = new DiscoverArticlePageInputDto();
			input.setId(id);
			PageModel<DiscoverArticleOutputDto> articlePage = discoverArticleBackgroundApi.findInfoPage(input, new PageModel());
			List<DiscoverArticleOutputDto> outputList = articlePage.getItems();
			if(null != outputList && outputList.size() > 0){
				article = outputList.get(0);
			}
			if(null == article){
				article = new DiscoverArticleOutputDto();
			}
			map.put("article", article);
			return "/manage/discover/articleView";
		}else{
			return "/manage/discover/articleList";
		}
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryArticleData")
	@ResponseBody
	public String queryArticleData(ModelMap map, DiscoverArticlePageVo discoverArticleVo, PageModel pageModel)throws Exception{
		DiscoverArticlePageInputDto pageInputDto = (DiscoverArticlePageInputDto) BeanUtil.convertBean(discoverArticleVo, DiscoverArticlePageInputDto.class);
		pageInputDto.setDeleteFlag(1);
		PageModel<DiscoverArticleOutputDto> result = discoverArticleBackgroundApi.findInfoPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 进入回收站菜单
	 */
	@RequestMapping("/articleRecycle")
	public String articleRecycle(ModelMap map){
		DiscoverChannelInputDto input = new DiscoverChannelInputDto();
		List<DiscoverChannelOutputDto> outputList = discoverChannelBackgroundApi.getChannelList(input);
		map.put("channelList", outputList);
		return "/manage/discover/articleRecycle";
	}
	

	/**
	 * 进入文章选择页面
	 */
	@RequestMapping("/articleSelected")
	public String articleSelected(ModelMap map){
		DiscoverChannelInputDto input = new DiscoverChannelInputDto();
		List<DiscoverChannelOutputDto> outputList = discoverChannelBackgroundApi.getChannelList(input);
		map.put("channelList", outputList);
		return "/manage/discover/articleSelected";
	}
	
	/**
	 * 分页查询回收站数据，并返回json格式的结果
	 */
	@RequestMapping("/queryArticleRecycleData")
	@ResponseBody
	public String queryArticleRecycleData(ModelMap map, DiscoverArticlePageVo discoverArticleVo, PageModel pageModel)throws Exception{
		DiscoverArticlePageInputDto pageInputDto = (DiscoverArticlePageInputDto) BeanUtil.convertBean(discoverArticleVo, DiscoverArticlePageInputDto.class);
		pageInputDto.setDeleteFlag(2);
		PageModel<DiscoverArticleOutputDto> result = discoverArticleBackgroundApi.findInfoPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 分页查询回收站数据，并返回json格式的结果
	 */
	@RequestMapping("/queryArticleSelectedData")
	@ResponseBody
	public String queryArticleSelectedData(ModelMap map, DiscoverArticlePageVo discoverArticleVo, PageModel pageModel)throws Exception{
		DiscoverArticlePageInputDto pageInputDto = (DiscoverArticlePageInputDto) BeanUtil.convertBean(discoverArticleVo, DiscoverArticlePageInputDto.class);
		String authorAccount = pageInputDto.getAuthorAccount();
		if(StringUtils.isNotBlank(authorAccount) && "优购微零售".equals(StringUtils.trim(authorAccount))){
			pageInputDto.setAuthorType(1);
			pageInputDto.setAuthorAccount(null);
		}
		pageInputDto.setDeleteFlag(1);
		PageModel<DiscoverArticleOutputDto> result = discoverArticleBackgroundApi.findInfoPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	@RequestMapping("/goodsList")
	public String goodsList(){
		return "/manage/discover/goodsList";
	}
	
	@RequestMapping("/goodsLink")
	public String goodsLink(){
		return "/manage/discover/goodsLink";
	}

	@RequestMapping("/shopList")
	public String shopList(){
		return "/manage/discover/shopList";
	}
	
	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getArticleById")
	@ResponseBody
	public String getArticleById(String id) throws Exception {
		DiscoverArticleOutputDto discoverArticle = new DiscoverArticleOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			discoverArticle = discoverArticleBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, discoverArticle);
	}
	
	/**
	 * 通过主键id删除数据
	 */
	@RequestMapping("/removeArticle")
	@ResponseBody
	public String removeArticle(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			DiscoverLogInputDto log = this.getLogDto(DisLogBusinessTypeEnum.DIS_LOG_BIZ_RECYCLE, DisLogOperTypeEnum.DIS_LOG_OPER_DEL, null, id);
			if(null != log){
				discoverLogBackgroundApi.insert(log);
			}
			discoverArticleBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 保存或修改数据
	 */
	@RequestMapping("/saveArticle")
	@ResponseBody
	public String saveArticle(DiscoverArticleInputDto discoverArticle) {
		try {
			String id = discoverArticle.getId();
			Date date = new Date();
			discoverArticle.setOperUser(getSysUserName());
			discoverArticle.setUpdateTime(date);
			if (StringUtils.isNotBlank(id)) {
				List<DiscoverLogInputDto> logList = this.getUpdateLogDto(discoverArticle, id);
				if(null != logList && logList.size() > 0){
					for(DiscoverLogInputDto log:logList){
						discoverLogBackgroundApi.insert(log);
					}
				}
				discoverArticleBackgroundApi.update(discoverArticle);
			}else{
				discoverArticle.setCreateTime(date);
				discoverArticleBackgroundApi.insert(discoverArticle);
				DiscoverLogInputDto input = this.getLogDto(DisLogBusinessTypeEnum.DIS_LOG_BIZ_ARTICLE, DisLogOperTypeEnum.DIS_LOG_OPER_ADD, discoverArticle, id);
				if(null != input){
					discoverLogBackgroundApi.insert(input);
				}
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
	@RequestMapping("/insertArticle")
	@ResponseBody
	public String insertArticle(DiscoverArticleInputDto discoverArticle) {
		try {
			discoverArticleBackgroundApi.insert(discoverArticle);
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/updateArticle")
	@ResponseBody
	public String updateArticle(DiscoverArticleInputDto discoverArticle,String type) {
		try {
			String id = discoverArticle.getId();
			Assert.hasText(id, "参数id不能为空");
			if(StringUtils.isNotBlank(type)){
				if("article_delete".equals(type)){
					DiscoverArticleOutputDto input = discoverArticleBackgroundApi.getById(id);
					if(null == input){
						logger.error("文章不存在");
						return super.jsonResult(StateCode.ERROR, "该文章不存在");
					}
					if(null != input.getPublishStatus() && 2 == input.getPublishStatus()){
						return super.jsonResult(StateCode.ERROR, "该文章已发布，不能删除");
					}
					DiscoverCarouselFigurePageInputDto pageInputDto = new DiscoverCarouselFigurePageInputDto();
					pageInputDto.setArticleNo(input.getNo()+"");
					PageModel<DiscoverCarouselFigureOutputDto> cfPage = discoverCarouselFigureBackgroundApi.findPage(pageInputDto, new PageModel());
					int count = cfPage.getTotalCount();
					if(count > 0){
						return super.jsonResult(StateCode.ERROR, "该文章存在链接的轮播图，不能删除");
					}
					DiscoverLogInputDto log = this.getUpdateLog(id, type);
					if(null != log){
						discoverLogBackgroundApi.insert(log);
					}
				}else{
					DiscoverLogInputDto log = this.getUpdateLog(id, type);
					if(null != log){
						discoverLogBackgroundApi.insert(log);
					}
				}
			}
			discoverArticle.setOperUser(getSysUserName());
			discoverArticle.setUpdateTime(new Date());
			discoverArticleBackgroundApi.update(discoverArticle);
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 通过主键集批量修改记录，必须传入id
	 * id格式为：('id1','id2','id3','id4')
	 */
	@RequestMapping("/batchUpdateArticle")
	@ResponseBody
	public String batchUpdateArticle(DiscoverArticleInputDto discoverArticle,String type) {
		try {
			Assert.hasText(discoverArticle.getId(), "参数id不能为空");
			if(StringUtils.isNotBlank(type)){
				//批量删除文章
				if("article_delete".equals(type)){
					int pubCount = discoverArticleBackgroundApi.getPublishCountByIds(discoverArticle.getId());
					if(pubCount > 0){
						return super.jsonResult(StateCode.ERROR, "你选择的文章中存在已发布的文章，不能删除。");
					}
					int cfCount = discoverArticleBackgroundApi.getChannelCountByids(discoverArticle.getId());
					if(cfCount > 0){
						return super.jsonResult(StateCode.ERROR, "你选择的文章中存在被轮播图链接的文章，不能删除。");
					}
					String[] idArr = DiscoverUtil.parseIds(discoverArticle.getId());
					if(null != idArr && idArr.length > 0){
						for(String id:idArr){
							DiscoverLogInputDto input = this.getLogDto(DisLogBusinessTypeEnum.DIS_LOG_BIZ_ARTICLE, DisLogOperTypeEnum.DIS_LOG_OPER_DEL, discoverArticle, id);
							if(null != input){
								discoverLogBackgroundApi.insert(input);
							}
						}
					}
				}else if("article_publish".equals(type) || "article_unPublish".equals(type) || "recycle_restore".equals(type)){
					String[] idArr = DiscoverUtil.parseIds(discoverArticle.getId());
					if(null != idArr && idArr.length > 0){
						for(String id:idArr){
							DiscoverLogInputDto input = this.getUpdateLog(id,type);
							if(null != input){
								discoverLogBackgroundApi.insert(input);
							}
						}
					}
				}
			}
			discoverArticle.setOperUser(getSysUserName());
			discoverArticle.setUpdateTime(new Date());
			discoverArticleBackgroundApi.batchUpdate(discoverArticle);
		} catch (Exception e) {
			logger.error("批量修改异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 通过主键集批量修改记录，必须传入id
	 * id格式为：('id1','id2','id3','id4')
	 */
	@RequestMapping("/batchRemoveArticle")
	@ResponseBody
	public String batchRemoveArticle(String ids) {
		try {
			Assert.hasText(ids, "参数ids不能为空");
			String[] idArr = DiscoverUtil.parseIds(ids);
			if(null != idArr && idArr.length > 0){
				for(String id:idArr){
					DiscoverLogInputDto input = this.getLogDto(DisLogBusinessTypeEnum.DIS_LOG_BIZ_RECYCLE, DisLogOperTypeEnum.DIS_LOG_OPER_DEL, null, id);
					if(null != input){
						discoverLogBackgroundApi.insert(input);
					}
				}
			}
			discoverArticleBackgroundApi.batchRemove(ids);
		} catch (Exception e) {
			logger.error("批量删除异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	@ResponseBody
	@RequestMapping(value = "/uploadArticleCover", method = RequestMethod.POST)
	public String uploadArticleCover(@RequestParam MultipartFile file, HttpServletRequest request,String name){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			
			WFXResult<String> result = fileUploadApi.uploadArticle(name,1,file.getInputStream());
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
			resultMap.put("resultCode", "6");
			try {
				resultMap.put("resultMsg",URLEncoder.encode("上传图片发生异常","utf-8"));
			} catch (UnsupportedEncodingException e1) {
				
			}
		}
		return jsonResult(StateCode.SUCCESS, SUCCESS, resultMap);
	}
	
	@ResponseBody
	@RequestMapping(value = "/uploadArticleEdit", method = RequestMethod.POST)
	public String uploadArticleEdit(@RequestParam MultipartFile imgFile, HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String fileName = imgFile.getOriginalFilename();
			InputStream in = imgFile.getInputStream();
			if(StringUtils.isNotBlank(fileName) && fileName.indexOf(".") >= 0){
				String expName = fileName.substring(fileName.lastIndexOf(".")+1);
				if(null == expName){
					resultMap.put("error", 1);
					resultMap.put("message", URLEncoder.encode("图片名称异常","utf-8"));
					return JSONObject.toJSONString(resultMap);
				}
				if("jpg".equals(expName) || "png".equals(expName)){
					if(in.available() > 1024*800){
						resultMap.put("error", 1);
						resultMap.put("message", URLEncoder.encode(expName+"格式图片大小不能超过800k。","utf-8"));
						return JSONObject.toJSONString(resultMap);
					}
				}else if("gif".equals(expName)){
					if(in.available() > 2*1024*1024){
						resultMap.put("error", 1);
						resultMap.put("message", URLEncoder.encode(expName+"格式图片大小不能超过2M。","utf-8"));
						return JSONObject.toJSONString(resultMap);
					}
				}else{
					resultMap.put("error", 1);
					resultMap.put("message", URLEncoder.encode("图片必须是jgp、png或gif格式。","utf-8"));
					return JSONObject.toJSONString(resultMap);
				}
			}else{
				resultMap.put("error", 1);
				resultMap.put("message", URLEncoder.encode("图片名称异常。","utf-8"));
				return JSONObject.toJSONString(resultMap);
			}
			WFXResult<String> result = fileUploadApi.uploadArticle(imgFile.getOriginalFilename(),2,imgFile.getInputStream());
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
	
	private DiscoverLogInputDto getLogDto(DisLogBusinessTypeEnum bizType,DisLogOperTypeEnum operType,DiscoverArticleInputDto article,String id){
		DiscoverLogInputDto inputDto = new DiscoverLogInputDto();
		inputDto.setOperateUser(getSysUserName());
		inputDto.setOperateAccount(UserSecurityUtil.getSystemUser().getLogin_name());
		inputDto.setBusinessType(bizType.getKey()+"");
		inputDto.setOperateType(operType.getKey()+"");
		inputDto.setOperateDate(new Date());
		//inputDto.setOperatorIp("");
		
		DiscoverArticleOutputDto articleOld = discoverArticleBackgroundApi.getById(id);
		String channelNameNew = "";
		String authorNameNew = "优购微零售";
		String publishNameNew = "保存文章";
		String reNameNew = "";
		String channelNameOld = "";
		String authorNameOld = "优购微零售";
		String publishNameOld = "";
		String reNameOld = "";
		if(null != articleOld){
			String channelId = articleOld.getChannelId();
			if(StringUtils.isNotBlank(channelId)){
				DiscoverChannelOutputDto oldChannel = discoverChannelBackgroundApi.getById(channelId);
				if(null != oldChannel){
					channelNameOld = oldChannel.getChannelName();
				}
			}
			if(null != articleOld.getAuthorType() && articleOld.getAuthorType() == 2){
				authorNameOld = articleOld.getAuthorAccount();
			}
			if(null != articleOld.getPublishStatus()){
				if(1 == articleOld.getPublishStatus()){
					publishNameOld = "保存文章";
				}else if(2 == articleOld.getPublishStatus()){
					publishNameOld = "发布文章";
				}
			}
			if(null != articleOld.getRecommendFlag()){
				if(1 == articleOld.getRecommendFlag()){
					reNameOld = "不推荐";
				}else if(2 == articleOld.getRecommendFlag()){
					reNameOld = "推荐";
				}
			}
		}else{
			articleOld = new DiscoverArticleOutputDto();
		}
		
		if(null != article){
			String channelId = article.getChannelId();
			if(StringUtils.isNotBlank(channelId)){
				DiscoverChannelOutputDto newChannel = discoverChannelBackgroundApi.getById(channelId);
				if(null != newChannel){
					channelNameNew = newChannel.getChannelName();
				}
			}
			if(null != article.getAuthorType() && article.getAuthorType() == 2){
				authorNameNew = article.getAuthorAccount();
			}
			if(null != article.getPublishStatus()){
				if(1 == article.getPublishStatus()){
					publishNameNew = "保存文章";
				}else if(2 == article.getPublishStatus()){
					publishNameNew = "发布文章";
				}
			}
			if(null != article.getRecommendFlag()){
				if(1 == article.getRecommendFlag()){
					reNameNew = "不推荐";
				}else if(2 == article.getRecommendFlag()){
					reNameNew = "推荐";
				}
			}
		}
		
		if(DisLogBusinessTypeEnum.DIS_LOG_BIZ_ARTICLE.equals(bizType)){//文章
			if(DisLogOperTypeEnum.DIS_LOG_OPER_ADD.equals(operType)){
				String content =  MessageFormat.format("【新增】文章【{0}】，作者【{1}】，所属频道【{2}】，【{3}】，【{4}】此文章", 
						article.getTitle(),authorNameNew,channelNameNew,
						publishNameNew,reNameNew);
				inputDto.setOperateContent(content);
			}else if(DisLogOperTypeEnum.DIS_LOG_OPER_DEL.equals(operType)){
				String content =  MessageFormat.format("【删除】文章【{0}】，作者【{1}】", 
						articleOld.getTitle(),authorNameOld);
				inputDto.setOperateContent(content);
			}
		}else if(DisLogBusinessTypeEnum.DIS_LOG_BIZ_RECYCLE.equals(bizType)){//回收站
			if(DisLogOperTypeEnum.DIS_LOG_OPER_DEL.equals(operType)){
				String content =  MessageFormat.format("【删除】删除文章【{0}】作者【{1}】频道【{2}】", articleOld.getTitle(),authorNameOld,channelNameOld);
				inputDto.setOperateContent(content);
			}else if(DisLogOperTypeEnum.DIS_LOG_OPER_UPDATE.equals(operType)){
				String content =  MessageFormat.format("【还原】还原文章【{0}】作者【{1}】频道【{2}】", articleOld.getTitle(),authorNameOld,channelNameOld);
				inputDto.setOperateContent(content);
			}
		}
		return inputDto;
	}
	
	private List<DiscoverLogInputDto> getUpdateLogDto(DiscoverArticleInputDto articleNew,String id){
		List<DiscoverLogInputDto> logList = new ArrayList<DiscoverLogInputDto>();
		DiscoverLogInputDto log = null;
		DiscoverArticleOutputDto articleOld = discoverArticleBackgroundApi.getById(id);
		if(null == articleOld || null == articleNew){
			return null;
		}
		String titleNew = articleNew.getTitle();
		String titleOld = articleOld.getTitle();
		if(StringUtils.isBlank(titleNew)){
			titleNew = "";
		}
		if(!titleNew.equals(titleOld)){
			log = new DiscoverLogInputDto();
			log.setOperateUser(getSysUserName());
			log.setOperateAccount(UserSecurityUtil.getSystemUser().getLogin_name());
			log.setBusinessType(DisLogBusinessTypeEnum.DIS_LOG_BIZ_ARTICLE.getKey()+"");
			log.setOperateType(DisLogOperTypeEnum.DIS_LOG_OPER_UPDATE.getKey()+"");
			log.setOperateDate(new Date());
			String content = MessageFormat.format("（标题）修改标题【{0}】为【{1}】",titleOld,titleNew);
			log.setOperateContent(content);
			logList.add(log);
		}
		Integer accountTypeOld = articleOld.getAuthorType();
		String accountAccountOld = articleOld.getAuthorAccount();
		String accountOld = "";
		Integer accountTypeNew = articleNew.getAuthorType();
		String accountAccountNew = articleNew.getAuthorAccount();
		String accountNew = "";
		if(null != accountTypeOld && 1 == accountTypeOld){
			accountOld = "优购微零售";
		}else{
			accountOld = accountAccountOld;
		}
		if(1==accountTypeNew){
			accountNew = "优购微零售";
		}else{
			accountNew = accountAccountNew;
		}
		if(null == accountOld){
			accountOld = "";
		}
		if(!accountOld.equals(accountAccountNew)){
			log = new DiscoverLogInputDto();
			log.setOperateUser(getSysUserName());
			log.setOperateAccount(UserSecurityUtil.getSystemUser().getLogin_name());
			log.setBusinessType(DisLogBusinessTypeEnum.DIS_LOG_BIZ_ARTICLE.getKey()+"");
			log.setOperateType(DisLogOperTypeEnum.DIS_LOG_OPER_UPDATE.getKey()+"");
			log.setOperateDate(new Date());
			String content = MessageFormat.format("（作者）修改作者【{0}】为【{1}】",accountOld,accountAccountNew);
			log.setOperateContent(content);
			logList.add(log);
		}
		Integer recommendOld = articleOld.getRecommendFlag();
		Integer recommendNew = articleNew.getRecommendFlag();
		if(recommendOld != recommendNew){
			String reName = "撤销推荐";
			if(null != recommendNew && recommendNew == 2){
				reName = "推荐";
			}
			log = new DiscoverLogInputDto();
			log.setOperateUser(getSysUserName());
			log.setOperateAccount(UserSecurityUtil.getSystemUser().getLogin_name());
			log.setBusinessType(DisLogBusinessTypeEnum.DIS_LOG_BIZ_ARTICLE.getKey()+"");
			log.setOperateType(DisLogOperTypeEnum.DIS_LOG_OPER_UPDATE.getKey()+"");
			log.setOperateDate(new Date());
			String content = MessageFormat.format("（推荐/撤销推荐）{0}文章【{1}】",reName,titleNew);
			log.setOperateContent(content);
			logList.add(log);
		}
		String channelIdOld = articleOld.getChannelId();
		String channelIdNew = articleNew.getChannelId();
		if(null == channelIdNew){
			channelIdNew = "";
		}
		if(!channelIdNew.equals(channelIdOld)){
			DiscoverChannelOutputDto chOld = discoverChannelBackgroundApi.getById(channelIdOld);
			DiscoverChannelOutputDto chNew = discoverChannelBackgroundApi.getById(channelIdNew);
			String chNameOld = "";
			String chNmaeNew = "";
			if(null != chOld){
				chNameOld = chOld.getChannelName();
			}
			if(null != chNew){
				chNmaeNew = chNew.getChannelName();
			}
			log = new DiscoverLogInputDto();
			log.setOperateUser(getSysUserName());
			log.setOperateAccount(UserSecurityUtil.getSystemUser().getLogin_name());
			log.setBusinessType(DisLogBusinessTypeEnum.DIS_LOG_BIZ_ARTICLE.getKey()+"");
			log.setOperateType(DisLogOperTypeEnum.DIS_LOG_OPER_UPDATE.getKey()+"");
			log.setOperateDate(new Date());
			String content = MessageFormat.format("（频道）文章【{0}】频道由【{1}】修改为【{2}】",titleNew,chNameOld,chNmaeNew);
			log.setOperateContent(content);
			logList.add(log);
		}
		Integer publishOld = articleOld.getPublishStatus();
		Integer publishNew = articleNew.getPublishStatus();
		if(null == publishOld){
			publishOld = 0;
		}
		if(null == publishNew){
			publishNew = 0;
		}
		if(publishNew != publishOld){
			String publishName = "";
			if(1 == publishNew){
				publishName = "取消发布";
			}else if(2 == publishNew){
				publishName = "发布";
			}
			log = new DiscoverLogInputDto();
			log.setOperateUser(getSysUserName());
			log.setOperateAccount(UserSecurityUtil.getSystemUser().getLogin_name());
			log.setBusinessType(DisLogBusinessTypeEnum.DIS_LOG_BIZ_ARTICLE.getKey()+"");
			log.setOperateType(DisLogOperTypeEnum.DIS_LOG_OPER_UPDATE.getKey()+"");
			log.setOperateDate(new Date());
			String content = MessageFormat.format("（取消发布/发布）{0}文章【{1}】",publishName,titleNew);
			log.setOperateContent(content);
			logList.add(log);
		}
		return logList;
	}
	
	private DiscoverLogInputDto getUpdateLog(String id,String type){
		DiscoverLogInputDto log = new DiscoverLogInputDto();
		String title = "";
		DiscoverArticleOutputDto article = discoverArticleBackgroundApi.getById(id);
		if(null != article){
			title = article.getTitle();
		}
		log.setOperateUser(getSysUserName());
		log.setOperateAccount(UserSecurityUtil.getSystemUser().getLogin_name());
		log.setBusinessType(DisLogBusinessTypeEnum.DIS_LOG_BIZ_ARTICLE.getKey()+"");
		log.setOperateType(DisLogOperTypeEnum.DIS_LOG_OPER_UPDATE.getKey()+"");
		log.setOperateDate(new Date());
		String content = "";
		if("article_publish".equals(type)){
			content = MessageFormat.format("（取消发布/发布）【发布】文章【{0}】",title);
		}else if("article_unPublish".equals(type)){
			content = MessageFormat.format("（取消发布/发布）【取消发布】文章【{0}】",title);
		}else if("article_recommend".equals(type)){
			content = MessageFormat.format("（推荐/撤销推荐）【推荐】文章【{0}】",title);
		}else if("article_unRecommend".equals(type)){
			content = MessageFormat.format("（推荐/撤销推荐）【撤销推荐】文章【{0}】",title);
		}else if("article_delete".equals(type)){
			String account = "优购微零售";
			if(null != article.getAuthorType() && article.getAuthorType() == 2){
				account = article.getAuthorAccount();
			}
			log.setOperateType(DisLogOperTypeEnum.DIS_LOG_OPER_DEL.getKey()+"");
			content = MessageFormat.format("【删除】文章【{0}】，作者【{1}】",title,account);
		}else if("article_sort".equals(type)){
			content = MessageFormat.format("（排序）文章【{0}】进行了排序修改操作",title);
		}else if("recycle_restore".equals(type)){
			String account = "优购微零售";
			if(null != article.getAuthorType() && article.getAuthorType() == 2){
				account = article.getAuthorAccount();
			}
			String channelName = "";
			DiscoverChannelOutputDto channel = discoverChannelBackgroundApi.getById(article.getChannelId());
			if(null != channel){
				channelName = channel.getChannelName();
			}
			log.setBusinessType(DisLogBusinessTypeEnum.DIS_LOG_BIZ_RECYCLE.getKey()+"");
			content = MessageFormat.format("【还原】还原文章【{0}】作者【{1}】频道【{2}】",title,account,channelName);
		}
		log.setOperateContent(content);
		return log;
	}
}
