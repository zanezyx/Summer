package com.zyx.jshop.controller;



import com.zyx.jshop.entity.IndexResult;
import com.zyx.jshop.entity.JsonResult;
import com.zyx.jshop.entity.Product;
import com.zyx.jshop.entity.User;
import com.zyx.jshop.service.ProductService;
import com.zyx.jshop.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ProductController {

    @Autowired
    private ProductService productService;


    @RequestMapping(value = "index", method = RequestMethod.GET)
    public ResponseEntity<IndexResult> getIndexProducts (){
        IndexResult r = new IndexResult();
        try {
            List<Product> hotSales = productService.getProductOfHotSales();
            r.setHotSales(hotSales);
            List<Product> news = productService.getProductOfNews();
            r.setNewList(news);
        } catch (Exception e) {
            r.setHotSales(null);
            r.setNewList(null);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


}