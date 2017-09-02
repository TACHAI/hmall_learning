package com.hmall.service;

import com.hmall.common.ServiceResponse;
import com.hmall.pojo.Category;

import java.util.List;

/**
 * Created by @Author tachai on 2017/8/26.
 *
 * @Email 1206966083@qq.com
 */
public interface ICategoryService {
    ServiceResponse addCategory(String categoryName, Integer parentId);
    ServiceResponse updateCategoryName(Integer categoryId,String categoryName);
    ServiceResponse<List<Category>> getChildrenParalletCategory(Integer categoryId);
    ServiceResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
