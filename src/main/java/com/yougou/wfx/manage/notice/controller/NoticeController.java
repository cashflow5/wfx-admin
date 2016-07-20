/*
 * 版本信息

 * 日期 2016-06-20 14:03:02

 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.notice.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.basicset.api.background.ISysConfigBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityStyleBackgroundApi;
import com.yougou.wfx.commodity.dto.input.CommodityStyleInputDto;
import com.yougou.wfx.commodity.dto.output.CommodityStyleOutputDto;
import com.yougou.wfx.discover.api.background.IDiscoverArticleBackgroundApi;
import com.yougou.wfx.discover.dto.output.DiscoverArticleOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.enums.NoticeStatusEnum;
import com.yougou.wfx.enums.RedirectTypeEnum;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.notice.vo.NoticePageVo;
import com.yougou.wfx.notice.api.background.INoticeBackgroundApi;
import com.yougou.wfx.notice.dto.input.NoticeInputDto;
import com.yougou.wfx.notice.dto.input.NoticePageInputDto;
import com.yougou.wfx.notice.dto.output.NoticeOutputDto;

/**
 * NoticeController
 * 
 * @author wfx
 * @Date 创建时间：2016-06-20 14:03:02
 */
@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController {

	@Resource
	private INoticeBackgroundApi noticeBackgroundApi;
	@Resource
	private IDiscoverArticleBackgroundApi discoverArticleBackgroundApi;
	@Resource
	private ICommodityStyleBackgroundApi iommodityStyleBackgroundApi;
	@Resource
	private ISysConfigBackgroundApi sysConfigBackgroundApi;

	/**
	 * 进入菜单
	 */
	@RequestMapping("/propList")
	public String propList(ModelMap map) {
		map.put("noticeStatusEnumList", NoticeStatusEnum.values());
		map.put("noticeRedirectTypeEnumList", RedirectTypeEnum.values());
		return "/manage/notice/noticeList";
	}

	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryPropData")
	@ResponseBody
	public String queryPropData(ModelMap map, NoticePageVo noticeVo, PageModel pageModel) throws Exception {
		NoticePageInputDto pageInputDto = (NoticePageInputDto) BeanUtil.convertBean(noticeVo, NoticePageInputDto.class);
		PageModel<NoticeOutputDto> result = noticeBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}

	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getPropById")
	@ResponseBody
	public String getPropById(String id) throws Exception {
		NoticeOutputDto notice = new NoticeOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			notice = noticeBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, notice);
	}

	/**
	 * 通过主键id删除数据
	 */
	@RequestMapping("/removeProp")
	@ResponseBody
	public String removeProp(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			NoticeInputDto notice = new NoticeInputDto();
			notice.setId(id);
			notice.setSort(0);
			notice.setDeleteFlag(1);
			noticeBackgroundApi.update(notice);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}

	/**
	 * 保存或修改数据
	 */
	@RequestMapping("/saveProp")
	@ResponseBody
	public String saveProp(NoticeInputDto notice) {
		try {
			String id = notice.getId();
			notice.setLastOperateUser(getSysUserName());
			notice.setLastOperateTime(new Date());
			if (StringUtils.isNotBlank(id)) {
				noticeBackgroundApi.update(notice);
			} else {
				noticeBackgroundApi.insert(notice);
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
	@RequestMapping("/insertProp")
	@ResponseBody
	public String insertProp(NoticeInputDto notice) {
		try {
			noticeBackgroundApi.insert(notice);
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}

	/**
	 * 通过主键id修改数据，必须传入id参数，否则不能修改
	 */
	@RequestMapping("/updateProp")
	@ResponseBody
	public String updateProp(NoticeInputDto notice) {
		try {
			Assert.hasText(notice.getId(), "参数id不能为空");
			noticeBackgroundApi.update(notice);
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}

	/**
	 * 批量删除公告
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/batchRemoveNotice")
	@ResponseBody
	public String batchRemoveNotice(String ids) {
		try {
			Assert.hasText(ids, "参数id不能为空");
			if (StringUtils.isNotBlank(ids)) {
				String idsStr = ids.substring(0, ids.length() - 1);
				String[] idArray = idsStr.split(",");
				for (String idStr : idArray) {
					NoticeInputDto noticeInputDto = new NoticeInputDto();
					noticeInputDto.setId(idStr);
					noticeInputDto.setDeleteFlag(1);// 删除标记 改为删除
					noticeBackgroundApi.update(noticeInputDto);
				}
			}
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}

	/**
	 * 获取有效的最大轮播图排序号
	 */
	@RequestMapping("/buildRedirectUrl")
	@ResponseBody
	public String buildRedirectUrl(int redirectType, String value) {

		String h5Domain = sysConfigBackgroundApi.getValueBykey("wfx.domain.h5");
		if(StringUtils.isBlank(h5Domain)){
			h5Domain = "http://m.yougou.net";
		}
		String redirectUrl = "";
		if (RedirectTypeEnum.DISCOVER_ARTICLE.getKey() == redirectType) {// 发现文章
			if (StringUtils.isBlank(value)) {
				return super.jsonResult(StateCode.SUCCESS, "请输入文章ID", redirectUrl);
			}
			DiscoverArticleOutputDto dto = discoverArticleBackgroundApi.getByNo(Integer.parseInt(value.trim()));
			if (dto == null) {
				return super.jsonResult(StateCode.SUCCESS, "未找到编号为" + value + "的文章,请重新输入", redirectUrl);
			}
			redirectUrl = h5Domain+"/discover/view/"+dto.getId()+".sc";
		} else if (RedirectTypeEnum.COMMODITY_DETAIL.getKey() == redirectType) {// 商品详情
			if (StringUtils.isBlank(value)) {
				return super.jsonResult(StateCode.SUCCESS, "请输入商品款色编码", redirectUrl);
			}
			CommodityStyleInputDto inputDto=new CommodityStyleInputDto();
			inputDto.setSupplierCode(value);
			List<CommodityStyleOutputDto> dtoList=iommodityStyleBackgroundApi.queryList(inputDto);
			if (dtoList == null||dtoList.isEmpty()) {
				return super.jsonResult(StateCode.SUCCESS, "未找到款色编码为" + value + "的商品,请重新输入", redirectUrl);
			}
			redirectUrl = h5Domain+"/yougoushop/item/"+dtoList.get(0).getNo()+".sc";
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, redirectUrl);
	}
}
