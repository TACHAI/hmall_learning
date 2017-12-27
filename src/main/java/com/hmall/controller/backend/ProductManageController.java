package com.hmall.controller.backend;

import com.google.common.collect.Maps;
import com.hmall.common.Const;
import com.hmall.common.ResponseCode;
import com.hmall.common.ServiceResponse;
import com.hmall.pojo.Product;
import com.hmall.pojo.User;
import com.hmall.service.IFileService;
import com.hmall.service.IProductService;
import com.hmall.service.IUserService;
import com.hmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by @Author tachai on 2017/8/28.
 *
 * @Email 1206966083@qq.com
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    @RequestMapping("save.do")
    @ResponseBody
    public ServiceResponse productSave(HttpSession session, Product product){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.saveOrUpdateProduct(product);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
        //return ServiceResponse.createByErrorMessage("商品保存失败");
    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServiceResponse setSaleStatus(HttpSession session, Integer productId,Integer status){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.setSaleStatus(productId,status);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }


    @RequestMapping("detail.do")
    @ResponseBody
    public ServiceResponse getdetail(HttpSession session, Integer productId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充业务
            //获取产品详情
            return iProductService.manageProductDetail(productId);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
    @RequestMapping("list.do")
    @ResponseBody
    public ServiceResponse getList(HttpSession session, @RequestParam(value="pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充业务
            //获取产品详情
            return iProductService.getProductList(pageNum,pageSize);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
    @RequestMapping("search.do")
    @ResponseBody
    public ServiceResponse productSearch(HttpSession session, String productName,Integer productId,@RequestParam(value="pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充业务
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    //文件上传
    @RequestMapping("upload.do")
    @ResponseBody
    public ServiceResponse upload(HttpSession session,@RequestParam(value ="upload_file",required = false) MultipartFile file, HttpServletRequest request){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充业务
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
            //todo PropertiesUtil.getProperty
            String url= PropertiesUtil.getProperty("")+targetFileName;
            Map fileMap= Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServiceResponse.createBySuccess(fileMap);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
    //富文本上传   是使用的simditor插件所以按照simditor的要求进行返回
    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpLoad(HttpSession session, @RequestParam(value ="upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap=Maps.newHashMap();
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }
        //富文本中对自己的返回值有自己的要求
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充业务
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url=PropertiesUtil.getProperty("")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            //前端要求
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        }else {
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }
    }

}
