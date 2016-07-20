package com.yougou.wfx.manage.commodity.vo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.yougou.wfx.framework.base.BaseVo;


public class BagPageVo extends BaseVo{

	private static final long serialVersionUID = 1L;
	
	//分销包id
	private String id;

	// 分销包名称
	private String bagName;
	
	//分销包小图
	private String bagSmallPic;
	
	//分销包大图
	private String bagBigPic;
	
	//分销包说明
	private String comments;
	
	//创建时间
	private Date createTime;
	
	//最后更新人
	private String updateUser;

	//最后更新时间
	private Date updateTime;
	
	// 状态
	private Integer status;
	
	//排序
	private Integer sortNo;
	
	// 开始时间
	private String startTime;
	
	// 结束时间
	private String endTime;
	
	// 关联的商品编号列表(以“,”分隔)
	private String commodityIds;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getBagName() {
		return bagName;
	}

	public void setBagName(String bagName) {
		this.bagName = bagName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getBagSmallPic() {
		return bagSmallPic;
	}

	public void setBagSmallPic(String bagSmallPic) {
		this.bagSmallPic = bagSmallPic;
	}

	public String getBagBigPic() {
		return bagBigPic;
	}

	public void setBagBigPic(String bagBigPic) {
		this.bagBigPic = bagBigPic;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the commodityIds
	 */
	public String getCommodityIds() {
		return commodityIds;
	}

	/**
	 * @param commodityIds the commodityIds to set
	 */
	public void setCommodityIds(String commodityIds) {
		this.commodityIds = commodityIds;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}