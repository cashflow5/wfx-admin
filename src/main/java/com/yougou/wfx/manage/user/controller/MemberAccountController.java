 /*
 * 版本信息
 
 * 日期 2016-03-24 18:30:55
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.user.controller;

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
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.dto.base.WFXResult;
import com.yougou.wfx.finance.dto.output.FinSellerInfoOutputDto;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.user.vo.MemberAccountPageVo;
import com.yougou.wfx.member.api.background.IMemberAccountBackgroundApi;
import com.yougou.wfx.member.dto.input.MemberAccountInputDto;
import com.yougou.wfx.member.dto.input.MemberAccountPageInputDto;
import com.yougou.wfx.member.dto.output.MemberAccountOutputDto;
import com.yougou.wfx.seller.api.background.ISellerInfoBackgroundApi;
import com.yougou.wfx.seller.dto.input.SellerInfoInputDto;
import com.yougou.wfx.seller.dto.output.SellerInfoOutputDto;
import com.yougou.wfx.shop.dto.input.ShopInputDto;
import com.yougou.wfx.util.Constant;

/**
 * MemberAccountController
 * @author luoq
 * @Date 创建时间：2016-03-24 18:30:55
 */
@Controller
@RequestMapping("/user")
public class MemberAccountController extends BaseController{
	
	@Resource
	private IMemberAccountBackgroundApi memberAccountBackgroundApi;
	
	@Resource
	private ISellerInfoBackgroundApi sellerInfoBackgroundApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/memberList")
	public String memberList(){
		return "/manage/user/memberList";
	}
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/memberMgtList")
	public String memberMgtList(ModelMap map){
		map.put("memberMgt", "1");
		return "/manage/user/memberList";
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryMemberData")
	@ResponseBody
	public String queryMemberData(ModelMap map, MemberAccountPageVo memberAccountVo, PageModel<MemberAccountOutputDto> pageModel)throws Exception{
		MemberAccountPageInputDto pageInputDto = (MemberAccountPageInputDto) BeanUtil.convertBean(memberAccountVo, MemberAccountPageInputDto.class);
		pageInputDto.setMemberType(Constant.MEMBER_NORMAL);
		PageModel<MemberAccountOutputDto> result = memberAccountBackgroundApi.findPage(pageInputDto, pageModel);
		if(result != null){
			List<MemberAccountOutputDto> items = result.getItems();
			if(null != items && items.size() > 0){
				for(MemberAccountOutputDto member : items){
					//父级分销商
					SellerInfoOutputDto parentSeller = sellerInfoBackgroundApi.getById(member.getParentSellerId());
					if(parentSeller != null){
						MemberAccountOutputDto parentMemberAccount = memberAccountBackgroundApi.getMemberInfoById(parentSeller.getLoginaccountId());
						if(null != parentMemberAccount){
							if("13888888888".equals(parentMemberAccount.getLoginName()) || "88888888888".equals(parentMemberAccount.getLoginName())){
								member.setParentLoginName("优购微零售总店");
							}else{
								member.setParentLoginName(parentMemberAccount.getLoginName());
							}
							member.setParentPlatformUsername(parentMemberAccount.getPlatformUsername());
						}
					}
				}
			}
		}
		return JSONObject.toJSONString(result);
	}
	
	@RequestMapping("/getMemberById")
	@ResponseBody
	public String getMemberById(String id) throws Exception {
		MemberAccountOutputDto memberAccount = new MemberAccountOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			memberAccount = memberAccountBackgroundApi.getMemberInfoById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, memberAccount);
	}
	
	@RequestMapping("/removeMember")
	@ResponseBody
	public String removeMember(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			memberAccountBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	@RequestMapping("/saveMember")
	@ResponseBody
	public String saveMember(MemberAccountInputDto memberAccount) {
		try {
			String id = memberAccount.getId();
			if (StringUtils.isNotBlank(id)) {
				memberAccountBackgroundApi.update(memberAccount);
			}else{
				memberAccountBackgroundApi.insert(memberAccount);
			}
		} catch (Exception e) {
			logger.error("保存数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	@RequestMapping("/applyToSeller")
	@ResponseBody
	public String applyToSeller(MemberAccountInputDto memberAccount) {
		try {
			SellerInfoInputDto sellerInfo = new SellerInfoInputDto();
			sellerInfo.setLoginaccountId(memberAccount.getId());
			sellerInfo.setLoginName(memberAccount.getLoginName());
			
			ShopInputDto shopInputDto = new ShopInputDto();
			shopInputDto.setLoginName(memberAccount.getLoginName());
			sellerInfo.setShopInputDto(shopInputDto);
			sellerInfo.setSellerName(memberAccount.getMemberName());
			sellerInfo.setLastActiveOptuser(getSysUserName());
			WFXResult<Boolean> result = sellerInfoBackgroundApi.applyToSeller(sellerInfo);
			if(!result.getResult()){
				return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + result.getResultMsg());
			}
		} catch (Exception e) {
			logger.error("升级成为分销商数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
}
