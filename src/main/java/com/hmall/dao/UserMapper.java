package com.hmall.dao;

import com.hmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    int checkUsername(String name);
    int checkEmail(String email);
    //用mybatis 传递多个参数的时候，要用@Param ，写sql的时候参数对应@Param里面的值
    User selectLogin(@Param("username") String username,@Param("password")String password);
    String selectQuestionByUsername( String username);
    int checkAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);
    int updatePasswordByUsername(@Param(("username")) String username,@Param("passwordNew") String passwordNew);
    int checkPassword(@Param("password")String password,@Param("userId")Integer userId);
    int checkEmailByUserId(@Param("email") String email,@Param("id") Integer userId);
}