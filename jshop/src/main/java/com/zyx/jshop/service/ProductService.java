package com.zyx.jshop.service;


import com.zyx.jshop.entity.Product;
import com.zyx.jshop.entity.User;

import java.util.List;

/**
 * @ClassName cn.saytime.service.UserService
 * @Description
 */
public interface ProductService {

    List<Product> getProductOfNews();
    List<Product> getProductOfHotSales();
    Product getProductDetail(int id);
    List<Product> searchProducts(String metaKeywords);

}