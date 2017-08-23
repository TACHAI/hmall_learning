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
        if(StringUtils.isNoneBlank()){
            if(Const.USERNAME.equals(type)){
                int resultCount=userMapper.checkUsername(str);
                if (resultCount>0){
                    return  ServiceResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.CURRENT_USER.equals(type)){
               int resultCount=userMapper.checkEmail(type);
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
            TokenCache.setKey("token_"+username,forgetToen);
            return ServiceResponse.createBySuccess(forgetToen);
        }
        return ServiceResponse.createByErrorMessage("答案不正确");
    }

}
