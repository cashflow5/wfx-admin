 /*
 * 版本信息
 
 * 日期 2016-03-24 16:58:55
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.commodity.controller;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.yougou.pc.model.category.Category;
import com.yougou.wfx.basicset.api.background.ISysConfigBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityCatb2cBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityStyleBackgroundApi;
import com.yougou.wfx.commodity.api.background.ISysLogBackgroundApi;
import com.yougou.wfx.commodity.dto.input.CommodityCatb2cInputDto;
import com.yougou.wfx.commodity.dto.input.CommodityStyleInputDto;
import com.yougou.wfx.commodity.dto.input.CommodityStylePageInputDto;
import com.yougou.wfx.commodity.dto.input.SysLogInputDto;
import com.yougou.wfx.commodity.dto.output.CommodityCatb2cOutputDto;
import com.yougou.wfx.commodity.dto.output.CommodityStyleOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.enums.BusinessTypeEnum;
import com.yougou.wfx.enums.OperateTypeEnum;
import com.yougou.wfx.finance.api.background.ICommissionPercentBackgroundApi;
import com.yougou.wfx.finance.dto.output.CommissionPercentOutputDto;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.commodity.vo.CommodityStylePageVo;
import com.yougou.wfx.util.ApiConstant;
import com.yougou.wfx.util.ExportXLSUtil;
import com.yougou.wfx.util.HttpUtil;
import com.yougou.wfx.util.UserSecurityUtil;
/**
 * CommodityStyleController
 * @author zhang.wj
 * @Date 创建时间：2016-03-24 16:58:55
 */
@Controller
@RequestMapping("/commodity")
public class CommodityStyleController extends BaseController{
	
	@Resource
	private ICommodityStyleBackgroundApi commodityStyleBackgroundApi;
	@Resource
	private ICommodityCatb2cBackgroundApi commodityCatb2cBackgroundApi;
	@Resource
	private ISysLogBackgroundApi  sysLogBackgroundApi;
	@Resource 
	private ISysConfigBackgroundApi sysConfigBackgroundApi;
	@Resource 
	private ICommissionPercentBackgroundApi commissionPercentBackgroundApi;
	
	 
	/**
	 * 进入菜单
	 */
	@RequestMapping("/commodityList")
	public String commodityList(ModelMap map,HttpServletRequest request){
	/*	Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("no", "100056607");
		frontApi.getCommodityByParameter(paramMap);*/
		//一级基础分类
		String commodityFlag=(String)request.getParameter("commodityFlag");
		CommodityCatb2cInputDto basicCatInputDto = new CommodityCatb2cInputDto();
		basicCatInputDto.setParentId("0");
		List<CommodityCatb2cOutputDto> basicList = commodityCatb2cBackgroundApi.queryList(basicCatInputDto);
		map.put("basicList", basicList);
		String postFee =sysConfigBackgroundApi.getValueBykey(ApiConstant.WFX_POSTFEE );//运费
		String taxRate =sysConfigBackgroundApi.getValueBykey(ApiConstant.TAX_RATE);//个人所得税率
		String vatRate =sysConfigBackgroundApi.getValueBykey(ApiConstant.VAT_RATE );//增值税税率
		postFee = (postFee==null)?"0":postFee;
		taxRate = (taxRate==null)?"0":taxRate;
		vatRate = (vatRate==null)?"0":vatRate;
		map.put("postFee",postFee);
		map.put("taxRate",taxRate);
		map.put("vatRate",vatRate);
		map.put("commodityFlag",commodityFlag);
		return "/manage/commodity/commodityList";
	}
	/**
	 * 进入菜单
	 */
	@RequestMapping("/wfxCommodityOperateList")
	public String wfxCommodityOperateList(ModelMap map,HttpServletRequest request){
	/*	Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("no", "100056607");
		frontApi.getCommodityByParameter(paramMap);*/
		//一级基础分类
		String commodityFlag=(String)request.getParameter("commodityFlag");
		
		CommodityCatb2cInputDto basicCatInputDto = new CommodityCatb2cInputDto();
		basicCatInputDto.setParentId("0");
		List<CommodityCatb2cOutputDto> basicList = commodityCatb2cBackgroundApi.queryList(basicCatInputDto);
		map.put("basicList", basicList);
		String postFee =sysConfigBackgroundApi.getValueBykey(ApiConstant.WFX_POSTFEE );//运费
		String taxRate =sysConfigBackgroundApi.getValueBykey(ApiConstant.TAX_RATE);//个人所得税率
		String vatRate =sysConfigBackgroundApi.getValueBykey(ApiConstant.VAT_RATE );//增值税税率
		postFee = (postFee==null)?"0":postFee;
		taxRate = (taxRate==null)?"0":taxRate;
		vatRate = (vatRate==null)?"0":vatRate;
		map.put("postFee",postFee);
		map.put("taxRate",taxRate);
		map.put("vatRate",vatRate);
		map.put("commodityFlag",commodityFlag);
		
		return "/manage/commodity/wfxCommodityOperateList";
	}
	
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/wfxCommodityList")
	public String wfxCommodityList(ModelMap map,HttpServletRequest request){
	/*	Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("no", "100056607");
		frontApi.getCommodityByParameter(paramMap);*/
		//一级基础分类
		
		CommodityCatb2cInputDto basicCatInputDto = new CommodityCatb2cInputDto();
		basicCatInputDto.setParentId("0");
		List<CommodityCatb2cOutputDto> basicList = commodityCatb2cBackgroundApi.queryList(basicCatInputDto);
		map.put("basicList", basicList);
		String postFee =sysConfigBackgroundApi.getValueBykey(ApiConstant.WFX_POSTFEE );//运费
		String taxRate =sysConfigBackgroundApi.getValueBykey(ApiConstant.TAX_RATE);//个人所得税率
		String vatRate =sysConfigBackgroundApi.getValueBykey(ApiConstant.VAT_RATE );//增值税税率
		postFee = (postFee==null)?"0":postFee;
		taxRate = (taxRate==null)?"0":taxRate;
		vatRate = (vatRate==null)?"0":vatRate;
		map.put("postFee",postFee);
		map.put("taxRate",taxRate);
		map.put("vatRate",vatRate);
		
		return "/manage/commodity/wfxCommodityList";
	}
	
	
	/**
	 * 获取一级基础分类
	 */
	@RequestMapping("/queryBasicList")
	@ResponseBody
	public String queryBasicList(){
		//一级基础分类
		CommodityCatb2cInputDto basicCatInputDto = new CommodityCatb2cInputDto();
		basicCatInputDto.setParentId("0");
		List<CommodityCatb2cOutputDto> basicList = commodityCatb2cBackgroundApi.queryList(basicCatInputDto);
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, basicList);
	}
	/**
	 * 获取微分销分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryWfxCommodityData")
	@ResponseBody
	public String queryWfxCommodityData(ModelMap map, CommodityStylePageVo commodityStyleVo, PageModel<CommodityStyleOutputDto> pageModel)throws Exception{
		
		int  warehouseFlag=1;
		
		CommodityStylePageInputDto pageInputDto = (CommodityStylePageInputDto) BeanUtil.convertBean(commodityStyleVo, CommodityStylePageInputDto.class);
		
		PageModel<CommodityStyleOutputDto> result = commodityStyleBackgroundApi.findPage(pageInputDto, pageModel,warehouseFlag);
		
		//一级基础分类
		CommodityCatb2cInputDto basicCatInputDto = new CommodityCatb2cInputDto();
		basicCatInputDto.setParentId("0");
		List<CommodityCatb2cOutputDto> basicList = commodityCatb2cBackgroundApi.queryList(basicCatInputDto);
		
		map.put("basicList", basicList);
		
		return JSONObject.toJSONString(result);
	}
	/**
	 * 获取优购库分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryCommodityData")
	@ResponseBody
	public String queryCommodityData(ModelMap map, CommodityStylePageVo commodityStyleVo, PageModel<CommodityStyleOutputDto> pageModel)throws Exception{
		//查询优购库
		int  warehouseFlag=2;
		CommodityStylePageInputDto pageInputDto = (CommodityStylePageInputDto) BeanUtil.convertBean(commodityStyleVo, CommodityStylePageInputDto.class);
		
		PageModel<CommodityStyleOutputDto> result = commodityStyleBackgroundApi.findPage(pageInputDto, pageModel,warehouseFlag);
		
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 查询商品信息，并返回json格式，供发现功能的文章使用
	 */
	@RequestMapping("/queryDisStyleInfo")
	@ResponseBody
	public String queryDisStyleInfo(ModelMap map, CommodityStylePageVo commodityStyleVo, PageModel pageModel)throws Exception{
		CommodityStylePageInputDto pageInputDto = (CommodityStylePageInputDto) BeanUtil.convertBean(commodityStyleVo, CommodityStylePageInputDto.class);
		PageModel<CommodityStyleOutputDto> result = commodityStyleBackgroundApi.queryDiscoverStyleInfo(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}

	/**
	 * 保存添加至微分销
	 */
	@RequestMapping("/ommodityAddToWfx")
	@ResponseBody
	public String commodityAddToWfx(ModelMap map,HttpServletRequest request, CommodityStylePageVo commodityStyleVo)throws Exception{
		List<CommodityStyleInputDto>  wfxCommodityList=new ArrayList<CommodityStyleInputDto>();
		//组装保存信息
		CommodityStyleInputDto inputDto = (CommodityStyleInputDto) BeanUtil.convertBean(commodityStyleVo, CommodityStyleInputDto.class);
		inputDto.setIsWfxCommodity(1);
		//inputDto.setWfxCommodityNo();
		//添加到微分销商品库中，商品状态为“未上架”状态
		inputDto.setIsOnsale(3);
		inputDto.setAddWfxTime(new Date());
		inputDto.setUpdateDate(new Date());
		inputDto.setSupplierId("GHS2016098767");
		CommodityStyleOutputDto  commodityStyleOutputDto=commodityStyleBackgroundApi.getById(inputDto.getId());
		Double distributableMoney = null;
		try {
			distributableMoney = commodityStyleBackgroundApi.computeDistributableMoney( inputDto.getWfxPrice()  , commodityStyleOutputDto.getCostPrice());
		} catch (Exception e) {
			logger.error( e.getMessage() );
			
		}
		inputDto.setDistributableMoney(distributableMoney);
		wfxCommodityList.add(inputDto);
		commodityStyleBackgroundApi.batchCommodityAddToWfx(wfxCommodityList);
		Map<String,Integer> resultMap = new HashMap<String,Integer> ();
		resultMap.put("resultCount", 1);
		Double wfxPrice=commodityStyleOutputDto.getWfxPrice();
		wfxPrice=wfxPrice!=null?wfxPrice:0;
		Double wfxCostPrice=commodityStyleOutputDto.getWfxCostPrice();
		wfxCostPrice=wfxCostPrice!=null?wfxCostPrice:0;
		String operateContent="款色ID："+commodityStyleOutputDto.getNo()+"原商品库：【优购商品库】添加至【分销商品库】"+"市场价"+commodityStyleOutputDto.getPublicPrice()+"微分销价"+wfxPrice+"修改为"+inputDto.getWfxPrice()+"微分销成本价"+wfxCostPrice+"修改为"+inputDto.getWfxCostPrice();
		String remark = commodityStyleOutputDto.getNo()+"#"+commodityStyleOutputDto.getSupplierCode() +"#"+commodityStyleOutputDto.getStyleNo();
		this.addLogInfo(BusinessTypeEnum.COMMODITY_CREATE.getKey(),OperateTypeEnum.ADD.getKey(),operateContent,remark);
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, resultMap);
	}
	/**
	 * 保存日志
	 * @param businessType
	 * @param opierateType
	 */
	private void  addLogInfo(String businessType,String operateType,String operateContent,String remark){
		try{
			SysLogInputDto sysLogDto=new SysLogInputDto();
			sysLogDto.setModule("COMMODITY");
			sysLogDto.setOperateUser(UserSecurityUtil.getSystemUserName());
			sysLogDto.setOperateDate(new Date());
			sysLogDto.setBusinessType(businessType);
			sysLogDto.setOperateType(operateType);
			sysLogDto.setOperateContent(operateContent);
			sysLogDto.setRemark(remark);
			sysLogBackgroundApi.insertSysLog(sysLogDto);
		}catch(Exception e){
			logger.error("记录日志发生异常",e);
		}
	}
	/**
	 * 批量保存添加至微分销
	 */
	@RequestMapping("/batchCommodityAddToWfx")
	@ResponseBody
	public String batchCommodityAddToWfx(ModelMap map,HttpServletRequest request, CommodityStyleInputDto CommodityStyleInputDto)throws Exception{
		//商品id
		String commodityIds=request.getParameter("commodityIds");
		Map<String,Integer> resultMap = new HashMap<String,Integer> ();
		List<CommodityStyleInputDto>  wfxCommodityList=new ArrayList<CommodityStyleInputDto>();
		if(commodityIds!=null && ! commodityIds.equals("")){
			String [] commodityArray=commodityIds.split(",");
			for (int i = 0; i < commodityArray.length; i++) {
				//组装保存信息
				CommodityStyleInputDto inputDto =new  CommodityStyleInputDto();
				inputDto.setId(commodityArray[i]);
				inputDto.setIsWfxCommodity(1);
				inputDto.setWfxCommodityNo("");
				inputDto.setSupplierId("GHS2016098767");
				//添加到微分销商品库中，商品状态为“未上架”状态
				inputDto.setIsOnsale(3);
				inputDto.setUpdateDate(new Date());
				inputDto.setAddWfxTime(new Date());
				wfxCommodityList.add(inputDto);
			}
			resultMap.put("resultCount", 1);
		}else{
			
			resultMap.put("resultCount", 0);
		}
		
		commodityStyleBackgroundApi.batchCommodityAddToWfx(wfxCommodityList);
		
		if(commodityIds!=null && ! commodityIds.equals("")){
			String [] commodityArray=commodityIds.split(",");
			for (int i = 0; i < commodityArray.length; i++) {
				CommodityStyleOutputDto  commodityStyleOutputDto=commodityStyleBackgroundApi.getById(commodityArray[i]);
				String operateContent="批量添加到微分销，款色ID："+commodityStyleOutputDto.getNo();
				String remark = commodityStyleOutputDto.getNo()+"#"+commodityStyleOutputDto.getSupplierCode() +"#"+commodityStyleOutputDto.getStyleNo();
				this.addLogInfo(BusinessTypeEnum.COMMODITY_CREATE.getKey(),OperateTypeEnum.ADD.getKey(),operateContent,remark);
			}
			resultMap.put("resultCount", 1);
		}
		
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, resultMap);
	}
	/**
	 * 修改上下架
	 */
	@RequestMapping("/updateShelvesStatus")
	@ResponseBody
	public   String  updateShelvesStatus(ModelMap map,HttpServletRequest request, HttpServletResponse response){
		Map<String,Integer> resultMap = new HashMap<String,Integer> ();
		try {
			//拼装上下架状态信息
			String commodityId=(String)request.getParameter("id");
			Integer  showind=Integer.valueOf(request.getParameter("showind"));
			if( showind==1 ){
				// 校验
				String checkResult = checkShelve(commodityId);
				if( "".equals(checkResult) ){
					
				}else{
					resultMap.put("resultCount", -1);
					return super.jsonResult(StateCode.SUCCESS, checkResult, resultMap);
				}
			}
			
			List<CommodityStyleInputDto>  arrayList=new ArrayList<CommodityStyleInputDto>();
			CommodityStyleInputDto   dto=new CommodityStyleInputDto();
			dto.setId(commodityId);
			dto.setIsOnsale(showind);
			dto.setUpdateDate(new Date());
			arrayList.add(dto);
			commodityStyleBackgroundApi.batchUpdateShelvesStatus(arrayList);
			
			resultMap.put("resultCount", 1);
			String str="";
			if( showind==ApiConstant.ON_SHOW ){
				str="由【下架】修改为【上架】";
//				// 上架的时候，商品的款号加入总经销商代理
//				commodityStyleBackgroundApi.batchAddProxyToOrigianlSeller(arrayList);
			}else{
				str="由【上架】修改为【下架】";
			}
			CommodityStyleOutputDto  commodityStyleOutputDto=commodityStyleBackgroundApi.getById(commodityId);
			String operateContent="款色ID："+commodityStyleOutputDto.getNo()+str;
			String remark = commodityStyleOutputDto.getNo()+"#"+commodityStyleOutputDto.getSupplierCode() +"#"+commodityStyleOutputDto.getStyleNo();
			this.addLogInfo(BusinessTypeEnum.COMMODITY_ON_SHELVES.getKey(),OperateTypeEnum.EDIT.getKey(),operateContent,remark);
			
		} catch (Exception e) {
			resultMap.put("resultCount", 0);
		}
		
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, resultMap);
	}
	/**
	 * 批量修改上下架状态
	 * @param map
	 * @return
	 */
	/**
	 * 修改上下架
	 */
	@RequestMapping("/batchUpdateShelvesStatus")
	@ResponseBody
    public   String  batchUpdateShelvesStatus(HttpServletRequest request,ModelMap map){
		Map<String,Integer> resultMap = new HashMap<String,Integer> ();
		String checkNotPass = "";
		try {
	    	//拼装批量修改上下架信息
	    	List<CommodityStyleInputDto>  arrayList=new ArrayList<CommodityStyleInputDto>();
	    	String commodityIds=(String)request.getParameter("commodityIds");
	    	Integer status=Integer.valueOf(request.getParameter("status"));
	    	
	    	if(commodityIds!=null && !commodityIds.equals("")){
	    		
	    		if(status==1){ // 上架

		    		String [] commodityArray=commodityIds.split(",");
		    		for (int i = 0; i < commodityArray.length; i++) {
		    			
		    			// 校验佣金比例
		    			String checkResult = checkShelve(commodityArray[i]);
		    			if( StringUtils.isNotBlank(checkResult) ){
		    				checkNotPass = checkNotPass +" "+checkResult;
		    				continue;
		    			}
		    			
		    			CommodityStyleInputDto   dto=new CommodityStyleInputDto();
		    			dto.setId(commodityArray[i]);
		    			dto.setIsOnsale(status);
		    			dto.setUpdateDate(new Date());
		    			arrayList.add(dto);
		    			String str="";
		    			if(status== ApiConstant.ON_SHOW ){
		    				str="由【下架】修改为【上架】";
		    			}else{
		    				str="由【上架】修改为【下架】";
		    			}
		    			CommodityStyleOutputDto  commodityStyleOutputDto=commodityStyleBackgroundApi.getById(commodityArray[i]);
		    			
		    			String operateContent="款色ID："+commodityStyleOutputDto.getNo()+str;
		    			String remark = commodityStyleOutputDto.getNo()+"#"+commodityStyleOutputDto.getSupplierCode() +"#"+commodityStyleOutputDto.getStyleNo();
		    			this.addLogInfo(BusinessTypeEnum.COMMODITY_ON_SHELVES.getKey(),OperateTypeEnum.EDIT.getKey(),operateContent,remark);
					}
		    		
		    		if( arrayList.size()>0 ){
			    		commodityStyleBackgroundApi.batchUpdateShelvesStatus(arrayList);
			    		
//			    		if(status== ApiConstant.ON_SHOW ){
//			    			// 上架的时候，商品的款号加入总经销商代理
//							commodityStyleBackgroundApi.batchAddProxyToOrigianlSeller(arrayList);
//		    			}
		    		}
		    		
		    		if( StringUtils.isNotBlank( checkNotPass ) ){
		    			resultMap.put("resultCount", -1);
		    		}else{
		    			resultMap.put("resultCount", 1);
		    		}
		    		return super.jsonResult(StateCode.SUCCESS, checkNotPass, resultMap);
		    		
	    		}else{ // 下架
	    		
		    		String [] commodityArray=commodityIds.split(",");
		    		for (int i = 0; i < commodityArray.length; i++) {
		    			CommodityStyleInputDto   dto=new CommodityStyleInputDto();
		    			dto.setId(commodityArray[i]);
		    			dto.setIsOnsale(status);
		    			dto.setUpdateDate(new Date());
		    			arrayList.add(dto);
		    			String str="";
		    			if(status== ApiConstant.ON_SHOW ){
		    				str="由【下架】修改为【上架】";
		    			}else{
		    				str="由【上架】修改为【下架】";
		    			}
		    			CommodityStyleOutputDto  commodityStyleOutputDto=commodityStyleBackgroundApi.getById(commodityArray[i]);
		    			
		    			String operateContent="款色ID："+commodityStyleOutputDto.getNo()+str;
		    			String remark = commodityStyleOutputDto.getNo()+"#"+commodityStyleOutputDto.getSupplierCode() +"#"+commodityStyleOutputDto.getStyleNo();
		    			this.addLogInfo(BusinessTypeEnum.COMMODITY_ON_SHELVES.getKey(),OperateTypeEnum.EDIT.getKey(),operateContent,remark);
					}
		    		commodityStyleBackgroundApi.batchUpdateShelvesStatus(arrayList);
		    		
//		    		if(status== ApiConstant.ON_SHOW ){
//		    			// 上架的时候，商品的款号加入总经销商代理
//						commodityStyleBackgroundApi.batchAddProxyToOrigianlSeller(arrayList);
//	    			}
		    		resultMap.put("resultCount", 1);
	    		}
	    	}
	    	
		} catch (Exception e) {
			resultMap.put("resultCount", 0);
		}
		
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, resultMap);
	}
	/**
	 * 调价
	 */
	@RequestMapping("/readjustPrices")
	@ResponseBody
	public String readjustPrices(ModelMap map,HttpServletRequest request, CommodityStylePageVo commodityStyleVo){
		List<CommodityStyleInputDto>  wfxCommodityList=new ArrayList<CommodityStyleInputDto>();
		Map<String,Integer> resultMap = new HashMap<String,Integer> ();
		try {
			//组装保存信息
			CommodityStyleInputDto inputDto = (CommodityStyleInputDto) BeanUtil.convertBean(commodityStyleVo, CommodityStyleInputDto.class);
			inputDto.setWfxCostPrice(commodityStyleVo.getWfxCostPrice());
			inputDto.setUpdateDate(new Date());
			CommodityStyleOutputDto  commodityStyleOutputDto=commodityStyleBackgroundApi.getById(inputDto.getId());
			Double distributableMoney = null;
			try {
				distributableMoney = commodityStyleBackgroundApi.computeDistributableMoney( inputDto.getWfxPrice()  , commodityStyleOutputDto.getCostPrice());
			} catch (Exception e) {
				logger.error( e.getMessage() );
				
			}
			
			inputDto.setDistributableMoney(distributableMoney);
			wfxCommodityList.add(inputDto);
			Double wfxPrice=commodityStyleOutputDto.getWfxPrice();
			wfxPrice=wfxPrice!=null?wfxPrice:0;
			Double wfxCostPrice=commodityStyleOutputDto.getWfxCostPrice();
			wfxCostPrice=wfxCostPrice!=null?wfxCostPrice:0;
			String operateContent="款色ID："+commodityStyleOutputDto.getNo()+"原商品库：【优购商品库】添加至【分销商品库】"+"市场价"+commodityStyleOutputDto.getPublicPrice()+"微分销价"+wfxPrice+"修改为"+inputDto.getWfxPrice()+"微分销成本价"+wfxCostPrice+"修改为"+inputDto.getWfxCostPrice();
			
			//String operateContent="款色ID："+commodityStyleOutputDto.getNo()+"微分销价："+commodityStyleOutputDto.getWfxPrice()+"微分销成本价格"+commodityStyleOutputDto.getWfxCostPrice();
			String remark = commodityStyleOutputDto.getNo()+"#"+commodityStyleOutputDto.getSupplierCode() +"#"+commodityStyleOutputDto.getStyleNo();
			this.addLogInfo(BusinessTypeEnum.COMMODITY_PRICE_UP.getKey(),OperateTypeEnum.EDIT.getKey(),operateContent,remark);
			commodityStyleBackgroundApi.readjustPrices(wfxCommodityList);
			resultMap.put("resultCount", 1);
		} catch (Exception e) {
			resultMap.put("resultCount", 0);
		}
		
		
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, resultMap);
	}
	
//	/**
//	 * 检查商品佣金比例是否有为0的，返回不合格(佣金比例为空或者小于等于0)的款色ID
//	 * @param commodityIds
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("/checkShelves")
//	@ResponseBody
//	public String checkShelves(String commodityIds) throws Exception {
//		String notQualifierNos = "";
//		try {
////			Assert.hasText(commodityIds, "commodityIds不能为空！");
//			if(StringUtils.isNotBlank(commodityIds)){
//				String [] commodityArray=commodityIds.split(",");
//				List<String> commodityList = new ArrayList<String>();
//				Collections.addAll(commodityList, commodityArray);
//				List<CommodityStyleOutputDto>  dtoList = commodityStyleBackgroundApi.queryExportCommodityInfo(commodityList);
//				
//				if(dtoList!=null && dtoList.size()>0 ){
//					for (int i = 0; i < dtoList.size(); i++) {
//						CommodityStyleOutputDto dto = dtoList.get(i);
//						String wfxCommodityNo = dto.getWfxCommodityNo();
//						CommissionPercentOutputDto percentOutputDto = 
//								commissionPercentBackgroundApi.getCommissionByCondition(dto.getBrandNo(), dto.getBaseCatId(), dto.getId() );
//						if( percentOutputDto!=null 
//								&& isNotZero(percentOutputDto.getCommissionLevel1Percent()) 
//									&& isNotZero(percentOutputDto.getCommissionLevel2Percent()) 
//										&& isNotZero(percentOutputDto.getCommissionLevel3Percent()) ){
//							continue;
//						}else{
//							 if( StringUtils.isBlank(notQualifierNos) ){
//								 notQualifierNos = wfxCommodityNo;
//							 }else{
//								 notQualifierNos = notQualifierNos+","+wfxCommodityNo;
//							 }
//						}
//					}
//				}else{
//					logger.error("上架之前检查:未查到Ids对应商品数据，"+commodityIds);
//					return "false";
//				}
//			}
//		} catch (Exception e) {
//			logger.error("上架之前检查佣金比例时查询数据异常", e);
//			return "false";
//		}
//		return notQualifierNos;
//	}
	
	public String checkShelve(String commodityId) throws Exception {
		try {
//			Assert.hasText(commodityIds, "commodityIds不能为空！");
			if(StringUtils.isNotBlank(commodityId)){
				List<String> commodityList = new ArrayList<String>();
				commodityList.add(commodityId);
				List<CommodityStyleOutputDto>  dtoList = commodityStyleBackgroundApi.queryExportCommodityInfo(commodityList);
				
				if(dtoList!=null && dtoList.size()>0 ){
						CommodityStyleOutputDto dto = dtoList.get(0);
						String no = dto.getNo();
						CommissionPercentOutputDto percentOutputDto = 
								commissionPercentBackgroundApi.getCommissionByCondition(dto.getBrandNo(), dto.getBaseCatId(), dto.getId() );
						if( percentOutputDto!=null 
								&& isNotZero(percentOutputDto.getCommissionLevel1Percent()) 
									&& isNotZero(percentOutputDto.getCommissionLevel2Percent()) 
										&& isNotZero(percentOutputDto.getCommissionLevel3Percent()) ){
							return "";
						}else{
							 return no;
						}
				}else{
					logger.error("上架之前检查:未查到Id对应商品数据，"+commodityId);
					return commodityId;
				}
			}
		} catch (Exception e) {
			logger.error("上架之前检查佣金比例时查询数据异常", e);
			return commodityId;
		}
		return commodityId;
	}
	
	private boolean isNotZero(Double percent){
		if( percent==null ){
			return false;
		}
		if( percent<=0d ){
			return false;
		}else{
			return true;
		}
	}
	
	
	
	@RequestMapping("/getCommodityById")
	@ResponseBody
	public String getCommodityById(String id) throws Exception {
		CommodityStyleOutputDto commodityStyle = new CommodityStyleOutputDto();
		try {
			Assert.hasText(id, "id不能为空！");
			commodityStyle = commodityStyleBackgroundApi.getById(id);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,e=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS, commodityStyle);
	}
	
	@RequestMapping("/removeCommodity")
	@ResponseBody
	public String removeCommodity(@RequestParam("id") String id) {
		try {
			Assert.hasText(id, "参数id不能为空");
			commodityStyleBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	@RequestMapping("/exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response,Map<String,Object> queryMap) {
		//选中的商品
		List<String> commodityIdList = new ArrayList<String>();
		String ids=request.getParameter("ids");
		if(ids!=null && !ids.equals("")){
			String []  commodityId=ids.split(",");
			for (int i = 0; i < commodityId.length; i++) {
				commodityIdList.add(commodityId[i]);
			}
		}
		//查询商品信息
		List<CommodityStyleOutputDto>  commodityList=commodityStyleBackgroundApi.queryExportCommodityInfo(commodityIdList);
		
		List<Object[]> dataList = new ArrayList<Object[]>();
		String title = "商品详情";
		String[] headers = {"款色ID", "商品名称", "市场价格", "优购价格","微分销成本价格", "微分销零售价"};
		Boolean[] amounts = {false, false, false, false, true, true, true};
		if (commodityList != null && commodityList.size() > 0) {
			for (CommodityStyleOutputDto commodityStyleOutputDto : commodityList) {
				Object[] obj = new Object[6];
				obj[0] = commodityStyleOutputDto.getNo();
				obj[1] = commodityStyleOutputDto.getCommodityName();
				obj[2] = commodityStyleOutputDto.getPublicPrice();
				obj[3] = commodityStyleOutputDto.getSalePrice();
				if(commodityStyleOutputDto.getWfxCostPrice()==null){
					Double   wfxCostPrice1=new Double(0.00);
					obj[4] =wfxCostPrice1;
				}else{
					obj[4] = commodityStyleOutputDto.getWfxCostPrice();
				}
				if(commodityStyleOutputDto.getWfxPrice()==null){
					Double   wfxPrice1=new Double(0.00);
					obj[5] =wfxPrice1;
				}else{
					obj[5] = commodityStyleOutputDto.getWfxPrice();
				}
				
				dataList.add(obj);
			}
		}
		HSSFWorkbook workbook = ExportXLSUtil.exportExcel("commodityData", headers, dataList, amounts);
		if (workbook == null) {
			return ;
		}
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
			this.logger.error("商品详情异常...", e);
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
		if (commodityList != null && commodityList.size() > 0) {
			for (CommodityStyleOutputDto commodityStyleOutputDto : commodityList) {
				String operateContent="微分销商品库导出商品,款色ID："+commodityStyleOutputDto.getNo();
				String remark = commodityStyleOutputDto.getNo()+"#"+commodityStyleOutputDto.getSupplierCode() +"#"+commodityStyleOutputDto.getStyleNo();
				this.addLogInfo(BusinessTypeEnum.COMMODITY_EXPORT.getKey(),OperateTypeEnum.EXPORT.getKey(),operateContent,remark);
			}
		}
		
	}
	
	/**
	 * 导出商品信息
	 * @param request
	 * @param response
	 * @param queryMap
	 * @throws Exception
	 */
	@RequestMapping("/commodityExcel")
	@ResponseBody
	private void commodityExcel(
				HttpServletRequest request, HttpServletResponse response,Map<String,Object> queryMap) throws Exception {
			
			//选中的商品
			List<String> commodityNoList = new ArrayList<String>();
			
			//查询商品信息
			List<CommodityStyleOutputDto>  commodityList=commodityStyleBackgroundApi.queryExportCommodityInfo(commodityNoList);
			
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("商品详情列表");
			int rowInd = 0, colInd = 0;
			HSSFCellStyle style = workbook.createCellStyle(); // 样式对象  
			HSSFRow  row =null;
			colInd = 0;
			HSSFPatriarch patr = sheet.createDrawingPatriarch();
			row = sheet.createRow(rowInd++);
			style = workbook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.CORAL.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			row.createCell(colInd).setCellValue("款色ID");
			row.getCell(colInd).setCellStyle(style);
			HSSFComment comment = patr.createCellComment(new HSSFClientAnchor(0,0,0,0,(short)colInd,row.getRowNum(),(short)(colInd+3),row.getRowNum()+3));
			
			row.getCell(colInd).setCellComment(comment);
			
			colInd++;
			row.createCell(colInd).setCellValue("商品名称");
			row.getCell(colInd).setCellStyle(style);
			row.getCell(colInd).setCellComment(comment);
			
			colInd++;
			row.createCell(colInd).setCellValue("商品卖点");
			row.getCell(colInd).setCellStyle(style);
			row.getCell(colInd).setCellComment(comment);
			
			colInd++;
			row.createCell(colInd).setCellValue("市场价格");
			row.getCell(colInd).setCellStyle(style);
			row.getCell(colInd).setCellComment(comment);
			colInd++;
			row.createCell(colInd).setCellValue("优购价格");
			row.getCell(colInd).setCellStyle(style);
			row.getCell(colInd).setCellComment(comment);
			colInd++;
			row.createCell(colInd).setCellValue("微分销零售价");
			row.getCell(colInd).setCellStyle(style);
			row.getCell(colInd).setCellComment(comment);
			colInd++;
			String catName="";
			Category cat=null;
			for (CommodityStyleOutputDto dto : commodityList) {
				colInd = 0;
				row = sheet.createRow(rowInd++);
				row.createCell(colInd++).setCellValue(ObjectUtils.toString(dto.getNo()));
				row.createCell(colInd++).setCellValue(ObjectUtils.toString(dto.getCommodityName()));
				row.createCell(colInd++).setCellValue(ObjectUtils.toString(dto.getSellingPoint()));
				row.createCell(colInd++).setCellValue(ObjectUtils.toString(dto.getPublicPrice()));
				row.createCell(colInd++).setCellValue(ObjectUtils.toString(dto.getSalePrice() ));
				row.createCell(colInd++).setCellValue(ObjectUtils.toString(dto.getWfxPrice()));
			}
			// 生成提示信息，  
		     response.setContentType("application/vnd.ms-excel");  
		     String codedFileName = java.net.URLEncoder.encode("中文", "UTF-8");  
	         response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");  
	         ServletOutputStream outputStream =  response.getOutputStream();  
			try{
				
				workbook.write(outputStream);
				outputStream.flush();
				
			}catch(Exception e){
			
			}finally{
				if(outputStream!=null){
					try {
						outputStream.close();
					}catch(Exception e){
						
					}
				}
			}
			
		}
	
	
	@RequestMapping("/saveCommodity")
	@ResponseBody
	public String saveCommodity(CommodityStyleInputDto commodityStyle) {
		try {
			String id = commodityStyle.getId();
			if (StringUtils.isNotBlank(id)) {
				commodityStyleBackgroundApi.update(commodityStyle);
			}else{
				commodityStyleBackgroundApi.insert(commodityStyle);
			}
		} catch (Exception e) {
			logger.error("保存数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	/**
	 * 跳转至导入区域数据
	 */
	@RequestMapping("/importExcelcommodityPrice")
	public String importExcelcommodityPrice(ModelMap map,HttpServletRequest request)throws Exception {
		map.addAttribute("username", UserSecurityUtil.getSystemUser().getUsername());
		map.addAttribute("loginIp", HttpUtil.getIpAddr(request));
		map.addAttribute("loginName", UserSecurityUtil.getSystemUser().getLogin_name());
		return "/manage/commodity/import_commodity_price";
	}
	/**
	 * 导入区域数据,导入excel
	 */
	@RequestMapping(value = "/importPrice", headers = "content-type=multipart/*", method = RequestMethod.POST)
	@ResponseBody
	public String importPrice(DefaultMultipartHttpServletRequest multipartRequest,HttpServletResponse response,ModelMap map,String username ,String loginIp ,String loginName)throws Exception {
		
		List<CommodityStyleInputDto>   arrayDto = new ArrayList<CommodityStyleInputDto>();   
		response.setContentType("text/html; charset=utf-8");
		String msg = "";
		try{
			    MultipartFile multipartFile = multipartRequest.getFile("excelBatchFile");
				List<Object[]>  listPrice = getNewDatasInSheet(0,0,6,multipartFile.getInputStream());
				//获取款色ID
				List<String>   nos=new ArrayList<String>();
				if(listPrice!=null && listPrice.size()>0){
					for (int i = 0; i < listPrice.size(); i++) {
						Object[] obj=listPrice.get(i);
						if(obj[0]!=null && !"".equals(obj[0].toString())){
							nos.add(obj[0].toString());
						}
					}
				}
				List<CommodityStyleOutputDto>  styleList=commodityStyleBackgroundApi.getCommodityByNos(nos);
				msg = checkExcelInfo(listPrice,styleList);
				String remark = "";
				if(listPrice!=null && listPrice.size()>0){
					for (int i = 0; i < listPrice.size(); i++) {
						Object[] obj=listPrice.get(i);
						CommodityStyleInputDto  dto = new CommodityStyleInputDto();
						//获取优购成本价格 
						Double costPrice=null;
						//获取微分销成本价格 
						Double oldWfxCostPrice=null;
						//获取微分销价格 
						Double oldWfxPrice=null;
						if(styleList!=null && styleList.size()>0){
							for (int j = 0; j < styleList.size(); j++) {
								CommodityStyleOutputDto   style=styleList.get(j);
								String no=style.getNo().trim();
								String no1=obj[0].toString().trim();
								if(no.equals(no1)){
									costPrice =style.getCostPrice();
									if(costPrice==null){
										costPrice=new Double(0);
									}
									oldWfxCostPrice=style.getWfxCostPrice();
									oldWfxCostPrice=oldWfxCostPrice!=null?oldWfxCostPrice:0;
									oldWfxPrice=style.getWfxPrice();
									oldWfxPrice=oldWfxPrice!=null?oldWfxPrice:0;
									remark = style.getNo()+"#"+style.getSupplierCode() +"#"+style.getStyleNo();
								}
							}
						}
						Double wfxPrice = Double.parseDouble(obj[5].toString());
						dto.setWfxCostPrice(Double.parseDouble(obj[4].toString()));
						dto.setWfxPrice( wfxPrice );
						dto.setNo(obj[0].toString());
						
						//计算可分配佣金金额
						boolean checkFlag = false;
						Double distributableMoney = null;
						try {
							distributableMoney = commodityStyleBackgroundApi.computeDistributableMoney( wfxPrice, costPrice);
						} catch (Exception e) {
							logger.error( e.getMessage() );
							checkFlag = true;
						}
						if( checkFlag || distributableMoney==null ){
							StringBuffer errorMsg = new StringBuffer("导入数据失败,无法正确计算第").append(i+1).append("行计算可分配金额出错，请首先确认第6列数据是否合理：")
									.append( costPrice).append(",").append(wfxPrice);
							logger.error( errorMsg.toString() );
							return super.jsonResult(StateCode.ERROR, errorMsg.toString());
						}else{
							dto.setDistributableMoney(distributableMoney);
						}
						arrayDto.add(dto);
						String operateContent="款色ID："+dto.getNo()+"原微分销价："+oldWfxPrice+"新微分销价："+dto.getWfxPrice()+"原微分销成本价格："+oldWfxCostPrice+"新微分销成本价格："+dto.getWfxCostPrice();
						
						this.addLogInfo(BusinessTypeEnum.COMMODITY_PRICE_UP.getKey(),OperateTypeEnum.EDIT.getKey(),operateContent,remark);
						//this.addLogInfo(BusinessTypeEnum.COMMODITY_PRICE_UP.getKey(),BusinessTypeEnum.COMMODITY_CREATE.getKey(),operateContent);
						
					}
				}
				//checkExcelInfo(listPrice);
				commodityStyleBackgroundApi.batchReadjustPrices(arrayDto);

		}catch (Exception ex) {
			logger.error(ex.getMessage());
			return super.jsonResult(StateCode.ERROR, "导入数据发生异常,ex=" + ex.getMessage());
		}
		
		return super.jsonResult(StateCode.SUCCESS, msg,"ok");
	}
	private String  checkExcelInfo(List<Object[]>  listPrice,List<CommodityStyleOutputDto>  styleList) throws Exception{
		
		String error="";
		
		//获取款色ID
		if(listPrice!=null && listPrice.size()>0){
			
			Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]{2})?$"); 
			
			for (int i = 0; i < listPrice.size(); i++) {
				int  flag=0;
				Object[] obj=listPrice.get(i);
			    
				Matcher wfxCostPrice = pattern.matcher(obj[4].toString());
				 if( !wfxCostPrice.matches() ){
				       flag=1; 
				       error=error+"款色ID"+(obj[0].toString())+"微分销成本价错误";
				 }
				 
				 Matcher wfxPrice = pattern.matcher(obj[5].toString());
				 if(!wfxPrice.matches() ){
				       flag=1; 
				       error=error+"款色ID"+(obj[0].toString())+"微分销成价格错误";
				 }
				 if(styleList!=null && styleList.size()>0){
					 for (int j = 0; j < styleList.size(); j++) {
					    CommodityStyleOutputDto  commodityStyle=styleList.get(j);
						if(obj[0].toString().equals(commodityStyle.getNo())){
							if(commodityStyle.getIsOnsale()==1){
								flag=1;
								 error=error+"款色ID"+(obj[0].toString())+"已经上架 不允许调价";
							}
						}
					 }
				 }
				 if(flag==1){
					
					listPrice.remove(i); 
					i--;
				 }
			}
		}
		
		return error.equals("")?"导入成功":error;
	}
	
	/**
	 * 获得表中的数据( flag=0 不增加id,flag=1自动增加id  )
	 * 
	 * @param sheetNumber
	 *            表格索引(EXCEL 是多表文档,所以需要输入表索引号)
	 *         flag=0 不增加id,flag=1自动增加id ，列总数 columnCount
	 * @return 由LIST构成的行和表
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<Object[]> getNewDatasInSheet(int sheetNumber,int flag,int columnCount,InputStream s) throws FileNotFoundException, IOException {
		List<Object[]> result = new ArrayList<Object[]>();

		HSSFWorkbook workbook;
		
		workbook = new HSSFWorkbook(s);
		
		// 获得指定的表
		HSSFSheet sheet = workbook.getSheetAt(sheetNumber);

		// 获得数据总行数
		int rowCount = sheet.getLastRowNum();
		//int rowCount=this.getRightRows(sheetAs);	
		int sjrsCols=0;
		logger.info("found excel rows count: " + rowCount);
		if (rowCount < 1) {
			return null;
		}

		// 逐行读取数据
		for (int rowIndex = 1; rowIndex <= rowCount; rowIndex++) {

			// 获得行对象
			HSSFRow row = sheet.getRow(rowIndex);

			if (row != null) {
				
				
				Object[] rowData = new Object[columnCount];
				
				// 获得本行中各单元格中的数据
				if (flag == 0) {
					
					for (short columnIndex = 0; columnIndex < columnCount; columnIndex++) {

						HSSFCell cell = row.getCell(columnIndex);

						// 获得指定单元格中数据
						Object cellStr = this.getCellString(cell);
						if(cellStr!=null){
							if(cellStr.toString().trim().length()>0){
								 rowData[columnIndex] = cellStr.toString().trim();
								 sjrsCols++;
							}
						}
						//rowData[columnIndex] = (cellStr);
					}
				} else {
					for (short columnIndex = 0; columnIndex < columnCount; columnIndex++) {
						
						if(columnIndex==0){
						   rowData[0] ="" ;//UUIDGenerator.getUUID();
						}else{
							// 获得指定单元格中数据
							HSSFCell cell = row.getCell(columnIndex-1);
							Object cellStr = this.getCellString(cell);
							if(cellStr!=null){
								if(cellStr.toString().trim().length()>0){
									 rowData[columnIndex] = cellStr.toString().trim();
									 sjrsCols++;
								}
							}//为空写入""
							else{
								 rowData[columnIndex] = "";
								 sjrsCols++;
							}
						}
						
					}
				}
				if(sjrsCols!=0){
				   result.add(rowData);
				}
				sjrsCols=0;
				
			}
		}
		return result;
	}

	/**
	 * 获得单元格中的内容
	 * 
	 * @param cell
	 * @return
	 */
	protected Object getCellString(HSSFCell cell) {
		DecimalFormat df = new DecimalFormat("#.00");
		Object result = null;
		if (cell != null) {

			int cellType = cell.getCellType();

			switch (cellType) {

			case HSSFCell.CELL_TYPE_STRING:
				result = cell.getRichStringCellValue().getString();
				
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				result = df.format(cell.getNumericCellValue());
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				result = cell.getCellFormula();
				break;
			case HSSFCell.CELL_TYPE_ERROR:
				result = null;
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				result = cell.getBooleanCellValue();
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				result = null;
				break;
			}
		}
		return result;
	}
	@RequestMapping("/synchronizationCommodity")
	@ResponseBody
	public String synchronizationCommodity(ModelMap map,HttpServletRequest request) {
		try {
			String commodityIds=request.getParameter("commodityIds");
			if(StringUtils.isNotBlank(commodityIds)){
				String [] commodityArray=commodityIds.split(",");
				List<String> commodityList = new ArrayList<String>();
				Collections.addAll(commodityList, commodityArray);
				List<CommodityStyleOutputDto>  dtoList=commodityStyleBackgroundApi.queryExportCommodityInfo(commodityList);
				List<String> synchronizationCommodityList = new ArrayList<String>();
				
				if(dtoList!=null && dtoList.size()>0 ){
					for (int i = 0; i < dtoList.size(); i++) {
						synchronizationCommodityList.add(dtoList.get(i).getNo());
					}
				}
				
				commodityStyleBackgroundApi.synchronizationCommodity("WFX", "WFX-001", synchronizationCommodityList);

			}
		} catch (Exception e) {
			logger.error("同步数据异常", e);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}

	/**
	 * 进入商品选择页面
	 */
	@RequestMapping("/commoditySelected")
	public String articleSelected(ModelMap map) {
		return "/manage/commodity/commoditySelected";
	}

	/**
	 * 商品筛选数据页面
	 */
	@RequestMapping("/queryCommoditySelectedData")
	@ResponseBody
	public String queryCommoditySelectedData(ModelMap map, CommodityStylePageVo commodityStyleVo, PageModel<CommodityStyleOutputDto> pageModel) throws Exception {
		int warehouseFlag = 1;
		CommodityStylePageInputDto pageInputDto = (CommodityStylePageInputDto) BeanUtil.convertBean(commodityStyleVo, CommodityStylePageInputDto.class);
		PageModel<CommodityStyleOutputDto> result = commodityStyleBackgroundApi.findPage(pageInputDto, pageModel, warehouseFlag);
		return JSONObject.toJSONString(result);
	}

}
