package com.hmall.controller.portal;

import com.hmall.common.ServiceResponse;
import com.hmall.service.IProductService;
import com.hmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by @Author tachai on 2017/8/30.
 *
 * @Email 1206966083@qq.com
 */
@Controller
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    @RequestMapping("detail.do")
    @ResponseBody
    public ServiceResponse<ProductDetailVo> detail(Integer productId){

    }
}
