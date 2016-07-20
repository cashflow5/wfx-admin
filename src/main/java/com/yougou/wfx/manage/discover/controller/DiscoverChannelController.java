 /*
 * 版本信息
 
 * 日期 2016-06-02 13:51:45
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.discover.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.yougou.tools.common.utils.UUIDGenerator;
import com.yougou.wfx.commodity.dto.input.CommodityStyleInputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.discover.vo.DiscoverArticlePageVo;
import com.yougou.wfx.manage.discover.vo.DiscoverChannelPageVo;
import com.yougou.wfx.util.UserSecurityUtil;
import com.yougou.wfx.discover.api.background.IDiscoverArticleBackgroundApi;
import com.yougou.wfx.discover.api.background.IDiscoverChannelBackgroundApi;
import com.yougou.wfx.discover.api.background.IDiscoverLogBackgroundApi;
import com.yougou.wfx.discover.dto.input.DiscoverArticlePageInputDto;
import com.yougou.wfx.discover.dto.input.DiscoverCarouselFigureInputDto;
import com.yougou.wfx.discover.dto.input.DiscoverChannelInputDto;
import com.yougou.wfx.discover.dto.input.DiscoverChannelPageInputDto;
import com.yougou.wfx.discover.dto.input.DiscoverLogInputDto;
import com.yougou.wfx.discover.dto.output.DiscoverArticleOutputDto;
import com.yougou.wfx.discover.dto.output.DiscoverCarouselFigureOutputDto;
import com.yougou.wfx.discover.dto.output.DiscoverChannelOutputDto;
import com.yougou.wfx.enums.BusinessTypeEnum;
import com.yougou.wfx.enums.DisLogBusinessTypeEnum;
import com.yougou.wfx.enums.DisLogOperTypeEnum;
import com.yougou.wfx.enums.OperateTypeEnum;

/**
 * DiscoverChannelController
 * @author zhang.wj
 * @Date 创建时间：2016-06-02 13:51:45
 */
@Controller
@RequestMapping("/discover")
public class DiscoverChannelController extends BaseController{
	
	@Resource
	private IDiscoverChannelBackgroundApi discoverChannelBackgroundApi;
	@Resource
	private IDiscoverArticleBackgroundApi discoverArticleBackgroundApi;
	
	@Resource
	private IDiscoverLogBackgroundApi discoverLogBackgroundApi;
	/**
	 * 进入菜单
	 */
	@RequestMapping("/discoverList")
	public String discoverList(ModelMap map,HttpServletRequest request){
		int orderMark=discoverChannelBackgroundApi.getorderMark("");
		map.put("orderMarkCode", orderMark);
		return "/manage/discover/discoverList";
	}
	/**
	 * 获取排序号
	 */
	@RequestMapping("/getorderMark")
	@ResponseBody
	public String  getorderMark(ModelMap map,HttpServletRequest request){
		int orderMark=discoverChannelBackgroundApi.getorderMark("");
		Map<String,Integer> resultMap = new HashMap<String,Integer> ();
		resultMap.put("orderMark", orderMark);
		
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, resultMap);
		
	}
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryDiscoverData")
	@ResponseBody
	public String queryDiscoverData(ModelMap map, DiscoverChannelPageVo discoverChannelVo, PageModel<DiscoverChannelOutputDto> pageModel)throws Exception{
		DiscoverChannelPageInputDto pageInputDto = (DiscoverChannelPageInputDto) BeanUtil.convertBean(discoverChannelVo, DiscoverChannelPageInputDto.class);
		PageModel<DiscoverChannelOutputDto> result = discoverChannelBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	@RequestMapping("/getDiscoverById")
	@ResponseBody
	public String getDiscoverById(String id) throws Exception {
		DiscoverChannelOutputDto discoverChannel = new DiscoverChannelOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			discoverChannel = discoverChannelBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, discoverChannel);
	}
	
	@RequestMapping("/removeDiscover")
	@ResponseBody
	public String removeDiscover(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			DiscoverArticlePageInputDto dto=new DiscoverArticlePageInputDto();
			dto.setChannelId(id);
			int count = discoverArticleBackgroundApi.findPageCount(dto);
			if(count>0){
				return super.jsonResult(StateCode.ERROR, "发生异常,删除频道已有文章");
			}
			DiscoverChannelOutputDto discoverChannel = discoverChannelBackgroundApi.getById(id);
			discoverChannelBackgroundApi.removeById(id);
			String operateContent="频道【"+discoverChannel.getChannelName()+"】";
			this.setLogDto(DisLogOperTypeEnum.DIS_LOG_OPER_DEL.getKey()+"",operateContent);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	@RequestMapping("/batchRemoveDiscover")
	@ResponseBody
	public String batchRemoveDiscover(ModelMap map,HttpServletRequest request) {
		try {
			
			//商品id
			String ids=request.getParameter("ids");
			
			
			List<String>  channelIdList=new ArrayList<String>();
			if(ids!=null && !ids.equals("")){
				String []  channelId=ids.split(",");
				for (int i = 0; i < channelId.length; i++) {
					DiscoverArticlePageInputDto dto=new DiscoverArticlePageInputDto();
					dto.setChannelId(channelId[i]);
					int count = discoverArticleBackgroundApi.findPageCount(dto);
					if(count>0){
						return super.jsonResult(StateCode.ERROR, "发生异常,删除频道已有文章");
					}
					channelIdList.add(channelId[i]);
				}
				discoverChannelBackgroundApi.batchRemove(channelIdList);
			}
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	@RequestMapping("/saveDiscover")
	@ResponseBody
	public String saveDiscover(DiscoverChannelInputDto discoverChannel) {
		try {
			String id = discoverChannel.getId();
			if (StringUtils.isNotBlank(id)) {
				discoverChannel.setUpdateUser(UserSecurityUtil.getSystemUserName());
				discoverChannel.setUpdateDate(new Date());
				DiscoverChannelInputDto  discoverChannelInputDto=new DiscoverChannelInputDto();
				discoverChannelInputDto.setStatus(1);
			    DiscoverChannelOutputDto  ountputDto=discoverChannelBackgroundApi.getById(id);
			    if(ountputDto!=null && ountputDto.getStatus().intValue()==discoverChannel.getStatus().intValue()){
			    	discoverChannelBackgroundApi.update(discoverChannel);
				}else{
					int count=discoverChannelBackgroundApi.findPageCount(discoverChannelInputDto);
					if(discoverChannel.getStatus().intValue()==1 && count>=6){
						return super.jsonResult(StateCode.ERROR, "显示频道不能大于6");
						
					}else{
						discoverChannelBackgroundApi.update(discoverChannel);
					}
				}
			    String operateContent="修改名称:【"+ountputDto.getChannelName()+"】为【"+discoverChannel.getChannelName()+"】";
			    if(discoverChannel.getStatus()==1){
			    	operateContent=operateContent+"状态:【"+ountputDto.getChannelName()+"】状态为【显示】";
			    }else{
			    	operateContent=operateContent+"状态:【"+ountputDto.getChannelName()+"】状态为【隐藏】";
			    }
			    this.setLogDto(DisLogOperTypeEnum.DIS_LOG_OPER_UPDATE.getKey()+"",operateContent);
			}else{
				int orderMark=discoverChannelBackgroundApi.getorderMark("");
				orderMark=orderMark+1;
				String strId = UUIDGenerator.getUUID();
				discoverChannel.setId(strId);
				discoverChannel.setCreateUser(UserSecurityUtil.getSystemUserName());
				discoverChannel.setCreateDate(new Date());
				discoverChannel.setUpdateUser(UserSecurityUtil.getSystemUserName());
				discoverChannel.setUpdateDate(new Date());
				discoverChannel.setOrderMark(orderMark);
				discoverChannel.setChannelCode(orderMark+"");
				discoverChannel.setStatus(2);
				discoverChannelBackgroundApi.insert(discoverChannel);
				
				String operateContent="频道【"+discoverChannel.getChannelName()+"】";
				this.setLogDto(DisLogOperTypeEnum.DIS_LOG_OPER_ADD.getKey()+"",operateContent);
			}
		} catch (Exception e) {
			logger.error("保存数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/queryChannelDetails")
	public String queryChannelDetails(ModelMap map,HttpServletRequest request){
		String channelId=request.getParameter("channelId");
		DiscoverChannelOutputDto channelDto=discoverChannelBackgroundApi.getById(channelId);
		map.put("channelDto", channelDto);
		map.put("channelId", channelId);
		return "/manage/discover/channelDetails";
		
	}
	/**
	 * 查询频道详情信息，并返回json格式的结果
	 */
	@RequestMapping("/queryChannelDetailsData")
	@ResponseBody
	public String queryChannelDetailsData(ModelMap map, DiscoverArticlePageVo discoverArticleVo, PageModel pageModel)throws Exception{
		DiscoverArticlePageInputDto pageInputDto = (DiscoverArticlePageInputDto) BeanUtil.convertBean(discoverArticleVo, DiscoverArticlePageInputDto.class);
		DiscoverChannelOutputDto channelDto=discoverChannelBackgroundApi.getById(pageInputDto.getChannelId());
		PageModel<DiscoverArticleOutputDto> result = discoverArticleBackgroundApi.findPage(pageInputDto, pageModel);
		map.put("channelDto", channelDto);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/setChannelTop")
	@ResponseBody
	public String setTop(String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			discoverChannelBackgroundApi.updateOrderMark();
			DiscoverChannelInputDto discoverChannelInputDto = new DiscoverChannelInputDto();
			discoverChannelInputDto.setId(id);
			discoverChannelInputDto.setOrderMark(1);
			discoverChannelInputDto.setUpdateDate(new Date());
			discoverChannelBackgroundApi.update(discoverChannelInputDto);
		} catch (Exception e) {
			logger.error("置顶操作异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
	  
	    String  operateContent="进行了排序修改操作";
	   
	    this.setLogDto(DisLogOperTypeEnum.DIS_LOG_OPER_UPDATE.getKey()+"",operateContent);
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}

	/**
	 * 向上移动
	 */
	@RequestMapping("/toChannelUp")
	@ResponseBody
	public String toUp(String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			DiscoverChannelOutputDto channelDto = discoverChannelBackgroundApi.getById(id);
			DiscoverChannelInputDto thisInput = (DiscoverChannelInputDto)BeanUtil.convertBean(channelDto, DiscoverChannelInputDto.class);
			DiscoverChannelOutputDto pre = discoverChannelBackgroundApi.getPrevious(thisInput);
			if(null != pre){
				Date date = new Date();
				Integer orderMark = channelDto.getOrderMark();
				Integer preNo =pre.getOrderMark();
				DiscoverChannelInputDto thisUpdate = new DiscoverChannelInputDto();
				thisUpdate.setId(thisInput.getId());
				thisUpdate.setOrderMark(preNo);
				thisUpdate.setUpdateDate(new Date(date.getTime()+9000));
				discoverChannelBackgroundApi.update(thisUpdate);
				DiscoverChannelInputDto preUpdate = new DiscoverChannelInputDto();
				preUpdate.setId(pre.getId());
				preUpdate.setOrderMark(orderMark);
				preUpdate.setUpdateDate(date);
				discoverChannelBackgroundApi.update(preUpdate);
			}
		} catch (Exception e) {
			logger.error("向上移动异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
	    String  operateContent="进行了排序修改操作";
	   
	    this.setLogDto(DisLogOperTypeEnum.DIS_LOG_OPER_UPDATE.getKey()+"",operateContent);
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 向下移动
	 */
	@RequestMapping("/toChannelDown")
	@ResponseBody
	public String toDown(String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			DiscoverChannelOutputDto channelDto = discoverChannelBackgroundApi.getById(id);
			DiscoverChannelInputDto thisInput = (DiscoverChannelInputDto)BeanUtil.convertBean(channelDto, DiscoverChannelInputDto.class);
			DiscoverChannelOutputDto next = discoverChannelBackgroundApi.getNext(thisInput);
			if(null != next){
				Date date = new Date();
				Integer thisNo = channelDto.getOrderMark();
				Integer preNo = next.getOrderMark();
				DiscoverChannelInputDto nextUpde = new DiscoverChannelInputDto();
				nextUpde.setId(next.getId());
				nextUpde.setOrderMark(thisNo);
				nextUpde.setUpdateDate(new Date(date.getTime()+9000));
				discoverChannelBackgroundApi.update(nextUpde);
				DiscoverChannelInputDto thisUpdate = new DiscoverChannelInputDto();
				thisUpdate.setId(thisInput.getId());
				thisUpdate.setOrderMark(preNo);
				thisUpdate.setUpdateDate(date);
				discoverChannelBackgroundApi.update(thisUpdate);
			}
		} catch (Exception e) {
			logger.error("向下移动异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		String  operateContent="进行了排序修改操作";
		   
		this.setLogDto(DisLogOperTypeEnum.DIS_LOG_OPER_UPDATE.getKey()+"",operateContent);
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	private void setLogDto(String operateType,String operateContent){
		DiscoverLogInputDto  dto=new DiscoverLogInputDto();
		dto.setOperateUser(getSysUserName());
		dto.setOperateAccount(UserSecurityUtil.getSystemUser().getLogin_name());
		dto.setBusinessType(DisLogBusinessTypeEnum.DIS_LOG_BIZ_CHANNEL.getKey()+"");
		dto.setOperateType(operateType);
		dto.setOperateDate(new Date());
		dto.setOperateContent(operateContent);
		discoverLogBackgroundApi.insert(dto);
	}
}
