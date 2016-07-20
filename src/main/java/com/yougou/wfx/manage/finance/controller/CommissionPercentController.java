 /*
 * 版本信息
 
 * 日期 2016-03-25 11:09:26
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.finance.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.commodity.api.background.ICommodityBrandBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityCatb2cBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityStyleBackgroundApi;
import com.yougou.wfx.commodity.dto.input.CommodityBrandPageInputDto;
import com.yougou.wfx.commodity.dto.input.CommodityStylePageInputDto;
import com.yougou.wfx.commodity.dto.output.CommodityBrandOutputDto;
import com.yougou.wfx.commodity.dto.output.CommodityCatb2cOutputDto;
import com.yougou.wfx.commodity.dto.output.CommodityStyleOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.commodity.vo.CommodityBrandPageVo;
import com.yougou.wfx.manage.finance.vo.CommissionPercentPageVo;
import com.yougou.wfx.finance.api.background.ICommissionPercentBackgroundApi;
import com.yougou.wfx.finance.dto.input.CommissionPercentInputDto;
import com.yougou.wfx.finance.dto.input.CommissionPercentLogPageInputDto;
import com.yougou.wfx.finance.dto.input.CommissionPercentPageInputDto;
import com.yougou.wfx.finance.dto.output.CommissionPercentLogOutputDto;
import com.yougou.wfx.finance.dto.output.CommissionPercentOutputDto;
import com.yougou.wfx.finance.util.FinanaceConstant;

/**
 * CommissionPercentController
 * @author langqiwei
 * @Date 创建时间：2016-03-25 11:09:26
 */
@Controller
@RequestMapping("/finance")
public class CommissionPercentController extends BaseController{
	
	@Resource
	private ICommissionPercentBackgroundApi commissionPercentBackgroundApi;
	@Resource
	private ICommodityCatb2cBackgroundApi commodityCatb2cBackgroundApiImpl;
	@Resource
    private ICommodityStyleBackgroundApi commodityStyleBackgroundApi;
	@Resource
	private ICommodityBrandBackgroundApi commodityBrandBackgroundApi;
	
	/**
	 * 进入菜单
	 */
	@RequestMapping("/commissionSetList")
	public String commissionSetList(ModelMap map,Integer type){
//	    CommissionPercentOutputDto dto=commissionPercentBackgroundApi.getCommissionByCondition("4e8J", "2c94813034879f8001348949992417c6","2800992ba9244d2195771043f4adbea3");
	    CommissionPercentOutputDto commissionPercentVo = new CommissionPercentOutputDto();
        try {
            commissionPercentVo = commissionPercentBackgroundApi.getById(FinanaceConstant.DEFAULT_COMMISSION_ID);//默认比例佣金id
        } catch (Exception e) {
            logger.error("查询数据异常", e);
        }
	    map.addAttribute("commissionPercentVo", commissionPercentVo);
	    
	    if(null==type||type==0){
            type=1;//设置默认值为默认比例佣金分类
        }
	    map.addAttribute("type",type);
	    if(type==4){//日志操作
	        return "/manage/finance/commissionSetLogList";
        }else{//默认佣金比例//品牌分类佣金比例//单品佣金比例设置
            return "/manage/finance/commissionSetList";
	    }
	}
	
	/**
     * 分页查询数据，并返回json格式的结果
     */
    @RequestMapping("/queryCommissionSetData")
    @ResponseBody
    public String queryCommissionSetData(ModelMap map, CommissionPercentPageVo commissionPercentVo, PageModel<CommissionPercentOutputDto> pageModel)throws Exception{
        CommissionPercentPageInputDto pageInputDto = (CommissionPercentPageInputDto) BeanUtil.convertBean(commissionPercentVo, CommissionPercentPageInputDto.class);
        String supplierCode=pageInputDto.getSupplierCode();
        if(StringUtils.isNotBlank(supplierCode)){
            pageInputDto.setSupplierCode("'"+supplierCode+"'");
        }
        PageModel<CommissionPercentOutputDto> result = commissionPercentBackgroundApi.findPage(pageInputDto, pageModel);
        return JSONObject.toJSONString(result);
    }
	
	/**
     * 分类佣金比例-商品分类获取,根据一级分类ID查询相应二级分类
     * @return
	 * @throws IOException 
     */
    @RequestMapping("/getBaseCatById")
    public void getBaseCatById(HttpServletResponse response,String baseCatId,Integer level,String brandNo) throws IOException {
        List<CommodityCatb2cOutputDto> list=commodityCatb2cBackgroundApiImpl.getCatList(brandNo, baseCatId, level);
        JSONObject obj = new JSONObject();
        obj.put("list", list);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(obj.toString());
    }
    
    /**
     * 品牌分类佣金比例-编辑/添加-页面跳转
     * 
     * @return
     */
    @RequestMapping("/commissionSetBrand")
    public String commissionSetBrand(String id,ModelMap map) {
        //根据相应id查询佣金比例数据
        CommissionPercentOutputDto commissionPercentVo=new CommissionPercentOutputDto();
        if(StringUtils.isNotBlank(id)){
            commissionPercentVo=commissionPercentBackgroundApi.getById(id);
            CommodityCatb2cOutputDto sencondCatDto=commodityCatb2cBackgroundApiImpl.getById(commissionPercentVo.getBaseCatId());
            CommodityCatb2cOutputDto firstCatDto=null;
            if(null!=sencondCatDto&&sencondCatDto.getLevel()==2){//当佣金比例表分类id保存的是二级分类id
                firstCatDto=commodityCatb2cBackgroundApiImpl.getById(sencondCatDto.getParentId());
            }else{
                firstCatDto=sencondCatDto;//当佣金比例表分类id保存的是一级分类id
                sencondCatDto=null;
            }
            map.addAttribute("firstCatDto", firstCatDto);
            map.addAttribute("sencondCatDto", sencondCatDto);
        }
        map.addAttribute("commissionPercentVo", commissionPercentVo);
        return "/manage/finance/commissionSetBrand";
    }   

    
	@RequestMapping("/removeCommissionSet")
	@ResponseBody
	public String removeCommissionSet(@RequestParam("id") String id) {
		try {
            if (StringUtils.isNotBlank(id)) {
                commissionPercentBackgroundApi.removeById(id,getSysUserName());
            }
		} catch (Exception ex) {
			logger.error("删除数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 品牌分类佣金和单品佣金比例设置，保存和编辑
	 * @param commissionPercent
	 * @return
	 */
	@RequestMapping("/saveCommission")
	@ResponseBody
	public String saveCommission(CommissionPercentInputDto commissionPercent) {
	    String messageString="";
		try {
		    if (commissionPercent!=null) {
		        if (StringUtils.isNotBlank(commissionPercent.getId())) {//佣金设置编辑保存
		            commissionPercent.setUpdateUser(getSysUserName());
		            commissionPercentBackgroundApi.update(commissionPercent);
		        }else{//单品佣金设置和品牌分类佣金设置保存
		            commissionPercent.setCreateUser(getSysUserName());
	                messageString=commissionPercentBackgroundApi.saveCommission(commissionPercent);
		        }
            }
		} catch (Exception e) {
			logger.error("保存数据异常", e);
			return super.jsonResult(StateCode.ERROR, null);
		}
		return super.jsonResult(StateCode.SUCCESS, messageString);
	}
	
	/**
     * 默认佣金比例设置，保存
     * @param commissionPercent
     * @return
     */
	@RequestMapping("/saveDefaultCommission")
    @ResponseBody
    public String saveDefaultCommission(CommissionPercentInputDto commissionPercent) {
        try {
            String id = commissionPercent.getId();
            if (StringUtils.isNotBlank(id)) {
                commissionPercent.setType(1);//默认佣金比例设置
                commissionPercent.setUpdateUser(getSysUserName());
                commissionPercentBackgroundApi.update(commissionPercent);
            }
        } catch (Exception e) {
            logger.error("保存数据异常", e);
            return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + e.getMessage());
        }
        return super.jsonResult(StateCode.SUCCESS, SUCCESS);
    }
	
    /**
     * 日志 分页查询数据，并返回json格式的结果
     */
    @RequestMapping("/queryCommissionSetLogData")
    @ResponseBody
    public String queryCommissionPercentData(ModelMap map, CommissionPercentLogPageInputDto pageInputDto, PageModel<CommissionPercentLogOutputDto> pageModel)throws Exception{
        PageModel<CommissionPercentLogOutputDto> result = commissionPercentBackgroundApi.findPageLog(pageInputDto, pageModel);
        return JSONObject.toJSONString(result);
    }
    
    /**
     * 品牌列表：进入菜单
     */
    @RequestMapping("/queryBrandList")
    public String queryBrandList(ModelMap map,String status){
       return "/manage/finance/queryBrandList";
    }
    /**
     * 品牌列表：数据查询
     */
    @RequestMapping("/queryBrandData")
    @ResponseBody
    public String queryBrandData(ModelMap map,CommodityBrandPageVo commodityBrandVo,PageModel<CommodityBrandOutputDto> pageModel)throws Exception{
        CommodityBrandPageInputDto pageInputDto = (CommodityBrandPageInputDto) BeanUtil.convertBean(commodityBrandVo, CommodityBrandPageInputDto.class);
        PageModel<CommodityBrandOutputDto>   result = commodityBrandBackgroundApi.findPage(pageInputDto, pageModel);
        return JSONObject.toJSONString(result);
    }

    /**
     * 商品佣金比例-新增、编辑-跳转页面
     */
    @RequestMapping("/commissionSetCommodiy")
    public String commissionSetCommodiy(ModelMap map,String id){
      //根据相应id查询佣金比例数据
        CommissionPercentOutputDto commissionPercentVo=new CommissionPercentOutputDto();
        if(StringUtils.isNotBlank(id)){
            commissionPercentVo=commissionPercentBackgroundApi.getById(id);
            List<CommodityStyleOutputDto> commodityList= commodityStyleBackgroundApi.queryCommodityList(commissionPercentVo.getSupplierCode(),commissionPercentVo.getCommodityId());
            if(null!=commodityList&&commodityList.size()>0){
                commissionPercentVo.setPicBig(commodityList.get(0).getPicBig());
            }
        }
        map.addAttribute("commissionPercentVo", commissionPercentVo);
        return "/manage/finance/commissionSetCommodiy";
    }
    
    /**
     * 根据款色编码获取商品信息
     */
    @RequestMapping("/queryCommodityList")
    @ResponseBody
    public String queryCommodityList(ModelMap map,CommodityStylePageInputDto pageInputDto)throws Exception{
        List<CommodityStyleOutputDto> commodityList=new ArrayList<CommodityStyleOutputDto>();
        String supplierCode=pageInputDto.getSupplierCode();
        if(StringUtils.isNotBlank(supplierCode)){
            commodityList= commodityStyleBackgroundApi.queryCommodityList(supplierCode,null);
        }
        return JSONObject.toJSONString(commodityList);
    }
}
