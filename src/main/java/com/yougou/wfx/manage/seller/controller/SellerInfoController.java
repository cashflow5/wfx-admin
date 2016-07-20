 /*
 * 版本信息
 
 * 日期 2016-03-24 15:23:34
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.seller.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.dto.base.WFXResult;
import com.yougou.wfx.enums.SellerStateEnum;
import com.yougou.wfx.finance.api.background.IFinSellerInfoBackgroundApi;
import com.yougou.wfx.finance.dto.output.FinSellerInfoOutputDto;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.seller.vo.SellerInfoPageVo;
import com.yougou.wfx.member.api.background.IMemberAccountBackgroundApi;
import com.yougou.wfx.member.dto.output.MemberAccountOutputDto;
import com.yougou.wfx.seller.api.background.ISellerInfoBackgroundApi;
import com.yougou.wfx.seller.dto.input.SellerInfoInputDto;
import com.yougou.wfx.seller.dto.input.SellerInfoPageInputDto;
import com.yougou.wfx.seller.dto.output.SellerInfoOutputDto;
import com.yougou.wfx.shop.api.background.IShopBackgroundApi;
import com.yougou.wfx.system.api.IWFXSystemApi;


/**
 * SellerInfoController
 * @author zhangfeng
 * @Date 创建时间：2016-03-24 15:23:34
 */
@Controller
@RequestMapping("/user")
public class SellerInfoController extends BaseController{
	
	@Resource
	private ISellerInfoBackgroundApi sellerInfoBackgroundApi;
	
	@Resource
	private IFinSellerInfoBackgroundApi finSellerInfoBackgroundApi;
	
	@Resource
	private IMemberAccountBackgroundApi memberAccountBackgroundApi;
	
	@Resource
	private IShopBackgroundApi shopBackgroundApi ;
	
	@Resource
	private IWFXSystemApi wfxSystemApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/sellerList")
	public String sellerList(){
		return "/manage/seller/sellerList";
	}
	
	
	
	/**
	 * 分销商列表
	 * @param map
	 * @param sellerInfoVo
	 * @param pageModel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/querySellerData")
	@ResponseBody
	public String querySellerData(ModelMap map, SellerInfoPageVo sellerInfoVo, PageModel<SellerInfoOutputDto> pageModel)throws Exception{
		SellerInfoPageInputDto pageInputDto = (SellerInfoPageInputDto) BeanUtil.convertBean(sellerInfoVo, SellerInfoPageInputDto.class);
		PageModel<SellerInfoOutputDto> result = sellerInfoBackgroundApi.findSellerPage(pageInputDto, pageModel);
		if(result != null){
			List<SellerInfoOutputDto> items = result.getItems();
			if(null != items && items.size() > 0){
				for(SellerInfoOutputDto seller : items){
					FinSellerInfoOutputDto finSeller = finSellerInfoBackgroundApi.getById(seller.getId());
					SellerInfoOutputDto parentSeller = sellerInfoBackgroundApi.getById(seller.getParentId());
					if(parentSeller != null){
						MemberAccountOutputDto parentMemberAccount = memberAccountBackgroundApi.getMemberInfoById(parentSeller.getLoginaccountId());
						if(null != parentMemberAccount){
							if("13888888888".equals(parentMemberAccount.getLoginName()) || "88888888888".equals(parentMemberAccount.getLoginName())){
								seller.setParentLoginName("优购微零售总店");
							}else{
								seller.setParentLoginName(parentMemberAccount.getLoginName());
							}
							seller.setParentPlatformUsername(parentMemberAccount.getPlatformUsername());
						}
					}
					if(null != finSeller){
						seller.setCommissionTotalAmount(finSeller.getCommissionAllTotalAmount());
					}
				}
			}
		}
		return JSONObject.toJSONString(result);
	}
	
	@RequestMapping("/getSellerById")
	@ResponseBody
	public String getSellerById(String id) throws Exception {
		SellerInfoOutputDto sellerInfo = new SellerInfoOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			sellerInfo = sellerInfoBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, sellerInfo);
	}
	
	
	
	/**
	 * 进入分销商审核菜单
	 */
	@RequestMapping("/sellerAuditList")
	public String sellerAuditList(){
		return "/manage/seller/sellerAuditList";
	}	
	
	
	/**
	 * 分页查询分销商审核数据，并返回json格式的结果
	 */
	@RequestMapping("/querySellerAuditData")
	@ResponseBody
	public String querySellerAuditData(ModelMap map, SellerInfoPageVo sellerInfoVo, PageModel<SellerInfoOutputDto> pageModel)throws Exception{
		SellerInfoPageInputDto pageInputDto = (SellerInfoPageInputDto) BeanUtil.convertBean(sellerInfoVo, SellerInfoPageInputDto.class);
		PageModel<SellerInfoOutputDto> result = sellerInfoBackgroundApi.findSellerAuditPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	
	/**
	 * 分销商审核通过（开启合作）
	 * @param map
	 * @param sellerInfoVo
	 * @param pageModel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sellerAuditPass")
	@ResponseBody
	public String sellerAuditPass(ModelMap map, SellerInfoInputDto sellerInfoDto,String type)throws Exception{
		
		Map<String,Object> resultMap = new HashMap<String,Object> ();
		WFXResult< Boolean> result = sellerInfoBackgroundApi.sellerAuditPass(sellerInfoDto, getSysUserName(),type);
		resultMap.put("result", result.getResult());
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, resultMap);
	}
	
	/**
	 * 分销商审核拒绝
	 * @param map
	 * @param sellerInfoVo
	 * @param pageModel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sellerAuditRefuse")
	@ResponseBody
	public String sellerAuditRefuse(ModelMap map, SellerInfoInputDto sellerInfoDto)throws Exception{
		//SellerInfoPageInputDto pageInputDto = (SellerInfoPageInputDto) BeanUtil.convertBean(sellerInfoDto, SellerInfoPageInputDto.class);
		sellerInfoDto.setState(SellerStateEnum.AUDIT_NOT_PASS.getState()); //分销商状态变为 审核拒绝
		int result = sellerInfoBackgroundApi.update(sellerInfoDto);
		Map<String,Integer> resultMap = new HashMap<String,Integer> ();
		resultMap.put("resultCount", result);
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, resultMap);
	}
	
	/**
	 * 根据分销商ID获取分销商审核详情
	 * @param map
	 * @param sellerInfoVo
	 * @param pageModel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getSellerAuditInfoById")
	public String getSellerAuditInfoById(ModelMap map, String sellerId)throws Exception{
				
		SellerInfoOutputDto outputDto = sellerInfoBackgroundApi.getSellerInfoById(sellerId);	
		
		map.put("sellerDetailInfo", outputDto);
		return "/manage/seller/sellerAuditDetailInfo";
	}
	
	/**
	 * 根据分销商ID获取分销商详情
	 * @param map
	 * @param sellerInfoVo
	 * @param pageModel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getSellerInfoById")
	public String getSellerInfoById(ModelMap map, String sellerId)throws Exception{
				
		SellerInfoOutputDto outputDto = sellerInfoBackgroundApi.getSellerInfoById(sellerId);		
		map.put("sellerDetailInfo", outputDto);
		return "/manage/seller/sellerDetailInfo";
	}
	
	/**
	 * 分销商停止合作
	 * @param map
	 * @param sellerInfoVo
	 * @param pageModel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sellerStop")
	@ResponseBody
	public String sellerStop(ModelMap map, SellerInfoInputDto sellerInfoDto)throws Exception{
		
		Map<String,Object> resultMap = new HashMap<String,Object> ();
		WFXResult< Boolean> result = sellerInfoBackgroundApi.sellerStop(sellerInfoDto, getSysUserName());
		resultMap.put("result", result.getResult());
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, resultMap);
	}
	
	
}
