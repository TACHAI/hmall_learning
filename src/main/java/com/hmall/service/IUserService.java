package com.hmall.service;

import com.hmall.common.ServiceResponse;
import com.hmall.pojo.User;

/**
 * Created by asus30 on 2017/8/20.
 */
public interface IUserService {
    ServiceResponse<User> login(String username, String password);
    ServiceResponse<String> register(User user);
    ServiceResponse<String> checkValid(String str,String type);
    ServiceResponse selectQuestion(String username);
    ServiceResponse<String> checkAnswer(String username,String  question,String answer);
}
