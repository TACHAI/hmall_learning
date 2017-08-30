package com.hmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hmall.common.ServiceResponse;
import com.hmall.dao.CategoryMapper;
import com.hmall.pojo.Category;
import com.hmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by @Author tachai on 2017/8/26.
 *
 * @Email 1206966083@qq.com
 */
@Service("iCategoryService")
public class CateServiceImpl implements ICategoryService {
    private org.slf4j.Logger logger= LoggerFactory.getLogger(CateServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;
    public ServiceResponse addCategory(String categoryName,Integer parentId){
        if(parentId==null|| StringUtils.isBlank(categoryName)){
            return ServiceResponse.createByErrorMessage("添加参数错误");
        }
        Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//这个分类是可用的
        int rowCount=categoryMapper.insert(category);
        if(rowCount>0){
            return ServiceResponse.createBySuccessMessage("添加商品成功");
        }
        return ServiceResponse.createByErrorMessage("添加品类失败");
    }
    public  ServiceResponse updateCategoryName(Integer categoryId,String categoryName){
        if(categoryId==null|| StringUtils.isBlank(categoryName)){
            return ServiceResponse.createByErrorMessage("跟新品类的参数错误");
        }
        Category category=new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        //有选择性的跟新，即根据category里面有的值来跟新
        int rowCount=categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount>0){
            return ServiceResponse.createBySuccessMessage("跟新品类名字成功");
        }
        return ServiceResponse.createByErrorMessage("跟新品类名字失败");
    }
    public ServiceResponse<List<Category>> getChildrenParalletCategory(Integer categoryId){
        List<Category> categorylist= categoryMapper.selectCategoryChildrenParentId(categoryId);
        if(CollectionUtils.isEmpty(categorylist)){
            logger.info("未找到当前分类id");
        }
        return ServiceResponse.createBySuccess(categorylist);
    }

    /**
     * 递归查询本节点的id及孩子节点的id
     * @param categoryId
     * @return
     */
    public ServiceResponse selectCategoryAndChildrenById(Integer categoryId){
        Set<Category> categorySet= Sets.newHashSet();
        findchildCategory(categorySet,categoryId);
        List<Integer> categoryIdList= Lists.newArrayList();
        if(categoryId!=null){
            for(Category categoryItem:categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServiceResponse.createBySuccess(categoryIdList);
    }
    //递归算法，算出子节点
    private Set<Category> findchildCategory(Set<Category> categorySet,Integer categoryId){
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categorySet.add(category);
        }
        //查找子节点
        //在mybatis里面如果没有返回，不会返回null，如果是其他的就会返回null，要做空判断否者会在用的时候报空指针
        List<Category> categoryList=categoryMapper.selectCategoryChildrenParentId(categoryId);
        //跳出条件是循环结束的时候即categoryList遍历完
        for(Category categoryItem:categoryList){
            findchildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }
}
