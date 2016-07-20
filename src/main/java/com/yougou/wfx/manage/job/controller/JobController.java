package com.yougou.wfx.manage.job.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.wfx.aftersale.api.background.IOrderRefundBackgroundApi;
import com.yougou.wfx.cms.api.background.ICommoditySaleCatBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityBrandBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityCatb2cBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityCortexBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityProductBackgroundApi;
import com.yougou.wfx.commodity.api.background.ICommodityStyleBackgroundApi;
import com.yougou.wfx.finance.api.background.ICommissionDetailBackgroundApi;
import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.member.api.background.IMemberProfileBackgroundApi;
import com.yougou.wfx.order.api.background.IOrderBackgroundApi;
import com.yougou.wfx.seller.api.background.ISellerInfoBackgroundApi;
import com.yougou.wfx.system.api.IFileUploadApi;

/**
 * 调度
 * 
 * @author li.j1
 */
@Controller
@RequestMapping("/job")
public class JobController extends BaseController {

	Logger logger = LoggerFactory.getLogger(JobController.class);
	@Resource
	IOrderRefundBackgroundApi orderRefundBackgroundApi;
	@Resource
	private IOrderBackgroundApi orderBackgroundApi;
	@Resource
	private ICommoditySaleCatBackgroundApi commoditySaleCatBackgroundApi;
	@Resource
	private ICommodityCatb2cBackgroundApi commodityCatb2cBackgroundApi;
	@Resource
	private IMemberProfileBackgroundApi memberProfileBackgroundApi;
	@Resource
	private ICommodityBrandBackgroundApi commodityBrandBackgroundApi;
	@Resource
	private ICommodityProductBackgroundApi commodityProductBackgroundApi;
	@Resource
	private ICommodityStyleBackgroundApi commodityStyleBackgroundApi;
	@Resource
	private ISellerInfoBackgroundApi sellerInfoBackgroundApi;
	@Resource
	private IFileUploadApi fileUploadApi;
	@Resource
    private ICommissionDetailBackgroundApi commissionDetailBackgroundApi;
	@Resource
	private ICommodityCortexBackgroundApi commodityCortexBackgroundApi;
	/**
	 * 定时作废未付款订单
	 * 
	 * @return
	 */
	@RequestMapping("closeOrders")
	@ResponseBody
	public String closeOrders() {
		try {
			logger.info("closeOrders start======================================");
			return orderBackgroundApi.closeOrders();
		} catch (Exception e) {
			logger.error("The job of closeOrders happens Exception ", e);
			return "error";
		}
	}

	/**
	 * 超时自动确认订单收货
	 * 
	 * @return
	 */
	@RequestMapping("autoChangeOrderStatusToDeliverd")
	@ResponseBody
	public String autoChangeOrderStatusToDeliverd() {
		try {
			logger.info("autoChangeOrderStatusToDeliverd start======================================");
			return orderBackgroundApi.autoChangeOrderStatusToDeliverd();
		} catch (Exception e) {
			logger.error("The job of autoChangeOrderStatusToDeliverd happens Exception ", e);
			return "error";
		}
	}

	/**
	 * 拒绝退款之后N天超时处理(N=7)
	 * 
	 * @return
	 */
	@RequestMapping("timeOutCloseRefund")
	@ResponseBody
	public String timeOutCloseRefund() {
		try {
			logger.info("timeOutCloseRefund start======================================");
			return orderRefundBackgroundApi.timeOutCloseRefund();
		} catch (Exception e) {
			logger.error("The job of timeOutCloseRefund happens Exception ", e);
			return "error";
		}
	}

	/**
	 * 确认收货N天之后自动生成佣金记录
	 * 
	 * @return
	 */
	@RequestMapping("generateCommission")
	@ResponseBody
	public String generateCommission() {
		try {
			orderBackgroundApi.generateCommission();
			return "success";
		} catch (Exception e) {
			return "error";
		}
	}

	/**
	 * 统计销售分类的sku数量
	 */
	@RequestMapping("saleCatSkuNumJob")
	@ResponseBody
	public String saleCatSkuNumJob() {
		String msg = "success";
		logger.info("统计销售分类的sku数量开始!");
		try {
			commoditySaleCatBackgroundApi.saleCatSkuNumJob();
		} catch (Exception e) {
			logger.error("统计销售分类的sku数量发生异常：" + e.getMessage());
			msg = "error";
		}
		logger.info("统计销售分类的sku数量结束!");
		return msg;
	}

	/**
	 * 会员信息表统计数据：tbl_wfx_member_profile统计订单数量，金额
	 */
	@RequestMapping("countOrderNumAndAmountForMember")
	@ResponseBody
	public String countOrderNumAndAmountForMember() {
		try {
			logger.info("countOrderNumAndAmountForMember start======================================");
			int i = memberProfileBackgroundApi.batchUpdateOrderAmountAndCount();
			logger.info("更新数据" + i + "条");
			logger.info("countOrderNumAndAmountForMember  end========================================");
		} catch (Exception e) {
			logger.error("  Error happens (countOrderNumAndAmountForMember): ", e);
			return "error";
		}
		return "success";
	}

	/**
	 * 基础分类统计数据：tbl_commodity_catb2c统计SKU数量
	 */
	@RequestMapping("countSkuNumGroupByCatNo")
	@ResponseBody
	public String countSkuNumGroupByCatNo() {
		try {
			logger.info("countSkuNumGroupByCatNo start======================================");
			int i = commodityCatb2cBackgroundApi.batchUpdateSkuNum();
			logger.info("更新数据" + i + "条");
			logger.info("countSkuNumGroupByCatNo end========================================");
		} catch (Exception e) {
			logger.error("  Error happens (countSkuNumGroupByCatNo): ", e);
			return "error";
		}
		return "success";
	}

	/**
	 * 更新品牌使用状态，是否自营商品在使用
	 */
	@RequestMapping("updateBrandUseFlag")
	@ResponseBody
	public String updateBrandUseFlag() {
		try {
			logger.info("updateBrandUseFlag start======================================");
			commodityBrandBackgroundApi.batchUpdateBrandUseFlag();
			logger.info("updateBrandUseFlag end======================================");
		} catch (Exception e) {
			return "error";
		}
		return "success";
	}

	/**
	 * 商品数据清理：清理tbl_commodity_product表的非自营数据
	 * 
	 * @return
	 */
	@RequestMapping("clearCommodityProductNotSPYG")
	@ResponseBody
	private String clearCommodityProductNotSPYG() {

		try {
			logger.info("clearCommodityProductNotSPYG start======================================");
			int i = commodityProductBackgroundApi.clearNotSPYG();
			logger.info("删除数据" + i + "条");
			logger.info("clearCommodityProductNotSPYG end========================================");
		} catch (Exception e) {
			logger.error("  Error happens (clearCommodityProductNotSPYG): ", e);
			return "error";
		}
		return "success";
	}

	/**
	 * 商品表统计数据：tbl_commodity_style表统计销量，人气，代理数
	 * 
	 * @return
	 */
	@RequestMapping("countCommodityStyleParams")
	@ResponseBody
	private String countCommodityStyleParams() {

		try {
			logger.info("countCommodityStyleParams start======================================");
			int i = commodityStyleBackgroundApi.countCommodityStyleParams();
			logger.info("修改数据据" + i + "条");
			logger.info("countCommodityStyleParams end========================================");
		} catch (Exception e) {
			logger.error("  Error happens (countCommodityStyleParams): ", e);
			return "error";
		}
		return "success";
	}
	
	/**
	 * 分销商自动审核，定时扫描满足条件的成为分销商申请，审核通过
	 * 
	 * @return
	 */
	@RequestMapping("sellerAutoAuditAllApply")
	@ResponseBody
	private String sellerAutoAuditAllApply() {

		try {
			logger.info("sellerAutoAudit 分销商自动审核 job start======================================");
			int i = sellerInfoBackgroundApi.sellerAutoAuditAllApply();
			logger.info("申请成为分销商，自动审核通过" + i + "笔申请");
			logger.info("sellerAutoAudit 分销商自动审核 job end========================================");
		} catch (Exception e) {
			logger.error("sellerAutoAudit 分销商自动审核 job 异常: ", e);
			return "error";
		}
		return "success";
	}

	/**
	 * 清理无效ftp文件调度
	 */
	@RequestMapping("clearupInvalidFtpFilesJob")
	@ResponseBody
	public String clearupInvalidFtpFilesJob() {
		String msg = "success";
		logger.info("清理无效ftp文件调度开始!");
		try {
			fileUploadApi.clearupInvalidFtpFiles();
		} catch (Exception e) {
			logger.error("清理无效ftp文件调度发生异常：" + e.getMessage());
			msg = "error";
		}
		logger.info("清理无效ftp文件调度结束!");
		return msg;
	}
	
	/**
     * 佣金明细结算-调度，计入账户余额 ，两个小时(0 0 0/2 * * ?)调度生成一次
     * 调度时间配置必须在1个小时以上，避免大数据处理时堵塞
     * 未结算数据量比较少时，可以缩短调度时间
     */
    @RequestMapping("/dispatchCreateAccountBalance")
    @ResponseBody
    public String dispatchCreateAccountBalance(){
        long start=System.currentTimeMillis();
        logger.info("dispatchCreateAccountBalance启动,标志:"+start);
        String msg = "success";
        try {
            commissionDetailBackgroundApi.dispatchCreateAccountBalance();
        } catch (Exception e) {
            logger.error("佣金明细结算处理-调度", e);
            msg = "error";
        }
        logger.info("dispatchCreateAccountBalance结束,标志:"+start+",耗时:"+(System.currentTimeMillis()-start));
        return msg;
    }
    
    /**
	 * 清理非自营商品图片
	 */
	@RequestMapping("clearCommodityStylePicNotSPYG")
	@ResponseBody
	public String clearCommodityStylePicNotSPYG() {
		try {
			logger.info("clearCommodityStylePicNotSPYG 清理非自营商品图片 job start======================================");
			int i = commodityStyleBackgroundApi.clearCommodityStylePicNotSPYG();
			logger.info("删除数据" + i + "条");
			logger.info("clearCommodityStylePicNotSPYG 清理非自营商品图片 job end========================================");
		} catch (Exception e) {
			logger.error("clearCommodityStylePicNotSPYG 清理非自营商品图片 job 异常: ", e);
			return "error";
		}
		return "success";
	}

	/**
	 * 定时扫描分销商自动代理所有上架商品
	 * 
	 * @return
	 */
	@RequestMapping("sellerAutoProxyAll")
	@ResponseBody
	private String sellerAutoProxyAll() {

		try {
			logger.info("sellerAutoProxyAll 分销商自动代理 job start======================================");
			int i = sellerInfoBackgroundApi.sellerAutoProxyAll();
			logger.info("分销商自动代理上架商品共增加" + i + "条记录！");
			logger.info("sellerAutoProxyAll 分销商自动代理 job end========================================");
		} catch (Exception e) {
			logger.error("sellerAutoProxyAll 分销商自动代理 job 异常: ", e);
			return "error";
		}
		return "success";
	}
	
	/**
	 * 定时更新皮质数据
	 * 
	 * @return
	 */
	@RequestMapping("updateCommodityCortex")
	@ResponseBody
	private String updateCommodityCortex() {
		try {
			logger.info("updateCommodityCortex 定时更新皮质数据 job start======================================");
			int i = commodityCortexBackgroundApi.updateCommodityCortex();
			logger.info("分销商自动代理上架商品共增加" + i + "条记录！");
			logger.info("updateCommodityCortex 定时更新皮质数据 job end========================================");
		} catch (Exception e) {
			logger.error("updateCommodityCortex 定时更新皮质数据job 异常: ", e);
			return "error";
		}
		return "success";
		
	}
	
}
