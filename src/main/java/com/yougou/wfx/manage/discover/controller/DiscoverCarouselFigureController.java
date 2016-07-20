 /*
 * 版本信息
 
 * 日期 2016-06-02 10:14:16
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.discover.controller;

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

import org.apache.commons.io.input.SwappedDataInputStream;
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
import com.yougou.wfx.discover.api.background.IDiscoverArticleBackgroundApi;
import com.yougou.wfx.discover.api.background.IDiscoverCarouselFigureBackgroundApi;
import com.yougou.wfx.discover.api.background.IDiscoverLogBackgroundApi;
import com.yougou.wfx.discover.dto.input.DiscoverArticleInputDto;
import com.yougou.wfx.discover.dto.input.DiscoverCarouselFigureInputDto;
import com.yougou.wfx.discover.dto.input.DiscoverCarouselFigurePageInputDto;
import com.yougou.wfx.discover.dto.input.DiscoverLogInputDto;
import com.yougou.wfx.discover.dto.output.DiscoverArticleOutputDto;
import com.yougou.wfx.discover.dto.output.DiscoverCarouselFigureOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.dto.base.WFXResult;
import com.yougou.wfx.enums.DisLogBusinessTypeEnum;
import com.yougou.wfx.enums.DisLogOperTypeEnum;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.discover.vo.DiscoverCarouselFigurePageVo;
import com.yougou.wfx.system.api.IFileUploadApi;
import com.yougou.wfx.system.api.IWFXSystemApi;
import com.yougou.wfx.util.UserSecurityUtil;

/**
 * DiscoverCarouselFigureController
 * @author wang.zf
 * @Date 创建时间：2016-06-02 10:14:16
 */
@Controller
@RequestMapping("/discover")
public class DiscoverCarouselFigureController extends BaseController{
	
	@Resource
	private IDiscoverCarouselFigureBackgroundApi discoverCarouselFigureBackgroundApi;
	@Resource
	private IDiscoverArticleBackgroundApi iDiscoverArticleBackgroundApi;
	@Resource
	private IFileUploadApi fileUploadApi;
	@Resource
	private IWFXSystemApi wfxSystemApi;
	@Resource
	private IDiscoverLogBackgroundApi discoverLogBackgroundApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/disCarouselList")
	public String disCarouselList(ModelMap map){
		String picUrl = wfxSystemApi.obtainImgBaseUrl();
		map.put("picUrl", picUrl);
		return "/manage/discover/disCarouselList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryCarouselData")
	@ResponseBody
	public String queryCarouselData(ModelMap map, DiscoverCarouselFigurePageVo discoverCarouselFigureVo, PageModel pageModel)throws Exception{
		DiscoverCarouselFigurePageInputDto pageInputDto = (DiscoverCarouselFigurePageInputDto) BeanUtil.convertBean(discoverCarouselFigureVo, DiscoverCarouselFigurePageInputDto.class);
		PageModel<DiscoverCarouselFigureOutputDto> result = discoverCarouselFigureBackgroundApi.findInfoPage(pageInputDto, pageModel);
		int page = result.getPage();
		if(page == 1){
			List<DiscoverCarouselFigureOutputDto> carList = result.getItems();
			if(null != carList && carList.size() > 0){
				for(int i=0;i<carList.size();i++){
					if(i < 6){
						carList.get(i).setShowFlag(10);
					}
				}
			}
		}
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getCarouselById")
	@ResponseBody
	public String getCarouselById(String id) throws Exception {
		DiscoverCarouselFigureOutputDto discoverCarouselFigure = new DiscoverCarouselFigureOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			discoverCarouselFigure = discoverCarouselFigureBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, discoverCarouselFigure);
	}
	
	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getCarouselInfoById")
	@ResponseBody
	public String getCarouselInfoById(String id) throws Exception {
		DiscoverCarouselFigureOutputDto discoverCarouselFigure = new DiscoverCarouselFigureOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			discoverCarouselFigure = discoverCarouselFigureBackgroundApi.getById(id);
			if(null != discoverCarouselFigure && StringUtils.isNotBlank(discoverCarouselFigure.getArticleNo())){
				DiscoverArticleInputDto articleInput = new DiscoverArticleInputDto();
				Integer articleNo = 0;
				try {
					articleNo = Integer.parseInt(discoverCarouselFigure.getArticleNo());
				} catch (Exception e) {
					return super.jsonResult(StateCode.SUCCESS, SUCCESS, discoverCarouselFigure);
				}
				articleInput.setNo(articleNo);
				List<DiscoverArticleOutputDto> outPutList = iDiscoverArticleBackgroundApi.findPage(articleInput);
				if(null != outPutList && outPutList.size() > 0){
					DiscoverArticleOutputDto article = outPutList.get(0);
					Integer authorType = article.getAuthorType();
					String articleAccount = article.getAuthorAccount();
					String account = "优购微零售";
					String articleMark = "";
					if(null == authorType || authorType == 2){
						account = articleAccount;
					}
					articleMark += "《"+article.getTitle() + "》  by  " + account;
					discoverCarouselFigure.setArticleMark(articleMark);
				}
			}
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, discoverCarouselFigure);
	}
	
	/**
	 * 通过主键id删除数据
	 */
	@RequestMapping("/removeCarousel")
	@ResponseBody
	public String removeCarousel(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			DiscoverLogInputDto input = this.getLogDto(DisLogOperTypeEnum.DIS_LOG_OPER_DEL, null, id);
			if(null != input){
				discoverLogBackgroundApi.insert(input);
			}
			discoverCarouselFigureBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 批量删除轮播图
	 */
	@RequestMapping("/batchRemoveCarousel")
	@ResponseBody
	public String batchRemoveCarousel(String ids) {
		try {
			Assert.hasText(ids, "参数id不能为空");
			String[] idArr = DiscoverUtil.parseIds(ids);
			if(null != idArr && idArr.length > 0){
				for(String id:idArr){
					DiscoverLogInputDto input = this.getLogDto(DisLogOperTypeEnum.DIS_LOG_OPER_DEL, null, id);
					if(null != input){
						discoverLogBackgroundApi.insert(input);
					}
				}
			}
			discoverCarouselFigureBackgroundApi.batchRemove(ids);
		} catch (Exception ex) {
			logger.error("批量删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 保存或修改数据
	 */
	@RequestMapping("/saveCarousel")
	@ResponseBody
	public String saveCarousel(DiscoverCarouselFigureInputDto discoverCarouselFigure) {
		try {
			String id = discoverCarouselFigure.getId();
			Date date = new Date();
			discoverCarouselFigure.setUpdateTime(date);
			discoverCarouselFigure.setOperUser(getSysUserName());
			if (StringUtils.isNotBlank(id)) {
				List<DiscoverLogInputDto> logList = this.getUpdateLogDto(discoverCarouselFigure, id);
				if(null != logList && logList.size() > 0){
					for(DiscoverLogInputDto log:logList){
						discoverLogBackgroundApi.insert(log);
					}
				}
				discoverCarouselFigureBackgroundApi.update(discoverCarouselFigure);
			}else{
				discoverCarouselFigure.setShowFlag(1);
				//排序号需要根据最大排序号进行自增
				discoverCarouselFigure.setCreateTime(date);
				discoverCarouselFigureBackgroundApi.insert(discoverCarouselFigure);
				DiscoverLogInputDto input = this.getLogDto(DisLogOperTypeEnum.DIS_LOG_OPER_ADD, discoverCarouselFigure, null);
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
	@RequestMapping("/insertCarousel")
	@ResponseBody
	public String insertCarousel(DiscoverCarouselFigureInputDto discoverCarouselFigure) {
		try {
			discoverCarouselFigureBackgroundApi.insert(discoverCarouselFigure);
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/updateCarousel")
	@ResponseBody
	public String updateCarousel(DiscoverCarouselFigureInputDto discoverCarouselFigure) {
		try {
			Assert.hasText(discoverCarouselFigure.getId(), "参数id不能为空");
			discoverCarouselFigureBackgroundApi.update(discoverCarouselFigure);
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/setTop")
	@ResponseBody
	public String setTop(String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			discoverCarouselFigureBackgroundApi.updateSortNo();
			DiscoverCarouselFigureInputDto discoverCarouselFigure = new DiscoverCarouselFigureInputDto();
			discoverCarouselFigure.setId(id);
			discoverCarouselFigure.setSortNo(1);
			discoverCarouselFigure.setOperUser(getSysUserName());
			discoverCarouselFigure.setUpdateTime(new Date());
			discoverCarouselFigureBackgroundApi.update(discoverCarouselFigure);
			DiscoverLogInputDto log = this.getUpdateSortLog(id,"toTop");
			if(null != log){
				discoverLogBackgroundApi.insert(log);
			}
		} catch (Exception e) {
			logger.error("置顶操作异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 向上移动
	 */
	@RequestMapping("/toUp")
	@ResponseBody
	public String toUp(String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			DiscoverCarouselFigureOutputDto output = discoverCarouselFigureBackgroundApi.getById(id);
			DiscoverCarouselFigureInputDto thisInput = (DiscoverCarouselFigureInputDto)BeanUtil.convertBean(output, DiscoverCarouselFigureInputDto.class);
			DiscoverCarouselFigureOutputDto pre = discoverCarouselFigureBackgroundApi.getPrevious(thisInput);
			if(null != pre){
				Date date = new Date();
				Integer thisNo = output.getSortNo();
				Integer preNo = pre.getSortNo();
				DiscoverCarouselFigureInputDto thisUpdate = new DiscoverCarouselFigureInputDto();
				thisUpdate.setId(thisInput.getId());
				thisUpdate.setSortNo(preNo);
				thisUpdate.setUpdateTime(new Date(date.getTime()+9000));
				discoverCarouselFigureBackgroundApi.update(thisUpdate);
				DiscoverCarouselFigureInputDto preUpdate = new DiscoverCarouselFigureInputDto();
				preUpdate.setId(pre.getId());
				preUpdate.setSortNo(thisNo);
				preUpdate.setUpdateTime(date);
				discoverCarouselFigureBackgroundApi.update(preUpdate);
			}
			DiscoverLogInputDto log = this.getUpdateSortLog(id,"toUp");
			if(null != log){
				discoverLogBackgroundApi.insert(log);
			}
		} catch (Exception e) {
			logger.error("向上移动异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 向下移动
	 */
	@RequestMapping("/toDown")
	@ResponseBody
	public String toDown(String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			DiscoverCarouselFigureOutputDto output = discoverCarouselFigureBackgroundApi.getById(id);
			DiscoverCarouselFigureInputDto thisInput = (DiscoverCarouselFigureInputDto)BeanUtil.convertBean(output, DiscoverCarouselFigureInputDto.class);
			DiscoverCarouselFigureOutputDto next = discoverCarouselFigureBackgroundApi.getNext(thisInput);
			if(null != next){
				Date date = new Date();
				Integer thisNo = output.getSortNo();
				Integer preNo = next.getSortNo();
				DiscoverCarouselFigureInputDto nextUpde = new DiscoverCarouselFigureInputDto();
				nextUpde.setId(next.getId());
				nextUpde.setSortNo(thisNo);
				nextUpde.setUpdateTime(new Date(date.getTime()+9000));
				discoverCarouselFigureBackgroundApi.update(nextUpde);
				DiscoverCarouselFigureInputDto thisUpdate = new DiscoverCarouselFigureInputDto();
				thisUpdate.setId(thisInput.getId());
				thisUpdate.setSortNo(preNo);
				thisUpdate.setUpdateTime(date);
				discoverCarouselFigureBackgroundApi.update(thisUpdate);
			}
			DiscoverLogInputDto log = this.getUpdateSortLog(id,"toDown");
			if(null != log){
				discoverLogBackgroundApi.insert(log);
			}
		} catch (Exception e) {
			logger.error("向下移动异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	@ResponseBody
	@RequestMapping(value = "uploadCarImg", method = RequestMethod.POST)
	public String uploadCarImg(@RequestParam MultipartFile file, HttpServletRequest request,String name){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			WFXResult<String> result = fileUploadApi.uploadCarImg(name,file.getInputStream());
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
	
	private DiscoverLogInputDto getLogDto(DisLogOperTypeEnum operType,DiscoverCarouselFigureInputDto cfNew,String id){
		DiscoverLogInputDto log = new DiscoverLogInputDto();
		log.setOperateUser(getSysUserName());
		log.setOperateAccount(UserSecurityUtil.getSystemUser().getLogin_name());
		log.setBusinessType(DisLogBusinessTypeEnum.DIS_LOG_BIZ_CARFIG.getKey()+"");
		log.setOperateType(operType.getKey()+"");
		log.setOperateDate(new Date());
		
		if(DisLogOperTypeEnum.DIS_LOG_OPER_ADD.equals(operType)){
			String articleNo = cfNew.getArticleNo();
			String articleTitle = "";
			String account = "";
			if(StringUtils.isNotBlank(articleNo)){
				DiscoverArticleInputDto articleInput = new DiscoverArticleInputDto();
				Integer no;
				try{
					no = Integer.parseInt(articleNo);
					articleInput.setNo(no);
					List<DiscoverArticleOutputDto> arList = iDiscoverArticleBackgroundApi.findPage(articleInput);
					if(null != arList && arList.size() > 0){
						DiscoverArticleOutputDto article = arList.get(0);
						if(null != article){
							articleTitle = article.getTitle();
							Integer type = article.getAuthorType();
							if(null != type && 2 == type){
								account = article.getAuthorAccount();
							}else{
								account = "优购微零售";
							}
						}
					}
				}catch(Exception e){
					
				}
			}
			String content =  MessageFormat.format("【新增】轮播图【{0}】，链接文章【{1}】，作者【{0}】", cfNew.getName(),articleTitle,account);
			log.setOperateContent(content);
		}else if(DisLogOperTypeEnum.DIS_LOG_OPER_DEL.equals(operType)){
			DiscoverCarouselFigureOutputDto cfOld = discoverCarouselFigureBackgroundApi.getById(id);
			String name = "";
			if(null != cfOld){
				name = cfOld.getName();
			}
			String content =  MessageFormat.format("【删除】轮播图【{0}】",name);
			log.setOperateContent(content);
		}
		return log;
	}
	
	private List<DiscoverLogInputDto> getUpdateLogDto(DiscoverCarouselFigureInputDto cfNew,String id){
		List<DiscoverLogInputDto> logList = new ArrayList<DiscoverLogInputDto>();
		DiscoverLogInputDto log = null;
		
		DiscoverCarouselFigureOutputDto cfOld = discoverCarouselFigureBackgroundApi.getById(id);
		if(null == cfOld || null == cfNew){
			return null;
		}
		
		String nameOld = cfOld.getName()==null?"":cfOld.getName();
		String nameNew = cfNew.getName()==null?"":cfNew.getName();
		if(!nameNew.equals(nameOld)){
			log = new DiscoverLogInputDto();
			log.setOperateUser(getSysUserName());
			log.setOperateAccount(UserSecurityUtil.getSystemUser().getLogin_name());
			log.setBusinessType(DisLogBusinessTypeEnum.DIS_LOG_BIZ_CARFIG.getKey()+"");
			log.setOperateType(DisLogOperTypeEnum.DIS_LOG_OPER_UPDATE.getKey()+"");
			log.setOperateDate(new Date());
			
			String content = MessageFormat.format("（名称）修改名称【{0}】为【{1}】", nameOld,nameNew);
			log.setOperateContent(content);
			logList.add(log);
		}
		
		String pictureOld = cfOld.getPicture()==null?"":cfOld.getPicture();
		String pictureNew = cfNew.getPicture()==null?"":cfNew.getPicture();
		if(!pictureNew.equals(pictureOld)){
			log = new DiscoverLogInputDto();
			log.setOperateUser(getSysUserName());
			log.setOperateAccount(UserSecurityUtil.getSystemUser().getLogin_name());
			log.setBusinessType(DisLogBusinessTypeEnum.DIS_LOG_BIZ_CARFIG.getKey()+"");
			log.setOperateType(DisLogOperTypeEnum.DIS_LOG_OPER_UPDATE.getKey()+"");
			log.setOperateDate(new Date());
			
			String content = MessageFormat.format("(图片)轮播图【{0}】修改图片", nameNew);
			log.setOperateContent(content);
			logList.add(log);
		}
		
		String articleNoOld = cfOld.getArticleNo()==null?"0":cfOld.getArticleNo();
		String articleNoNew = cfNew.getArticleNo()==null?"0":cfNew.getArticleNo();
		if(!articleNoNew.equals(articleNoOld)){
			log = new DiscoverLogInputDto();
			log.setOperateUser(getSysUserName());
			log.setOperateAccount(UserSecurityUtil.getSystemUser().getLogin_name());
			log.setBusinessType(DisLogBusinessTypeEnum.DIS_LOG_BIZ_CARFIG.getKey()+"");
			log.setOperateType(DisLogOperTypeEnum.DIS_LOG_OPER_UPDATE.getKey()+"");
			log.setOperateDate(new Date());
			String articleTitleOld = "";
			String articleTitleNew = "";
			String accountOld = "";
			String accountNew = "";
			Integer noOld;
			Integer noNew;
			try {
				noOld = Integer.parseInt(articleNoOld);
				noNew = Integer.parseInt(articleNoNew);
				DiscoverArticleInputDto articleOld = new DiscoverArticleInputDto();
				articleOld.setNo(noOld);
				List<DiscoverArticleOutputDto> arOldList = iDiscoverArticleBackgroundApi.findPage(articleOld);
				if(null != arOldList && arOldList.size() > 0){
					DiscoverArticleOutputDto article = arOldList.get(0);
					if(null != article){
						articleTitleOld = article.getTitle();
						Integer type = article.getAuthorType();
						if(null != type && 2 == type){
							accountOld = article.getAuthorAccount();
						}else{
							accountOld = "优购微零售";
						}
					}
				}
				
				DiscoverArticleInputDto articleNew = new DiscoverArticleInputDto();
				articleNew.setNo(noNew);
				List<DiscoverArticleOutputDto> arNewList = iDiscoverArticleBackgroundApi.findPage(articleNew);
				if(null != arNewList && arNewList.size() > 0){
					DiscoverArticleOutputDto article = arNewList.get(0);
					if(null != article){
						articleTitleNew = article.getTitle();
						Integer type = article.getAuthorType();
						if(null != type && 2 == type){
							accountNew = article.getAuthorAccount();
						}else{
							accountNew = "优购微零售";
						}
					}
				}
			} catch (Exception e) {
				
			}
			String content = MessageFormat.format("（链接文章）修改【{0}】为【{1}】，作者【{2}】",articleTitleOld,articleTitleNew,accountNew) ;
			log.setOperateContent(content);
			logList.add(log);
		}
		
		return logList;
	}
	
	private DiscoverLogInputDto getUpdateSortLog(String id,String type){
		DiscoverLogInputDto log = new DiscoverLogInputDto();
		String oper = "";
		if(null != type){
			if("toTop".equals(type)){
				oper = "置顶";
			}else if("toUp".equals(type)){
				oper = "上移";
			}else if("toDown".equals(type)){
				oper = "下移";
			}
		}
		String name = "";
		DiscoverCarouselFigureOutputDto cf = discoverCarouselFigureBackgroundApi.getById(id);
		if(null != cf){
			name = cf.getName();
		}
		log = new DiscoverLogInputDto();
		log.setOperateUser(getSysUserName());
		log.setOperateAccount(UserSecurityUtil.getSystemUser().getLogin_name());
		log.setBusinessType(DisLogBusinessTypeEnum.DIS_LOG_BIZ_CARFIG.getKey()+"");
		log.setOperateType(DisLogOperTypeEnum.DIS_LOG_OPER_UPDATE.getKey()+"");
		log.setOperateDate(new Date());
		String content = MessageFormat.format("（排序）轮播图【{0}】进行了排序【{1}】修改操作", name,oper);
		log.setOperateContent(content);
		return log;
	}
}
