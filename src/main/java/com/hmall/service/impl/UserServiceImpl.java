package com.hmall.service.impl;

import com.hmall.common.Const;
import com.hmall.common.ServiceResponse;
import com.hmall.common.TokenCache;
import com.hmall.dao.UserMapper;
import com.hmall.pojo.User;
import com.hmall.service.IUserService;
import com.hmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by asus30 on 2017/8/20.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public ServiceResponse<User> login(String username, String password) {
        int resultCount= userMapper.checkUsername(username);
        if(resultCount==0){
            return ServiceResponse.createByErrorMessage("用户名不存在");
        }

        String md5Passswrd=MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,md5Passswrd);
        if(user==null){
            return ServiceResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess(user,"登录成功");
    }
    public  ServiceResponse<String> register(User user){
        ServiceResponse validResponse=this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return  validResponse;
        }
        validResponse=this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return  validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount=userMapper.insert(user);
        if (resultCount==0){
            return ServiceResponse.createByErrorMessage("注册失败");
        }
        return ServiceResponse.createBySuccess("注册成功");
    }
    public ServiceResponse<String> checkValid(String str,String type){
        if(StringUtils.isNoneBlank(type)){
            if(Const.USERNAME.equals(type)){
                int resultCount=userMapper.checkUsername(str);
                if (resultCount>0){
                    return  ServiceResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)){
               int resultCount=userMapper.checkEmail(str);
                if(resultCount>0){

                    return ServiceResponse.createByErrorMessage("email已存在");
                    }
            }
        }else {
            return ServiceResponse.createByErrorMessage("参数错误");
        }
        return ServiceResponse.createBySuccess("教验成功");
    }
    public ServiceResponse selectQuestion(String username){
        ServiceResponse validResponse=this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServiceResponse.createByErrorMessage("用户不存在");
        }
        String question=userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNoneBlank(question)){
            return ServiceResponse.createBySuccess(question);
        }
        return ServiceResponse.createByErrorMessage("返回值是空的");
    }
    public  ServiceResponse<String> checkAnswer(String username,String  question,String answer){
        int resultCount =userMapper.checkAnswer(username,question,answer);
        if(resultCount>0){
            String forgetToen= UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToen);
            return ServiceResponse.createBySuccess(forgetToen);
        }
        return ServiceResponse.createByErrorMessage("答案不正确");
    }
    public ServiceResponse<String> forgetRestPassword(String username,String passwordNew, String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServiceResponse.createByErrorMessage("参数错误,token需要传递");
        }
        ServiceResponse validResponse=this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServiceResponse.createByErrorMessage("用户不存在");
        }
        String token=TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServiceResponse.createByErrorMessage("token无效或者过期");
        }
        if(StringUtils.equals(forgetToken,token)){
            String md5Password =MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount=userMapper.updatePasswordByUsername(username, md5Password);
            if(rowCount>0){
                return ServiceResponse.createBySuccessMessage("修改成功");
            }
        }else {
            return ServiceResponse.createByErrorMessage("请重新获取重置密码的token");
        }
        return ServiceResponse.createByErrorMessage("修改密码失败");
    }
    public ServiceResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        //防止横向越权，要教验一下这个用户的旧密码，一定是这个用户，因为我们会查询一个count（1），如果不指定id,那么结果就是true
        int resultCount=userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount==0){
            return ServiceResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount=userMapper.updateByPrimaryKeySelective(user);
        if(updateCount>0){
            return ServiceResponse.createBySuccessMessage("密码跟新成功");
        }
        return ServiceResponse.createByErrorMessage("密码跟新失败");
    }
    public ServiceResponse<User> updateInformation(User user){
        //用户名不能被跟新
        //email也要进行一个教验，教验新的email是否存在，并且email如果相同的话，不能是我们当前的这个用户的。
        int resultCount =userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount>0){
            return ServiceResponse.createByErrorMessage("email已经被使用，请更换email");
        }
        User updateUser=new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        //updateByPrimaryKey只跟新不为空的字段
        int updateCount=userMapper.updateByPrimaryKey(updateUser);
        if(updateCount>0){
            return ServiceResponse.createBySuccess(updateUser,"更新个人信息成功");
        }
        return ServiceResponse.createByErrorMessage("更新个人信息失败");
    }
    public ServiceResponse<User> getInformation(Integer userId){
        User user=userMapper.selectByPrimaryKey(userId);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(10,"找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess(user);
    }
    //backend
    public ServiceResponse checkAdminRole(User user){
        if(user!=null&&user.getRole().intValue()==Const.Role.ROLE_ADMIN){
            return  ServiceResponse.createBySuccess();
        }
        return ServiceResponse.createByError();
    }


}
