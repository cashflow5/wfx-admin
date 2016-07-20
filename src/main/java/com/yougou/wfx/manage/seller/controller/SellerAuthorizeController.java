 /*
 * 版本信息
 
 * 日期 2016-04-15 16:09:24
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.seller.controller;

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
import com.yougou.tools.common.utils.StringUtil;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.enums.MemberOptTypeEnum;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.seller.vo.SellerAuthorizePageVo;
import com.yougou.wfx.member.api.background.IMemberActionLogBackgroundApi;
import com.yougou.wfx.member.dto.input.MemberActionLogPageInputDto;
import com.yougou.wfx.member.dto.output.MemberActionLogOutputDto;
import com.yougou.wfx.seller.api.background.ISellerAuthorizeBackgroundApi;
import com.yougou.wfx.seller.api.background.ISellerBankBackgroundApi;
import com.yougou.wfx.seller.api.background.ISellerInfoBackgroundApi;
import com.yougou.wfx.seller.dto.input.SellerAuthorizeInputDto;
import com.yougou.wfx.seller.dto.output.SellerAuthorizeOutputDto;
import com.yougou.wfx.seller.dto.output.SellerBankOutputDto;
import com.yougou.wfx.seller.dto.output.SellerInfoOutputDto;
import com.yougou.wfx.shop.api.background.IShopBackgroundApi;
import com.yougou.wfx.system.api.IWFXSystemApi;

/**
 * SellerAuthorizeController
 * @author zheng.qq
 * @Date 创建时间：2016-04-15 16:09:26
 */
@Controller
@RequestMapping("/user")
public class SellerAuthorizeController extends BaseController{
	
	@Resource
	private ISellerAuthorizeBackgroundApi sellerAuthorizeBackgroundApi;
	@Resource
	private ISellerBankBackgroundApi sellerBankBackgroundApi;
	@Resource
	private ISellerInfoBackgroundApi sellerInfoBackgroundApi;
	@Resource
	private IShopBackgroundApi shopBackgroundApi ;
	@Resource
	private IWFXSystemApi wfxSystemApi;
	@Resource
	private IMemberActionLogBackgroundApi memberActionLogBackgroundApi;
	/**
	 * 进入菜单
	 */
	@RequestMapping("/authorizeList")
	public String authorizeList(){
		return "/manage/seller/authorizeList";
	}
	
	/**
	 * 进入资质详情页面
	 */
	@RequestMapping("/authorizeInfo")
	public String authorizeInfo(ModelMap map, String sellerId){
		try {
			Assert.hasText(sellerId, "sellerId不能为空！");
			//获取分销商基本信息
			SellerInfoOutputDto sellerInfo = sellerInfoBackgroundApi.getSellerInfoById(sellerId);
			map.addAttribute("sellerInfo", sellerInfo);
			//获取分销商银行卡信息
			List<SellerBankOutputDto> sellerBankInfos = sellerBankBackgroundApi.getSellerBanks(sellerId);
			if(sellerBankInfos!=null && sellerBankInfos.size()>0){
				SellerBankOutputDto sellerBank = sellerBankInfos.get(0);
				if(StringUtils.isNotBlank(sellerBank.getBankAccount())){
					sellerBank.setBankAccount(StringUtils.substring(sellerBank.getBankAccount(), 0, 4)+
							"**********"+StringUtils.substring(sellerBank.getBankAccount(), -4));
				}
				map.addAttribute("sellerBankInfo", sellerBank);
			}
			//获取分销商资质信息
			List<SellerAuthorizeOutputDto> sellerAuthorizeInfos = sellerAuthorizeBackgroundApi.getSellerAuthorizeBySellerId(sellerId);
			if(sellerAuthorizeInfos!=null && sellerAuthorizeInfos.size()>0){
				String imgBaseUrl = wfxSystemApi.obtainImgBaseUrl();
				map.addAttribute("sellerAuthorizeInfo", sellerAuthorizeInfos.get(0));
				map.addAttribute("imgBaseUrl", imgBaseUrl);
			}
		} catch (Exception e) {
			logger.error("查询数据异常", e);
		}
		return "/manage/seller/authorizeInfo";
	}
	
	/**
	 * 分页查询分销商银行卡信息修改数据，并返回json格式的结果
	 */
	@RequestMapping("/querySellerBankLog")
	@ResponseBody
	public String querySellerBankLog(String loginAccountId, PageModel pageModel)throws Exception{
		MemberActionLogPageInputDto pageInputDto = new MemberActionLogPageInputDto();
		pageInputDto.setLoginaccountId(loginAccountId);
		pageInputDto.setOptType(MemberOptTypeEnum.MODIFY_BANK_CARD.getKey()+"','"+MemberOptTypeEnum.VERIFY_AUTHORIZE.getKey());
		PageModel<MemberActionLogOutputDto> result = memberActionLogBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryAuthorizeData")
	@ResponseBody
	public String queryAuthorizeData(ModelMap map, SellerAuthorizePageVo sellerAuthorizeVo, PageModel pageModel)throws Exception{
		SellerAuthorizeInputDto pageInputDto = (SellerAuthorizeInputDto) BeanUtil.convertBean(sellerAuthorizeVo, SellerAuthorizeInputDto.class);
		PageModel<SellerAuthorizeOutputDto> result = sellerAuthorizeBackgroundApi.findSellerAuthorizePage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 通过主键id查询数据
	 */
	@RequestMapping("/getAuthorizeById")
	@ResponseBody
	public String getAuthorizeById(String id) throws Exception {
		SellerAuthorizeOutputDto sellerAuthorize = new SellerAuthorizeOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			sellerAuthorize = sellerAuthorizeBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, sellerAuthorize);
	}
	
	/**
	 * 通过主键id删除数据
	 */
	@RequestMapping("/removeAuthorize")
	@ResponseBody
	public String removeAuthorize(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			sellerAuthorizeBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 保存或修改数据
	 */
	@RequestMapping("/saveAuthorize")
	@ResponseBody
	public String saveAuthorize(SellerAuthorizeInputDto sellerAuthorize) {
		try {
			String id = sellerAuthorize.getId();
			if (StringUtils.isNotBlank(id)) {
				sellerAuthorizeBackgroundApi.update(sellerAuthorize);
			}else{
				sellerAuthorizeBackgroundApi.insert(sellerAuthorize);
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
	@RequestMapping("/insertAuthorize")
	@ResponseBody
	public String insertAuthorize(SellerAuthorizeInputDto sellerAuthorize) {
		try {
			sellerAuthorizeBackgroundApi.insert(sellerAuthorize);
		} catch (Exception e) {
			logger.error("插入数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 通过id更新资质的审核状态
	 */
	@RequestMapping("/updateAuthorizeStatus")
	@ResponseBody
	public String updateAuthorizeStatus(String id, int status) {
		try {
			SellerAuthorizeOutputDto out = sellerAuthorizeBackgroundApi.getById(id);
			if(out!=null){
				if(out.getStatus()==status){
					return super.jsonResult(StateCode.SUCCESS, SUCCESS, "重复操作!");
				}
				//审核通过是验证资质材料是否完整
				if(status==2 && (StringUtil.isStrEmpty(out.getIdCardPicBack())
						||StringUtil.isStrEmpty(out.getIdCardPic()))){
					return super.jsonResult(StateCode.ERROR, "资质材料不完整!");
				}else{
					sellerAuthorizeBackgroundApi.updateSellerAuthorizeStatusById(id, status);
				}
				
			}else{
				return super.jsonResult(StateCode.ERROR, "不存在该分销商的资质记录，请核查！");
			}
			
		} catch (Exception e) {
			logger.error("修改数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
}
