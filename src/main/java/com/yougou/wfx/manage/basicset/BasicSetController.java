package com.yougou.wfx.manage.basicset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.commodity.api.background.ISysLogBackgroundApi;
import com.yougou.wfx.commodity.dto.input.SysLogPageInputDto;
import com.yougou.wfx.commodity.dto.output.SysLogOutputDto;
import com.yougou.wfx.dto.base.PageModel;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.framework.bean.BeanUtil;
import com.yougou.wfx.manage.commodity.vo.SysLogPageVo;
import com.yougou.wfx.util.Constant;

/**
 * 基础设置模块
 * 
 * @author luoq
 */
@Controller
@RequestMapping("/basicSet")
public class BasicSetController extends BaseController {

	@Autowired
	private	ISysLogBackgroundApi sysLogBackgroundApi;

	/**
	 * 操作日志菜单
	 */
	@RequestMapping("/sysOperationLog")
	public String operationLog() {
		return "/manage/basicset/operationLogList";
	}
	
	/**
	 * 查询基础设置模块操作日志列表
	 * 分页查询数据，并返回json格式的结果
	 */
	@RequestMapping("/queryOperationLogData")
	@ResponseBody
	public String queryOperationLogData(ModelMap map, SysLogPageVo sysLogVo, PageModel<SysLogOutputDto> pageModel)throws Exception{
		sysLogVo.setModule(Constant.MODULE_BASIC_SET);
		SysLogPageInputDto pageInputDto = (SysLogPageInputDto) BeanUtil.convertBean(sysLogVo, SysLogPageInputDto.class);
		PageModel<SysLogOutputDto> result = sysLogBackgroundApi.findPage(pageInputDto, pageModel);
		return JSONObject.toJSONString(result);
	}
}
