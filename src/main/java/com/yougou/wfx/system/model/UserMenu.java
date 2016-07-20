package com.yougou.wfx.system.model;

import java.util.List;

import com.google.common.collect.Lists;

public class UserMenu extends BaseModel implements Comparable<UserMenu> {
	private static final long serialVersionUID = 3130080704579057044L;
	private String structure;
	private String menuName;
	private String menuUrl;
	private int sort;

	private List<UserMenu> kids = Lists.newArrayList();

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public List<UserMenu> getKids() {
		return kids;
	}

	public void setKids(List<UserMenu> kids) {
		this.kids = kids;
	}

	@Override
	public String toString() {
		return "UserMenu [structure=" + structure + ", menuName=" + menuName + ", menuUrl=" + menuUrl + ", sort="
				+ sort + ", kids=" + kids + "]";
	}

	@Override
	public int compareTo(UserMenu obj) {
		if (this.sort > obj.sort) {
			return 1;
		} else {
			return 0;
		}
	}

}
