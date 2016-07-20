package com.yougou.wfx.proxy;

import java.util.List;

import com.yougou.pc.model.commodityinfo.Commodity;
import com.yougou.pc.model.product.Product;

/**
 * 商品模块代理
 * @author wuyang
 *
 */
public interface ICommodityApiProxy {
	/**
     * 根据商品编号得到商品信息
     * @param commodityNo 商品编号
     * @return Commodity
     */
    Commodity getCommodityByNo(String commodityNo);
    
    /**
     * 根据商品编号，得到货品列表
     *
     * @param commodityNo 商品编号
     * @return List<Product>
     */
    List<Product> getProductListByCommodityNo(String commodityNo);
}
