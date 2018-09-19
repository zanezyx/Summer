package com.zyx.jshop.controller;

import com.zyx.jshop.entity.IndexResult;
import com.zyx.jshop.entity.JsonResult;
import com.zyx.jshop.entity.Product;
import com.zyx.jshop.entity.SettingInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class InfoController {


    @RequestMapping(value = "settings", method = RequestMethod.GET)
    public ResponseEntity<JsonResult> getSettingInfo (){
        JsonResult r = new JsonResult();
        SettingInfo si = new SettingInfo();
        si.setLatestVersionCode("1.0");
        si.setLatestVersionURL("");
        si.setServiceMobile("13603021940");
        r.setStatus("ok");
        r.setResult(si);
        return ResponseEntity.ok(r);
    }

}