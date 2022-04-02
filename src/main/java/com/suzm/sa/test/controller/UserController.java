package com.suzm.sa.test.controller;

import cn.dev33.satoken.annotation.*;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user/")
public class UserController {

    // 测试登录，浏览器访问： http://localhost:8081/user/doLogin?username=zhang&password=123456
    @RequestMapping("doLogin")
    public SaResult doLogin(String username, String password, HttpServletRequest request) {
        if(StpUtil.isLogin()){
            return SaResult.ok().setMsg("已经登陆过，请不要重复登陆");
        }
        HttpSession session= request.getSession();
        session.setAttribute("aa",1);

        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if("zhang".equals(username) && "123456".equals(password)) {
            StpUtil.login(10001);

            StpUtil.getSession().set("a","a");

            return SaResult.ok("成功").set("token",StpUtil.getTokenInfo().getTokenValue());
        }
        return SaResult.error();
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    // 登录认证：只有登录之后才能进入该方法
    @SaCheckLogin
    @RequestMapping("info")
    public String info() {
        //StpUtil.getSession();
        System.out.println(StpUtil.getSession());
        System.out.println(StpUtil.getTokenSession());
        System.out.println(StpUtil.getTokenInfo().getTokenName());
        System.out.println(StpUtil.getTokenInfo().getTokenValue());
        System.out.println(StpUtil.getRoleList());
        return "查询用户信息";
    }

    // 角色认证：必须具有指定角色才能进入该方法
    @SaCheckRole("super-admin")
    @RequestMapping("add")
    public String add() {
        return "用户增加";
    }

    // 权限认证：必须具有指定权限才能进入该方法
    @SaCheckPermission("user-add")
    @RequestMapping("add1")
    public String add1() {
        return "用户增加1";
    }

    // 二级认证：必须二级认证之后才能进入该方法
    @SaCheckSafe()
    @RequestMapping("add2")
    public String add2() {
        return "用户增加2";
    }

    // Http Basic 认证：只有通过 Basic 认证后才能进入该方法
    @SaCheckBasic(account = "sa:123456")
    @RequestMapping("add3")
    public String add3() {
        return "用户增加3";
    }

}