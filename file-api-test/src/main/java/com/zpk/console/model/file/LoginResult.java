package com.zpk.console.model.file;

/**
 * 登录接口结果类
 */
public class LoginResult {

    private String name;

    private String sex;

    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "loginResult = { /n"
        + "name = " + name + " , /n"
        + "sex = " + sex + " , /n"
        + "age = " + age + " }";
    }
}
