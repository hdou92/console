package com.example.retrofittest.Service;

import com.example.retrofittest.model.LoginInfo;
import com.example.retrofittest.model.LoginResult;
import org.springframework.stereotype.Service;


/**
 *
 */
@Service
public interface UserLoginService {

    /**
     * 登录方法
     * @param loginInfo
     * @return
     */
    LoginResult login(LoginInfo loginInfo);




}
