package com.zpk.console.web.controller;

import com.zpk.console.model.file.LoginResult;
import com.zpk.console.model.file.RestResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TestController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object getFileName(@PathVariable("id") String id, HttpServletRequest request) {
        return id;
    }

    @RequestMapping(value = "/login" , method = RequestMethod.GET)
    public RestResult<LoginResult> login(@RequestParam String account , @RequestParam String password){

        if (!("admin".equals(account) && "123456".equals(password)))
            return null;

        LoginResult loginResult = new LoginResult();
        loginResult.setName("无名");
        loginResult.setAge("18");
        loginResult.setSex("男");
        RestResult<LoginResult> restResult = new RestResult<>();
        restResult.setCode("200");
        restResult.setMessage("success");
        restResult.setData(loginResult);
        return restResult;
    }
}
