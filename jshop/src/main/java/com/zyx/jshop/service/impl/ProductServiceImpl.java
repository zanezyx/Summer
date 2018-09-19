package com.zyx.jshop.service.impl;


import com.zyx.jshop.entity.Product;
import com.zyx.jshop.entity.User;
import com.zyx.jshop.mapper.ProductMapper;
import com.zyx.jshop.mapper.UserMapper;
import com.zyx.jshop.service.ProductService;
import com.zyx.jshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName cn.saytime.service.impl.UserServiceImpl
 * @Description
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;


    @Override
    public List<Product> getProductOfNews() {
        return productMapper.getProductOfNews();
    }

    @Override
    public List<Product> getProductOfHotSales() {
        return productMapper.getProductOfHotSales();
    }

    @Override
    public Product getProductDetail(int id) {
        return productMapper.getProductById(id);
    }

    @Override
    public List<Product> searchProducts(String metaKeywords) {
        return productMapper.searchProducts(metaKeywords);
    }


}