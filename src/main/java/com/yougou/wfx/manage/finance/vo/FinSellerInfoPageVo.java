 /*
 * 版本信息
 
 * 日期 2016-03-28 10:58:34
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */

package com.yougou.wfx.manage.finance.vo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.yougou.tools.common.utils.DatetimeUtil;
import com.yougou.wfx.framework.base.BaseVo;

/**
 * FinSellerInfoPageVo
 * @author wfx
 * @Date 创建时间：2016-03-28 10:58:34
 */
public class FinSellerInfoPageVo extends BaseVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 微分销商账户总表主键ID
	 */
	private String id;
	/**
	 * 店铺名称
	 */
	private String shopName;
	/**
	 * 店铺编码
	 */
	private String shopCode;
	/**
	 * 分销商账户
	 */
	private String sellerAccount;
	/**
	 * 分销商姓名
	 */
	private String sellerName;
	/**
	 * 注册时间
	 */
	private Date registerTime;
	/**
	 * 最近交易时间
	 */
	private Date latelyTransactionTime;
	/**
	 * 账户余额
	 */
	private Double accountBalance;
	/**
	 * 选中是否余额大于零的标示.
	 */
	private String  amountCheckFlag;

	public FinSellerInfoPageVo(){
	}

	public FinSellerInfoPageVo(
		String id
	){
		this.id = id;
	}

	public void setId(String value) {
		this.id = value;
	}
	
	public String getId() {
		return this.id == null ? null : this.id.trim();
	}
	
	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public void setShopName(String value) {
		this.shopName = value;
	}
	
	public String getShopName() {
		return this.shopName == null ? null : this.shopName.trim();
	}
	public void setSellerAccount(String value) {
		this.sellerAccount = value;
	}
	
	public String getSellerAccount() {
		return this.sellerAccount == null ? null : this.sellerAccount.trim();
	}
	public void setRegisterTime(Date value) {
		this.registerTime = value;
	}
	
	public Date getRegisterTime() {
		return this.registerTime;
	}
	
	public String getStringRegisterTime() {
		if(this.registerTime == null){
		return null;
		}
		return DatetimeUtil.DateToString(this.registerTime, DatetimeUtil.LONG_DATE_TIME_PATTERN);
	}
	public void setLatelyTransactionTime(Date value) {
		this.latelyTransactionTime = value;
	}
	
	public Date getLatelyTransactionTime() {
		return this.latelyTransactionTime;
	}
	
	public String getStringLatelyTransactionTime() {
		if(this.latelyTransactionTime == null){
		return null;
		}
		return DatetimeUtil.DateToString(this.latelyTransactionTime, DatetimeUtil.LONG_DATE_TIME_PATTERN);
	}
	public void setAccountBalance(Double value) {
		this.accountBalance = value;
	}
	
	public Double getAccountBalance() {
		return this.accountBalance;
	}

	/**
	 * @return the amountCheckFlag
	 */
	public String getAmountCheckFlag() {
		return this.amountCheckFlag == null ? null : this.amountCheckFlag.trim();
	}

	/**
	 * @param amountCheckFlag the amountCheckFlag to set
	 */
	public void setAmountCheckFlag(String amountCheckFlag) {
		this.amountCheckFlag = amountCheckFlag;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}

