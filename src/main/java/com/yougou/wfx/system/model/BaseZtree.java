 /*
 * 版本信息
 
 * 日期 2016-03-24 16:06:23
 
 * 版权声明Copyright (C) 2011- 2016 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */
package com.yougou.wfx.system.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.yougou.wfx.dto.base.OutputDto;

/**
 * 实现树形结构的zTree对象
 * 结合前面的功能，可以实现懒加载
 * @author wzf
 * @Date 创建时间：2016-03-24 16:06:23
 */
public class BaseZtree extends OutputDto {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 结点id
	 */
	private String id;
	/**
	 * 父结点id
	 */
	private String pId;
	/**
	 * 结点名称
	 */
	private String name;
	/**
	 * 结点标题
	 */
	private String t;
	/**
	 * 结点的类型（根：root，目录：folder，菜单：menu）
	 */
	private String rtype;
	/**
	 * 是否默认打开
	 */
	private String open;
	/**
	 * 是否默认点击
	 */
	private String click;
	/**
	 * 显示图片名称（根：root，目录：folder，菜单：menu）
	 */
	private String iconSkin;
	/**
	 * url(默认为空)
	 */
	private String url;
	/**
	 * 结点描述
	 */
	private String description;
	/**
	 * 删除标志
	 */
	private String isFlag;
	/**
	 * 是否父结点，用于确定是否显示前面的+
	 */
	private String isParent;
	
	public BaseZtree(){
		
	}
	
	public BaseZtree(String id,String pId,String name,String t,String rtype,
			String open,String click,String iconSkin,String url,String description,
			String isFlag,String isParent){
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.t = t;
		this.rtype = rtype;
		this.open = open;
		this.click = click;
		this.iconSkin = iconSkin;
		this.url = url;
		this.description = description;
		this.isFlag = isFlag;
		this.isParent = isParent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getPId(){
		return pId;
	}
	
	public void setPId(String pId){
		this.pId = pId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public String getRtype() {
		return rtype;
	}

	public void setRtype(String rtype) {
		this.rtype = rtype;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getClick() {
		return click;
	}

	public void setClick(String click) {
		this.click = click;
	}

	public String getIconSkin() {
		return iconSkin;
	}

	public void setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsFlag() {
		return isFlag;
	}

	public void setIsFlag(String isFlag) {
		this.isFlag = isFlag;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

