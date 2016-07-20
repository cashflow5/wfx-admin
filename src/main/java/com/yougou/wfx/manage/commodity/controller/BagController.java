package com.yougou.wfx.manage.commodity.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.yougou.permission.remote.UserRemote;
import com.yougou.tools.common.utils.StringUtil;
import com.yougou.wfx.commodity.api.background.IBagBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityStyleBackgroundApi;
import com.yougou.wfx.commodity.api.background.ISysLogBackgroundApi;
import com.yougou.wfx.commodity.dto.input.BagInputDto;
import com.yougou.wfx.commodity.dto.input.BagPageInputDto;
import com.yougou.wfx.commodity.dto.input.CommodityBagRelaInputDto;
import com.yougou.wfx.commodity.dto.input.CommodityStyleInputDto;
import com.yougou.wfx.commodity.dto.input.SysLogPageInputDto;
import com.yougou.wfx.commodity.dto.output.BagOutputDto;
import com.yougou.wfx.commodity.dto.output.CommodityStyleOutputDto;
import com.yougou.wfx.commodity.dto.output.SysLogOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.dto.base.WFXResult;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.commodity.vo.BagPageVo;
import com.yougou.wfx.manage.commodity.vo.SysLogPageVo;
import com.yougou.wfx.system.api.IFileUploadApi;
import com.yougou.wfx.system.api.IWFXSystemApi;
import com.yougou.wfx.util.Constant;
import com.yougou.wfx.util.ExcelUtil;
import com.yougou.wfx.util.UserSecurityUtil;

/**
 * 分销包
 * 
 * @author wuyang
 */
@Controller
@RequestMapping("/commodity")
public class BagController extends BaseController {

	@Autowired
	private IBagBackgroundApi bagBackgroundApi;

	@Autowired
	private	ISysLogBackgroundApi sysLogBackgroundApi;
	
	@Autowired
	private ICommodityStyleBackgroundApi commodityStyleBackgroundApi;
	
	@Autowired
	private IFileUploadApi fileUploadApi;
	
	@Autowired
	private IWFXSystemApi wfxSystemApi;
	/**
	 * 分销包菜单
	 */
	@RequestMapping("/packageList")
	public String packageList() {
		return "/manage/commodity/packageList";
	}
	
	/**
	 * 获取分销包详情
	 */
	@RequestMapping("/packageInfo")
	public String getPackageInfo(String id, String type, ModelMap map) {
		String imgBaseUrl = wfxSystemApi.obtainImgBaseUrl();
		BagOutputDto packageInfo = null;
		List<CommodityStyleOutputDto> commodityList = null;
		if(!StringUtil.isStrEmpty(id)){
			packageInfo = bagBackgroundApi.getById(id);
			commodityList = bagBackgroundApi.getRelaCommodityList(id);
		}
		map.put("packageInfo", packageInfo);
		map.put("commodityList", commodityList);
		map.put("imgBaseUrl", imgBaseUrl);
		if(type.equals("edit")||type.equals("add")){
			return "/manage/commodity/packageEdit";
		}else{
			return "/manage/commodity/packageView";
		}
	}

	/**
	 * 查询分销包列表
	 */
	@RequestMapping("/queryPackageListData")
	@ResponseBody
	public String queryPackageListData(ModelMap map, BagPageVo bagVo, PageModel<BagOutputDto> pageModel) {
		BagPageInputDto pageInputDto = (BagPageInputDto) BeanUtil.convertBean(bagVo, BagPageInputDto.class);
		PageModel<BagOutputDto> result = bagBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
	
	/**
	 * 新增/修改分销包数据
	 * @return
	 */
	@RequestMapping("/savePackageData")
	@ResponseBody
	public String savePackageData(BagPageVo bagVo) {
		try {
			String operator = UserSecurityUtil.getSystemUserName();
			Date current = new Date();
			String id = bagVo.getId();
			String[] commodityIds = new String[0];
			if(StringUtils.isNotBlank(bagVo.getCommodityIds())){
				commodityIds = bagVo.getCommodityIds().split(",");
			}
			if(StringUtils.isNotBlank(id)) {//更新
				BagOutputDto outputDto = bagBackgroundApi.getById(id);
				if(null != outputDto) {
					BagInputDto inputDto = new BagInputDto();
					BeanUtils.copyProperties(bagVo, inputDto);
					inputDto.setStatus(new Integer(1));
					inputDto.setCommodityNum(commodityIds.length);
					inputDto.setCommodityIds(commodityIds);
					inputDto.setUpdateUser(operator);
					inputDto.setUpdateTime(current);
					bagBackgroundApi.update(inputDto, true);
				} else {
					return super.jsonResult(StateCode.INVALID, WARN);
				}
			} else {//新增
				BagInputDto inputDto = new BagInputDto();
				BeanUtils.copyProperties(bagVo, inputDto);
				inputDto.setStatus(new Integer(1));
				inputDto.setCommodityNum(commodityIds.length);
				inputDto.setCommodityIds(commodityIds);
				inputDto.setCreateTime(new Date());
				inputDto.setUpdateTime(new Date());
				inputDto.setUpdateUser(operator);
				bagBackgroundApi.insert(inputDto);
				
			}
		} catch (Exception ex) {
			logger.error("修改分销包数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	
	/**
	 * 编辑分销包的基本信息
	 * @return
	 */
	@RequestMapping("/updatePackageData")
	@ResponseBody
	public String updatePackageData(BagPageVo bagVo) {
		try {
			String operator = UserSecurityUtil.getSystemUserName();
			Date current = new Date();
			BagInputDto inputDto = new BagInputDto();
			BeanUtils.copyProperties(bagVo, inputDto);
			inputDto.setUpdateUser(operator);
			inputDto.setUpdateTime(current);
			bagBackgroundApi.update(inputDto, false);
			
		} catch (Exception ex) {
			logger.error("修改分销包数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 删除分销包数据
	 * @return
	 */
	@RequestMapping("/deletePackageData")
	@ResponseBody
	public String deletePackageData(String id) {
		try {
			bagBackgroundApi.removeById(id);
		} catch (Exception ex) {
			logger.error("修改分销包数据异常", ex);
			return super.jsonResult(StateCode.ERROR, "发生异常,ex=" + ex.getMessage());
		}
		return super.jsonResult(StateCode.SUCCESS, SUCCESS);
	}
	
	/**
	 * 导出模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "reportTemplete")
	private void reportTemplete(HttpServletRequest request, HttpServletResponse response) {
		try {
			String fileName = "bagCommodityTemp.xls";
			String newFileName = "分销包商品导入模板.xls";
			String realFile = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/")
					+ "/WEB-INF/ftl/manage/commodity/download/" + fileName;
			File excelName = new File(realFile);
			response.setHeader("Content-Disposition", "attachment; filename=" + new String(newFileName.getBytes("GBK"), "ISO8859-1"));
			response.setContentLength((int) excelName.length());
			response.setContentType("application/x-msdownload");
			byte[] buffer = new byte[4096];
			int i = 0;
			FileInputStream fis = new FileInputStream(excelName);
			while ((i = fis.read(buffer, 0, 4096)) > 0) {
				response.getOutputStream().write(buffer, 0, i);
			}
			response.flushBuffer();

		} catch (Exception e) {
			logger.error("导出锁库模板错误：", e);
		}
	}
	
	/**
	 * 导入分销包数据
	 */
	@RequestMapping(value = "/importPackageData", method = RequestMethod.POST)
	@ResponseBody
	public String importPackageData(@RequestParam MultipartFile file, HttpServletResponse response)throws Exception {
		List<CommodityStyleOutputDto> commodityList =new ArrayList<CommodityStyleOutputDto>();   
		try{
			InputStream in = file.getInputStream();
			try {
				List<Object[]> list = ExcelUtil.readExcel(in, 0);				
				if(list!=null && list.size()>1){
					list.remove(0);
					for(Object[] o : list){
						String styleNo = (String) o[0];
						CommodityStyleInputDto inputDto = new CommodityStyleInputDto();
						inputDto.setStyleNo(styleNo);
						List<CommodityStyleOutputDto> csList = commodityStyleBackgroundApi.queryList(inputDto);
						if(csList!=null&&csList.size()>0){
							commodityList.add(csList.get(0));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				in.close();
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		return jsonResult(StateCode.SUCCESS, SUCCESS, commodityList);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "uploadImg", method = RequestMethod.POST)
	public String uploadImg(@RequestParam MultipartFile file, HttpServletRequest request, String name, @RequestParam(defaultValue="1") Integer imgType, String bagId) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			WFXResult<String> result = fileUploadApi.uploadBagImg(bagId, name, imgType.intValue(), file.getInputStream());
			String imgBaseUrl = wfxSystemApi.obtainImgBaseUrl();
			System.out.println("=====:" + imgBaseUrl);
			resultMap.put("imgPath", result.getResult());
			resultMap.put("imgBasePath", imgBaseUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(jsonResult(StateCode.SUCCESS, SUCCESS, resultMap));
		return jsonResult(StateCode.SUCCESS, SUCCESS, resultMap);
	}
	
	/**
	 * 操作日志菜单
	 */
	@RequestMapping("/operationLog")
	public String operationLog() {
		return "/manage/commodity/operationLogList";
	}
	
	/**
	 * 查询商品模块操作日志列表
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryOperationLogData")
	@ResponseBody
	public String queryOperationLogData(ModelMap map, SysLogPageVo sysLogVo, PageModel<SysLogOutputDto> pageModel)throws Exception{
		sysLogVo.setModule(Constant.MODULE_COMMODITY);
		SysLogPageInputDto pageInputDto = (SysLogPageInputDto) BeanUtil.convertBean(sysLogVo, SysLogPageInputDto.class);
		PageModel<SysLogOutputDto> result = sysLogBackgroundApi.findPage(pageInputDto, pageModel);
		List<SysLogOutputDto> lstSysLog = result.getItems();
		if(null != lstSysLog){
			for(SysLogOutputDto item : lstSysLog){
				if(StringUtils.isNotBlank(item.getRemark())){
					String[] remark = item.getRemark().split("#");
					item.setCommodityNo(remark[0]);
					if(remark.length > 1){
						item.setSupplierCode(remark[1]);
					}
					if(remark.length > 2){
						item.setStyleNo(remark[2]);
					}
				}
			}
		}
		return JSONObject.toJSONString(result);
	}
}
