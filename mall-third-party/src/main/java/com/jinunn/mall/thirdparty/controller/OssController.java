package com.jinunn.mall.thirdparty.controller;

import com.jinunn.common.utils.R;
import com.jinunn.mall.thirdparty.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author : JinDun
 * @date : 2021/6/6 1:24
 */
@RestController
public class OssController {

    @Autowired
    private OssService ossService;

    @GetMapping("oss/policy")
    public R getPolicy(){
        Map<String,String> data =ossService.getPolicy();
        return R.ok().put("data",data);
    }
}
