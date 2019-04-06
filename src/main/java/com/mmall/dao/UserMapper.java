package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    int checkEmail(String email);

    // 注解 声明之后 就使用注解里的名称 不使用注解 那么xml里 #{0}代表第一个参数 不利于维护
    User selectLogin(@Param("username") String username, @Param("password") String password);

    String selectQuestionByUsername(String username);

    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

    // @Param(value = "password") == @Param("password")
    int checkPassword(@Param(value = "password") String password, @Param("userId") Integer userId);

    int checkEmailByUserId(@Param("email") String email, @Param("userId") Integer userId);
}