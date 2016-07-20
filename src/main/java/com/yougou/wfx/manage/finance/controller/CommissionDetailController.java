 /*
 * 版本信息
 
 * 日期 2016-03-29 10:46:57
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.finance.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.finance.vo.CommissionDetailPageVo;
import com.yougou.wfx.util.ExportXLSUtil;
import com.yougou.wfx.util.HttpUtil;
import com.yougou.wfx.finance.api.background.ICommissionDetailBackgroundApi;
import com.yougou.wfx.finance.dto.input.CommissionDetailInputDto;
import com.yougou.wfx.finance.dto.input.CommissionDetailPageInputDto;
import com.yougou.wfx.finance.dto.output.CommissionDetailOutputDto;

/**
 * CommissionDetailController
 * @author langqiwei
 * @Date 创建时间：2016-03-29 10:46:58
 */
@Controller
@RequestMapping("/finance")
public class CommissionDetailController extends BaseController{
	
	@Resource
	private ICommissionDetailBackgroundApi commissionDetailBackgroundApi;
	/**
	 * 进入菜单
	 */
	@RequestMapping("/commissionDetailList")
	public String commissionDetailList(ModelMap modelMap,String status){
	    if(StringUtils.isBlank(status)){
	        status="0";//设置默认值为未结算
	    }
        modelMap.addAttribute("status",status);
		return "/manage/finance/commissionDetailList";
	}
	
	/**
     * 佣金明细汇总生成调度
     */
    @RequestMapping("/dispatchCreateAccountBalance")
    public String dispatchCreateAccountBalance(ModelMap modelMap,String status){
        if(StringUtils.isBlank(status)){
            status="0";//设置默认值为未结算
        }
        try {
            commissionDetailBackgroundApi.dispatchCreateAccountBalance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        modelMap.addAttribute("status",status);
        return "/manage/finance/commissionDetailList";
    }
	
	/**
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryCommissionDetailData")
	@ResponseBody
	public String queryCommissionDetailData(ModelMap map, CommissionDetailPageVo commissionDetailVo, PageModel<CommissionDetailOutputDto> pageModel)throws Exception{
	    CommissionDetailPageInputDto pageInputDto = (CommissionDetailPageInputDto) BeanUtil.convertBean(commissionDetailVo, CommissionDetailPageInputDto.class);
		PageModel<CommissionDetailOutputDto> result = commissionDetailBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
     * 导出佣金明细数据
     */
	@RequestMapping("/exportCommissionDetail")
	public void exportCommissionDetail(CommissionDetailPageVo commissionDetailVo, HttpServletRequest request,HttpServletResponse response)throws Exception{
        CommissionDetailPageInputDto pageInputDto = (CommissionDetailPageInputDto) BeanUtil.convertBean(commissionDetailVo, CommissionDetailPageInputDto.class);
        List<CommissionDetailOutputDto> exportList= commissionDetailBackgroundApi.exportCommissionDetail(pageInputDto);
        List<Object[]> dataList = new ArrayList<Object[]>();
        String title = "佣金明细导出";
        String[] headers = {"订单号", "下单时间", "分销商编码", "分销商账号", "商品编码", "商家编码", "品牌", "一级分类", "二级分类","确认收货/退货时间", "结算类型",
                            "数量",   "单价",   "商品金额", "运费",     "扣佣比例", "佣金收益", "状态", "结算时间", "创建时间", "备注"};
        Boolean[] amounts = {false, false, false, false, false, false, false, false, false, false, false,
                             false, true,  true,  true,  false, true,  false, false, false, false};
        if("2".equals(commissionDetailVo.getStatus())){
            headers[18]="操作时间";
        }
        if (exportList != null && exportList.size() > 0) {
            for (CommissionDetailOutputDto dto : exportList) {
                Object[] obj = new Object[21];
                obj[0] = dto.getWfxOrderDetailNo();
                obj[1] = dto.getOrderTime();
                obj[2] = dto.getWfxShopName();
                obj[3] = dto.getLoginName();
                obj[4] = dto.getCommodityNo();
                obj[5] = dto.getSupplierCode();
                obj[6] = dto.getBrandNo();
                obj[7] = dto.getBaseCatId1();
                obj[8] = dto.getBaseCatId2();
                obj[9] = dto.getConfirmTime();
                String settlementType=dto.getSettlementType();
                if ("0".equals(settlementType)){
                    obj[10]="妥投结算";
                }else if("1".equals(settlementType)){
                    obj[10]="退货结算";
                }
                obj[11] = dto.getCommodityQuantity();
                obj[12] = dto.getGoodsPrice();
                obj[13] = dto.getGoodsAmount();
                obj[14] = dto.getFreightCharges();
                obj[15] = dto.getDeductRatio()+"%";
                obj[16] = dto.getCommissionAmount();
                String status = dto.getStatus();
                if ("0".equals(status)){
                    obj[17] ="未结算";
                }else if("1".equals(status)){
                    obj[17] ="已结算";
                }else if("2".equals(status)){
                    obj[17] ="异常挂起";
                }else if("3".equals(status)){
                    obj[17] ="已关闭";
                }
                obj[18] = dto.getUpdateTime();
                obj[19] = dto.getCreateTime();
                obj[20] = dto.getRemark();
                dataList.add(obj);
            }
        }
        HSSFWorkbook workbook = ExportXLSUtil.exportExcel("FinanceData", headers, dataList, amounts);
        this.logger.info("佣金明细导出记录数："+exportList.size());
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel;charset=UTF-8"); 
            if (StringUtils.isNotBlank(title)) {
                if (1 == HttpUtil.getBrowingType(request)) {
                    title = URLEncoder.encode(title, "UTF-8");
                } else {
                    title = new String(title.getBytes("UTF-8"), "ISO-8859-1");
                }
            }
            response.setHeader("Content-disposition","attachment; filename="+title+".xls");  
            workbook.write(outputStream);
            response.flushBuffer();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            this.logger.error("导出佣金明细报表异常...", e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                    outputStream = null;
                }
                if (workbook != null) {
                    workbook = null;
                }
                if (dataList != null) {
                    dataList = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	  

        
	/**
     * 异常挂起处理界面
     */
    @RequestMapping("/commissionExceptionPage")
    public String commissionExceptionPage(ModelMap modelMap,String id){
        CommissionDetailOutputDto commissionDetailVo= commissionDetailBackgroundApi.getById(id);
        modelMap.addAttribute("commissionDetailVo",commissionDetailVo);//设置默认值为未结算
        return "/manage/finance/commissionExceptionPage";
    }
	
    /**
     * 异常挂起处理-保存更新
     */
    @RequestMapping("/commissionExceptionDeal")
	@ResponseBody
	public String commissionExceptionDeal(CommissionDetailInputDto commissionDetail) {
		try {
			String id = commissionDetail.getId();
			if (StringUtils.isNotBlank(id)) {
			    if("2".equals(commissionDetail.getDealType())){//处理方式为：异常关闭，同时更新状态为“已关闭”
			        commissionDetail.setStatus("3");
			    }else if("1".equals(commissionDetail.getDealType())){//处理方式为：重新结算，同时更新状态为“未结算”
			        commissionDetail.setStatus("0");
			    }
			    commissionDetail.setUpdateTime(new Date());
				commissionDetailBackgroundApi.update(commissionDetail);
			}
		} catch (Exception e) {
			logger.error("保存数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
}
