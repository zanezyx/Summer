package com.zyx.jshop.mapper;


import com.zyx.jshop.entity.Product;
import com.zyx.jshop.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

// @Mapper 这里可以使用@Mapper注解，但是每个mapper都加注解比较麻烦，所以统一配置@MapperScan在扫描路径在application类中
public interface ProductMapper {

    @Select("SELECT * FROM product WHERE id = #{id}")
    public Product getProductById(Integer id);


    @Select("SELECT * FROM product")
    public List<Product> getProductList();

    @Select("SELECT * FROM product WHERE isNew>0")
    public List<Product> getProductOfNews();

    @Select("SELECT * FROM product WHERE hotSales>0")
    public List<Product> getProductOfHotSales();


    @Select("select * from product where metaKeywords like '%${metaKeywords}%'")
    public List<Product> searchProducts(@Param("metaKeywords") String metaKeywords);

}