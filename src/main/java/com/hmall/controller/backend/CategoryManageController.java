package com.hmall.controller.backend;

import com.hmall.common.Const;
import com.hmall.common.ServiceResponse;
import com.hmall.pojo.User;
import com.hmall.service.ICategoryService;
import com.hmall.service.IUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by @Author tachai on 2017/8/26.
 *
 * @Email 1206966083@qq.com
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "add_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse addCategory(HttpSession session, String categoryName, @RequestParam(value ="parentId",defaultValue = "0") int parentId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        //校验一下是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员
            //增加我们处理分类的逻辑
            return iCategoryService.addCategory(categoryName,parentId);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
    @RequestMapping(value = "set_category_name.do")
    @ResponseBody
    public ServiceResponse setCategoryName(HttpSession session ,String categoryName,Integer categoryId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        //校验一下是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
    //获得平级
    @RequestMapping(value = "get_category.do")
    @ResponseBody
    public ServiceResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value ="categoryId",defaultValue = "0") Integer categoryId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        //校验一下是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员
            //查询字节点的信息不递归
            return iCategoryService.getChildrenParalletCategory(categoryId);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
    //递归查询字节点的id
    @RequestMapping(value = "get_deep_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value ="categoryId",defaultValue = "0") Integer categoryId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        //校验一下是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员
            //查询字节点递归
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

}
