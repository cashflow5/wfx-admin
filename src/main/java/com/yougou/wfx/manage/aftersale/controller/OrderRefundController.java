 /*
 * 版本信息
 
 * 日期 2016-03-30 16:50:25
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.aftersale.controller;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.yougou.wfx.aftersale.api.background.IOrderRefundBackgroundApi;
import com.yougou.wfx.aftersale.dto.input.OrderRefundInputDto;
import com.yougou.wfx.aftersale.dto.input.OrderRefundPageInputDto;
import com.yougou.wfx.aftersale.dto.output.OrderRefundOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.aftersale.vo.OrderRefundPageVo;
import com.yougou.wfx.order.api.background.IOrderLogBackgroundApi;
import com.yougou.wfx.order.dto.input.OrderLogPageInputDto;
import com.yougou.wfx.order.dto.output.OrderLogOutputDto;

/**
 * OrderRefundController
 * @author luoq
 * @Date 创建时间：2016-03-30 16:50:25
 */
@Controller
@RequestMapping("/afterSale")
public class OrderRefundController extends BaseController{
	
	@Resource
	private IOrderRefundBackgroundApi orderRefundBackgroundApi;
	
	@Resource
	private IOrderLogBackgroundApi orderLogBackgroundApi;

	/**
	 * 进入菜单,分页查询数据
	 */
	@RequestMapping("/afterSaleList")
	public String afterSaleList(ModelMap model, OrderRefundPageVo orderRefundVo, PageModel<OrderRefundOutputDto> pageModel)throws Exception{
		return "/manage/aftersale/afterSaleList";
	}
	
	/**
	 * 进入菜单,分页查询数据
	 */
	@RequestMapping("/queryAfterSaleList")
	public String queryAfterSaleList(ModelMap model, OrderRefundPageVo orderRefundVo, PageModel<OrderRefundOutputDto> pageModel)throws Exception{
		OrderRefundPageInputDto pageInputDto = (OrderRefundPageInputDto) BeanUtil.convertBean(orderRefundVo, OrderRefundPageInputDto.class);
		PageModel<OrderRefundOutputDto> pageData = orderRefundBackgroundApi.findPage(pageInputDto, pageModel);
		model.addAttribute("vo", orderRefundVo);
	    model.addAttribute("pageData", pageData);
		return "/manage/aftersale/afterSaleList";
	}
	
	@RequestMapping("/getRefundDetails")
	public String getRefundDetailsById(ModelMap model,String refundNo) throws Exception {
		
		OrderRefundOutputDto orderRefund = orderRefundBackgroundApi.getDetailByRefundNo(refundNo);
		model.addAttribute("vo", orderRefund);
		
		OrderLogPageInputDto pageInputDto = new OrderLogPageInputDto();
		pageInputDto.setLogType(2);//退货日志类型
		pageInputDto.setRejectedNo(refundNo);
		List<OrderLogOutputDto> logList = orderLogBackgroundApi.queryList(pageInputDto);
		model.addAttribute("logList", logList);
		return "/manage/aftersale/afterSaleDetails";
	}
	
	@RequestMapping("/auditRefund")
	@ResponseBody
	public String auditRefund(String refundNo,int manualRefundFlag,String denyReason) {
		try {
			Assert.hasText(refundNo, "参数refundNo不能为空");
			OrderRefundOutputDto outputDto = orderRefundBackgroundApi.getDetailByRefundNo(refundNo);
			if( null!=outputDto ){
				String operator = getSysUserName();
				String result = orderRefundBackgroundApi.auditRefund(operator, manualRefundFlag, denyReason,outputDto);
				if( result!=null && result.startsWith("error1:") ){
					return super.jsonResult(StateCode.ERROR, "调财务接口生成退款单失败:"+result.substring(7) );
				}else if( result!=null && result.equals("error2") ){
					return super.jsonResult(StateCode.ERROR, "参数不能为空" );
				}else if( result!=null && result.equals("SUCCESS") ){
					return super.jsonResult(StateCode.SUCCESS, SUCCESS);
				}else  if( result!=null && result.equals("error3") ){
					return super.jsonResult(StateCode.ERROR, "本地数据库审核退款单失败！");
				}else{
					return result;
				}
			}else{
				return super.jsonResult(StateCode.ERROR, "数据库未定位到该条退款编号，请检查！");
			}
		} catch (Exception ex) {
			logger.error("后台进行退款审核操作异常,退款编号："+refundNo, ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		
	}
	
	@RequestMapping("/saveAfterSale")
	@ResponseBody
	public String saveAfterSale(OrderRefundInputDto orderRefund) {
		try {
			String id = orderRefund.getId();
			if (StringUtils.isNotBlank(id)) {
				orderRefundBackgroundApi.update(orderRefund);
			}else{
				orderRefundBackgroundApi.insert(orderRefund);
			}
		} catch (Exception e) {
			logger.error("保存数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
}
