 /*
 * 版本信息
 
 * 日期 2016-06-02 20:18:25
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.discover.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.discover.api.background.IDiscoverLogBackgroundApi;
import com.yougou.wfx.discover.dto.input.DiscoverLogInputDto;
import com.yougou.wfx.discover.dto.input.DiscoverLogPageInputDto;
import com.yougou.wfx.discover.dto.output.DiscoverLogOutputDto;
import com.yougou.wfx.manage.discover.vo.DiscoverLogPageVo;

/**
 * DiscoverLogController
 * @author zhang.wj
 * @Date 创建时间：2016-06-02 20:18:26
 */
@Controller
@RequestMapping("/discoverLog")
public class DiscoverLogController extends BaseController{
	
	@Resource
	private IDiscoverLogBackgroundApi discoverLogBackgroundApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/discoverLogList")
	public String discoverList(){
		return "/manage/discover/discoverLogList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryDiscoverLogData")
	@ResponseBody
	public String querydiscoverLogData(ModelMap map, DiscoverLogPageVo discoverLogVo, PageModel<DiscoverLogOutputDto> pageModel)throws Exception{
		DiscoverLogPageInputDto pageInputDto = (DiscoverLogPageInputDto) BeanUtil.convertBean(discoverLogVo, DiscoverLogPageInputDto.class);
		PageModel<DiscoverLogOutputDto> result = discoverLogBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	@RequestMapping("/getDiscoverById")
	@ResponseBody
	public String getDiscoverById(String id) throws Exception {
		DiscoverLogOutputDto discoverLog = new DiscoverLogOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			discoverLog = discoverLogBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, discoverLog);
	}
	
	@RequestMapping("/removeDiscover")
	@ResponseBody
	public String removeDiscover(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			discoverLogBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	@RequestMapping("/saveDiscover")
	@ResponseBody
	public String saveDiscover(DiscoverLogInputDto discoverLog) {
		try {
			String id = discoverLog.getId();
			if (StringUtils.isNotBlank(id)) {
				discoverLogBackgroundApi.update(discoverLog);
			}else{
				discoverLogBackgroundApi.insert(discoverLog);
			}
		} catch (Exception e) {
			logger.error("保存数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
}
