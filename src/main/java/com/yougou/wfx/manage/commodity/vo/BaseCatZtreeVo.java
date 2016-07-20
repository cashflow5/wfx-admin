package com.yougou.wfx.manage.commodity.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.yougou.wfx.framework.base.BaseVo;

public class BaseCatZtreeVo extends BaseVo{
	
	private String id;
	
	private String pId;
	
	private String name;
	
	private boolean checked;
	
	private boolean open;
	
	private Integer level;
	
	private String title;

	public String getId() {
		return id; 
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getPId() {
		return pId;
	}

	public void setPId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
}
