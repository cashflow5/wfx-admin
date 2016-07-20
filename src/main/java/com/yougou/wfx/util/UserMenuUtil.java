package com.yougou.wfx.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;
import com.yougou.permission.remote.ResourceRemote;
import com.yougou.wfx.system.model.UserMenu;

public class UserMenuUtil {
	
	/**
	 * 抽取子菜单
	 * 
	 * @param data
	 * @return
	 */
	public static List<UserMenu> sortUserMenu(List<ResourceRemote> lstResource) {
		List<UserMenu> fathers = Lists.newArrayList();
		List<UserMenu> kids = Lists.newArrayList();
		for (ResourceRemote item : lstResource) {
			int count = StringUtils.countMatches(item.getStructure(), "-");
			String localUrl = item.getMemu_url();
			UserMenu um = new UserMenu();
			um.setMenuName(item.getMenu_name());
			um.setMenuUrl(localUrl);
			um.setStructure(item.getStructure());
			um.setSort(item.getSort().intValue());
			// 定制
			
			if (count == 2) {
				boolean pass = true;
				for(UserMenu menu : fathers){
					if(StringUtils.equals(menu.getStructure(), um.getStructure())){
						pass = false;
					}
				}
				if(pass){
					fathers.add(um);
				}
			} else {
				if(StringUtils.isNotBlank(localUrl)){
					boolean pass = true;
					for(UserMenu menu : kids){
						if(StringUtils.equals(menu.getStructure(), um.getStructure())){
							pass = false;
						}
					}
					if(pass){
						kids.add(um);
					}
				}
			}
			
		}
		for (UserMenu item : fathers) {
			setKids(item, kids);
		}
		List<UserMenu> delList = Lists.newArrayList();
		for(UserMenu item : fathers){
			if(item.getKids().isEmpty()){
				delList.add(item);
			}
		}
		if(!delList.isEmpty()){
			fathers.removeAll(delList);
		}
		
		List<UserMenu> newLst = Lists.newArrayList(); 
		for(UserMenu um: fathers){
			boolean isExist = false;
			for(UserMenu newUm : newLst){
				if(StringUtils.equals(um.getStructure(), newUm.getStructure())){
					isExist = true;
					break;
				}
			}
			if(!isExist){
				newLst.add(um);
			}
		}
		Collections.sort(newLst,new Comparator<UserMenu>(){
			@Override
			public int compare(UserMenu o1, UserMenu o2) {
				int sort1 = o1.getSort();
				int sort2 = o2.getSort();
				if(sort1 == sort2){
					return 0;
				}else if(sort1 > sort2){
					return 1;
				}else if(sort1 < sort2){
					return -1;
				}
				return 0;
			}
		});
		return newLst;
	}

	/**
	 * 查找次级子菜单
	 * 
	 * @param target
	 * @param all
	 * @return
	 */
	public static void setKids(UserMenu current, List<UserMenu> kids) {
		for (UserMenu item : kids) {
			boolean isKid = isKid(current.getStructure(), item.getStructure());
			if (isKid) {
				boolean isExist = false;
				for(UserMenu o: current.getKids()){
					if(StringUtils.equals(o.getMenuUrl(), item.getMenuUrl())){
						isExist = true;
					}
				}
				if(!isExist){
					current.getKids().add(item);
				}
			}
		}
	}

	/**
	 * 判断是否次级子菜单 current
	 * 
	 * @param current
	 * @param target
	 * @return
	 */
	public static boolean isKid(String father, String kid) {
		if (kid.equals(father)) {
			return false;
		}
		String result = StringUtils.removeStart(kid, father);
		if (!result.equals(kid)) {
			return result.contains("-");
		}
		return false;
	}
}
