package com.hmall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.hmall.common.Const;
import com.hmall.common.ResponseCode;
import com.hmall.common.ServiceResponse;
import com.hmall.dao.CategoryMapper;
import com.hmall.dao.ProductMapper;
import com.hmall.pojo.Category;
import com.hmall.pojo.Product;
import com.hmall.service.ICategoryService;
import com.hmall.service.IProductService;
import com.hmall.util.DateTimeUtil;
import com.hmall.util.PropertiesUtil;
import com.hmall.vo.ProductDetailVo;
import com.hmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by @Author tachai on 2017/8/28.
 *
 * @Email 1206966083@qq.com
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ICategoryService iCategoryService;

    public ServiceResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }
            if (product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return ServiceResponse.createBySuccess("更新产品成功");

                }
            } else {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return ServiceResponse.createBySuccess("添加产品成功");
                }
                return ServiceResponse.createByErrorMessage("新增产品失败");
            }
        }
        return ServiceResponse.createByErrorMessage("新增或更新产品失败");
    }
    public ServiceResponse<String> setSaleStatus(Integer productId,Integer status){
        if(productId==null||status==null){
           return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product=new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount=productMapper.updateByPrimaryKeySelective(product);
        if(rowCount>0){
            return ServiceResponse.createBySuccess("修改产品销售状态成功");
        }
        return ServiceResponse.createByErrorMessage("修改产品销售状态失败");
    }

    public ServiceResponse<Object> manageProductDetail(Integer productId){
        if(productId==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product=new Product();
        if(product==null){
            return ServiceResponse.createByErrorMessage("产品已下架");
        }
        //vo对象-value object
        ProductDetailVo productDetailVo=assembleProductDetailVo(product);
        return ServiceResponse.createBySuccess(productDetailVo);
        //pojo->bo->vo(view object)
    }
    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo=new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        //todo
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://image.laishishui.cn/"));

        Category category= categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category==null){
            productDetailVo.setParentCategoryId(0);
        }else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
//        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
//        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    public ServiceResponse<PageInfo> getProductList(int pageNum,int pageSize){
        //startPage-start
        //填充自己的sql查询逻辑
        //pageHelper-收尾
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList=productMapper.selectList();
        List<ProductListVo> productListVoList= Lists.newArrayList();
        for (Product productItem:productList){
            ProductListVo productListVo=assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult=new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServiceResponse.createBySuccess(pageResult);
    }

    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo=new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        //todo
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://image.laishishui.cn/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }
    public ServiceResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(productName)){
            productName=new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList=productMapper.selectByNameAndProductId(productName,productId);
        List<ProductListVo> productListVoList= Lists.newArrayList();
        for (Product productItem:productList){
            ProductListVo productListVo=assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult=new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServiceResponse.createBySuccess(pageResult);
    }

    public ServiceResponse<ProductDetailVo> getProductDetail(Integer productId){
        if(productId==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product=productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServiceResponse.createByErrorMessage("产品已下架");
        }
        if(product.getStatus()!= Const.ProductStatusEnum.ON_SALE.getCode()){
            return ServiceResponse.createByErrorMessage("产品已下架或被删除");
        }
        ProductDetailVo productDetailVo=assembleProductDetailVo(product);
        return ServiceResponse.createBySuccess(productDetailVo);
    }
    public ServiceResponse<PageInfo> getProductBykeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy){
        if(StringUtils.isBlank(keyword)&&categoryId==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList=new ArrayList<Integer>();
        if(categoryId!=null){
            Category category=categoryMapper.selectByPrimaryKey(categoryId);
            if(category==null&&StringUtils.isBlank(keyword)){
                //没有该分类，并且还没有关键字，这个时候返回一个空的结果集，不报错
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> productListVoList=new ArrayList<>();
                PageInfo pageInfo=new PageInfo(productListVoList);
                return ServiceResponse.createBySuccess(pageInfo);
            }
            categoryIdList=iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }
        if(StringUtils.isNotBlank(keyword)){
            keyword=new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum,pageSize);
        //排序处理
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.ProductListOrderBy.PRICE_ASE_DESC.contains(orderBy)){
                String[] orderByArray=orderBy.split("_");
                PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
            }
        }
        List<Product> productList=productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);
        List<ProductListVo> productListVoList=Lists.newArrayList();
        for(Product product:productList){
            ProductListVo productListVo=assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo=new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServiceResponse.createBySuccess(pageInfo);
    }
}
