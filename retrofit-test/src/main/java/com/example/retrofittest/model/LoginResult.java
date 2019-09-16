package com.example.retrofittest.model;

/**
 * 登录响应类
 */
public class LoginResult {

    /**
     * 名字
     */
    private String name;

    /**
     * 性别
     */
    private char sex;

    /**
     * 年龄
     */
    private int age;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "loginResult = /n"
        + "name = " + name + " ,/n"
        + "sex = " + sex + " ,/n"
        + "age = " + age + "}"  ;
    }
}
