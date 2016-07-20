package com.yougou.wfx.proxy.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yougou.pc.api.ICommodityBaseApiService;
import com.yougou.pc.model.commodityinfo.Commodity;
import com.yougou.pc.model.product.Product;
import com.yougou.wfx.framework.cache.CacheCable;
import com.yougou.wfx.proxy.ICommodityApiProxy;

@Service
public class CommodityApiProxyImpl implements ICommodityApiProxy{

	@Autowired
	ICommodityBaseApiService commodityBaseApiService;
	
	@Override
	@CacheCable(key="'member:admin:commodity:no:'+#commodityNo" ,value="member-admin" ,expiration=30*60,localExpiration=20*60)
	public Commodity getCommodityByNo(String commodityNo) {
		Commodity commodity = commodityBaseApiService.getCommodityByNo(commodityNo);
		return commodity;
	}

	@Override
	@CacheCable(key="'member:admin:commodity:products:'+#commodityNo" ,value="member-admin" ,expiration=30*60,localExpiration=20*60)
	public List<Product> getProductListByCommodityNo(String commodityNo) {
		return commodityBaseApiService.getProductListByCommodityNo(commodityNo);
	}
}
