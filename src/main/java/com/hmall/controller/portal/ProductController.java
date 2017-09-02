package com.hmall.controller.portal;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hmall.common.ResponseCode;
import com.hmall.common.ServiceResponse;
import com.hmall.dao.CategoryMapper;
import com.hmall.pojo.Category;
import com.hmall.pojo.PayInfo;
import com.hmall.service.ICategoryService;
import com.hmall.service.IProductService;
import com.hmall.vo.ProductDetailVo;
import com.hmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("detail.do")
    @ResponseBody
    public ServiceResponse<ProductDetailVo> detail(Integer productId){
        return iProductService.getProductDetail(productId);
    }

    public ServiceResponse<PageInfo> list(@RequestParam(value = "keyword",required = false)String keyword,@RequestParam(value = "categoryId" ,required = false)Integer categoryId,@RequestParam(value = "pageNum" ,defaultValue = "1")int pageNum,@RequestParam(value = "pageNum" ,defaultValue = "10")int pageSize,@RequestParam(value = "orderBy" ,defaultValue = "")String orderBy){
    return iProductService.getProductBykeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }

}
